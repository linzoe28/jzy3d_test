/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jzy_3d_sample.model;

import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Stage;
import org.jzy3d.chart.AWTChart;
import org.jzy3d.chart.Settings;
import org.jzy3d.javafx.JavaFXChartFactory;
import org.jzy3d.javafx.controllers.mouse.JavaFXCameraMouseController;
import org.jzy3d.plot3d.primitives.Polygon;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.canvas.Quality;

/**
 * a utility class for constructing rendering related objects
 *
 * @author lendle
 */
public class RenderModel {

    private Shape surface = null;
    private ImageView view = null;
    private Scene scene = null;
    private List<Mesh> meshs = new ArrayList<>();
    private Stage stage = null;
    private AWTChart chart = null;
    private float currentZoom = 1;

    public RenderModel(Scene scene, Stage stage, List<Mesh> meshs) {
        this.scene = scene;
        this.stage = stage;
        Settings.getInstance().setHardwareAccelerated(true);
        //System.out.println(Settings.getInstance().isHardwareAccelerated());
        surface = new Shape(new ArrayList<Polygon>(meshs));
        surface.setWireframeDisplayed(true);
        surface.setFaceDisplayed(true);
        surface.setWireframeColor(org.jzy3d.colors.Color.BLUE);
//        surface.setColorMapper(new ColorMapper(new ColorMapRainbow(), surface.getBounds().getZmin(), surface.getBounds().getZmax(), new org.jzy3d.colors.Color(1, 1, 1, 1f)));

        JavaFXChartFactory factory = new JavaFXChartFactory();
        chart = (AWTChart) factory.newChart(Quality.Fastest, "offscreen");
        chart.getScene().getGraph().add(surface);
        chart.getView().setSquared(false);
        view = factory.bindImageView(chart);
        chart.getControllers().add(new JavaFXCameraMouseController(chart, view) {
            @Override
            protected void mouseWheelMoved(ScrollEvent e) {

            }
            
        });
        factory.addSceneSizeChangedListener(chart, scene);

        this.meshs.addAll(meshs);
    }

    public AWTChart getChart() {
        return chart;
    }

    public Shape getSurface() {
        return surface;
    }

    public ImageView getView() {
        return view;
    }

    public Cube getBoundingCube() {
        return new Cube(surface.getBounds(), meshs);
    }

    public void zoom(float factor) {
        chart.getView().zoomX(1 / currentZoom, true);
        chart.getView().zoomY(1 / currentZoom, true);
        chart.getView().zoomZ(1 / currentZoom, true);
        chart.getView().zoomX(factor, true);
        chart.getView().zoomY(factor, true);
        chart.getView().zoomZ(factor, true);
        currentZoom=factor;
//        chart.getView().setBoundManual(surface.getBounds().scale(new Coord3d(factor, factor, factor)));
//        chart.getView().shoot();
        //chart.getView().updateBoundsForceUpdate(true);
    }

    public void repaint() {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                stage.requestFocus();
                chart.render();
                if (stage.getWidth() % 10 == 0) {
                    stage.setWidth(stage.getWidth() - 1);
                } else {
                    stage.setWidth(stage.getWidth() + 1);
                }
            }
        });

    }
}
