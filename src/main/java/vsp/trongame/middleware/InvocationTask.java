package vsp.trongame.middleware;

public record InvocationTask(MethodCall methodCall, String remoteId,
                             ISender.Protocol protocol) {

}

