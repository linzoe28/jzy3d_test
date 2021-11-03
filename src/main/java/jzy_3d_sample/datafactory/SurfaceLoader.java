/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jzy_3d_sample.datafactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import jzy_3d_sample.model.Edge;
import jzy_3d_sample.model.Mesh;
import jzy_3d_sample.model.Vertex;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Polygon;
import org.jzy3d.plot3d.primitives.Shape;

/**
 *
 * @author lendle
 */
public class SurfaceLoader {

    public static Shape loadSurface(List<Mesh> meshes) {
//        Map<Edge, List<Mesh>> edge2Mesh = new HashMap<>();
//        for (Mesh m : meshes) {
//            List<Edge> edges = m.getEdges();
//            for (Edge edge : edges) {
//                List<Mesh> list = edge2Mesh.get(edge);
//                if (list == null) {
//                    list = new ArrayList<>();
//                    edge2Mesh.put(edge, list);
//                }
//                list.add(m);
//            }
//        }
//
//        List<Polygon> preserved = new ArrayList<>();
//        Map<Mesh, String> consumedMesh = new HashMap<>();
//        outer:
//        for (List<Mesh> ms : edge2Mesh.values()) {
//            Set<Vertex> vertices = new HashSet<>();
//            Mesh m1 = ms.get(0);
//            Mesh m2 = ms.get(1);
//            if (consumedMesh.containsKey(m1) == false && consumedMesh.containsKey(m2) == false) {
//                for (Mesh m : ms) {
//                    consumedMesh.put(m, "");
//                    vertices.add(m.getVertices()[0]);
//                    vertices.add(m.getVertices()[1]);
//                    vertices.add(m.getVertices()[2]);
//                }
//                Polygon newPolygon = new Polygon();
//                for (Vertex v : vertices) {
//                    newPolygon.add(v);
//                }
//                preserved.add(newPolygon);
//            }else if(consumedMesh.containsKey(m1) == false){
//                consumedMesh.put(m1, "");
//                preserved.add(m1);
//            }else if(consumedMesh.containsKey(m2) == false){
//                consumedMesh.put(m2, "");
//                preserved.add(m2);
//            }
//        }
//
//        System.out.println(preserved.size() + ":" + meshes.size());
//        Shape surface = new Shape(new ArrayList<Polygon>(preserved));
        Shape surface = new Shape(new ArrayList<Polygon>(meshes));
        surface.setWireframeDisplayed(true);
        surface.setFaceDisplayed(true);
        surface.setWireframeColor(org.jzy3d.colors.Color.BLUE);
        return surface;
    }
}
