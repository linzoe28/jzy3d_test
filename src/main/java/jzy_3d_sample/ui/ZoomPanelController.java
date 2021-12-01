/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jzy_3d_sample.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import jzy_3d_sample.model.RenderModel;
import org.jzy3d.maths.BoundingBox3d;
import org.jzy3d.maths.Coord3d;

/**
 *
 * @author lendle
 */
public class ZoomPanelController {

    @FXML
    private Slider zoomSlider;
   
    @FXML
    private Slider xSlider;

    @FXML
    private Slider ySlider;

    @FXML
    private Slider zSlider;

    @FXML
    private Button forwordViewButton;

    @FXML
    private Button leftViewButton;

    @FXML
    private Button rightViewButton;

    @FXML
    private Button downViewButton;

    @FXML
    private Button upViewButton;

    @FXML
    private Button backViewButton;
    
    private RenderModel renderModel=null;

    public RenderModel getRenderModel() {
        return renderModel;
    }

    public void setRenderModel(RenderModel renderModel) {
        this.renderModel = renderModel;
    }
    
    
    @FXML
    void onZoomClicked(MouseEvent event) {
        System.err.println(zoomSlider.getValue());
        renderModel.zoom((float) zoomSlider.getValue());
        renderModel.repaint();
        setslider();
    }
    
    @FXML
    void onZoomLClicked(ActionEvent event) {
        renderModel.moveX(-1f);
        renderModel.repaint();
        setslider();
    }
    
    @FXML
    void onZoomRClicked(ActionEvent event) {
        renderModel.moveX(1f);
        renderModel.repaint();
        setslider();
    }
    
    @FXML
    void onZoomUClicked(ActionEvent event) {
        renderModel.moveZ(-1f);
        renderModel.repaint();
        setslider();
    }
    
    @FXML
    void onZoomDClicked(ActionEvent event) {
        renderModel.moveZ(1f);
        renderModel.repaint();
        setslider();
    }
    
    @FXML
    void onZoomFClicked(ActionEvent event) {
        renderModel.moveY(-1f);
        renderModel.repaint();
        setslider();
    }
    

    @FXML
    void xSliderMove(MouseEvent event) {
        renderModel.move((float)xSlider.getValue()/100, (float)ySlider.getValue()/100, (float)zSlider.getValue()/100);
        renderModel.repaint();
    }
    
    @FXML
    void ySliderMove(MouseEvent event) {
        renderModel.move((float)xSlider.getValue()/100, (float)ySlider.getValue()/100, (float)zSlider.getValue()/100);
        renderModel.repaint();
    }
    
    @FXML
    void zSliderMove(MouseEvent event) {
        renderModel.move((float)xSlider.getValue()/100, (float)ySlider.getValue()/100, (float)zSlider.getValue()/100);
        renderModel.repaint();
    }
    
    @FXML
    void backViewButtonclick(ActionEvent event) {
        renderModel.changeView(new Coord3d(Math.PI, 0, 0));
    }

    @FXML
    void downViewButtonclick(ActionEvent event) {
        renderModel.changeView(new Coord3d(0, -Math.PI/2, 0));
    }

    @FXML
    void forwordViewButtonclick(ActionEvent event) {
        renderModel.changeView(new Coord3d(0, 0, 0));
    }

    @FXML
    void leftViewButtonclick(ActionEvent event) {
        renderModel.changeView(new Coord3d(-Math.PI/2, 0, 0));
    }


    @FXML
    void rightViewButtonclick(ActionEvent event) {
        renderModel.changeView(new Coord3d(Math.PI/2, 0, 0));
    }

    @FXML
    void upViewButtonclick(ActionEvent event) {
        renderModel.changeView(new Coord3d(0, Math.PI/2, 0));
    }
    
    
    
    void setslider(){
        BoundingBox3d bounds = renderModel.getChart().getView().getBounds();
        float x = (bounds.getCenter().x-renderModel.getMinX())/renderModel.getRange().x;
        float y = (bounds.getCenter().y-renderModel.getMinY())/renderModel.getRange().y;
        float z = (bounds.getCenter().z-renderModel.getMinZ())/renderModel.getRange().z;
        xSlider.setValue(x*100);
        ySlider.setValue(y*100);
        zSlider.setValue(z*100);
    }
    
}
