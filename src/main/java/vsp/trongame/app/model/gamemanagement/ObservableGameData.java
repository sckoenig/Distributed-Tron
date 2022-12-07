package vsp.trongame.app.model.gamemanagement;

import edu.cads.bai5.vsp.tron.view.Coordinate;
import edu.cads.bai5.vsp.tron.view.ITronView;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.scene.paint.Color;
import vsp.trongame.app.model.ITronModel;
import vsp.trongame.app.model.datatypes.GameResult;
import vsp.trongame.app.model.datatypes.TronColor;

import java.util.*;

/**
 * Represents both the Game's Data and the observable Model.
 */
public class ObservableGameData implements IGameData, ITronModel.IObservableTronModel {

    private final StringProperty observableResultColor;
    private final StringProperty observableResultText;
    private final IntegerProperty observableCountDownCounter;
    private final ObservableMap<String, Color> observableKeyMappings;
    private final ITronView view;

    public ObservableGameData(ITronView view) {
        this.view = view;
        this.observableCountDownCounter = new SimpleIntegerProperty(0);
        this.observableResultText = new SimpleStringProperty();
        this.observableResultColor = new SimpleStringProperty();
        this.observableKeyMappings = FXCollections.observableHashMap();
    }

    @Override
    public void updateGameResult(TronColor winner, GameResult result) {
        Platform.runLater(() -> {
            this.observableResultColor.set(winner.getColor().toString());
            this.observableResultText.set(result.getResultText());
        });
    }

    @Override
    public void updateCountDownCounter(int value) {
        Platform.runLater(() -> this.observableCountDownCounter.set(value));
    }

    @Override
    public void updateKeyMappings(Map<String, TronColor> mappings) {
        for (Map.Entry<String, TronColor> entry: mappings.entrySet()){
            this.observableKeyMappings.put(entry.getKey(), entry.getValue().getColor());
        }
    }

    @Override
    public void updateArenaSize(int rows, int columns) {
        view.setColumns(columns);
        view.setRows(rows);
    }

    @Override
    public void updatePlayers(Map<TronColor, List<Coordinate>> players) {
        for (Map.Entry<TronColor, List<Coordinate>> player: players.entrySet()) {
            view.draw(player.getValue(), player.getKey().getColor());
        }
    }

    @Override
    public StringProperty getObservableResultColor() {
        return this.observableResultColor;
    }

    @Override
    public StringProperty getObservableResultText() {
        return this.observableResultText;
    }

    @Override
    public IntegerProperty getObserverableCountDownCounter() {
        return this.observableCountDownCounter;
    }

    @Override
    public ObservableMap<String, Color> getObservableKeyMappings() {
        return this.observableKeyMappings;
    }

}
