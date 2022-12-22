package vsp.trongame.middleware;

public interface IRemoteInvocation {

    enum InvocationType{
        RELIABLE,
        UNRELIABLE
    }

    void invoke(String remoteID, int serviceID, InvocationType type, int... parameters);

}
