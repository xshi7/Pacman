package Pacman;

/**
 * This is the interface that dots, energizers and ghosts implements.
 * All collidable objects include a getScore method and a collide method.
 */
public interface Collidable {

	/**
	 * This method include what happens when pacman collide with a collidable object.
	 */
	public void collide();
	
	/**
	 * This method returns the score of the collidable method.
	 * @return
	 */
	public int getScore();
}
