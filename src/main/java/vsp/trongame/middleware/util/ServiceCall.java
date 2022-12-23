package vsp.trongame.middleware.util;

/**
 * Represents a Method Call to a remote Object.
 * @param serviceId the id of the service that shall be called
 * @param intParameters service parameters of type int
 * @param stringParameters service parameters of type string
 */
public record ServiceCall(int serviceId, int[] intParameters, String[] stringParameters) {

}
