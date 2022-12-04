package vsp.trongame.app.model.gamemanagement;

import edu.cads.bai5.vsp.tron.view.Coordinate;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.scene.paint.Color;
import vsp.trongame.app.model.ITronModel;
import vsp.trongame.app.model.datatypes.GameResult;
import vsp.trongame.app.model.datatypes.TronColor;

import java.util.List;
import java.util.Map;

public class ObservableGameData implements IGameData, ITronModel.IObservableTronModel {

    private final StringProperty observableResultColor;
    private final StringProperty observableGameResult;
    private final IntegerProperty observableCountDownCounter;
    private final MapProperty<String, Coordinate> observablePlayers;
    private final ObservableMap<String, Coordinate> players;

    public ObservableGameData() {
        this.observableCountDownCounter = new SimpleIntegerProperty(4);
        this.observableGameResult = new SimpleStringProperty();
        this.observableResultColor = new SimpleStringProperty();
        this.players = FXCollections.observableHashMap();
        this.observablePlayers = new SimpleMapProperty<>();
        this.observablePlayers.set(players);
    }

    @Override
    public void updateGameResult(TronColor winner, GameResult result) {

    }

    @Override
    public void updateKeyMappings(Map<String, Coordinate> mappings) {

    }

    @Override
    public void updateArenaSize(int rows, int columns) {

    }

    @Override
    public void updatePlayers(Map<Color, List<Coordinate>> players) {

    }

    @Override
    public void updateState(String state) {

    }

    @Override
    public StringProperty getObservableResultColor() {
        return null;
    }

    @Override
    public StringProperty getObservableResultText() {
        return null;
    }

    @Override
    public IntegerProperty getObserverableCountDownCounter() {
        return null;
    }

    @Override
    public MapProperty<String, Coordinate> getObservableKeyMappings() {
        return null;
    }

    @Override
    public MapProperty<Color, List<Coordinate>> getObservablePlayers() {
        return null;
    }

    @Override
    public StringProperty getObservableState() {
        return null;
    }
}
