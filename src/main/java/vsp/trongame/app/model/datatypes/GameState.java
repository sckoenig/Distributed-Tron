package vsp.trongame.app.model.datatypes;

/**
 * Contains the enums for the current game state
 */
public enum GameState {
    INIT,
    REGISTRATION,
    STARTING,
    RUNNING,
    FINISHING;

    public static GameState getByOrdinal(int ordinal){

        GameState[] results = GameState.values();
        if (ordinal > results.length-1) throw new IllegalArgumentException("Unkown Ordinal!");

        return results[ordinal];
    }
}
