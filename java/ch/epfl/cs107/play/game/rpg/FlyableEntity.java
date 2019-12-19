package ch.epfl.cs107.play.game.rpg;

/**
 * FlyableEntity interface 
 *
 */
public interface FlyableEntity {
	/**
	 * @return the entity canFly or not
	 */
	default boolean canFly() {
		return true;
	}
}
