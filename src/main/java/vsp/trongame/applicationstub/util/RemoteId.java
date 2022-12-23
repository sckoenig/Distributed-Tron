package vsp.trongame.applicationstub.util;

import java.util.UUID;

/**
 * Unique Identifier for remote objects. The remote objects of a node share the same remote ID and
 * differ only by their provided services.
 */
public class RemoteId {

    public static final String DEFAULT_ID = ""; // for when the concrete remote id is not important
    private static final RemoteId INSTANCE = new RemoteId();

    public static RemoteId getRemoteId(){
        return INSTANCE;
    }

    private final UUID id;

    private RemoteId() {
        this.id = UUID.randomUUID();
    }

    public String getIdString() {
        return id.toString();
    }

}
