package vsp.trongame.applicationstub.model.game;

import vsp.trongame.application.model.IUpdateListener;
import vsp.trongame.application.model.game.IGameFactory;
import vsp.trongame.application.model.datatypes.DirectionChange;
import vsp.trongame.Modus;
import vsp.trongame.application.model.datatypes.Steer;
import vsp.trongame.application.model.game.IGame;
import vsp.trongame.application.model.gamemanagement.Configuration;
import vsp.trongame.application.model.gamemanagement.IGameManager;
import vsp.trongame.application.model.gamemanagement.IGameManagerFactory;
import vsp.trongame.application.view.listener.IUpdateListenerFactory;
import vsp.trongame.applicationstub.util.ICaller;
import vsp.trongame.applicationstub.util.RemoteId;
import vsp.trongame.applicationstub.util.Service;
import vsp.middleware.IRegister;
import vsp.middleware.IRemoteObject;
import vsp.middleware.Middleware;

import java.util.concurrent.ExecutorService;

import static vsp.trongame.Modus.LOCAL;
import static vsp.trongame.Modus.REST;
import static vsp.trongame.applicationstub.util.Service.*;

/**
 * Callee Remote Object, that knows the local Instance of {@link IGame} and can call its methods.
 */
public class GameCallee implements IRemoteObject {

    private final IGame localGame;

    public GameCallee(Modus modus, Configuration config, ExecutorService executorService) {
        this.localGame = IGameFactory.getGame(modus);
        Modus gameModus = modus == REST? REST : LOCAL;
        this.localGame.initialize(gameModus, Integer.parseInt(config.getAttribut(Configuration.SPEED)),
                Integer.parseInt(config.getAttribut(Configuration.ROWS)),
                Integer.parseInt(config.getAttribut(Configuration.COLUMNS)),
                Integer.parseInt(config.getAttribut(Configuration.WAITING_TIMER)),
                Integer.parseInt(config.getAttribut(Configuration.ENDING_TIMER)), executorService);

        // can be called from remote
        IRegister middleware = Middleware.getInstance();
        middleware.registerRemoteObject(PREPARE.ordinal(),RemoteId.STRING_ID, this);
        middleware.registerRemoteObject(REGISTER.ordinal(), RemoteId.STRING_ID, this);
        middleware.registerRemoteObject(HANDLE_STEER.ordinal(), RemoteId.STRING_ID, this);
    }

    @Override
    public void call(int serviceID, int[] intParameters, String... stringParameters) {
        Service service = Service.getByOrdinal(serviceID);
        switch (service){
            case PREPARE -> {
                if (intParameters.length == 1){
                    int playerCount = intParameters[0];
                    localGame.prepareForRegistration(playerCount);
                }
            }
            case REGISTER -> {
                if (intParameters.length == 2 && stringParameters.length == 2){

                    // Caller Objects that represent the remote Objects that want to register at the local game
                    IGameManager managerCaller = IGameManagerFactory.getGameManager(Modus.NETWORK);
                    IUpdateListener listenerCaller = IUpdateListenerFactory.getUpdateListener(Modus.NETWORK);
                    ((ICaller) managerCaller).setRemoteId(stringParameters[0]);
                    ((ICaller) listenerCaller).setRemoteId(stringParameters[1]);

                    localGame.register(managerCaller, listenerCaller, intParameters[0], intParameters[1]);
                }
            }
            case HANDLE_STEER -> {
                if (intParameters.length == 2){
                    Steer steer = new Steer(intParameters[0], DirectionChange.getByOrdinal(intParameters[1]));
                    localGame.handleSteer(steer);
                }
            }
            default -> {}
        }
    }
}
