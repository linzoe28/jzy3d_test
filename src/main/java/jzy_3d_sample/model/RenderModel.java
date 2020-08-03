/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jzy_3d_sample.model;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import org.jzy3d.chart.AWTChart;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.javafx.JavaFXChartFactory;
import org.jzy3d.plot3d.primitives.Polygon;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.canvas.Quality;

/**
 * a utility class for constructing rendering
 * related objects
 * @author lendle
 */
public class RenderModel {
    private Shape surface=null;
    private ImageView view=null;
    private Scene scene=null;
    public RenderModel(Scene scene, List<Mesh> meshs){
        this.scene=scene;
        surface = new Shape(new ArrayList<Polygon>(meshs));
        surface.setWireframeDisplayed(true);
        surface.setFaceDisplayed(true);
        surface.setWireframeColor(org.jzy3d.colors.Color.BLACK);
//        surface.setColorMapper(new ColorMapper(new ColorMapRainbow(), surface.getBounds().getZmin(), surface.getBounds().getZmax(), new org.jzy3d.colors.Color(1, 1, 1, 1f)));

        JavaFXChartFactory factory = new JavaFXChartFactory();
        AWTChart chart = (AWTChart) factory.newChart(Quality.Intermediate, "offscreen");
        chart.getScene().getGraph().add(surface);
        view = factory.bindImageView(chart);
        factory.addSceneSizeChangedListener(chart, scene);
    }

    public Shape getSurface() {
        return surface;
    }

    public ImageView getView() {
        return view;
    }
    
    public Cube getBoundingCube(){
        return new Cube(surface.getBounds());
    }
}
