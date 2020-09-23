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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
import jzy_3d_sample.ui.LegendController;
import jzy_3d_sample.ui.RainbowColorPainter;
import jzy_3d_sample.ui.RcslistController;
import jzy_3d_sample.ui.SlicecubeController;
import jzy_3d_sample.ui.SubCubesColorPainter;
import org.apache.commons.io.FileUtils;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.LineStrip;
import org.jzy3d.plot3d.primitives.Point;
import org.jzy3d.plot3d.primitives.Sphere;

/**
 *
 * @author user
 */
public class Main extends Application {

    private static final boolean TEST = true;
    private List<Mesh> meshs = null;
    private RenderModel renderModel = null;
    private Scene scene = null;
    private BorderPane container = null;
    private List<Cube> subCubes = null;
    File subCubeRoot = null;
    VBox colorLegend = null;
    FXMLLoader legendloader = null;
    Slider slider = new Slider();

    private RenderModel loadRenderModel(Stage primaryStage, List<Mesh> meshs) {
        this.meshs = meshs;
        this.renderModel = new RenderModel(scene, primaryStage, meshs);
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
            MenuItem fileSliceMenuItem = new MenuItem("子空間切割");
            fileMenu.getItems().add(fileSliceMenuItem);
            MenuItem fileRCSMenuItem = new MenuItem("RCS資料輸入");
            fileMenu.getItems().add(fileRCSMenuItem);
            fileMenu.getItems().add(new SeparatorMenuItem());
            MenuItem fileExitMenuItem = new MenuItem("Exit");
            fileMenu.getItems().add(fileExitMenuItem);

            fileOpenMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/fileopen.fxml"));
                        Parent root1 = (Parent) fxmlLoader.load();
                        FileOpenController fileOpenController = fxmlLoader.getController();
                        Stage stage = new Stage();
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.initStyle(StageStyle.UNDECORATED);
                        stage.setTitle("Open File...");
                        stage.setScene(new Scene(root1));
                        stage.showAndWait();
                        if (fileOpenController.isOk()) {
                            if (renderModel != null) {
                                container.setCenter(null);
                            }
                            meshs = r.getdata_from_nas(fileOpenController.getNasFile(), fileOpenController.getOsFile());
                            subCubeRoot = new File(fileOpenController.getNasFile().getName());
                            renderModel = loadRenderModel(primaryStage, meshs);
                            ScrollPane scrollPane = new ScrollPane();
                            container.setCenter(scrollPane);
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    scrollPane.setContent(renderModel.getView());

                                    renderModel.repaint();
                                }
                            });
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });

            fileSliceMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/slicecube.fxml"));
                        Parent root1 = (Parent) fxmlLoader.load();
                        SlicecubeController slicecubeController = fxmlLoader.getController();
                        Stage stage = new Stage();
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.initStyle(StageStyle.UTILITY);
                        stage.setTitle("Paste Slice Values......");
                        stage.setScene(new Scene(root1));
                        stage.showAndWait();
                        if (slicecubeController.isOk()) {
                            double[] slice_value = slicecubeController.getslice();
                            subCubes = renderModel.getBoundingCube().slice(slice_value[0], slice_value[1], slice_value[2]);
                            SubCubesColorPainter colorPainter = new SubCubesColorPainter();
                            for (int i = 0; i < subCubes.size(); i++) {
                                Cube cube = subCubes.get(i);
                                colorPainter.paint(i, cube);
//                               if(i!=22){
//                                   continue;
//                               }
//                                List<Coord3d> vertices = cube.getVertices();
//                               
//                                Coord3d[] mappedVertices=new Coord3d[]{vertices.get(0), vertices.get(3), vertices.get(1), vertices.get(4), vertices.get(2), vertices.get(5), vertices.get(7), vertices.get(6)};
//                                
//                                LineStrip lineStrip1 = new LineStrip(new Point(mappedVertices[0], Color.RED), new Point(mappedVertices[1], Color.RED));
//                                LineStrip lineStrip2 = new LineStrip(new Point(mappedVertices[0], Color.RED), new Point(mappedVertices[2], Color.RED));
//                                LineStrip lineStrip3 = new LineStrip(new Point(mappedVertices[0], Color.RED), new Point(mappedVertices[3], Color.RED));
//                                LineStrip lineStrip4 = new LineStrip(new Point(mappedVertices[1], Color.RED), new Point(mappedVertices[4], Color.RED));
//                                LineStrip lineStrip5 = new LineStrip(new Point(mappedVertices[2], Color.RED), new Point(mappedVertices[4], Color.RED));
//                                LineStrip lineStrip6 = new LineStrip(new Point(mappedVertices[7], Color.RED), new Point(mappedVertices[4], Color.RED));
//                                LineStrip lineStrip7 = new LineStrip(new Point(mappedVertices[3], Color.RED), new Point(mappedVertices[6], Color.RED));
//                                LineStrip lineStrip8 = new LineStrip(new Point(mappedVertices[1], Color.RED), new Point(mappedVertices[6], Color.RED));
//                                LineStrip lineStrip9 = new LineStrip(new Point(mappedVertices[7], Color.RED), new Point(mappedVertices[6], Color.RED));
//                                LineStrip lineStrip10 = new LineStrip(new Point(mappedVertices[7], Color.RED), new Point(mappedVertices[4], Color.RED));
//                                LineStrip lineStrip11 = new LineStrip(new Point(mappedVertices[5], Color.RED), new Point(mappedVertices[2], Color.RED));
//                                LineStrip lineStrip12 = new LineStrip(new Point(mappedVertices[5], Color.RED), new Point(mappedVertices[7], Color.RED));
//                                renderModel.getChart().getScene().getGraph().add(lineStrip1);
//                                renderModel.getChart().getScene().getGraph().add(lineStrip2);
//                                renderModel.getChart().getScene().getGraph().add(lineStrip3);
//                                renderModel.getChart().getScene().getGraph().add(lineStrip4);
//                                renderModel.getChart().getScene().getGraph().add(lineStrip5);
//                                renderModel.getChart().getScene().getGraph().add(lineStrip6);
//                                renderModel.getChart().getScene().getGraph().add(lineStrip7);
//                                renderModel.getChart().getScene().getGraph().add(lineStrip8);
//                                renderModel.getChart().getScene().getGraph().add(lineStrip9);
//                                renderModel.getChart().getScene().getGraph().add(lineStrip10);
//                                renderModel.getChart().getScene().getGraph().add(lineStrip11);
//                                renderModel.getChart().getScene().getGraph().add(lineStrip12);
//                                
//                                for(Mesh m : cube.getMeshs()){
//                                    m.setColor(Color.BLUE);
//                                }
                            }
                            renderModel.getSurface().setWireframeDisplayed(false);
                            renderModel.repaint();
//                            System.out.println(subCubes.size());
                            FileUtils.deleteDirectory(subCubeRoot);
                            FileUtils.forceMkdir(subCubeRoot);
                            for (int i = 0; i < subCubes.size(); i++) {
                                Cube c = subCubes.get(i);
                                File subCubeFolder = new File(subCubeRoot, "" + i);
//                                System.out.println(subCubeFolder);
                                FileUtils.forceMkdir(subCubeFolder);
                                FastN2fWriter.writeTriFile(c.getMeshs(), new File(subCubeFolder, i + ".tri"));
                                FastN2fWriter.writeCurMFile(c.getMeshs(), new File(subCubeFolder, i + ".curM"));
                                FastN2fWriter.writeCurJFile(c.getMeshs(), new File(subCubeFolder, i + ".curJ"));
                            }

                        }
                    } catch (IOException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
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

            BorderPane menuBarContainer = new BorderPane();
            menuBarContainer.setTop(menuBar);
            scene = new Scene(menuBarContainer, 800, 600);
            container = new BorderPane();
            menuBarContainer.setCenter(container);

            HBox hBox = new HBox();
            hBox.setSpacing(10);
            hBox.setPadding(new Insets(10));
            Label label = new Label("RCS 臨界值：");
            slider.setMin(0);
            slider.setMax(1);
            slider.setValue(0);
            slider.setShowTickLabels(true);
            slider.setShowTickMarks(true);
            slider.setMajorTickUnit(0.01);
            slider.setBlockIncrement(0.01);
            slider.setSnapToTicks(true);
            slider.setPrefWidth(500);
            TextField textField = new TextField();
            textField.setText(Double.toString(slider.getValue()));

            fileRCSMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/rcslist.fxml"));
                        Parent root1 = (Parent) fxmlLoader.load();
                        RcslistController rcslistController = fxmlLoader.getController();
                        Stage stage = new Stage();
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.initStyle(StageStyle.UTILITY);
                        stage.setTitle("Paste RCS Values......");
                        stage.setScene(new Scene(root1));
                        stage.showAndWait();
                        if (rcslistController.isOk()) {
                            //處理貼進來的 rcs 清單
                            String[] rcsList = rcslistController.getValues().split("\\n");
                            for (int i = 0; i < rcsList.length; i++) {
                                subCubes.get(i).setRcs(Double.valueOf(rcsList[i]));
                            }
                            sortCube(subCubes);
                            resetColor(Double.valueOf(textField.getText()));
                            colorLegend.setPrefWidth(63);
                            colorLegend.setVisible(true);
                            renderModel.repaint();
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            slider.valueProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    textField.setText(Double.toString(slider.getValue()));
                }
            });

