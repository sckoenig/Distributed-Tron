package vsp.trongame.app.model.datatypes;

/**
 * Represents a steer Object, which contains a playerID and a directtion.
 */
public class Steer {

    private int playerId;
    private DirectionChange directionChange;

    public Steer(int playerId, DirectionChange directionChange){
        this.playerId = playerId;
        this.directionChange = directionChange;
    }
    public int getPlayerId() {
        return playerId;
    }

    public DirectionChange getDirectionChange() {
        return directionChange;
    }
}
