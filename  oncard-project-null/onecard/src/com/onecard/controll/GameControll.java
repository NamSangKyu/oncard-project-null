package com.onecard.controll;

import java.util.ArrayList;

import com.onecard.gameinterface.AI;
import com.onecard.gameinterface.CardSuffle;

public class GameControll {
	public static final int GAME_START = 0;
	public static final int GAME_RUN= 1;
	public static final int GAME_OVER = 2;
	
	//게임 컨트롤러
	private static GameControll instance = new GameControll();
	//턴 방향 false : left, true : right
	private boolean turn;
	//플레이어 턴 min : 1, max : 3
	private int playerTurn;
	//각 플레이어 객체
	private ArrayList<Player> playerList;
	//게임 상태 : 게임 시작 == 0, 게임 진행 == 1, 게임 오버 == 2 
	private boolean gameState;
	//카드 셔플 객체
	private CardSuffle cardSuffle;
	//게임 AI
	private AI ai;
	private GameControll() {
		super();
		// TODO Auto-generated constructor stub
	}

	public static GameControll getInstance() {
		if(instance == null)
			instance = new GameControll();
		return instance;
	}
    /**
     * 게임 시작 부분
     */
	public void start() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 다음 턴으로 이동
	 */
	public GameCurrentState nextTurn(){
		if(turn){
			playerTurn++;
		}else{
			playerTurn--;
		}
		
		if(playerTurn < 0)
			playerTurn = playerList.size() -1;
		if(playerTurn >= playerList.size())
			playerTurn = 0;
		
		//현재 상태 만들어 주는 부분
		return null;
	}
	
	/**
	 * 턴 방향 바뀌는 부분
	 */
	public void changeTurn(){
		turn = !turn;
		nextTurn();
	}
	
	/**
	 *카드 내기 및 먹는 부분 
	 * @param state true 먹기, false 내기
	 */
	public GameCurrentState cardInputOutput(boolean state){
		
		
		return null;
	}
	
	/**
	 * 카드 선택 부분
	 * @param state true : right, false : left
	 */
	public GameCurrentState cardSelect(boolean state){
		return null;
	}
	/**
	 * AI 활동 호출하는 메서드
	 * @param groundCard
	 * @param playerDec
	 * @param item
	 * @param state
	 */
	public GameCurrentState playAI(String groundCard,ArrayList<ArrayList<String>> playerDec, String item, String state){
		ai.play(groundCard, playerDec, item, state);
		return null;
	}

	public boolean isTurn() {
		return turn;
	}

	public void setTurn(boolean turn) {
		this.turn = turn;
	}

	public int getPlayerTurn() {
		return playerTurn;
	}

	public void setPlayerTurn(int playerTurn) {
		this.playerTurn = playerTurn;
	}

	public ArrayList<Player> getPlayerList() {
		return playerList;
	}

	public void setPlayerList(ArrayList<Player> playerList) {
		this.playerList = playerList;
	}

	public boolean isGameState() {
		return gameState;
	}

	public void setGameState(boolean gameState) {
		this.gameState = gameState;
	}

	public CardSuffle getCardSuffle() {
		return cardSuffle;
	}

	public void setCardSuffle(CardSuffle cardSuffle) {
		this.cardSuffle = cardSuffle;
	}

	public AI getAi() {
		return ai;
	}

	public void setAi(AI ai) {
		this.ai = ai;
	}
	
	
	
	
}
