package vsp.middleware.serverstub;

/**
 * Represents a Receiver for messages.
 */
public interface IReceiver {

    int PACKAGE_SIZE = 1400;
    int MIN_PORT = 1024;
    int MAX_PORT = 49152;

    /**
     * Starts the Receiver.
     */
    void stop();

    /**
     * Stops the Receiver.
     */
    void start();

}
