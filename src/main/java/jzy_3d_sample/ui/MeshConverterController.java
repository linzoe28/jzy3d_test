/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jzy_3d_sample.ui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import jzy_3d_sample.datafactory.FastN2fWriter;
import jzy_3d_sample.datafactory.OSFileParser;
import jzy_3d_sample.datafactory.Read_data;
import jzy_3d_sample.datafactory.SurfaceLoader;
import jzy_3d_sample.model.Cube;
import jzy_3d_sample.model.Mesh;
import jzy_3d_sample.model.os.OSRecordMap;
import jzy_3d_sample.model.serialized.CurrentData;
import jzy_3d_sample.model.serialized.ProjectModel;
import jzy_3d_sample.ui.BackgroundRunner.ProgressReporter;
import jzy_3d_sample.utils.OSFileSplitter;
import jzy_3d_sample.utils.SerializeUtil;
import org.apache.commons.io.FileUtils;
import org.jzy3d.plot3d.primitives.Shape;
import rocks.imsofa.n2fproxy.N2fExecutor;
import rocks.imsofa.n2fproxy.N2fExecutorEvent;
import rocks.imsofa.n2fproxy.N2fExecutorImpl;
import rocks.imsofa.n2fproxy.N2fExecutorListener;

/**
 *
 * @author lendle
 */
public class MeshConverterController {

    private File nasFile = null;
    private File osFile = null;
    private boolean ok = false;
    private FileChooser fileChooser = new FileChooser();
    @FXML
    private TextField outputFolderText;

    @FXML
    private TextField nasFileText;

    @FXML
    private TextField osFileText;

    @FXML
    private TextField x_value;

    @FXML
    private TextField y_value;

    @FXML
    private TextField z_value;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label textStatus;

    @FXML
    private CheckBox checkboxn2f;

    @FXML
    void buttonCancelClicked(ActionEvent event) {

    }

    @FXML
    void buttonNasClicked(ActionEvent event) {
        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Nas Files", "*.nas"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        nasFile = fileChooser.showOpenDialog(nasFileText.getScene().getWindow());
        if (nasFile != null) {
            fileChooser.setInitialDirectory(nasFile.getParentFile());
        }
        nasFileText.setText((nasFile != null) ? nasFile.getAbsolutePath() : "");
    }

