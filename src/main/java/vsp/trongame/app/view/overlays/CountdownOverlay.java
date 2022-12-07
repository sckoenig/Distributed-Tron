package vsp.trongame.app.view.overlays;

import javafx.collections.MapChangeListener;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import vsp.trongame.app.model.ITronModel;

/**
 * Overlay, that shows a countdown and key controls.
 */
public class CountdownOverlay implements Overlay {

    @FXML
    public Label counterLabel;

    @FXML
    public Canvas canvas;

    @Override
    public void init(ITronModel.IObservableTronModel observableModel) {
        counterLabel.setVisible(false);
        observableModel.getObserverableCountDownCounter().addListener((observableValue, s, t1) -> {
            counterLabel.setText(String.valueOf(t1));
            counterLabel.setVisible(true);
            if (t1.equals(0)) reset();
        });
        observableModel.getObservableKeyMappings().addListener((MapChangeListener<String, Color>) change -> paintOnCanvas());
    }

    private void paintOnCanvas(){
        //TODO
    }

    private void reset(){
        this.canvas.getGraphicsContext2D().clearRect(0,0, canvas.getWidth(), canvas.getHeight());
        counterLabel.setVisible(false);
    }

}
