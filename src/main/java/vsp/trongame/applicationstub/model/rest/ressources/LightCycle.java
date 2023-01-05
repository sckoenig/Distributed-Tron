package vsp.trongame.applicationstub.model.rest.ressources;

import edu.cads.bai5.vsp.tron.view.Coordinate;
import vsp.trongame.application.model.datatypes.Direction;

public record LightCycle(int playerId, Coordinate position, Direction direction) {

}