    @FXML
    void buttonOkClicked(ActionEvent event) {
        BackgroundRunner r = new BackgroundRunner(new ProgressReporter() {
            public void startProgress() {
            }

            public void stopProgress() {
            }

            public void setStatus(String status) {
            }
        }) {
            public void runBeforeWorkerThread() {
                progressBar.setVisible(true);
            }

            public void runInWorkerThread() {
                try {
                    ProjectModel projectModel = new ProjectModel();
                    File bigOsFile = new File(osFileText.getText());
                    File nasFile = new File(nasFileText.getText());
                    setStatusMessage("splitting os files");
                    File outputDir = new File(outputFolderText.getText());
                    if (outputDir.exists()) {
                        FileUtils.deleteDirectory(outputDir);
                    }
                    FileUtils.forceMkdir(outputDir);
                    File outputOsFolder = new File(bigOsFile.getParentFile(), ".os");
                    List<File> osFiles=null;
                    if (!outputOsFolder.exists()) {
                        outputOsFolder.mkdirs();
                        osFiles = OSFileSplitter.splitByAngle(bigOsFile, new OSFileSplitter.OutputFileCreator() {
                            public File createFile(File osFile, long index) {
                                return new File(outputOsFolder, "" + index + ".os");
                            }
                        }, null);
                    }else if(outputOsFolder.listFiles().length>0){
                        osFiles=Arrays.asList(outputOsFolder.listFiles());
                    }
                    Read_data r = new Read_data(outputDir, "angle0");
                    long x = Long.valueOf(x_value.getText());
                    long y = Long.valueOf(y_value.getText());
                    long z = Long.valueOf(z_value.getText());
                    int index = 0;

                    //first, output the no-os model
                    File firstOsFile = osFiles.get(0);
                    setStatusMessage("processing mesh");
                    List<Mesh> meshes = r.getdata_from_nas(nasFile, firstOsFile);
                    setStatusMessage("done loading mesh definition");
                    {
                        Shape surface = SurfaceLoader.loadSurface(meshes);
                        setStatusMessage("done loading surface");
                        Cube boundingCube = new Cube(surface.getBounds(), meshes);
                        List<Cube> cubes = boundingCube.slice(x, y, z);
                        projectModel.setxSlice(x);
                        projectModel.setySlice(y);
                        projectModel.setzSlice(z);
                        projectModel.setCubes(cubes);
                    }
                    ///////////////////////////////
                    List<File> n2fProcessingQueue = new ArrayList<>();
                    index = 0;//angle 0 was already processed when loading meshes
                    for (int n = 0; n < osFiles.size(); n++) {
                        File osFile = osFiles.get(n);
                        setStatusMessage("slicing for angle " + (index + 1) + "/" + osFiles.size());
                        File subOutputFolder = new File(outputDir, "angle" + (index));
                        if (subOutputFolder.exists()) {
                            FileUtils.deleteDirectory(subOutputFolder);
                        }
                        FileUtils.forceMkdir(subOutputFolder);

                        //re-apply different os records to the original mesh
                        OSRecordMap osRecords = null;
                        if (n == 0) {
                            osRecords = r.getLastOSRecordMap();
                        } else {
                            osRecords = OSFileParser.readOSFile(outputDir, osFile, "angle" + (index));
                            r.applyOStoMeshes(meshes, osRecords);
                        }
                        Shape surface = SurfaceLoader.loadSurface(meshes);
                        Cube boundingCube = new Cube(surface.getBounds(), meshes);
                        List<Cube> cubes = boundingCube.slice(x, y, z);
                        for (int i = 0; i < cubes.size(); i++) {
                            Cube c = cubes.get(i);
                            File subCubeDir = new File(subOutputFolder, "" + i);
                            if (subCubeDir.exists()) {
                                FileUtils.deleteDirectory(subCubeDir);
                            }
                            FileUtils.forceMkdir(subCubeDir);
                            FastN2fWriter.writeTriFile(c.getMeshs(), new File(subCubeDir, i + ".tri"));
                            FastN2fWriter.writeCurMFile(c.getMeshs(), new File(subCubeDir, i + ".curM"));
                            FastN2fWriter.writeCurJFile(c.getMeshs(), new File(subCubeDir, i + ".curJ"));
                        }
                        n2fProcessingQueue.add(subOutputFolder);

                        //TODO: separate n2f process from slicing process
                        CurrentData currentData = new CurrentData();
                        currentData.setOsRecordsMap(osRecords);
                        File currentObjFile = new File(outputDir, "angle" + index + ".current");
                        SerializeUtil.writeToFile(currentData, currentObjFile);
                        projectModel.getCurrentDataList(true).add("angle" + index);
                        index++;

                        //FileUtils.forceDelete(osFile);
                        System.out.println("hit: " + osRecords.getHit() + ", miss: " + osRecords.getMiss());
                    }
                    index = 0;
                    projectModel.setHomeFolder(outputDir);
                    if (checkboxn2f.isSelected()) {
                        for (File n2fFolder : n2fProcessingQueue) {
                            setStatusMessage("processing n2f for angle " + (index + 1) + "/" + osFiles.size());
                            N2fExecutor executor = new N2fExecutorImpl(new File("n2ftools"));

                            executor.addN2fExecutorListener(new N2fExecutorListener() {
                                public void statusUpdated(N2fExecutorEvent e) {
                                    setStatusMessage(e.getMessage());
                                }
                            });

                            executor.init(n2fFolder);

                            //TODO: update embedded n2f tools to recognize theta and phi
                            double theta = 170;
                            double phi = 180;
                            executor.execute(x * y * z, 1000, theta, phi);
                            System.out.println(Arrays.toString(executor.getResults()));
                            CurrentData currentData = projectModel.getCurrentData("angle" + index);
                            //update current data with rcs values
                            currentData.setTheta(theta);
                            currentData.setPhi(phi);
                            currentData.setRcs(executor.getResults());
                            File currentObjFile = new File(outputDir, "angle" + index + ".current");
                            SerializeUtil.writeToFile(currentData, currentObjFile);
                            executor.close();
                        }
                    }
                    for (Mesh mesh : meshes) {
                        mesh.emptyCurrent();
                    }
                    SerializeUtil.writeToFile(projectModel, new File(outputDir, "cubes.obj"));
                    setStatusMessage("done");
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                }
            }

            public void runInUIThread() {
                progressBar.setVisible(false);
            }
        };
        r.start();

    }

    private void setStatusMessage(String message) {
        Platform.runLater(new Runnable() {
            public void run() {
                textStatus.setText(message);
            }
        });
    }

    @FXML
    void buttonOsClicked(ActionEvent event) {
        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Os Files", "*.os"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        osFile = fileChooser.showOpenDialog(osFileText.getScene().getWindow());
        if (osFile != null) {
            fileChooser.setInitialDirectory(osFile.getParentFile());
        }
        osFileText.setText((osFile != null) ? osFile.getAbsolutePath() : "");
    }

    @FXML
    void buttonOutputDirClicked(ActionEvent event) {
        DirectoryChooser dc = new DirectoryChooser();
        File dir = dc.showDialog(outputFolderText.getScene().getWindow());
        if (dir != null) {
            try {
                this.outputFolderText.setText(dir.getCanonicalPath());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void log(File logFile, String message) {

    }
}
