package jzy_3d_sample.ui;

import java.io.File;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class RcslistController {

    private boolean ok = false;
    private FileChooser fileChooser = new FileChooser();
    private File RCSFile = null;

    @FXML
    private TextField RCSFileText;

    public RcslistController() {
          fileChooser.setInitialDirectory(new File(".").getAbsoluteFile());
    }

    @FXML
    void RCSFileButtonAction(ActionEvent event) {
        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("RCS Value", "*.csv"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        RCSFile = fileChooser.showOpenDialog(RCSFileText.getScene().getWindow());

        if (RCSFile != null) {
            fileChooser.setInitialDirectory(RCSFile.getParentFile());
        }
        RCSFileText.setText((RCSFile != null) ? RCSFile.getAbsolutePath() : "");
    }


    @FXML
    void onCancel(ActionEvent event) {
        ok = false;
        ((Stage) RCSFileText.getScene().getWindow()).close();
    }

    @FXML
    void onOk(ActionEvent event) {
        ok = true;
        ((Stage) RCSFileText.getScene().getWindow()).close();
    }

    public boolean isOk() {
        return ok;
    }

    public File getRCSFile() {
        return RCSFile;
    }

}
