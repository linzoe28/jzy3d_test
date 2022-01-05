/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jzy_3d_sample.utils.meshconverter;

import java.io.File;
import java.io.IOException;
import jzy_3d_sample.model.meshconverter.SerializedStatus;
import jzy_3d_sample.model.meshconverter.Status;
import jzy_3d_sample.utils.SerializeUtil;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author lendle
 */
public class StatusFileUtil {
    public static Status loadStatus(File meshconverterOutputFolder, File osFileFolder) throws IOException{
        File statusFolder=new File(meshconverterOutputFolder, ".status");
        File statusFile=new File(statusFolder, "saved");
        SerializedStatus status=null;
        if(!statusFolder.exists()){
            FileUtils.forceMkdir(statusFolder);
        }
        if(!statusFile.exists()){
            status=new SerializedStatus();
            SerializeUtil.writeToJsonFile(status, statusFile);
            return new Status(status);
        }else{
            status=(SerializedStatus) SerializeUtil.readFromJsonFile(SerializedStatus.class, statusFile);
            Status returnStatus=new Status(status);
            if(verify(meshconverterOutputFolder, osFileFolder, returnStatus)){
                return returnStatus;
            }
            return new Status(new SerializedStatus());
        }
    }
    
    public static void saveStatus(File meshconverterOutputFolder, Status status) throws IOException{
        File statusFolder=new File(meshconverterOutputFolder, ".status");
        File statusFile=new File(statusFolder, "saved");
        if(!statusFolder.exists()){
            FileUtils.forceMkdir(statusFolder);
        }
        SerializeUtil.writeToJsonFile(status.getS(), statusFile);
    }
    
    public static boolean verify(File meshconverterOutputFolder, File osFileFolder, Status status){
        if(SerializedStatus.VERSION.equals(status.getVersion())==true){
            if(status.isOsFileSplitDone()){
                File outputOsFolder = new File(osFileFolder, ".os");
                if(osFileFolder.exists()==false || osFileFolder.listFiles().length<1){
                    return false;
                }
            }
            if(status.getCurrentDataSerializationProgress()!=-1){
                File currentObjFile = new File(meshconverterOutputFolder, "angle" + status.getCurrentDataSerializationProgress() + ".current");
                if(!currentObjFile.exists()){
                    return  false;
                }
            }
            return true;
        }
        return false;
    }
}
