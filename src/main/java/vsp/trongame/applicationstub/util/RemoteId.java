package vsp.trongame.applicationstub.util;

import java.util.UUID;

/**
 * Unique Identifier for remote objects. The remote objects of a node share the same remote ID and
 * differ only by their provided services.
 */
public class RemoteId {

    private static final UUID ID = UUID.randomUUID();
    public static final String DEFAULT_ID = ""; // for when the concrete remote id is not important
    public static final String STRING_ID = ID.toString();

    private RemoteId(){}

}
