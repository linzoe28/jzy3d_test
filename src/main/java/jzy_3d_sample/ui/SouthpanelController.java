package jzy_3d_sample.ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import jzy_3d_sample.model.Vertex;
import jzy_3d_sample.ui.BackgroundRunner.ProgressReporter;

public class SouthpanelController implements ProgressReporter{

    @FXML
    private TextField textBefore;
    @FXML
    private Label extremePointPosition;
    
    public void setExtremePointPosition(Vertex point){
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                extremePointPosition.setText(""+String.format("%.4f", point.x)+", "+String.format("%.4f", point.y)+", "+String.format("%.4f", point.z));
                extremePointPosition.setTooltip(new Tooltip(""+point.x+", "+point.y+", "+point.z));
            }
        });
    }
    
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
