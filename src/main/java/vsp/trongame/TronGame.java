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
import vsp.trongame.applicationstub.model.game.IGameCallee;
import vsp.trongame.applicationstub.model.gamemanagement.IGameManagerCallee;
import vsp.trongame.applicationstub.view.IModelUpdateListenerCallee;
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

    private static final int MODEL_THREAD_SIZE = 3;
    private GameModus gameModus;

    private ExecutorService modelExecutor;

    @Override
    public void start(Stage stage) throws IOException {

        /* BOOT APP */

        Configuration config = new Configuration();
        gameModus = GameModus.valueOf(config.getAttribut("gameMode"));
        boolean singleView = gameModus == GameModus.LOCAL;

        modelExecutor = Executors.newFixedThreadPool(MODEL_THREAD_SIZE);

        /* local components */
        IViewWrapper tronView = IViewWrapperFactory.getViewWrapper(GameModus.LOCAL);
        ITronController tronController = ITronControllerFactory.getTronController(GameModus.LOCAL);
        ITronModel tronModel = ITronModelFactory.getTronModel(GameModus.LOCAL);

        /* middleware & stubs if not LOCAL */
        if (gameModus != GameModus.LOCAL){
            boolean asNameServerHost = Boolean.parseBoolean(config.getAttribut(Configuration.NAME_SERVER_HOST));
            String nameServerAddress = config.getAttribut(Configuration.NAME_SERVER);
            Middleware.getInstance().start(nameServerAddress, asNameServerHost);

            //create stub
            new IGameCallee(config, modelExecutor);
            new IModelUpdateListenerCallee(tronView.getListener());
            new IGameManagerCallee((IGameManager) tronModel);
        }

        /* assemble */
        tronController.initialize(tronModel);
        tronModel.initialize(config, gameModus, singleView, modelExecutor);
        tronView.buildView(tronModel, tronController, Integer.parseInt(config.getAttribut(Configuration.HEIGHT)),
                Integer.parseInt(config.getAttribut(Configuration.WIDTH)),
                Integer.parseInt(config.getAttribut(Configuration.DEFAULT_PLAYER_NUMBER)), STATE_VIEW_MAPPING);

        /* open stage */
        stage.setTitle("LightCycles");
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
        if (gameModus != GameModus.LOCAL) Middleware.getInstance().stop(); //shutdown any middleware threads
    }

}