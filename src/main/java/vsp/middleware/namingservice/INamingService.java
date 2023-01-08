package vsp.middleware.namingservice;

/**
 * Enables Registration und Lookup for Services.
 */
public interface INamingService {

    /**
     * Performs a lookup for the given parameters.
     * @param remoteID the service provider's remoteId
     * @param serviceId the service's id
     * @return the service provider's address
     */
    String lookupService(String remoteID, int serviceId);

    /**
     * Registers a service so it can be found.
     * @param remoteID the service provider's remoteId
     * @param serviceID the service's id
     * @param address the service provider's address
     */
    void registerService(String remoteID, int serviceID, String address);

    /**
     * Unregisters a service provider's services so they can no longer be found.
     * @param remoteID the service provider's remoteId
     */
    void unregisterService(String remoteID);

}
