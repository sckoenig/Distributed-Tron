package vsp.trongame.app.model.gamemanagement;

import edu.cads.bai5.vsp.tron.view.Coordinate;
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
    private final ObservableMap<Color, List<Coordinate>> observablePlayers;
    private final ObservableMap<String, Color> observableKeyMappings;
    private final IntegerProperty observableArenaRowCount;
    private final IntegerProperty observableArenaColumnCount;
    private final StringProperty observableModelState;

    public ObservableGameData() {
        this.observableArenaRowCount = new SimpleIntegerProperty();
        this.observableArenaColumnCount = new SimpleIntegerProperty();
        this.observableCountDownCounter = new SimpleIntegerProperty(4);
        this.observableResultText = new SimpleStringProperty();
        this.observableResultColor = new SimpleStringProperty();
        this.observablePlayers = FXCollections.observableHashMap();
        this.observableKeyMappings = FXCollections.observableHashMap();
        this.observableModelState = new SimpleStringProperty();
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
        Platform.runLater(() -> this.observableCountDownCounter.set(this.observableCountDownCounter.getValue()-value));
    }

    @Override
    public void updateKeyMappings(Map<String, TronColor> mappings) {
        for (Map.Entry<String, TronColor> entry: mappings.entrySet()){
            this.observableKeyMappings.put(entry.getKey(), entry.getValue().getColor());
        }
    }

    @Override
    public void updateArenaSize(int rows, int columns) {
        this.observableArenaColumnCount.set(columns);
        this.observableArenaRowCount.set(rows);
    }

    @Override
    public void updatePlayers(Map<TronColor, List<Coordinate>> players) {
        Platform.runLater(() -> {
            for (Map.Entry<TronColor, List<Coordinate>> entry: players.entrySet()){
                this.observablePlayers.put(entry.getKey().getColor(), new ArrayList<>(entry.getValue())); //new value, so change is triggered
            }
        });
    }

    @Override
    public void updateState(String state) {
        Platform.runLater(() -> this.observableModelState.set(state));
        if (state.equals("RUNNING")) observableCountDownCounter.set(4);
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

    @Override
    public ObservableMap<Color, List<Coordinate>> getObservablePlayers() {
        return this.observablePlayers;
    }

    @Override
    public StringProperty getObservableState() {
        return this.observableModelState;
    }
}
