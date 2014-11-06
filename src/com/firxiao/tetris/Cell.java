package com.firxiao.tetris;

import java.awt.image.BufferedImage;

public class Cell {
	private int row; // 格子的行
	private int col; // 格子的列
	private BufferedImage image;

	public Cell(int row, int col, BufferedImage image) {
		super();
		this.row = row;
		this.col = col;
		this.image = image;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public void setCol(int col) {
		this.col = col;
	}
	public int getCol() {
		return col;
	}



	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public void drop() {
		row++;
	}

	public void moveRight() {
		col++;
	}

	public void moveLeft() {
		col--;

	}

	@Override
	// 重写
	public String toString() {
		return row + "," + col;
	}
}
