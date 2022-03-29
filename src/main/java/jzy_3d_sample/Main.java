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
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jzy_3d_sample.datafactory.FastN2fWriter;
import jzy_3d_sample.model.ColorPaintingMode;
import jzy_3d_sample.model.Cube;
import jzy_3d_sample.model.Mesh;
import jzy_3d_sample.model.RenderModel;
import jzy_3d_sample.model.Vertex;
import jzy_3d_sample.model.VertexCurrent;
import jzy_3d_sample.model.serialized.CurrentData;
import jzy_3d_sample.ui.AnglePanelController;
import jzy_3d_sample.ui.AngleSelectionHandler;
import jzy_3d_sample.ui.BackgroundRunner;
import jzy_3d_sample.model.ColorPaintingModel;
import jzy_3d_sample.ui.Context;
import jzy_3d_sample.ui.EffectivePointHandler;
import jzy_3d_sample.ui.FileOpenObjController;
import jzy_3d_sample.ui.LegendController;
import jzy_3d_sample.ui.RCSvalueController;
import jzy_3d_sample.ui.RainbowColorPainter;
import jzy_3d_sample.ui.SouthpanelController;
import jzy_3d_sample.ui.ZoomPanelController;
import org.apache.commons.io.FileUtils;
import org.apache.commons.math3.complex.Complex;
import org.jzy3d.colors.Color;
import org.jzy3d.plot3d.primitives.Point;

/**
 *
 * @author user
 */
public class Main extends Application implements AngleSelectionHandler, Context, EffectivePointHandler {

    private static final boolean TEST = false;
    private List<Mesh> meshs = new ArrayList<>();
    private RenderModel renderModel = null;
    private Scene scene = null;
    private BorderPane container = null;
    private List<Cube> subCubes = new ArrayList<>();
    File subCubeRoot = null;
    VBox colorLegend = null;
    FXMLLoader legendloader = null;
    String RCSTotal = "";
    private SouthpanelController southpanelController = null;
    private RCSvalueController rCSvalueController = null;
    private ZoomPanelController zoomPanelController = null;
    private AnglePanelController anglePanelController = null;
    private ObservableList<Vertex> effectivePoints=null;

    private Map<Integer, Double> angle2RcsThreshold = new HashMap<>();//store selected angle to rcs threshold
    private int currentAngleIndex = 0;
    private ColorPaintingModel colorPaintingModel = null;

    private RenderModel loadRenderModel(Stage primaryStage, List<Mesh> meshs) {
        this.meshs = meshs;
        this.renderModel = new RenderModel(scene, primaryStage, meshs);
        colorPaintingModel = new ColorPaintingModel(this);
        return this.renderModel;
    }

