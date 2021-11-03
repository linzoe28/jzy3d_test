/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jzy_3d_sample.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.math3.complex.Complex;
import org.jzy3d.plot3d.primitives.Point;
import org.jzy3d.plot3d.primitives.Polygon;

/**
 *
 * @author user
 */
public class Mesh extends Polygon implements Cloneable, Serializable{
    private static final long serialVersionUID = -1636927109633279805L;
    private String osRecordKey=null;//used for mapping mesh to its corresponding os record
    private Vertex[] vertices=null;
    private Map<Vertex, VertexCurrent> currentMap=new HashMap<>();
    
    
    public Mesh(Vertex [] vertices){
        this.vertices=vertices;
        for(Vertex v : vertices){
            super.add(new Point(v));
        }
    }

    public String getOsRecordKey() {
        return osRecordKey;
    }

    public void setOsRecordKey(String osRecordKey) {
        this.osRecordKey = osRecordKey;
    }
    
    
    
    public void emptyCurrent(){
        for(Vertex vertex : vertices){
            this.setCurrent(vertex, new VertexCurrent(new Complex(0, 0), new Complex(0, 0), new Complex(0, 0)));
        }
    }

    public void setCurrent(Vertex vertex, VertexCurrent current){
        currentMap.put(vertex, current);
    }
    
    public void setCurrent(float x, float y, float z, VertexCurrent current){
        this.setCurrent(new Vertex(x, y, z), current);
    }
    
    public VertexCurrent getCurrent(Vertex vertex){
        return currentMap.get(vertex);
    }
    
    public VertexCurrent getCurrent(float x, float y, float z){
        return this.getCurrent(new Vertex(x, y, z));
    }
    
    public Vertex[] getVertices() {
        return vertices;
    }

    
    public Vertex getCenter(){
        return new Vertex(((double)vertices[0].x+vertices[1].x+vertices[2].x)/3, ((double)vertices[0].y+vertices[1].y+vertices[2].y)/3, ((double)vertices[0].z+vertices[1].z+vertices[2].z)/3);
    }
    
    public double getCurrentAbs(){
        Complex x=new Complex(0, 0);
        Complex y=new Complex(0, 0);
        Complex z=new Complex(0, 0);
        for(VertexCurrent c : this.currentMap.values()){
            x.add(c.getX());
            y.add(c.getY());
            z.add(c.getZ());
        }
        x.add(y);
        x.add(z);
        return x.abs();
    }

    @Override
    protected Mesh clone() throws CloneNotSupportedException {
        Vertex [] newVertices=new Vertex[this.vertices.length];
        for(int i=0; i<newVertices.length; i++){
            newVertices[i]=(Vertex) this.vertices[i].clone();
        }
        Mesh newMesh=new Mesh(newVertices);
        for(Vertex v : newVertices){
            newMesh.setCurrent(v, (VertexCurrent) currentMap.get(v).clone());
        }
        return newMesh;
    }
    
    public List<Edge> getEdges(){
        return Arrays.asList(
                new Edge(this.vertices[0], this.vertices[1]),
                new Edge(this.vertices[0], this.vertices[2]),
                new Edge(this.vertices[1], this.vertices[2])
        );
    }
}
