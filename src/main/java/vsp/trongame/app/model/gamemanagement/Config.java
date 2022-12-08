package vsp.trongame.app.model.gamemanagement;

import javafx.scene.input.KeyCode;
import vsp.trongame.app.model.datatypes.Steer;

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

    /**
     * Checks if the given config file is valid, if there are any missing keys or values.
     * @return if the config file is valid
     */
    private boolean isConfigValid(){
        return false;
    }

    /**
     * Reloads the config file from the system.
     * @return the new properties file
     */
    private Properties reloadConfig(){
        return null;
    }

    /**
     * Sets the keys and the directions to the key for each player .
     */
    private void setKeyMappings(){

    }

    /**
     * Gets the config from an file.
     */
    public void loadConfigFromFile(){

    }

    /**
     * Gets an attribute to the given key from the config file.
     * @param key to which we want to get the attribute
     * @return the found attribute
     */
    public String getAttribut(String key){
        return null;
    }

    /**
     * Gets an steer object to a key.
     * @param key to which we want the steer
     * @return the steer onject
     */
    public Steer getSteer(KeyCode key){
        return null;
    }
}
