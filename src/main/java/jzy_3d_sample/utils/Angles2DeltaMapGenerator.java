/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jzy_3d_sample.utils;

import com.google.gson.Gson;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author lendle
 */
public class Angles2DeltaMapGenerator {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        File folder = new File("Angle_Data Sheet");
        Map<String, String> angleCount2DeltaMap = new HashMap<>();
        for (int delta = 1; delta <= 180; delta++) {
            if (delta < 5 || (delta<60 && delta % 5 == 0) || (delta<=90 && delta%10==0) || (delta==120 || delta==180)){
                File file = new File(folder, "TheatPhi_delta" + delta + ".csv");
                int lines = FileUtils.readLines(file, "utf-8").size();
                angleCount2DeltaMap.put(""+((int)lines), ""+((int)delta));
            }
        }
        Gson gson=new Gson();
        System.out.println(gson.toJson(angleCount2DeltaMap));
        final URL resource=Angles2DeltaMapGenerator.class.getClassLoader().getResource("jzy_3d/sample/model/angleCount2DeltaMap.json");
        try(InputStream input=resource.openStream()){
            String str=IOUtils.toString(input, "utf-8");
//            System.out.println(str);
        }
    }

}
