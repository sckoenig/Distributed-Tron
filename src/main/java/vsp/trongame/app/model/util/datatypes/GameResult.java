package vsp.trongame.app.model.util.datatypes;

/**
 * Represents the Game Result Text, that can be displayed in GUI.
 */

public enum GameResult {
    
    WON("Der Gewinner ist ..."),
    DRAW("Unentschieden!");

    private final String resultText;

    GameResult(String resultText){
        this.resultText = resultText;
    }

    public String getResultText() {
        return this.resultText;
    }

    public GameResult getByOrdinal(int ordinal){

        GameResult[] results = GameResult.values();
        if (ordinal > results.length-1) throw new IllegalArgumentException("Unkown Ordinal!");

        return results[ordinal];
    }
    
}
