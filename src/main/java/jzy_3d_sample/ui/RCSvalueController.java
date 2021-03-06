package jzy_3d_sample.ui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class RCSvalueController {

    @FXML
    private TextField textRCS;

    @FXML
    private Slider sliderRCS;
    
    private EventHandler<ActionEvent> eventHandler=null;
    
    public void setActionHandler(EventHandler<ActionEvent> handler){
        this.eventHandler=handler;
    }
    
    @FXML
    void onTextRCSChange(KeyEvent event) {
        try{
            if(event.getCode()==KeyCode.ENTER){
                double value=Double.valueOf(textRCS.getText());
                sliderRCS.setValue(value);
            }
        }catch(Exception e){}
    }

    
    @FXML
    public void initialize() {
        textRCS.setText(""+sliderRCS.getValue());
        sliderRCS.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                textRCS.setText(Double.toString(sliderRCS.getValue()));
            }
        });
        
    }

    @FXML
    void Drawmodel(ActionEvent event) {
        this.eventHandler.handle(event);
    }

    public void setSlidermin(double value) {
        sliderRCS.setMin(value);
    }

    public void setSlidermax(double value) {
        sliderRCS.setMax(value);
    }

    public void setSlidervalue(double value) {
        sliderRCS.setValue(value);
    }

    public void setSliderMajorTickUnit(double value) {
        sliderRCS.setMajorTickUnit(value);
    }

    public double getThreshold() {
        return Double.valueOf(textRCS.getText());
    }

}
