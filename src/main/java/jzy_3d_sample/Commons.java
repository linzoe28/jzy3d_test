/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jzy_3d_sample;

/**
 *
 * @author lendle
 */
public class Commons {
    public static final String COORD_FORMAT="%13.8f";
    public static String createCoordKey(double x, double y, double z){
        String key = String.format(Commons.COORD_FORMAT, (double) x).trim()
                    + String.format(Commons.COORD_FORMAT, (double) y).trim()
                    + String.format(Commons.COORD_FORMAT, (double) z).trim();
        return key;
    }
    
    public static String createCoordKey(String x, String y, String z){
        return createCoordKey(Double.valueOf(x), Double.valueOf(y), Double.valueOf(z));
    }
    
    public static String createCoordFuzzyKey(double x, double y, double z){
        String key = String.format(Commons.COORD_FORMAT, (double) x).trim().replace("-", "").substring(0, 4)
                    + String.format(Commons.COORD_FORMAT, (double) y).trim().replace("-", "").substring(0, 4)
                    + String.format(Commons.COORD_FORMAT, (double) z).trim().replace("-", "").substring(0, 4);
        return key;
    }
    
    public static String createCoordFuzzyKey(String x, String y, String z){
        return createCoordFuzzyKey(Double.valueOf(x), Double.valueOf(y), Double.valueOf(z));
    }
}
