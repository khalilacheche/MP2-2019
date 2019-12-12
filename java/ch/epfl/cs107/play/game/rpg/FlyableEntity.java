package ch.epfl.cs107.play.game.rpg;

public interface FlyableEntity {
	default boolean canFly() {
		return true;
	}
}
