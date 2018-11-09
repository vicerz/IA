package retoatasco;

import retoatasco.coordinate.*;

/**
 * @author Carlos Moreno Morera
 * @author Pablo Mart�n Huertas
 */

public class RetoAtascoBoard {
	public final int FORWARD_INDEX = 0;
	public final int BACKWARD_INDEX = 1;
	public final int CAR_SIZE = 2;
	public final int LORRY_SIZE = 3;
	
	protected static Square[] board;
	protected static Coordinate exit;
	protected static Coordinate[] vehicles;
	protected static int numRows;
	protected static int numColumns;
	
	/*
	 * Entiendo que si queremos delegar la construcci�n en sus subclases se debe dejar
	 * vac�o este constructor.
	 */
	public RetoAtascoBoard() {
		 
	}
	
	
	public RetoAtascoBoard(Square[] b, Coordinate c, int nRows, int nColumns,
			int numVehicles) {
		board = new Square[b.length];
		vehicles = new Coordinate[numVehicles];
		for (int i= 0; i < b.length; ++i) {
			board[i].setId(b[i].getId());
			board[i].setPiece(b[i].getPiece());
			if (!b[i].isEmpty()) {
				vehicles[b[i].getId()].setRow(getRowCoord(i));
				vehicles[b[i].getId()].setColumn(getColCoord(i));
			}
		}
		exit = new Coordinate(c);
	}
	
	public RetoAtascoBoard(RetoAtascoBoard copyBoard) {
		this(copyBoard.getBoard(), copyBoard.getExit(), copyBoard.getNumRows(),
				copyBoard.getNumColumns(), copyBoard.getNumVehicles());
	}
	
	public Square[] getBoard() {
		return board;
	}
	
	public Coordinate getExit() {
		return exit;
	}
	
	public int getNumRows() {
		return numRows;
	}

	public int getNumColumns() {
		return numColumns;
	}
	
	public int getNumVehicles() {
		return vehicles.length;
	}
	
	public Square getValueAt(Coordinate c) {
		return getValueAt(c.getRow(), c.getColumn());
	}

	public Coordinate getLocationOf(int id) {
		return vehicles[id];
	}
	
	public boolean isEmpty(Coordinate c) {
		return getValueAt(c).isEmpty();
	}
	
	public Coordinate getRedCarPosition() {
		return vehicles[0];
	}
	
	public void moveVehicleForward(int id) {
		Coordinate c = getLocationOf(id);
		Vehicle v = new Vehicle(this, c);
		if (v.canMoveForward(this)) {
			setEmpty(v.getLastCoordinate());
			setValue(v.getFirstCoordinate().applyMovement(v.getDirectionForward()), 
					v.getProperties());
			vehicles[id] = v.getFirstCoordinate();
		}
	}
	
	public void moveVehicleBackwards(int id) {
		Coordinate c = getLocationOf(id);
		Vehicle v = new Vehicle(this, c);
		if (v.canMoveBackwards(this)) {
			setEmpty(v.getFirstCoordinate());
			setValue(v.getLastCoordinate().applyMovement(v.getDirectionBackwards()),
					v.getProperties());
			vehicles[id] = v.getLastCoordinate();
		}
	}
	
	public boolean[] canMoveVehicle(int id){
		boolean[] movement = new boolean[2];
		Coordinate c = getLocationOf(id);
		Vehicle v = new Vehicle(this, c);
		movement[FORWARD_INDEX] = v.canMoveForward(this);
		movement[BACKWARD_INDEX] = v.canMoveBackwards(this);
		return movement;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o != null && getClass() == o.getClass()) {
			RetoAtascoBoard aBoard = (RetoAtascoBoard) o;
			if (aBoard.getNumRows() != numRows || aBoard.getNumColumns() != numColumns
					|| aBoard.getExit().getRow() != exit.getRow() 
					|| aBoard.getExit().getColumn() != exit.getColumn())
				return false;
			Square[] b = aBoard.getBoard();
			for (int i = 0; i < numRows*numColumns; i++) {
				if (board[i].getId() != b[i].getId() || board[i].getPiece() != b[i].getPiece())
					return false;
			}
			return true;
		}
		return false;
	}
/*
	@Override
	public int hashCode() {
		int result = 17;
		for (int i = 0; i < 8; i++) {
			int position = this.getPositionOf(i);
			result = 37 * result + position;
		}
		return result;
	}

	@Override
	public String toString() {
		return state[0] + " " + state[1] + " " + state[2] + "\n"
				+ state[3] + " " + state[4] + " " + state[5] + " " + "\n"
				+ state[6] + " " + state[7] + " " + state[8];
	}
*/

	protected void setValue(Coordinate c, Square s) {
		int absPos = getAbsPosition(c.getRow(), c.getColumn());
		board[absPos].setPiece(s.getPiece());
		board[absPos].setId(s.getId());
	}
	
	//
	// PRIVATE METHODS
	//
	
	private int getRowCoord(int absPos) {
		return absPos / numRows;
	}

	private int getColCoord(int absPos) {
		return absPos % numRows;
	}

	private int getAbsPosition(int x, int y) {
		return x * numRows + y;
	}

	private Square getValueAt(int x, int y) {
		return board[getAbsPosition(x, y)];
	}
	
	private void setEmpty(Coordinate c) {
		int absPos = getAbsPosition(c.getRow(), c.getColumn());
		board[absPos].setEmpty();
	}
}