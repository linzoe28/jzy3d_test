/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jzy_3d_sample.ui;

import jzy_3d_sample.model.Cube;
import jzy_3d_sample.model.Mesh;
import jzy_3d_sample.model.Vertex;
import org.jzy3d.colors.Color;

/**
 *
 * @author lendle
 */
public class EffectivePointColorPainter implements ColorPainter{
    private Vertex selectedVertex=null;

    public Vertex getSelectedVertex() {
        return selectedVertex;
    }

    public void setSelectedVertex(Vertex selectedVertex) {
        this.selectedVertex = selectedVertex;
    }
    
    
    
    @Override
    public void paint(int index, Cube cube) {
        Vertex effectivePointCube=cube.getEffectivePoint();
        if(effectivePointCube.equals(Vertex.NULL_VERTEX)){
            return;
        }
        System.out.println(effectivePointCube.equals(selectedVertex));
        Color color=(effectivePointCube.equals(selectedVertex))?new Color(0,0,255, 1f):new Color(0,0,255, 0.1f);
//        Color color=(effectivePointCube.equals(selectedVertex))?new Color(191,191,191, 1f):new Color(191,191,191, 0.1f);
        for (Mesh m : cube.getMeshs()) {
            m.setColor(color);
        }
    }
    
}