//            textField.textProperty().addListener(new ChangeListener<String>() {
//                @Override
//                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                    slider.setValue(Double.valueOf(newValue));
//                }
//            });
            
            Button refreshButton=new Button("GO");
            hBox.getChildren().addAll(label, textField, slider, refreshButton);
            refreshButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    resetColor(Double.valueOf(textField.getText()));
                    renderModel.repaint();
                }
            });
            container.setTop(hBox);

            HBox bottomBar = new HBox();
            bottomBar.setAlignment(Pos.CENTER);
//            container.setBottom(bottomBar);//disable zoom in/out for now
            Label zoomLabel = new Label("Zoom in/out：");
            Slider zoomSlider = new Slider();
            zoomSlider.setMin(0);
            zoomSlider.setMax(10);
            zoomSlider.setValue(1);
            zoomSlider.setShowTickLabels(true);
            zoomSlider.setShowTickMarks(true);
            zoomSlider.setMajorTickUnit(0.5);
            zoomSlider.setBlockIncrement(0.5);
            zoomSlider.setPrefWidth(500);
            bottomBar.getChildren().addAll(zoomLabel, zoomSlider);
            zoomSlider.valueProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    renderModel.zoom(1 / newValue.floatValue());
                    renderModel.repaint();
                }
            });

            legendloader = new FXMLLoader(getClass().getResource("/fxml/legend.fxml"));
            colorLegend = (VBox) legendloader.load();
            container.setRight(colorLegend);
            colorLegend.setPrefWidth(0);
            colorLegend.setVisible(false);
            colorLegend.setPadding(new Insets(10));
            primaryStage.setTitle("CS");
            primaryStage.setScene(scene);
            if (TEST) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
