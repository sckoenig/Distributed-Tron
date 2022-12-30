package vsp.trongame.app.model.datatypes;

import java.util.Objects;

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

    public static GameResult getByOrdinal(int ordinal){

        GameResult[] results = GameResult.values();
        if (ordinal > results.length-1) throw new IllegalArgumentException("Unkown Ordinal!");

        return results[ordinal];
    }

    public static GameResult getGameResultByText(String text){
        for (GameResult result: values()) {
            if (Objects.equals(result.getResultText(), text)) return result;
        }
        throw new IllegalArgumentException("Unkown Result!");
    }
    
}
