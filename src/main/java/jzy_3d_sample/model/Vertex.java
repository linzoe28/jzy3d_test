/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jzy_3d_sample.model;

import org.jzy3d.maths.Coord3d;

/**
 *
 * @author user
 */
public class Vertex extends Coord3d implements Comparable<Vertex>, Cloneable{
    public Vertex(float x, float y, float z){
        super(x, y, z);
    }
    
    public Vertex(double x, double y, double z){
        super((float)x, (float)y, (float)z);
    }
    
    public Vertex(float [] xyz){
        super(xyz[0], xyz[1], xyz[2]);
    }
    
    public Vertex(double [] xyz){
        super((float)xyz[0], (float)xyz[1], (float)xyz[2]);
    }

    public Vertex() {
    }
    
    
    
    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
        hash = 29 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
        hash = 29 * hash + (int) (Double.doubleToLongBits(this.z) ^ (Double.doubleToLongBits(this.z) >>> 32));
        return hash;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Vertex other = (Vertex) obj;
        if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x)) {
            return false;
        }
        if (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(other.y)) {
            return false;
        }
        if (Double.doubleToLongBits(this.z) != Double.doubleToLongBits(other.z)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Vertex o) {
        if(x==o.getX() && y==o.getY() && z==o.getZ()){
            return 0;
        }else if(x<=o.getX() && y<=o.getY() && z<=o.getZ()){
            return -1;
        }else{
            return 1;
        }
    }

    public String toString(){
        return "("+x+","+y+","+z+")";
    }

    @Override
    public Coord3d clone() {
        return new Vertex(x, y, z);
    }
    
    
}
