/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jzy_3d_sample;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jzy_3d_sample.model.Mesh;
import jzy_3d_sample.model.Vertex;
import org.jzy3d.analysis.AbstractAnalysis;
import org.jzy3d.analysis.AnalysisLauncher;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Point;
import org.jzy3d.plot3d.primitives.Polygon;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.canvas.Quality;

/**
 *
 * @author user
 */
public class jzy3d extends AbstractAnalysis {

    List<Mesh> meshs = new ArrayList<>();

    public jzy3d(List<Mesh> meshs) {
        this.meshs = meshs;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Read_csvdata r = new Read_csvdata();
        List<Mesh> meshs = r.getdata(new File("./sample/cone_fine_point.csv"), new File("./sample/cone_fine_mesh.csv"));
//        List<Mesh> meshs=r.getdata(new File("./sample/chopper_point.csv"), new File("./sample/chopper_mesh.csv"));
//        List<Mesh> meshs=r.getdata(new File("./sample/sphere_point.csv"), new File("./sample/sphere_mesh.csv"));
//        List<Mesh> meshs=r.getdata(new File("./sample/cone_point.csv"), new File("./sample/cone_mesh.csv"));
        try {
            AnalysisLauncher.open(new jzy3d(meshs));
        } catch (Exception ex) {
            Logger.getLogger(jzy3d.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void init() throws Exception {
        List<Polygon> polygons = new ArrayList<Polygon>();
        System.out.println(meshs.size());
        for (int i = 0; i < meshs.size(); i++) {
            System.out.println(meshs.get(i));
            Polygon polygon = new Polygon();
            Vertex[] vertices = meshs.get(i).getVertices();
            for (int j = 0; j < 3; j++) {
                polygon.add(new Point(new Coord3d(
                        (float) vertices[j].getX(),
                        (float) vertices[j].getY(),
                        (float) vertices[j].getZ()
                )));
                polygon.setColor(Color.random());
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
