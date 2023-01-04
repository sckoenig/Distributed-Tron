package vsp.trongame.application.view.listener;

import javafx.scene.paint.Color;

public enum CoordinateColor {

    BLUE("#7ca7eb"),
    RED("#f68888"),
    YELLOW("#f6d388"),
    GREEN("#b7d977"),
    VIOLETT("#ce91ed"),
    WHITE("#f2eee1"),
    DEFAULT("#2e2f30");

    private final String hexCode;

    CoordinateColor(String color){
        this.hexCode = color;
    }

    public String getHex(){
        return this.hexCode;
    }

    public static CoordinateColor getByOrdinal(int ordinal){
        CoordinateColor[] results = values();
        if (ordinal > results.length-1 || ordinal < 0) return DEFAULT;

        return results[ordinal];
    }

    public Color getColor(){
        return Color.valueOf(hexCode);
    }


}
