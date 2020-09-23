/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jzy_3d_sample.ui;

import jzy_3d_sample.model.Cube;
import jzy_3d_sample.model.Mesh;
import org.jzy3d.colors.Color;

/**
 *
 * @author lendle
 */
public class SubCubesColorPainter implements ColorPainter{
    private Color[] colors=new Color[]{
        new Color(220,220,220, 100),
        new Color(211,211,211, 100),
        new Color(192,192,192, 100),
        new Color(169,169,169, 100),
        new Color(128,128,128, 100),
        new Color(105,105,105, 100),
        new Color(119,136,153, 100),
        new Color(112,128,144, 100),
        new Color(47,79,79, 100),
        new Color(0,0,0, 100)
    };
    @Override
    public void paint(int index, Cube cube) {
        for(Mesh m : cube.getMeshs()){
            m.setColor(colors[index%colors.length]);
        }
    }
    
}
