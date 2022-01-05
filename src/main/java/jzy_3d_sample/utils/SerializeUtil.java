/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jzy_3d_sample.utils;

import com.google.gson.Gson;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author lendle
 */
public class SerializeUtil {
    public static Object readFromJsonFile(Class t, File file) throws IOException{
        Gson gson=new Gson();
        String str=FileUtils.readFileToString(file, "utf-8");
        return gson.fromJson(str, t);
    }
    public static Object readFromFile(File file) throws FileNotFoundException, IOException, ClassNotFoundException {
        try (ObjectInputStream objectInputStreamCurrent
                = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)))) {
            return objectInputStreamCurrent.readObject();
        }
//        FSTConfiguration conf=FSTConfiguration.createDefaultConfiguration().setForceSerializable(true);
//        try (FSTObjectInput in = conf.getObjectInput(new BufferedInputStream(new FileInputStream(file)))) {
//            return in.readObject();
//        }
    }
    
    public static void writeToJsonFile(Object obj, File file) throws IOException{
        Gson gson=new Gson();
        String json=gson.toJson(obj);
        FileUtils.write(file, json, "utf-8");
    }
    public static void writeToFile(Serializable obj, File file) throws IOException {
        try (ObjectOutputStream objectOutputStreamCurrent
                = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)))) {
            objectOutputStreamCurrent.writeObject(obj);
            objectOutputStreamCurrent.flush();
        }
//        FSTConfiguration conf=FSTConfiguration.createDefaultConfiguration().setForceSerializable(true);
//        try (FSTObjectOutput objectOutputStreamCurrent
//                = conf.getObjectOutput(new BufferedOutputStream(new FileOutputStream(file)))) {
//            objectOutputStreamCurrent.writeObject(obj);
//            objectOutputStreamCurrent.flush();
//        }
    }
}
