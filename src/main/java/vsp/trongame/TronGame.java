package vsp.trongame;

import javafx.application.Application;
import javafx.stage.Stage;
import vsp.trongame.app.controller.ITronController;
import vsp.trongame.app.controller.ITronControllerFactory;
import vsp.trongame.app.model.util.Configuration;
import vsp.trongame.app.model.ITronModel;
import vsp.trongame.app.model.ITronModelFactory;
import vsp.trongame.app.model.gamemanagement.*;
import vsp.trongame.app.model.util.datatypes.GameModus;
import vsp.trongame.app.view.IViewWrapper;
import vsp.trongame.app.view.IViewWrapperFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TronGame extends Application {

    private static final Map<String, String> STATE_VIEW_MAPPING = new HashMap<>(Map.of(
            ModelState.MENU.toString(), "menuOverlay.fxml",
            ModelState.WAITING.toString(), "waitingOverlay.fxml",
            ModelState.COUNTDOWN.toString(), "countdownOverlay.fxml",
            ModelState.ENDING.toString(), "endingOverlay.fxml"));

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {

        /* BOOT APP */

        Configuration config = Configuration.getConfig();
        GameModus modus = GameModus.valueOf(config.getAttribut("gameMode"));
        boolean singleView = modus == GameModus.LOCAL;

        /* components */
        IViewWrapper tronView = IViewWrapperFactory.getViewWrapper(modus);
        ITronController tronController = ITronControllerFactory.getTronController(modus);
        ITronModel tronModel = ITronModelFactory.getTronModel(modus);

        /* assemble */
        tronController.initialize(tronModel);
        tronModel.initialize(modus, singleView);
        tronView.initialize(tronModel, tronController, Integer.parseInt(config.getAttribut(Configuration.HEIGHT)),
                Integer.parseInt(config.getAttribut(Configuration.WIDTH)),
                Integer.parseInt(config.getAttribut(Configuration.DEFAULT_PLAYER_NUMBER)), STATE_VIEW_MAPPING);

        /* open stage */
        stage.setTitle("Tron");
        stage.setScene(tronView.getScene());
        stage.show();

    }

    /**
     * Executed on pressing close-Button.
     */
    @Override
    public void stop() throws Exception {
        super.stop();
        Configuration.getConfig().getExecutorService().shutdownNow(); //stop threads
    }

}