/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jzy_3d_sample.datafactory;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import jzy_3d_sample.datafactory.model.AngleIndexThetaPhiMap;
import jzy_3d_sample.model.serialized.ProjectModel;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author lendle
 */
public class AngleLabelMapGenerator {
    private static Map<String, String> angleCount2DeltaMap=null;
    static{
        final URL resource=AngleLabelMapGenerator.class.getClassLoader().getResource("jzy_3d/sample/model/angleCount2DeltaMap.json");
        try(InputStream input=resource.openStream()){
            String str=IOUtils.toString(input, "utf-8");
            Gson gson=new Gson();
            angleCount2DeltaMap=gson.fromJson(str, Map.class);
//            System.out.println("angleCount2DeltaMap="+angleCount2DeltaMap);
        } catch (IOException ex) {
            Logger.getLogger(AngleLabelMapGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static boolean isValidDeltaExists(int currentDataSize){
        return angleCount2DeltaMap.containsKey(""+currentDataSize);
    }
    
    public static int getDelta(int currentDataSize){
        int delta=Integer.valueOf(angleCount2DeltaMap.get(""+currentDataSize));
        return delta;
    }
    
    public static AngleIndexThetaPhiMap createAngleIndex2ThetaPhiMap(ProjectModel projectModel){
        AngleIndexThetaPhiMap ret=new AngleIndexThetaPhiMap();
        System.out.println(projectModel.getCurrentDataList(true).size());
        int delta=getDelta(projectModel.getCurrentDataList(true).size());//Integer.valueOf(angleCount2DeltaMap.get(""+(projectModel.getCurrentDataList(true).size())));
        System.out.println("delta="+delta);
        int index=0;
        for(int phi=0; phi<=360; phi+=delta){
            for(int theta=0; theta<=180; theta+=delta){
                String angleIndex="angle"+(index++);
                String thetaPhi="theta "+theta+", phi "+phi;
                ret.putAngleIndex2ThetaPhi(angleIndex, thetaPhi);
                ret.putThetaPhi2AngleIndex(thetaPhi, angleIndex);
            }
        }
        return ret;
    }
}
