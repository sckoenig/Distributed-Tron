package vsp.trongame.applicationstub.model.rest;

public record RESTRegistration(String address, int playerCount, int lowestPlayerId) implements Comparable<RESTRegistration>{

    @Override
    public int compareTo(RESTRegistration o) {
        if (this.lowestPlayerId < o.lowestPlayerId){
            return -1;
        } else if (this.lowestPlayerId == o.lowestPlayerId) {
            return 0;
        }else{
            return 1;
        }
    }
}
