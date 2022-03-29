package jzy_3d_sample.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import jzy_3d_sample.model.Vertex;

public class AnglePanelController {

    private AngleSelectionHandler angleSelectionHandler = null;
    private EffectivePointHandler effectivePointHandler = null;
    @FXML
    private ToggleButton buttonSwitchColorMode=null;

    @FXML
    private ListView<?> anglelist;

    @FXML
    private ListView<Vertex> effective_point_list;

    public ListView<?> getAnglelist() {
        return anglelist;
    }

    public AngleSelectionHandler getAngleSelectionHandler() {
        return angleSelectionHandler;
    }

    public void setAngleSelectionHandler(AngleSelectionHandler angleSelectionHandler) {
        this.angleSelectionHandler = angleSelectionHandler;
    }

    public ListView<Vertex> getEffective_point_list() {
        return effective_point_list;
    }

    public EffectivePointHandler getEffectivePointHandler() {
        return effectivePointHandler;
    }

    public void setEffectivePointHandler(EffectivePointHandler effectivePointHandler) {
        this.effectivePointHandler = effectivePointHandler;
    }

    @FXML
    void onShowButtonClicked(ActionEvent event) {
        System.out.println("anglelist_index" + anglelist.getSelectionModel().getSelectedIndex());
        if (this.angleSelectionHandler != null) {
            this.angleSelectionHandler.angleSelectionChanged(anglelist.getSelectionModel().getSelectedIndex());
        }
    }

    @FXML
    void onShow_EffectivePoint(ActionEvent event) {
        System.err.println("effective_point_list_index" + effective_point_list.getSelectionModel().getSelectedIndex());
        if (this.effectivePointHandler != null) {
            buttonSwitchColorMode.setSelected(true);
            this.effectivePointHandler.EffectivePointChanged(effective_point_list.getSelectionModel().getSelectedIndex());
        }
    }

//    @FXML
//    void onEffectivePointToggle(ActionEvent event) {
//
//    }

}
