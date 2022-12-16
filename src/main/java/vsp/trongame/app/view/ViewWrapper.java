package vsp.trongame.app.view;

import edu.cads.bai5.vsp.tron.view.Coordinate;
import edu.cads.bai5.vsp.tron.view.ITronView;
import edu.cads.bai5.vsp.tron.view.TronView;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import vsp.trongame.app.controller.ITronController;
import vsp.trongame.app.model.ITronModel;
import vsp.trongame.app.view.overlays.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ViewWrapper implements IViewWrapper, ITronModel.IUpdateListener {

    private final ITronView mainView;
    private CountdownOverlay countdownOverlay;
    private EndingOverlay endingOverlay;
    private MenuOverlay menuOverlay;
    private WaitingOverlay waitingOverlay;

    private ITronController mainController;
    private int registrationID;

    public ViewWrapper() throws IOException {
        this.mainView = new TronView();
    }

    public void initialize(MenuOverlay menuOverlay, WaitingOverlay waitingOverlay, CountdownOverlay countdownOverlay, EndingOverlay endingOverlay, ITronModel model, ITronController mainController, int defaultPlayerCount) {
        this.menuOverlay = menuOverlay;
        this.waitingOverlay = waitingOverlay;
        this.countdownOverlay = countdownOverlay;
        this.endingOverlay = endingOverlay;
        this.mainController = mainController;
        menuOverlay.initialize(mainController, defaultPlayerCount);
        mainView.clear();
        model.registerListener(this);
    }

    @Override
    public Scene getScene() {
        return mainView.getScene();
    }

    @Override
    public void registerOverlay(String overlayName, Node node) {
        mainView.registerOverlay(overlayName, node);
    }


    @Override
    public void updateOnKeyMappings(Map<String, String> mappings) {
        countdownOverlay.setKeyMappings(mappings);
    }

    @Override
    public void updateOnRegistration(int id) {
        this.registrationID = id;
        this.menuOverlay.setId(registrationID);
        mainView.getScene().setOnKeyPressed((KeyEvent event) -> mainController.handleKeyEvent(registrationID, event));
    }

    @Override
    public void updateOnArena(int rows, int columns) {
        mainView.setRows(rows);
        mainView.setColumns(columns);
    }

    @Override
    public void updateOnState(String state) {
        mainView.hideOverlays();
        mainView.showOverlay(state);
    }

    @Override
    public void updateOnGameStart() {
        mainView.hideOverlays();
    }

    @Override
    public void updateOnGameResult(String color, String result) {
        Platform.runLater(() -> endingOverlay.setResult(result, color));
        mainView.clear();
    }

    @Override
    public void updateOnCountDown(int value) {
        Platform.runLater(() -> countdownOverlay.setCounterLabel(value));
    }

    @Override
    public void updateOnField(Map<String, List<Coordinate>> field) {
        for (Map.Entry<String, List<Coordinate>> entry : field.entrySet()) {
            mainView.draw(entry.getValue(), Color.valueOf(entry.getKey()));
        }
    }
}
