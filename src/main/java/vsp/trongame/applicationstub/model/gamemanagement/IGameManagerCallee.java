package vsp.trongame.applicationstub.model.gamemanagement;

import vsp.trongame.app.model.gamemanagement.IGameManager;
import vsp.trongame.middleware.IRegister;
import vsp.trongame.middleware.IRemoteObject;

public class IGameManagerCallee implements IRemoteObject {

    private IGameManager gameManager;
    private IRegister middleware;

    @Override
    public void call(int serviceID, int... parameters) {

    }
}
