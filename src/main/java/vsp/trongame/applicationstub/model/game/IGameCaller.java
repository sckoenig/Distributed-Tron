package vsp.trongame.applicationstub.model.game;

import vsp.trongame.app.model.ITronModel;
import vsp.trongame.app.model.datatypes.Steer;
import vsp.trongame.app.model.game.IGame;
import vsp.trongame.app.model.gamemanagement.IGameManager;
import vsp.trongame.middleware.IRemoteInvocation;

import java.util.List;
import java.util.concurrent.ExecutorService;

public class IGameCaller implements IGame {

    IRemoteInvocation middleware;

    @Override
    public void initialize(ExecutorService executorService, int waitingTimer, int endingTimer, int rows, int columns, int speed) {

    }

    @Override
    public void prepare(int playerCount) {

    }

    @Override
    public void register(IGameManager gameManager, ITronModel.IUpdateListener listener, int listenerId, int managedPlayerCount) {

    }

    @Override
    public void handleSteers(List<Steer> steers, int tickCount) {

    }
}
