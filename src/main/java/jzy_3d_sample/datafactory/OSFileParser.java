/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jzy_3d_sample.datafactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import jzy_3d_sample.model.os.OSRecord;

/**
 *
 * @author lendle
 */
public class OSFileParser {

    /**
     * read a os file, return a map from mesh number to OSRecord
     *
     * @return
     */
    public static Map<String, OSRecord> readOSFile(File osFile) throws IOException {

        Map<String, OSRecord> ret = new HashMap<>();
        if (osFile == null) {
            return ret;
        }
        try (BufferedReader input = new BufferedReader(new FileReader(osFile))) {
            String line = null;
            boolean firstRecordGot = false;
            while ((line = input.readLine()) != null) {
                line = line.trim();
                if (line.length() > 0 && line.startsWith("#") == false && line.startsWith("*") == false) {
                    firstRecordGot = true;
                    String[] row = line.split(" +");
                    OSRecord record = new OSRecord();
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
                    
                    ret.put(record.getNum(), record);
                } else {
                    if (firstRecordGot) {
                        break;
                    }
                }
            }
        }
        return ret;
    }
}
