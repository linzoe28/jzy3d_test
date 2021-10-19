/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jzy_3d_sample.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import jzy_3d_sample.model.RenderModel;

/**
 *
 * @author lendle
 */
public class ZoomPanelController {
    @FXML
    private Button buttonZoom2X;
    private RenderModel renderModel=null;

    public RenderModel getRenderModel() {
        return renderModel;
    }

    public void setRenderModel(RenderModel renderModel) {
        this.renderModel = renderModel;
    }
    
    @FXML
    void onZoom2XClicked(ActionEvent event) {
        renderModel.zoom(2.0f);
        renderModel.repaint();
    }
    
    @FXML
    void onZoomLClicked(ActionEvent event) {
        renderModel.moveX(0.3f);
        renderModel.repaint();
    }
    
    @FXML
    void onZoomRClicked(ActionEvent event) {
        renderModel.moveX(-0.3f);
        renderModel.repaint();
    }
}
