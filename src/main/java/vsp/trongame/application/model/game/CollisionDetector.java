package vsp.trongame.application.model.game;

import edu.cads.bai5.vsp.tron.view.Coordinate;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements the Interface ICollisonDetector.
 */
public class CollisionDetector implements ICollisionDetector {

    @Override
    public void detectCollision(List<IPlayer> alivePlayer, IArena arena) {

        List<Integer> crashedPlayers = new ArrayList<>();

        for (int i = 0; i < alivePlayer.size(); i++) {
            IPlayer currentPlayer = alivePlayer.get(i);
            if (currentPlayer.isAlive()) {
                Coordinate coordinate = currentPlayer.getHeadPosition();
                if (arena.detectCollision(coordinate)) {
                    crashedPlayers.add(currentPlayer.getId());
                    currentPlayer.crash();
                }
                if (i + 1 != alivePlayer.size()) { //wenn i nicht der letzte Spieler in der Liste ist
                    crashedPlayers.addAll(headCollision(alivePlayer, i));
                }
                if (currentPlayer.isAlive()) {
                    arena.addPlayerPosition(currentPlayer.getId(), currentPlayer.getHeadPosition());
                }
            }
        }
        arena.deletePlayerPositions(crashedPlayers);
    }

    /**
     * Detects head collision with at least on other player.
     * @param alivePlayer list of all players that are still alive
     * @return a list of all crashed players
     */
    private List<Integer> headCollision(List<IPlayer> alivePlayer, int index) {
        List<Integer> crashedPlayers = new ArrayList<>();
        IPlayer playerToInspect = alivePlayer.get(index);
        for (int i = index + 1; i < alivePlayer.size(); i++) { //index+1 wollen bei dem nächsten Spieler anfangen
            if (playerToInspect.getHeadPosition() == alivePlayer.get(i).getHeadPosition()) {
                playerToInspect.crash();
                alivePlayer.get(i).crash();
                crashedPlayers.add(playerToInspect.getId());
                crashedPlayers.add(alivePlayer.get(i).getId());
            }
        }
        return crashedPlayers;
    }
}
