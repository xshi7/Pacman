package Pacman;


import java.util.ArrayList;
import java.util.LinkedList;

import cs015.fnl.PacmanSupport.BoardLocation;
import cs015.fnl.PacmanSupport.SupportMap;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

/**
 * This class models game logic incuding setting up the game and what happens in different modes
 * @author
 *
 */
public class Game {
	
	private BorderPane _root; 
	private Pane _gamePane;
	private Square[][] _map;
	private Pacman _pacman;
	private int _score;
	private Timeline _timeline;
	private Timeline _ghostPenTimeline;
	private Ghost _rGhost;
	private Ghost _bGhost;
	private Ghost _oGhost;
	private Ghost _pGhost;
	private Mode _mode; // which of the three modes is pacman in
	private int _live;
	private int _chaseCount; // stores how many seconds of chase mode is left
	private int _scatterCount;
	private int _frightenedCount;
	private int _pacmanStartRow; //pacman start row-location on the map
	private int _pacmanStartColumn; //pacman start column-location on the map
	private LinkedList<Ghost> _ghostQueue; // queue of ghosts in the ghostpen
	private int _penCount; // stores how many seconds is left till pen releases ghost
	private KeyHandler _keyHandler;
	private int _dotNumber;
	private int _energizerNumber;
	private Label _livesLabel;
	private Label _scoreLabel;
	private Label _status; // if the game is over or is won 
	private boolean _ghostCollide; // if at the current timeframe pacman collide with ghost
	
	
	/**
	 * This is constructor for Game. 
	 * It takes in a parameter of type BorderPane, associating the class with PaneOrganizer.
	 * @param root
	 */
	public Game(BorderPane root) {
		
		_ghostCollide = false; 		
		_dotNumber = Constants.DOT_NUMBER;
		_energizerNumber = Constants.ENERGIZER_NUMBER;
		_ghostQueue = new LinkedList<Ghost>();
		_mode = Mode.Chase; // original mode is chase
		_chaseCount = (int)(Constants.CHASE_TIME/Constants.DURATION);
		_frightenedCount = (int)(Constants.FRIGHTENED_TIME/Constants.DURATION);
		this.resetPenCount();
		_live = 3;
		_score = 0;
		_root = root;
		this.setUpGamePane();
		this.setUpMap();
		this.resetGhostQueue();
		this.setupTimeLine();	
		this.setupGhostPen();
		this.setupLabelPane();
	}
	
	
	/**
	 * This method sets up the score and lives labels.
	 */
	private void setupLabelPane() {
		HBox labelPane = new HBox(); 
		_root.setTop(labelPane); 
		_scoreLabel = new Label("Score: " + _score);
		_scoreLabel.setFont(Font.font("Cambria", 15));
		_scoreLabel.setTextFill(Color.YELLOW);
		_livesLabel = new Label("Lives: " + _live);
		_livesLabel.setFont(Font.font("Cambria", 15));
		_livesLabel.setTextFill(Color.YELLOW);
		labelPane.getChildren().addAll(_scoreLabel, _livesLabel);
		labelPane.setSpacing(70);
		labelPane.setAlignment(Pos.CENTER);
		labelPane.setStyle("-fx-background-color: darkblue;");
		
	}
	
	/**
	 * This method returns the row location of Pacman
	 * @return
	 */
	public int getPacLocrow() {
		return _pacman.getRow();
	}
	
	/**
	 * This method returns the column location of Pacman
	 * @return
	 */
	public int getPacLoccolumn() {
		return _pacman.getColumn();
	}
	
	/**
	 * This method resets the time left till pen releases next ghost
	 */
	public void resetPenCount() {
		_penCount = (int)(Constants.PEN_TIME/Constants.DURATION);
	}
	
	/**
	 * This method sets up the game pane
	 */
	private void setUpGamePane() {
		_keyHandler = new KeyHandler();
		_gamePane = new Pane();
		_gamePane.setStyle("-fx-background-color: darkblue;");

		_root.setCenter(_gamePane);
		_gamePane.setPrefHeight(600);
		_gamePane.addEventHandler(KeyEvent.KEY_PRESSED, _keyHandler); // register gamePane with KeyHandler
    	_gamePane.setFocusTraversable(true);

    	// sets up status (could be game over or game won)
		_status = new Label("PACMAN");
		_status.setFont(Font.font("Cambria", 15));
		_status.setTextFill(Color.YELLOW);
		_status.setLayoutY(315);
		_status.setLayoutX(255);

		_gamePane.getChildren().add(_status);
		_status.toFront();

	}
	
