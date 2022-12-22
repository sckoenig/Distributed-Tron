package vsp.trongame.middleware;

public interface IRemoteObject {

    /**
     * The Method...
     * @param serviceID
     * @param parameters
     */
    void call(int serviceID, int... parameters );
}
