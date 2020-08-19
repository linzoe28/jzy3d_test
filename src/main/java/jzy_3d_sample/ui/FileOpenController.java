/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jzy_3d_sample.ui;

import java.io.File;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author lendle
 */
public class FileOpenController{
    @FXML
    private TextField nasFileText;

    @FXML
    private TextField osFileText;
    
    private File nasFile=null;
    private File osFile=null;
    private boolean ok=false;

    @FXML
    void cancelButtonAction(MouseEvent event) {
        ok=false;
        ((Stage)nasFileText.getScene().getWindow()).close();
    }

    @FXML
    void nasFileButtonAction(MouseEvent event) {
        FileChooser fileChooser=new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Nas Files", "*.nas"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        nasFile=fileChooser.showOpenDialog(nasFileText.getScene().getWindow());
        nasFileText.setText((nasFile!=null)?nasFile.getAbsolutePath():"");
    }

    @FXML
    void okButtonAction(MouseEvent event) {
        ok=true;
        ((Stage)nasFileText.getScene().getWindow()).close();
    }

    public File getNasFile() {
        return nasFile;
    }

    

    public File getOsFile() {
        return osFile;
    }

    public boolean isOk() {
        return ok;
    }

    
    @FXML
    void osFileButtonAction(MouseEvent event) {
        FileChooser fileChooser=new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Os Files", "*.os"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        osFile=fileChooser.showOpenDialog(osFileText.getScene().getWindow());
        osFileText.setText((osFile!=null)?osFile.getAbsolutePath():"");
    }
    
}
