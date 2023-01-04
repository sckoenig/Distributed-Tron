package vsp.trongame.app.view;

import javafx.scene.Scene;
import vsp.trongame.app.controller.ITronController;
import vsp.trongame.app.model.ITronModel;
import vsp.trongame.app.model.IUpdateListener;

import java.io.IOException;
import java.util.Map;

/**
 * A Wrapper Interface for the view and overlays.
 */
public interface ITronViewWrapper {

    /**
     * Initializes the wrapper with its dependencies.
     * @param model the model
     * @param controller the controller
     * @param height window height
     * @param width window width
     * @param defaultPlayerCount the default player count to display
     * @param idOverlayMapping the overlays to load
     * @throws IOException on I/O Error
     */
    void buildView(ITronModel model, ITronController controller, int height, int width,
                   int defaultPlayerCount, Map<String, String> idOverlayMapping) throws IOException;

    /**
     * Gets the main Scene.
     * @return the main scene
     */
    Scene getScene();

    /**
     * Gets the UpdateListener.
     * @return the UpdateListener
     */
    IUpdateListener getListener();

}
