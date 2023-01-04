package vsp.trongame.application.model;

import edu.cads.bai5.vsp.tron.view.Coordinate;

import java.util.List;
import java.util.Map;

/**
 * Represents a listener the model can inform about its state.
 */
public interface IUpdateListener {

    /**
     * After Registration, the listener is informed about its id.
     *
     * @param id the id under which the listener is known by the model
     */
    void updateOnRegistration(int id);


    /**
     * Informs the Listener about Key Mappings.
     *
     * @param mappings mappings in the form of keys, id
     */
    void updateOnKeyMappings(Map<String, Integer> mappings);

    /**
     * Informs the Listener about the arena's size.
     *
     * @param rows    number of rows
     * @param columns number of columns
     */
    void updateOnArena(int rows, int columns);

    /**
     * Informs the Listener about the model's state.
     *
     * @param state model's current state
     */
    void updateOnState(String state);

    /**
     * Informs the Listener about the game's start.
     */
    void updateOnGameStart();

    /**
     * Informs the Listener about the result of a finished game.
     *
     * @param id  the winner's color or default color on draw
     * @param result the result text
     */
    void updateOnGameResult(Integer id, String result);

    /**
     * Informs the Listener about the countdown's value.
     *
     * @param value countdown value
     */
    void updateOnCountDown(int value);

    /**
     * Informs the Listener about the field's status.
     *
     * @param field the field in the form of id and list of coordinates
     */
    void updateOnField(Map<Integer, List<Coordinate>> field);

}