	/**
	 * This method sets the variable _ghostCollide according the the passed in boolean
	 * @param b
	 */
	public void setGhostCollide(boolean b) {
		_ghostCollide = b;
	}
	
	/**
	 * This method sets up the map.
	 */
	private void setUpMap() {
		BoardLocation[][] map = SupportMap.getMap();
		_map = new Square[map.length][map[0].length]; // consists of smart square objects
		
		// for every cell on the map
		for (int r = 0; r < map.length; r ++ ) {
			for (int c = 0; c < map[0].length; c ++ ) {
				
				//if it is a wall
				if(map[r][c] == BoardLocation.WALL){
					//logically add wall
					_map[r][c] = new Square(c*Constants.SQUARE_SIDE, r*Constants.SQUARE_SIDE, Color.DARKBLUE);
					_map[r][c].setWall();
					//graphically add wall
					_gamePane.getChildren().add(_map[r][c].getNode()); 
				}
				
				// all other cases
				else{
					_map[r][c] = new Square(c*Constants.SQUARE_SIDE, r*Constants.SQUARE_SIDE, Color.BLACK);
					_gamePane.getChildren().add(_map[r][c].getNode()); 
					
					if (map[r][c] == BoardLocation.DOT) {
						Dot dot = new Dot(this, Constants.DOT_SCORE, _gamePane, c*Constants.SQUARE_SIDE + Constants.DOT_OFFSET, r*Constants.SQUARE_SIDE + Constants.DOT_OFFSET);
						_map[r][c].getCollidables().add(dot);
						_gamePane.getChildren().add(dot.getNode());
					}
					
					else if (map[r][c] == BoardLocation.ENERGIZER) {
						Energizer energizer = new Energizer(this, Constants.ENERGIZER_SCORE, _gamePane, c*Constants.SQUARE_SIDE + Constants.DOT_OFFSET, r*Constants.SQUARE_SIDE + Constants.DOT_OFFSET);
						_map[r][c].getCollidables().add(energizer);
						_gamePane.getChildren().add(energizer.getNode());
					}
					
					else if (map[r][c] == BoardLocation.PACMAN_START_LOCATION) {
						_pacman = new Pacman(_map, c*Constants.SQUARE_SIDE + Constants.DOT_OFFSET, r*Constants.SQUARE_SIDE + Constants.DOT_OFFSET);
						_gamePane.getChildren().add(_pacman.getNode());
						
						// store pacman's starting location to be used for resetting in collide
						_pacmanStartRow = r;
						_pacmanStartColumn = c;
					}
					
				}
			}
		}
		
		// sets up ghosts
		_rGhost = new Ghost(this, _map, 11*Constants.SQUARE_SIDE, 8*Constants.SQUARE_SIDE, Color.RED);
		_bGhost = new Ghost(this, _map, 11*Constants.SQUARE_SIDE, 10*Constants.SQUARE_SIDE, Color.AQUA);
		_oGhost = new Ghost(this, _map, 12*Constants.SQUARE_SIDE, 10*Constants.SQUARE_SIDE, Color.ORANGE);
		_pGhost = new Ghost(this, _map, 10*Constants.SQUARE_SIDE, 10*Constants.SQUARE_SIDE, Color.DEEPPINK);
		
		_map[8][11].getCollidables().add(_rGhost);
		_map[10][11].getCollidables().add(_bGhost);
		_map[10][12].getCollidables().add(_oGhost);
		_map[10][10].getCollidables().add(_pGhost);

		_rGhost.getNode().toFront();
		_bGhost.getNode().toFront();
		_oGhost.getNode().toFront();
		_pGhost.getNode().toFront();
		
		_gamePane.getChildren().addAll(_rGhost.getNode(), _bGhost.getNode(), _oGhost.getNode(), _pGhost.getNode());
	}
	
	
	/**
	 * This method resets pacman to its staring location
	 */
	public void resetPacman() {
		_pacman.resetLocation(_pacmanStartRow, _pacmanStartColumn);
	}
	
