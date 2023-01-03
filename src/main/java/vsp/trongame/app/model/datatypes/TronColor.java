package vsp.trongame.app.model.datatypes;

import java.util.Objects;

/**
 * Represents a Color that a Player Bike can have. Maps to javafx Colors.
 */
public enum TronColor {

    BLUE("#7ca7eb"),
    RED("#f68888"),
    YELLOW("#f6d388"),
    GREEN("#b7d977"),
    VIOLETT("#ce91ed"),
    WHITE("#f2eee1"),
    DEFAULT("#2e2f30");

    private final String color;

    TronColor(String color){
        this.color = color;
    }

    public String getHex(){
        return this.color;
    }

    public static TronColor getByOrdinal(int ordinal){
        TronColor[] results = TronColor.values();
        if (ordinal > results.length-1) throw new IllegalArgumentException("Unkown Ordinal!");

        return results[ordinal];
    }

    public static TronColor getTronColorByHex(String hex){
        for (TronColor color: values()) {
            if (Objects.equals(color.getHex(), hex)) return color;
        }
        throw new IllegalArgumentException("Unkown Color!");
    }


}
