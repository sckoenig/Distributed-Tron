package vsp.trongame;

import javafx.application.Application;
import javafx.stage.Stage;
import vsp.trongame.application.controller.ITronController;
import vsp.trongame.application.controller.ITronControllerFactory;
import vsp.trongame.application.model.gamemanagement.Configuration;
import vsp.trongame.application.model.ITronModel;
import vsp.trongame.application.model.ITronModelFactory;
import vsp.trongame.application.model.gamemanagement.*;
import vsp.trongame.application.view.ITronViewWrapper;
import vsp.trongame.application.view.IViewWrapperFactory;
import vsp.trongame.applicationstub.model.game.GameCallee;
import vsp.trongame.applicationstub.model.gamemanagement.GameManagerCallee;
import vsp.trongame.applicationstub.view.UpdateListenerCallee;
import vsp.middleware.Middleware;

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
    private Modus gameModus;

    private ExecutorService modelExecutor;

    @Override
    public void start(Stage stage) throws IOException {

        /* BOOT APP */

        Configuration config = new Configuration();
        gameModus = Modus.valueOf(config.getAttribut("gameMode"));
        boolean singleView = gameModus == Modus.LOCAL;

        modelExecutor = Executors.newFixedThreadPool(MODEL_THREAD_SIZE);

        /* local components */
        ITronViewWrapper tronView = IViewWrapperFactory.getViewWrapper(Modus.LOCAL);
        ITronController tronController = ITronControllerFactory.getTronController(Modus.LOCAL);
        ITronModel tronModel = ITronModelFactory.getTronModel(Modus.LOCAL);

        /* middleware & stubs if not LOCAL */
        if (gameModus != Modus.LOCAL){
            boolean asNameServerHost = Boolean.parseBoolean(config.getAttribut(Configuration.NAME_SERVER_HOST));
            String nameServerAddress = config.getAttribut(Configuration.NAME_SERVER);
            Middleware.getInstance().start(nameServerAddress, asNameServerHost);

            //create stub
            new GameCallee(config, modelExecutor);
            new UpdateListenerCallee(tronView.getListener());
            new GameManagerCallee((IGameManager) tronModel);
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
        if (gameModus != Modus.LOCAL) Middleware.getInstance().stop(); //shutdown any middleware threads
    }

}