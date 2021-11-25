package jzy_3d_sample.ui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class RCSvalueController {

    @FXML
    private TextField textRCS;

    @FXML
    private Slider sliderRCS;

    @FXML
    private CheckBox to_db_check;

    public CheckBox getTo_db_check() {
        return to_db_check;
    }

    private EventHandler<ActionEvent> eventHandler = null;
    private boolean to_db_check_isOK;

    public void setActionHandler(EventHandler<ActionEvent> handler) {
        this.eventHandler = handler;
    }

    @FXML
    void onTextRCSChange(KeyEvent event) {
        try {
            if (event.getCode() == KeyCode.ENTER) {
                double value = Double.valueOf(textRCS.getText());
                sliderRCS.setValue(value);
            }
        } catch (Exception e) {
        }
    }

//    @FXML
//    void ondbCheckBoxChange(ActionEvent event) {
//        to_db_check_isOK = to_db_check.isSelected();
//        
//    }

    @FXML
    public void initialize() {
        textRCS.setText("" + sliderRCS.getValue());
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

    public boolean get_to_db_checkisOK() {
        return to_db_check.isSelected();
    }

    public double to_dbvalue(double value) {
        double dbvalue = Math.log10(value);
        return dbvalue;
    }

}
