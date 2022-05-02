/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jzy_3d_sample.model.meshconverter;

import java.util.List;

/**
 *
 * @author lendle
 */
public interface StatusProvider {

    int getCurrentDataSerializationProgress();

    List<String> getOsFilePaths();

    String getVersion();

    boolean isOsFileSplitDone();

    void setCurrentDataSerializationProgress(int currentDataSerializationProgress);

    void setOsFilePaths(List<String> osFilePaths);

    void setOsFileSplitDone(boolean osFileSplitDone);

    void setVersion(String version);
    
    List<Integer> getFrequencies();
    void setFrequencies(List<Integer> frequencies);
    
    List<String> getN2fProcessingQueuePaths();
    void setN2fProcessingQueuePaths(List<String> paths);
    
}
