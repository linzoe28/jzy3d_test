/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jzy_3d_sample.utils;

/**
 *
 * @author lendle
 */
public class ThreadUtils {

    private final static boolean activated = true;

    public static boolean isInterrupted() {
        return isInterrupted(Thread.currentThread());
    }

    public static boolean isInterrupted(Thread t) {
        if (activated) {
            return t.isInterrupted();
        }else{
            return false;
        }
    }
}
