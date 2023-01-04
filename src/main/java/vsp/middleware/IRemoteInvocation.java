package vsp.middleware;

/**
 * Represents remote method invocations.
 */
public interface IRemoteInvocation {

    /**
     * Represents the type of invocation that shall be performed.
     */
    enum InvocationType{
        RELIABLE,
        UNRELIABLE
    }

    /**
     * Invokes a Method.
     * @param remoteID the ID of the remote Object that shall be called
     * @param serviceID the ID of the service that shall be called
     * @param type the type of invocation that shall be performed
     * @param intParameters the method's parameters of type int
     * @param stringParameters the method's parameters of type string
     */
    void invoke(String remoteID, int serviceID, InvocationType type, int[] intParameters, String... stringParameters);
}
