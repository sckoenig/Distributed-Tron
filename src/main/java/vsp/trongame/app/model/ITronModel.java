package vsp.trongame.app.model;

import edu.cads.bai5.vsp.tron.view.Coordinate;
import javafx.scene.input.KeyCode;
import vsp.trongame.app.model.datatypes.GameModus;
import vsp.trongame.app.model.gamemanagement.Config;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * Represents the Model to the outside of the Model component.
 */
public interface ITronModel {


    /**
     * Handles a steer event by accepting a KeyCode and processing it.
     *
     * @param id the initiator's registration id
     * @param key keyCode of the key that initiated the steer event.
     */
    void handleSteerEvent(int id, KeyCode key);

    /**
     * Initializes the Model with necessary dependencies.
     */
    void initialize(GameModus modus, ExecutorService executor, boolean singleView, Config config);

    /**
     * Registers the initiator at the game.
     *
     * @param id the initiator's registration id
     * @param playerNumber number of Players for the game.
     */
    void playGame(int id, int playerNumber);

    void registerListener(IUpdateListener listener);

    interface IUpdateListener {


        void updateOnRegistration(int id);

        void updateOnKeyMappings(Map<String, String> mappings);

        void updateOnArena(int rows, int columns);

        void updateOnState(String state);

        void updateOnGameStart();

        void updateOnGameResult(String color, String result);

        void updateOnCountDown(int value);

        void updateOnField(Map<String, List<Coordinate>> field);
        void updateOnCrash(String crashedColor, String newColor);

    }



}
