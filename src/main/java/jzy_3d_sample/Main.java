/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jzy_3d_sample;

import java.io.File;
import jzy_3d_sample.datafactory.Read_data;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jzy_3d_sample.datafactory.FastN2fWriter;
import jzy_3d_sample.model.Cube;
import jzy_3d_sample.model.Mesh;
import jzy_3d_sample.model.RenderModel;
import jzy_3d_sample.ui.FileOpenController;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author user
 */
public class Main extends Application {
    private List<Mesh> meshs=null;
    private RenderModel renderModel=null;
    private Scene scene=null;
    private BorderPane container=null;
    
    private RenderModel loadRenderModel(List<Mesh> meshs){
        this.meshs=meshs;
        this.renderModel = new RenderModel(scene, meshs);
        return this.renderModel;
    }
    @Override
    public void start(Stage primaryStage) {
        try {
            Read_data r = new Read_data();


            MenuBar menuBar = new MenuBar();
            Menu fileMenu = new Menu("File");
            MenuItem fileOpenMenuItem = new MenuItem("Open");
            fileMenu.getItems().add(fileOpenMenuItem);
            fileMenu.getItems().add(new SeparatorMenuItem());
            MenuItem fileExitMenuItem = new MenuItem("Exit");
            fileMenu.getItems().add(fileExitMenuItem);

            fileOpenMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/fileopen.fxml"));
                        Parent root1 = (Parent) fxmlLoader.load();
                        FileOpenController fileOpenController=fxmlLoader.getController();
                        Stage stage = new Stage();
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.initStyle(StageStyle.UNDECORATED);
                        stage.setTitle("Open File...");
                        stage.setScene(new Scene(root1));
                        stage.showAndWait();
                        if(fileOpenController.isOk()){
                            if(renderModel!=null){
                                container.setCenter(null);
                            }
                            meshs = r.getdata_from_nas(fileOpenController.getNasFile(), fileOpenController.getOsFile());
                            renderModel=loadRenderModel(meshs);
                            Cube cube=renderModel.getBoundingCube();
                            List<Cube> subCubes=cube.slice(0.5);
                            FileUtils.forceMkdir(new File(fileOpenController.getNasFile().getName()));
                            for (int i=0; i<subCubes.size(); i++){
                                Cube c=subCubes.get(i);
                                FastN2fWriter.writeTriFile(c.getMeshs(), new File(fileOpenController.getNasFile().getName()+File.separator+i+".tri"));
                                FastN2fWriter.writeCurMFile(c.getMeshs(), new File(fileOpenController.getNasFile().getName()+File.separator+i+".curM"));
                                FastN2fWriter.writeCurJFile(c.getMeshs(), new File(fileOpenController.getNasFile().getName()+File.separator+i+".curJ"));
                            }
                            
                            ScrollPane scrollPane = new ScrollPane();
                            container.setCenter(scrollPane);
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    scrollPane.setContent(renderModel.getView());
                                    primaryStage.setWidth(800);
                                }
                            });
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });

            fileExitMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    System.exit(0);
                }
            });
            menuBar.getMenus().add(fileMenu);

            VBox menuBarContainer = new VBox(menuBar);
            scene = new Scene(menuBarContainer, 800, 600);
            container = new BorderPane();
            menuBarContainer.getChildren().add(container);


            HBox hBox = new HBox();
            hBox.setSpacing(10);
            Label label = new Label("RCS 臨界值");
            Slider slider = new Slider();
            slider.setMin(0);
            slider.setMax(20);
            slider.setValue(10);
            slider.setShowTickLabels(true);
            slider.setShowTickMarks(true);
            slider.setMajorTickUnit(1);
            slider.setBlockIncrement(1);
            TextField textField = new TextField();
            textField.setText(Double.toString(slider.getValue()));

            slider.valueProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    textField.setText(Double.toString(slider.getValue()));
                }
            });

            hBox.getChildren().addAll(label, slider, textField);
            container.setTop(hBox);

            primaryStage.setTitle("Hello World!");
            primaryStage.setScene(scene);
            primaryStage.show();
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private List<Float> range(float min, float max, int segments) {
        float step = (max - min) / segments;
        List<Float> ret = new ArrayList<>();
        for (int i = 0; i <= segments; i++) {
            ret.add(min + i * step);
        }
        return ret;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
