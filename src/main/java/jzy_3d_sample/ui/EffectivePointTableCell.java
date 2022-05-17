/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jzy_3d_sample.ui;

import javafx.scene.control.TableCell;
import jzy_3d_sample.model.EffectivePointModel;

/**
 *
 * @author lendle
 */
public class EffectivePointTableCell<EffectivePointModel> extends TableCell<EffectivePointModel, Double> {

    public EffectivePointTableCell() {
        
    }

    @Override
    protected void updateItem(Double item, boolean empty) {
        super.updateItem(item, empty); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        if (empty || item == null) {
            setText(null);
        } else {
            setText(String.format("%3.2e", item));
        }
    }
    
}