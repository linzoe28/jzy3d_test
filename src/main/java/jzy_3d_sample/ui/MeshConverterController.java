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
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import jzy_3d_sample.datafactory.Read_data;
import jzy_3d_sample.datafactory.SurfaceLoader;
import jzy_3d_sample.model.Cube;
import jzy_3d_sample.model.Mesh;
import jzy_3d_sample.ui.BackgroundRunner.ProgressReporter;
import org.jzy3d.plot3d.primitives.Shape;

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
                    List<Mesh> meshes = r.getdata_from_nas(new File(nasFileText.getText()), new File(osFileText.getText()));
                    Shape surface = SurfaceLoader.loadSurface(meshes);
                    Cube boundingCube = new Cube(surface.getBounds(), meshes);
                    List<Cube> cubes = boundingCube.slice(Double.valueOf(x_value.getText()), Double.valueOf(y_value.getText()), Double.valueOf(z_value.getText()));
                    File outputDir = new File(outputFolderText.getText());
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(new File(outputDir, "cubes.obj"))));
                    objectOutputStream.writeObject(cubes);
                    objectOutputStream.close();
                } catch (IOException ex) {
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
