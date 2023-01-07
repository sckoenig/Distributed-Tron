package vsp.trongame.applicationstub.model.rest;

/**
 * Protocol defined for playing with other implementations with the goal
 * of synchronizing each participant's game.
 */
public final class RESTProtocol {
    public static final String REST_NAMESERVER_ADDRESS = "https://name-service.onrender.com";
    public static final String PUT_NAMESERVER_ROUTE = "/entry";
    public static final String GET_NAMESERVER_ROUTE = "/entry/%s"; //entry/{name}
    public static final String COORDINATOR_NAME = "tron.coordinator";
    public static final String ROUTE_PUT_REGISTRATION = "/registration";
    public static final String ROUTE_PUT_GAME = "/game";
    public static final String ROUTE_PUT_STEERING = "/steering";
    public static final String SUPPORTED_METHOD = "PUT";
    public static final int STATUS_DENIED = 406;
    public static final int STATUS_NOT_AVAILABLE = 410;
    public static final int STATUS_OK = 200;

    private RESTProtocol(){}

}