//                            System.out.println(new File(".").getAbsolutePath());
                            meshs = r.getdata_from_nas(new File("./sample/missile_cone_test/missile_cone_test.nas"), new File("./sample/missile_cone_test/missile_cone_test.os"));
                            subCubeRoot = new File(new File("./sample/missile_cone_test/missile_cone_test.nas").getName());
                            renderModel = loadRenderModel(primaryStage, meshs);
                            ScrollPane scrollPane = new ScrollPane();
                            container.setCenter(scrollPane);
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    scrollPane.setContent(renderModel.getView());

                                    renderModel.repaint();
                                }
                            });
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            }
            primaryStage.show();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void resetColor(double rcsThreshold) {
        List<Cube> colorCubes = new ArrayList<>(subCubes);
        for (Cube c : colorCubes) {
            if (c.getRcs() != 0) {
                System.out.println(c.getRcs());
            }
        }
        System.out.println("MIN" + colorCubes.get(0).getRcs() + ",MAX" + colorCubes.get(colorCubes.size() - 1).getRcs());
        double gap = (rcsThreshold - colorCubes.get(0).getRcs()) / 5;
        System.out.println("gap" + gap);
        LegendController legendController = legendloader.getController();
        legendController.getRedValue().setText(String.format("%06.4f", rcsThreshold));
        legendController.getoValue().setText(String.format("%06.4f", rcsThreshold - gap));
        legendController.getyValue().setText(String.format("%06.4f", rcsThreshold - 2 * gap));
        legendController.getgValue().setText(String.format("%06.4f", rcsThreshold - 3 * gap));
        legendController.getbValue().setText(String.format("%06.4f", rcsThreshold - 4 * gap));
        legendController.getbValue1().setText(String.format("%06.4f", rcsThreshold - 5 * gap));
//        System.out.println(Arrays.deepToString(colors));
        RainbowColorPainter painter = new RainbowColorPainter(rcsThreshold, gap);
        for (int i = 0; i < colorCubes.size(); i++) {
            Cube c = colorCubes.get(i);
            painter.paint(i, c);
        }
        renderModel.getSurface().setWireframeDisplayed(false);
    }

    private void sortCube(List<Cube> Cubes) {
        Collections.sort(Cubes, new Comparator<Cube>() {

            @Override
            public int compare(Cube o1, Cube o2) {
                if (o1.getRcs() < o2.getRcs()) {
                    return -1;
                } else if (o1.getRcs() == o2.getRcs()) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });
        slider.setMax(Cubes.get(Cubes.size() - 1).getRcs());
        slider.setMin(Cubes.get(0).getRcs());
        slider.setValue(Cubes.get(0).getRcs());
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
