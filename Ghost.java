package Pacman;

import java.util.ArrayList;
import java.util.LinkedList;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 * This class models a ghost and its behaviors.
 * @author
 *
 */
public class Ghost implements Collidable{
	
	private Rectangle _rectangle;
	private double _lx; // x-location
	private double _ly; // y-location
	private Game _game;
	private Square[][] _map;
	private Direction[][] _directionMap; 
	private Color _color;
	
	private Direction _direction;
	private BoardCoordinate _ghoLoc; // location of ghost as a BoardCoordinate object
	
	
	/**
	 * This is the constructor for ghost class.
	 * It takes in a parameter of Game which creates association with game class.
	 * It takes a parameter of type 2-D array that represents the map.
	 * It takes in two parameters of type double that represents the graphical location of the ghost.
	 * It also takes in the color parameter
	 * This method sets up the ghost.
	 * @param game
	 * @param map
	 * @param x
	 * @param y
	 * @param col
	 */
	public Ghost (Game game, Square[][] map, double x, double y, Color col) {
		_game = game;
		_color = col;
		_directionMap = new Direction[23][23]; 
		_direction = Direction.UP; // default direction of the ghost
		_map = map;
		_rectangle = new Rectangle(Constants.SQUARE_SIDE, Constants.SQUARE_SIDE, col);
		this.setX(x);
		this.setY(y);
		_ghoLoc = new BoardCoordinate((int)(y/Constants.SQUARE_SIDE), (int)(x/Constants.SQUARE_SIDE), false);
	}
	
	/**
	 * This is an accessor method. 
	 * It allows access of the node of the circle
	 * it returns the node
	 * @return
	 */
	public Rectangle getNode() {
		return _rectangle;
	}
	
	
	/**
	 * This is a mutator method
	 * It sets x-location of the ghost
	 * @param x
	 */
	public void setX (double x) {
		_lx = x;
		_rectangle.setX(x);
	}
	
	/**
	 * This is a mutator method
	 * It sets y-location of the ghost
	 * @param y
	 */
	public void setY (double y) {
		_ly = y;
		_rectangle.setY(y);
	}
	

	/**
	 * This is an accessor method
	 * It allows access to the y-location of the ghost
	 * @return
	 */
	public double getY () {
		return _ly;
	}
	
	/**
	 * This is an accessor method
	 * It allows access to the x-location of the ghost
	 * @return
	 */
	public double getX () {
		return _lx;
	}
	
	/**
	 * This is an accessor method that returns the direction of the ghost
	 * @return
	 */
	public Direction getGhostDirection() {
		return _direction;
	}
	
	/**
	 * This method changes the color of the ghost to the passed in color
	 * @param col
	 */
	public void changeColor(Color col) {
		_rectangle.setFill(col);
	}
	
	/**
	 * This method resumes the ghost's original color
	 */
	public void resumeColor() {
		_rectangle.setFill(_color);
	}
	
	
	/**
	 * This method is used when the ghost's location is reset.
	 * The passed in integers represent the logical location that the ghost is reset to
	 * @param r
	 * @param c
	 */
	public void resetLocation(int r, int c) {
		_ghoLoc = new BoardCoordinate(r, c, false);
		_map[_ghoLoc.getRow()][ _ghoLoc.getColumn()].getCollidables().add(this);
		this.setX(Constants.SQUARE_SIDE * c );
		this.setY(Constants.SQUARE_SIDE * r );
		_direction = Direction.UP;
	}
	
	/**
	 * This method models what happens when Pacman collides with ghost.
	 */
	public void collide() {
		
		// in chase and scatter mode
		if (_game.getMode() != Mode.Frightened) {
			_game.livesMinus();
			_game.resetPacman();
			_game.resetGhostsLocation();
			_game.resetGhostQueue();
			_game.resetPenCount();
			_game.setGhostCollide(true);
		}
		
		// in frightened mode
		else {
			this.setX(11*Constants.SQUARE_SIDE);
			this.setY(10* Constants.SQUARE_SIDE);
			_ghoLoc = new BoardCoordinate(10, 11, false);
			_game.increaseScore(200);
			_game.addtoGhostQueue(this);
		}
		
		
	}
	

