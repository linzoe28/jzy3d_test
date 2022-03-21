/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package jzy_3d_sample.ui;

import java.util.List;
import jzy_3d_sample.model.Cube;
import jzy_3d_sample.model.RenderModel;

/**
 *
 * @author lendle
 */
public interface Context {
    public List<Cube> getSubCubes();
    public RenderModel getRenderModel();
    public SouthpanelController getSouthpanelController();
}
