package vsp.trongame.applicationstub.model.rest.ressources;

/**
 * Steering REST ressource. Represents a player event.
 * @param playerId the player's id
 * @param turn the direction the player wants to turn in, must be "LEFT" or "RIGHT"
 */
public record Steering(Integer playerId, String turn) {
}
