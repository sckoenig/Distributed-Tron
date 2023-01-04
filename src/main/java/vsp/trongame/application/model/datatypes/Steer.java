package vsp.trongame.application.model.datatypes;

/**
 * Represents a steer Object, which contains a playerID and a directtion.
 */
public record Steer(int playerId, DirectionChange directionChange) { }
