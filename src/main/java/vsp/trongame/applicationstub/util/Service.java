package vsp.trongame.applicationstub.util;

/**
 * Represents Services that can be provided.
 */
public enum Service {
    PREPARE,
    REGISTER,
    HANDLE_STEER,
    HANDLE_MANAGED_PLAYERS,
    HANDLE_GAME_STATE,
    UPDATE_ARENA,
    UPDATE_STATE,
    UPDATE_START,
    UPDATE_RESULT,
    UPDATE_COUNTDOWN,
    UPDATE_FIELD;

    public static Service getByOrdinal(int ordinal) {

        Service[] results = Service.values();
        if (ordinal > results.length - 1) throw new IllegalArgumentException("Unkown Ordinal!");

        return results[ordinal];
    }

}
