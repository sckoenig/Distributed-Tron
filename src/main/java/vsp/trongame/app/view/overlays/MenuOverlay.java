package vsp.trongame.app.view.overlays;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import vsp.trongame.app.controller.ITronController;

/**
 * Overlay, that represents a menu.
 */
public class MenuOverlay  {

    public static final String IDENTIFIER = "MENU";
    public static final int MAX_PLAYER = 6;
    public static final int MIN_PLAYER = 2;

    private ITronController controller;
    private int registrationId;

    @FXML
    public Button startGameButton;
    @FXML
    public Label playerCountLabel;
    @FXML
    public Button decreasePlayerCountButton;
    @FXML
    public Button increasePlayerCountButton;

    public void handleButtonPress() {
        this.controller.playGame(registrationId, Integer.parseInt(playerCountLabel.getText()));
    }

    public void handleDecrease(){
        int value = Integer.parseInt(playerCountLabel.getText());
        if (value > MIN_PLAYER) playerCountLabel.setText(String.valueOf(value-1));
    }
    public void handleIncrease(){
        int value = Integer.parseInt(playerCountLabel.getText());
        if (value < MAX_PLAYER) playerCountLabel.setText(String.valueOf(value+1));
    }

    public void initialize(ITronController controller, int defaultPlayerCount){
        this.controller = controller;
        this.playerCountLabel.setText(String.valueOf(defaultPlayerCount));
    }

    public void setId(int id){
        this.registrationId = id;
    }

}
