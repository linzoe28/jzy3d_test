/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jzy_3d_sample;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import jzy_3d_sample.model.serialized.ProjectModel;
import jzy_3d_sample.utils.SerializeUtil;
import org.nustaq.serialization.FSTConfiguration;
import org.nustaq.serialization.FSTObjectOutput;

/**
 *
 * @author lendle
 */
public class FSTConverter {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
//        File sourceDir = new File("/home/lendle/remote_dir/largesampleobj1");
//        File targetDir = new File("/home/lendle/remote_dir/largesampleobj2");
//        FSTConfiguration conf=FSTConfiguration.createDefaultConfiguration().setForceSerializable(true);
//        
//        for (File file : sourceDir.listFiles()) {
//            if (file.getName().endsWith(".obj") || file.getName().endsWith(".current") || file.getName().endsWith(".osRecordMap")) {
//                //then convert
//                File targetFile = new File(targetDir, file.getName());
//                System.out.println("converting "+file+" to "+targetFile);
//                try (ObjectInputStream objectInputStreamCurrent
//                        = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
//                        FSTObjectOutput objectOutputStreamCurrent
//                        = conf.getObjectOutput(new BufferedOutputStream(new FileOutputStream(targetFile)))) {
//                    Object obj = objectInputStreamCurrent.readObject();
//                    objectOutputStreamCurrent.writeObject(obj);
//                }
//            }
//        }
            
            File targetFile=new File("/home/lendle/remote_dir/largesampleobj2/cubes.obj");
            ProjectModel pm=(ProjectModel) SerializeUtil.readFromFile(targetFile);
            System.out.println(pm.getCubes().get(0).getBox3d().getVertices());
    }

}
