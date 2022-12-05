package vsp.trongame.app.view.overlays;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import vsp.trongame.app.controller.ITronController;

/**
 * Overlay, that represents a menu.
 */
public class MenuOverlay  {
    public static final int MAX_PLAYER = 6;
    public static final int MIN_PLAYER = 2;

    private ITronController controller;

    @FXML
    public Button startGameButton;
    @FXML
    public Label playerCountLabel;
    @FXML
    public Button decreasePlayerCountButton;
    @FXML
    public Button increasePlayerCountButton;

    public void handleButtonPress() {
        this.controller.startGame(Integer.parseInt(playerCountLabel.getText()));
    }

    public void handleDecrease(){
        int value = Integer.parseInt(playerCountLabel.getText());
        if (value > MIN_PLAYER) playerCountLabel.setText(String.valueOf(value-1));
    }
    public void handleIncrease(){
        int value = Integer.parseInt(playerCountLabel.getText());
        if (value < MAX_PLAYER) playerCountLabel.setText(String.valueOf(value+1));
    }

    public void setController(ITronController controller){
        this.controller = controller;
    }

    public void setDefaultPlayerCount(int playerCount){
        this.playerCountLabel.setText(String.valueOf(playerCount));
    }
}
