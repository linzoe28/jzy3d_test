package jzy_3d_sample.ui;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class SouthpanelController {

    @FXML
    private TextField textBefore;

    public void setTextBeforeValue(String value){
        textBefore.setText(value);
    }

}
