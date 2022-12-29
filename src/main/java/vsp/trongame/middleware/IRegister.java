package vsp.trongame.middleware;

/**
 * Represents a Register for instances of type {@link IRemoteObject}, so they can be called.
 */
public interface IRegister {

    /**
     * Registers a remote Object so it can be called.
     * @param serviceID the id of the service the remote object provides
     * @param remoteObject the remote object
     */
    void registerRemoteObject(int serviceID, String remoteId, IRemoteObject remoteObject);
}
