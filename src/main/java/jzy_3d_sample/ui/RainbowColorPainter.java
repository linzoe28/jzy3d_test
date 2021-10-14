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
public class RainbowColorPainter implements ColorPainter {

    private double rcsThreshold = -1;
    private double gap = -1;

    public RainbowColorPainter(double rcsThreshold, double gap) {
        this.rcsThreshold = rcsThreshold;
        this.gap = gap;
    }

    @Override
    public void paint(int index, Cube c) {
        for (Mesh m : c.getMeshs()) {
            if (c.getRcs() >= rcsThreshold) {
                m.setColor(Color.RED);
            } else if (c.getRcs() >= rcsThreshold - gap && c.getRcs() < rcsThreshold) {
                m.setColor(new Color(255, 128, 0));
            } else if (c.getRcs() >= rcsThreshold - 2 * gap && c.getRcs() < rcsThreshold - gap) {
                m.setColor(new Color(247, 208, 56));
            } else if (c.getRcs() >= rcsThreshold - 3 * gap && c.getRcs() < rcsThreshold - 2 * gap) {
                m.setColor(new Color(163, 224, 72));
            } else if (c.getRcs() >= rcsThreshold - 4 * gap && c.getRcs() < rcsThreshold - 3 * gap) {
                m.setColor(new Color(52, 187, 230));
            } else {
                m.setColor(new Color(0, 59, 231));
            }
        }
    }

}
