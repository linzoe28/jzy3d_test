/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jzy_3d_sample.model.meshconverter;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lendle
 */
public class SerializedStatus implements StatusProvider{
    public static final String VERSION="1.0";
    protected String version="1.0";
    protected boolean osFileSplitDone=false;
    protected List<String> osFilePaths=new ArrayList<>();
    //the index of finished current data, start from 0
    protected int currentDataSerializationProgress=-1;
    protected List<Integer> frequencies=new ArrayList<>();
    protected List<String> n2fProcessingQueuePaths=new ArrayList<>();

    @Override
    public List<String> getOsFilePaths() {
        return osFilePaths;
    }

    @Override
    public void setOsFilePaths(List<String> osFilePaths) {
        this.osFilePaths = osFilePaths;
    }

    
    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public boolean isOsFileSplitDone() {
        return osFileSplitDone;
    }

    @Override
    public void setOsFileSplitDone(boolean osFileSplitDone) {
        this.osFileSplitDone = osFileSplitDone;
    }


    @Override
    public int getCurrentDataSerializationProgress() {
        return currentDataSerializationProgress;
    }

    @Override
    public void setCurrentDataSerializationProgress(int currentDataSerializationProgress) {
        this.currentDataSerializationProgress = currentDataSerializationProgress;
    }

    @Override
    public List<Integer> getFrequencies() {
        return this.frequencies;
    }

    @Override
    public void setFrequencies(List<Integer> frequencies) {
        this.frequencies=frequencies;
    }

    public List<String> getN2fProcessingQueuePaths() {
        return n2fProcessingQueuePaths;
    }

    public void setN2fProcessingQueuePaths(List<String> n2fProcessingQueuePaths) {
        this.n2fProcessingQueuePaths = n2fProcessingQueuePaths;
    }
    
    
    
}
