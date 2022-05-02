/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jzy_3d_sample.datafactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import jzy_3d_sample.Commons;
import jzy_3d_sample.model.os.OSRecord;
import jzy_3d_sample.model.os.OSRecordMap;
import jzy_3d_sample.utils.SerializeUtil;
import jzy_3d_sample.utils.ThreadUtils;

/**
 *
 * @author lendle
 */
public class OSFileParser {

    public static OSRecordMap readSerializedOSFile(File osMapFile) throws IOException, ClassNotFoundException {
        return (OSRecordMap) SerializeUtil.readFromFile(osMapFile);
//        try(ObjectInputStream input=new ObjectInputStream(new FileInputStream(osMapFile))){
//            return (OSRecordMap) input.readObject();
//        }
    }

    /**
     * read a os file, return a map from mesh number to OSRecord
     *
     * @return
     */
    public static OSRecordMap readOSFile(File outputFolder, File osFile, String outputPrefix) throws IOException {
        OSRecordMap oSRecordMap = new OSRecordMap();
        oSRecordMap.setHomeFolder(outputFolder);
        HashMap<String, OSRecord> ret = new HashMap<>();
        if (osFile == null) {
            return oSRecordMap;
        }
        int osRecordMapIndex = 0;
        String currentFileName = outputPrefix + "_" + osRecordMapIndex + ".osRecordMap";
        System.out.println("currentFileName=" + currentFileName);
        oSRecordMap.getOsRecordFileNames().add(currentFileName);
        try (BufferedReader input = new BufferedReader(new FileReader(osFile))) {
            String line = null;
            boolean firstRecordGot = false;
            while ((line = input.readLine()) != null) {
                if (ThreadUtils.isInterrupted()) {
                    throw new IOException(new RuntimeException("interrupted"));
                }
                line = line.trim();
                if (line.startsWith("#Frequency:")) {
                    String[] row = line.split(" +");
                    int frequency = (int) (Double.valueOf(row[1]) / 1000000);
                    oSRecordMap.setFrequency(frequency);
                } else if (line.length() > 0 && line.startsWith("#") == false && line.startsWith("*") == false) {
                    firstRecordGot = true;
                    String[] row = line.split(" +");
                    OSRecord record = new OSRecord();
                    record.setRow(line);
                    String key = Commons.createCoordKey(row[1], row[2], row[3]);
                    String fuzzyCenterKeyString = Commons.createCoordFuzzyKey(row[1], row[2], row[3]);

                    record.setKey(key);
                    record.setFuzzyKey(fuzzyCenterKeyString);
                    oSRecordMap.getKey2FileNameMap().put(key, currentFileName);
                    Map<String, String> realKeyList = oSRecordMap.getFuzzyKey2RealKeyMap().get(fuzzyCenterKeyString);
                    if (realKeyList == null) {
                        realKeyList = new HashMap<>();
                        oSRecordMap.getFuzzyKey2RealKeyMap().put(fuzzyCenterKeyString, realKeyList);
                    }
                    if (realKeyList.containsKey(key) == false) {
                        realKeyList.put(key, "");
                        if (realKeyList.size() > FAST_ACCESS_THRESHOLD) {
                            if (oSRecordMap.getFastAccessMap().size() < OSMAP_MAX_ENTRIES) {
                                //then put this into fast access
                                oSRecordMap.getFastAccessMap().put(key, record);
                            }
                        }
                    }
                    record.setX(Double.valueOf(row[1]));
                    record.setY(Double.valueOf(row[2]));
                    record.setZ(Double.valueOf(row[3]));
                    record.setNum(row[0]);
                    record.setReC1X(row[13]);
                    record.setImC1X(row[14]);
                    record.setReC1Y(row[15]);
                    record.setImC1Y(row[16]);
                    record.setReC1Z(row[17]);
                    record.setImC1Z(row[18]);

                    record.setReC2X(row[19]);
                    record.setImC2X(row[20]);
                    record.setReC2Y(row[21]);
                    record.setImC2Y(row[22]);
                    record.setReC2Z(row[23]);
                    record.setImC2Z(row[24]);

                    record.setReC3X(row[25]);
                    record.setImC3X(row[26]);
                    record.setReC3Y(row[27]);
                    record.setImC3Y(row[28]);
                    record.setReC3Z(row[29]);
                    record.setImC3Z(row[30]);

                    ret.put(record.getKey(), record);
                    //System.out.println("ret.size="+ret.size());
                    if (ret.size() > OSMAP_MAX_ENTRIES) {
                        SerializeUtil.writeToFile(ret, new File(outputFolder, currentFileName));
                        ret = new HashMap<>();
                        osRecordMapIndex++;
                        currentFileName = outputPrefix + "_" + osRecordMapIndex + ".osRecordMap";
                        oSRecordMap.getOsRecordFileNames().add(currentFileName);
                    }
                } else {
                    if (firstRecordGot) {
                        break;
                    }
                }
            }
        }
        SerializeUtil.writeToFile(ret, new File(outputFolder, currentFileName));
        //there is no need to output the index file since it is included in CurrentData
//        SerializeUtil.writeToFile(oSRecordMap, new File(outputFolder, outputPrefix+".osRecordIndex"));
        return oSRecordMap;
    }
    private static final int FAST_ACCESS_THRESHOLD = 100;
    private static final int OSMAP_MAX_ENTRIES = 400000;
}
