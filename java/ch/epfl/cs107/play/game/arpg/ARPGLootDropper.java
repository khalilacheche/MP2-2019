package ch.epfl.cs107.play.game.arpg;

public interface ARPGLootDropper {
    /** Handle what loot is dropped by Actor
     * Note: Need to be Override, can have empty body
     */
	void dropLoot();
}
