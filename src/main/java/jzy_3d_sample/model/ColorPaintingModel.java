/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jzy_3d_sample.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import jzy_3d_sample.ui.ColorPainter;
import jzy_3d_sample.ui.Context;
import jzy_3d_sample.ui.EffectivePointColorPainter;
import jzy_3d_sample.ui.RainbowColorPainter;
import org.jzy3d.colors.Color;
import org.jzy3d.plot3d.primitives.Point;

/**
 *
 * @author lendle
 */
public class ColorPaintingModel {

    private Context context = null;
    //for RainbowColorPainter
    private double rcsThresholdForHighlight = -1;
    private double rcsGapForRainbowLevels = -1;
    private Point extremeValuePoint = null;
    private Vertex extremePointPosition = null;
    ///////////////////////////////////////////

    //for EFFECTIVE_POINTS
    private Vertex selectedEffectivePoint = null;
    //////////////////////

    public ColorPaintingModel(Context context) {
        this.context = context;
    }

    private ColorPaintingMode colorPaintingMode = ColorPaintingMode.RAINBOW;

    public ColorPaintingMode getColorPaintingMode() {
        return colorPaintingMode;
    }

    public Vertex getSelectedEffectivePoint() {
        return selectedEffectivePoint;
    }

    public void setSelectedEffectivePoint(Vertex selectedEffectivePoint) {
        this.selectedEffectivePoint = selectedEffectivePoint;
    }
    
    
    public Point getExtremeValuePoint() {
        return extremeValuePoint;
    }

    public Vertex getExtremePointPosition() {
        return extremePointPosition;
    }

    public void setColorPaintingMode(ColorPaintingMode colorPaintingMode) {
        this.colorPaintingMode = colorPaintingMode;
    }

    public double getRcsThresholdForHighlight() {
        return rcsThresholdForHighlight;
    }

    public void setRcsThresholdForHighlight(double rcsThresholdForHighlight) {
        this.rcsThresholdForHighlight = rcsThresholdForHighlight;
    }

    public double getRcsGapForRainbowLevels() {
        return rcsGapForRainbowLevels;
    }

  
    public void update() {
        ColorPainter painter = null;
        List<Cube> colorCubes = new ArrayList<>(context.getSubCubes());
        if (extremeValuePoint != null) {
            context.getRenderModel().getChart().getScene().remove(extremeValuePoint);
        }
        if (colorPaintingMode.equals(ColorPaintingMode.RAINBOW)) {
            double gap = (rcsThresholdForHighlight - colorCubes.get(0).getRcs()) / 5;
            rcsGapForRainbowLevels=gap;
            painter = new RainbowColorPainter(rcsThresholdForHighlight, gap);
            context.getRenderModel().getSurface().setWireframeDisplayed(false);
            for (int i = 0; i < colorCubes.size(); i++) {
                Cube c = colorCubes.get(i);
                painter.paint(i, c);
            }
            List<Cube> Cubes = ColorPaintingModel.sortCube(colorCubes);
            List<Mesh> meshs = Cubes.get(Cubes.size() - 1).getMeshs();
            Collections.sort(meshs, new Comparator<Mesh>() {

                @Override
                public int compare(Mesh o1, Mesh o2) {
                    if (o1.getCurrentAbs() < o2.getCurrentAbs()) {
                        return -1;
                    } else if (o1.getCurrentAbs() == o2.getCurrentAbs()) {
                        return 0;
                    } else {
                        return 1;
                    }
                }
            });
            //to be fixed
            if (meshs.size() > 0) {
                meshs.get(meshs.size() - 1).setColor(Color.WHITE);
                this.extremeValuePoint = new Point(meshs.get(meshs.size() - 1).getCenter(), Color.WHITE, 10);
                this.extremePointPosition = meshs.get(meshs.size() - 1).getCenter();
                context.getSouthpanelController().setExtremePointPosition(getExtremePointPosition());
                context.getRenderModel().getChart().getScene().add(extremeValuePoint);
            }
        } else if (colorPaintingMode.equals(ColorPaintingMode.EFFECTIVE_POINTS)) {
            painter = new EffectivePointColorPainter();
            Color color=new Color(0,0,255);
            for (int i = 0; i < colorCubes.size(); i++) {
                Cube c = colorCubes.get(i);
                List<Mesh> meshs = c.getMeshs();
                Collections.sort(meshs, new Comparator<Mesh>() {

                    @Override
                    public int compare(Mesh o1, Mesh o2) {
                        if (o1.getCurrentAbs() < o2.getCurrentAbs()) {
                            return -1;
                        } else if (o1.getCurrentAbs() == o2.getCurrentAbs()) {
                            return 0;
                        } else {
                            return 1;
                        }
                    }
                });
                
                Point effectivePoint=null;
                if(selectedEffectivePoint==null){
                    selectedEffectivePoint=meshs.get(meshs.size() - 1).getCenter();
                    
                }
                if(c.getEffectivePoint().equals(selectedEffectivePoint)){
                    effectivePoint=new Point(meshs.get(meshs.size() - 1).getCenter(), Color.RED, 10);
                }else{
                     effectivePoint=new Point(meshs.get(meshs.size() - 1).getCenter(), Color.WHITE, 10);
                }
                ((EffectivePointColorPainter)painter).setSelectedVertex(selectedEffectivePoint);
                painter.paint(i, c);
                context.getRenderModel().getChart().getScene().add(effectivePoint);
            }
        }
    }

    public static List<Cube> sortCube(List<Cube> Cubes) {
        List<Cube> clonedCubes = new ArrayList<>(Cubes);
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
