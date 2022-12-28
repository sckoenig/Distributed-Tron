package vsp.trongame.applicationstub.model.gamemanagement;

import vsp.trongame.app.model.datatypes.GameState;
import vsp.trongame.app.model.datatypes.TronColor;
import vsp.trongame.app.model.gamemanagement.IGameManager;
import vsp.trongame.applicationstub.util.ICaller;
import vsp.trongame.middleware.IRemoteInvocation;
import vsp.trongame.middleware.Middleware;

import java.util.Map;
import java.util.Set;

import static vsp.trongame.applicationstub.util.Service.*;

public class IGameManagerCaller implements IGameManager, ICaller {

    private final IRemoteInvocation middleware;
    private String remoteId;

    public IGameManagerCaller() {
        this.middleware = Middleware.getInstance();
    }

    @Override
    public void handleGameState(GameState gameState) {
        middleware.invoke(remoteId, HANDLE_GAME_STATE.ordinal(), IRemoteInvocation.InvocationType.RELIABLE,
                new int[]{gameState.ordinal()});
    }

    @Override
    public void handleGameTick(int tickCount) {
        middleware.invoke(remoteId, HANDLE_GAME_TICK.ordinal(), IRemoteInvocation.InvocationType.RELIABLE, new int[] {tickCount});
    }

    @Override
    public void handleManagedPlayers(int id, Map<Integer, TronColor> managedPlayers) {
        //TODO
        int[] parameters = new int[managedPlayers.size()*2];
        parameters[0] = id;
        int index = 1;
        Set<Map.Entry<Integer, TronColor>> entries =  managedPlayers.entrySet();
        for (Map.Entry<Integer, TronColor> entry: entries) {
            parameters[index] = Integer.parseInt(entry.getKey().toString());
            index++;
            parameters[index] = Integer.parseInt(entry.getValue().toString());
            index++;
        }
        middleware.invoke(remoteId, HANDLE_MANAGED_PLAYERS.ordinal(), IRemoteInvocation.InvocationType.RELIABLE, parameters);
    }

    @Override
    public void setRemoteId(String remoteId) {
        this.remoteId = remoteId;
    }
}
