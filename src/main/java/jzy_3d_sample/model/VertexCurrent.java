/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jzy_3d_sample.model;

import java.io.Serializable;
import org.apache.commons.math3.complex.Complex;

/**
 *
 * @author lendle
 */
public class VertexCurrent implements Cloneable, Serializable{
    private static final long serialVersionUID = -1636927109633279805L;
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

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new VertexCurrent(x, y, z);
    }
    
    

}
