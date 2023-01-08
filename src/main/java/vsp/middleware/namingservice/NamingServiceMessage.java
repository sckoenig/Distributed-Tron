package vsp.middleware.namingservice;

/**
 * Message format used between {@link NameResolver} and {@link NameServer}.
 * @param messageType the type of the message, see Message Types.
 * @param serviceId the service's id or {@value NO_SERVICE} on {@value UNREGISTER} or {@value RESPONSE}
 * @param remoteId the service provider's id or {@value NO_REMOTE_ID} on {@value RESPONSE} or {@value LOOKUP} if remoteId is not relevant.
 * @param address the service provider's address or {@value NO_ADDRESS} on {@value UNREGISTER} or {@value LOOKUP}
 */
public record NamingServiceMessage(byte messageType, int serviceId, String remoteId, String address) {

    /* Default values */
   public static final int NO_SERVICE = -1;
   public static final String NO_ADDRESS = "";
   public static final String NO_REMOTE_ID = "";

    /* Message Types */
   public static final byte REGISTER = 1;
   public static final byte UNREGISTER = 2;
   public static final byte LOOKUP = 3;
   public static final byte RESPONSE = 4;
}
