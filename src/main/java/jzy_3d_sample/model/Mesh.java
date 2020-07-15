/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jzy_3d_sample.model;

import org.apache.commons.math3.complex.Complex;
import org.jzy3d.plot3d.primitives.Point;
import org.jzy3d.plot3d.primitives.Polygon;

/**
 *
 * @author user
 */
public class Mesh extends Polygon{

    private Vertex[] vertices=null;
    private Complex rcs;
    
    public Mesh(Vertex [] vertices){
        this.vertices=vertices;
        for(Vertex v : vertices){
            super.add(new Point(v));
        }
    }

    public Vertex[] getVertices() {
        return vertices;
    }

    public Complex getRcs() {
        return rcs;
    }

    public void setRcs(Complex rcs) {
        this.rcs = rcs;
    }
}
