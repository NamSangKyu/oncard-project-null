package com.onecard.controll;

import java.util.ArrayList;

import android.text.TextUtils.TruncateAt;
import android.util.Log;

import com.onecard.Ais;
import com.onecard.gameinterface.AI;
import com.onecard.gameinterface.CardSuffle;
import com.onecard.gameinterface.GameResult;
import com.onecard.gameinterface.Item;

public class GameControll {
	public static final int GAME_START = 0;
	public static final int GAME_RUN = 1;
	public static final int GAME_OVER = 2;
	public static final int GAME_ROUND_EXIT = 3;
	
	public boolean inputSelect;
	public static GameCurrentState currentState = new GameCurrentState();
	// 게임 컨트롤러
	private static GameControll instance = new GameControll();
	// 게임 상태 : 게임 시작 == 0, 게임 진행 == 1, 게임 오버 == 2
	private int gameState;
	
	// 게임 AI
	private AI ai;

	private GameControll() {
		super();
		// TODO Auto-generated constructor stub
	}

	public static GameControll getInstance() {
		if (instance == null)
			instance = new GameControll();
		return instance;
	}


	/**
	 * 게임 시작 부분
	 * 
	 */
	public void start(int playerCount) {
		// TODO Auto-generated method stub
		
		currentState.createGame(playerCount);
		ai = Ais.getInstance(null);
		// 게임 시작 부분
		if(gameState == GAME_START){
			gameState = GAME_RUN;
		}
	}
	
	/**
	 * 새게임 시작 부분
	 * @return
	 */
	public void restart(){
		currentState.restart();
		gameState = GAME_RUN;
	}
	
	
	public GameCurrentState playingGame() {
		// TODO Auto-generated method stub
		if(currentState.getCurrentTurn()!=0){
			inputSelect = false;
			return playAI();
		}else{
			inputSelect = true;
		}
		return currentState;
	}

	/**
	 * 다음 턴으로 이동
	 */
	public GameCurrentState nextTurn() {
		//다음턴 이동
		currentState.nextTurn();
		return currentState;
	}
	/**
	 * 아이템 사용하기
	 * @param item 사용할 아이템 명
	 * @return 패 감소시키는 아이템은 사용후 카드 입력을 받아야 하므로 true 리턴, 그외 무적 및 턴 바꾸기는 카드를 받을 필요가 없으므로 false
	 */
	public boolean useItem(String item){
		int num = Item.getInstance().getItemList().indexOf(item);
		currentState.useItem(num);
		if(num == 3||num == 4)
			return false;
		return true;
	}
	
	/**
	 * 카드 내기 및 먹는 부분
	 * 
	 * @param state true 먹기, false 내기
	 *            
	 */
	public GameCurrentState cardInputOutput(boolean state, int index) {
		Log.d("MyLog", " index : " +state + " index : " +index);
		if(state){
			currentState.inputCard();
		}else{
			currentState.outputCard(index);
		}
		return currentState;
	}

	/**
	 * AI 활동 호출하는 메서드
	 * 
	 * @param item
	 *            적용된 아이템
	 * @param state
	 *            선택된 문양
	 */
	public GameCurrentState playAI() {
		// AI 선택 부분
		String aiPlay[] = ai.play(currentState);
		/*//사용한 아이템
		if(aiPlay[1]!=null){
			useItem(aiPlay[1]);
		}*/
		//낸 카드
		if(aiPlay[0]==null){
			cardInputOutput(true, 0);
		}else{
			cardInputOutput(false, Integer.parseInt(aiPlay[0]));
		}
		// 카드 진행 및 아이템 적용
		return currentState;
	}
	/**
	 * 모양 바꾸기
	 */
	public void changePattern(String pattern){
		currentState.changePattern(pattern);
	}
	/**
	 * 게임오버 체크 부분
	 */
	public int checkGame(){
		if(currentState.checkGame()){
			gameState = GAME_ROUND_EXIT;
			//1등을 제외한 모든 플레이어 연승 제거 및 점수 셋팅
			currentState.setWinner();
		}
		if(currentState.resultGame()){
			gameState = GAME_OVER;
		}
		return gameState;
	}
	
	/**
	 * 게임 결과 받아오기
	 * 
	 */
	public ArrayList<GameResult> resultGame(){
				return currentState.getGameResultList();
	}
	
	// -----------------set/get method------------------------------

	public static GameCurrentState getCurrentState() {
		if (currentState == null)
			currentState = new GameCurrentState();
		return currentState;
	}

	public int getGameState() {
		return gameState;
	}

	public void setGameState(int gameState) {
		this.gameState = gameState;
	}

	
	public AI getAi() {
		return ai;
	}

	public void setAi(AI ai) {
		this.ai = ai;
	}

}
