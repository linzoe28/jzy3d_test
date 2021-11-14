/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jzy_3d_sample.datafactory;

import java.util.ArrayList;
import java.util.List;
import jzy_3d_sample.model.Mesh;
import org.jzy3d.plot3d.primitives.Polygon;
import org.jzy3d.plot3d.primitives.Shape;

/**
 *
 * @author lendle
 */
public class SurfaceLoader {
    public static Shape[] loadSurfaces(List<Mesh> meshes) {
        List<Mesh> preserved=new ArrayList<>();
        for(Mesh mesh : meshes){
            if(Math.random()>0.5){
                preserved.add(mesh);
            }
        }
        Shape surface = new Shape(new ArrayList<Polygon>(meshes));
//        Shape surface = new Shape(new ArrayList<Polygon>(preserved));
        surface.setWireframeDisplayed(true);
        surface.setFaceDisplayed(true);
        surface.setWireframeColor(org.jzy3d.colors.Color.BLUE);
        
//        Shape surface = new Shape(new ArrayList<Polygon>(meshes));
        Shape surfaceLight = new Shape(new ArrayList<Polygon>(preserved));
        surfaceLight.setWireframeDisplayed(true);
        surfaceLight.setFaceDisplayed(true);
        surfaceLight.setWireframeColor(org.jzy3d.colors.Color.BLUE);
        
        return new Shape[]{surface, surfaceLight};
    }
    public static Shape loadSurface(List<Mesh> meshes) {
        List<Mesh> preserved=new ArrayList<>();
        for(Mesh mesh : meshes){
            if(Math.random()>0.7){
                preserved.add(mesh);
            }
        }
        Shape surface = new Shape(new ArrayList<Polygon>(meshes));
//        Shape surface = new Shape(new ArrayList<Polygon>(preserved));
        surface.setWireframeDisplayed(true);
        surface.setFaceDisplayed(true);
        surface.setWireframeColor(org.jzy3d.colors.Color.BLUE);
        
        
        return surface;
    }
}
