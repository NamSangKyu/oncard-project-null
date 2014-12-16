package com.onecard.controll;

import java.util.ArrayList;

/**
 * 게임 상태 정보 저장하는 클래스
 * @author user
 *
 */
/**
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
	private String inputCard;
	//공격해서 누적된 카드 수
	private int attackCard;
	public GameCurrentState(String groundCard, ArrayList<Player> playerList,
			boolean turn, int currentTurn, String inputCard, int attackCard) {
		super();
		this.groundCard = groundCard;
		this.playerList = playerList;
		this.turn = turn;
		this.currentTurn = currentTurn;
		this.inputCard = inputCard;
		this.attackCard = attackCard;
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
	public String getInputCard() {
		return inputCard;
	}
	public void setInputCard(String inputCard) {
		this.inputCard = inputCard;
	}
	public int getAttackCard() {
		return attackCard;
	}
	public void setAttackCard(int attackCard) {
		this.attackCard = attackCard;
	}
	@Override
	public String toString() {
		return "GameCurrentState [groundCard=" + groundCard + ", playerList="
				+ playerList + ", turn=" + turn + ", currentTurn="
				+ currentTurn + ", inputCard=" + inputCard + ", attackCard="
				+ attackCard + "]";
	}
	
	
	
}
