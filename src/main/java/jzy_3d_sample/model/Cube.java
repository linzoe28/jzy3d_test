/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jzy_3d_sample.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jzy3d.maths.BoundingBox3d;
import org.jzy3d.maths.Coord3d;

/**
 *
 * @author lendle
 */
public class Cube extends BoundingBox3d{

    private List<Mesh> meshs=new ArrayList<>();
    private double rcs;

    public double getRcs() {
        return rcs;
    }

    public void setRcs(double rcs) {
        this.rcs = rcs;
    }
    /**
     * @param vertices must be of length 8
     */
    public Cube(List<Vertex> vertices, List<Mesh> meshs) {
        super(new ArrayList<Coord3d>(vertices));
        this.meshs.addAll(meshs);
    }
    
    public Cube(BoundingBox3d box, List<Mesh> meshs) {
        super(box);
        this.meshs.addAll(meshs);
    }
    
    public Cube(List<Vertex> vertices) {
        this(vertices, new ArrayList<Mesh>());
    }
    
    public Cube(BoundingBox3d box) {
        this(box, new ArrayList<Mesh>());
    }

    public List<Mesh> getMeshs() {
        return meshs;
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
    
//    public List<Cube> slice(double length) {
//        return this.slice(length, true);
//    }
    /**
     * create a vertex in the cube
     * the method will check bounds
     * @param x
     * @param y
     * @param z
     * @return 
     */
    private Vertex createVertexInCube(double x, double y, double z){
        x=(x<this.getXmin())?this.getXmin():x;
        x=(x>this.getXmax())?this.getXmax():x;
        y=(y<this.getYmin())?this.getYmin():y;
        y=(y>this.getYmax())?this.getYmax():y;
        z=(z<this.getZmin())?this.getZmin():z;
        z=(z>this.getZmax())?this.getZmax():z;
        return new Vertex(x, y, z);
    }
    
    public List<Cube> slice(double xParts, double yParts, double zParts){
        double xLength=this.getRange().x;
        double yLength=this.getRange().y;
        double zLength=this.getRange().z;
        double xUnit=xLength/xParts;
        double yUnit=yLength/yParts;
        double zUnit=zLength/zParts;
        List<Mesh> localMeshs=new ArrayList<>(this.meshs);
        List<Cube> subCubes = new ArrayList<>();
        for(int i=0; i<xParts; i++){
            for(int j=0; j<yParts; j++){
                for(int k=0; k<zParts; k++){
                    double [] origin=new double[]{this.getXmin()+i*xUnit, this.getYmin()+j*yUnit, this.getZmin()+k*zUnit};
                    List<Vertex> subVertices=Arrays.asList(
                            createVertexInCube(origin[0], origin[1], origin[2]),
                            createVertexInCube(origin[0]+xUnit, origin[1], origin[2]),
                            createVertexInCube(origin[0], origin[1]+yUnit, origin[2]),
                            createVertexInCube(origin[0], origin[1], origin[2]+zUnit),
                            createVertexInCube(origin[0]+xUnit, origin[1]+yUnit, origin[2]),
                            createVertexInCube(origin[0], origin[1]+yUnit, origin[2]+zUnit),
                            createVertexInCube(origin[0]+xUnit, origin[1], origin[2]+zUnit),
                            createVertexInCube(origin[0]+xUnit, origin[1]+yUnit, origin[2]+zUnit)
                    );
                    subCubes.add(new Cube(subVertices));
                }
            }
        }
        Cube lastCube=null;
        outer: for(Mesh mesh : localMeshs){
            if(lastCube!=null && lastCube.contains(mesh.getCenter())){
                lastCube.getMeshs().add(mesh);
                continue;
            }
            for(Cube cube : subCubes){
                if(cube.contains(mesh.getCenter())){
                    lastCube=cube;
                    cube.getMeshs().add(mesh);
//                    System.out.println(cube);
                    continue outer;
                }
            }
        }
//        for(Cube cube : subCubes){
//            List<Mesh> consumedMeshs=new ArrayList<>();
//            for(Mesh mesh : localMeshs){
//                if(cube.contains(mesh.getCenter())){
//                    cube.getMeshs().add(mesh);
//                    consumedMeshs.add(mesh);
//                }
//            }
//            localMeshs.removeAll(consumedMeshs);
//        }
        return subCubes;
    }
    
//    public List<Cube> slice(double length, boolean boundInclusiveIfNotMatch) {
//        List<Mesh> localMeshs=new ArrayList<>(this.meshs);
//        List<Cube> subCubes = new ArrayList<>();
//        Vertex origin = getSmallestVertex();
//        Vertex farthest = getLargestVertex();
//        List<Vertex> visitingVertexs = new ArrayList<>();
//        List<Vertex> visitedVertexs = new ArrayList<>();
//        visitingVertexs.add(origin);
//        while (!visitingVertexs.isEmpty()) {
//            Vertex now = visitingVertexs.remove(0);
//            //System.out.println(now);
//            if(visitedVertexs.contains(now)){
//                continue;
//            }
//            visitedVertexs.add(now);
//            double xnow = now.getX();
//            double ynow = now.getY();
//            double znow = now.getZ();
//            List<Vertex> adjacencyList = Arrays.asList(
//                    new Vertex(xnow + length, ynow, znow), new Vertex(xnow, ynow + length, znow), new Vertex(xnow, ynow, znow + length),
//                    new Vertex(xnow, ynow + length, znow + length), new Vertex(xnow + length, ynow, znow + length), new Vertex(xnow + length, ynow + length, znow),
//                    new Vertex(xnow + length, ynow + length, znow + length));
//            boolean acceptCube = true;
//            for (Vertex v : adjacencyList) {
//                //System.out.println("visited: "+v +":" +v.compareTo(farthest)+":"+farthest);
//                if (v.compareTo(farthest) < 0) {
//                    //contained in the original bound
//                    if (!visitedVertexs.contains(v)) {
//                        visitingVertexs.add(v);
//                    }
//                } else {
//                    //may be exceed, check boundInclusiveIfNotMatch
//                    if (boundInclusiveIfNotMatch) {
//                        //use bound instead
//                        if (v.getX() > farthest.getX()) {
//                            v.setX(farthest.getX());
//                        }
//                        if (v.getY() > farthest.getY()) {
//                            v.setY(farthest.getY());
//                        }
//                        if (v.getZ() > farthest.getZ()) {
//                            v.setZ(farthest.getZ());
//                        }//but not visit further
//                    } else {
//                        //drop the cube
//                        acceptCube = false;
//                        break;
//                    }
//                }
//            }
//            //require at least 4 distinct points (a plane)
//            Set<Vertex> vertexSet=new HashSet<>(adjacencyList);
//            if(vertexSet.size()<4){
//                acceptCube=false;
//            }
//            if (acceptCube) {
//                List<Vertex> vNewCube = new ArrayList<>();
//                List<Mesh> assignedMeshs=new ArrayList<>();
//                vNewCube.add(now);
//                vNewCube.addAll(adjacencyList);
//                Cube newCube=new Cube(vNewCube);
//                //scan for meshs
//                for(Mesh mesh : localMeshs){
//                    if(newCube.contains(mesh.getCenter())){
//                        assignedMeshs.add(mesh);
//                        newCube.getMeshs().add(mesh);
//                    }
//                }
//                localMeshs.removeAll(assignedMeshs);
//                subCubes.add(newCube);
//            }
//        }
//
//        return subCubes;
//    }

    public String toString() {
        return (Arrays.deepToString(getVerticesVertexs().toArray()));
    }

    public static void main(String[] args) throws Exception {
        List<Vertex> list = Arrays.asList(
                new Vertex(0, 0, 0),
                new Vertex(2, 0, 0), new Vertex(0, 2, 0), new Vertex(0, 0, 2),
                new Vertex(0, 2, 2), new Vertex(2, 0, 2), new Vertex(2, 2, 0),
                new Vertex(2, 2, 2));
        Cube cube = new Cube(list, new ArrayList<>());
        List<Cube> subCubes = cube.slice(1, 1, 1);
        System.out.println(subCubes.size());
        System.out.println(Arrays.deepToString(subCubes.toArray()));
    }
}
