/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jzy_3d_sample.model;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Stage;
import jzy_3d_sample.datafactory.SurfaceLoader;
import org.jzy3d.chart.AWTChart;
import org.jzy3d.chart.Settings;
import org.jzy3d.javafx.JavaFXChartFactory;
import org.jzy3d.javafx.controllers.mouse.JavaFXCameraMouseController;
import org.jzy3d.maths.BoundingBox3d;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.view.AWTRenderer3d;

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
        surface = SurfaceLoader.loadSurface(meshs);
//        surface.setColorMapper(new ColorMapper(new ColorMapRainbow(), surface.getBounds().getZmin(), surface.getBounds().getZmax(), new org.jzy3d.colors.Color(1, 1, 1, 1f)));

        JavaFXChartFactory factory = new JavaFXChartFactory();
        chart = (AWTChart) factory.newChart(Quality.Fastest, "offscreen");
        
        chart.getScene().getGraph().add(surface);
        chart.getView().setSquared(false);
        view = factory.bindImageView(chart);
        
        chart.getControllers().add(new JavaFXCameraMouseController(chart, view) {
            @Override
            protected void mouseWheelMoved(ScrollEvent e) {
//                BoundingBox3d bounds = chart.getView().getBounds();
//                bounds.setZmin(1.5f);
//                bounds.setZmax(1.5f);
//                bounds.setXmin(1.5f);
//                bounds.setXmax(1.5f);
//                bounds.setYmin(1.5f);
//                bounds.setYmax(1.5f);
////                chart.render();
//                chart.getView().shoot();
            }

        });
        factory.addSceneSizeChangedListener(chart, scene);
//        Thread t = new Thread() {
//            public void run() {
//                try {
//                    while (true) {
//                        Thread.sleep(500);
//                        chart.getView().shoot();
//                        repaint();
//                    }
//                } catch (InterruptedException ex) {
//                    ex.printStackTrace();
//                }
//            }
//        };
//        t.setDaemon(true);
//        t.start();
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
        BoundingBox3d bounds = chart.getView().getBounds();
        System.out.println(bounds + ":" + bounds.getZRange());
        bounds.setZmin(-0.2f);
        bounds.setZmax(0f);
        bounds.setXmin(0f);
        bounds.setXmax(0.3f);
        bounds.setYmin(-0.2f);
        bounds.setYmax(0f);

        chart.getView().shoot();
        chart.getView().computeScaledViewBounds();
//        chart.getView().updateBoundsForceUpdate(true);//don't call this, this will reset rendering
    }

    public void moveX(float x) {
        BoundingBox3d bounds = chart.getView().getBounds();
        bounds.setXmin(bounds.getXmin() + x);
        bounds.setXmax(bounds.getXmax() + x);
        chart.getView().shoot();
        chart.getView().computeScaledViewBounds();
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
