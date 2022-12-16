package vsp.trongame.app.view.overlays;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;

import java.util.Map;

/**
 * Overlay, that shows a countdown and key controls.
 */
public class CountdownOverlay {

    @FXML
    private Node root;

    @FXML
    public Label counterLabel;

    public void setKeyMappings(Map<String, String> mappings){

        //TODO

    }

    public void setCounterLabel(int value){
        counterLabel.setText(String.valueOf(value));
        counterLabel.setVisible(true);
        if (value == 0) reset();
    }

    private void reset(){
        counterLabel.setVisible(false);
    }

}
