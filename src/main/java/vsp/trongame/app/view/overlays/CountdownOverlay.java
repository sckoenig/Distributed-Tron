package vsp.trongame.app.view.overlays;

import javafx.beans.property.IntegerProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;

/**
 * Is shown when the game is in the state countdown.
 */
public class CountdownOverlay extends Overlay{
    public Label counterLabel;
    //public Canvas canvas;

    @Override
    public void init() {
        //this.getController().getModel().getCounterObservable().addListener((observableValue, s, t1) -> counterLabel.setText(String.valueOf(t1)));
        //this.getController().getModel().getPlayerObservable().addListener((observableValue, s, t1) -> paintOnCanvas());
    }

    private void paintOnCanvas(){

    }
}
