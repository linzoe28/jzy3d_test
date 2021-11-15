/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jzy_3d_sample.model;

import java.io.File;
import java.io.FileNotFoundException;
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
import jzy_3d_sample.model.serialized.ProjectModel;
import jzy_3d_sample.ui.SubCubesColorPainter;
import jzy_3d_sample.utils.SerializeUtil;
import org.jzy3d.chart.AWTChart;
import org.jzy3d.chart.Settings;
import org.jzy3d.javafx.JavaFXChartFactory;
import org.jzy3d.javafx.controllers.mouse.JavaFXCameraMouseController;
import org.jzy3d.maths.BoundingBox3d;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.canvas.Quality;

/**
 * a utility class for constructing rendering related objects
 *
 * @author lendle
 */
public class RenderModel {

    private Shape surface = null, surfaceLight=null;
    private ImageView view = null;
    private Scene scene = null;
    private List<Mesh> meshs = new ArrayList<>();
    private Stage stage = null;
    private AWTChart chart = null;
    private float currentZoom = 1;
    private ProjectModel projectModel = null;//only a rendermodel built from saved model will have a project model

    /**
     * load model from pre-defined file
     *
     * @param scene
     * @param stage
     * @param savedModel
     */
    public RenderModel(Scene scene, Stage stage, File savedModel) throws FileNotFoundException, IOException, ClassNotFoundException {
        this.scene = scene;
        this.stage = stage;
        List<Mesh> meshs = loadProjectModel(savedModel).getMeshes();
        Settings.getInstance().setHardwareAccelerated(true);
//        Shape [] surfaces=SurfaceLoader.loadSurfaces(meshs);
//        surface=surfaces[0];
//        surfaceLight=surfaces[1];
        surface = SurfaceLoader.loadSurface(meshs);
        this.meshs.addAll(meshs);

        for (Mesh m : meshs) {
            for (Vertex v : m.getVertices()) {
                m.add(v);
            }
        }
        this.init();
        SubCubesColorPainter colorPainter = new SubCubesColorPainter();
        for (int i = 0; i < projectModel.getCubes().size(); i++) {
            Cube cube = projectModel.getCubes().get(i);
            colorPainter.paint(i, cube);
        }
        this.getSurface().setWireframeDisplayed(false);
    }

    private ProjectModel loadProjectModel(File file) throws FileNotFoundException, IOException, ClassNotFoundException {
        projectModel = (ProjectModel) SerializeUtil.readFromFile(file);
        projectModel.setHomeFolder(file.getParentFile());
        return projectModel;
    }

    public ProjectModel getProjectModel() {
        return projectModel;
    }

    /**
     * load model from meshes
     *
     * @param scene
     * @param stage
     * @param meshs
     */
    public RenderModel(Scene scene, Stage stage, List<Mesh> meshs) {
        this.scene = scene;
        this.stage = stage;
        Settings.getInstance().setHardwareAccelerated(true);
        surface = SurfaceLoader.loadSurface(meshs);
        this.meshs.addAll(meshs);
        this.init();
    }

    private void init() {
        JavaFXChartFactory factory = new JavaFXChartFactory();
        chart = (AWTChart) factory.newChart(Quality.Fastest, "offscreen");
        //chart.getScene().getGraph().add(surfaceLight);
        chart.getScene().getGraph().add(surface);
        chart.getView().setSquared(false);
        view = factory.bindImageView(chart);
        try {
            Thread t = new Thread() {
                public void run() {
                    try {
                        Thread.sleep(60*3*1000);
                        System.out.println("replacing");
                        chart.getScene().getGraph().remove(surfaceLight);
                        chart.getScene().getGraph().add(surface);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(RenderModel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };
            t.setDaemon(true);
            //t.start();

        } catch (Exception ex) {
            Logger.getLogger(RenderModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            Thread t = new Thread() {
                public void run() {
                    Coord3d viewPoint = chart.getAWTView().getViewPoint();
                    double distance = viewPoint.distance(Coord3d.ORIGIN);
                    double x = 0, y = 0, z = 0;
                    for (int i = 0; i < 360; i++) {
                        chart.getAWTView().setViewPoint(new Coord3d(Math.cos(i * Math.PI / 180d), y, Math.sin(i * Math.PI / 180d)));
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
