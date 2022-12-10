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
        List<IPlayer> crashedPlayers = new ArrayList<>();
        List<IPlayer> tempCrashedPlayers;
        for (int i = 0; i < alivePlayer.size(); i++) {
            if(alivePlayer.get(i).isAlive()){
                Coordinate coordinate = alivePlayer.get(i).getHeadPosition();
                if(arena.detectCollision(coordinate)){
                    crashedPlayers.add(alivePlayer.get(i));
                    alivePlayer.get(i).crash();
                }
                if (i != alivePlayer.size()) {
                    tempCrashedPlayers = headCollision(alivePlayer, i);
                    if (!tempCrashedPlayers.isEmpty()) {
                        crashedPlayers.addAll(tempCrashedPlayers);
                    }
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
    private List<IPlayer> headCollision(List<IPlayer> alivePlayer, int index){
        List<IPlayer> crashedPlayers = new ArrayList<>();
        IPlayer playerToInspect = alivePlayer.get(index);
        for (int i = index; i < alivePlayer.size(); i++) {
            if(i < alivePlayer.size()-1 && playerToInspect.getHeadPosition() == alivePlayer.get(i).getHeadPosition()){
                crashedPlayers.add(playerToInspect);
                crashedPlayers.add(alivePlayer.get(i));
            }
        }
        return crashedPlayers;
    }
}
