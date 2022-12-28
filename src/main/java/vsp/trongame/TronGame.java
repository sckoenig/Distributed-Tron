package vsp.trongame;

import javafx.application.Application;
import javafx.stage.Stage;
import vsp.trongame.app.controller.ITronController;
import vsp.trongame.app.controller.ITronControllerFactory;
import vsp.trongame.app.model.gamemanagement.Configuration;
import vsp.trongame.app.model.ITronModel;
import vsp.trongame.app.model.ITronModelFactory;
import vsp.trongame.app.model.gamemanagement.*;
import vsp.trongame.app.model.datatypes.GameModus;
import vsp.trongame.app.view.IViewWrapper;
import vsp.trongame.app.view.IViewWrapperFactory;
import vsp.trongame.middleware.Middleware;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TronGame extends Application {

    private static final Map<String, String> STATE_VIEW_MAPPING = new HashMap<>(Map.of(
            ModelState.MENU.toString(), "menuOverlay.fxml",
            ModelState.WAITING.toString(), "waitingOverlay.fxml",
            ModelState.COUNTDOWN.toString(), "countdownOverlay.fxml",
            ModelState.ENDING.toString(), "endingOverlay.fxml"));

    public static void main(String[] args) {
        launch();
    }

    private static final int MODEL_THREAD_SIZE = 4;

    private ExecutorService modelExecutor;

    @Override
    public void start(Stage stage) throws IOException {

        /* BOOT APP */

        Configuration config = new Configuration();
        GameModus modus = GameModus.valueOf(config.getAttribut("gameMode"));
        boolean singleView = modus == GameModus.LOCAL;

        modelExecutor = Executors.newFixedThreadPool(MODEL_THREAD_SIZE);

        /* middleware & stubs if not LOCAL */
        if (modus != GameModus.LOCAL){
            // ... create callees
        }

        /* local components */
        IViewWrapper tronView = IViewWrapperFactory.getViewWrapper(GameModus.LOCAL);
        ITronController tronController = ITronControllerFactory.getTronController(GameModus.LOCAL);
        ITronModel tronModel = ITronModelFactory.getTronModel(GameModus.LOCAL);

        /* assemble */
        tronController.initialize(tronModel);
        tronModel.initialize(config, modus, singleView, modelExecutor);
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
        modelExecutor.shutdownNow(); //shutdown any model threads
        Middleware.getInstance().stop(); //shutdown any middleware threads
    }

}