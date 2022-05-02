/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jzy_3d_sample.ui;

import javafx.application.Platform;

/**
 *
 * @author 70938
 */
public abstract class BackgroundRunner {

    private ProgressReporter progressReporter = null;
    private Thread runner=null;
    private boolean stopping=false;
    private boolean stopped=true;

    public boolean isStopping() {
        return stopping;
    }

    public boolean isStopped() {
        return stopped;
    }
    
    public BackgroundRunner(ProgressReporter progressReporter) {
        this.progressReporter = progressReporter;
    }

    public void start() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                progressReporter.startProgress();
                runBeforeWorkerThread();
            }
        });
        runner=new Thread() {
            public void run() {
                stopped=false;
                runInWorkerThread();
                
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        runInUIThread();
                        stopped=true;
                        afterStopped();
                        progressReporter.stopProgress();
                    }
                });
            }
        };
        runner.start();
    }
    
    public void shutdown(){
        this.stopping=true;
        if(runner!=null){
            runner.interrupt();
        }
    }
    
    public abstract void runBeforeWorkerThread();

    public abstract void runInWorkerThread();

    public abstract void runInUIThread();
    
    protected void afterStopped(){}

    public static interface ProgressReporter {

        public void startProgress();

        public void stopProgress();

        public void setStatus(String status);
    }
}
