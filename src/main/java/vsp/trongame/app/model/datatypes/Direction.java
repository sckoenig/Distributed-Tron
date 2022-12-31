package vsp.trongame.app.model.datatypes;

/**
 * Represents the directions a player can move in.
 */
public enum Direction {
    LEFT,
    UP,
    RIGHT,
    DOWN;

    public static Direction getNextDirection(Direction oldDirection, DirectionChange directionChange) {
        if (directionChange == DirectionChange.NO_STEER) return oldDirection;
        switch (oldDirection) {
            case LEFT -> {
                if (directionChange == DirectionChange.LEFT_STEER) return Direction.DOWN;
                else return Direction.UP;
            }
            case RIGHT -> {
                if (directionChange == DirectionChange.LEFT_STEER) return Direction.UP;
                else return Direction.DOWN;
            }
            case DOWN -> {
                if (directionChange == DirectionChange.LEFT_STEER) return Direction.RIGHT;
                else return Direction.LEFT;
            }
            case UP -> {
                if (directionChange == DirectionChange.LEFT_STEER) return Direction.LEFT;
                else return Direction.RIGHT;
            }
            default -> {
                return oldDirection;
            }
        }
    }
}
