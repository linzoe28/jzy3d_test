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

    public BackgroundRunner(ProgressReporter progressReporter) {
        this.progressReporter = progressReporter;
    }

    public void start() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                runBeforeWorkerThread();
            }
        });
        this.progressReporter.startProgress();
        runner=new Thread() {
            public void run() {
                runInWorkerThread();
                progressReporter.stopProgress();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        runInUIThread();
                    }
                });
            }
        };
        runner.start();
    }
    
    public void shutdown(){
        
    }
    
    public abstract void runBeforeWorkerThread();

    public abstract void runInWorkerThread();

    public abstract void runInUIThread();

    public static interface ProgressReporter {

        public void startProgress();

        public void stopProgress();

        public void setStatus(String status);
    }
}
