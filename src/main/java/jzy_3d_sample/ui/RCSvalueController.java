package jzy_3d_sample.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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
import javafx.util.StringConverter;
import jzy_3d_sample.model.Cube;

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

    
    @FXML
    public void initialize() {
//        System.out.println(sliderRCS.getLabelFormatter());
        sliderRCS.setLabelFormatter(new StringConverter<Double>() {
            @Override
            public String toString(Double t) {
                double ret = 0;
                if (to_db_check.isSelected()) {
                    ret = to_dbvalue(t);
                } else {
                    ret = t;
                }
                return String.format("%6.4f", ret);
            }

            @Override
            public Double fromString(String string) {
                if (!to_db_check.isSelected()) {
                    return Math.pow(10, Double.valueOf(string)/10);
                } else {
                    return Double.valueOf(string);
                }
            }
        });
        sliderRCS.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                double value = sliderRCS.getValue();
                if (to_db_check.isSelected()) {
                    value = to_dbvalue(value);
                }
                textRCS.setText(String.format("%6.4f", value));
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
        return sliderRCS.getValue();
    }
    
    public void setThreshold(double  threshold) {
        this.sliderRCS.setValue(threshold);
    }

    public boolean get_to_db_checkisOK() {
        return to_db_check.isSelected();
    }

    public double to_dbvalue(double value) {
        if (value == 0) {
            return 0;
        }
        double dbvalue = 10 * Math.log10(value);
        return dbvalue;
    }

    public void repaint() {
        double value = sliderRCS.getValue();
        if (to_db_check.isSelected()) {
            value = to_dbvalue(value);
        }
        textRCS.setText(String.format("%6.4f", value));
    }

    public List<Cube> sortCube(List<Cube> Cubes) {
        List<Cube> clonedCubes=new ArrayList<>(Cubes);
        Collections.sort(clonedCubes, new Comparator<Cube>() {

            @Override
            public int compare(Cube o1, Cube o2) {
                if (o1.getRcs() < o2.getRcs()) {
                    return -1;
                } else if (o1.getRcs() == o2.getRcs()) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });

        return clonedCubes;

    }

}
