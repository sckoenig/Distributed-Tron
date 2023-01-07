package vsp.trongame.applicationstub.model.rest.registration;

public record RESTRegistration(String address, int playerCount, int lowestPlayerId) implements Comparable<RESTRegistration>{

    @Override
    public int compareTo(RESTRegistration o) {
        return Integer.compare(this.lowestPlayerId, o.lowestPlayerId);
    }
}
