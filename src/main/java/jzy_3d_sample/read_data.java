/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jzy_3d_sample;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class read_data {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        InputStreamReader isr;
        try {
//            isr = new InputStreamReader(new FileInputStream("./sample/Sample_cone_fine_mesh.csv"));
//            BufferedReader reader = new BufferedReader(isr);
//            BufferedWriter bw = new BufferedWriter(new FileWriter("./sample/cone_fine_mesh.csv"));
//            bw.write("#MESH,GRID*,GRID*,GRID*");
//            String line = null;
//            while ((line = reader.readLine()) != null) {
//                String item[] = line.split(" +");
//                String mesh = item[1];
//                String gred1 = item[3];
//                String gred2 = item[4];
//                String gred3 = item[5];
//                bw.newLine();
//                bw.write(mesh + "," + gred1 + "," + gred2 + "," + gred3);
//            }
//            bw.close();

            isr = new InputStreamReader(new FileInputStream("./sample/Sample_Chopper.csv"));
            BufferedReader reader = new BufferedReader(isr);
            BufferedWriter bw = new BufferedWriter(new FileWriter("./sample/chopper_point.csv"));
            bw.write("GRID*,X,Y,Z");
            String line = null;
            while ((line = reader.readLine()) != null) {
                String item[] = line.split(" +");
                String GRID = item[1];
                String X = item[2];
                String Y = item[3];
                String Z = null;
                if ((line = reader.readLine()) != null) {
                    String item1[] = line.split(" +");
                    Z = item1[2];
                }
                bw.newLine();
                bw.write(GRID + "," + X + "," + Y + "," + Z);
            }
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(read_data.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
