/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jzy_3d_sample.model.os;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;

/**
 *
 * @author lendle
 */
public class OSRecord implements Cloneable, Serializable{
    private static final long serialVersionUID = -1636927109633279805L;
    private String num = null;
    private String key=null;
    private String reC1X = null, reC2X = null, reC3X = null;
    private String imC1X = null, imC2X = null, imC3X = null;
    private String reC1Y = null, reC2Y = null, reC3Y = null;
    private String imC1Y = null, imC2Y = null, imC3Y = null;
    private String reC1Z = null, reC2Z = null, reC3Z = null;
    private String imC1Z = null, imC2Z = null, imC3Z = null;
    private double x=-1, y=-1, z=-1;

    public static void serialize2File(Map<String, OSRecord> osRecords, File file) throws Exception{
        try(ObjectOutputStream output=new ObjectOutputStream(new FileOutputStream(file))){
            output.writeObject(osRecords);
            output.flush();
        }
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

   
    

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    
    
    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getReC1X() {
        return reC1X;
    }

    public void setReC1X(String reC1X) {
        this.reC1X = reC1X;
    }

    public String getReC2X() {
        return reC2X;
    }

    public void setReC2X(String reC2X) {
        this.reC2X = reC2X;
    }

    public String getReC3X() {
        return reC3X;
    }

    public void setReC3X(String reC3X) {
        this.reC3X = reC3X;
    }

    public String getImC1X() {
        return imC1X;
    }

    public void setImC1X(String imC1X) {
        this.imC1X = imC1X;
    }

    public String getImC2X() {
        return imC2X;
    }

    public void setImC2X(String imC2X) {
        this.imC2X = imC2X;
    }

    public String getImC3X() {
        return imC3X;
    }

    public void setImC3X(String imC3X) {
        this.imC3X = imC3X;
    }

    public String getReC1Y() {
        return reC1Y;
    }

    public void setReC1Y(String reC1Y) {
        this.reC1Y = reC1Y;
    }

    public String getReC2Y() {
        return reC2Y;
    }

    public void setReC2Y(String reC2Y) {
        this.reC2Y = reC2Y;
    }

    public String getReC3Y() {
        return reC3Y;
    }

    public void setReC3Y(String reC3Y) {
        this.reC3Y = reC3Y;
    }

    public String getImC1Y() {
        return imC1Y;
    }

    public void setImC1Y(String imC1Y) {
        this.imC1Y = imC1Y;
    }

    public String getImC2Y() {
        return imC2Y;
    }

    public void setImC2Y(String imC2Y) {
        this.imC2Y = imC2Y;
    }

    public String getImC3Y() {
        return imC3Y;
    }

    public void setImC3Y(String imC3Y) {
        this.imC3Y = imC3Y;
    }

    public String getReC1Z() {
        return reC1Z;
    }

    public void setReC1Z(String reC1Z) {
        this.reC1Z = reC1Z;
    }

    public String getReC2Z() {
        return reC2Z;
    }

    public void setReC2Z(String reC2Z) {
        this.reC2Z = reC2Z;
    }

    public String getReC3Z() {
        return reC3Z;
    }

    public void setReC3Z(String reC3Z) {
        this.reC3Z = reC3Z;
    }

    public String getImC1Z() {
        return imC1Z;
    }

    public void setImC1Z(String imC1Z) {
        this.imC1Z = imC1Z;
    }

    public String getImC2Z() {
        return imC2Z;
    }

    public void setImC2Z(String imC2Z) {
        this.imC2Z = imC2Z;
    }

    public String getImC3Z() {
        return imC3Z;
    }

    public void setImC3Z(String imC3Z) {
        this.imC3Z = imC3Z;
    }

    @Override
    public String toString() {
        return "OSRecord{" + "num=" + num + ", reC1X=" + reC1X + ", reC2X=" + reC2X + ", reC3X=" + reC3X + ", imC1X=" + imC1X + ", imC2X=" + imC2X + ", imC3X=" + imC3X + ", reC1Y=" + reC1Y + ", reC2Y=" + reC2Y + ", reC3Y=" + reC3Y + ", imC1Y=" + imC1Y + ", imC2Y=" + imC2Y + ", imC3Y=" + imC3Y + ", reC1Z=" + reC1Z + ", reC2Z=" + reC2Z + ", reC3Z=" + reC3Z + ", imC1Z=" + imC1Z + ", imC2Z=" + imC2Z + ", imC3Z=" + imC3Z + '}';
    }

    
}
