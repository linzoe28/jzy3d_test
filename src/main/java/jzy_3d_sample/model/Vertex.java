/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jzy_3d_sample.model;

/**
 *
 * @author user
 */
public class Vertex implements Comparable<Vertex>{
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
        }else if(x<o.getX() && y<o.getY() && z<o.getZ()){
            return -1;
        }else{
            return 1;
        }
    }

    public String toString(){
        return "("+x+","+y+","+z+")";
    }
}
