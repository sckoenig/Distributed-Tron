package vsp.middleware.serverstub;

public interface IReceiver {

    int PACKAGE_SIZE = 1400;
    int MIN_PORT = 1024;
    int MAX_PORT = 49152;

    void stop();
    void start();

}
