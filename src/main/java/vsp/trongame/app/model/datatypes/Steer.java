package vsp.trongame.app.model.datatypes;

/**
 * Represents a steer Object, which contains a playerID and a DirecttionChange.
 */
public class Steer {

    private int playerId;
    private DirectionChange directionChange;

    public int getPlayerId() {
        return playerId;
    }

    public DirectionChange getDirectionChange() {
        return directionChange;
    }
}
