/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jzy_3d_sample.model.os;

/**
 *
 * @author lendle
 */
public class OSRecord {

    private String num = null;
    private String reC1 = null, reC2 = null, reC3 = null;
    private String imC1 = null, imC2 = null, imC3 = null;

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getReC1() {
        return reC1;
    }

    public void setReC1(String reC1) {
        this.reC1 = reC1;
    }

    public String getReC2() {
        return reC2;
    }

    public void setReC2(String reC2) {
        this.reC2 = reC2;
    }

    public String getReC3() {
        return reC3;
    }

    public void setReC3(String reC3) {
        this.reC3 = reC3;
    }

    public String getImC1() {
        return imC1;
    }

    public void setImC1(String imC1) {
        this.imC1 = imC1;
    }

    public String getImC2() {
        return imC2;
    }

    public void setImC2(String imC2) {
        this.imC2 = imC2;
    }

    public String getImC3() {
        return imC3;
    }

    public void setImC3(String imC3) {
        this.imC3 = imC3;
    }

    @Override
    public String toString() {
        return "OSRecord{" + "num=" + num + ", reC1=" + reC1 + ", reC2=" + reC2 + ", reC3=" + reC3 + ", imC1=" + imC1 + ", imC2=" + imC2 + ", imC3=" + imC3 + '}';
    }

    
}
