/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jzy_3d_sample.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author lendle
 */
public class OSFileSplitter {
    public static List<File> splitByAngle(File osFile) throws Exception{
        boolean firstSection = false;
        List<File> outputFiles = new ArrayList<>();
        File currentOutputFile = null;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(osFile), "utf-8"))) {
            String line = reader.readLine();
            while (true) {
                if (line != null) {
                    line = line.trim();
                    if (line.startsWith("#Configuration")) {
                        currentOutputFile = File.createTempFile("tempCurrent", ".os");
                        outputFiles.add(currentOutputFile);
                        firstSection = true;
                    }
                    if (firstSection) {
                        FileUtils.write(currentOutputFile, line + "\r\n", "utf-8", true);
                    }

                    line = reader.readLine();
                } else {
                    break;
                }
            }
        }
        return outputFiles;
    }
}
