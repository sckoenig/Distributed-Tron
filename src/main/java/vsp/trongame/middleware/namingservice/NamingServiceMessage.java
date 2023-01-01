package vsp.trongame.middleware.namingservice;

public record NamingServiceMessage(byte messageType, int serviceId, String remoteId, String address) {

}
