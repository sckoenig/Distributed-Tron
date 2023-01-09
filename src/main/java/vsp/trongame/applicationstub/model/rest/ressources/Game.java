package vsp.trongame.applicationstub.model.rest.ressources;

/**
 * Game REST ressource.
 * @param superNodes an ordered list of {@link SuperNode} entries that take part in the game.
 */
public record Game(SuperNode[] superNodes) {
}
