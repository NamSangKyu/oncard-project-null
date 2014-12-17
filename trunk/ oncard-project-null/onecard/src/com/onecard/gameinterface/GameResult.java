package com.onecard.gameinterface;

public class GameResult {
	private String name;
	private int score;
	private int win;
	public GameResult(String name) {
		super();
		this.name = name;
	}

	public int getWin() {
		return win;
	}

	public void setWin(int win) {
		this.win = win;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return "GameResult [name=" + name + ", score=" + score + "]";
	}

}
