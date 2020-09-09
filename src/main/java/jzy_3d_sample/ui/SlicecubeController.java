package jzy_3d_sample.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class SlicecubeController {

    private boolean ok = false;
    @FXML
    private TextField x_value;

    @FXML
    private TextField y_value;

    @FXML
    private TextField z_value;

    @FXML
    private Button onCancel;

    @FXML
    void onOk(ActionEvent event) {
        ok = true;
        ((Stage) z_value.getScene().getWindow()).close();
    }

    @FXML
    void onCancel(ActionEvent event) {
        ok = false;
        ((Stage) z_value.getScene().getWindow()).close();
    }

    public boolean isOk() {
        return ok;
    }

    public double[] getslice() {
        return new double[]{Double.valueOf(x_value.getText()), Double.valueOf(y_value.getText()), Double.valueOf(z_value.getText())};
    }

}
