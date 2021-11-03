/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jzy_3d_sample.model.serialized;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import jzy_3d_sample.model.Cube;
import jzy_3d_sample.model.Mesh;
import jzy_3d_sample.utils.SerializeUtil;

/**
 *
 * @author lendle
 */
public class ProjectModel implements Cloneable, Serializable {
    transient private File homeFolder=null;
    private static final long serialVersionUID = -1636927109633279805L;
    private List<Cube> cubes = null;
    transient private List<CurrentData> currentDataList = new ArrayList<>();
    private long xSlice = -1, ySlice = -1, zSlice = -1;

    public File getHomeFolder() {
        return homeFolder;
    }

    public void setHomeFolder(File homeFolder) {
        this.homeFolder = homeFolder;
    }

    
    
    
    public long getxSlice() {
        return xSlice;
    }

    public void setxSlice(long xSlice) {
        this.xSlice = xSlice;
    }

    public long getySlice() {
        return ySlice;
    }

    public void setySlice(long ySlice) {
        this.ySlice = ySlice;
    }

    public long getzSlice() {
        return zSlice;
    }

    public void setzSlice(long zSlice) {
        this.zSlice = zSlice;
    }

    public List<Cube> getCubes() {
        return cubes;
    }

    public List<Mesh> getMeshes() {
        List<Mesh> meshes = new ArrayList<>();
        for (Cube cube : cubes) {
            meshes.addAll(cube.getMeshs());
        }
        return meshes;
    }

    public void setCubes(List<Cube> cubes) {
        this.cubes = cubes;
    }

    public CurrentData getCurrentData(int angleIndex) throws Exception{
        return (CurrentData) SerializeUtil.readFromFile(new File(this.homeFolder, angleIndex+".current"));
    }
//    public List<CurrentData> getCurrentDataList() {
//        return currentDataList;
//    }
//
//    public void setCurrentDataList(List<CurrentData> currentDataList) {
//        this.currentDataList = currentDataList;
//    }

}
