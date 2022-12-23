package vsp.trongame.applicationstub.model.game;

import vsp.trongame.app.model.ITronModel;
import vsp.trongame.app.model.util.datatypes.Steer;
import vsp.trongame.app.model.game.IGame;
import vsp.trongame.app.model.gamemanagement.IGameManager;
import vsp.trongame.applicationstub.util.ICaller;
import vsp.trongame.applicationstub.util.RemoteId;
import vsp.trongame.applicationstub.util.Service;
import vsp.trongame.middleware.IRemoteInvocation;
import vsp.trongame.middleware.Middleware;

import static vsp.trongame.middleware.IRemoteInvocation.*;


import java.util.List;

public class IGameCaller implements IGame, ICaller {


    private String remoteId; //id of the remote Object I want to call
    private final IRemoteInvocation middleware;

    public IGameCaller() {
        this.remoteId = RemoteId.DEFAULT_ID; //id of the remote Object Game is not known and not relevant, we want any game that is callable
        this.middleware = Middleware.getInstance();
    }

    @Override
    public void prepare(int playerCount) {
        middleware.invoke(remoteId, Service.PREPARE.ordinal(), InvocationType.RELIABLE, new int[]{playerCount});
    }

    @Override
    public void register(IGameManager gameManager, ITronModel.IUpdateListener listener, int listenerId, int managedPlayerCount) {
        middleware.invoke(remoteId, Service.REGISTER.ordinal(), InvocationType.RELIABLE, new int[]{listenerId, managedPlayerCount},
                RemoteId.getRemoteId().getIdString(), RemoteId.getRemoteId().getIdString());
    }

    @Override
    public void handleSteers(List<Steer> steers, int tickCount) {
        int[] steerArray = new int[steers.size()*2+1];
        steerArray[0] = tickCount;
        for (int i = 1; i < steerArray.length; i+=2) {
            steerArray[i] = steers.get(i).playerId();
            steerArray[i+1] = steers.get(i).directionChange().ordinal();
        }
        middleware.invoke(remoteId, Service.HANDLE_STEERS.ordinal(), IRemoteInvocation.InvocationType.UNRELIABLE,
                steerArray);
    }

    @Override
    public void setRemoteId(String remoteId) {
     this.remoteId = remoteId;
    }
}
