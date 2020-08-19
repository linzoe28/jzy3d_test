/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jzy_3d_sample;

import jzy_3d_sample.datafactory.Read_data;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
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
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jzy_3d_sample.datafactory.FastN2fWriter;
import jzy_3d_sample.model.Mesh;
import jzy_3d_sample.model.RenderModel;
import jzy_3d_sample.ui.FileOpenController;

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
            //List<Mesh> meshs = r.getdata_from_pointAndMesh(new File("./sample/cone_fine_point.csv"), new File("./sample/cone_fine_mesh.csv"));
            meshs = r.getdata_from_nas(new File("./sample/FEKO2EMsuite/FEKO/Triangle with 4 mesh.nas"), new File("./sample/FEKO2EMsuite/FEKO/Triangle with 4 mesh_Currents1.os"));
            FastN2fWriter.writeTriFile(meshs, new File("test.tri"));
            FastN2fWriter.writeCurMFile(meshs, new File("test.curM"));
            FastN2fWriter.writeCurJFile(meshs, new File("test.curJ"));

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
                        stage.show();
                        if(fileOpenController.isOk()){
                            if(renderModel!=null){
                                container.setCenter(null);
                            }
                            meshs = r.getdata_from_nas(fileOpenController.getNasFile(), fileOpenController.getOsFile());
                            RenderModel renderModel=loadRenderModel(meshs);
                            ScrollPane scrollPane = new ScrollPane(renderModel.getView());
                            container.setCenter(scrollPane);
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
            StackPane root = new StackPane();
            menuBarContainer.getChildren().add(root);
            scene = new Scene(menuBarContainer, 800, 600);
            RenderModel model = this.loadRenderModel(meshs);
            ImageView view = model.getView();
            ScrollPane scrollPane = new ScrollPane(view);
//            Cube cube = model.getBoundingCube();
//            List<Cube> subCubes = cube.slice(0.05);

//            for (Cube subCube : subCubes) {
//                System.out.println(subCube.getMeshs().size());
//            }
//            System.out.println(model.getBoundingCube().slice(0.001, true));

            container = new BorderPane();
            container.setCenter(scrollPane);

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
            root.getChildren().add(container);

            primaryStage.setTitle("Hello World!");
            primaryStage.setScene(scene);
            primaryStage.show();

//        List<Polygon> polygons = new ArrayList<Polygon>();
//
//        for (int i = 0; i < meshs.size(); i++) {
//            Polygon polygon = new Polygon();
//            Vertex [] vertices=meshs.get(i).getVertices();
//            for (int j = 0; j < 3; j++) {
//                polygon.add(new Point(new Coord3d(
//                        (float) vertices[j].getX(),
//                        (float) vertices[j].getY(),
//                        (float) vertices[j].getZ()
//                )));
//                polygons.add(polygon);
//            }
//        }
// Create the object to represent the function over the given range.
//        Shape surface = new Shape(new ArrayList<Polygon>(meshs));
//        //surface.setColorMapper(new ColorMapper(new ColorMapRainbow(), surface.getBounds().getZmin(), surface.getBounds().getZmax(), new org.jzy3d.colors.Color(1, 1, 1, 1f)));
//        surface.setWireframeDisplayed(true);
//        surface.setFaceDisplayed(true);
//        surface.setWireframeColor(org.jzy3d.colors.Color.BLACK);
//
//        JavaFXChartFactory factory = new JavaFXChartFactory();
//        AWTChart chart = (AWTChart) factory.newChart(Quality.Intermediate, "offscreen");
//        chart.getScene().getGraph().add(surface);
//        BoundingBox3d boundingBox3d = surface.getBounds();
//        List<Float> xSteps = range(boundingBox3d.getXmin(), boundingBox3d.getXmax(), 5);
//        List<Float> ySteps = range(boundingBox3d.getYmin(), boundingBox3d.getYmax(), 5);
//        List<Float> zSteps = range(boundingBox3d.getZmin(), boundingBox3d.getZmax(), 5);
//
//        for (float x : xSteps) {
//            for (float y : ySteps) {
//                LineStrip lineStrip = new LineStrip(new Point(new Coord3d(x, y, boundingBox3d.getZmin()), Color.RED), new Point(new Coord3d(x, y, boundingBox3d.getZmax()), Color.RED));
//                chart.getScene().getGraph().add(lineStrip);
//            }
//        }
//        for (float x : xSteps) {
//            for (float z : zSteps) {
//                LineStrip lineStrip = new LineStrip(new Point(new Coord3d(x, boundingBox3d.getYmin(), z), Color.RED), new Point(new Coord3d(x, boundingBox3d.getYmax(), z), Color.RED));
//                chart.getScene().getGraph().add(lineStrip);
//            }
//        }
//        for (float y : ySteps) {
//            for (float z : zSteps) {
//                LineStrip lineStrip = new LineStrip(new Point(new Coord3d(boundingBox3d.getXmin(), y, z), Color.RED), new Point(new Coord3d(boundingBox3d.getXmax(), y, z), Color.RED));
//                chart.getScene().getGraph().add(lineStrip);
//            }
//        }
        } catch (IOException ex) {
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
