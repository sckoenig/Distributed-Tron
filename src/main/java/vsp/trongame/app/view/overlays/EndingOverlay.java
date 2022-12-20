package vsp.trongame.app.view.overlays;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

/**
 * Overlay, that shows a game's result.
 */
public class EndingOverlay {

    public static final String IDENTIFIER = "ENDING";
    @FXML
    public Label gameResultLabel;
    @FXML
    public Circle winningColorCircle;

    public void setResult(String resultText, String resultColor){
       winningColorCircle.setFill(Paint.valueOf(resultColor));
       gameResultLabel.setText(resultText);
    }
}
