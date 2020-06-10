/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jzy_3d_sample;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.jzy3d.chart.AWTChart;
import org.jzy3d.colors.Color;
import org.jzy3d.javafx.JavaFXChartFactory;
import org.jzy3d.maths.BoundingBox3d;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.LineStrip;
import org.jzy3d.plot3d.primitives.Point;
import org.jzy3d.plot3d.primitives.Polygon;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.canvas.Quality;

/**
 *
 * @author user
 */
public class NewFXMain1 extends Application {

    @Override
    public void start(Stage primaryStage) {
        Read_csvdata r=new Read_csvdata();
        List<Mesh> meshs= r.getdata(new File("./sample/cone_point.csv"), new File("./sample/cone_mesh.csv"));
        
        List<Polygon> polygons = new ArrayList<Polygon>();
//        double[][][] meshs = new double[][][]{
//            {{-1, -0.5, 0}, {1, -0.5, 0}, {1, 0.5, 0}},
//            {{-1, -0.5, 0}, {1, 0.5, 0}, {-1, 0.5, 0}}
//        };
        for (int i = 0; i < meshs.size(); i++) {
            Polygon polygon = new Polygon();
//            double[][] mesh = meshs[i];
            Vertex [] vertices=meshs.get(i).getVertices();
            for (int j = 0; j < 3; j++) {
                polygon.add(new Point(new Coord3d(
//                        (float) mesh[j][0],
//                        (float) mesh[j][1],
//                        (float) mesh[j][2]
                        (float) vertices[j].getX(),
                        (float) vertices[j].getY(),
                        (float) vertices[j].getZ()
                )));
                polygons.add(polygon);
            }
        }
        // Create the object to represent the function over the given range.
        Shape surface = new Shape(polygons);
        //surface.setColorMapper(new ColorMapper(new ColorMapRainbow(), surface.getBounds().getZmin(), surface.getBounds().getZmax(), new org.jzy3d.colors.Color(1, 1, 1, 1f)));
        surface.setWireframeDisplayed(true);
        surface.setFaceDisplayed(true);
        surface.setWireframeColor(org.jzy3d.colors.Color.BLACK);

        JavaFXChartFactory factory = new JavaFXChartFactory();
        AWTChart chart = (AWTChart) factory.newChart(Quality.Intermediate, "offscreen");
        chart.getScene().getGraph().add(surface);
        BoundingBox3d boundingBox3d=surface.getBounds();
        List<Float> xSteps=range(boundingBox3d.getXmin(), boundingBox3d.getXmax(), 5);
        List<Float> ySteps=range(boundingBox3d.getYmin(), boundingBox3d.getYmax(), 5);
        List<Float> zSteps=range(boundingBox3d.getZmin(), boundingBox3d.getZmax(), 5);
//        LineStrip lineStrip=new LineStrip(new Point(new Coord3d(0,0 , 0), Color.RED), new Point(new Coord3d(0, 0.5, 0.5), Color.RED));
//        chart.getScene().getGraph().add(lineStrip);
        for(float x: xSteps){
            for(float y: ySteps){
                LineStrip lineStrip=new LineStrip(new Point(new Coord3d(x, y, boundingBox3d.getZmin()), Color.RED), new Point(new Coord3d(x,y,boundingBox3d.getZmax()), Color.RED));
                chart.getScene().getGraph().add(lineStrip);
            }
        }
        for(float x: xSteps){
            for(float z: zSteps){
                LineStrip lineStrip=new LineStrip(new Point(new Coord3d(x, boundingBox3d.getYmin(), z), Color.RED), new Point(new Coord3d(x,boundingBox3d.getYmax(),z), Color.RED));
                chart.getScene().getGraph().add(lineStrip);
            }
        }
        for(float y: ySteps){
            for(float z: zSteps){
                LineStrip lineStrip=new LineStrip(new Point(new Coord3d(boundingBox3d.getXmin(), y, z), Color.RED), new Point(new Coord3d(boundingBox3d.getXmax(),y,z), Color.RED));
                chart.getScene().getGraph().add(lineStrip);
            }
        }
        ImageView view = factory.bindImageView(chart);

        StackPane root = new StackPane();
        root.getChildren().add(view);

        Scene scene = new Scene(root, 800, 600);
        factory.addSceneSizeChangedListener(chart, scene);

        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    private List<Float> range(float min, float max, int segments){
        float step=(max-min)/segments;
        List<Float> ret=new ArrayList<>();
        for(int i=0; i<=segments; i++){
            ret.add(min+i*step);
        }
        return ret;
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
