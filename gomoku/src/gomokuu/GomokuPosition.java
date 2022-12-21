package gomokuu;

import java.io.Serializable;

public class GomokuPosition extends Position implements Serializable{
	public final static short BLANK = 0;
	public final static short PROGRAM = 1;
	public final static short HUMAN = -1;
	
	public final static int size_board = 19;
	
	int[][] board = new int[size_board][size_board];
	
	public GomokuPosition() {
		for(int i = 0; i < size_board; i++)
			for(int j = 0; j < size_board; j++)
				board[i][j] = BLANK;
	}
	
	public void DefaultState() {
		for(int i = 0; i < size_board; i++)
			for(int j = 0; j < size_board; j++)
				board[i][j] = BLANK;
	}
	
	public GomokuPosition getNewPosition() {
		GomokuPosition pos = new GomokuPosition();
		for(int i = 0; i < size_board; i++)
			for(int j = 0; j < pos.size_board; j++)
				pos.board[i][j] = board[i][j];
		return pos;
	}
}
