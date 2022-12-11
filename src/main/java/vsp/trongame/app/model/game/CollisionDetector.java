package vsp.trongame.app.model.game;

import edu.cads.bai5.vsp.tron.view.Coordinate;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements the Interface ICollisonDetector.
 */
public class CollisionDetector implements ICollisionDetector{

    @Override
    public void detectCollision(List<IPlayer> alivePlayer, IArena arena) {

        List<Integer> crashedPlayers = new ArrayList<>();

        for (int i = 0; i < alivePlayer.size(); i++) {
            if(alivePlayer.get(i).isAlive()){
                Coordinate coordinate = alivePlayer.get(i).getHeadPosition();
                if(arena.detectCollision(coordinate)){
                    crashedPlayers.add(alivePlayer.get(i).getId());
                    alivePlayer.get(i).crash();
                }
                if (i + 1 != alivePlayer.size()) { //wenn i nicht der letzte Spieler in der Liste ist
                    crashedPlayers.addAll(headCollision(alivePlayer, i));
                }
                arena.addPlayerPosition(alivePlayer.get(i).getId(), alivePlayer.get(i).getHeadPosition());
            }
        }
        arena.deletePlayerPositions(crashedPlayers);

    }
    /**
     * Detects head collision with at least on other player.
     * @param alivePlayer list of all players that are still alive
     * @return a list of all crashed players
     */
    private List<Integer> headCollision(List<IPlayer> alivePlayer, int index){
        List<Integer> crashedPlayers = new ArrayList<>();
        IPlayer playerToInspect = alivePlayer.get(index);
        for (int i = index + 1; i < alivePlayer.size(); i++) { //index+1 wollen bei dem nächsten Spieler anfangen
            if(playerToInspect.getHeadPosition() == alivePlayer.get(i).getHeadPosition()){ //keine Ahnung warum die Bedingung da eigentlich noch drin war (i < alivePlayer.size()-1 &&)
                playerToInspect.crash();
                alivePlayer.get(i).crash();
                crashedPlayers.add(playerToInspect.getId());
                crashedPlayers.add(alivePlayer.get(i).getId());
            }
        }
        return crashedPlayers;
    }
}
