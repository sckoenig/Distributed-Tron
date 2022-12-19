package vsp.trongame.app.model.gamemanagement;

import javafx.scene.input.KeyCode;
import vsp.trongame.app.model.datatypes.DirectionChange;
import vsp.trongame.app.model.datatypes.GameModus;
import vsp.trongame.app.model.datatypes.Steer;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Manages all adjustable parameters and prepares the properties files,
 */
public class Config {

    public static final Map<String, String> DEFAULTS;
    static{
        DEFAULTS = new HashMap<>();
        DEFAULTS.put("width","750");
        DEFAULTS.put("height", "60");
        DEFAULTS.put("rows", "30");
        DEFAULTS.put("columns", "50");
        DEFAULTS.put("waitingTimer", "5000");
        DEFAULTS.put("endingTimer", "5000");
        DEFAULTS.put("defaultPlayerNumber", "2");
        DEFAULTS.put("speed", "50");
        DEFAULTS.put("p1", "Q,W");
        DEFAULTS.put("p2", "C,V");
        DEFAULTS.put("p3", "D,F");
        DEFAULTS.put("p4", "O,P");
        DEFAULTS.put("p5", "M,N");
        DEFAULTS.put("p6", "Z,U");
        DEFAULTS.put("gameMode", "LOCAL");
        DEFAULTS.put("nameServer", "127.0.0.1:5555");
    }
    private Properties properties;
    private Map<KeyCode, Steer> keyMappings;
    private String filePath = "tronConfig.properties";

