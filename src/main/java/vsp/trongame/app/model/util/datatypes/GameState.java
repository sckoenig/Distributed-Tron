package vsp.trongame.app.model.util.datatypes;

/**
 * Contains the enums for the current game state
 */
public enum GameState {
    INIT,
    PREPARING,
    COUNTDOWN,
    RUNNING,
    FINISHED;

    public static GameState getByOrdinal(int ordinal){

        GameState[] results = GameState.values();
        if (ordinal > results.length-1) throw new IllegalArgumentException("Unkown Ordinal!");

        return results[ordinal];
    }
}
