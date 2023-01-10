package vsp.trongame.applicationstub.model.rest.ressources;

/**
 * Registration REST ressource. Represents a SuperNode's registration at the Coordinator.
 * @param playerCount the SuperNode's participating player count
 * @param uri the SuperNode's address
 */
public record Registration (int playerCount, String uri) {
}
