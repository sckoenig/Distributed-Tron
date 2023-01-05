package vsp.trongame.applicationstub.model.rest;

import vsp.trongame.application.model.IUpdateListener;
import vsp.trongame.application.model.gamemanagement.IGameManager;

public record RPCRegistration(IGameManager gameManager, IUpdateListener updateListener, int listenerId, int playerCount) {
}
