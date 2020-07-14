/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jzy_3d_sample.model;

import org.apache.commons.math3.complex.Complex;

/**
 *
 * @author user
 */
public class Mesh {

    private Vertex[] vertices=null;
    private Complex rcs;

    public Vertex[] getVertices() {
        return vertices;
    }

    public void setVertices(Vertex[] vertices) {
        this.vertices = vertices;
    }
    

    public Complex getRcs() {
        return rcs;
    }

    public void setRcs(Complex rcs) {
        this.rcs = rcs;
    }
}
