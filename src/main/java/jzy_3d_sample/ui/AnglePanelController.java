package jzy_3d_sample.ui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import jzy_3d_sample.model.ColorPaintingMode;
import jzy_3d_sample.model.ColorPaintingModel;
import jzy_3d_sample.model.Vertex;
import org.controlsfx.control.ToggleSwitch;

public class AnglePanelController {

    private AngleSelectionHandler angleSelectionHandler = null;
    @FXML
    private HBox toggleButtonContainer;

    @FXML
    private ListView<?> anglelist;

    @FXML
    private ListView<Vertex> effective_point_list;
    private Context context = null;

    private ToggleSwitch toggleSwitch = null;

    public void init(Context context) {
        this.context = context;
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
                        if (effective_point_list.getSelectionModel().getSelectedIndex() == -1) {
                            effective_point_list.getSelectionModel().select(0);
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

    public ListView<Vertex> getEffective_point_list() {
        return effective_point_list;
    }

    @FXML
    void onShowButtonClicked(ActionEvent event) {
//        System.out.println("anglelist_index" + anglelist.getSelectionModel().getSelectedIndex());
        if (this.angleSelectionHandler != null) {
            this.angleSelectionHandler.angleSelectionChanged(anglelist.getSelectionModel().getSelectedIndex());
        }
    }

    @FXML
    void onShow_EffectivePoint(ActionEvent event) {

        BackgroundRunner runner = new BackgroundRunner(context.getSouthpanelController()) {
            @Override
            public void runInWorkerThread() {
                context.getColorPaintingModel().setColorPaintingMode(ColorPaintingMode.EFFECTIVE_POINTS);
                context.getColorPaintingModel().setSelectedEffectivePoint(effective_point_list.getSelectionModel().getSelectedItem());
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
