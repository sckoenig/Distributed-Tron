package vsp.trongame.applicationstub.model.rest.registration;

import vsp.trongame.application.model.IUpdateListener;
import vsp.trongame.application.model.gamemanagement.IGameManager;

/**
 * Represents a registration per rpc within a supernode.
 * @param gameManager the registration's game manager
 * @param updateListener the registration's listener
 * @param listenerId the listener's registration id
 * @param playerCount the playercount
 */
public record RPCRegistration(IGameManager gameManager, IUpdateListener updateListener, int listenerId, int playerCount) {
}
