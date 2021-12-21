/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jzy_3d_sample.datafactory.model;

import jzy_3d_sample.model.Mesh;
import jzy_3d_sample.model.os.OSRecord;

/**
 *
 * @author lendle
 */
public class MeshOSMatchingEntry {
    private Mesh mesh=null;
    private String meshKey=null;
    private String meshFuzzyKey=null;
    private OSRecord bestOSRecord=null;
    private double minDistance=-1;

    public Mesh getMesh() {
        return mesh;
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    public String getMeshKey() {
        return meshKey;
    }

    public void setMeshKey(String meshKey) {
        this.meshKey = meshKey;
    }

    public String getMeshFuzzyKey() {
        return meshFuzzyKey;
    }

    public void setMeshFuzzyKey(String meshFuzzyKey) {
        this.meshFuzzyKey = meshFuzzyKey;
    }

    public OSRecord getBestOSRecord() {
        return bestOSRecord;
    }

    public void setBestOSRecord(OSRecord bestOSRecord) {
        this.bestOSRecord = bestOSRecord;
    }

    public double getMinDistance() {
        return minDistance;
    }

    public void setMinDistance(double minDistance) {
        this.minDistance = minDistance;
    }
    
    
}
