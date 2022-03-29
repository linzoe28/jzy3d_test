/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jzy_3d_sample.ui;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.FileAppender;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jzy_3d_sample.datafactory.AngleLabelMapGenerator;
import jzy_3d_sample.datafactory.FastN2fWriter;
import jzy_3d_sample.datafactory.OSFileParser;
import jzy_3d_sample.datafactory.Read_data;
import jzy_3d_sample.datafactory.SurfaceLoader;
import jzy_3d_sample.model.Cube;
import jzy_3d_sample.model.Mesh;
import jzy_3d_sample.model.meshconverter.Status;
import jzy_3d_sample.model.os.OSRecordMap;
import jzy_3d_sample.model.serialized.CurrentData;
import jzy_3d_sample.model.serialized.ProjectModel;
import jzy_3d_sample.ui.BackgroundRunner.ProgressReporter;
import jzy_3d_sample.utils.OSFileSplitter;
import jzy_3d_sample.utils.SerializeUtil;
import jzy_3d_sample.utils.ThreadUtils;
import jzy_3d_sample.utils.meshconverter.StatusFileUtil;
import org.apache.commons.io.FileUtils;
import org.jzy3d.plot3d.primitives.Shape;
import org.slf4j.LoggerFactory;
import rocks.imsofa.n2fproxy.N2fExecutor;
import rocks.imsofa.n2fproxy.N2fExecutorEvent;
import rocks.imsofa.n2fproxy.N2fExecutorImpl;
import rocks.imsofa.n2fproxy.N2fExecutorListener;
import ch.qos.logback.classic.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 *
 * @author lendle
 */
public class MeshConverterController {

    private File nasFile = null;
    private File osFile = null;
    private boolean ok = false;
    private FileChooser fileChooser = new FileChooser();
    private boolean closing = false;
    @FXML
    private Button buttonOk;
    
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
    private Button buttonCancel;

    private BackgroundRunner r = null;

    @FXML
    void buttonCancelClicked(ActionEvent event) {
        shutdown();
    }

