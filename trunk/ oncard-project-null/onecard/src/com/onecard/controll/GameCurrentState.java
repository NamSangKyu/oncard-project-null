package com.onecard.controll;

import java.util.ArrayList;

/**
 * 게임 상태 정보 저장하는 클래스
 * @author user
 *
 */
public class GameCurrentState {
	//바닥에 깔려 있는 패 or 낸 패
	private String groundCard;
	//플레이어 정보들 0번이 플레이어 나머지는 AI
	private ArrayList<Player> playerList;
	//턴 방향
	private boolean turn;
	//현재 턴인 플레이어 번호 == 리스트 인덱스와 동일
	private int currentTurn;
	//먹는 패
	private String inOutCard;
	public GameCurrentState(String groundCard, ArrayList<Player> playerList,
			boolean turn, int currentTurn, String inOutCard) {
		super();
		this.groundCard = groundCard;
		this.playerList = playerList;
		this.turn = turn;
		this.currentTurn = currentTurn;
		this.inOutCard = inOutCard;
	}
	public String getGroundCard() {
		return groundCard;
	}
	public void setGroundCard(String groundCard) {
		this.groundCard = groundCard;
	}
	public ArrayList<Player> getPlayerList() {
		return playerList;
	}
	public void setPlayerList(ArrayList<Player> playerList) {
		this.playerList = playerList;
	}
	public boolean isTurn() {
		return turn;
	}
	public void setTurn(boolean turn) {
		this.turn = turn;
	}
	public int getCurrentTurn() {
		return currentTurn;
	}
	public void setCurrentTurn(int currentTurn) {
		this.currentTurn = currentTurn;
	}
	public String getInOutCard() {
		return inOutCard;
	}
	public void setInOutCard(String inOutCard) {
		this.inOutCard = inOutCard;
	}
	
	@Override
	public String toString() {
		return "GameCurrentState [groundCard=" + groundCard + ", playerList="
				+ playerList + ", turn=" + turn + ", currentTurn="
				+ currentTurn + ", inOutCard=" + inOutCard + "]";
	}
	
	
	
	
	
}
