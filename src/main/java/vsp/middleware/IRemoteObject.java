package vsp.middleware;

/**
 * Represents an Object, that can be called by the middleware.
 */
public interface IRemoteObject {

    /**
     * Calls a method on a remote object with the given parameters.
     * @param serviceID the ID of the service that shall be called
     * @param intParameters service parameters of type int
     * @param stringParameters service parameters of type string
     */
    void call(int serviceID, int[] intParameters, String... stringParameters );
}
