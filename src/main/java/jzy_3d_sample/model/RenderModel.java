/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//test
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
import org.jzy3d.colors.Color;
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

    private Shape surface = null, surfaceLight = null;
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
        surface = SurfaceLoader.loadSurface(meshs, 1.0);
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
    private float maxX = 0, minX = 0, maxY = 0, minY = 0, maxZ = 0, minZ = 0, xcenter = 0, ycenter = 0, zcenter = 0;
    private Coord3d range;

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
        chart.getScene().getGraph().add(surface);
        chart.getAxeLayout().setGridColor(Color.WHITE);
        chart.getAxeLayout().setTickLineDisplayed(false);
        chart.getAxeLayout().setXTickColor(Color.WHITE);
        chart.getAxeLayout().setYTickColor(Color.WHITE);
        chart.getAxeLayout().setZTickColor(Color.WHITE);
        chart.getView().setSquared(false);
        view = factory.bindImageView(chart);
        try {
            Thread t = new Thread() {
                public void run() {
                    try {
                        Thread.sleep(60000*3);
                        Shape detailedSurface=SurfaceLoader.loadSurface(meshs);
                        chart.getScene().getGraph().remove(surface);
                        chart.getScene().getGraph().add(detailedSurface);
                        surface=detailedSurface;
                    } catch (InterruptedException ex) {
                        Logger.getLogger(RenderModel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };
            t.setDaemon(true);
//            t.start();

        } catch (Exception ex) {
            Logger.getLogger(RenderModel.class.getName()).log(Level.SEVERE, null, ex);
        }
	maxX = chart.getView().getBounds().getXmax();
        minX = chart.getView().getBounds().getXmin();
        maxY = chart.getView().getBounds().getYmax();
        minY = chart.getView().getBounds().getYmin();
        maxZ = chart.getView().getBounds().getZmax();
        minZ = chart.getView().getBounds().getZmin();
        xcenter = chart.getView().getBounds().getCenter().x;
        ycenter = chart.getView().getBounds().getCenter().y;
        zcenter = chart.getView().getBounds().getCenter().z;
        range = (chart.getView().getBounds().getRange());

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

    public float getMinX() {
        return minX;
    }

    public float getMinY() {
        return minY;
    }

    public float getMinZ() {
        return minZ;
    }

    public Coord3d getRange() {
        return range;
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
        float zoom = factor / currentZoom;
        BoundingBox3d bounds = chart.getView().getBounds();
        System.out.println(bounds + ":" + bounds.getZRange());
        float xlen = (bounds.getRange().x) / zoom / 2;
//        float xcenter = bounds.getCenter().x;
        float ylen = (bounds.getRange().y) / zoom / 2;
//        float ycenter = bounds.getCenter().y;
        float zlen = (bounds.getRange().z) / zoom / 2;
//        float zcenter = bounds.getCenter().z;
        bounds.setZmin(-zlen);
        bounds.setZmax(zlen);
        bounds.setXmin(xcenter - xlen);
        bounds.setXmax(xcenter + xlen);
        bounds.setYmin(ycenter - ylen);
        bounds.setYmax(ycenter + ylen);
        currentZoom = factor;
        chart.getView().shoot();
        chart.getView().computeScaledViewBounds();
//        chart.getView().updateBoundsForceUpdate(true);//don't call this, this will reset rendering
    }

    public void moveX(float dir) {
        BoundingBox3d bounds = chart.getView().getBounds();
        float x = bounds.getRange().x / 10 * dir;
        if (1==1 || (!(bounds.getCenter().x > maxX && dir > 0) && !(bounds.getCenter().x < minX && dir < 0))) {
            bounds.setXmin(bounds.getXmin() + x);
            bounds.setXmax(bounds.getXmax() + x);
            chart.getView().shoot();
            chart.getView().computeScaledViewBounds();
        }
    }

    public void moveY(float dir) {
        BoundingBox3d bounds = chart.getView().getBounds();
        float y = bounds.getRange().y / 10 * dir;
        if (!(bounds.getCenter().y > maxY && dir > 0) && !(bounds.getCenter().y < minY && dir < 0)) {
            bounds.setYmin(bounds.getYmin() + y);
            bounds.setYmax(bounds.getYmax() + y);
            chart.getView().shoot();
            chart.getView().computeScaledViewBounds();
        }

    }

    public void moveZ(float dir) {
        BoundingBox3d bounds = chart.getView().getBounds();
        float z = bounds.getRange().z / 10 * dir;
        if (!(bounds.getCenter().z > maxZ && dir > 0) && !(bounds.getCenter().z < minZ && dir < 0)) {
            bounds.setZmin(bounds.getZmin() + z);
            bounds.setZmax(bounds.getZmax() + z);
            chart.getView().shoot();
            chart.getView().computeScaledViewBounds();
        }
    }

    public void move(float x, float y, float z) {
        BoundingBox3d bounds = chart.getView().getBounds();
        Coord3d range_ = bounds.getRange();
        bounds.setXmin(minX + range.x * x - range_.x / 2);
        bounds.setXmax(minX + range.x * x + range_.x / 2);
        bounds.setYmin(minY + range.y * y - range_.y / 2);
        bounds.setYmax(minY + range.y * y + range_.y / 2);
        bounds.setZmin(minZ + range.z * z - range_.z / 2);
        bounds.setZmax(minZ + range.z * z + range_.z / 2);
        chart.getView().shoot();
        chart.getView().computeScaledViewBounds();
    }

    public void repaint() {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                stage.requestFocus();
//                System.out.println(System.currentTimeMillis()+":before render");
                chart.render();
                if(stage.isMaximized()){
                    double maximizedWidth=stage.getWidth();
                    double maxmizedHeight=stage.getHeight();
                    stage.setMaximized(false);
                    stage.setWidth(maximizedWidth-1);
                    stage.setHeight(maxmizedHeight);
                    stage.setX(0);
                    stage.setY(0);
                }
//                System.out.println(System.currentTimeMillis()+":after render");
                if (stage.getWidth() % 10 == 0) {
                    stage.setWidth(stage.getWidth() - 1);
                } else {
                    stage.setWidth(stage.getWidth() + 1);
                }
            }
        });

    }
}