    public void shutdown() {
        closing = true;
        if (r != null && !r.isStopped()) {
            buttonCancel.setDisable(true);
            buttonCancel.setText("Stopping");
            r.shutdown();
        } else {
            ((Stage) nasFileText.getScene().getWindow()).close();
        }
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
        buttonOk.setDisable(true);
        r = new BackgroundRunner(new ProgressReporter() {
            public void startProgress() {
            }

            public void stopProgress() {
            }

            public void setStatus(String status) {
            }
        }) {
//            private List<Integer> frequencies = new ArrayList<>();
//            private List<File> n2fProcessingQueue = new ArrayList<>();
//            private List<File> osFiles=new ArrayList<>();
            private Status status = null;
            private File outputDir = null;
            private Logger logger=null;

            @Override
            public void afterStopped() {
                buttonOk.setDisable(false);
                if (closing) {
                    ((Stage) nasFileText.getScene().getWindow()).close();
                }
            }

            public void runBeforeWorkerThread() {
                progressBar.setVisible(true);
            }

            private void checkpoint() throws Exception {
                StatusFileUtil.saveStatus(outputDir, status);
                if (ThreadUtils.isInterrupted()) {
                    throw new Exception("interrupted");
                }
            }

            public void runInWorkerThread() {
                try {
                    ProjectModel projectModel = new ProjectModel();
                    File bigOsFile = new File(osFileText.getText());
                    File nasFile = new File(nasFileText.getText());
                    setStatusMessage("splitting os files");
                    outputDir = new File(outputFolderText.getText());
//                    if (!outputDir.exists()) {
//                        FileUtils.forceMkdir(outputDir);
//                    }
//                    status = StatusFileUtil.loadStatus(outputDir, bigOsFile.getParentFile());

//                    if (outputDir.exists()) {
//                        FileUtils.deleteDirectory(outputDir);
//                    }
//                    System.out.println(outputDir.listFiles()!=null && outputDir.listFiles().length>0);
                    if(outputDir.exists() && outputDir.listFiles()!=null && outputDir.listFiles().length>0){
                        Platform.runLater(new Runnable(){
                            public void run(){
                                Alert a = new Alert(AlertType.ERROR);
                                a.setTitle("Not Empty");
                                a.setContentText("Output Directory not Empty!");
                                a.show();
                                shutdown();
                            }
                        });
                        
                        return;
                    }
                    if(!outputDir.exists()){
                        FileUtils.forceMkdir(outputDir);
                    }

                    //configure logger
                    LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
                    PatternLayoutEncoder ple = new PatternLayoutEncoder();
                    ple.setPattern("%date %level [%thread] %logger{10} [%file:%line] %msg%n");
                    ple.setContext(lc);
                    ple.start();
                    FileAppender<ILoggingEvent> fileAppender = new FileAppender<ILoggingEvent>();
                    fileAppender.setFile(new File(outputDir, "MeshConverter.log").getCanonicalPath());
                    fileAppender.setEncoder(ple);
                    fileAppender.setContext(lc);
                    fileAppender.start();
                    ConsoleAppender consoleAppender=new ConsoleAppender();
                    consoleAppender.setEncoder(ple);
                    consoleAppender.setContext(lc);
                    consoleAppender.start();
                    logger = (Logger) LoggerFactory.getLogger(MeshConverterController.class.getName());
                    logger.addAppender(fileAppender);
                    logger.addAppender(consoleAppender);
                    logger.setLevel(Level.ALL);
                    
                    status = StatusFileUtil.loadStatus(outputDir, bigOsFile.getParentFile());
                    File outputOsFolder = new File(bigOsFile.getParentFile(), ".os");

                    if (outputOsFolder.exists() && outputOsFolder.list().length == 0) {
                        FileUtils.deleteDirectory(outputOsFolder);
                    }
                    logger.info("splitting os file");
                    //split os files by angle
                    if (status.getOsFiles().isEmpty()) {
                        status.setOsFiles(splitOSFileByAngle(outputOsFolder, bigOsFile));
                    }
                    ////////////////////////////
                    Read_data r = new Read_data(outputDir, "angle0");
                    long x = Long.valueOf(x_value.getText());
                    long y = Long.valueOf(y_value.getText());
                    long z = Long.valueOf(z_value.getText());
                    int index = 0;

                    //first, output the no-os model
                    File firstOsFile = status.getOsFiles().get(0);
                    checkpoint();
                    setStatusMessage("processing mesh");
                    logger.info("processing mesh");
                    List<Mesh> meshes = r.getdata_from_nas(nasFile, firstOsFile);
                    checkpoint();
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

                    //serializeCurrentData
                    logger.info("serializing current data");
                    serializeCurrentData(index, status.getOsFiles(), outputDir, r, meshes, x, y, z, projectModel);
                    ///////////////////////
                    index = 0;
                    projectModel.setHomeFolder(outputDir);
                    logger.info("running n2f and RCS");
                    if (checkboxn2f.isSelected()) {
                        int delta = AngleLabelMapGenerator.getDelta(status.getN2fProcessingQueue().size());
                        int theta = 0;
                        int phi = 0;
                        for (phi = 0; index < status.getN2fProcessingQueue().size() && phi <= 360; phi += delta) {
                            for (theta = 0; index < status.getN2fProcessingQueue().size() && theta <= 180; theta += delta) {
                                checkpoint();
                                File n2fFolder = status.getN2fProcessingQueue().get(index);
                                int frequency = status.getFrequencies().get(index);
                                logger.info("theta=" + theta + ", phi=" + phi + ", frequency=" + frequency);
                                setStatusMessage("processing n2f for angle " + (index + 1) + "/" + status.getOsFiles().size());
                                logger.info("processing n2f for angle " + (index + 1) + "/" + status.getOsFiles().size());
                                N2fExecutor executor = new N2fExecutorImpl(new File("n2ftools"));

                                executor.addN2fExecutorListener(new N2fExecutorListener() {
                                    public void statusUpdated(N2fExecutorEvent e) {
                                        setStatusMessage(e.getMessage());
                                    }
                                });

                                executor.init(n2fFolder);
                                executor.execute(x * y * z, frequency, theta, phi, delta);
                                CurrentData currentData = projectModel.getCurrentData("angle" + index);
                                //update current data with rcs values
                                currentData.setTheta(theta);
                                currentData.setPhi(phi);
                                currentData.setRcs(executor.getResults());
                                try{
                                    currentData.setRcsTotal(executor.getRCSTotal());
                                    logger.info("\trcs=" + executor.getRCSTotal() + ":" + Arrays.toString(executor.getResults()));
                                }catch(Exception e){
                                    logger.warn("empty RCS_total file, assume rcs=0");
                                    currentData.setRcsTotal(0);
                                }
                                
                                File currentObjFile = new File(outputDir, "angle" + index + ".current");
                                SerializeUtil.writeToFile(currentData, currentObjFile);
                                logger.info("current data serialized");
                                executor.close();
                                index++;
                            }
                        }

                    }
                    for (Mesh mesh : meshes) {
                        mesh.emptyCurrent();
                    }
                    logger.info("serializing obj file");
                    SerializeUtil.writeToFile(projectModel, new File(outputDir, "cubes.obj"));
                    setStatusMessage("done");
                    logger.info("done");
                    checkpoint();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    logger.error(ex.getMessage(), ex);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    logger.error(ex.getMessage(), ex);
                } finally {
                }
            }

            private void serializeCurrentData(int index, List<File> osFiles, File outputDir, Read_data r1, List<Mesh> meshes, long x, long y, long z, ProjectModel projectModel) throws Exception {
                ///////////////////////////////
                index = 0;//angle 0 was already processed when loading meshes
                for (int n = 0; n < osFiles.size(); n++) {
                    checkpoint();
                    File osFile = osFiles.get(n);
                    setStatusMessage("slicing for angle " + (index + 1) + "/" + osFiles.size());
                    logger.info("slicing for angle " + (index + 1) + "/" + osFiles.size());
                    File subOutputFolder = new File(outputDir, "angle" + (index));
                    if (subOutputFolder.exists()) {
                        FileUtils.deleteDirectory(subOutputFolder);
//                        n2fProcessingQueue.add(subOutputFolder);
//                        continue;
                    }
                    FileUtils.forceMkdir(subOutputFolder);
                    //re-apply different os records to the original mesh
                    OSRecordMap osRecords = null;
                    if (n == 0) {
                        osRecords = r1.getLastOSRecordMap();
                    } else {
                        osRecords = OSFileParser.readOSFile(outputDir, osFile, "angle" + (index));
                        r1.applyOStoMeshes(meshes, osRecords);
                    }
                    logger.info("finished slicing for angle " + (index + 1) + "/" + osFiles.size());
                    status.getFrequencies().add(osRecords.getFrequency());
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
                    status.getN2fProcessingQueue().add(subOutputFolder);
                    //TODO: separate n2f process from slicing process
                    CurrentData currentData = new CurrentData();
                    currentData.setOsRecordsMap(osRecords);
                    File currentObjFile = new File(outputDir, "angle" + index + ".current");
                    SerializeUtil.writeToFile(currentData, currentObjFile);
                    projectModel.getCurrentDataList(true).add("angle" + index);
                    index++;

                    //FileUtils.forceDelete(osFile);
//                        logger.info("hit: " + osRecords.getHit() + ", miss: " + osRecords.getMiss());
                }
            }

            private List<File> splitOSFileByAngle(File outputOsFolder, File bigOsFile) throws Exception {
                //split os files by angle
                List<File> osFiles = null;
                if (!outputOsFolder.exists()) {
                    outputOsFolder.mkdirs();
                    osFiles = OSFileSplitter.splitByAngle(bigOsFile, new OSFileSplitter.OutputFileCreator() {
                        public File createFile(File osFile, long index) {
                            return new File(outputOsFolder, "" + index + ".os");
                        }
                    }, null);
                } else if (outputOsFolder.listFiles().length > 0) {
                    osFiles = Arrays.asList(outputOsFolder.listFiles());
                    Collections.sort(osFiles, new Comparator<File>() {
                        public int compare(File o1, File o2) {
                            int number1 = Integer.valueOf(o1.getName().substring(0, o1.getName().indexOf(".")));
                            int number2 = Integer.valueOf(o2.getName().substring(0, o2.getName().indexOf(".")));
                            return number1 - number2;
                        }
                    });
                }
                return osFiles;
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
