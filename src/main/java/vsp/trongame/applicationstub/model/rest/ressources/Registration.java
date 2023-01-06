package vsp.trongame.applicationstub.model.rest.ressources;

/**
 * Registration REST ressource. Represents a SuperNode's registration at the Coordinator.
 * @param port the SuperNode's port
 * @param playerCount the SuperNode's participating player count
 */
public record Registration (int port, int playerCount) {
}
