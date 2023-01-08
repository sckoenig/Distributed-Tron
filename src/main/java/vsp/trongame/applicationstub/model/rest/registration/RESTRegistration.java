package vsp.trongame.applicationstub.model.rest.registration;

/**
 * Represents a supernodes registration.
 * @param address the supernode's address
 * @param playerCount the supernode's player count
 * @param lowestPlayerId the supernode's lowest playerId for sorting purposes
 */
public record RESTRegistration(String address, int playerCount, int lowestPlayerId) implements Comparable<RESTRegistration>{

    @Override
    public int compareTo(RESTRegistration o) {
        return Integer.compare(this.lowestPlayerId, o.lowestPlayerId);
    }
}
