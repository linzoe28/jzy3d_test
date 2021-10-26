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
    public static Shape loadSurface(List<Mesh> meshes){
        List<Polygon> preserved=new ArrayList<>();
//        List<Mesh> pending=new ArrayList<>();
//        for(int i=0; i<meshes.size(); i++){
//            if(i%10==0){
//                preserved.add(meshes.get(i));   
//            }else{
//                pending.add(meshes.get(i));
//            }
//        }
        Shape surface = new Shape(new ArrayList<Polygon>(meshes));
//        Shape surface = new Shape(preserved);
//        Thread t=new Thread(){
//            public void run(){
//                try {
//                    Thread.sleep(5000);
//                } catch (InterruptedException ex) {
//                    ex.printStackTrace();
//                }
//                for(Mesh m : pending){
//                    surface.add(m);
//                }
//                System.out.println("no more meshes");
//                surface.updateBounds();
//            }
//        };
//        t.setDaemon(true);
//        t.start();
        surface.setWireframeDisplayed(true);
        surface.setFaceDisplayed(true);
        surface.setWireframeColor(org.jzy3d.colors.Color.BLUE);
        return surface;
    }
}
