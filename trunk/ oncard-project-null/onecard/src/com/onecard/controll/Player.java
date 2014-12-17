package com.onecard.controll;

import java.util.ArrayList;
import java.util.Queue;

import com.onecard.gameinterface.AI;

/**
 * @author user
 *
 */
public class Player {
	// 플레이어 이름
	private String name;
	// 플레이어 연승 횟수
	private int win;
	// 플레이어 덱
	private ArrayList<String> dec;
	// 아이템 리스트
	private ArrayList<String> itemList;
	// 파산여부
	private boolean workout;

	public Player(String name, int win, ArrayList<String> dec,
			ArrayList<String> itemList) {
		super();
		this.name = name;
		this.win = win;
		this.dec = dec;
		this.itemList = itemList;
		this.workout = false;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getWin() {
		return win;
	}

	public void setWin(int win) {
		this.win = win;
	}

	public ArrayList<String> getDec() {
		return dec;
	}

	public void setDec(ArrayList<String> dec) {
		this.dec = dec;
	}

	public ArrayList<String> getItemList() {
		return itemList;
	}

	public void setItemList(ArrayList<String> itemList) {
		this.itemList = itemList;
	}

	public boolean isWorkout() {
		return workout;
	}

	public void setWorkout(boolean workout) {
		this.workout = workout;
	}

	@Override
	public String toString() {
		return "Player [name=" + name + ", win=" + win + ", dec=" + dec
				+ ", itemList=" + itemList + ", workout=" + workout + "]";
	}

	
}
