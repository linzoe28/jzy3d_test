
import java.util.ArrayList;
import java.util.List;
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
        AnalysisLauncher.open(new SurfaceDemo());
    }

    @Override
    public void init() throws Exception {
//        1	1.0	0.5	0.0	1.0	-0.5	0.0	-1.0	-0.5	0.0
//        2	-1.0	-0.5	0.0	-1.0	0.5	0.0	1.0	0.5	0.0

//        double[][] disDataProp = new double[][]{{1.0, 1.0, -1.0}, {0.5, -0.5, -0.5}, {0, 0, 0}};
//        List<Polygon> polygons = new ArrayList<>();
//        for (int i = 0; i < disDataProp.length - 1; i++) {
//            for (int j = 0; j < disDataProp[i].length - 1; j++) {
//                Polygon polygon = new Polygon();
//                polygon.add(new Point(new Coord3d(i, j, disDataProp[i][j])));
//                polygon.add(new Point(new Coord3d(i, j + 1, disDataProp[i][j + 1])));
//                polygon.add(new Point(new Coord3d(i + 1, j + 1, disDataProp[i + 1][j + 1])));
//                polygon.add(new Point(new Coord3d(i + 1, j, disDataProp[i + 1][j])));
//                polygons.add(polygon);
//            }
//        }
        Color[] colors = new Color[]{Color.BLUE, Color.CYAN, Color.GRAY, Color.GREEN, Color.MAGENTA};
        List<Polygon> polygons = new ArrayList<Polygon>();
        for (int i = 0; i < 1000; i++) {
            Polygon polygon = new Polygon();
            polygon.add(new Point(new Coord3d(Math.random() * 100, Math.random() * 100, Math.random() * 100)));
            polygon.add(new Point(new Coord3d(Math.random() * 100, Math.random() * 100, Math.random() * 100)));
            polygon.add(new Point(new Coord3d(Math.random() * 100, Math.random() * 100, Math.random() * 100)));
            polygon.setColor(colors[(int) Math.floor(Math.random() * colors.length)]);
            polygons.add(polygon);
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

