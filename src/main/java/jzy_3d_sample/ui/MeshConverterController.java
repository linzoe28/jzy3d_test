/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jzy_3d_sample.ui;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import jzy_3d_sample.datafactory.FastN2fWriter;
import jzy_3d_sample.datafactory.Read_data;
import jzy_3d_sample.datafactory.SurfaceLoader;
import jzy_3d_sample.model.Cube;
import jzy_3d_sample.model.Mesh;
import jzy_3d_sample.ui.BackgroundRunner.ProgressReporter;
import jzy_3d_sample.utils.OSFileSplitter;
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
                    Read_data r = new Read_data();
                    File bigOsFile = new File(osFileText.getText());
                    File nasFile = new File(nasFileText.getText());
                    List<File> osFiles = OSFileSplitter.splitByAngle(bigOsFile);
                    File outputDir = new File(outputFolderText.getText());
                    long x=Long.valueOf(x_value.getText());
                    long y=Long.valueOf(y_value.getText());
                    long z=Long.valueOf(z_value.getText());
                    int index = 0;
                    N2fExecutor executor = new N2fExecutorImpl(new File("n2ftools"));

                    executor.addN2fExecutorListener(new N2fExecutorListener() {
                        public void statusUpdated(N2fExecutorEvent e) {
                            System.out.println(e.getMessage());
                        }
                    });

                    for (File osFile : osFiles) {
                        File subOutputFolder = new File(outputDir, "angle" + (index++));
                        if (subOutputFolder.exists()) {
                            FileUtils.deleteDirectory(subOutputFolder);
                        }
                        FileUtils.forceMkdir(subOutputFolder);

                        List<Mesh> meshes = r.getdata_from_nas(nasFile, osFile);
                        Shape surface = SurfaceLoader.loadSurface(meshes);
                        Cube boundingCube = new Cube(surface.getBounds(), meshes);
                        List<Cube> cubes = boundingCube.slice(x,y,z);

                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(new File(subOutputFolder, "cubes.obj"))));
                        objectOutputStream.writeObject(cubes);
                        objectOutputStream.close();
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
                        executor.init(subOutputFolder);
                        executor.execute(x*y*z, 1000, 170, 180);
                        System.out.println(Arrays.toString(executor.getResults()));
                    }
                    executor.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            public void runInUIThread() {
                progressBar.setVisible(false);
            }
        };
        r.start();

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
}
