package vsp.trongame.applicationstub.model.rest.ressources;

import java.util.List;

/**
 * Game REST ressource.
 * @param superNodes an ordered list of {@link SuperNode} entries that take part in the game.
 */
public record Game(List<SuperNode> superNodes) {
}
