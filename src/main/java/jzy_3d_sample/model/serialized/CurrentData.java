/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jzy_3d_sample.model.serialized;

import java.io.Serializable;
import java.util.Map;
import jzy_3d_sample.model.os.OSRecord;
import jzy_3d_sample.model.os.OSRecordMap;

/**
 *
 * @author lendle
 */
public class CurrentData implements Cloneable, Serializable{
    private static final long serialVersionUID = -1636927109633279805L;
    private double theta=-1, phi=-1;
    private OSRecordMap osRecordsMap=null;
    private double [] rcs=null;
    private double rcsTotal=-1;

    public double getRcsTotal() {
        return rcsTotal;
    }

    public void setRcsTotal(double rcsTotal) {
        this.rcsTotal = rcsTotal;
    }
    
    

    public double[] getRcs() {
        return rcs;
    }

    public void setRcs(double[] rcs) {
        this.rcs = rcs;
    }
    
    

    public double getTheta() {
        return theta;
    }

    public void setTheta(double theta) {
        this.theta = theta;
    }

    public double getPhi() {
        return phi;
    }

    public void setPhi(double phi) {
        this.phi = phi;
    }

    public OSRecordMap getOsRecordsMap() {
        return osRecordsMap;
    }

    public void setOsRecordsMap(OSRecordMap osRecordsMap) {
        this.osRecordsMap = osRecordsMap;
    }
    
    
}
