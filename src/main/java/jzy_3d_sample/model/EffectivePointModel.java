/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jzy_3d_sample.model;

/**
 *
 * @author lendle
 */
public class EffectivePointModel implements Comparable<EffectivePointModel>{
    private Double x;
    private Double y;
    private Double z;
    private Double rcs;

    public EffectivePointModel(double x, double y, double z, double rcs) {
        this.x = x;
        this.y = y;
        this.z=z;
        this.rcs = rcs;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Double getRcs() {
        return rcs;
    }

    public void setRcs(double rcs) {
        this.rcs = rcs;
    }

    public Double getZ() {
        return z;
    }

    public void setZ(Double z) {
        this.z = z;
    }

    @Override
    public int compareTo(EffectivePointModel o) {
        if(o.rcs>rcs){
            return 1;
        }else{
            return -1;
        }
    }
    
    public Vertex getVertex(){
        return new Vertex(x, y, z);
    }
    
    @Override
    public String toString() {
        return "EffectivePointModel{" + "x=" + x + ", y=" + y + ", z=" + z + ", rcs=" + rcs + '}';
    }
    
    
}
