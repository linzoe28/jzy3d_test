package jzy_3d_sample;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jzy3d.analysis.AbstractAnalysis;
import org.jzy3d.analysis.AnalysisLauncher;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Point;
import org.jzy3d.plot3d.primitives.Polygon;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author user
 */
public class jzy3d_sample extends AbstractAnalysis {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            AnalysisLauncher.open(new jzy3d_sample());
        } catch (Exception ex) {
            Logger.getLogger(jzy3d_sample.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void init() throws Exception {
        List<Polygon> polygons = new ArrayList<Polygon>();
        double[][][] meshs = new double[][][]{
            {{1, 0.5, 0}, {1, -0.5, 0}, {-1, -0.5, 0}},
            {{-1, -0.5, 0}, {-1, 0.5, 0}, {1, 0.5, 0}}
        };
        for (int i = 0; i < meshs.length; i++) {
            Polygon polygon = new Polygon();
            double[][] mesh = meshs[i];
            for (int j = 0; j < 3; j++) {
                polygon.add(new Point(new Coord3d(
                        (float) mesh[j][0],
                        (float) mesh[j][1],
                        (float) mesh[j][2]
                )));
                
                polygons.add(polygon);
            }
        }
        // Create the object to represent the function over the given range.
        Shape surface = new Shape(polygons);
        //surface.setColorMapper(new ColorMapper(new ColorMapRainbow(), surface.getBounds().getZmin(), surface.getBounds().getZmax(), new org.jzy3d.colors.Color(1, 1, 1, 1f)));
        surface.setWireframeDisplayed(true);
        surface.setWireframeColor(org.jzy3d.colors.Color.BLACK);

        chart = AWTChartComponentFactory.chart(Quality.Intermediate, "awt");
        chart.getScene().getGraph().add(surface);


    }

}
