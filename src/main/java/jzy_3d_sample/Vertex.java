/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jzy_3d_sample;

/**
 *
 * @author user
 */
public class Vertex {
    private double x,y,z;
    
    public Vertex(double x, double y, double z){
        this.x=x;
        this.y=y;
        this.z=z;
    }
    
    public Vertex(double [] xyz){
        this.x=xyz[0];
        this.y=xyz[1];
        this.z=xyz[2];
    }

    public Vertex() {
    }
    
    
    
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }
    
}