	/**
	 * This method moves the ghost according to a given direction
	 * @param direction
	 */
	public void move(Direction direction) {
		switch (direction) {
		case LEFT:
			// wrapping
			if (_ghoLoc.getColumn() == 0) {
				this.setX(22*Constants.SQUARE_SIDE); // graphically move
				_map[_ghoLoc.getRow()][ _ghoLoc.getColumn()].getCollidables().remove(this); // logically remove from collidables of the current square
				_ghoLoc = new BoardCoordinate(_ghoLoc.getRow(), 22, false); // logically move
				_map[_ghoLoc.getRow()][ _ghoLoc.getColumn()].getCollidables().add(this); // logically add to collidables
			}
			
			// other cases
			if (this.canMoveLeft()) {
				_direction = Direction.LEFT;
				this.setX(this.getX() - Constants.SQUARE_SIDE);
				_map[_ghoLoc.getRow()][ _ghoLoc.getColumn()].getCollidables().remove(this);
				_ghoLoc = new BoardCoordinate(_ghoLoc.getRow(), _ghoLoc.getColumn() - 1, false);
				_map[_ghoLoc.getRow()][ _ghoLoc.getColumn()].getCollidables().add(this);
			}
			break;
		case RIGHT:
			
			// wrapping
			if (_ghoLoc.getColumn() == 22) {
				this.setX(0);
				_map[_ghoLoc.getRow()][ _ghoLoc.getColumn()].getCollidables().remove(this);
				_ghoLoc = new BoardCoordinate(_ghoLoc.getRow(), 0, false);
				_map[_ghoLoc.getRow()][ _ghoLoc.getColumn()].getCollidables().add(this);

			}
			if (this.canMoveRight()) {
				_direction = Direction.RIGHT;
				this.setX(this.getX() + Constants.SQUARE_SIDE);
				_map[_ghoLoc.getRow()][ _ghoLoc.getColumn()].getCollidables().remove(this);
				_ghoLoc = new BoardCoordinate(_ghoLoc.getRow(), _ghoLoc.getColumn() + 1, false);
				_map[_ghoLoc.getRow()][ _ghoLoc.getColumn()].getCollidables().add(this);
				
			}
			break;
		case UP:
			if (this.canMoveUp()) {
				_direction = Direction.UP;
				this.setY(this.getY() - Constants.SQUARE_SIDE);
				_map[_ghoLoc.getRow()][ _ghoLoc.getColumn()].getCollidables().remove(this);	
				_ghoLoc = new BoardCoordinate(_ghoLoc.getRow() - 1, _ghoLoc.getColumn(), false);
				_map[_ghoLoc.getRow()][ _ghoLoc.getColumn()].getCollidables().add(this);
				

			}
			break;
		
		case DOWN:
			if (this.canMoveDown()) {
				_direction = Direction.DOWN;
				this.setY(this.getY() + Constants.SQUARE_SIDE);
				_map[_ghoLoc.getRow()][ _ghoLoc.getColumn()].getCollidables().remove(this);
				_ghoLoc = new BoardCoordinate(_ghoLoc.getRow() + 1, _ghoLoc.getColumn(), false);
				_map[_ghoLoc.getRow()][ _ghoLoc.getColumn()].getCollidables().add(this);
				
			}
			break;
		}	
	}
	
	
	/**
	 * This method checks if the left move is valid for the ghost or not
	 * @return
	 */
	public boolean canMoveLeft() {
		
		// move is not valid if the ghost was previously moving right (ghost cannot turn 180 degree)
		if (_direction == Direction.RIGHT) {
			return false;
		}	
		
		// if the ghost is not moving into a wall
		if (_map[_ghoLoc.getRow()][_ghoLoc.getColumn()-1].isWall() == true){
			return false;
		}
		return true;
	}
	
	/**
	 * This method checks if the right move is valid for the ghost or not
	 * @return
	 */
	public boolean canMoveRight() {
		if (_direction == Direction.LEFT) {
			return false;
		}
		if (_map[_ghoLoc.getRow()][_ghoLoc.getColumn()+1].isWall() == true){
			return false;
		}
		return true;
	}
	
	/**
	 * This method checks if the up move is valid for the ghost or not
	 * @return
	 */
	public boolean canMoveUp() {
		if (_direction == Direction.DOWN) {
			return false;
		}
					
		if (_map[_ghoLoc.getRow() - 1 ][_ghoLoc.getColumn()].isWall() == true){
			return false;
		}
		return true;
	}
	
