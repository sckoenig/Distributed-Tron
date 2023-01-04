package vsp.trongame.app.view;

import edu.cads.bai5.vsp.tron.view.Coordinate;
import edu.cads.bai5.vsp.tron.view.ITronView;
import javafx.application.Platform;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import vsp.trongame.app.controller.ITronController;
import vsp.trongame.app.model.IUpdateListener;
import vsp.trongame.app.view.overlays.CountdownOverlay;
import vsp.trongame.app.view.overlays.EndingOverlay;

import java.util.*;

/**
 * Listens to model updates and updates the view accordingly.
 */
public class UpdateListener implements IUpdateListener {

    private ITronView mainView;
    private CountdownOverlay countdownOverlay;
    private EndingOverlay endingOverlay;
    private ITronController mainController;
    private Map<String, Set<Coordinate>> coordinates;

    public void initialize(ITronView mainView, CountdownOverlay countdownOverlay, EndingOverlay endingOverlay, ITronController mainController) {
        this.mainView = mainView;
        this.countdownOverlay = countdownOverlay;
        this.endingOverlay = endingOverlay;
        this.mainController = mainController;
        this.coordinates = new HashMap<>();
    }

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
