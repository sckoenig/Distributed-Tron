package vsp.trongame.app.model.datatypes;

/**
 * Contains the DirectionChange enums, which can be calculated from the keyboard input of the player.
 */
public enum DirectionChange {
    NO_STEER,
    LEFT_STEER,
    RIGHT_STEER;

    public static DirectionChange getByOrdinal(int ordinal){

        DirectionChange[] results = DirectionChange.values();
        if (ordinal > results.length-1) throw new IllegalArgumentException("Unkown Ordinal!");

        return results[ordinal];
    }


}
