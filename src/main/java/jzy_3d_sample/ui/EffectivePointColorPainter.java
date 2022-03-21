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
    
    @Override
    public void paint(int index, Cube cube) {
        Color color=new Color(0,0,255, 0.1f);
        for (Mesh m : cube.getMeshs()) {
            m.setColor(color);
        }
    }
    
}
