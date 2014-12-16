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
	//사용자가 받을 무덤 덱
	private ArrayList<String> templateDec;
	//사용자가 낸 카드 무덤 덱
	private ArrayList<String> useDec;
	
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
     * 
     */
	public void start(int playerCount) {
		// TODO Auto-generated method stub
		//전체 카드덱 받아옴
		ArrayList<ArrayList<String>> list = cardSuffle.createDec(playerCount);
		
		//플레이어 덱 셋팅
		for(int i=0;i<playerCount;i++){
			String name = "";
			if(i!=0){
				name = "인공지능 "+i;
			}
			playerList.add(new Player(name, 0, list.get(i)));
		}
		
		//무덤 덱 및 사용한 덱 셋팅
		templateDec = list.get(list.size()-1);
		useDec.add(templateDec.get(0));
		templateDec.remove(0);
		
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
	public GameCurrentState cardInputOutput(boolean state,int index){
		if(state){
			//카드 먹는 부분
			playerList.get(playerTurn).getDec().add(templateDec.get(0));
			templateDec.remove(0);
		}else{
			//카드 내는 부분
			useDec.add(0, playerList.get(playerTurn).getDec().get(index));
			playerList.get(playerTurn).getDec().remove(index);
		}
		//무덤 카드 셔플 부분
		if(templateDec.size() < 5){
			cardSuffle.otherDecSuffle(templateDec,useDec);
		}else{
			
		}
		return null;
	}
	
	/**
	 * AI 활동 호출하는 메서드
	 * @param item 적용된 아이템
	 * @param state 선택된 문양
	 */
	public GameCurrentState playAI(String item, String state){
		//AI 선택 부분
		ai.play(playerTurn, templateDec.get(0), item, state);
		//카드 진행
		
		
		return null;
	}

	//-----------------set/get method------------------------------
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

	public ArrayList<String> getTemplateDec() {
		return templateDec;
	}

	public void setTemplateDec(ArrayList<String> templateDec) {
		this.templateDec = templateDec;
	}

	public ArrayList<String> getUseDec() {
		return useDec;
	}

	public void setUseDec(ArrayList<String> useDec) {
		this.useDec = useDec;
	}
	
	
	
	
}