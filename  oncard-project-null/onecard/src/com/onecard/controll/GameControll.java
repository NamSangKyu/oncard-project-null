package com.onecard.controll;

import java.util.ArrayList;
import java.util.Arrays;

import android.content.Context;
import android.util.Log;

import com.onecard.Ais;
import com.onecard.LoginActivity;
import com.onecard.db.OnecardDb;
import com.onecard.db.OnecardManager;
import com.onecard.gameinterface.AI;
import com.onecard.gameinterface.GameResult;
import com.onecard.gameinterface.Item;

public class GameControll {
	public static final int GAME_START = 0;
	public static final int GAME_RUN = 1;
	public static final int GAME_OVER = 2;
	public static final int GAME_ROUND_EXIT = 3;
	
	public boolean inputSelect;
	public static GameCurrentState currentState;
	// 게임 컨트롤러
	private static GameControll instance = null;
	// 게임 상태 : 게임 시작 == 0, 게임 진행 == 1, 게임 오버 == 2
	private int gameState;
	//ai 게임 상태
	String aiPlay[];
	// 게임 AI
	private AI ai;
	
	private static Context context;

	private GameControll(Context context) {
		super();
		// TODO Auto-generated constructor stub
		this.context  = context;
	}

	public static GameControll getInstance(Context context) {
		if (instance == null){
			instance = new GameControll(context);
			currentState = getCurrentState();
		}
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
			if(currentState.getTemplateDec().size() > 0)
				currentState.inputCard();
			else
				currentState.setCurrentTurn(currentState.getCurrentTurn()-1);
		}else{
			currentState.outputCard(index);
		}
				//카드가 5장 이하면 사용한 덱을 무덤 덱으로 셔플
		if(currentState.getTemplateDec().size() < 5){
			currentState.cardMerge();
		}
		
		Log.d("CardInfo", "카드 inputoutput after");
		Log.d("CardInfo","선택된 카드 인덱스 : " + index);
		Log.d("CardInfo","누적된 공격 수 : " + currentState.getAttackCard());
		Log.d("CardInfo","현재 패턴 : " + currentState.getPattern());
		Log.d("CardInfo","무덤 덱 : " + currentState.getTemplateDec().toString());
		Log.d("CardInfo","사용한 덱 : " + currentState.getUseDec().toString());
		int count = 0;
		for(int i=0;i<currentState.getPlayerList().size();i++){
			Log.d("CardInfo",currentState.getPlayerList().get(i).getName()+" 덱 : " + currentState.getPlayerList().get(i).getDec().toString());
			if(!currentState.getPlayerList().get(i).isWorkout()){
				count += currentState.getPlayerList().get(i).getDec().size();
			}
		}
		for(int i=0;i<currentState.getPlayerList().size();i++){
			Log.d("CardInfo",currentState.getPlayerList().get(i).getName()+" 상태 : " + currentState.getPlayerList().get(i).isWorkout());
		}
		count += currentState.getTemplateDec().size();
		count += currentState.getUseDec().size();
		
		Log.d("CardInfo","총 카드 개수 : " + count);
		Log.d("CardInfo","현재 턴 : " + currentState.getCurrentTurn());
		Log.d("CardInfo", "------------------------------");
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
		aiPlay = ai.play(currentState);
		Log.d("MyLog", "ai : " + Arrays.toString(aiPlay));
		/*//사용한 아이템
		if(aiPlay[1]!=null){
			useItem(aiPlay[1]);
		}*/
		//낸 카드
		if(aiPlay[0]==null){
			cardInputOutput(true, 0);
		}else{
			int cardIndex = currentState.getPlayerList().get(currentState.getCurrentTurn()).getDec().indexOf(aiPlay[0]);
			cardInputOutput(false, cardIndex);
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
			for(int i=0;i<currentState.getGameResultList().size();i++){
				Log.d("GameResult", currentState.getGameResultList().get(i).toString());
			}
		}
		if(currentState.resultGame()){
			gameState = GAME_OVER;
			currentState.rankUpdate(context);
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
			currentState = new GameCurrentState(context);
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
