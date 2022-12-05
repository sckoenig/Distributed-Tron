package vsp.trongame.app.model;

import edu.cads.bai5.vsp.tron.view.Coordinate;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableMap;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import vsp.trongame.app.model.gamemanagement.Config;

import java.util.Set;

/**
 * Represents the Model to the outside of the Model component.
 */
public interface ITronModel {

    /**
     * Initializes the Model and informs if there is one or several views.
     * @param singleView true if one view, false for several views.
     */
    void init(boolean singleView, Config config);

    /**
     * Handles a steer event by accepting a KeyCode and processing it.
     * @param key keyCode of the key that initiated the steer event.
     */
    void handleSteerEvent(KeyCode key);

    /**
     * Starts a game in the Model.
     * @param playerNumber number of Players for the game.
     */
    void initializeGame(int playerNumber);

    /**
     * Returns the Model as an Observable Model with ObservableValues.
     * @return observable Model
     */
    IObservableTronModel getObservableModel();

    /**
     * Represents the Model with ObservableValues.
     */
    interface IObservableTronModel {

        /**
         * Returns the Model's ResultColor as an ObservableValue
         * @return game result color
         */
        StringProperty getObservableResultColor();

        /**
         * Returns the Model's ResultText as an ObservableValue
         * @return game result text
         */
        StringProperty getObservableResultText();

        /**
         * Returns the Model's Countdown Counter as an ObservableValue
         * @return game countdown counter
         */
        IntegerProperty getObserverableCountDownCounter();

        /**
         * Returns the Model's key mappings as an ObservableValue
         * @return map of key mappings
         */
        ObservableMap<String, Color> getObservableKeyMappings();

        /**
         * Returns the Model's Players as an ObservableValue
         * @return game players
         */
        ObservableMap<Color, Set<Coordinate>> getObservablePlayers();

        /**
         * Returns the Model's current State as an ObservableValue
         * @return model state
         */
        StringProperty getObservableState();

    }

}
