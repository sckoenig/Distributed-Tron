package vsp.trongame.app.view;

import edu.cads.bai5.vsp.tron.view.Coordinate;
import edu.cads.bai5.vsp.tron.view.ITronView;
import edu.cads.bai5.vsp.tron.view.TronView;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import vsp.trongame.app.controller.ITronController;
import vsp.trongame.app.model.ITronModel;
import vsp.trongame.app.view.overlays.*;

import java.io.IOException;
import java.util.*;

public class ViewWrapper implements IViewWrapper, ITronModel.IUpdateListener {

    private ITronView mainView;
    private CountdownOverlay countdownOverlay;
    private EndingOverlay endingOverlay;
    private MenuOverlay menuOverlay;
    private ITronController mainController;
    private final Map<String, Set<Coordinate>> coordinates;

    public ViewWrapper() {
        this.coordinates = new HashMap<>();
    }

    @Override
    public void initialize(ITronModel model, ITronController mainController, int height, int width, int defaultPlayerCount,
                           Map<String, String> mapping) throws IOException {

        this.mainView = new TronView();
        this.mainController = mainController;

        Parent root;
        FXMLLoader loader;
        for (Map.Entry<String, String> overlay : mapping.entrySet()) {

            String fxml = overlay.getValue();
            String identifier = overlay.getKey();
            loader = new FXMLLoader(getClass().getResource(fxml));
            root = loader.load();
            root.minHeight(600);
            root.minWidth(750);
            mainView.registerOverlay(identifier, root);

            switch (overlay.getKey()) {
                case MenuOverlay.IDENTIFIER -> menuOverlay = loader.getController();
                case CountdownOverlay.IDENTIFIER -> countdownOverlay = loader.getController();
                case EndingOverlay.IDENTIFIER -> endingOverlay = loader.getController();
                default -> {
                }
            }
        }

        menuOverlay.initialize(mainController, defaultPlayerCount);
        mainView.clear();
        model.registerUpdateListener(this);
    }

    @Override
    public Scene getScene() {
        return mainView.getScene();
    }


    /* LISTENER */

    @Override
    public void updateOnKeyMappings(Map<String, String> mappings) {
        countdownOverlay.setKeyMappings(mappings);
    }

    @Override
    public void updateOnRegistration(int id) {
        mainView.getScene().setOnKeyPressed((KeyEvent event) -> mainController.handleKeyEvent(id, event));
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
        Platform.runLater(() -> {
            endingOverlay.setResult(result, color);
            mainView.clear();
            coordinates.clear();
            countdownOverlay.reset();
        });
    }

    @Override
    public void updateOnCountDown(int value) {
        Platform.runLater(() -> countdownOverlay.setCounterLabel(value));
    }

    @Override
    public void updateOnField(Map<String, List<Coordinate>> field) {
        Platform.runLater(() -> {
            mainView.clear();
            for (Map.Entry<String, List<Coordinate>> entry : field.entrySet()) {
                String key = entry.getKey();
                coordinates.computeIfAbsent(key, v -> new HashSet<>()).addAll(entry.getValue());
                List<Coordinate> coords = new ArrayList<>(coordinates.get(key));
                mainView.draw(coords, Color.valueOf(key));
            }
        });
    }

}

