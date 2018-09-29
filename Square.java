package Pacman;

import java.util.ArrayList;

import cs015.fnl.PacmanSupport.SupportMap;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * This class models a smart square class that keeps track of the elements inside of a particular square
 * @author xshi7
 *
 */
public class Square {
	
	private Rectangle _rectangle;
	private double _lx; // x-location
	private double _ly; // y-location
	private ArrayList<Collidable> _collidables; // hold elements in the square
	private boolean _isWall;
	
	
	/**
	 * This is the constructor. 
	 * It takes in two parameters of type double representing the x-location and y-location, and one parameter of type Color
	 * It sets up properties of the square
	 * @param x
	 * @param y
	 * @param color
	 */
	public Square(double x, double y, Color color) {
		_isWall = false;
		_collidables = new ArrayList<Collidable>();
		_lx = x;
		_ly = y;
		_rectangle = new Rectangle(Constants.SQUARE_SIDE, Constants.SQUARE_SIDE, color);
		_rectangle.setX(x);
		_rectangle.setY(y);
		
	}
	
	/**
	 * This is an accessor method. 
	 * It allows access of the node of the square
	 * it returns the node
	 * @return
	 */
	public Node getNode() {
		return _rectangle;
	}
	
	/**
	 * This is an accessor method.
	 * It returns the arrayList that contains the elements inside the square other than pacman and wall
	 * @return
	 */
	public ArrayList<Collidable> getCollidables() {
		return _collidables;
	}
	
	/**
	 * This is a mutator method that sets the boolean _isWall
	 */
	public void setWall() {
		_isWall = true;
	}
	
	/**
	 * This is an accessor method that returns the value of _isWall
	 * @return
	 */
	public boolean isWall() {
		return _isWall;
	}

}
