package vsp.trongame.applicationstub.model.gamemanagement;

import vsp.trongame.app.model.util.datatypes.GameState;
import vsp.trongame.app.model.util.datatypes.TronColor;
import vsp.trongame.app.model.gamemanagement.IGameManager;
import vsp.trongame.applicationstub.util.ICaller;
import vsp.trongame.middleware.IRemoteInvocation;

import java.util.Map;

public class IGameManagerCaller implements IGameManager, ICaller {

    private IRemoteInvocation middleware;
    private String remoteId;

    @Override
    public void handleGameState(GameState gameState) {

    }

    @Override
    public void handleGameTick(int tickCount) {

    }

    @Override
    public void handleManagedPlayers(int id, Map<Integer, TronColor> managedPlayers) {

    }

    @Override
    public void setRemoteId(String remoteI) {
        
    }
}
