/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jzy_3d_sample.model;

import org.apache.commons.math3.complex.Complex;

/**
 *
 * @author lendle
 */
public class VertexCurrent {

    private Complex x, y, z = null;

    public VertexCurrent(Complex x, Complex y, Complex z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Complex getX() {
        return x;
    }

    public void setX(Complex x) {
        this.x = x;
    }

    public Complex getY() {
        return y;
    }

    public void setY(Complex y) {
        this.y = y;
    }

    public Complex getZ() {
        return z;
    }

    public void setZ(Complex z) {
        this.z = z;
    }

}
