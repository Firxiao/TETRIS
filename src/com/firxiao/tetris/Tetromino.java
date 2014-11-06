package com.firxiao.tetris;

import java.util.Arrays;
import java.util.Random;

public abstract class Tetromino {
	/**
	 * 工厂方法，随机产生4格方块
	 */
	protected Cell[] cells = new Cell[4];
	
	//旋转状态
	protected State[] states;
	protected int index = 10000;

	protected class State {
		int row0, col0, row1, col1, row2, col2, row3, col3;

		public State(int row0, int col0, int row1, int col1, int row2,
				int col2, int row3, int col3) {
			this.row0 = row0;
			this.col0 = col0;
			this.row1 = row1;
			this.col1 = col1;
			this.row2 = row2;
			this.col2 = col2;
			this.row3 = row3;
			this.col3 = col3;
		}
	}

	
	
	public static Tetromino randomOne() {
		Random random = new Random();
		int type = random.nextInt(7);
		switch (type) {
		case 0:
			return new I();
		case 1:
			return new O();
		case 2:
			return new S();
		case 3:
			return new L();
		case 4:
			return new J();
		case 5:
			return new T();
		case 6:
			return new Z();
		}
		return null;
	}


	//向右旋转

		public void rotateRight() {
			index++;
			State s = states[index % states.length];


			int row = cells[0].getRow();
			int col = cells[0].getCol();
//			System.out.println("s"+index % states.length);
//			System.out.println("row"+row);
//			System.out.println("col"+col);
//			System.out.println(s.row1+","+s.col1+","+s.row2+","+s.col2+","+s.row3+","+s.col3);
		    cells[1].setRow(row + s.row1);
			cells[1].setCol(col + s.col1);
			cells[2].setRow(row + s.row2);
			cells[2].setCol(col + s.col2);
			cells[3].setRow(row + s.row3);
			cells[3].setCol(col + s.col3);

		}
	
	//向左旋转
	
	public void rotateLeft() {
		index--;
		State s = states[index % states.length];
		Cell o = cells[0];
		int row = o.getRow();
		int col = o.getCol();
		cells[1].setRow(row + s.row1);
		cells[1].setCol(col + s.col1);
		cells[2].setRow(row + s.row2);
		cells[2].setCol(col + s.col2);
		cells[3].setRow(row + s.row3);
		cells[3].setCol(col + s.col3);

		

	}
	/*
	 * 方块下落一格子
	 */

	public void softDrop() {
		for (int i = 0; i < cells.length; i++) {
			cells[i].drop();
		}
	}

	/*
	 * 方块左移一格子
	 */
	public void moveLeft() {
		for (int i = 0; i < cells.length; i++) {
			cells[i].moveLeft();
		}
	}

	/*
	 * 方块右移一格子
	 */
	public void moveRight() {
		for (int i = 0; i < cells.length; i++) {
			cells[i].moveRight();
		}

	}

	/*
	 * (non-Javadoc) 覆盖toString方法，显示方块中每个格子的行列信息
	 */
	public String toString() {
		return Arrays.toString(cells);
	}

}

class T extends Tetromino {
	public T() {
		cells[0] = new Cell(0, 4, Tetris.T);
		cells[1] = new Cell(0, 3, Tetris.T);
		cells[2] = new Cell(0, 5, Tetris.T);
		cells[3] = new Cell(1, 4, Tetris.T);
		


		states = new State[4];
		states[0] = new State(0, 0, 0, -1, 0, 1, 1, 0);
		states[1] = new State(0, 0, -1, 0, 1, 0, 0, -1);
		states[2] = new State(0, 0, 0, 1, 0, -1, -1, 0);
		states[3] = new State(0, 0, 1, 0, -1, 0, 0, 1);

	}

}

class I extends Tetromino {
	public I() {
		cells[0] = new Cell(0, 4, Tetris.I);
		cells[1] = new Cell(0, 3, Tetris.I);
		cells[2] = new Cell(0, 5, Tetris.I);
		cells[3] = new Cell(0, 6, Tetris.I);

	
		
		states = new State[] { new State(0, 0, 0, -1, 0, 1, 0, 2),
				new State(0, 0, -1, 0, 1, 0, 2, 0) };
	}
}

class Z extends Tetromino {
	public Z() {
		cells[0] = new Cell(0, 4, Tetris.Z);
		cells[1] = new Cell(0, 5, Tetris.Z);
		cells[2] = new Cell(1, 3, Tetris.Z);
		cells[3] = new Cell(1, 4, Tetris.Z);
		
		

		states = new State[] { new State(0, 0, -1, -1, -1, 0, 0, 1),
				new State(0, 0, -1, 1, 0, 1, 1, 0) };

	}

}

class S extends Tetromino {
	public S() {
		cells[0] = new Cell(0, 4, Tetris.S);
		cells[1] = new Cell(0, 5, Tetris.S);
		cells[2] = new Cell(1, 4, Tetris.S);
		cells[3] = new Cell(1, 3, Tetris.S);

		states = new State[] { new State(0, 0, 0, -1, -1, 0, -1, 1),
				new State(0, 0, -1, 0, 0, 1, 1, 1) };

	}

}

class L extends Tetromino {
	public L() {
		cells[0] = new Cell(0, 4, Tetris.L);
		cells[1] = new Cell(0, 3, Tetris.L);
		cells[2] = new Cell(0, 5, Tetris.L);
		cells[3] = new Cell(1, 3, Tetris.L);
		states = new State[] { new State(0, 0, 0, 1, 0, -1, -1, 1),
				new State(0, 0, 1, 0, -1, 0, 1, 1),
				new State(0, 0, 0, -1, 0, 1, 1, -1),
				new State(0, 0, -1, 0, 1, 0, -1, -1) };

	}
}

class J extends Tetromino {
	public J() {
		cells[0] = new Cell(0, 4, Tetris.J);
		cells[1] = new Cell(0, 3, Tetris.J);
		cells[2] = new Cell(0, 5, Tetris.J);
		cells[3] = new Cell(1, 5, Tetris.J);
		states = new State[] { new State(0, 0, 0, -1, 0, 1, 1, 1),
				new State(0, 0, -1, 0, 1, 0, 1, -1),
				new State(0, 0, 0, 1, 0, -1, -1, -1),
				new State(0, 0, 1, 0, -1, 0, -1, 1) };
	}

}

class O extends Tetromino {
	public O() {
		cells[0] = new Cell(0, 4, Tetris.O);
		cells[1] = new Cell(0, 5, Tetris.O);
		cells[2] = new Cell(1, 4, Tetris.O);
		cells[3] = new Cell(1, 5, Tetris.O);
		states = new State[] { new State(0, 0, 0, 1, 1, 0, 1, 1),
				new State(0, 0, 0, 1, 1, 0, 1, 1) };
	}
}