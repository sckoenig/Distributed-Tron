package vsp.trongame.app.model.gamemanagement;

import edu.cads.bai5.vsp.tron.view.Coordinate;
import javafx.scene.paint.Color;
import vsp.trongame.app.model.datatypes.GameResult;
import vsp.trongame.app.model.datatypes.TronColor;

import java.util.List;
import java.util.Map;

public interface IGameData {

    void updateGameResult(TronColor winner, GameResult result);

    void updateKeyMappings(Map<String, Coordinate> mappings);

    void updateArenaSize(int rows, int columns);

    void updatePlayers(Map<Color, List<Coordinate>> players);

    void updateState(String state);

}
