package vsp.middleware.namingservice;

public record NamingServiceMessage(byte messageType, int serviceId, String remoteId, String address) {

}
