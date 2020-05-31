/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jzy_3d_sample;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.jzy3d.chart.AWTChart;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.javafx.JavaFXChartFactory;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Point;
import org.jzy3d.plot3d.primitives.Polygon;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.canvas.Quality;
/**
 *
 * @author user
 */
public class NewFXMain extends Application  {
   @Override
    public void start(Stage primaryStage) {
        List<Polygon> polygons = new ArrayList<Polygon>();
        double [][][] meshs=new double[][][]{
            {{-1,-0.5,0}, {1,-0.5,0}, {1,0.5,0}},
            {{-1,-0.5,0}, {1,0.5,0}, {-1,0.5,0}}
        };
        for(int i=0; i<meshs.length; i++){
            Polygon polygon = new Polygon();
            double [][] mesh=meshs[i];
            for(int j=0; j<3; j++){
                polygon.add(new Point(new Coord3d(
                    (float)mesh[j][0],
                    (float)mesh[j][1],
                    (float)mesh[j][2]
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

        JavaFXChartFactory factory=new JavaFXChartFactory();
        AWTChart chart = (AWTChart) factory.newChart(Quality.Intermediate, "offscreen");
        chart.getScene().getGraph().add(surface);
        ImageView view=factory.bindImageView(chart);
        
        StackPane root = new StackPane();
        root.getChildren().add(view);
        
        Scene scene = new Scene(root, 800, 600);
        factory.addSceneSizeChangedListener(chart, scene);
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
