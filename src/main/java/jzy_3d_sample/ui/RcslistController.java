package jzy_3d_sample.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class RcslistController {
    private boolean ok=false;

    @FXML
    private TextArea rcsValues;

    @FXML
    void onCancel(ActionEvent event) {
        ok=false;
        ((Stage)rcsValues.getScene().getWindow()).close();
    }

    @FXML
    void onOk(ActionEvent event) {
        ok=true;
        ((Stage)rcsValues.getScene().getWindow()).close();
    }

    public boolean isOk() {
        return ok;
    }
    
    public String getValues(){
        return this.rcsValues.getText();
    }
}
