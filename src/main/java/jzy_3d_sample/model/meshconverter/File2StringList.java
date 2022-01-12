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

    public List<String> getInternalList() {
        return internalList;
    }
    
    @Override
    public synchronized int size() {
        return internalList.size();
    }

    @Override
    public synchronized boolean isEmpty() {
        return internalList.isEmpty();
    }

    @Override
    public synchronized boolean contains(Object o) {
        try {
            File f=(File) o;
            return internalList.contains(f.getCanonicalPath());
        } catch (IOException ex) {
            Logger.getLogger(File2StringList.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public synchronized Iterator<File> iterator() {
        return paths2Files(internalList).iterator();
    }

    @Override
    public synchronized Object[] toArray() {
        return paths2Files(internalList).toArray();
    }

    @Override
    public synchronized <T> T[] toArray(T[] a) {
        return paths2Files(internalList).toArray((T[]) new File[0]);
    }

    @Override
    public synchronized boolean add(File e) {
        try {
            return internalList.add(e.getCanonicalPath());
        } catch (IOException ex) {
            Logger.getLogger(File2StringList.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public synchronized boolean remove(Object o) {
        try {
            File e=(File) o;
            return internalList.remove(e.getCanonicalPath());
        } catch (IOException ex) {
            Logger.getLogger(File2StringList.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public synchronized boolean containsAll(Collection<?> c) {
        return paths2Files(internalList).containsAll(c);
    }

    @Override
    public synchronized boolean addAll(Collection<? extends File> c) {
        try {
            return this.internalList.addAll(files2Paths(new ArrayList<File>(c)));
        } catch (IOException ex) {
            Logger.getLogger(File2StringList.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public synchronized boolean addAll(int index, Collection<? extends File> c) {
        try {
            return this.internalList.addAll(index, files2Paths(new ArrayList<File>(c)));
        } catch (IOException ex) {
            Logger.getLogger(File2StringList.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public synchronized boolean removeAll(Collection<?> c) {
        try {
            return this.internalList.removeAll(files2Paths(collection2FileList(c)));
        } catch (IOException ex) {
            Logger.getLogger(File2StringList.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public synchronized boolean retainAll(Collection<?> c) {
        try {
            return this.internalList.retainAll(files2Paths(collection2FileList(c)));
        } catch (IOException ex) {
            Logger.getLogger(File2StringList.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public synchronized void replaceAll(UnaryOperator<File> operator) {
        List.super.replaceAll(operator); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public synchronized void sort(Comparator<? super File> c) {
        List.super.sort(c); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public synchronized void clear() {
        this.internalList.clear();
    }

    @Override
    public synchronized File get(int index) {
        return new File(this.internalList.get(index));
    }

    @Override
    public synchronized File set(int index, File element) {
        try {
            return new File(this.internalList.set(index, element.getCanonicalPath()));
        } catch (IOException ex) {
            Logger.getLogger(File2StringList.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public synchronized void add(int index, File element) {
        try {
            this.internalList.add(index, element.getCanonicalPath());
        } catch (IOException ex) {
            Logger.getLogger(File2StringList.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public synchronized File remove(int index) {
        try {
            return new File(this.internalList.remove(index));
        } catch (Exception ex) {
            Logger.getLogger(File2StringList.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public synchronized int indexOf(Object o) {
        File file=(File) o;
        try {
            return this.internalList.indexOf(file.getCanonicalPath());
        } catch (IOException ex) {
            Logger.getLogger(File2StringList.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    @Override
    public synchronized int lastIndexOf(Object o) {
        File file=(File) o;
        try {
            return this.internalList.lastIndexOf(file.getCanonicalPath());
        } catch (IOException ex) {
            Logger.getLogger(File2StringList.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    @Override
    public synchronized ListIterator<File> listIterator() {
        return paths2Files(internalList).listIterator();
    }

    @Override
    public synchronized ListIterator<File> listIterator(int index) {
        return paths2Files(internalList).listIterator(index);
    }

    @Override
    public synchronized List<File> subList(int fromIndex, int toIndex) {
        return paths2Files(internalList).subList(fromIndex, toIndex);
    }

    @Override
    public synchronized Spliterator<File> spliterator() {
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
    
    private static List<File> collection2FileList(Collection<?> c){
        List<File> files=new ArrayList<>();
        for(Object o : c){
            files.add((File) o);
        }
        return files;
    }
}
