package vsp.trongame.application.model.gamemanagement;

import vsp.trongame.application.model.datatypes.DirectionChange;
import vsp.trongame.Modus;
import vsp.trongame.application.model.datatypes.Steer;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Manages all adjustable parameters and prepares the properties file.
 */
public class Configuration {
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
    public static final String NAME_SERVER_HOST = "nameServerHost";
    private static final String FILE_PATH = "tronConfig.properties";
    private static final Map<String, String> DEFAULTS;

    static {
        DEFAULTS = new HashMap<>(Map.of());
        DEFAULTS.put(WIDTH, "750");
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
        DEFAULTS.put(NAME_SERVER_HOST, "false");
    }

    private Properties properties;
    private final Map<String, Steer> keyMappings;

    public Configuration() {
        this.properties = new Properties();
        new Properties();
        this.keyMappings = new HashMap<>();
        loadConfigFromFile();
        if (!isConfigValid()) {
            try {
                properties = reloadConfig();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        setKeyMappings();
    }

    /**
     * Checks if the given config file is valid, if there are any missing keys or values.
     *
     * @return if the config file is valid
     */
    private boolean isConfigValid() {
        if (!hasAllAttributes()) return false;

        if (!isNumber(properties.getProperty(WIDTH), properties.getProperty(HEIGHT), properties.getProperty(ROWS),
                properties.getProperty(COLUMNS), properties.getProperty(WAITING_TIMER), properties.getProperty(ENDING_TIMER),
                properties.getProperty(DEFAULT_PLAYER_NUMBER), properties.getProperty(SPEED))) return false;

        if (Integer.parseInt(properties.getProperty(ROWS)) < 10 || Integer.parseInt(properties.getProperty(COLUMNS)) < 10)
            return false;
        if (2 > Integer.parseInt(properties.getProperty(DEFAULT_PLAYER_NUMBER))
                || Integer.parseInt(properties.getProperty(DEFAULT_PLAYER_NUMBER)) > 6)
            return false;

        if (1 > Integer.parseInt(properties.getProperty(SPEED)) || Integer.parseInt(properties.getProperty(SPEED)) > 500)
            return false;

        if (isSteerValid(properties.getProperty(P_EINS), properties.getProperty(P_ZWEI), properties.getProperty(P_DREI),
                properties.getProperty(P_VIER), properties.getProperty(P_FUENF), properties.getProperty(P_SECHS)))
            return false;
        if (!properties.getProperty(GAME_MODE).equalsIgnoreCase(Modus.LOCAL.toString()) && !properties.getProperty(GAME_MODE).equalsIgnoreCase(Modus.NETWORK.toString())
        && !properties.getProperty(GAME_MODE).equalsIgnoreCase(Modus.REST.toString())) return false;
        if (!properties.getProperty(NAME_SERVER_HOST).equalsIgnoreCase("true") && !properties.getProperty(NAME_SERVER_HOST).equalsIgnoreCase("false")) return false;
        //return properties.getProperty(NAME_SERVER).equalsIgnoreCase("127.0.0.1:5555"); //TODO check for valid ip:port string
        return true;
    }

    /**
     * Checks if all necessary attributes are present.
     * @return true, if all attributes are present, false otherwise.
     */
    private boolean hasAllAttributes() {
        return properties.keySet().equals(DEFAULTS.keySet());
    }

    /**
     * Checks if the given Strings are numeric.
     *
     * @param strings which should be a number.
     * @return true, if all strings are numbers, false otherwise
     */
    private boolean isNumber(String... strings) {
        for (String string : strings) {
            if (string == null) {
                return false;
            }
            try {
                Integer.parseInt(string);
            } catch (NumberFormatException nfe) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if given steers are only [a-zA-Z1-9] or the arrow keys.
     *
     * @param steers which we want to validate
     * @return true if all steers are valid, false otherwise
     */
    private boolean isSteerValid(String... steers) {
        for (String steer: steers) {
            if (steer.length() < 3 || !steer.contains(",")) {
                return false;
            }
            String[] tempStringArray = steer.split(",");
            if (!isValidKey(tempStringArray, 0) || !isValidKey(tempStringArray, 1)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the string is only [a-zA-Z1-9] or the arrow keys.
     *
     * @param tempStringArray which we want to validate
     * @param index           on which positions we want to check the String
     * @return if string is valid
     */
    private boolean isValidKey(String[] tempStringArray, int index) {
        String regex = "a-zA-Z1-9";
        return !(tempStringArray[index].equalsIgnoreCase("Right") ||
                tempStringArray[index].equalsIgnoreCase("Left") ||
                tempStringArray[index].equalsIgnoreCase("Up") ||
                tempStringArray[index].equalsIgnoreCase("Down") ||
                !tempStringArray[index].matches(regex));
    }

    /**
     * Reloads the config file from the system.
     *
     * @return the new properties file
     */
    private Properties reloadConfig() throws IOException {

        try (OutputStream outputStream = new FileOutputStream(FILE_PATH)) {
            Properties prop = new Properties();

            Set<Map.Entry<String, String>> entries = DEFAULTS.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                prop.setProperty(entry.getKey(), entry.getValue());
            }

            prop.store(outputStream, null);
            return prop;
        }
    }

    /**
     * Sets the keys and the directions to the key for each player.
     */
    private void setKeyMappings() {
        String keys;
        for (int i = 0; i < 6; i++) {
            keys = properties.getProperty("p" + (i+1));
            String[] temp = keys.split(",");
            keyMappings.put(temp[0], new Steer(i, DirectionChange.LEFT_STEER));
            keyMappings.put(temp[1], new Steer(i, DirectionChange.RIGHT_STEER));
        }
    }

    /**
     * Gets the config from a file.
     */
    private void loadConfigFromFile() {
        File propertiesFile = new File(FILE_PATH);

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(propertiesFile))) {
            properties.load(bis);
        } catch (IOException e) {
            try {
                properties = reloadConfig();
            } catch (IOException ex) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Gets an attribute to the given key from the config file.
     *
     * @param key to which we want to get the attribute
     * @return the found attribute
     */
    public String getAttribut(String key) {
        return properties.getProperty(key);
    }

    /**
     * Gets an steer object to a key.
     *
     * @param key to which we want the steer
     * @return the steer object
     */
    public Steer getSteer(String key) {
        return keyMappings.get(key);
    }

    /**
     * Finds the Key Mappings for a specific player
     * @param id the player's id
     * @return Key Mappings in the form of e.g. "Q,W" with Q being the right-key, and W being the left-key.
     */
    public String getKeyMappingForPlayer(int id) {
        return switch (id) {
            case 0 -> properties.getProperty(P_EINS);
            case 1 -> properties.getProperty(P_ZWEI);
            case 2 -> properties.getProperty(P_DREI);
            case 3 -> properties.getProperty(P_VIER);
            case 4 -> properties.getProperty(P_FUENF);
            case 5 -> properties.getProperty(P_SECHS);
            default -> "";
        };
    }

}
