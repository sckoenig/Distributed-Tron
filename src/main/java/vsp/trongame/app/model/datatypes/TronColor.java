package vsp.trongame.app.model.datatypes;

import javafx.scene.paint.Color;

/**
 * Represents a Color that a Player Bike can have. Maps to javafx Colors.
 */
public enum TronColor {

    BLUE(Color.ROYALBLUE),
    RED(Color.SALMON),
    YELLOW(Color.GOLD),
    GREEN(Color.MEDIUMSEAGREEN),
    VIOLETT(Color.MEDIUMPURPLE),
    WHITE(Color.LINEN),
    DEFAULT(Color.BLUEVIOLET.darker().darker().darker().desaturate());

    private final Color color;

    TronColor(Color color){
        this.color = color;
    }

    public Color getColor(){
        return this.color;
    }

    public static TronColor getByOrdinal(int ordinal){

        TronColor[] results = TronColor.values();
        if (ordinal > results.length-1) throw new IllegalArgumentException("Unkown Ordinal!");

        return results[ordinal];
    }


}
