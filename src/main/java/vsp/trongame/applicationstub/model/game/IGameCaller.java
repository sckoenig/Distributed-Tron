package vsp.trongame.applicationstub.model.game;

import vsp.trongame.app.model.ITronModel;
import vsp.trongame.app.model.datatypes.Steer;
import vsp.trongame.app.model.game.IGame;
import vsp.trongame.app.model.gamemanagement.IGameManager;
import vsp.trongame.applicationstub.ICaller;
import vsp.trongame.applicationstub.Service;
import vsp.trongame.middleware.IRemoteInvocation;

import java.util.List;
import java.util.concurrent.ExecutorService;

public class IGameCaller implements IGame, ICaller {


    private String remoteId;
    private IRemoteInvocation middleware;

    @Override
    public void initialize(ExecutorService executorService, int waitingTimer, int endingTimer, int rows, int columns, int speed) {
        //not needed
    }

    @Override
    public void prepare(int playerCount) {
        middleware.invoke("", Service.PREPARE.ordinal(), IRemoteInvocation.InvocationType.RELIABLE, playerCount);
    }

    @Override
    public void register(IGameManager gameManager, ITronModel.IUpdateListener listener, int listenerId, int managedPlayerCount) {


        //middleware.invoke("", Service.REGISTER.ordinal(), IRemoteInvocation.InvocationType.RELIABLE, );
    }

    @Override
    public void handleSteers(List<Steer> steers, int tickCount) {
        int[] steerArray = new int[steers.size()+1];
        steerArray[0] = tickCount;
        for (int i = 1; i < steers.size(); i++) {
            steerArray[i] = steers.get(i).playerId();
            steerArray[++i] = steers.get(i).directionChange().ordinal();
        }
        middleware.invoke("", Service.HANDLE_STEERS.ordinal(), IRemoteInvocation.InvocationType.UNRELIABLE,
                steerArray);
    }

    @Override
    public void setRemoteId(String remoteId) {
     this.remoteId = remoteId;
    }
}