	/**
	 * This method checks if the down move is valid for the ghost or not
	 * @return
	 */
	public boolean canMoveDown() {
		if (_direction == Direction.UP) {
			return false;
		}

		if (_map[_ghoLoc.getRow() + 1 ][_ghoLoc.getColumn()].isWall() == true){
			return false;
		}
		return true;
	}
	
	
	/**
	 * Breadth-First Search method allows the ghost to chase a target.
	 * @param target
	 * @return
	 */
	public Direction bfs(BoardCoordinate target) {
		
		// first store all the possible directions the ghost can go in an arrayList
		ArrayList<Direction> ds = new ArrayList<Direction>();
		if (!_map[this.toLeft(_ghoLoc).getRow()][this.toLeft(_ghoLoc).getColumn()].isWall() && _direction != Direction.RIGHT) {
			ds.add(Direction.LEFT);
		}
		if (!_map[this.toRight(_ghoLoc).getRow()][this.toRight(_ghoLoc).getColumn()].isWall() && _direction != Direction.LEFT) {
			ds.add(Direction.RIGHT);
		}
		if (!_map[_ghoLoc.getRow()-1][_ghoLoc.getColumn()].isWall() && _direction != Direction.DOWN) {
			ds.add(Direction.UP);
		}
		if (!_map[_ghoLoc.getRow()+1][_ghoLoc.getColumn()].isWall() && _direction != Direction.UP) {
			ds.add(Direction.DOWN);
		}
		
		// if there is only one direction to go, go that direction 
		if (ds.size() == 1){
			return ds.get(0);
		}
				
		// make a queue of BoardCoordinate objects
		LinkedList<BoardCoordinate> queue = new LinkedList<BoardCoordinate>();
		double minDist = this.distance(_ghoLoc, target); // store the minimum distance to the target
		BoardCoordinate min = _ghoLoc; // the Boardcoordinate that has the minimum distance
		
		// first store the current ghost location 
		_directionMap[_ghoLoc.getRow()][_ghoLoc.getColumn()] = _direction;
		
		// check neighbors of ghost location
		
		if (_ghoLoc.getColumn() != 22 && _ghoLoc.getColumn() != 0) {
			
			// if this is a valid neighbor and the neighbored is not added so that the ghost is turning 180 degrees
			if (this.isValidNeighbor(_ghoLoc, new BoardCoordinate(_ghoLoc.getRow(), _ghoLoc.getColumn() - 1, false), _direction) && (this.findRelativeDirection(_ghoLoc, new BoardCoordinate(_ghoLoc.getRow(), _ghoLoc.getColumn() - 1, false)) != _direction.getOpposite())) {
				// add the location to queue
				queue.addLast(new BoardCoordinate(_ghoLoc.getRow(), _ghoLoc.getColumn() - 1, false));
				
				// record location in direction map
				_directionMap [_ghoLoc.getRow()][_ghoLoc.getColumn() - 1] = Direction.LEFT;

				// update minimum distance information if needed
				if (this.distance(target, new BoardCoordinate(_ghoLoc.getRow(), _ghoLoc.getColumn() - 1, false)) < minDist){
					minDist = this.distance(target, new BoardCoordinate(_ghoLoc.getRow(), _ghoLoc.getColumn() - 1, false));
					min = new BoardCoordinate(_ghoLoc.getRow(), _ghoLoc.getColumn() - 1, false);
				}
			}
			if (this.isValidNeighbor(_ghoLoc, new BoardCoordinate(_ghoLoc.getRow(), _ghoLoc.getColumn() + 1, false), _direction) && (this.findRelativeDirection(_ghoLoc, new BoardCoordinate(_ghoLoc.getRow(), _ghoLoc.getColumn()+1, false)) != _direction.getOpposite())) {
				queue.addLast(new BoardCoordinate(_ghoLoc.getRow(), _ghoLoc.getColumn() + 1, false));
				_directionMap [_ghoLoc.getRow()][_ghoLoc.getColumn() + 1] = Direction.RIGHT;
	
				if (this.distance(target, new BoardCoordinate(_ghoLoc.getRow(), _ghoLoc.getColumn() + 1, false)) < minDist){
					minDist = this.distance(target, new BoardCoordinate(_ghoLoc.getRow(), _ghoLoc.getColumn() + 1, false));
					min = new BoardCoordinate(_ghoLoc.getRow(), _ghoLoc.getColumn() + 1, false);
	
				}
			}
			if (this.isValidNeighbor(_ghoLoc, new BoardCoordinate(_ghoLoc.getRow() - 1, _ghoLoc.getColumn(), false), _direction) && (this.findRelativeDirection(_ghoLoc, new BoardCoordinate(_ghoLoc.getRow()-1, _ghoLoc.getColumn(), false)) != _direction.getOpposite())) {
				
				queue.addLast(new BoardCoordinate(_ghoLoc.getRow() - 1, _ghoLoc.getColumn(), false));
				_directionMap [_ghoLoc.getRow()-1][_ghoLoc.getColumn()] = Direction.UP;
	
				if (this.distance(target, new BoardCoordinate(_ghoLoc.getRow() - 1, _ghoLoc.getColumn(), false)) < minDist){
					minDist = this.distance(target, new BoardCoordinate(_ghoLoc.getRow() - 1, _ghoLoc.getColumn(), false));
					min = new BoardCoordinate(_ghoLoc.getRow() - 1, _ghoLoc.getColumn(), false);
					
				}
			}
			if (this.isValidNeighbor(_ghoLoc, new BoardCoordinate(_ghoLoc.getRow() + 1, _ghoLoc.getColumn(), false), _direction) && (this.findRelativeDirection(_ghoLoc, new BoardCoordinate(_ghoLoc.getRow()+1, _ghoLoc.getColumn(), false)) != _direction.getOpposite())) {
				queue.addLast(new BoardCoordinate(_ghoLoc.getRow() + 1, _ghoLoc.getColumn(), false));
				_directionMap [_ghoLoc.getRow()+1][_ghoLoc.getColumn()] = Direction.DOWN;
	
				if (this.distance(target, new BoardCoordinate(_ghoLoc.getRow() + 1, _ghoLoc.getColumn(), false)) < minDist){
					minDist = this.distance(target, new BoardCoordinate(_ghoLoc.getRow() + 1, _ghoLoc.getColumn(), false));
					min = new BoardCoordinate(_ghoLoc.getRow() + 1, _ghoLoc.getColumn(), false);
				}
			}
		}
		
		// wrapping
		
		// when the column of ghost is 22, we know the only valid neighbors are at the same row, column 0 and same row, column 21
		else if (_ghoLoc.getColumn() == 22) {
			
			// if this is valid neighbor and the neighbored is not added so that the ghost is turning 180 degrees
			if (this.isValidNeighbor(_ghoLoc, new BoardCoordinate(_ghoLoc.getRow(), 0, false), _direction) && (this.findRelativeDirection(_ghoLoc, new BoardCoordinate(_ghoLoc.getRow(), 0, false)) != _direction.getOpposite())) {
				// add the location to queue
				queue.addLast(new BoardCoordinate(_ghoLoc.getRow(), 0, false));
				// record location in direction map
				_directionMap [_ghoLoc.getRow()][0] = Direction.RIGHT;
				
				// update minimum distance information if needed
				if (this.distance(target, new BoardCoordinate(_ghoLoc.getRow(), 0, false)) < minDist){
					minDist = this.distance(target, new BoardCoordinate(_ghoLoc.getRow(), 0, false));
					min = new BoardCoordinate(_ghoLoc.getRow(), 0, false);
				}
			}
			if (this.isValidNeighbor(_ghoLoc, new BoardCoordinate(_ghoLoc.getRow(), 21, false), _direction) && (this.findRelativeDirection(_ghoLoc, new BoardCoordinate(_ghoLoc.getRow(), 21, false)) != _direction.getOpposite())) {
				queue.addLast(new BoardCoordinate(_ghoLoc.getRow(), 21, false));
				_directionMap [_ghoLoc.getRow()][21] = Direction.LEFT;
				
				if (this.distance(target, new BoardCoordinate(_ghoLoc.getRow(), 21, false)) < minDist){
					minDist = this.distance(target, new BoardCoordinate(_ghoLoc.getRow(), 21, false));
					min = new BoardCoordinate(_ghoLoc.getRow(), 21, false);
				}
			}
		}
		
		// when the column of ghost is 0, we know the only valid neighbors are at the same row, column 1 and same row, column 22
		else if (_ghoLoc.getColumn() == 0) {
			if (this.isValidNeighbor(_ghoLoc, new BoardCoordinate(_ghoLoc.getRow(), 22, false), _direction) && (this.findRelativeDirection(_ghoLoc, new BoardCoordinate(_ghoLoc.getRow(), 22, false)) != _direction.getOpposite())) {
				queue.addLast(new BoardCoordinate(_ghoLoc.getRow(), 22, false));
				_directionMap [_ghoLoc.getRow()][22] = Direction.LEFT;
				
				if (this.distance(target, new BoardCoordinate(_ghoLoc.getRow(), 22, false)) < minDist){
					minDist = this.distance(target, new BoardCoordinate(_ghoLoc.getRow(), 22, false));
					min = new BoardCoordinate(_ghoLoc.getRow(), 22, false);
				}
			}
			if (this.isValidNeighbor(_ghoLoc, new BoardCoordinate(_ghoLoc.getRow(), 1, false), _direction) && (this.findRelativeDirection(_ghoLoc, new BoardCoordinate(_ghoLoc.getRow(), 1, false)) != _direction.getOpposite())) {
				queue.addLast(new BoardCoordinate(_ghoLoc.getRow(), 1, false));
				_directionMap [_ghoLoc.getRow()][1] = Direction.RIGHT;
				
				if (this.distance(target, new BoardCoordinate(_ghoLoc.getRow(), 1, false)) < minDist){
					minDist = this.distance(target, new BoardCoordinate(_ghoLoc.getRow(), 1, false));
					min = new BoardCoordinate(_ghoLoc.getRow(), 1, false);
				}
			}
		}
		
		// if the queue is not empty
		while (queue.size() != 0) {
			
			// examine the first element of the queue and remove it from queue
			BoardCoordinate current = queue.removeFirst();
			
			// update minimum distance information if needed
			if (this.distance(current, target) < minDist) {
				minDist = this.distance(current, target);
				min = current;
			}
			
			if (current.getColumn() != 22 && current.getColumn() != 0) {
				
				// add all the valid neighbors to the queue and store their directions in _directionMap as the direction of current
				if (this.isValidNeighbor(current, new BoardCoordinate(current.getRow() + 1, current.getColumn(), false), _directionMap[current.getRow()][current.getColumn()])) {
					queue.addLast(new BoardCoordinate(current.getRow() + 1, current.getColumn(), false));
					_directionMap[current.getRow() + 1][current.getColumn()] = _directionMap[current.getRow()][current.getColumn()];
					
				}
				if (this.isValidNeighbor(current, new BoardCoordinate(current.getRow() - 1, current.getColumn(), false), _directionMap[current.getRow()][current.getColumn()])) {
					queue.addLast(new BoardCoordinate(current.getRow() - 1, current.getColumn(), false));
					_directionMap[current.getRow() - 1][current.getColumn()] = _directionMap[current.getRow()][current.getColumn()];
	
				}
				if (this.isValidNeighbor(current, new BoardCoordinate(current.getRow(), current.getColumn() + 1, false), _directionMap[current.getRow()][current.getColumn()])) {
					queue.addLast(new BoardCoordinate(current.getRow(), current.getColumn() + 1, false));
					_directionMap[current.getRow()][current.getColumn() + 1] = _directionMap[current.getRow()][current.getColumn()];
	
				}
				if (this.isValidNeighbor(current, new BoardCoordinate(current.getRow(), current.getColumn() - 1, false), _directionMap[current.getRow()][current.getColumn()])) {
					queue.addLast(new BoardCoordinate(current.getRow(), current.getColumn() - 1, false));
					_directionMap[current.getRow()][current.getColumn() - 1] = _directionMap[current.getRow()][current.getColumn()];	
				}
			}
			
			// wrapping
			else if (current.getColumn() == 22) {
				
				// only valid neighbors are at the same row, column 0 and same row, column 21
				if (_directionMap[current.getRow()][0] == null) {
					queue.addLast(new BoardCoordinate(current.getRow(), 0, false));
					_directionMap[current.getRow()][0] = _directionMap[current.getRow()][22];
				}
				if (_directionMap[current.getRow()][21] == null) {
					queue.addLast(new BoardCoordinate(current.getRow(), 21, false));
					_directionMap[current.getRow()][21] = _directionMap[current.getRow()][22];
				}
			}
			else if (current.getColumn() == 0) {
				
				// only valid neighbors are at the same row, column 1 and same row, column 22
				if (_directionMap[current.getRow()][22] == null) {
					queue.addLast(new BoardCoordinate(current.getRow(), 22, false));
					_directionMap[current.getRow()][22] = _directionMap[current.getRow()][0];
				}
				if (_directionMap[current.getRow()][1] == null) {
					queue.addLast(new BoardCoordinate(current.getRow(), 1, false));
					_directionMap[current.getRow()][1] = _directionMap[current.getRow()][0];
				}
			}
		}

		// we find the direction of the location with least distance to the target
		Direction dir =  _directionMap[min.getRow()][min.getColumn()];
		_directionMap = new Direction[23][23];
		return dir;
		
	}
	
