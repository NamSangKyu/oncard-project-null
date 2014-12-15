package com.onecard.controll;

public class Point {
	private float x;
	private float y;
	private boolean draw;
	public Point(float x, float y, boolean draw) {
		super();
		this.x = x;
		this.y = y;
		this.draw = draw;
	}
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	public boolean isDraw() {
		return draw;
	}
	public void setDraw(boolean draw) {
		this.draw = draw;
	}
	
	
	
}
