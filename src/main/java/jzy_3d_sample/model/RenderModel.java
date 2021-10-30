/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jzy_3d_sample.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import org.jzy3d.maths.Coord2d;
import org.jzy3d.maths.Coord3d;
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
        surface = SurfaceLoader.loadSurface(meshs);
//        surface.setColorMapper(new ColorMapper(new ColorMapRainbow(), surface.getBounds().getZmin(), surface.getBounds().getZmax(), new org.jzy3d.colors.Color(1, 1, 1, 1f)));

        JavaFXChartFactory factory = new JavaFXChartFactory();
        chart = (AWTChart) factory.newChart(Quality.Fastest, "offscreen");
        
        chart.getScene().getGraph().add(surface);
        chart.getView().setSquared(false);
        view = factory.bindImageView(chart);
        try {
            Thread t=new Thread(){
                public void run(){
                    Coord3d viewPoint=chart.getAWTView().getViewPoint();
                    System.out.println(viewPoint);
                    double distance=viewPoint.distance(Coord3d.ORIGIN);
                    double x=0, y=0, z=0;
                    for(int i=0; i<360; i++){
                        chart.getAWTView().setViewPoint(new Coord3d(Math.cos(i*Math.PI/180d), y, Math.sin(i*Math.PI/180d)));
//                        try {
//                            chart.screenshot(new File("test"+i+".png"));
//                        } catch (IOException ex) {
//                            Logger.getLogger(RenderModel.class.getName()).log(Level.SEVERE, null, ex);
//                        }
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(RenderModel.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            };
            t.setDaemon(true);
            //t.start();
//            chart.getAWTView().getCamera().setEye(new Coord3d(0.2, 0.1, 0.1));
//            chart.getAWTView().rotate(new Coord2d(100, 100));
            
        } catch (Exception ex) {
            Logger.getLogger(RenderModel.class.getName()).log(Level.SEVERE, null, ex);
        }
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
