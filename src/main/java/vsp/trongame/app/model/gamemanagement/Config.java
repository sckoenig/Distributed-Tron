package vsp.trongame.app.model.gamemanagement;

import javafx.scene.input.KeyCode;
import vsp.trongame.app.model.datatypes.DirectionChange;
import vsp.trongame.app.model.datatypes.GameModus;
import vsp.trongame.app.model.datatypes.Steer;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Manages all adjustable parameters and prepares the properties files,
 */
public class Config {

    public static final String WIDTH = "width";
    public static final String HEIGHT = "height";
    public static final String ROWS = "rows";
    public static final String COLUMNS = "columns";
    public static final String WAITING_TIMER = "waitingTimer";
    public static final String ENDING_TIMER = "endingTimer";
    public static final String DEFAULT_PLAYER_NUMBER = "defaultPlayerNumber";
    public static final String SPEED = "speed";
    public static final String P_EINS = "p1";
    public static final String P_ZWEI = "p2";
    public static final String P_DREI = "p3";
    public static final String P_VIER = "p4";
    public static final String P_FUENF = "p5";
    public static final String P_SECHS = "p6";
    public static final String GAME_MODE = "gameMode";
    public static final String NAME_SERVER = "nameServer";


    public static final Map<String, String> DEFAULTS;
    static{
        DEFAULTS = new HashMap<>();
        DEFAULTS.put(WIDTH,"750");
        DEFAULTS.put(HEIGHT, "600");
        DEFAULTS.put(ROWS, "30");
        DEFAULTS.put(COLUMNS, "50");
        DEFAULTS.put(WAITING_TIMER, "5000");
        DEFAULTS.put(ENDING_TIMER, "5000");
        DEFAULTS.put(DEFAULT_PLAYER_NUMBER, "2");
        DEFAULTS.put(SPEED, "50");
        DEFAULTS.put(P_EINS, "Q,W");
        DEFAULTS.put(P_ZWEI, "C,V");
        DEFAULTS.put(P_DREI, "D,F");
        DEFAULTS.put(P_VIER, "O,P");
        DEFAULTS.put(P_FUENF, "M,N");
        DEFAULTS.put(P_SECHS, "Z,U");
        DEFAULTS.put(GAME_MODE, "LOCAL");
        DEFAULTS.put(NAME_SERVER, "127.0.0.1:5555");
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

        if(!properties.containsKey(WIDTH) || !isNumber(properties.getProperty(WIDTH))){
            return false;
        } else if (!properties.containsKey(HEIGHT) || !isNumber((properties.getProperty(HEIGHT)))) {
            return false;
        } else if (!properties.containsKey(ROWS) || !isNumber(properties.getProperty(ROWS)) ||
                Integer.parseInt(properties.getProperty(ROWS)) < 10) {
            return false;
        } else if (!properties.containsKey(COLUMNS) || !isNumber(properties.getProperty(COLUMNS)) ||
                Integer.parseInt(properties.getProperty(COLUMNS)) < 10) {
            return false;
        } else if (!properties.containsKey(WAITING_TIMER) || !isNumber(properties.getProperty(WAITING_TIMER))) {

            return false;
        } else if (!properties.containsKey(ENDING_TIMER) || !isNumber(properties.getProperty(ENDING_TIMER))) {
            return false;
        } else if (!properties.containsKey(DEFAULT_PLAYER_NUMBER) || !isNumber(properties.getProperty(DEFAULT_PLAYER_NUMBER))
                    || 2 > Integer.parseInt(properties.getProperty(DEFAULT_PLAYER_NUMBER))
                    || Integer.parseInt(properties.getProperty(DEFAULT_PLAYER_NUMBER)) > 6) {
            return false;
        } else if (!properties.containsKey(SPEED) || !isNumber(properties.getProperty(SPEED)) ||
                    1 > Integer.parseInt(properties.getProperty(SPEED)) || Integer.parseInt(properties.getProperty(SPEED)) > 100){
            return false;
        }
        else if (!properties.containsKey(P_EINS) || isSteerValid(properties.getProperty(P_EINS))) {
            return false;
        } else if (!properties.containsKey(P_ZWEI) || isSteerValid(properties.getProperty(P_ZWEI))) {
            return false;
        } else if (!properties.containsKey(P_DREI) || isSteerValid(properties.getProperty(P_DREI))) {
            return false;
        } else if (!properties.containsKey(P_VIER) || isSteerValid(properties.getProperty(P_VIER))) {
            return false;
        } else if (!properties.containsKey(P_FUENF) || isSteerValid(properties.getProperty(P_FUENF))) {
            return false;
        } else if (!properties.containsKey(P_SECHS) || isSteerValid(properties.getProperty(P_SECHS))) {
            return false;
        } else if(!properties.containsKey(GAME_MODE) ||
                properties.getProperty(GAME_MODE).equalsIgnoreCase(GameModus.LOCAL.name())){
            if (properties.getProperty(GAME_MODE).equalsIgnoreCase(GameModus.NETWORK.name())) {
                return false;
            }
        } else if (!properties.containsKey(NAME_SERVER) || properties.getProperty(NAME_SERVER).equalsIgnoreCase("127.0.0.1:5555")) {
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
        if(steer.length() < 3 || !steer.contains(",")){
            return false;
        }
        String[] tempStringArray = steer.split(",");
        if(!isValidKey(tempStringArray, 0) || !isValidKey(tempStringArray, 1)){
            return false;
        }
        return true;
    }

    /**
     * Checks if the string is only [a-zA-Z1-9] or the arrow keys.
     * @param tempStringArray which we want to validate
     * @param index on which positions we want to check the String
     * @return if string is valid
     */
    private boolean isValidKey(String[] tempStringArray, int index){
        String regex = "a-zA-Z1-9";
        if(!(tempStringArray[index].equalsIgnoreCase("Right") ||
                tempStringArray[index].equalsIgnoreCase("Left") ||
                tempStringArray[index].equalsIgnoreCase("Up") ||
                tempStringArray[index].equalsIgnoreCase("Down"))){
            if(!tempStringArray[index].matches(regex)){
                return false;
            }
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

        Set<Map.Entry<String, String>> entries = DEFAULTS.entrySet();
        for(Map.Entry<String, String> entry : entries){
            prop.setProperty(entry.getKey(), entry.getValue());
        }

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
