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
        new Color(238, 238, 238, 100),
        new Color(204, 204, 204, 100),
        new Color(153, 153, 153, 100),
        new Color(102,102,102, 100),
        new Color(51, 51, 51, 100),
        new Color(0,0,0, 100)/*,
        new Color(119,136,153, 100)/*,
        new Color(112,128,144, 100),
        new Color(47,79,79, 100),
        new Color(0,0,0, 100)*/
    };
    @Override
    public void paint(int index, Cube cube) {
        for(Mesh m : cube.getMeshs()){
            m.setColor(colors[index%colors.length]);
        }
    }
    
}
