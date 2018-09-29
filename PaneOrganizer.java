package Pacman;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/**
 * This class is the top-level class that is initialized in the App class.
 * It creates the root BorderPane and a pane that contains a quit button.
 * @author 
 *
 */
public class PaneOrganizer {

	private BorderPane _root;
	private Game _game;
	
	/**
	 * This is the constructor of the PaneOrganizer class.
	 * It instantiates the root pane and the game, and it calls setUpButton
	 */
	public PaneOrganizer() {
		
		_root = new BorderPane();
		_game = new Game(_root);
		this.setupButton();
		
	}
	
	/**
	 * This an accessor method. It returns a variable of type Pane which is the root pane.
	 * @return
	 */
	public Pane getRoot() {
    	return _root;
    }
	
	/**
	 * This method sets up the quit button.
	 */
	public void setupButton() {
		HBox buttonPane = new HBox();
		buttonPane.setPrefHeight(20);
		_root.setBottom(buttonPane);
		Button b1 = new Button("Quit");	
	    buttonPane.getChildren().addAll(b1);
	    b1.setOnAction(new QuitHandler()); // register the button with QuitHandler
	    buttonPane.setAlignment(Pos.CENTER);
	    buttonPane.setFocusTraversable(false);
		buttonPane.setStyle("-fx-background-color: darkblue;");

	
	}  
	
	/**
     * This class specifies what happens when the quit button is clicked on - exit the game.
     * @author
     *
     */
	private class QuitHandler implements EventHandler<ActionEvent> {

    	@Override
        public void handle(ActionEvent event) {
            System.exit(0); // code to exit
        }
    }
}
