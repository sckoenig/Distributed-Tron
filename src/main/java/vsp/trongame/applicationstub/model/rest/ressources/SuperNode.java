package vsp.trongame.applicationstub.model.rest.ressources;

import java.util.List;

/**
 * SuperNode REST ressource. Represents a SuperNode participating in a game.
 * @param address the SuperNode's address
 * @param lightCycles the SuperNode's List of registered players
 */
public record SuperNode(String address, List<LightCycle> lightCycles) {
}
