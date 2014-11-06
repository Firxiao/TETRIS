package com.firxiao.tetris;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.text.StyledEditorKit.BoldAction;



public class Tetris extends JPanel {
	private int score;// 分数
	private int lines;// 销毁的行数
	private Cell[][] wall;// 背景墙
	private Tetromino tetromino;// 正在下落的四格方块
	private Tetromino nextOne;// 下一个四格方块

	private static BufferedImage background;
	private static BufferedImage overImage;
	public static BufferedImage T;
	public static BufferedImage I;
	public static BufferedImage S;
	public static BufferedImage O;
	public static BufferedImage L;
	public static BufferedImage Z;
	public static BufferedImage J;

	static {
		try {
			background = ImageIO.read(Tetris.class.getResource("tetris.png"));
			overImage = ImageIO.read(Tetris.class.getResource("GAMEOVER.png"));
			T = ImageIO.read(Tetris.class.getResource("T.png"));
			I = ImageIO.read(Tetris.class.getResource("I.png"));
			S = ImageIO.read(Tetris.class.getResource("S.png"));
			O = ImageIO.read(Tetris.class.getResource("O.png"));
			J = ImageIO.read(Tetris.class.getResource("J.png"));
			Z = ImageIO.read(Tetris.class.getResource("Z.png"));
			L = ImageIO.read(Tetris.class.getResource("L.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final int ROWS = 20; // 背景墙行数
	public static final int COLS = 10; // 背景墙列数

	@Override
	public void paint(Graphics g) {
		g.drawImage(background, 0, 0, null);
		g.translate(15, 15);
		paintWall(g);
		paintTeromino(g);
		paintNextOne(g);
		painScore(g); // 绘制分数
		if (gameOver) {
			g.drawImage(overImage, 80, 200, null);
		}
	}

	private void paintWall(Graphics g) {
		for (int row = 0; row < wall.length; row++) {
			Cell[] line = wall[row];
			for (int col = 0; col < line.length; col++) {
				Cell cell = line[col];
				int x = col * CELL_SIZE;
				int y = row * CELL_SIZE;
				if (cell == null) {
					g.drawRect(x, y, CELL_SIZE, CELL_SIZE);

				} else {
					g.drawImage(cell.getImage(), x - 1, y - 1, null);
				}
//				 g.drawString(row+","+col,x,y+CELL_SIZE);
			}
		}

	}

//	public static final int FONT_COLOR = 0x667799;
	public static final int FONT_COLOR = 0x000000;
	public static final int FONT_SIZE = 35;

	private void painScore(Graphics g) {
		int x = 290;
		int y = 160;
		g.setColor(new Color(FONT_COLOR));
		Font font = g.getFont();
		font = new Font(font.getName(), font.getStyle(), FONT_SIZE);
		g.setFont(font);
		String str = "分数:" + score;
		g.drawString(str, x, y);
		y += 56;
		str = "行数:" + lines;
		g.drawString(str, x, y);
		
		y += 56;
		str = "[P]暂停";
		if (pause) {
			str = "[C]继续";
		}
		if (gameOver) {
			str = "[S]开始";
		}
		g.drawString(str, x, y);
	}

	private boolean outOfBounds() {
		Cell[] cells = tetromino.cells;
		for (int i = 0; i < cells.length; i++) {
			Cell cell = cells[i];
			int col = cell.getCol();
			if (col < 0 || col >= COLS) {
				return true;
			}
		}
		return false;
	}

	private boolean coincide() {
		Cell[] cells = tetromino.cells;
		for (int i = 0; i < cells.length; i++) {
			Cell cell = cells[i];
			int row = cell.getRow();
			int col = cell.getCol();
			if (row >= 0 && row < ROWS && col >= 0 && col <= COLS
					&& wall[row][col] != null) {
				return true;
			}
		}
		return false;
	}

	private void paintNextOne(Graphics g) {
		if (nextOne == null) {
			return;
		}
		Cell[] cells = nextOne.cells;
		for (int i = 0; i < cells.length; i++) {
			Cell cell = cells[i];
			int x = (cell.getCol() + 10) * CELL_SIZE;
			int y = (cell.getRow() + 1) * CELL_SIZE;
			g.drawImage(cell.getImage(), x - 1, y - 1, null);
		}

	}

	// 右移控制
	public void moveRightAction() {
		tetromino.moveRight();
		if (outOfBounds() || coincide()) {
			tetromino.moveLeft();
		}
	}

	// 左移控制
	public void moveLeftAction() {
		tetromino.moveLeft();
		if (outOfBounds() || coincide()) {
			tetromino.moveRight();
		}
	}

	// 下落控制
	public void softDropAction() {
		if (canDrop()) {
			tetromino.softDrop();
		} else {
			landintoWall();
			destoryLines();
			checkGameOverAction();
			tetromino = nextOne;
			nextOne = Tetromino.randomOne();
		}
	}

	// 硬下落
	public void hardDropAction() {
		while (canDrop()) {
			tetromino.softDrop();
		}
		landintoWall();
		destoryLines();
		checkGameOverAction();
		tetromino = nextOne;
		nextOne = Tetromino.randomOne();
	}
	

	/**
	 * 俄罗斯方块
	 * @param <checkGameOverAction>
	 */
	public void checkGameOverAction(){

		if(wall[0][4]!=null){
			gameOver = true;
			timer.cancel();
		}
	}

	private Timer timer;
	private boolean pause;
	private boolean gameOver;
	private long interval = 600l;

	public void startAction() {
		pause = false;
		gameOver = false;
		score = 0;
		lines = 0;
		clearWall();
		tetromino = Tetromino.randomOne();
		nextOne = Tetromino.randomOne();
		timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				softDropAction();
				repaint();
			}
		}, interval, interval);
	}
/**
 * 清除方块
 */
	private void clearWall() {
		for (Cell[] line : wall) {
			Arrays.fill(line, null);
		}
	}
/**
 * 暂停
 */
	private void pauseAction() {
		timer.cancel();
		pause = true;
	}
/**
 * 继续
 */
   private void continueAction(){
	   timer=new Timer();
	   timer.schedule(new TimerTask(){
		   @Override
		   public void run(){
			   softDropAction();
			   repaint();
		   }
	   }, interval,interval);
	   pause = false;
   }

	
	
	private static int[] scoreTable = { 0, 1, 10, 50, 100 };

	private void destoryLines() {
		int lines = 0;
		for (int row = 0; row < wall.length; row++) {
			if (fullCells(row)) {
				deleteRow(row);
				lines++;
			}
		}
		this.score += scoreTable[lines];
		this.lines += lines;

	}

	private void deleteRow(int row) {
		for (int i = row; i >= 1; i--) {
			System.arraycopy(wall[i - 1], 0, wall[i], 0, COLS);
		}
		Arrays.fill(wall[0], null);
	}

	private boolean fullCells(int row) {
		Cell[] line = wall[row];
		for (Cell cell : line) {
			if (cell == null) {
				return false;
			}
		}
		return true;
	}

	private void landintoWall() {
		Cell[] cells = tetromino.cells;
		for (int i = 0; i < cells.length; i++) {
			Cell cell = cells[i];
			int row = cell.getRow();
			int col = cell.getCol();
			wall[row][col] = cell;
		}
	}

	private boolean canDrop() {
		Cell[] cells = tetromino.cells;
		for (int i = 0; i < cells.length; i++) {
			Cell cell = cells[i];
			int row = cell.getRow();
			if (row == ROWS - 1) {
				return false;
			}
		}
		for (Cell cell : cells) {
			int row = cell.getRow() + 1;
			int col = cell.getCol();
			if (row >= 0 && row < ROWS && col >= 0 && col < COLS
					&& wall[row][col] != null) {
				return false;
			}
		}
		return true;
	}

	public void rotateRightAction() {
		tetromino.rotateRight();
		if (outOfBounds() || coincide()) {
			tetromino.rotateLeft();
		}

	}

	public void action() {
		wall = new Cell[ROWS][COLS];
		startAction();
		// wall[2][2] = new Cell(2, 2, T);
		tetromino = Tetromino.randomOne();
		nextOne = Tetromino.randomOne();
		KeyAdapter I = new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_Q) {
					System.exit(0);
				}
				if (gameOver) {
					if (key == KeyEvent.VK_S) {
						startAction();
						repaint();
					}
					return;
				}
				if (pause) {
					if (key == KeyEvent.VK_C) {
						continueAction();
						repaint();
					}
					return;
				}

				switch (key) {
				case KeyEvent.VK_DOWN:
					// tetromino.softDrop();
					softDropAction();
					break;
				case KeyEvent.VK_RIGHT:
					// tetromino.moveRight();
					moveRightAction();
					break;
				case KeyEvent.VK_LEFT:
					// tetromino.moveLeft();
					moveLeftAction();
					break;
				case KeyEvent.VK_SPACE:
					hardDropAction();
					break;
				case KeyEvent.VK_UP:
					rotateRightAction();
					break;
				case KeyEvent.VK_P:
					pauseAction();
					break;
				}
				repaint();
			}
		};

		this.requestFocus();
		this.addKeyListener(I);
	}

	public void paintTeromino(Graphics g) {
		if (tetromino == null) {
			return;

		}
		Cell[] cells = tetromino.cells;
		for (int i = 0; i < cells.length; i++) {
			Cell cell = cells[i];
			int x = cell.getCol() * CELL_SIZE;
			int y = cell.getRow() * CELL_SIZE;
			g.drawImage(cell.getImage(), x - 1, y - 1, null);
		}
	}

	public static final int CELL_SIZE = 26;

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		Tetris tetris = new Tetris();
		tetris.setBackground(new Color(0x0000ff));
		frame.add(tetris);
		frame.setSize(530, 580);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		tetris.action();

	}

}
