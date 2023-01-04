package vsp.middleware.serverstub;

public interface IUnmarshaller {

    void addToQueue(byte[] message);

    void setPort(int port);
}
