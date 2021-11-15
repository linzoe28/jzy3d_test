/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jzy_3d_sample.model.serialized;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import jzy_3d_sample.datafactory.AngleLabelMapGenerator;
import jzy_3d_sample.datafactory.model.AngleIndexThetaPhiMap;
import jzy_3d_sample.model.Cube;
import jzy_3d_sample.model.Mesh;
import jzy_3d_sample.utils.SerializeUtil;

/**
 *
 * @author lendle
 */
public class ProjectModel implements Cloneable, Serializable {

    transient private File homeFolder = null;
    transient private AngleIndexThetaPhiMap angleIndexThetaPhiMap = null;
    private static final long serialVersionUID = -1636927109633279805L;
    private List<Cube> cubes = null;
    private List<String> currentDataList = new ArrayList<>();
    transient private List<String> currentThetaPhiList = null;
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

    public CurrentData getCurrentData(String label) throws Exception {
        initAngleIndexThetaPhiMap();
        File file = new File(this.homeFolder, label + ".current");
        if (!file.exists()) {
            label = angleIndexThetaPhiMap.getAngleIndex(label);
            file = new File(this.homeFolder, label + ".current");
        }
        return (CurrentData) SerializeUtil.readFromFile(file);
    }

    private void initAngleIndexThetaPhiMap() {
        if (angleIndexThetaPhiMap == null) {
            angleIndexThetaPhiMap = AngleLabelMapGenerator.createAngleIndex2ThetaPhiMap(this);
        }
    }

    public List<String> getCurrentDataList(boolean angleIndex) {
        if (angleIndex == true) {
            return this.currentDataList;
        } else {
            if (currentThetaPhiList == null) {
                currentThetaPhiList = new ArrayList<>();
                initAngleIndexThetaPhiMap();
                for (String _angleIndex : currentDataList) {
                    currentThetaPhiList.add(angleIndexThetaPhiMap.getThetaPhi(_angleIndex));
                    System.out.println(angleIndexThetaPhiMap.getThetaPhi(_angleIndex));
                }
            }
            return currentThetaPhiList;
        }
    }

    public List<String> getCurrentDataList() {
        return this.getCurrentDataList(false);
    }

    public void setCurrentDataList(List<String> currentDataList) {
        this.currentDataList = currentDataList;
    }

}
