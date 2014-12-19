package com.onecard.controll;

import java.util.ArrayList;

import android.util.Log;

import com.onecard.CardModel;
import com.onecard.GameMain;
import com.onecard.gameinterface.CardSuffle;
import com.onecard.gameinterface.GameResult;

public class GameCurrentState {
	
	// 플레이어 정보들 0번이 플레이어 나머지는 AI
	private ArrayList<Player> playerList;
	//턴 방향 false : left, true : right
	private boolean turn;
	// 현재 턴인 플레이어 번호 == 리스트 인덱스와 동일
	private int currentTurn;
	// 먹는 패
	private String inputCard;
	// 공격해서 누적된 카드 수
	private int attackCard;
	// 사용자가 받을 무덤 덱
	private ArrayList<String> templateDec;
	// 사용자가 낸 카드 무덤 덱
	private ArrayList<String> useDec;
	//현재 문양
	private String pattern;
	// 카드 셔플 객체
	private CardSuffle cardSuffle;
	//각 플레이어 게임 결과 저장 리스트
	private ArrayList<GameResult> gameResultList;
	public GameCurrentState() {
		super();
		// TODO Auto-generated constructor stub
		currentTurn = -1;
		turn = true;
		cardSuffle = CardModel.getInstance(null);
		gameResultList = new ArrayList<GameResult>();
	}
	
	public ArrayList<GameResult> getGameResultList() {
		return gameResultList;
	}

