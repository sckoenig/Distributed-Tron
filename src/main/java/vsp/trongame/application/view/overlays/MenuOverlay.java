package vsp.trongame.application.view.overlays;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import vsp.trongame.application.controller.ITronController;
import vsp.trongame.application.model.IUpdateListener;

/**
 * Overlay, that represents a menu.
 */
public class MenuOverlay  {

    public static final String IDENTIFIER = "MENU";
    public static final int MAX_PLAYER = 6;
    public static final int MIN_PLAYER = 2;
    @FXML
    public Button startGameButton;
    @FXML
    public Label playerCountLabel;
    @FXML
    public Button decreasePlayerCountButton;
    @FXML
    public Button increasePlayerCountButton;

    private IUpdateListener listener;

    private ITronController controller;

    public void handleButtonPress() {
        this.controller.playGame(listener, Integer.parseInt(playerCountLabel.getText()));
    }

    public void handleDecrease(){
        int value = Integer.parseInt(playerCountLabel.getText());
        if (value > MIN_PLAYER) playerCountLabel.setText(String.valueOf(value-1));
    }
    public void handleIncrease(){
        int value = Integer.parseInt(playerCountLabel.getText());
        if (value < MAX_PLAYER) playerCountLabel.setText(String.valueOf(value+1));
    }

    public void initialize(ITronController controller, int defaultPlayerCount, IUpdateListener listener){
        this.controller = controller;
        this.playerCountLabel.setText(String.valueOf(defaultPlayerCount));
        this.listener = listener;
    }

}
