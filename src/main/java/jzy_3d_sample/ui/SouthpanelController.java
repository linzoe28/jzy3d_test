package jzy_3d_sample.ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;

public class SouthpanelController {

    @FXML
    private TextField textBefore;

    public void setTextBeforeValue(String value){
        textBefore.setText(value);
    }
    @FXML
    private Label textStatus;

    @FXML
    private ProgressIndicator progress;
    
    public void setStatus(String status){
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                textStatus.setText(status);
            }
            
        });
    }
    
    public void startProgress(){
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                progress.setVisible(true);
            }
            
        });
        
    }
    
    public void stopProgress(){
       Platform.runLater(new Runnable(){
            @Override
            public void run() {
                progress.setVisible(false);
            }
            
        });
    }
}
