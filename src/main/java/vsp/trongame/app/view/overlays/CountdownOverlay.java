package vsp.trongame.app.view.overlays;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.util.Map;

/**
 * Overlay, that shows a countdown and key controls.
 */
public class CountdownOverlay {

    public static final String IDENTIFIER = "COUNTDOWN";
    @FXML
    public AnchorPane pane;
    @FXML
    public Label counterLabel;
    @FXML
    public HBox mainHBox;

    /**
     * Happens once a game. Displays Key Mappings and Colors.
     * @param mappings mappings in the form of keys and color hexcode.
     */
    public void setKeyMappings(Map<String, String> mappings) {
        Platform.runLater(() -> {

            for (Map.Entry<String, String> entry : mappings.entrySet()) {
                HBox hBox = new HBox();
                hBox.setSpacing(10);
                hBox.setAlignment(Pos.CENTER);
                hBox.setPadding(new Insets(0, 15, 0, 0));

                Label label = new Label();
                label.setText(entry.getKey());
                label.setTextFill(Paint.valueOf(entry.getValue()));

                Circle circle = new Circle();
                circle.setFill(Paint.valueOf(entry.getValue()));
                circle.setRadius(8);

                hBox.getChildren().add(circle);
                hBox.getChildren().add(label);

                mainHBox.getChildren().add(hBox);
            }
        });
    }

    public void setCounterLabel(int value) {
        counterLabel.setVisible(true);
        counterLabel.setText(String.valueOf(value));
    }

    public void reset(){
        mainHBox.getChildren().clear(); //happens once a game
        counterLabel.setVisible(false);
    }

}
