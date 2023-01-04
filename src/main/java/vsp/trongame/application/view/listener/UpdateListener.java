package vsp.trongame.application.view.listener;

import edu.cads.bai5.vsp.tron.view.Coordinate;
import edu.cads.bai5.vsp.tron.view.ITronView;
import javafx.application.Platform;
import javafx.scene.input.KeyEvent;
import vsp.trongame.application.controller.ITronController;
import vsp.trongame.application.model.IUpdateListener;
import vsp.trongame.application.view.overlays.CountdownOverlay;
import vsp.trongame.application.view.overlays.EndingOverlay;

import java.util.*;

/**
 * Listens to model updates and updates the view accordingly.
 */
public class UpdateListener implements IUpdateListener {

    private ITronView mainView;
    private CountdownOverlay countdownOverlay;
    private EndingOverlay endingOverlay;
    private ITronController mainController;
    private Map<Integer, Set<Coordinate>> coordinates;

    public void initialize(ITronView mainView, CountdownOverlay countdownOverlay, EndingOverlay endingOverlay, ITronController mainController) {
        this.mainView = mainView;
        this.countdownOverlay = countdownOverlay;
        this.endingOverlay = endingOverlay;
        this.mainController = mainController;
        this.coordinates = new HashMap<>();
    }

    @Override
    public void updateOnKeyMappings(Map<String, Integer> mappings) {
        Map<String, String> toColorMapping = new HashMap<>();
        for (Map.Entry<String, Integer> entry: mappings.entrySet()) {
            toColorMapping.put(entry.getKey(), CoordinateColor.getByOrdinal(entry.getValue()).getHex());
        }
        countdownOverlay.setKeyMappings(toColorMapping);
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
    public void updateOnGameResult(Integer id, String result) {
        Platform.runLater(() -> {
            endingOverlay.setResult(result, CoordinateColor.getByOrdinal(id).getHex());
            mainView.clear();
            coordinates.clear();
            countdownOverlay.reset();
        });
    }

    @Override
    public void updateOnCountDown(int value) {
        Platform.runLater(() -> countdownOverlay.setCounterLabelValue(value));
    }

    @Override
    public void updateOnField(Map<Integer, List<Coordinate>> field) {
        Platform.runLater(() -> {
            mainView.clear();
            for (Map.Entry<Integer, List<Coordinate>> entry : field.entrySet()) {
                Integer key = entry.getKey();
                coordinates.computeIfAbsent(key, v -> new HashSet<>()).addAll(entry.getValue());
                List<Coordinate> coords = new ArrayList<>(coordinates.get(key));
                mainView.draw(coords, CoordinateColor.getByOrdinal(key).getColor());
            }
        });
    }


}
