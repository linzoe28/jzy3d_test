/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jzy_3d_sample;

import au.com.bytecode.opencsv.CSVReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class Read_csvdata {
     public List<Mesh> getdata(File pointFile, File meshFile) {
        Map<String, double[]> points = new HashMap<>();
        List<Mesh> meshs = new ArrayList<>();
        try {
            CSVReader reader = new CSVReader(new FileReader(pointFile), ',', '\'', 1);
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                points.put(nextLine[0], new double[]{Double.valueOf(nextLine[1]), Double.valueOf(nextLine[2]), Double.valueOf(nextLine[3])});
            }
            CSVReader reader_mesh = new CSVReader(new FileReader(meshFile), ',', '\'', 1);
            while ((nextLine = reader_mesh.readNext()) != null) {
                Mesh m = new Mesh();
                m.setVertices(new Vertex[]{
                    new Vertex(points.get(nextLine[1])),
                    new Vertex(points.get(nextLine[2])),
                    new Vertex(points.get(nextLine[3])),
                });
                meshs.add(m);
            }

            System.out.println(meshs.toArray());
        } catch (FileNotFoundException ex) {
             Logger.getLogger(Read_csvdata.class.getName()).log(Level.SEVERE, null, ex);
         } catch (IOException ex) {
             Logger.getLogger(Read_csvdata.class.getName()).log(Level.SEVERE, null, ex);
         }
        return meshs;
    }
}
