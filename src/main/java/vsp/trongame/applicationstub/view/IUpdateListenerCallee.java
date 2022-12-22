package vsp.trongame.applicationstub.view;

import vsp.trongame.app.model.ITronModel;
import vsp.trongame.middleware.IRegister;
import vsp.trongame.middleware.IRemoteObject;

public class IUpdateListenerCallee implements IRemoteObject {

    private ITronModel.IUpdateListener updateListener;
    private IRegister middleware;

    @Override
    public void call(int serviceID, int... parameters) {

    }
}
