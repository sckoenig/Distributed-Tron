package vsp.trongame.middleware.namingservice;

import java.net.InetSocketAddress;

public record NamingServiceMessage(byte messageType, int serviceId, String remoteId, String address) {

}
