package vsp.trongame.applicationstub.model.gamemanagement;

import vsp.trongame.application.model.datatypes.GameState;
import vsp.trongame.application.model.gamemanagement.IGameManager;
import vsp.trongame.applicationstub.util.ICaller;
import vsp.middleware.IRemoteInvocation;
import vsp.middleware.Middleware;

import java.util.List;

import static vsp.trongame.applicationstub.util.Service.*;

public class GameManagerCaller implements IGameManager, ICaller {

    private final IRemoteInvocation middleware;
    private String remoteId;

    public GameManagerCaller() {
        this.middleware = Middleware.getInstance();
    }

    @Override
    public void handleGameState(GameState gameState) {
        middleware.invoke(remoteId, HANDLE_GAME_STATE.ordinal(), IRemoteInvocation.InvocationType.RELIABLE,
                new int[]{gameState.ordinal()});
    }

    @Override
    public void handleManagedPlayers(int id, List<Integer> managedPlayers) {
        int[] parameters = new int[managedPlayers.size()+1];
        parameters[0] = id;

        int index = 1;
        for (Integer playerId: managedPlayers) {
            parameters[index++] = playerId;
        }
        middleware.invoke(remoteId, HANDLE_MANAGED_PLAYERS.ordinal(), IRemoteInvocation.InvocationType.RELIABLE, parameters);
    }

    @Override
    public void setRemoteId(String remoteId) {
        this.remoteId = remoteId;
    }
}