	/**
	 * This method populates the array of ghosts. Useful when want to do the same thing to all the ghosts.
	 * @return
	 */
	public Ghost[] getGhosts() {
		Ghost[] ghosts = new Ghost[4];
		ghosts[0] = _rGhost;
		ghosts[1] = _oGhost;
		ghosts[2] = _pGhost;
		ghosts[3] = _bGhost;
		return ghosts;
	}
	
	/**
	 * Decrease number of dots by one
	 */
	public void decreaseDot() {
		_dotNumber --;
	}
	
	
	/**
	 * This method decrease the number of energizers by one 
	 */
	public void decreaseEnergizer() {
		_energizerNumber --;
	}
	
	/**
	 * This accessor method returns the current score
	 * @return
	 */
	public int getScore() {
		return _score;
	}
	
	/**
	 * This accessor method returns the current number of lives
	 * @return
	 */
	public int getLives() {
		return _live;
	}
	
	/**
	 * This method sets the current mode to frightened. 
	 * This method is particularly useful in ghost class's collide method
	 */
	public void setFrightened() {
		_mode = Mode.Frightened;
	}
	
	/**
	 * This method resets time left for frightened
	 */
	public void setFrightenedCount() {
		_frightenedCount = (int)(Constants.FRIGHTENED_TIME/Constants.DURATION);
	}
	
	/**
	 * This method decrease the number of lives by one 
	 */
	public void livesMinus() {
		_live -=1;
	}
	
	/**
	 * This method increases the current score by the passed in integer
	 * @param score
	 */
	public void increaseScore(int score) {
		_score += score;
	}
	
	
	/**
	 * This method add the passed in ghost to the _ghostQueue for ghost pen
	 * @param ghost
	 */
	public void addtoGhostQueue(Ghost ghost) {
		_ghostQueue.addLast(ghost);
	}
	
	
	/**
	 * This method resets the ghost queue
	 */
	public void resetGhostQueue() {
		System.out.println("hit reset");
		int size = _ghostQueue.size();
		if (size > 0) {
			for (int i = 0; i < size; i ++ ) {
			_ghostQueue.remove();
			}
		}
		
		_ghostQueue.addLast(_pGhost);
		_ghostQueue.addLast(_bGhost);
		_ghostQueue.addLast(_oGhost);
	}
	
	/**
	 * This method resets all ghosts' location.
	 * Particularly useful after pacman collide with ghosts in chase/scatter
	 */
	public void resetGhostsLocation() {
		_rGhost.resetLocation(8, 11);
		_pGhost.resetLocation(10, 10);
		_bGhost.resetLocation(10, 11);
		_oGhost.resetLocation(10, 12);
	}
	

	/**
	 * This is accessor method that returns the current mode
	 * @return
	 */
	public Mode getMode() {
		return _mode;
	}
	
	
	/**
	 * This method sets up the timeline.
	 */
	private void setupTimeLine() {
		
		KeyFrame kf = new KeyFrame(Duration.seconds(Constants.DURATION), new TimeHandler()); //incremental changes made each time TimeHandler is called
		_timeline = new Timeline(kf);
		_timeline.setCycleCount(Animation.INDEFINITE);
		_timeline.play();
	}
	
	/**
	 * This method sets up the ghost pen
	 */
	private void setupGhostPen() {
		KeyFrame kf = new KeyFrame(Duration.seconds(Constants.DURATION), new GhostPenHandler()); //incremental changes made each time TimeHandler is called
		_ghostPenTimeline = new Timeline(kf);
		_ghostPenTimeline.setCycleCount(Animation.INDEFINITE);
		_ghostPenTimeline.play();
	}
	
	
	/**
	 * This is the separate timeHandler for ghost pen
	 * @author xshi7
	 *
	 */
	private class GhostPenHandler implements EventHandler<ActionEvent> {
				
		
		/**
		 * This method specifies what occurs at the end of each KeyFrame.
		 * It takes in a parameter event of type ActionEvent
		 */
		@Override
		public void handle(ActionEvent event){
			
			// if its time to release a ghost and there are actually ghosts in the queue
			if (_penCount == 0 && _ghostQueue.size() > 0) {
				System.out.println("QUEUE: " + _ghostQueue);
				System.out.println("SIZE: " + _ghostQueue.size());
				Ghost g = _ghostQueue.removeFirst(); // the ghost first entered the pen get released
				System.out.println("GHOST: " + g);
				g.resetLocation(8, 11);
				_penCount = (int)(Constants.PEN_TIME/Constants.DURATION);
			}
			else {
				_penCount -=1;
			}
		}
	}
	

