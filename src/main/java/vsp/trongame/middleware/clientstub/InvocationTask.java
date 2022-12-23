package vsp.trongame.middleware.clientstub;

import vsp.trongame.middleware.util.ServiceCall;

/**
 * Represents a task for the {@link Marshaller}.
 * @param serviceCall the service call that shall be performed
 * @param remoteId the remote id of the object to be called
 * @param protocol the protocol the sender should use
 */
public record InvocationTask(ServiceCall serviceCall, String remoteId, ISender.Protocol protocol) {

}

