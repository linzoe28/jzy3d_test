package jzy_3d_sample.ui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import jzy_3d_sample.model.ColorPaintingMode;
import jzy_3d_sample.model.ColorPaintingModel;
import jzy_3d_sample.model.EffectivePointModel;
import jzy_3d_sample.model.Vertex;
import org.controlsfx.control.ToggleSwitch;

public class AnglePanelController {

    private AngleSelectionHandler angleSelectionHandler = null;
    @FXML
    private HBox toggleButtonContainer;

    @FXML
    private ListView<?> anglelist;
    @FXML
    private TableView<EffectivePointModel> effectivePointsTable;

    private Context context = null;

    private ToggleSwitch toggleSwitch = null;

    public void init(Context context) {
        this.context = context;
        TableColumn xColumn=new TableColumn("X");
        xColumn.setCellValueFactory(new PropertyValueFactory<EffectivePointModel, Double>("x"));
        TableColumn yColumn=new TableColumn("Y");
        yColumn.setCellValueFactory(new PropertyValueFactory<EffectivePointModel, Double>("y"));
        TableColumn zColumn=new TableColumn("Z");
        yColumn.setCellValueFactory(new PropertyValueFactory<EffectivePointModel, Double>("z"));
        TableColumn rcsColumn=new TableColumn("RCS");
        rcsColumn.setCellValueFactory(new PropertyValueFactory<EffectivePointModel, Double>("rcs"));
        rcsColumn.setSortType(TableColumn.SortType.DESCENDING);
        rcsColumn.setSortable(true);
        effectivePointsTable.getColumns().addAll(xColumn, yColumn, zColumn, rcsColumn);
        effectivePointsTable.setItems(FXCollections.observableArrayList());
        effectivePointsTable.getSortOrder().add(rcsColumn);
        xColumn.setCellFactory(new Callback<TableColumn<EffectivePointModel, Double>, TableCell<EffectivePointModel, Double>>() {

            @Override
            public TableCell<EffectivePointModel, Double> call(TableColumn<EffectivePointModel, Double> param) {
                return new EffectivePointTableCell<>();
            }
        });
        yColumn.setCellFactory(new Callback<TableColumn<EffectivePointModel, Double>, TableCell<EffectivePointModel, Double>>() {

            @Override
            public TableCell<EffectivePointModel, Double> call(TableColumn<EffectivePointModel, Double> param) {
                return new EffectivePointTableCell<>();
            }
        });
        zColumn.setCellFactory(new Callback<TableColumn<EffectivePointModel, Double>, TableCell<EffectivePointModel, Double>>() {

            @Override
            public TableCell<EffectivePointModel, Double> call(TableColumn<EffectivePointModel, Double> param) {
                return new EffectivePointTableCell<>();
            }
        });
        rcsColumn.setCellFactory(new Callback<TableColumn<EffectivePointModel, Double>, TableCell<EffectivePointModel, Double>>() {

            @Override
            public TableCell<EffectivePointModel, Double> call(TableColumn<EffectivePointModel, Double> param) {
                return new EffectivePointTableCell<>();
            }
        });
    }

    @FXML
    public void initialize() {
        Label mode1 = new Label("一般顯示");
//        mode1.setStyle("-fx-background-color: red");
        Label mode2 = new Label("等效顯示");
        toggleSwitch = new ToggleSwitch();
        toggleSwitch.setAlignment(Pos.CENTER);
        toggleSwitch.setPrefWidth(40);
//        toggleSwitch.setStyle("-fx-background-color: red");
        toggleButtonContainer.getChildren().addAll(mode1, toggleSwitch, mode2);
        toggleSwitch.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                BackgroundRunner runner = new BackgroundRunner(context.getSouthpanelController()) {

                    @Override
                    public void runInWorkerThread() {
                        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                        ColorPaintingModel colorPaintingModel = context.getColorPaintingModel();
                        if (newValue) {
                            colorPaintingModel.setColorPaintingMode(ColorPaintingMode.EFFECTIVE_POINTS);
                        } else {
                            colorPaintingModel.setColorPaintingMode(ColorPaintingMode.RAINBOW);
                        }
                        context.resetColor();
                    }

                    @Override
                    public void runInUIThread() {

                    }

                    @Override
                    public void runBeforeWorkerThread() {
                        if (effectivePointsTable.getSelectionModel().getSelectedIndex() == -1) {
                            effectivePointsTable.getSelectionModel().select(0);
                        }
                    }
                };
                runner.start();

            }
        });
    }

    public ListView<?> getAnglelist() {
        return anglelist;
    }

    public AngleSelectionHandler getAngleSelectionHandler() {
        return angleSelectionHandler;
    }

    public void setAngleSelectionHandler(AngleSelectionHandler angleSelectionHandler) {
        this.angleSelectionHandler = angleSelectionHandler;
    }

    public TableView<EffectivePointModel> getEffectivePointsTable() {
        return effectivePointsTable;
    }


    @FXML
    void onShowButtonClicked(ActionEvent event) {
//        System.out.println("anglelist_index" + anglelist.getSelectionModel().getSelectedIndex());
        if (this.angleSelectionHandler != null) {
            this.angleSelectionHandler.angleSelectionChanged(anglelist.getSelectionModel().getSelectedIndex());
        }
    }
    
    @FXML
    void onShow_EffectivePoint2(ActionEvent event) {
        BackgroundRunner runner = new BackgroundRunner(context.getSouthpanelController()) {
            @Override
            public void runInWorkerThread() {
                context.getColorPaintingModel().setColorPaintingMode(ColorPaintingMode.EFFECTIVE_POINTS);
                EffectivePointModel model=effectivePointsTable.getSelectionModel().getSelectedItem();
                context.getColorPaintingModel().setSelectedEffectivePoint(new Vertex(model.getX(),model.getY(), model.getZ()));
                context.resetColor();
            }

            @Override
            public void runInUIThread() {
            }

            @Override
            public void runBeforeWorkerThread() {
                toggleSwitch.setSelected(true);
            }

        };
        runner.start();
    }

    @FXML
    void onShow_EffectivePoint(ActionEvent event) {

        BackgroundRunner runner = new BackgroundRunner(context.getSouthpanelController()) {
            @Override
            public void runInWorkerThread() {
                context.getColorPaintingModel().setColorPaintingMode(ColorPaintingMode.EFFECTIVE_POINTS);
                context.getColorPaintingModel().setSelectedEffectivePoint(effectivePointsTable.getSelectionModel().getSelectedItem().getVertex());
                context.resetColor();
            }

            @Override
            public void runInUIThread() {
            }

            @Override
            public void runBeforeWorkerThread() {
                toggleSwitch.setSelected(true);
            }

        };
        runner.start();
    }

//    @FXML
//    void onEffectivePointToggle(ActionEvent event) {
//
//    }
}
