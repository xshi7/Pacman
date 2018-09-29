package Pacman;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
  * This is the  main class where your Pacman game will start.
  * The main method of this application calls the App constructor. You
  * will need to fill in the constructor to instantiate your game.
  *
  *
  * This is the App class. This is what we use to start our program.
  * It contains the constructor and the main method.
  *
  */

public class App extends Application {

	/**
	 * This is the constructor for the App class. This is method is automatically called when a JavaFX
	 * program is launched.
	 */
    @Override
    public void start(Stage stage) {
        // Create top-level object, set up the scene, and show the stage here.
    	PaneOrganizer organizer = new PaneOrganizer();
    	Scene scene = new Scene(organizer.getRoot(), Constants.APP_WIDTH, Constants.APP_HEIGHT);
        stage.setScene(scene);
        stage.setTitle("PacMan");
        stage.show();
    }

    /*
    * Here is the mainline! No need to change this.
    */
    public static void main(String[] argv) {
        // launch is a method inherited from Application
        launch(argv);
    }
}
