package Pacman;

import java.util.ArrayList;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * This class models an energizer and all the game behaviors associated with it.
 * @author
 *
 */
public class Energizer implements Collidable{
	
	private Circle _circle;
	private Pane _gamePane;
	private int _score; // indicates how much would the score increase if an energizer is eaten
	private int[] _enerLoc; // 2-element array that contains the x- and y-location of the energizer
	private Game _game;

	/**
	 * This is the constructor of the energizer.
	 * It takes in a game parameter to create association with the game class.
	 * It takes a parameter of type int indicating the score.
	 * It takes a parameter of type Pane that represents the gamePane 
	 * It takes in two parameters of type double indicating the physical location of the energizer.
	 * 
	 * @param game
	 * @param score
	 * @param gamePane
	 * @param x
	 * @param y
	 */
	public Energizer(Game game, int score, Pane gamePane, double x, double y) {
		_game = game;
		_score = score;
		_gamePane = gamePane;
		_enerLoc = new int[2];
		
		// store logical locations
		_enerLoc[0] = (int)(y/Constants.SQUARE_SIDE);
		_enerLoc[1] = (int)(x/Constants.SQUARE_SIDE);
		_circle = new Circle(x, y, 6, Color.WHITE);		
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
	 * This method models what happens when pacman collides with an energizer
	 */
	public void collide() {
			
		_gamePane.getChildren().remove(_circle); // graphically remove
		_game.decreaseEnergizer(); // energizer is one less
		_game.setFrightenedCount(); 
		
		// all ghosts change color
		Ghost[] ghosts = _game.getGhosts();
		System.out.println("1");
		for (int i = 0; i < 4; i ++ ) {
			ghosts[i].changeColor(Color.LIGHTCYAN);
		}
		
		_game.setFrightened(); // frightened mode starts
	}
	
	/**
	 * This is an accessor method that returns the score of energizer
	 */
	public int getScore() {
		return _score;
	}

}
