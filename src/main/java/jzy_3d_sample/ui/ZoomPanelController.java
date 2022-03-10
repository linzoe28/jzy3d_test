/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jzy_3d_sample.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import jzy_3d_sample.model.RenderModel;
import org.jzy3d.maths.BoundingBox3d;

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
    private CheckBox gridCheckbox;
    
    private RenderModel renderModel=null;

    public RenderModel getRenderModel() {
        return renderModel;
    }

    public void setRenderModel(RenderModel renderModel) {
        this.renderModel = renderModel;
    }
    
    
    @FXML
    void onZoomClicked(MouseEvent event) {
        renderModel.zoom((float) zoomSlider.getValue());
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
    
<<<<<<< Updated upstream
    
=======
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
    
    @FXML
    void setGrid(ActionEvent event) {
        renderModel.setGrid(gridCheckbox.isSelected());
    }
>>>>>>> Stashed changes
    
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
