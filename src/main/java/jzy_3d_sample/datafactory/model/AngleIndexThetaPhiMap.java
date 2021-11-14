/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jzy_3d_sample.datafactory.model;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author lendle
 */
public class AngleIndexThetaPhiMap {
    private Map<String, String> angleIndex2ThetaPhi=new HashMap<>();
    private Map<String, String> thetaPhi2AngleIndex=new HashMap<>();
    
    public void putAngleIndex2ThetaPhi(String angleIndex, String thetaphi){
        angleIndex2ThetaPhi.put(angleIndex, thetaphi);
    }
    
    public void putThetaPhi2AngleIndex(String thetaphi, String angleIndex){
        thetaPhi2AngleIndex.put(thetaphi, angleIndex);
    }
    
    public String getThetaPhi(String angleIndex){
        return this.angleIndex2ThetaPhi.get(angleIndex);
    }
    
    public String getAngleIndex(String thetaphi){
        return this.thetaPhi2AngleIndex.get(thetaphi);
    }
}