	/**
	 * This method calculates the distance between two locations 
	 * @param a
	 * @param b
	 * @return
	 */
	public double distance(BoardCoordinate a, BoardCoordinate b) {
		return Math.sqrt((a.getRow() - b.getRow())*(a.getRow() - b.getRow()) + (a.getColumn() - b.getColumn())* (a.getColumn() - b.getColumn()));
	}
	
	
	/**
	 * This method checks if "loc" is a valid neighbor of "current"
	 * @param current
	 * @param loc
	 * @param d
	 * @return
	 */
	public boolean isValidNeighbor(BoardCoordinate current, BoardCoordinate loc, Direction d) {
		if (current.getColumn() == 22) {
			if (loc.getColumn() != 0) {
				return false;
			}
		}
		
		// if loc is a wall, it is not valid neighbot
		if (_map[loc.getRow()][loc.getColumn()].isWall()) {
			return false;
		}
		else {
			
			// if loc is already visited in bfs, it is not a valid neighbor
			if (_directionMap[loc.getRow()][loc.getColumn()] != null) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * This method finds the relative direction of loc relative to current
	 * This method accounts for situation when current and loc are next to each other
	 * @param current
	 * @param loc
	 * @return
	 */
	public Direction findRelativeDirection (BoardCoordinate current, BoardCoordinate loc) {
		int r = loc.getRow();
		int c = loc.getColumn();
		int dr = r - current.getRow();
		int dc = c - current.getColumn();
		if (dr < 0) {
			return Direction.UP;
		}
		else if (dr > 0) {
			return Direction.DOWN;
		}
		else if (dc < 0) {
			return Direction.LEFT;
		}
		else if (dc > 0){
			return Direction.RIGHT;
		}
		return null;
	}
	
	/**
	 * This method models the ghost's behavior when it is frightened.
	 * @return
	 */
	public Direction frighten() {
		
		// first store all the possible directions the ghost can go in an arrayList
		ArrayList<Direction> ds = new ArrayList<Direction>();
		if (!_map[this.toLeft(_ghoLoc).getRow()][this.toLeft(_ghoLoc).getColumn()].isWall() && _direction != Direction.RIGHT) {
			ds.add(Direction.LEFT);
		}
		if (!_map[this.toRight(_ghoLoc).getRow()][this.toRight(_ghoLoc).getColumn()].isWall() && _direction != Direction.LEFT) {
			ds.add(Direction.RIGHT);
		}
		if (!_map[_ghoLoc.getRow()-1][_ghoLoc.getColumn()].isWall() && _direction != Direction.DOWN) {
			ds.add(Direction.UP);
		}
		if (!_map[_ghoLoc.getRow()+1][_ghoLoc.getColumn()].isWall() && _direction != Direction.UP) {
			ds.add(Direction.DOWN);
		}
		
		// if there is only one direction to go, go that direction 
		if (ds.size() == 1){
			System.out.println("only one way "+ds.get(0));
			return ds.get(0);
		}
		else {
			// return a random location from all possible directions (in ds)
			int rand = (int) (Math.random() * (ds.size()-1));	 // rand is an integer from 0 to the size of ds-1
			return ds.get(rand);
		}
	}
	
	/**
	 * This method returns the BoardCoordinate that is to the left of s
	 * @param s
	 * @return
	 */
	public BoardCoordinate toLeft(BoardCoordinate s) {
		
		// wrapping
		if (s.getColumn() == 0) {
			return new BoardCoordinate(s.getRow(), 22, false);
		}
		else {
			return new BoardCoordinate(s.getRow(), s.getColumn()-1, false);
		}
	}
	

	/**
	 * This method returns the BoardCoordinate that is to the right of s
	 * @param s
	 * @return
	 */
	public BoardCoordinate toRight(BoardCoordinate s) {
		if (s.getColumn() == 22) {
			return new BoardCoordinate(s.getRow(), 0, false);
		}
		else {
			return new BoardCoordinate(s.getRow(), s.getColumn()+1, false);
		}
	}
	
	
   /**
    * This accessor method returns the score of ghost which is 0
    */
	@Override
	public int getScore() {
		// TODO Auto-generated method stub
		return 0;
	}
	

}
