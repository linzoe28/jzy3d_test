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
                    record.setReC1(row[13]);
                    record.setImC1(row[14]);
                    record.setReC2(row[15]);
                    record.setImC2(row[16]);
                    record.setReC3(row[17]);
                    record.setImC3(row[18]);
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
