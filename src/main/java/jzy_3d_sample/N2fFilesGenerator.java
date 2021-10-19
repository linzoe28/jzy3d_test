/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jzy_3d_sample;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import jzy_3d_sample.datafactory.FastN2fWriter;
import jzy_3d_sample.datafactory.Read_data;
import jzy_3d_sample.datafactory.SurfaceLoader;
import jzy_3d_sample.model.Cube;
import jzy_3d_sample.model.Mesh;
import org.apache.commons.io.FileUtils;
import org.jzy3d.plot3d.primitives.Shape;

/**
 *
 * @author lendle
 */
public class N2fFilesGenerator {

    /**
     * @param args the command line arguments args[0]=nas file path args[1]=os
     * file path args[2]=target folder
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        File osFile = new File(args[1]);
        File nasFile = new File(args[0]);
        File outputFolder=new File(args[2]);
        //first, split the osFile to temp Folder
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
        //////////////////////////
        Read_data r = new Read_data();
        int index=0;
        for (File osFile1 : outputFiles) {
            File subOutputFolder=new File(outputFolder, "angle"+(index++));
            FileUtils.forceMkdir(subOutputFolder);
            List<Mesh> meshes = r.getdata_from_nas(new File(args[0]), osFile1);
            Shape surface = SurfaceLoader.loadSurface(meshes);
            Cube boundingCube = new Cube(surface.getBounds(), meshes);
            FastN2fWriter.writeCurJFile(meshes, new File(subOutputFolder, "0.curJ"));
            FastN2fWriter.writeCurMFile(meshes, new File(subOutputFolder, "0.curM"));
            FastN2fWriter.writeTriFile(meshes, new File(subOutputFolder, "0.tri"));
        }
    }

}
