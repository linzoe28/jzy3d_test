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
import jzy_3d_sample.Commons;
import jzy_3d_sample.datafactory.model.MeshOSMatchingEntry;
import jzy_3d_sample.model.Mesh;
import jzy_3d_sample.model.Vertex;
import jzy_3d_sample.model.VertexCurrent;
import jzy_3d_sample.model.os.OSRecord;
import jzy_3d_sample.model.os.OSRecordMap;
import jzy_3d_sample.utils.ThreadUtils;
import org.apache.commons.math3.complex.Complex;

/**
 *
 * @author user
 */
public class Read_data {

    private File tempFolder = null;
    private String tempFilePrefix = null;
    private OSRecordMap lastOSRecordMap = null;

    public Read_data(File tempFolder, String tempFilePrefix) {
        this.tempFolder = tempFolder;
        this.tempFilePrefix = tempFilePrefix;
    }

    public Read_data() {
        this(new File(System.getProperty("java.io.tmpdir")), "tmp");
    }

    public OSRecordMap getLastOSRecordMap() {
        return lastOSRecordMap;
    }

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
                if (ThreadUtils.isInterrupted()) {
                    throw new IOException(new Exception("interrupted"));
                }
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
                    if (ThreadUtils.isInterrupted()) {
                        throw new IOException(new Exception("interrupted"));
                    }
                    String item[] = _line.split(" +");
                    String GRID = item[1];
                    String X = item[2];
                    String Y = item[3];
                    String Z = null;
                    if ((line = reader.readLine()) != null) {
                        String item1[] = line.split(" +");
                        if (item1.length < 3) {
                            Z = item1[1];
                        } else {
                            Z = item1[2];
                        }
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
                    if (ThreadUtils.isInterrupted()) {
                        throw new IOException(new Exception("interrupted"));
                    }
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
//            System.out.println("getdata_from_pointAndMesh started");
            List<Mesh> meshs = this.getdata_from_pointAndMesh(pointCSVFile, meshCSVFile, osFile);
//            System.out.println("getdata_from_pointAndMesh finished");
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
//        System.out.println("getdata_from_pointAndMesh");
        Map<String, double[]> points = new HashMap<>();
        OSRecordMap osRecords = OSFileParser.readOSFile(tempFolder, osFile, tempFilePrefix);
        lastOSRecordMap = osRecords;
        List<Mesh> meshs = new ArrayList<>();
        try {
            int totalNoCurrentMeshes = 0;
            CSVReader reader = new CSVReader(new FileReader(pointFile), ',', '\'', 1);
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                if (ThreadUtils.isInterrupted()) {
                    throw new IOException(new Exception("interrupted"));
                }
                points.put(nextLine[0], new double[]{Double.valueOf(nextLine[1]), Double.valueOf(nextLine[2]), Double.valueOf(nextLine[3])});
//                System.out.println(nextLine[0]+":"+Arrays.toString(new double[]{Double.valueOf(nextLine[1]), Double.valueOf(nextLine[2]), Double.valueOf(nextLine[3])}));
            }
            long total = 0;
            CSVReader reader_mesh = new CSVReader(new FileReader(meshFile), ',', '\'', 1);
            while ((nextLine = reader_mesh.readNext()) != null) {
                if (ThreadUtils.isInterrupted()) {
                    throw new IOException(new Exception("interrupted"));
                }
                //System.out.println((++total) + "/" + osRecords.size());
                Vertex v1 = new Vertex(points.get(nextLine[1]));
                Vertex v2 = new Vertex(points.get(nextLine[2]));
                Vertex v3 = new Vertex(points.get(nextLine[3]));
                //use the center point of the mesh as the key value to osrecords
                Mesh m = new Mesh(new Vertex[]{v1, v2, v3});
                /*OSRecord oSRecord = this.getOSRecordForMesh(m, osRecords);
                if (oSRecord != null) {
                    m.setOsRecordKey(oSRecord.getKey());
                    osRecords.remove(oSRecord.getKey());
                    applyOSRecord2Mesh(m, oSRecord);
                    if (m.getCurrent(v1).getX().abs() == 0 && m.getCurrent(v1).getY().abs() == 0 && m.getCurrent(v1).getZ().abs() == 0
                            && m.getCurrent(v2).getX().abs() == 0 && m.getCurrent(v2).getY().abs() == 0 && m.getCurrent(v2).getZ().abs() == 0
                            && m.getCurrent(v3).getX().abs() == 0 && m.getCurrent(v3).getY().abs() == 0 && m.getCurrent(v3).getZ().abs() == 0) {
                        totalNoCurrentMeshes++;
                    }
                }*/
                meshs.add(m);
            }
            this.applyOStoMeshes(meshs, osRecords);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Read_data.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Read_data.class.getName()).log(Level.SEVERE, null, ex);
        }
        return meshs;
    }

    /*public List<Mesh> getdata_from_pointAndMesh(File pointFile, File meshFile, File osFile) throws IOException {
        Map<String, double[]> points = new HashMap<>();
        OSRecordMap osRecords = OSFileParser.readOSFile(tempFolder, osFile, tempFilePrefix);
        lastOSRecordMap=osRecords;
        List<Mesh> meshs = new ArrayList<>();
        try {
            int totalNoCurrentMeshes = 0;
            CSVReader reader = new CSVReader(new FileReader(pointFile), ',', '\'', 1);
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                points.put(nextLine[0], new double[]{Double.valueOf(nextLine[1]), Double.valueOf(nextLine[2]), Double.valueOf(nextLine[3])});
            }
            long total=0;
            CSVReader reader_mesh = new CSVReader(new FileReader(meshFile), ',', '\'', 1);
            while ((nextLine = reader_mesh.readNext()) != null) {
                System.out.println((++total)+"/"+osRecords.size());
                Vertex v1 = new Vertex(points.get(nextLine[1]));
                Vertex v2 = new Vertex(points.get(nextLine[2]));
                Vertex v3 = new Vertex(points.get(nextLine[3]));
                //use the center point of the mesh as the key value to osrecords
                Mesh m = new Mesh(new Vertex[]{v1, v2, v3});
                OSRecord oSRecord = this.getOSRecordForMesh(m, osRecords);
                if (oSRecord != null) {
                    m.setOsRecordKey(oSRecord.getKey());
                    osRecords.remove(oSRecord.getKey());
                    applyOSRecord2Mesh(m, oSRecord);
                    if (m.getCurrent(v1).getX().abs() == 0 && m.getCurrent(v1).getY().abs() == 0 && m.getCurrent(v1).getZ().abs() == 0
                            && m.getCurrent(v2).getX().abs() == 0 && m.getCurrent(v2).getY().abs() == 0 && m.getCurrent(v2).getZ().abs() == 0
                            && m.getCurrent(v3).getX().abs() == 0 && m.getCurrent(v3).getY().abs() == 0 && m.getCurrent(v3).getZ().abs() == 0) {
                        totalNoCurrentMeshes++;
                    }
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
    }*/
    private OSRecord getOSRecordForMesh(Mesh mesh, OSRecordMap osRecordsMap) {
        if (mesh.getOsRecordKey() == null) {
            Mesh m = mesh;
            Vertex center = m.getCenter();
            String key = Commons.createCoordKey(center.getX(), center.getY(), center.getZ());
            OSRecord oSRecord = osRecordsMap.get(key);
            if (oSRecord == null) {
                double minDistance = Double.MAX_VALUE;
                OSRecord candidate = null;
                String fuzzyCenterKey = Commons.createCoordFuzzyKey(center.getX(), center.getY(), center.getZ());

                List<String> candidateRealKeys = osRecordsMap.getCandidateKeyFromFuzzyKey(fuzzyCenterKey);
                //System.out.println(candidateRealKeys.size());
                int keyIndex = 0;
                for (String _key : candidateRealKeys) {
                    OSRecord record = osRecordsMap.get(_key);
                    double distance = Math.sqrt(Math.pow(record.getX() - center.getX(), 2) + Math.pow(record.getY() - center.getY(), 2) + Math.pow(record.getZ() - center.getZ(), 2));
                    if (distance < minDistance) {
                        candidate = record;
                        minDistance = distance;
                    }
                }
                oSRecord = candidate;
            }
            if (oSRecord != null) {
                m.setOsRecordKey(oSRecord.getKey());
                return oSRecord;
            } else {
                return null;
            }
        } else {
            return osRecordsMap.get(mesh.getOsRecordKey());
        }
    }

    private void applyOSRecord2Mesh(Mesh mesh, OSRecord oSRecord) {
        mesh.setCurrent(mesh.getVertices()[0], new VertexCurrent(
                new Complex(Double.valueOf(oSRecord.getReC1X()), Double.valueOf(oSRecord.getImC1X())),
                new Complex(Double.valueOf(oSRecord.getReC1Y()), Double.valueOf(oSRecord.getImC1Y())),
                new Complex(Double.valueOf(oSRecord.getReC1Z()), Double.valueOf(oSRecord.getImC1Z()))
        ));
        mesh.setCurrent(mesh.getVertices()[1], new VertexCurrent(
                new Complex(Double.valueOf(oSRecord.getReC2X()), Double.valueOf(oSRecord.getImC2X())),
                new Complex(Double.valueOf(oSRecord.getReC2Y()), Double.valueOf(oSRecord.getImC2Y())),
                new Complex(Double.valueOf(oSRecord.getReC2Z()), Double.valueOf(oSRecord.getImC2Z()))
        ));
        mesh.setCurrent(mesh.getVertices()[2], new VertexCurrent(
                new Complex(Double.valueOf(oSRecord.getReC3X()), Double.valueOf(oSRecord.getImC3X())),
                new Complex(Double.valueOf(oSRecord.getReC3Y()), Double.valueOf(oSRecord.getImC3Y())),
                new Complex(Double.valueOf(oSRecord.getReC3Z()), Double.valueOf(oSRecord.getImC3Z()))
        ));
        mesh.setOsRecordKey(oSRecord.getKey());
    }

    public void applyOStoMeshes(Map<String, List<MeshOSMatchingEntry>> fuzzyKeyMatchingMap, OSRecordMap osRecordsMap) {
        List<String> fileNames = osRecordsMap.getOsRecordFileNames();
        for (String fileName : fileNames) {
            try {
                Map<String, OSRecord> map = osRecordsMap.load(fileName);
                for (OSRecord osRecord : map.values()) {
//                    leftOverOSRecords.put(osRecord.getKey(), osRecord);
                    String fuzzyOSKey = osRecord.getFuzzyKey();
                    List<MeshOSMatchingEntry> entries = fuzzyKeyMatchingMap.get(fuzzyOSKey);
                    boolean preciseMathFound=false;
                    //for 100% matched entries
                    for (MeshOSMatchingEntry entry : entries) {
                        if (ThreadUtils.isInterrupted()) {
                            throw new RuntimeException("interrupted");
                        }
                        if (entry.getMeshKey().equals(osRecord.getKey())) {
                            preciseMathFound=true;
                            entry.setBestOSRecord(osRecord);
                            entry.setMinDistance(0);
                            applyOSRecord2Mesh(entry.getMesh(), osRecord);
                            break;
                        }
                    }
                    if (!preciseMathFound) {
                        //for not 100% matched entries
                        for (MeshOSMatchingEntry entry : entries) {
                            Vertex center = entry.getMesh().getCenter();
                            double distance = Math.sqrt(Math.pow(osRecord.getX() - center.getX(), 2)
                                    + Math.pow(osRecord.getY() - center.getY(), 2) + Math.pow(osRecord.getZ() - center.getZ(), 2));
                            if (distance < entry.getMinDistance()) {
                                entry.setMinDistance(distance);
                                entry.setBestOSRecord(osRecord);
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                Logger.getLogger(Read_data.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException(ex);
//                System.out.println(current.getRow());
//                System.exit(0);
            }
        }
        List<MeshOSMatchingEntry> orphanedEntries = new ArrayList<>();
        //process all undetermined entries
        for (List<MeshOSMatchingEntry> entries : fuzzyKeyMatchingMap.values()) {
            if (!entries.isEmpty()) {
                for (MeshOSMatchingEntry entry : entries) {
                    if (entry.getBestOSRecord() != null) {
                        applyOSRecord2Mesh(entry.getMesh(), entry.getBestOSRecord());
                    } else {
                        orphanedEntries.add(entry);
                    }
                }

            }
        }
       
        if(!orphanedEntries.isEmpty()){
            System.out.println("warning orphanedEntries: " + orphanedEntries.size());
        }
//        for (MeshOSMatchingEntry entry : orphanedEntries) {
//
//            for (OSRecord osRecord : leftOverOSRecords.values()) {
//                Vertex center = entry.getMesh().getCenter();
//                double distance = Math.sqrt(Math.pow(osRecord.getX() - center.getX(), 2)
//                        + Math.pow(osRecord.getY() - center.getY(), 2) + Math.pow(osRecord.getZ() - center.getZ(), 2));
//                if (distance < entry.getMinDistance()) {
//                    entry.setMinDistance(distance);
//                    entry.setBestOSRecord(osRecord);
//                }
//            }
//
//        }
//        boolean printed=false;
//        for (MeshOSMatchingEntry entry : orphanedEntries) {
//            if (entry.getBestOSRecord() != null) {
//                leftOverOSRecords.remove(entry.getBestOSRecord().getKey());
//                applyOSRecord2Mesh(entry.getMesh(), entry.getBestOSRecord());
//                if(!printed){
//                    printed=true;
//                    System.out.println(entry.getMeshFuzzyKey()+":"+entry.getBestOSRecord().getFuzzyKey());
//                }
//            }
//        }

        /*System.out.println("matched entries: "+counter);
        int orphanedCounter=0;
        for (List<MeshOSMatchingEntry> entries : fuzzyKeyMatchingMap.values()) {
            if(entries.size()>0){
                System.out.println("orphaned entries: ");
                for(MeshOSMatchingEntry entry : entries){
                    orphanedCounter++;
                    System.out.println(entry.getMeshFuzzyKey()+":"+entry.getMeshKey());
                }
            }
        }
        System.out.println("unmatched: "+orphanedCounter);*/
    }

    public void applyOStoMeshes(List<Mesh> meshes, OSRecordMap osRecordsMap) {
        Map<String, List<MeshOSMatchingEntry>> fuzzyKeyMatchingMap = createMatchingMap(meshes);
        this.applyOStoMeshes(fuzzyKeyMatchingMap, osRecordsMap);
        int counter = 0;
        for (Mesh m : meshes) {
            if (m.getOsRecordKey() == null) {
                counter++;
            }
        }
    }

    private Map<String, List<MeshOSMatchingEntry>> createMatchingMap(List<Mesh> meshes) {
        Map<String, List<MeshOSMatchingEntry>> fuzzyKeyMatchingMap = new HashMap<>();
        for (Mesh mesh : meshes) {
            if (ThreadUtils.isInterrupted()) {
                throw new RuntimeException("interrupted");
            }
            Vertex center = mesh.getCenter();
            String key = Commons.createCoordKey(center.getX(), center.getY(), center.getZ());
            String fuzzyCenterKey = Commons.createCoordFuzzyKey(center.getX(), center.getY(), center.getZ());

            List<MeshOSMatchingEntry> entries = fuzzyKeyMatchingMap.get(fuzzyCenterKey);

            if (entries == null) {
                entries = new ArrayList<>();
                fuzzyKeyMatchingMap.put(fuzzyCenterKey, entries);
            }
            MeshOSMatchingEntry entry = new MeshOSMatchingEntry();
            entry.setMesh(mesh);
            entry.setMeshFuzzyKey(fuzzyCenterKey);
            entry.setMeshKey(key);
            entry.setMinDistance(Double.MAX_VALUE);
            entries.add(entry);

        }
        return fuzzyKeyMatchingMap;
    }
}
