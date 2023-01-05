package vsp.middleware.namingservice;

public interface INamingService {

    int NO_SERVICE = -1;
    String NO_REMOTE_ID = "";
    byte REGISTER = 1;
    byte UNREGISTER = 2;
    byte LOOKUP = 3;
    byte RESPONSE = 4;

    /**
     * Returns the address of a service.
     *
     * @param remoteID the id to the remote id with the service
     * @param serviceId the id of the service we are looking for
     * @return the address of the service
     */
    String lookupService(String remoteID, int serviceId);

    /**
     * A remote object is registered with a remote id and a service id, at the name server with the given address.
     *
     * @param remoteID the id to the remote object which we want to register at the name server
     * @param serviceID the id to the service which the remote objects offers
     * @param address the address of the name server
     */
    void registerService(String remoteID, int serviceID, String address);

    /**
     * The remote object with the given id will be deleted from the naming server.
     *
     * @param remoteID the id to the remote object which we want to unregister
     */
    void unregisterService(String remoteID);

}
