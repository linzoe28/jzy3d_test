/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jzy_3d_sample.model.meshconverter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lendle
 */
public class File2StringList implements List<File>{
    private List<String> internalList=null;

    public File2StringList(List<String> internalList) {
        this.internalList=internalList;
    }
    
    @Override
    public int size() {
        return internalList.size();
    }

    @Override
    public boolean isEmpty() {
        return internalList.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        try {
            File f=(File) o;
            return internalList.contains(f.getCanonicalPath());
        } catch (IOException ex) {
            Logger.getLogger(File2StringList.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public Iterator<File> iterator() {
        return paths2Files(internalList).iterator();
    }

    @Override
    public Object[] toArray() {
        return paths2Files(internalList).toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return paths2Files(internalList).toArray((T[]) new File[0]);
    }

    @Override
    public boolean add(File e) {
        try {
            return internalList.add(e.getCanonicalPath());
        } catch (IOException ex) {
            Logger.getLogger(File2StringList.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        try {
            File e=(File) o;
            return internalList.remove(e.getCanonicalPath());
        } catch (IOException ex) {
            Logger.getLogger(File2StringList.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends File> c) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean addAll(int index, Collection<? extends File> c) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void replaceAll(UnaryOperator<File> operator) {
        List.super.replaceAll(operator); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void sort(Comparator<? super File> c) {
        List.super.sort(c); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public File get(int index) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public File set(int index, File element) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void add(int index, File element) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public File remove(int index) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int indexOf(Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ListIterator<File> listIterator() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ListIterator<File> listIterator(int index) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<File> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Spliterator<File> spliterator() {
        return List.super.spliterator(); //To change body of generated methods, choose Tools | Templates.
    }
 
    private static List<File> paths2Files(List<String> paths){
        List<File> files=new ArrayList<>();
        for(String path : paths){
            files.add(new File(path));
        }
        return files;
    }
    
    private static List<String> files2Paths(List<File> files) throws IOException{
        List<String> paths=new ArrayList<>();
        for(File file : files){
            paths.add(file.getCanonicalPath());
        }
        return paths;
    }
}
