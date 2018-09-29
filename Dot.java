package Pacman;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * This class models a dot and all the game behaviors associated with it.
 * @author 
 *
 */
public class Dot implements Collidable{
	
	private Circle _circle;
	double _lx; // x-location
	double _ly; // y-location
	private Pane _gamePane;
	private int _score;
	private int[] _dotLoc; // 2-element array that contains the x- and y-location of the dot
	private Game _game;


	/**
	 * This is the constructor of the dot.
	 * It takes in a game parameter to create association with the game class.
	 * It takes a parameter of type int indicating the score.
	 * It takes a parameter of type Pane that represents the gamePane 
	 * It takes in two parameters of type double indicating the graphical location of the dot.
	 * 
	 * @param game
	 * @param score
	 * @param gamePane
	 * @param x
	 * @param y
	 */
	public Dot(Game game, int score, Pane gamePane, double x, double y) {
		_game = game;
		_score = score;
		_gamePane = gamePane;
		_lx = x;
		_ly = y;
		_dotLoc = new int[2];
		
		//convert from graphical location to logical location
		_dotLoc[0] = (int)(y/Constants.SQUARE_SIDE); 
		_dotLoc[1] = (int)(x/Constants.SQUARE_SIDE);
		_circle = new Circle(x, y, 3, Color.WHITE);		
	}
	
	/**
	 * This is an accessor method. 
	 * It allows access of the node of the circle
	 * it returns the node
	 * @return
	 */
	public Node getNode() {
		return _circle;
	}
	
	/**
	 * This method models what happens when pacman collides with a dot.
	 */
	public void collide() {

		_gamePane.getChildren().remove(_circle); // graphically remove
		_game.decreaseDot(); // dot number decrease

	}

	@Override
	/**
	 * This is an accessor method for the score 
	 */
	public int getScore() {
		// TODO Auto-generated method stub
		return _score;
	}

}
