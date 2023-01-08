package vsp.trongame.applicationstub.model.rest.ressources;

import edu.cads.bai5.vsp.tron.view.Coordinate;
import vsp.trongame.application.model.datatypes.Direction;

/**
 * Light Cycle REST ressource. Represents a player participating in a game.
 * @param playerId the player's id
 * @param position the player's position
 * @param direction the player's direction
 */
public record LightCycle(int playerId, Coordinate position, Direction direction) {

}
