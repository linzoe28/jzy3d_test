/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jzy_3d_sample.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jzy_3d_sample.model.Mesh;
import org.jzy3d.colors.Color;

/**
 *
 * @author lendle
 */
public class MeshColoringMonitor {

    private List<MeshColoringTaskQueue> queues = new ArrayList<>();
    private static MeshColoringMonitor instance=null;

    private MeshColoringMonitor(int numOfQueues) {
        queues = new ArrayList<>();
        for (int i = 0; i < numOfQueues; i++) {
            MeshColoringTaskQueue queue=new MeshColoringTaskQueue();
            queues.add(queue);
            MeshColoringThread t=new MeshColoringThread(queue);
            t.start();
        }
    }
    
    public synchronized static MeshColoringMonitor getInstance(){
        if(instance==null){
            instance=new MeshColoringMonitor(2);
        }
        return instance;
    }

    public void submitTask(Mesh mesh, Color color) {
        int index = (int) Math.floor(Math.random() * queues.size());
        MeshColoringTaskQueue queue = queues.get(index);
        queue.submitTask(mesh, color);
    }

    private static class MeshColoringTaskQueue {

        private List<MeshColringTask> tasks = new ArrayList<>();

        public synchronized void submitTask(Mesh mesh, Color color) {
            tasks.add(new MeshColringTask(mesh, color));
            notifyAll();
        }
        
        public synchronized MeshColringTask getTask(){
            while(tasks.isEmpty()){
                try {
                    wait();
                } catch (InterruptedException ex) {
                    Logger.getLogger(MeshColoringMonitor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return tasks.remove(0);
        }
    }
    
    public static class MeshColoringThread extends Thread{
        private MeshColoringTaskQueue queue=null;
        private boolean running=true;
        public MeshColoringThread(MeshColoringTaskQueue queue) {
            this.setDaemon(true);
            this.queue=queue;
        }
        
        public void run(){
            while(running){
                MeshColringTask task=queue.getTask();
                task.execute();
            }
        }
        
        public void shutdown(){
            running=false;
            interrupt();
        }
    }

    public static class MeshColringTask {

        private Mesh mesh = null;
        private Color color = null;

        public MeshColringTask(Mesh mesh, Color color) {
            this.mesh = mesh;
            this.color = color;
        }

        public Mesh getMesh() {
            return mesh;
        }

        public void setMesh(Mesh mesh) {
            this.mesh = mesh;
        }

        public Color getColor() {
            return color;
        }

        public void setColor(Color color) {
            this.color = color;
        }

        public void execute() {
            this.mesh.setColor(color);
        }

    }
}
