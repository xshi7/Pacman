package Pacman;

/**
 * This enum class contains enums of directions
 * @author 
 *
 */
public enum Direction {
	
	LEFT, RIGHT, UP, DOWN;
	
	/**
	 * This method returns the opposite direction of the current direction.
	 * @return
	 */
	public Direction getOpposite () {
		switch(this) {
		case LEFT:
			return RIGHT;
		case RIGHT:
			return LEFT;
		case UP:
			return DOWN;
		default:
			return UP;
		}
	}
	
	
}