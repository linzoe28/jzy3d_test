/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jzy_3d_sample;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jzy_3d_sample.datafactory.FastN2fWriter;
import jzy_3d_sample.model.Cube;
import jzy_3d_sample.model.Mesh;
import jzy_3d_sample.model.RenderModel;
import jzy_3d_sample.model.Vertex;
import jzy_3d_sample.model.VertexCurrent;
import jzy_3d_sample.ui.BackgroundRunner;
import jzy_3d_sample.ui.FileOpenController;
import jzy_3d_sample.ui.LegendController;
import jzy_3d_sample.ui.RCSvalueController;
import jzy_3d_sample.ui.RainbowColorPainter;
import jzy_3d_sample.ui.RcslistController;
import jzy_3d_sample.ui.SlicecubeController;
import jzy_3d_sample.ui.SouthpanelController;
import jzy_3d_sample.ui.SubCubesColorPainter;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.util.Zip4jUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.math3.complex.Complex;
import org.jzy3d.colors.Color;
import org.jzy3d.plot3d.primitives.Point;
import org.jzy3d.plot3d.primitives.Sphere;

/**
 *
 * @author user
 */
public class Main extends Application {

    private static final boolean TEST = false;
    private List<Mesh> meshs = null;
    private RenderModel renderModel = null;
    private Scene scene = null;
    private BorderPane container = null;
    private List<Cube> subCubes = null;
    File subCubeRoot = null;
    VBox colorLegend = null;
    FXMLLoader legendloader = null;
    String RCSTotal = "";
    private SouthpanelController southpanelController = null;
    private RCSvalueController rCSvalueController = null;
    private Point extremeValuePoint = null;

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
            MenuItem researchMenuItem = new MenuItem("研改輸出");
            fileMenu.getItems().add(researchMenuItem);
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
                            new BackgroundRunner(southpanelController) {
                                @Override
                                public void runBeforeWorkerThread() {
                                    southpanelController.setStatus("Rendering......");
                                }
                                
                                @Override
                                public void runInWorkerThread() {
                                    try {
                                        if (renderModel != null) {
                                            container.setCenter(null);
                                        }
                                        meshs = r.getdata_from_nas(fileOpenController.getNasFile(), fileOpenController.getOsFile());
                                        subCubeRoot = new File(fileOpenController.getNasFile().getName());
                                        renderModel = loadRenderModel(primaryStage, meshs);
                                        ScrollPane scrollPane = new ScrollPane();
                                        
                                        Platform.runLater(new Runnable() {
                                            @Override
                                            public void run() {
                                                container.setCenter(scrollPane);
                                                scrollPane.setContent(renderModel.getView());
                                                renderModel.repaint();
                                            }
                                        });     } catch (IOException ex) {
                                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                                
                                @Override
                                public void runInUIThread() {
                                    
                                    southpanelController.setStatus("Done");
                                }
                            }.start();
                            
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
                            new BackgroundRunner(southpanelController) {
                                @Override
                                public void runBeforeWorkerThread() {
                                    southpanelController.setStatus("Slicing cubes......");
                                }

                                @Override
                                public void runInWorkerThread() {
                                    try {
                                        double[] slice_value = slicecubeController.getslice();
                                        subCubes = renderModel.getBoundingCube().slice(slice_value[0], slice_value[1], slice_value[2]);
                                        SubCubesColorPainter colorPainter = new SubCubesColorPainter();
                                        for (int i = 0; i < subCubes.size(); i++) {
                                            Cube cube = subCubes.get(i);
                                            colorPainter.paint(i, cube);
                                        }
                                        renderModel.getSurface().setWireframeDisplayed(false);
                                        renderModel.repaint();
                                        southpanelController.setStatus("Output fast_n2f files......");
                                        FileUtils.deleteDirectory(subCubeRoot);
                                        FileUtils.forceMkdir(subCubeRoot);
                                        for (int i = 0; i < subCubes.size(); i++) {
                                            Cube c = subCubes.get(i);
                                            File subCubeFolder = new File(subCubeRoot, "" + i);
                                            FileUtils.forceMkdir(subCubeFolder);
                                            FastN2fWriter.writeTriFile(c.getMeshs(), new File(subCubeFolder, i + ".tri"));
                                            FastN2fWriter.writeCurMFile(c.getMeshs(), new File(subCubeFolder, i + ".curM"));
                                            FastN2fWriter.writeCurJFile(c.getMeshs(), new File(subCubeFolder, i + ".curJ"));
                                        }
                                        ZipFile zipFile = new ZipFile(new File(subCubeRoot.getName() + ".zip"));
                                        zipFile.addFolder(subCubeRoot);
                                        FileUtils.deleteDirectory(subCubeRoot);
                                    } catch (IOException ex) {
                                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }

                                @Override
                                public void runInUIThread() {
                                    southpanelController.setStatus("Done");
                                }
                            }.start();

                        }
                    } catch (IOException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });

            researchMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    new BackgroundRunner(southpanelController) {
                        File researchFile = null;
                        public void runBeforeWorkerThread() {
                            southpanelController.setStatus("Outputing research results......");
                        }

                        @Override
                        public void runInWorkerThread() {
                            try {
                                double rcsvalue = rCSvalueController.getThreshold();
                                ArrayList<Mesh> rmeshs = new ArrayList<>();
                                for (Cube c : subCubes) {
                                    if (c.getRcs() >= rcsvalue) {
                                        for (Mesh m : c.getClonedMeshs()) {
                                            for (Vertex v : m.getVertices()) {
                                                m.setCurrent(v, new VertexCurrent(Complex.ZERO, Complex.ZERO, Complex.ZERO));
                                                rmeshs.add(m);
                                            }
                                        }
                                    } else {
                                        for (Mesh m : c.getMeshs()) {
                                            rmeshs.add(m);
                                        }
                                    }
                                }
                                researchFile = new File("research_" + subCubeRoot.getName());
                                FileUtils.deleteDirectory(researchFile);
                                FileUtils.forceMkdir(researchFile);
                                researchFile = new File(researchFile, "0");
                                FileUtils.forceMkdir(researchFile);
                                FastN2fWriter.writeTriFile(rmeshs, new File(researchFile, "0.tri"));
                                FastN2fWriter.writeCurMFile(rmeshs, new File(researchFile, "0.curM"));
                                FastN2fWriter.writeCurJFile(rmeshs, new File(researchFile, "0.curJ"));
                                ZipFile zipFile = new ZipFile(new File("research_" + subCubeRoot.getName() + ".zip"));
                                zipFile.addFolder(researchFile.getParentFile());
                            } catch (IOException ex) {
                                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                        @Override
                        public void runInUIThread() {
                            try {
                                southpanelController.setStatus("Done");
                                Alert alert = new Alert(AlertType.INFORMATION);
                                alert.setHeaderText(researchFile.getParentFile().getName() + "已輸出完成");
                                alert.showAndWait();
                                FileUtils.deleteDirectory(researchFile.getParentFile());
                            } catch (IOException ex) {
                                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                    }.start();
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
                        ArrayList<String> rcsList = new ArrayList<>();
                        if (rcslistController.isOk()) {
                            FileReader fileReader = new FileReader(rcslistController.getRCSFile());
                            BufferedReader br = new BufferedReader(fileReader);
                            while (br.ready()) {
                                rcsList.add(br.readLine());
                            }
                            //處理讀進來的 rcs 清單
                            for (int i = 0; i < rcsList.size() - 1; i++) {
                                subCubes.get(i).setRcs(Double.valueOf(rcsList.get(i)));
                            }
                            RCSTotal = rcsList.get(rcsList.size() - 1);
                            southpanelController.setTextBeforeValue(RCSTotal);
                            sortCube(subCubes);
                            resetColor(Double.valueOf(rCSvalueController.getThreshold()));
                            colorLegend.setPrefWidth(63);
                            colorLegend.setVisible(true);
                            renderModel.repaint();
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });

            FXMLLoader rcsvalueFxmlLoader = new FXMLLoader(getClass().getResource("/fxml/rcsvaluepanel.fxml"));
            AnchorPane rcsvalueRoot = (AnchorPane) rcsvalueFxmlLoader.load();
            rCSvalueController = rcsvalueFxmlLoader.getController();
            container.setTop(rcsvalueRoot);
            rCSvalueController.setActionHandler(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    double threshold = Double.valueOf(rCSvalueController.getThreshold());
                    resetColor(threshold);
                    double sum = 0;
                    for (Cube cube : subCubes) {
                        if (cube.getRcs() < threshold) {
                            sum += cube.getRcs();
                        }
                    }
                    renderModel.repaint();
                }
            });

            FXMLLoader southpanelFxmlLoader = new FXMLLoader(getClass().getResource("/fxml/southpanel.fxml"));
            AnchorPane southpanelRoot = (AnchorPane) southpanelFxmlLoader.load();
            southpanelController = southpanelFxmlLoader.getController();
            container.setBottom(southpanelRoot);
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
                            meshs = r.getdata_from_nas(new File("./sample/Missile_RCS_Vpol_MLFMM_10GHz_Efield_YZplane 154deg/Missile_RCS_Vpol_MLFMM_10GHz_Efield_YZplane 154deg.nas"), new File("./sample/Missile_RCS_Vpol_MLFMM_10GHz_Efield_YZplane 154deg/Missile_RCS_Vpol_MLFMM_10GHz_Efield_YZplane 154deg.os"));
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
        if (extremeValuePoint != null) {
            renderModel.getChart().getScene().remove(extremeValuePoint);
        }
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
        //設定亮點
        List<Mesh> meshs = subCubes.get(subCubes.size() - 1).getMeshs();
        Collections.sort(meshs, new Comparator<Mesh>() {

            @Override
            public int compare(Mesh o1, Mesh o2) {
                if (o1.getCurrentAbs() < o2.getCurrentAbs()) {
                    return -1;
                } else if (o1.getCurrentAbs() == o2.getCurrentAbs()) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });
        meshs.get(meshs.size() - 1).setColor(Color.WHITE);
        this.extremeValuePoint = new Point(meshs.get(meshs.size() - 1).getCenter(), Color.WHITE, 10);
        southpanelController.setExtremePointPosition(meshs.get(meshs.size() - 1).getCenter());
        renderModel.getChart().getScene().add(extremeValuePoint);
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
        rCSvalueController.setSlidermax(Cubes.get(Cubes.size() - 1).getRcs());
        rCSvalueController.setSlidermin(Cubes.get(0).getRcs());
        rCSvalueController.setSlidervalue(Cubes.get(0).getRcs());
        double majortick = ((Cubes.get(Cubes.size() - 1).getRcs()) - (Cubes.get(0).getRcs())) / 20;
        rCSvalueController.setSliderMajorTickUnit(majortick);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
