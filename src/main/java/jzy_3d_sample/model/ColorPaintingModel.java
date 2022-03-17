/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jzy_3d_sample.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import jzy_3d_sample.ui.Context;

/**
 *
 * @author lendle
 */
public class ColorPaintingModel {
    private Context context=null;

    public ColorPaintingModel(Context context) {
        this.context=context;
    }
    
    private ColorPaintingMode colorPaintingMode=ColorPaintingMode.RAINBOW;
    public static List<Cube> sortCube(List<Cube> Cubes) {
        List<Cube> clonedCubes=new ArrayList<>(Cubes);
        Collections.sort(clonedCubes, new Comparator<Cube>() {

            @Override
            public int compare(Cube o1, Cube o2) {
                if (o1.getRcs() < o2.getRcs()) {
                    return -1;
                } else if (o1.getRcs() == o2.getRcs()) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });

        return clonedCubes;

    }
}
