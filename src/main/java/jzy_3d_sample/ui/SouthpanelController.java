package jzy_3d_sample.ui;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class SouthpanelController {

    @FXML
    private TextField textBefore;

    @FXML
    private TextField textAfter;
    
    public void setTextBeforeValue(double value){
        textBefore.setText(""+value);
    }
    
    public void setTextAfterValue(double value){
        textAfter.setText(""+value);
    }

}
