/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jzy_3d_sample.datafactory;

import au.com.bytecode.opencsv.CSVReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import jzy_3d_sample.model.Mesh;
import jzy_3d_sample.model.Vertex;
import jzy_3d_sample.model.VertexCurrent;
import jzy_3d_sample.model.os.OSRecord;
import org.apache.commons.math3.complex.Complex;

/**
 *
 * @author user
 */
public class Read_data {

    public List<Mesh> getdata_from_nas(File nasFile, File osFile) throws IOException {
        //first, split to point and mesh files
        boolean pointSection = true;
        File pointFile = File.createTempFile("point_", ".point");
        File meshFile = File.createTempFile("mesh_", ".mesh");
        File pointCSVFile = File.createTempFile("point_", ".csv");
        File meshCSVFile = File.createTempFile("mesh_", ".csv");
        try (BufferedReader input = new BufferedReader(new FileReader(nasFile));
                PrintWriter pointFileWriter = new PrintWriter(pointFile);
                PrintWriter meshFileWriter = new PrintWriter(meshFile);
                PrintWriter pointCSVFileWriter = new PrintWriter(pointCSVFile);
                PrintWriter meshCSVFileWriter = new PrintWriter(meshCSVFile)) {
            String line = input.readLine();
            while (line != null) {
                if (!line.startsWith("$")) {
                    if (pointSection) {
                        if (line.startsWith("CTRIA3")) {
                            pointSection = false;
                            continue;
                        }
                        pointFileWriter.println(line);
                    } else {
                        if (!line.startsWith("ENDDATA")) {
                            meshFileWriter.println(line);
                        }
                    }
                }
                line = input.readLine();
            }
            pointFileWriter.flush();
            meshFileWriter.flush();
            //convert point section to csv
            {
                InputStreamReader isr = new InputStreamReader(new FileInputStream(pointFile));
                BufferedReader reader = new BufferedReader(isr);
                BufferedWriter bw = new BufferedWriter(pointCSVFileWriter);
                bw.write("GRID*,X,Y,Z");
                String _line = null;
                while ((_line = reader.readLine()) != null) {
                    String item[] = _line.split(" +");
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
            }
            //convert mesh section to csv
            {
                InputStreamReader isr = new InputStreamReader(new FileInputStream(meshFile));
                BufferedReader reader = new BufferedReader(isr);
                BufferedWriter bw = new BufferedWriter(meshCSVFileWriter);
                bw.write("#MESH,GRID*,GRID*,GRID*");
                String _line = null;
                while ((_line = reader.readLine()) != null) {
                    String item[] = _line.split(" +");
                    String mesh = item[1];
                    String gred1 = item[3];
                    String gred2 = item[4];
                    String gred3 = item[5];
                    bw.newLine();
                    bw.write(mesh + "," + gred1 + "," + gred2 + "," + gred3);
                }
                bw.close();
            }
            List<Mesh> meshs = this.getdata_from_pointAndMesh(pointCSVFile, meshCSVFile, osFile);
            pointFile.deleteOnExit();
            meshFile.deleteOnExit();
            pointCSVFile.deleteOnExit();
            meshCSVFile.deleteOnExit();
            return meshs;
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public List<Mesh> getdata_from_pointAndMesh(File pointFile, File meshFile, File osFile) throws IOException {
        Map<String, double[]> points = new HashMap<>();
        Map<String, OSRecord> osRecords = OSFileParser.readOSFile(osFile);
        List<Mesh> meshs = new ArrayList<>();
        try {
            CSVReader reader = new CSVReader(new FileReader(pointFile), ',', '\'', 1);
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                points.put(nextLine[0], new double[]{Double.valueOf(nextLine[1]), Double.valueOf(nextLine[2]), Double.valueOf(nextLine[3])});
            }
            CSVReader reader_mesh = new CSVReader(new FileReader(meshFile), ',', '\'', 1);
            while ((nextLine = reader_mesh.readNext()) != null) {
//                System.out.println(oSRecord);
                Vertex v1 = new Vertex(points.get(nextLine[1]));
                Vertex v2 = new Vertex(points.get(nextLine[2]));
                Vertex v3 = new Vertex(points.get(nextLine[3]));
                //use the center point of the mesh as the key value to osrecords
                Mesh m = new Mesh(new Vertex[]{v1, v2, v3});
                Vertex center=m.getCenter();
                String key=String.format("%13.3E", (double)center.getX())+
                            String.format("%13.3E", (double)center.getY())+
                            String.format("%13.3E", (double)center.getZ());
                OSRecord oSRecord = osRecords.get(key);
                if(oSRecord==null){
                    double minDistance=Double.MAX_VALUE;
                    OSRecord candidate=null;
                    for(OSRecord record : osRecords.values()){
                        double distance=Math.sqrt(Math.pow(record.getX()-center.getX(), 2)+Math.pow(record.getY()-center.getY(), 2)+Math.pow(record.getZ()-center.getZ(), 2));
                        if(distance<minDistance){
                            candidate=record;
                            minDistance=distance;
                        }
                    }
                    oSRecord=candidate;
//                    System.out.println(key+":"+oSRecord.getKey()+":"+nextLine[0]+":"+oSRecord.getNum());
                }
                if (oSRecord != null) {
                    osRecords.remove(oSRecord.getKey());
//                    if(nextLine[0].equals(oSRecord.getNum())==false){
//                        System.out.println("");
//                    }
//                    osRecords.remove(oSRecord.getKey());
                    m.setCurrent(v1, new VertexCurrent(
                            new Complex(Double.valueOf(oSRecord.getReC1X()), Double.valueOf(oSRecord.getImC1X())),
                            new Complex(Double.valueOf(oSRecord.getReC1Y()), Double.valueOf(oSRecord.getImC1Y())),
                            new Complex(Double.valueOf(oSRecord.getReC1Z()), Double.valueOf(oSRecord.getImC1Z()))
                    ));
                    m.setCurrent(v2, new VertexCurrent(
                            new Complex(Double.valueOf(oSRecord.getReC2X()), Double.valueOf(oSRecord.getImC2X())),
                            new Complex(Double.valueOf(oSRecord.getReC2Y()), Double.valueOf(oSRecord.getImC2Y())),
                            new Complex(Double.valueOf(oSRecord.getReC2Z()), Double.valueOf(oSRecord.getImC2Z()))
                    ));
                    m.setCurrent(v3, new VertexCurrent(
                            new Complex(Double.valueOf(oSRecord.getReC3X()), Double.valueOf(oSRecord.getImC3X())),
                            new Complex(Double.valueOf(oSRecord.getReC3Y()), Double.valueOf(oSRecord.getImC3Y())),
                            new Complex(Double.valueOf(oSRecord.getReC3Z()), Double.valueOf(oSRecord.getImC3Z()))
                    ));
                }
                meshs.add(m);
            }

//            System.out.println(meshs.toArray());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Read_data.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Read_data.class.getName()).log(Level.SEVERE, null, ex);
        }
        return meshs;
    }
}
