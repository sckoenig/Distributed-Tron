package vsp.trongame.middleware;

import vsp.trongame.middleware.IRegister;
import vsp.trongame.middleware.IRemoteInvocation;
import vsp.trongame.middleware.IRemoteObject;

public class Middleware implements IRegister, IRemoteInvocation {
    @Override
    public void registerRemoteObject(int serviceID, IRemoteObject remoteObject) {

    }

    @Override
    public void invoke(String remoteID, int serviceID, InvocationType type, int... parameters) {

    }
}
