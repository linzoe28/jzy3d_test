/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jzy_3d_sample.datafactory;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import jzy_3d_sample.model.Mesh;
import jzy_3d_sample.model.Vertex;
import jzy_3d_sample.model.VertexCurrent;

/**
 *
 * @author lendle
 */
public class FastN2fWriter {

    public static void writeCurMFile(List<Mesh> meshs, File targetFile) throws IOException {
        try (PrintWriter writer = new PrintWriter(targetFile)) {
            for (int j = 0; j < meshs.size(); j++) {
                for (int i = 0; i < 9; i++) {
                    writer.println("0   0");
                }
                if (j < meshs.size() - 1) {
                    writer.println("");
                }
            }
        }
    }
    
    public static void writeCurJFile(List<Mesh> meshs, File targetFile) throws IOException {
        try (PrintWriter writer = new PrintWriter(targetFile)) {
            for (int j = 0; j < meshs.size(); j++) {
                Mesh m=meshs.get(j);
                Vertex [] vertices=m.getVertices();
                for(Vertex v : vertices){
                    VertexCurrent current=m.getCurrent(v);
                    if(current==null){
                        System.out.println("current is null for "+v);
                        continue;
                    }
                    writer.println(String.format("%13.8E", current.getX().getReal())+"   "+String.format("%13.8E", current.getX().getImaginary()));
                    writer.println(String.format("%13.8E", current.getY().getReal())+"   "+String.format("%13.8E", current.getY().getImaginary()));
                    writer.println(String.format("%13.8E", current.getZ().getReal())+"   "+String.format("%13.8E", current.getZ().getImaginary()));
                }
                if (j < meshs.size() - 1) {
                    writer.println("");
                }
            }
        }
    }

    public static void writeTriFile(List<Mesh> meshs, File targetFile) throws IOException {
        Set<Vertex> verticeSet = new HashSet<>();
        for (Mesh m : meshs) {
            verticeSet.addAll(Arrays.asList(m.getVertices()));
        }
        Map<Vertex, Integer> vertexNumbering = new HashMap<>();
        try (PrintWriter writer = new PrintWriter(targetFile)) {
            writer.println("1");
            writer.println(verticeSet.size());
            for (Mesh m : meshs) {
                Vertex[] vertices = m.getVertices();
                for (Vertex v : vertices) {
                    if (vertexNumbering.containsKey(v) == false) {
                        vertexNumbering.put(v, vertexNumbering.size());
                        writer.println("   " + String.format("%11.5e", v.getX()) + "   " + String.format("%11.5e", v.getY()) + "   " + String.format("%11.5e", v.getZ()));
                    }
                }
            }
            writer.println("");
            writer.println(meshs.size());
            for (Mesh m : meshs) {
                Vertex[] vertices = m.getVertices();
                String[] numbers = new String[]{
                    "" + vertexNumbering.get(vertices[0]),
                    "" + vertexNumbering.get(vertices[1]),
                    "" + vertexNumbering.get(vertices[2])
                };
                writer.println(String.join("   ", numbers));
            }
            //writer.println();
        }
    }
}