	public void setGameResultList(ArrayList<GameResult> gameResultList) {
		this.gameResultList = gameResultList;
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

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	@Override
	public String toString() {
		return "GameCurrentState [playerList="
				+ playerList + ", turn=" + turn + ", currentTurn="
				+ currentTurn + ", inputCard=" + inputCard + ", attackCard="
				+ attackCard + "]";
	}
	/**
	 * 다음 턴으로 넘기기
	 */
	public void nextTurn() {
		// TODO Auto-generated method stub
		do{
			if(turn){
				currentTurn++;
			}else{
				currentTurn--;
			}
			if(currentTurn< 0)
				currentTurn = playerList.size()-1;
			else if(currentTurn >= playerList.size())
				currentTurn = 0;
				
		}while(playerList.get(currentTurn).isWorkout());
	}
	/**
	 * 턴 바꾸고 다음턴으로 가기
	 */
	public void changeTurn() {
		// TODO Auto-generated method stub
		setTurn(!isTurn());
		nextTurn();
	}
	/**
	 * 카드 내기
	 * @param index 낼 카드 인덱스
	 */
	public void outputCard(int index) {
		// TODO Auto-generated method stub
		String tempCard = playerList.get(currentTurn).getDec().get(index);
		Log.d("MyLog", "outputCard() : currentTurn : " + currentTurn);
		
		if(checkCard(tempCard)){
			//낼 수 있는 카드 일때
			//낸 카드를 바닥으로 깔음
			playerList.get(currentTurn).getDec().remove(index);
			useDec.add(0,tempCard);
			//특수 카드 체크
			checkSpecialCard(tempCard);
			
		}else{
			//맞는 카드가 아님
			currentTurn--;
		}
	}
	/**
	 * 아이템 사용하는 부분
	 * @param item
	 */
	public void useItem(int num){
		
		switch(num){
		case 0:
			attackCard *= 2;
			break;
		case 1:
			attackCard += 2;
			break;
		case 2:
			attackCard += 1;
			break;
		case 3:
			attackCard = 0;
			break;
		case 4:
			changeTurn();
			break;
		case 5:
			attackCard /= 2;
			break;
			
		}
		
	}
	/**
	 * 특수카드 체크 부분
	 * @param tempCard 체크할 카드
	 */
	private void checkSpecialCard(String tempCard) {
		// TODO Auto-generated method stub
		switch(tempCard.charAt(1)){
		case 'A':
			attackCard += 3;
			break;
		case '2':
			attackCard += 2;
			break;
		case 'C':
			attackCard += 7;
			break;
		case 'B':
			attackCard += 5;
			break;
		case 'Q':
			changeTurn();
			break;
		case '7':
		case 'K':
			currentTurn--;
			break;
		case 'J':
			nextTurn();
			break;
		}
	}
	/**
	 * 7을 냈을 때 모양 바꾸기
	 * @param pattern 모양 이니셜 하트 - H, 스페이드-S, 클로버-C, 다이아-D
	 */
	public void changePattern(String pattern){
		this.pattern = pattern;
	}
	
	/**
	 * 선택한 카드 체크하는 부분
	 * @param tempCard 낼 카드
	 * @return 내도 되는 카드면 참, 아니면 false
	 */
	private boolean checkCard(String tempCard) {
		String groundCard = useDec.get(0);
		Log.d("MyLog", "카드 체크 부분");
		Log.d("MyLog", "낸 카드 : "+tempCard);
		Log.d("MyLog", "바닥카드 : "+groundCard);
		
		
		boolean temp = false;
		if(pattern.charAt(0) == tempCard.charAt(0) || pattern.equals("J") || tempCard.charAt(0) == 'J'){
			Log.d("MyLog", "공격여부 체크 부분");
			//공격이 있는지 없는 지에 따라서 체크
			if(attackCard == 0)
				temp = true;
			else
				temp = checkAttackCard(tempCard);
		}else{
			Log.d("MyLog", "숫자가 같은지 판단하는 부분");
			if(groundCard.charAt(1) == tempCard.charAt(1))
				temp = true;
			temp = false;
		}
		return temp;
	}
	/**
	 * 공격 카드 체크
	 * @param tempCard
	 * @return
	 */
	private boolean checkAttackCard(String tempCard) {
		// TODO Auto-generated method stub
		switch(tempCard.charAt(1)){
		case 'A':
		case '2':
		case 'C':
		case 'B':
			return true;
		}
		return false;
	}

	/**
	 * 카드 먹기
	 * attackCard 개수 만큼 먹기
	 * 그냥 먹더라도 0개니까 1개 증가후 반복문 실행
	 */
	public void inputCard() {
		// TODO Auto-generated method stub
		if(attackCard == 0)
			attackCard++;
		while(attackCard > 0){
			playerList.get(currentTurn).getDec().add(templateDec.get(0));
			templateDec.remove(0);
			attackCard--;
		}
	}
	/**
	 * 게임 생성 메서드
	 * @param list 전체 덱
	 * @param playerCount 플레이어 숫자
	 */
	public void createGame(int playerCount) {
		// TODO Auto-generated method stub
		gameResultList = new ArrayList<GameResult>();
		useDec = new ArrayList<String>();
		// 전체 카드덱 받아옴
		ArrayList<ArrayList<String>> list = cardSuffle.createDec(playerCount);
		
		playerList = new ArrayList<Player>();
		// 플레이어 덱 셋팅
		for (int i = 0; i < playerCount; i++) {
			String name = "플레이어";
			if (i != 0) {
				name = "인공지능 " + i;
			}
			//플레이어 추가 부분 - 맨마지막 부분은 아이템
			playerList.add(new Player(name, 0, list.get(i), null));
			gameResultList.add(new GameResult(name));
		}
		//무덤덱 및 초기 패 셋팅
		templateDec = list.get(list.size()-1);
		useDec.add(templateDec.get(0));
		templateDec.remove(0);
		pattern = String.valueOf(useDec.get(0).charAt(0));
		//초기 턴
		currentTurn = -1;
	}
	/**
	 * 현재 게임 종료 체크 부분
	 * @return true : 게임 오버 or false 게임 계속
	 */
	public boolean checkGame() {
		// TODO Auto-generated method stub
		boolean temp = false;
			//플레이어 및 AI가 모두 카드를 소진한 경우
			if(playerList.get(0).getDec().size() == 0){
				temp = true;
			}
			//현재 턴 플레이어 및 AI 파산체크
			if(playerList.get(currentTurn).getDec().size() >= 15 || temp){
				playerList.get(currentTurn).setWorkout(true);
				templateDec.addAll(playerList.get(currentTurn).getDec());
				cardSuffle.otherDecSuffle(templateDec, useDec);
				//playerList.get(i).getDec().clear();
			}
			
		return temp;
	}

	
	public CardSuffle getCardSuffle() {
		return cardSuffle;
	}

	public void setCardSuffle(CardSuffle cardSuffle) {
		this.cardSuffle = cardSuffle;
	}
	/**
	 * 승자 셋팅
	 */
	public void setWinner() {
		// TODO Auto-generated method stub
		for(int i=0;i<playerList.size();i++){
			if(playerList.get(i).isWorkout()){
				playerList.get(i).setWin(0);
			}else{
				playerList.get(i).setWin(playerList.get(i).getWin()+1);
				gameResultList.get(i).setScore(gameResultList.get(i).getScore()+excuteScore(i));
				gameResultList.get(i).setWin(gameResultList.get(i).getWin()+1);
			}
		}
		
	}
	/**
	 * 해당 플레이어 점수 산출 메서드
	 * @param playerIndex 해당 플레이어 인덱스
	 * @return 산출된 점수
	 */
	private int excuteScore(int playerIndex) {
		// TODO Auto-generated method stub
		int total = 0;
		for(int i=0;i<playerList.size();i++){
			if(i==playerIndex)
				continue;
			total += playerList.get(i).getDec().size() * playerList.get(i).getWin();
		}
		return total;
	}
	/**
	 *  5연승한 플레이어가 있는 지 확인하는 메서드
	 * @return 있으면 true, 없으면 false
	 */
	public boolean resultGame() {
		// TODO Auto-generated method stub
		for(int i=0;i<playerList.size();i++){
			if(playerList.get(i).getWin() >= 5){
				return true;
			}
		}
		return false;
	}
	/**
	 * 새게임 시작하는 부분
	 */
	public void restart() {
		// TODO Auto-generated method stub
		// 전체 카드덱 받아옴
				ArrayList<ArrayList<String>> list = cardSuffle.createDec(playerList.size());
				
				// 플레이어 덱 셋팅
				for (int i = 0; i < playerList.size(); i++) {
					playerList.get(i).setDec(list.get(i));
				}
				//무덤덱 및 초기 패 셋팅
				templateDec = list.get(list.size()-1);
				useDec.add(templateDec.get(0));
				templateDec.remove(0);
				//초기 턴
				currentTurn = -1;
	}

}
