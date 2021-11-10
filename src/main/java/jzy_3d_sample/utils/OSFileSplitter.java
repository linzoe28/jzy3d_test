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

    public static List<File> splitByAngle(File osFile, Callback callback) throws Exception {
        boolean firstSection = false;
        List<File> outputFiles = new ArrayList<>();
        File currentOutputFile = null;
        StringBuilder sb = new StringBuilder();
        int count = 0;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(osFile), "utf-8"))) {
            String line = reader.readLine();
            while (true) {
                if (line != null) {
                    line = line.trim();
                    if (line.startsWith("#Configuration")) {
                        if (sb.length() > 0) {
                            FileUtils.write(currentOutputFile, sb.toString(), "utf-8", true);
                            if (callback != null) {
                                callback.osFileGenerated(currentOutputFile);
                            }
                            count = 0;
                            sb = new StringBuilder();
                        }
                        currentOutputFile = File.createTempFile("tempCurrent", ".os", osFile.getParentFile());
                        System.out.println("output os file: " + currentOutputFile.getAbsolutePath());
                        outputFiles.add(currentOutputFile);
                        firstSection = true;
                    }
                    if (firstSection) {
                        sb.append(line + "\r\n");
                        count++;
                    }
                    if (count >= MAX_BUFFER) {
                        FileUtils.write(currentOutputFile, sb.toString(), "utf-8", true);
                        count = 0;
                        sb = new StringBuilder();
                    }
                    line = reader.readLine();
                } else {
                    break;
                }
            }
            if (sb.length() > 0) {
                FileUtils.write(currentOutputFile, sb.toString(), "utf-8", true);
            }
            if (callback != null) {
                callback.osFileGenerated(currentOutputFile);
            }
        }
        return outputFiles;
    }

    public static List<File> splitByAngle(File osFile) throws Exception {
        return splitByAngle(osFile, null);
    }
    private static final int MAX_BUFFER = 100000;

    public static interface Callback {

        public void osFileGenerated(File file);
    }
}
