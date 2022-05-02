/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jzy_3d_sample.model.os;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import jzy_3d_sample.utils.SerializeUtil;

/**
 *
 * @author lendle
 */
public class OSRecordMap implements Cloneable, Serializable {

    private static final long serialVersionUID = -1636927109633279805L;
    private Map<String, String> key2FileNameMap = new HashMap<>();//store osrecord key => file name mappings
    private Map<String, Map<String, String>> fuzzyKey2RealKeyMap = new HashMap<>();
    private Map<String, OSRecord> fastAccessMap = new HashMap<>();
    private List<String> osRecordFileNames = new ArrayList<>();
    private transient List<Map<String, OSRecord>> cache = new ArrayList<>();
    private transient File homeFolder = null;
    private long hit = 0, miss = 0;
    private int frequency=-1;

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
    
    

    public File getHomeFolder() {
        return homeFolder;
    }

    public Map<String, Map<String, String>> getFuzzyKey2RealKeyMap() {
        return fuzzyKey2RealKeyMap;
    }

    public Map<String, OSRecord> getFastAccessMap() {
        return fastAccessMap;
    }

    public void setFastAccessMap(Map<String, OSRecord> fastAccessMap) {
        this.fastAccessMap = fastAccessMap;
    }

    public List<String> getOsRecordFileNames() {
        return osRecordFileNames;
    }

    public List<String> getCandidateKeyFromFuzzyKey(String fuzzyKey) {
        return new ArrayList<>(fuzzyKey2RealKeyMap.get(fuzzyKey).keySet());
    }

    public void setHomeFolder(File homeFolder) {
        this.homeFolder = homeFolder;
    }

    public Map<String, String> getKey2FileNameMap() {
        return key2FileNameMap;
    }

    public void setKey2FileNameMap(Map<String, String> key2FileNameMap) {
        this.key2FileNameMap = key2FileNameMap;
    }

    public OSRecord get(String key) {
        try {
            return this.getOSRecord(key);
        } catch (Exception ex) {
            Logger.getLogger(OSRecordMap.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public long getHit() {
        return hit;
    }

    public long getMiss() {
        return miss;
    }

    public OSRecord getOSRecord(String key) throws Exception {
        OSRecord record = null;
        if(cache==null){
            cache=new ArrayList<>();
        }
        if (fastAccessMap.containsKey(key)) {
            hit++;
            return fastAccessMap.get(key);
        }
        for (Map<String, OSRecord> map : cache) {
            record = map.get(key);
            if (record != null) {
                hit++;
                break;
            }
        }
        miss++;
        if (record == null) {
            if (cache.size() >= MAX_CACHE_SIZE) {
                cache.remove(0);
            }
            String fileName = key2FileNameMap.get(key);
            if (fileName == null) {
                return null;
            }
            File file = new File(homeFolder, fileName);
            Map<String, OSRecord> map = (Map<String, OSRecord>) SerializeUtil.readFromFile(file);
            cache.add(map);
            record = map.get(key);
        }
        return record;
    }
    private static final int MAX_CACHE_SIZE = 4;

    public void remove(String key) {
        if (1 == 1) {
            return;
        }
        this.key2FileNameMap.remove(key);
    }

    public long size() {
        return this.key2FileNameMap.size();
    }

    public Map<String, OSRecord> load(String fileName) throws Exception {
        File file = new File(homeFolder, fileName);
        Map<String, OSRecord> map = (Map<String, OSRecord>) SerializeUtil.readFromFile(file);
        return map;
    }
}