    public Config(){
        this.properties = new Properties();
        this.keyMappings = new HashMap<>();
        loadConfigFromFile();
        if(!isConfigValid()){
            try {
                properties = reloadConfig();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        setKeyMappings();
    }
    /**
     * Checks if the given config file is valid, if there are any missing keys or values.
     * @return if the config file is valid
     */
    private boolean isConfigValid(){

        if(!properties.containsKey("width") || !isNumber(properties.getProperty("width"))){
            return false;
        } else if (!properties.containsKey("height") || !isNumber((properties.getProperty("height")))) {
            return false;
        } else if (!properties.containsKey("rows") || !isNumber(properties.getProperty("rows")) ||
                Integer.parseInt(properties.getProperty("rows")) < 10) {
            return false;
        } else if (!properties.containsKey("columns") || !isNumber(properties.getProperty("columns")) ||
                Integer.parseInt(properties.getProperty("columns")) < 1) {
            return false;
        } else if (!properties.containsKey("waitingTimer") || !isNumber(properties.getProperty("waitingTimer"))) {

            return false;
        } else if (!properties.containsKey("endingTimer") || !isNumber(properties.getProperty("endingTimer"))) {
            return false;
        } else if (!properties.containsKey("defaultPlayerNumber") || !isNumber(properties.getProperty("defaultPlayerNumber"))
                    || 2 > Integer.parseInt(properties.getProperty("defaultPlayerNumber"))
                    || Integer.parseInt(properties.getProperty("defaultPlayerNumber")) > 6) {
            return false;
        } else if (!properties.containsKey("speed") || !isNumber(properties.getProperty("speed")) ||
                    1 > Integer.parseInt(properties.getProperty("speed")) || Integer.parseInt(properties.getProperty("speed")) > 100){
            return false;
        }
        else if (!properties.containsKey("p1") || isSteerValid(properties.getProperty("p1"))) {
            return false;
        } else if (!properties.containsKey("p2") || isSteerValid(properties.getProperty("p2"))) {
            return false;
        } else if (!properties.containsKey("p3") || isSteerValid(properties.getProperty("p3"))) {
            return false;
        } else if (!properties.containsKey("p4") || isSteerValid(properties.getProperty("p4"))) {
            return false;
        } else if (!properties.containsKey("p5") || isSteerValid(properties.getProperty("p5"))) {
            return false;
        } else if (!properties.containsKey("p6") || isSteerValid(properties.getProperty("p6"))) {
            return false;
        } else if(!properties.containsKey("gameMode") ||
                properties.getProperty("gameMode").equalsIgnoreCase(GameModus.LOCAL.name())){
            if (properties.getProperty("gameMode").equalsIgnoreCase(GameModus.NETWORK.name())) {
                return false;
            }
        } else if (!properties.containsKey("nameServer") || properties.getProperty("nameServer").equalsIgnoreCase("127.0.0.1:5555")) {
            return false;
        }
        return true;
    }

    /**
     * Checks if the given String is numeric.
     * @param string which should be a number.
     * @return if the string is a number
     */
    private boolean isNumber(String string){
        if (string == null) {
            return false;
        }
        try {
             Integer.parseInt(string);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /**
     * Checks if steer is only [a-zA-Z1-9] or the arrow keys.
     * @param steer which we want to validate
     * @return if steer is valid
     */
    private boolean isSteerValid(String steer){
        String regex = "a-zA-Z1-9";
        String[] tempStringArray = steer.split(",");
        if(!(tempStringArray[0].equalsIgnoreCase("Right") ||
                tempStringArray[0].equalsIgnoreCase("Left") ||
                tempStringArray[0].equalsIgnoreCase("Up") ||
                tempStringArray[0].equalsIgnoreCase("Down"))){
            if(!tempStringArray[0].matches(regex)){
                return false;
            }
        }
        if(!(tempStringArray[1].equalsIgnoreCase("Right") ||
                tempStringArray[1].equalsIgnoreCase("Left") ||
                tempStringArray[1].equalsIgnoreCase("Up") ||
                tempStringArray[1].equalsIgnoreCase("Down"))){
            return tempStringArray[1].matches(regex);
        }
        return true;
    }

    /**
     * Reloads the config file from the system.
     * @return the new properties file
     */
    private Properties reloadConfig() throws IOException {

        OutputStream outputStream = new FileOutputStream(filePath);

        Properties prop = new Properties();

        prop.setProperty("width", DEFAULTS.get("width"));
        prop.setProperty("height", DEFAULTS.get("height"));
        prop.setProperty("rows", DEFAULTS.get("rows"));
        prop.setProperty("columns", DEFAULTS.get("columns"));
        prop.setProperty("waitingTimer", DEFAULTS.get("waitingTimer"));
        prop.setProperty("endingTimer", DEFAULTS.get("endingTimer"));
        prop.setProperty("defaultPlayerNumber", DEFAULTS.get("defaultPlayerNumber"));
        prop.setProperty("speed", DEFAULTS.get("speed"));
        prop.setProperty("p1", DEFAULTS.get("p1"));
        prop.setProperty("p2", DEFAULTS.get("p2"));
        prop.setProperty("p3", DEFAULTS.get("p3"));
        prop.setProperty("p4", DEFAULTS.get("p4"));
        prop.setProperty("p5", DEFAULTS.get("p5"));
        prop.setProperty("p6", DEFAULTS.get("p6"));
        prop.setProperty("gameMode", DEFAULTS.get("gameMode"));
        prop.setProperty("nameServer", DEFAULTS.get("nameServer"));
        prop.store(outputStream, null);

        outputStream.close();
        return prop;
    }

    /**
     * Sets the keys and the directions to the key for each player.
     */
    private void setKeyMappings(){
        String keys;
        for (int i = 1; i < 7; i++) {
            keys = properties.getProperty("p"+i);
            String[] temp = keys.split(",");
            keyMappings.put(KeyCode.valueOf(temp[0]), new Steer(i, DirectionChange.LEFT_STEER));
            keyMappings.put(KeyCode.valueOf(temp[1]), new Steer(i, DirectionChange.RIGHT_STEER));
        }

    }

    /**
     * Gets the config from a file.
     */
    public void loadConfigFromFile() {
        File propertiesFile = new File(filePath);

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(propertiesFile))){
            properties.load(bis);
        }catch (IOException e){
            try {
                properties = reloadConfig();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * Gets an attribute to the given key from the config file.
     * @param key to which we want to get the attribute
     * @return the found attribute
     */
    public String getAttribut(String key){
        return properties.getProperty(key);
    }

    /**
     * Gets an steer object to a key.
     * @param key to which we want the steer
     * @return the steer object
     */
    public Steer getSteer(KeyCode key){
        return keyMappings.get(key);
    }
}
