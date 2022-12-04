package vsp.trongame.app.view.overlays;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * Is shown when the game is in the state Menu.
 */
public class MenuOverlay extends Overlay {
    public static final int MAX_PLAYER = 6;
    public static final int MIN_PLAYER = 2;
    public Button startGameButton;
    public Label playerCountLabel;
    public Button decreasePlayerCountButton;
    public Button increasePlayerCountButton;

    public void handleButtonPress(ActionEvent actionEvent) {
        getController().startGame(Integer.parseInt(playerCountLabel.getText()));
    }

    public void handleDecrease(ActionEvent actionEvent){
        int value = Integer.parseInt(playerCountLabel.getText());
        if (value > MIN_PLAYER) playerCountLabel.setText(String.valueOf(value-1));
    }
    public void handleIncrease(ActionEvent actionEvent){
        int value = Integer.parseInt(playerCountLabel.getText());
        if (value < MAX_PLAYER) playerCountLabel.setText(String.valueOf(value+1));
    }

    @Override
    public void init() {

    }
}
