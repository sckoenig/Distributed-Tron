package vsp.trongame.app.model.gamemanagement;

import javafx.scene.input.KeyCode;
import vsp.trongame.app.model.datatypes.Direction;
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

    public static final Map<String, String> DEFAULTS = new HashMap<>();
    private Properties properties;
    private Map<KeyCode, Steer> keyMappings;
    private String filePath = "tronConfig.properties";

    public Config(){
        this.properties = new Properties();
        this.keyMappings = new HashMap<>();
        loadConfigFromFile();
        if(!isConfigValid()){
            reloadConfig();
        }
        setKeyMappings();
    }
    /**
     * Checks if the given config file is valid, if there are any missing keys or values.
     * @return if the config file is valid
     */
    private boolean isConfigValid(){

        if(!properties.containsKey("width")){
            System.out.println("width");
            return false;
        } else if (!properties.containsKey("height")) {
            System.out.println("height");
            return false;
        } else if (!properties.containsKey("rows") || Integer.parseInt(properties.getProperty("rows")) < 10) {
            System.out.println("rows");
            return false;
        } else if (!properties.containsKey("columns") || Integer.parseInt(properties.getProperty("columns")) < 1 ) {
            System.out.println("columns");
            return false;
        } else if (!properties.containsKey("defaultPlayerNumber") || 2 > Integer.parseInt(properties.getProperty("defaultPlayerNumber"))
                || Integer.parseInt(properties.getProperty("defaultPlayerNumber")) > 6 ) {
            System.out.println("defaultPlayerNumber");
            return false;
        } else if (1 > Integer.parseInt(properties.getProperty("speed")) || Integer.parseInt(properties.getProperty("speed")) > 100 || !properties.containsKey("speed")){
            System.out.println("speed");
            return false;
        }
        else if (!properties.containsKey("p1") || isSteerValid(properties.getProperty("p1"))) {
            System.out.println("p1");
            return false;
        } else if (!properties.containsKey("p2") || isSteerValid(properties.getProperty("p2"))) {
            System.out.println("p2");
            return false;
        } else if (!properties.containsKey("p3") || isSteerValid(properties.getProperty("p3"))) {
            System.out.println("p3");
            return false;
        } else if (!properties.containsKey("p4") || isSteerValid(properties.getProperty("p4"))) {
            System.out.println("p4");
            return false;
        } else if (!properties.containsKey("p5") || isSteerValid(properties.getProperty("p5"))) {
            System.out.println("p5");
            return false;
        } else if (!properties.containsKey("p6") || isSteerValid(properties.getProperty("p6"))) {
            System.out.println("p6");
            return false;
        } else if(!properties.containsKey("gameMode") ||
                properties.getProperty("gameMode").equalsIgnoreCase(GameModus.LOCAL.name())){
            if (properties.getProperty("gameMode").equalsIgnoreCase(GameModus.NETWORK.name())) {
                System.out.println("gameMode");
                return false;
            }
        } else if (!properties.containsKey("nameServer") || properties.getProperty("nameServer").equalsIgnoreCase("127.0.0.1:5555")) {
            System.out.println("nameServer");
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
            if(!tempStringArray[1].matches(regex)){
                return false;
            }
        }
        return true;
    }

    /**
     * Reloads the config file from the system.
     * @return the new properties file
     */
    private Properties reloadConfig(){
        System.out.println("geht noch nicht");
        return null;
    }

    /**
     * Sets the keys and the directions to the key for each player.
     */
    private void setKeyMappings(){
        String keys;
        for (int i = 1; i < 7; i++) {
            keys = properties.getProperty("p"+i);
            String[] temp = keys.split(",");
            keyMappings.put(KeyCode.valueOf(temp[0]), new Steer(i, DirectionChange.LEFT));
                    //PROBLEM steer braucht Direction wir haben aber nur DirectionChange
            keyMappings.put(KeyCode.valueOf(temp[1]), new Steer(i, DirectionChange.RIGHT));
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
            properties = reloadConfig();
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
        return null;
    }
}
