package Pacman;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * This class models the Pacman and all his behaviors.
 * @author
 *
 */
public class Pacman {
	
	private Circle _circle;
	private double _lx; // x-location
	private double _ly; // y-location
	private Square[][] _map;
	private int[] _pacLoc; // pacman's location on the map
	private Direction _direction; // pacman's current direction of moving
	
	
	/**
	 * This is the constructor for Pacman.
	 * It takes a parameter of type 2-D array that represents the map.
	 * It takes in two parameters of type double that represents the graphical location of the pacman.
	 * This method sets up the pacman.
	 * @param map
	 * @param x
	 * @param y
	 */
	public Pacman(Square[][] map, double x, double y) {
		_direction = Direction.UP;
		_map = map;
		_lx = x;
		_ly = y;
		_pacLoc = new int[2];
		
		// convert from graphical location to logical location
		_pacLoc[0] = (int)(y/Constants.SQUARE_SIDE);
		_pacLoc[1] = (int)(x/Constants.SQUARE_SIDE);
		_circle = new Circle(x, y, 12, Color.YELLOW);	
	}
	
	/**
	 * This is an accessor method. 
	 * It allows access of the node of the circle
	 * it returns the node
	 * @return
	 */
	public Circle getNode() {
		return _circle;
	}
	
	/**
	 * This is a mutator method
	 * It sets x-location of the pacman
	 * @param x
	 */
	public void setX (double x) {
		_lx = x;
		_circle.setCenterX(x);
	}
	
	/**
	 * This is a mutator method
	 * It sets y-location of the pacman
	 * @param y
	 */
	public void setY (double y) {
		_ly = y;
		_circle.setCenterY(y);
	}
	

	/**
	 * This is an accessor method
	 * It allows access to the y-location of the pacman
	 * @return
	 */
	public double getY () {
		return _ly;
	}
	
	/**
	 * This is an accessor method
	 * It allows access to the x-location of the pacman
	 * @return
	 */
	public double getX () {
		return _lx;
	}
	
	
	/**
	 * This method resets the location of pacman to the passed in logical location.
	 * @param r
	 * @param c
	 */
	public void resetLocation(int r, int c) {
		
		// logically reset 
		_pacLoc[0] = r;
		_pacLoc[1] = c;
		
		// graphically reset
		this.setX(Constants.SQUARE_SIDE * c + Constants.DOT_OFFSET);
		this.setY(Constants.SQUARE_SIDE * r + Constants.DOT_OFFSET);
		
		// set to default direction UP
		_direction = Direction.UP;
	}
	
	
	/**
	 * This is an accossor method for the direction
	 * @return
	 */
	public Direction getDirection() {
		return _direction;
	}
	
	/**
	 * This is an mutator method for the direction that is used in game's KeyHandler
	 * It takes in a parameter of type direction
	 * @param d
	 */
	public void setDirection(Direction d) {
		
		// only change the direction if pacman is able to move in that direction
		switch(d) {
		case UP:
			if (this.canMoveUp()) {
				_direction = d;
			}
			break;
		case DOWN:
			if (this.canMoveDown()) {				
				_direction = d;
			}
			break;
		case LEFT:
			if (this.canMoveLeft()) {
				_direction = d;
			}
			break;
		case RIGHT:
			if (this.canMoveRight()) {
				_direction = d;
			}
			break;
		}		
	}
	
	
	/**
	 * This method moves Pacman leftwards
	 */
	public void moveLeft() {
		
		// wrapping
		if (_pacLoc[1] == 0) {
			this.setX(22*Constants.SQUARE_SIDE + Constants.DOT_OFFSET); 
			_pacLoc[1] = 22;
		}
		else {
			if (this.canMoveLeft()) {
				this.setX(this.getX() - Constants.SQUARE_SIDE); // graphically move
				_pacLoc[1] = _pacLoc[1] -1; // logically move
			}
		}				
	}

	
	/**
	 * This method checks if pacman is able to move to the left or not
	 * This method return true if pacman is free to move to the left,
	 * false otherwise.
	 * @return
	 */
	public boolean canMoveLeft() {
		
		if (_pacLoc[1] != 0 && _map[_pacLoc[0]][_pacLoc[1]-1].isWall() == true){
			return false;
		}
		_direction = Direction.LEFT;
		return true;
	}
	
	
	/**
	 * This method moves Pacman rightwards.
	 */
	public void moveRight() {
		
		// wrapping
		if (_pacLoc[1] == 22) {
			this.setX(Constants.DOT_OFFSET);
			_pacLoc[1] = 0;
		}
		else {
			if (this.canMoveRight()) {
				this.setX(this.getX() + Constants.SQUARE_SIDE); // graphically move
				_pacLoc[1] = _pacLoc[1] +1; // logically move
			}
		}				
	}
	
	
	/**
	 * This method checks if Pacman is able to move rightwards or not
	 * This method return true if pacman is free to move to the right,
	 * false otherwise.
	 * @return
	 */
	public boolean canMoveRight() {

		if (_pacLoc[1] != 22 && _map[_pacLoc[0]][_pacLoc[1]+1].isWall() == true){
			return false;
		}
		_direction = Direction.RIGHT;
		return true;
	}
	
	/**
	 * This method moves Pacman upwards.
	 */
	public void moveUp() {
		if (this.canMoveUp()) {
			this.setY(this.getY() - Constants.SQUARE_SIDE); // graphically move
			_pacLoc[0] = _pacLoc[0] -1; // logically move
		}	
	}
	
	
	/**
	 * This method checks if Pacman is able to move upwards or not
	 * This method return true if pacman is free to move up,
	 * false otherwise.
	 * @return
	 */
	public boolean canMoveUp() {
		
		if (_map[_pacLoc[0] - 1 ][_pacLoc[1]].isWall() == true){
			return false;
		}
		return true;
	}
	
	
	/**
	 * This method moves Pacman downwards.
	 */
	public void moveDown() {
		if (this.canMoveDown()) {
			this.setY(this.getY() + Constants.SQUARE_SIDE);
			_pacLoc[0] = _pacLoc[0] +1;
		}		
	}
	
	
	/**
	 * This method checks if Pacman is able to move downwards or not
	 * This method return true if pacman is free to move down,
	 * false otherwise.
	 * @return
	 */
	public boolean canMoveDown() {
		
		if (_map[_pacLoc[0] + 1 ][_pacLoc[1]].isWall() == true){
			return false;
		}
		return true;
	}
	
	
	/**
	 * This method is an accessor method for pacman's row location
	 * @return
	 */
	public int getRow() {
		return _pacLoc[0];
	}
	
	/**
	 * This method is an accessor method for pacman's column location
	 * @return
	 */
	public int getColumn() {
		return _pacLoc[1];
	}
	
	public void printLoc() {
		System.out.println(_pacLoc[0] + " " + _pacLoc[1]);
	}
}
