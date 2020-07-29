/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jzy_3d_sample.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jzy3d.maths.BoundingBox3d;
import org.jzy3d.maths.Coord3d;

/**
 *
 * @author lendle
 */
public class Cube extends BoundingBox3d{

    
    /**
     * @param vertices must be of length 8
     */
    public Cube(List<Vertex> vertices) {
        super(new ArrayList<Coord3d>(vertices));
    }
    
    public Cube(BoundingBox3d box) {
        super(box);
    }
    

    protected List<Vertex> getVerticesVertexs(){
        List<Vertex> ret=new ArrayList<>();
        for(Coord3d point : super.getVertices()){
            ret.add(new Vertex(point.x, point.y, point.z));
        }
        return ret;
    }

    public Vertex getSmallestVertex() {
        double xmin = Double.MAX_VALUE, ymin = Double.MAX_VALUE, zmin = Double.MAX_VALUE;
        for (Vertex v : getVerticesVertexs()) {
            xmin = Math.min(v.getX(), xmin);
            ymin = Math.min(v.getY(), ymin);
            zmin = Math.min(v.getZ(), zmin);
        }
        return new Vertex(xmin, ymin, zmin);
    }

    public Vertex getLargestVertex() {
        double xmax = Double.MIN_VALUE, ymax = Double.MIN_VALUE, zmax = Double.MIN_VALUE;
        for (Vertex v : getVerticesVertexs()) {
            xmax = Math.max(v.getX(), xmax);
            ymax = Math.max(v.getY(), ymax);
            zmax = Math.max(v.getZ(), zmax);
        }
        return new Vertex(xmax, ymax, zmax);
    }

    public List<Cube> slice(double length, boolean boundInclusiveIfNotMatch) {
        List<Cube> subCubes = new ArrayList<>();
        Vertex origin = getSmallestVertex();
        Vertex farthest = getLargestVertex();
        List<Vertex> visitingVertexs = new ArrayList<>();
        List<Vertex> visitedVertexs = new ArrayList<>();
        visitingVertexs.add(origin);
        while (!visitingVertexs.isEmpty()) {
            Vertex now = visitingVertexs.remove(0);
            if(visitedVertexs.contains(now)){
                continue;
            }
            visitedVertexs.add(now);
            double xnow = now.getX();
            double ynow = now.getY();
            double znow = now.getZ();
            List<Vertex> adjacencyList = Arrays.asList(
                    new Vertex(xnow + length, ynow, znow), new Vertex(xnow, ynow + length, znow), new Vertex(xnow, ynow, znow + length),
                    new Vertex(xnow, ynow + length, znow + length), new Vertex(xnow + length, ynow, znow + length), new Vertex(xnow + length, ynow + length, znow),
                    new Vertex(xnow + length, ynow + length, znow + length));
            boolean acceptCube = true;
            for (Vertex v : adjacencyList) {
                if (v.compareTo(farthest) < 0) {
                    //contained in the original bound
                    if (!visitedVertexs.contains(v)) {
                        visitingVertexs.add(v);
                    }
                } else {
                    //may be exceed, check boundInclusiveIfNotMatch
                    if (boundInclusiveIfNotMatch) {
                        //use bound instead
                        if (v.getX() > farthest.getX()) {
                            v.setX(farthest.getX());
                        }
                        if (v.getY() > farthest.getY()) {
                            v.setY(farthest.getY());
                        }
                        if (v.getZ() > farthest.getZ()) {
                            v.setZ(farthest.getZ());
                        }//but not visit further
                    } else {
                        //drop the cube
                        acceptCube = false;
                        break;
                    }
                }
            }
            if (acceptCube) {
                List<Vertex> vNewCube = new ArrayList<>();
                vNewCube.add(now);
                vNewCube.addAll(adjacencyList);
                subCubes.add(new Cube(vNewCube));
            }
        }

        return subCubes;
    }

    public String toString() {
        return (Arrays.deepToString(getVerticesVertexs().toArray()));
    }

    public static void main(String[] args) throws Exception {
        List<Vertex> list = Arrays.asList(
                new Vertex(0, 0, 0),
                new Vertex(2, 0, 0), new Vertex(0, 2, 0), new Vertex(0, 0, 2),
                new Vertex(0, 2, 2), new Vertex(2, 0, 2), new Vertex(2, 2, 0),
                new Vertex(2, 2, 2));
        Cube cube = new Cube(list);
        List<Cube> subCubes = cube.slice(1, true);
        System.out.println(subCubes.size());
        System.out.println(Arrays.deepToString(subCubes.toArray()));
    }
}
