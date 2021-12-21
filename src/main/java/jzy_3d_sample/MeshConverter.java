/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jzy_3d_sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author user
 */
public class MeshConverter extends Application {
    private Scene scene = null;
    
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader zoompanelFxmlLoader = new FXMLLoader(getClass().getResource("/fxml/batch_mesh_converter.fxml"));
            AnchorPane anchorPane=zoompanelFxmlLoader.load();
            scene=new Scene(anchorPane);
            primaryStage.setTitle("CS");
            primaryStage.setScene(scene);
            
            

            primaryStage.show();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