    private RenderModel loadRenderModel(Stage primaryStage, File savedFile) {
        try {
            subCubeRoot = savedFile;
            this.renderModel = new RenderModel(scene, primaryStage, savedFile);
            this.meshs = renderModel.getProjectModel().getMeshes();
            subCubes = renderModel.getProjectModel().getCubes();
            colorPaintingModel = new ColorPaintingModel(this);
            colorPaintingModel.setColorPaintingMode(ColorPaintingMode.RAINBOW);
            return this.renderModel;
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            Read_data r = new Read_data();
            MenuBar menuBar = new MenuBar();
            menuBar.setStyle("-fx-font-size: 11pt;");
            Menu fileMenu = new Menu("File");
            MenuItem fileOpenObjItem = new MenuItem("Open Obj");
            fileMenu.getItems().add(fileOpenObjItem);
//            MenuItem fileRCSMenuItem = new MenuItem("RCS資料輸入");
//            fileMenu.getItems().add(fileRCSMenuItem);
            MenuItem researchMenuItem = new MenuItem("研改輸出");
//            researchMenuItem.setDisable(true);
            fileMenu.getItems().add(researchMenuItem);
            fileMenu.getItems().add(new SeparatorMenuItem());
            MenuItem fileExitMenuItem = new MenuItem("Exit");
            fileMenu.getItems().add(fileExitMenuItem);

            fileOpenObjItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    try {
                        angle2RcsThreshold.clear();
                        subCubes.clear();
                        meshs.clear();
                        currentAngleIndex = 0;

                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/fileopen_obj.fxml"));
                        Parent root1 = (Parent) fxmlLoader.load();
                        FileOpenObjController fileOpenObjController = fxmlLoader.getController();
                        Stage stage = new Stage();
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.initStyle(StageStyle.UNDECORATED);
                        stage.setTitle("Open File...");
                        stage.setScene(new Scene(root1));
                        stage.showAndWait();
                        if (fileOpenObjController.isOk()) {

                            new BackgroundRunner(southpanelController) {
                                ObservableList angleList;
                                @Override

                                public void runBeforeWorkerThread() {
                                    southpanelController.setStatus("Open data......");
                                }

                                @Override
                                public void runInWorkerThread() {
                                    File ObjFile = fileOpenObjController.getobjFile();
                                    renderModel = loadRenderModel(primaryStage, ObjFile);
                                    zoomPanelController.setRenderModel(renderModel);
                                    angleList = FXCollections.observableArrayList();
                                    int angle = 0;
                                    for (String currentData : renderModel.getProjectModel().getCurrentDataList()) {
                                        angleList.add(currentData);

                                        //處理讀進來的 rcs 清單
                                        //需要改成 ondemand 的方式
                                        if (angle == 0) {
                                            try {
                                                CurrentData cd = renderModel.getProjectModel().getCurrentData("angle" + (angle));
//                                                System.out.println("cd.getRcs().length=" + cd.getRcs().length);
//                                                System.out.println(Arrays.toString(cd.getRcs()));
                                                for (int i = 0; angle == 0 && i < cd.getRcs().length - 1; i++) {
                                                    subCubes.get(i).setRcs(Double.valueOf(cd.getRcs()[i]));
                                                }
                                                //設定RCS總值
                                                RCSTotal = "" + cd.getRcsTotal();
//                                                System.out.println(RCSTotal);

                                            } catch (Exception ex) {
                                                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                        }
                                        angle++;

                                        colorLegend.setPrefWidth(63);
                                        colorLegend.setVisible(true);
                                        renderModel.repaint();
                                    }
                                    
                                    
                                }

                                @Override
                                public void runInUIThread() {
                                    TabPane tabPane = new TabPane();
                                    tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
                                    ScrollPane scrollPane = new ScrollPane();
                                    tabPane.getTabs().add(new Tab("Main View", scrollPane));
                                    container.setCenter(tabPane);
                                    scrollPane.setContent(renderModel.getView());
                                    renderModel.repaint();
                                    southpanelController.setStatus("Done");
                                    colorPaintingModel.setRcsThresholdForHighlight(Double.valueOf(rCSvalueController.getThreshold()));
                                    resetColor();
                                    //加入所有角度資料於清單
                                    anglePanelController.getAnglelist().setItems(angleList);
                                    //加入所有等效散射點清單
                                    effectivePoints=FXCollections.observableArrayList();
                                    for(Cube c : subCubes){
                                        effectivePoints.add(c.getEffectivePoint());
                                    }
                                    anglePanelController.getEffective_point_list().setItems(effectivePoints);
                                    //顯示RCSTotal
                                    southpanelController.setTextBeforeValue(RCSTotal);
                                    resetSlider();
                                    rCSvalueController.getTo_db_check().setOnAction(new EventHandler<ActionEvent>() {
                                        @Override
                                        public void handle(ActionEvent event) {
                                            renderModel.repaint();
                                            rCSvalueController.repaint();
//                                            System.out.println(rCSvalueController.get_to_db_checkisOK());
                                            if (!rCSvalueController.get_to_db_checkisOK()) {
                                                southpanelController.setTextBeforeValue(RCSTotal);
                                            } else {
                                                southpanelController.setTextBeforeValue("" + rCSvalueController.to_dbvalue(Double.valueOf(RCSTotal)));
                                            }
                                        }
                                    });

                                }

                            }.start();

                        }
                    } catch (IOException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (Exception ex) {
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
                                researchFile = new File(researchFile, renderModel.getProjectModel().getCurrentDataList(false).get(currentAngleIndex).replace(',', '_'));
                                FileUtils.forceMkdir(researchFile);
                                FastN2fWriter.writeTriFile(rmeshs, new File(researchFile, "0.tri"));
                                FastN2fWriter.writeCurMFile(rmeshs, new File(researchFile, "0.curM"));
                                FastN2fWriter.writeCurJFile(rmeshs, new File(researchFile, "0.curJ"));
//                                ZipFile zipFile = new ZipFile(new File("research_" + subCubeRoot.getName() + ".zip"));
//                                zipFile.addFolder(researchFile.getParentFile());
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
//                                FileUtils.deleteDirectory(researchFile.getParentFile());
                            } catch (Exception ex) {
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

            Menu toolMenu = new Menu("Tools");
            MenuItem openMeshConverterMenuItem = new MenuItem("開啓 MeshConverter");
            //menuBar.getMenus().add(toolMenu);
            toolMenu.getItems().add(openMeshConverterMenuItem);
            openMeshConverterMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    ProcessBuilder pb = new ProcessBuilder();
                    File jarFile = new File("jzy3d_test-1.1-jar-with-dependencies.jar");
                    if (jarFile.exists()) {
                        pb = pb.directory(new File("."));
                    } else {
                        pb = pb.directory(new File("dist"));
                    }
                    pb = pb.redirectErrorStream(true);
                    if ("linux".equals(System.getProperty("os.name").toLowerCase())) {
                        pb = pb.command("linux_jre/bin/java", "-Xmx10G", "-XX:+UseG1GC", "-cp", "./jzy3d_test-1.1-jar-with-dependencies.jar", "jzy_3d_sample.MeshConverter");
                    } else {
                        pb = pb.command("jre\\bin\\java", "-Xmx10G", "-XX:+UseG1GC", "-cp", "./jzy3d_test-1.1-jar-with-dependencies.jar", "jzy_3d_sample.MeshConverter");
                    }
                    try {
                        Process process = pb.start();
                    } catch (IOException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });

            BorderPane menuBarContainer = new BorderPane();
            menuBarContainer.setTop(menuBar);
            scene = new Scene(menuBarContainer, 1100, 768);
            container = new BorderPane();
            menuBarContainer.setCenter(container);

            FXMLLoader rcsvalueFxmlLoader = new FXMLLoader(getClass().getResource("/fxml/rcsvaluepanel.fxml"));
            AnchorPane rcsvalueRoot = (AnchorPane) rcsvalueFxmlLoader.load();
            rCSvalueController = rcsvalueFxmlLoader.getController();
            container.setTop(rcsvalueRoot);
            rCSvalueController.setActionHandler(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    double threshold = Double.valueOf(rCSvalueController.getThreshold());
                    angle2RcsThreshold.put(currentAngleIndex, threshold);
                    colorPaintingModel.setRcsThresholdForHighlight(threshold);
                    resetColor();
                    double sum = 0;
                    for (Cube cube : subCubes) {
                        if (cube.getRcs() < threshold) {
                            sum += cube.getRcs();
                        }
                    }
                    renderModel.repaint();
                }
            });

            FXMLLoader anglepanelFXMLLoader = new FXMLLoader(getClass().getResource("/fxml/anglepanel.fxml"));
            VBox anglepanelRoot = (VBox) anglepanelFXMLLoader.load();
            anglePanelController = anglepanelFXMLLoader.getController();
            anglePanelController.setAngleSelectionHandler(this);
            anglePanelController.setEffectivePointHandler(this);
            container.setLeft(anglepanelRoot);

            FXMLLoader southpanelFxmlLoader = new FXMLLoader(getClass().getResource("/fxml/southpanel.fxml"));
            AnchorPane southpanelRoot = (AnchorPane) southpanelFxmlLoader.load();
            southpanelController = southpanelFxmlLoader.getController();
            container.setBottom(southpanelRoot);

            BorderPane rightToolBarContainer = new BorderPane();
            container.setRight(rightToolBarContainer);
            legendloader = new FXMLLoader(getClass().getResource("/fxml/legend.fxml"));
            colorLegend = (VBox) legendloader.load();
            colorLegend.setPrefWidth(0);
            colorLegend.setVisible(false);
            colorLegend.setPadding(new Insets(10));

            FXMLLoader zoompanelFxmlLoader = new FXMLLoader(getClass().getResource("/fxml/zoompanel.fxml"));
            AnchorPane zoomPanelRoot = (AnchorPane) zoompanelFxmlLoader.load();
            zoomPanelController = zoompanelFxmlLoader.getController();
            rightToolBarContainer.setTop(colorLegend);
            rightToolBarContainer.setBottom(zoomPanelRoot);
            rightToolBarContainer.setStyle("-fx-border-style: solid; -fx-border-width: 1px; -fx-border-color: rgb(217,213,212)");
//            rightToolBarContainer.getChildren().addAll(colorLegend, zoomPanelRoot);
            primaryStage.setTitle("CS");
            primaryStage.setScene(scene);

            primaryStage.show();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void printRCS(List<Cube> cubes) {
        List<Double> list = new ArrayList<>();
        for (Cube cube : cubes) {
            list.add(cube.getRcs());
        }
        System.out.println("cube rcs=" + Arrays.deepToString(list.toArray()));
    }

    
    private void resetColor() {
        List<Cube> colorCubes = new ArrayList<>(subCubes);
        double rcsThreshold=colorPaintingModel.getRcsThresholdForHighlight();
        double gap=colorPaintingModel.getRcsGapForRainbowLevels();
        colorPaintingModel.update();

        LegendController legendController = legendloader.getController();
        legendController.getRedValue().setText(String.format("%06.3f", rcsThreshold));
        legendController.getoValue().setText(String.format("%06.3f", rcsThreshold - gap));
        legendController.getyValue().setText(String.format("%06.3f", rcsThreshold - 2 * gap));
        legendController.getgValue().setText(String.format("%06.3f", rcsThreshold - 3 * gap));
        legendController.getbValue().setText(String.format("%06.3f", rcsThreshold - 4 * gap));
        legendController.getbValue1().setText(String.format("%06.3f", rcsThreshold - 5 * gap));

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void angleSelectionChanged(final int index) {
        try {
            new BackgroundRunner(southpanelController) {
                @Override
                public void runBeforeWorkerThread() {
                    southpanelController.setStatus("Loading angle " + index + "......");
                }

                @Override
                public void runInWorkerThread() {
                    try {
                        Read_data r = new Read_data();
//                        System.out.println("index="+index);
                        CurrentData cd = renderModel.getProjectModel().getCurrentData(index);
                        cd.getOsRecordsMap().setHomeFolder(renderModel.getProjectModel().getHomeFolder());
                        //set rcs to cubes
                        System.out.println(Arrays.toString(cd.getRcs()));
                        for (int i = 0; i < cd.getRcs().length - 1; i++) {
                            subCubes.get(i).setRcs(Double.valueOf(cd.getRcs()[i]));
                        }
                        RCSTotal = "" + cd.getRcsTotal();
                        southpanelController.setTextBeforeValue(RCSTotal);
                        //set current to meshes
                        System.out.println("apply current");
                        r.applyOStoMeshes(meshs, cd.getOsRecordsMap());

                        System.out.println("done apply current");
                    } catch (Exception ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                @Override
                public void runInUIThread() {
                    //reset UI
                    resetSlider();
                    currentAngleIndex = index;
                    if (angle2RcsThreshold.containsKey(currentAngleIndex)) {
                        double threshold = angle2RcsThreshold.get(currentAngleIndex);
                        colorPaintingModel.setRcsThresholdForHighlight(threshold);
                        resetColor();
                        rCSvalueController.setThreshold(threshold);
                    } else {
                        colorPaintingModel.setRcsThresholdForHighlight(0);
                        resetColor();
                    }
                    renderModel.repaint();
                    rCSvalueController.repaint();
                    southpanelController.setStatus("Done");
                }
            }.start();

        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void resetSlider() {
        //設定Slider值
        List<Cube> Cubes = rCSvalueController.sortCube(subCubes);
        rCSvalueController.setSlidermax(Cubes.get(Cubes.size() - 1).getRcs());
        rCSvalueController.setSlidermin(Cubes.get(0).getRcs());
        rCSvalueController.setSlidervalue(Cubes.get(0).getRcs());
        double majortick = ((Cubes.get(Cubes.size() - 1).getRcs()) - (Cubes.get(0).getRcs())) / 20;
        rCSvalueController.setSliderMajorTickUnit(majortick);
    }

    @Override
    public List<Cube> getSubCubes() {
        return this.subCubes;
    }

    @Override
    public RenderModel getRenderModel() {
        return this.renderModel;
    }

    @Override
    public SouthpanelController getSouthpanelController() {
        return southpanelController;
    }

    @Override
    public void EffectivePointChanged(int index) {
        colorPaintingModel.setColorPaintingMode(ColorPaintingMode.EFFECTIVE_POINTS);
        colorPaintingModel.setSelectedEffectivePoint(effectivePoints.get(index));
        resetColor();
    }
}
