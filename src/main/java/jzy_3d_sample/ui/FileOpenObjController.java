package jzy_3d_sample.ui;

import java.io.File;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class FileOpenObjController {

    @FXML
    private TextField objFileText;

    private File objfile = null;
    private boolean ok = false;
    private FileChooser fileChooser = new FileChooser();

    @FXML
    void cancelButtonAction(MouseEvent event) {
        ok = false;
        ((Stage) objFileText.getScene().getWindow()).close();
    }

    @FXML
    void objFileButtonAction(ActionEvent event) {
        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Obj Files", "*.obj"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        objfile = fileChooser.showOpenDialog(objFileText.getScene().getWindow());
        if (objfile != null) {
            fileChooser.setInitialDirectory(objfile.getParentFile());
        }
        objFileText.setText((objfile != null) ? objfile.getAbsolutePath() : "");
    }

    @FXML
    void okButtonAction(MouseEvent event) {
        ok = true;
        ((Stage) objFileText.getScene().getWindow()).close();
    }

    public boolean isOk() {
        return ok;
    }

    public File getobjFile() {
        return objfile;
    }

}