	/**
	 * This class specifies the incremental changes for the timeline.
	 * It implements the interface EventHandler
	 * @author xshi7
	 *
	 */
	private class TimeHandler implements EventHandler<ActionEvent> {
		
		/**
		 * This method specifies what occurs at the end of each KeyFrame.
		 * It takes in a parameter event of type ActionEvent
		 */
		@Override
		public void handle(ActionEvent event){
			
			this.collide(); 
			this.movePacman();
			_pacman.getNode().toFront();
			this.collide(); 

	
			/**
			 * If at this current timeFrame pacman collides with ghost outside of frightened mode
			 * then this timeFrame is purely dedicated to resetting the ghosts' locations instead
			 * of moving them according to bfs or frightened
			 */
			if (_ghostCollide == false) {
				this.moveGhost();
			}
			else {
				_ghostCollide = false;
			}
			_rGhost.getNode().toFront();
			_oGhost.getNode().toFront();
			_pGhost.getNode().toFront();
			_bGhost.getNode().toFront();
			
			// update label
			_scoreLabel.setText("Score: " + _score);
			_livesLabel.setText("Lives: " + _live);

			this.GameWon();
			this.GameOver();
			
		}
		
		/**
		 * This method models gameOver
		 */
		public void GameOver() {
			
			// if pacman runs out of lives
			if (_live == 0) {
				System.out.println("Game Over!!!!!!!");
				_status.setText("Game Over!");	
				_status.toFront();
				_timeline.stop();
				_ghostPenTimeline.stop();
				//disable keyHandler
				_gamePane.removeEventHandler(KeyEvent.KEY_PRESSED, _keyHandler);
			}
		}
		
		/**
		 * This method models winning the game which happens when all dots and energizers are eaten
		 */
		public void GameWon() {
			
			if(_dotNumber == 0 && _energizerNumber == 0) {
				System.out.println("Game Won!!!!!!!");
				_status.setText("You Win!");
				_status.toFront();
				_timeline.stop();
				_ghostPenTimeline.stop();
				_gamePane.removeEventHandler(KeyEvent.KEY_PRESSED, _keyHandler);
			}
		}
		
		/**
		 * This method moves Pacman in each timeFrame.
		 * Pacman would move differently according to different directions
		 */
		public void movePacman() {
			Direction d = _pacman.getDirection();
			switch(d) {
			case UP:
				_pacman.moveUp();
				break;
			case DOWN:
				_pacman.moveDown();
				break;
			case LEFT:
				_pacman.moveLeft();
				break;
			case RIGHT:
				_pacman.moveRight();
				break;
			default:
				System.out.println("default");
			}
		}
		
		/**
		 * This method make pacman collide with elements inside of a smart square
		 */
		public void collide() {
			Square current = _map[_pacman.getRow()][_pacman.getColumn()];
			int al = current.getCollidables().size();
			
			//if there are elements inside of the square
			if (al > 0) {
				
				// for each square, collide pacamn with each of the elements inside of the square
				while(current.getCollidables().size() > 0) {
					Collidable a = current.getCollidables().get(0);
					a.collide();
					_score += a.getScore();
					current.getCollidables().remove(0);
				}
			}
		}
		
