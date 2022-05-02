/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jzy_3d_sample.model.meshconverter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lendle
 */
public class Status implements StatusProvider{
    private SerializedStatus s=null;
    private List<File> osFiles=null;
    private List<File> n2fProcessingQueue=null; 
    public Status(SerializedStatus s){
        this.s=s;
        osFiles=new File2StringList(s.getOsFilePaths());
        n2fProcessingQueue=new File2StringList(s.getN2fProcessingQueuePaths());
    }
    
    public List<File> getOsFiles(){
        return osFiles;
    }
    
    public void setOsFiles(List<File> files) throws IOException{
        osFiles.clear();
        osFiles.addAll(files);
    }
    
    @Override
    public int getCurrentDataSerializationProgress() {
        return s.getCurrentDataSerializationProgress();
    }

    @Override
    public List<String> getOsFilePaths() {
        return s.getOsFilePaths();
    }

    @Override
    public String getVersion() {
        return s.getVersion();
    }

    @Override
    public boolean isOsFileSplitDone() {
        return s.isOsFileSplitDone();
    }

    @Override
    public void setCurrentDataSerializationProgress(int currentDataSerializationProgress) {
        s.setCurrentDataSerializationProgress(currentDataSerializationProgress);
    }

    @Override
    public void setOsFilePaths(List<String> osFilePaths) {
        s.setOsFilePaths(osFilePaths);
    }

    @Override
    public void setOsFileSplitDone(boolean osFileSplitDone) {
        s.setOsFileSplitDone(osFileSplitDone);
    }

    @Override
    public void setVersion(String version) {
        s.setVersion(version);
    }

    public SerializedStatus getS() {
        return s;
    }

    public void setS(SerializedStatus s) {
        this.s = s;
    }

    @Override
    public List<Integer> getFrequencies() {
        return s.getFrequencies();
    }

    @Override
    public void setFrequencies(List<Integer> frequencies) {
        this.s.setFrequencies(frequencies);
    }

    @Override
    public List<String> getN2fProcessingQueuePaths() {
        return s.getN2fProcessingQueuePaths();
    }

    @Override
    public void setN2fProcessingQueuePaths(List<String> paths) {
        this.s.setN2fProcessingQueuePaths(paths);
    }
    
    public List<File> getN2fProcessingQueue() {
        return this.n2fProcessingQueue;
    }

    public void setN2fProcessingQueue(List<File> paths) throws IOException {
        this.n2fProcessingQueue.clear();
        this.n2fProcessingQueue.addAll(paths);
    }
}
