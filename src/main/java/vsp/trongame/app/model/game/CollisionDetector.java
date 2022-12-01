package vsp.trongame.app.model.game;

import java.util.List;

/**
 * Implements the Interface ICollisonDetector.
 */
public class CollisionDetector implements ICollisionDetector{

    @Override
    public void detectCollision(List<IPlayer> alivePlayer, IArena arena){

    }

    /**
     * Detects head collision with at least on other player.
     * @param alivePlayer list of all players that are still alive
     * @return a list of all crashed players
     */
    private List<IPlayer> headCollision(List<IPlayer> alivePlayer){
        return null;
    }
}