		/**
		 * This method moves the ghost in each timeFrame according to the current mode
		 */
		public void moveGhost() {
			switch (_mode) {
			case Chase:
				this.chase();
				break;
			case Scatter:
				this.scatter();
				break;
			case Frightened:
				this.frightened();
				break;
			}
			
		}
		
		
		/**
		 * This method models the chase mode
		 */
		public void chase() {
			
			// if chase mode did not run out of time
			if (_chaseCount > 0 ){
				
				// each ghost move according to bfs
				_rGhost.move(_rGhost.bfs(new BoardCoordinate(_pacman.getRow(), _pacman.getColumn(), true)));
				_oGhost.move(_oGhost.bfs(new BoardCoordinate(_pacman.getRow() - 4, _pacman.getColumn(), true)));
				_bGhost.move(_bGhost.bfs(new BoardCoordinate(_pacman.getRow(), _pacman.getColumn() + 2, true)));
				_pGhost.move(_pGhost.bfs(new BoardCoordinate(_pacman.getRow() + 1, _pacman.getColumn() - 3, true)));
				_chaseCount -=1;
			}
			
			// if the chase mode runs out of time, scatter mode begins
			else if (_chaseCount == 0){
				
				_mode = Mode.Scatter;
				_scatterCount = (int)(Constants.SCATTER_TIME/Constants.DURATION);
			}
			
		}
		
		/**
		 * This method models scatter mode 
		 */
		public void scatter() {
			
			// if the scatter mode did not run out of time
			if (_scatterCount > 0) {
				
				// each ghost move according to bfs with their separate set targets
				_rGhost.move(_rGhost.bfs(new BoardCoordinate(Constants.R_SCATTER_ROW, Constants.R_SCATTER_COLUMN, true)));
				_oGhost.move(_oGhost.bfs(new BoardCoordinate(Constants.O_SCATTER_ROW, Constants.O_SCATTER_COLUMN, true)));
				_bGhost.move(_bGhost.bfs(new BoardCoordinate(Constants.B_SCATTER_ROW, Constants.B_SCATTER_COLUMN, true)));
				_pGhost.move(_pGhost.bfs(new BoardCoordinate(Constants.P_SCATTER_ROW, Constants.P_SCATTER_COLUMN, true)));
				_scatterCount -=1;				
			}
			
			// if no time left,  mode switch to chase
			else if (_scatterCount == 0){
				_mode = Mode.Chase;
				_chaseCount = (int)(Constants.CHASE_TIME/Constants.DURATION);
			}
		}
		
		
		/**
		 * This method models frightened mode 
		 */
		public void frightened() {
			
			// if this mode did not run out of time
			if (_frightenedCount > 0) {
				
				_rGhost.move(_rGhost.frighten());
				_oGhost.move(_oGhost.frighten());
				_pGhost.move(_pGhost.frighten());
				_bGhost.move(_bGhost.frighten());
				System.out.println("frighten");
				_frightenedCount -= 1;
			}
			
			// after frightened mode runs out of time, mode is set to chase
			else if (_frightenedCount == 0) {
				_mode = Mode.Chase;
				_chaseCount = (int)(Constants.CHASE_TIME/Constants.DURATION);
				Ghost[] ghosts = Game.this.getGhosts();
				for (int i = 0; i < 4; i ++ ) {
					ghosts[i].resumeColor();
				}
			}			
		}		
	}
	
	
	
	
	
	
	/**
	 * This class specifies what happens when different keys are pressed.
	 * This class implements the interface EventHandler and includes a handle method.
	 * @author xshi7
	 *
	 */
	private class KeyHandler implements EventHandler<KeyEvent> {
		
		/**
		 * This method specifies what happens when "left", "right", "up", "down" key on the keyboard are pressed.
		 * This method takes in a parameter e of type KeyEvent.
		 */
		@Override
		public void handle (KeyEvent e){
			
			// keyPressed stores which key is pressed 
			KeyCode keyPressed = e.getCode();
			
			switch (keyPressed){
			case DOWN:
				_pacman.setDirection(Direction.DOWN);
				break;
			case UP:
				_pacman.setDirection(Direction.UP);
				break;
			case LEFT:
				_pacman.setDirection(Direction.LEFT);
				break;
			case RIGHT:
				_pacman.setDirection(Direction.RIGHT);
				break;
			case SPACE:
				_timeline.pause();
				break;
				
			}
			e.consume();
		}
	}

}
