package vsp.trongame.applicationstub.model.game;

import vsp.trongame.app.model.IUpdateListener;
import vsp.trongame.app.model.datatypes.GameModus;
import vsp.trongame.app.model.datatypes.Steer;
import vsp.trongame.app.model.game.IGame;
import vsp.trongame.app.model.gamemanagement.IGameManager;
import vsp.trongame.applicationstub.util.ICaller;
import vsp.trongame.applicationstub.util.RemoteId;
import vsp.trongame.applicationstub.util.Service;
import vsp.trongame.middleware.IRemoteInvocation;
import vsp.trongame.middleware.Middleware;

import static vsp.trongame.middleware.IRemoteInvocation.*;

import java.util.concurrent.ExecutorService;

public class GameCaller implements IGame, ICaller {

    private String remoteId; //id of the remote Object I want to call
    private final IRemoteInvocation middleware;

    public GameCaller() {
        this.remoteId = RemoteId.DEFAULT_ID; //id of the remote Object Game is not known and not relevant, we want any game that is callable
        this.middleware = Middleware.getInstance();
    }

    @Override
    public void initialize(GameModus modus, int speed, int rows, int columns, int waitingTimer, int endingTimer, ExecutorService executorService) {
        //not needed
    }

    @Override
    public void prepareForRegistration(int playerCount) {
        middleware.invoke(remoteId, Service.PREPARE.ordinal(), InvocationType.RELIABLE, new int[]{playerCount});
    }

    @Override
    public void register(IGameManager gameManager, IUpdateListener listener, int listenerId, int managedPlayerCount) {
        middleware.invoke(remoteId, Service.REGISTER.ordinal(), InvocationType.RELIABLE, new int[]{listenerId, managedPlayerCount},
                RemoteId.STRING_ID, RemoteId.STRING_ID);
    }

    @Override
    public void handleSteer(Steer steer) {
        middleware.invoke(remoteId, Service.HANDLE_STEERS.ordinal(), IRemoteInvocation.InvocationType.UNRELIABLE,
                new int[]{steer.playerId(), steer.directionChange().ordinal()});
    }

    @Override
    public void setRemoteId(String remoteId) {
     this.remoteId = remoteId;
    }
}
