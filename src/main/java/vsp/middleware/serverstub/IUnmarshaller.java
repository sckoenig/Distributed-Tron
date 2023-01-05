package vsp.middleware.serverstub;

public interface IUnmarshaller {

    /**
     * Adds a message to a queue.
     *
     * @param message which is added
     */
    void addToQueue(byte[] message);

    /**
     * Sets the given Port.
     *
     * @param port which is set
     */
    void setPort(int port);
}
