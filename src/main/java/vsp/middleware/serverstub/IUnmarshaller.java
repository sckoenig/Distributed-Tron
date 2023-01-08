package vsp.middleware.serverstub;

/**
 * Represents an Unmarshaller, that unmarshalles byte[] messages.
 */
public interface IUnmarshaller {

    /**
     * Adds a message to this unmarshaller's queue.
     * @param message message to unmarshal
     */
    void addToQueue(byte[] message);

    /**
     * Informs the unmarshaller about the receiver's port for NamingService-Registration.
     * @param port the receiver's port.
     */
    void setPort(int port);

    /**
     * Stops this unmarshaller.
     */
    void stop();
}
