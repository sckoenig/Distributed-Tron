package vsp.trongame.app.model;

import edu.cads.bai5.vsp.tron.view.Coordinate;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.MapProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import vsp.trongame.app.model.gamemanagement.Config;

import java.util.List;

/**
 * The model which communicates with the view and the controller.
 */
public interface ITronModel {

    void setSingleView(boolean singleView);

    /**
     * @param key
     */
    void handleSteerEvent(KeyCode key);

    /**
     * @param playerNumber
     */
    void initializeGame(int playerNumber);

    void setConfig(Config config);

    IObservableTronModel getObservableModel();

    interface IObservableTronModel {

        StringProperty getObservableResultColor();

        StringProperty getObservableResultText();

        IntegerProperty getObserverableCountDownCounter();

        MapProperty<String, Coordinate> getObservableKeyMappings();

        MapProperty<Color, List<Coordinate>> getObservablePlayers();

        StringProperty getObservableState();


    }

}
