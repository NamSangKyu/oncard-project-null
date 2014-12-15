package com.onecard.controll;

import java.util.ArrayList;

import com.onecard.gameinterface.AI;

public class Player {
	//인공지능
	private AI ai;
	//플레이어 이름
	private String name;
	//플레이어 연승 횟수
	private int win;
	//플레이어 덱
	private ArrayList<String> dec;
	public Player(AI ai, String name, int win, ArrayList<String> dec) {
		super();
		this.name = name;
		this.win = win;
		this.dec = dec;
		if(ai != null){
			this.ai = ai;
		}
	}
	public AI getAi() {
		return ai;
	}
	public void setAi(AI ai) {
		this.ai = ai;
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
	
	public void playAI(String groundCard,String item, String state){
		ai.play(groundCard, dec, item, state);
	}
	@Override
	public String toString() {
		return "Player [name=" + name + ", win=" + win + ", dec=" + dec + "]";
	}
	
	
}
