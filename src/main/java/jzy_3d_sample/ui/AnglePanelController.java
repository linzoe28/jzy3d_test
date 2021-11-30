package jzy_3d_sample.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class AnglePanelController {

    private AngleSelectionHandler angleSelectionHandler = null;

    @FXML
    private ListView<?> anglelist;

    public ListView<?> getAnglelist() {
        return anglelist;
    }

    public AngleSelectionHandler getAngleSelectionHandler() {
        return angleSelectionHandler;
    }

    public void setAngleSelectionHandler(AngleSelectionHandler angleSelectionHandler) {
        this.angleSelectionHandler = angleSelectionHandler;
    }

    @FXML
    void onShowButtonClicked(ActionEvent event) {
        System.out.println(anglelist.getSelectionModel().getSelectedIndex());
        if (this.angleSelectionHandler != null) {
            this.angleSelectionHandler.angleSelectionChanged(anglelist.getSelectionModel().getSelectedIndex());
        }
    }

}
