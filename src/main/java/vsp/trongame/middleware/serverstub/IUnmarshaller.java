package vsp.trongame.middleware.serverstub;

import java.net.InetSocketAddress;

public interface IUnmarshaller {

    void addToQueue(byte[] message);

    void setAddress(InetSocketAddress address);
}
