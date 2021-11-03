/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jzy_3d_sample.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 *
 * @author lendle
 */
public class SerializeUtil {

    public static Object readFromFile(File file) throws FileNotFoundException, IOException, ClassNotFoundException {
        try (ObjectInputStream objectInputStreamCurrent
                = new ObjectInputStream(new FileInputStream(file))) {
            return objectInputStreamCurrent.readObject();
        }
    }

    public static void writeToFile(Serializable obj, File file) throws IOException {
        try (ObjectOutputStream objectOutputStreamCurrent
                = new ObjectOutputStream(new FileOutputStream(file))) {
            objectOutputStreamCurrent.writeObject(obj);
            objectOutputStreamCurrent.flush();
        }
    }
}
