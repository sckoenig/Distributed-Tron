package vsp.trongame.app.view.overlays;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import vsp.trongame.app.model.ITronModel;

/**
 * Overlay, that shows a game's result.
 */
public class EndingOverlay implements Overlay {

    @FXML
    public Label gameResultLabel;
    @FXML
    public Circle winningColorCircle;

    public void init(ITronModel.IObservableTronModel observableModel) {
        observableModel.getObservableResultText().addListener(((observableValue, s, t1) -> gameResultLabel.setText(t1)));
        observableModel.getObservableResultColor().addListener(((observableValue, s, t1) -> winningColorCircle.setFill(Paint.valueOf(t1))));
    }
}
