package com.onecard.controll;

import java.util.ArrayList;

public class GameCurrentState {
	// 바닥에 깔려 있는 패 or 낸 패
	private String groundCard;
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
	/**
	 * 
	 * @param groundCard
	 *            바닥패 or 낸 패
	 * @param playerList
	 *            현재 플레이어 리스트
	 * @param turn
	 *            턴 방향
	 * @param currentTurn
	 *            현재 턴 번호
	 * @param inputCard
	 *            먹은 카드
	 * @param attackCard
	 *            공격해서 누적된 카드숫자
	 */
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

	public GameCurrentState() {
		super();
		// TODO Auto-generated constructor stub
		currentTurn = -1;
		turn = true;
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

	@Override
	public String toString() {
		return "GameCurrentState [groundCard=" + groundCard + ", playerList="
				+ playerList + ", turn=" + turn + ", currentTurn="
				+ currentTurn + ", inputCard=" + inputCard + ", attackCard="
				+ attackCard + "]";
	}
	/**
	 * 다음 턴으로 넘기기
	 */
	public void nextTurn() {
		// TODO Auto-generated method stub
		if(turn){
			currentTurn++;
		}else{
			currentTurn--;
		}
		if(currentTurn< 0)
			currentTurn = playerList.size()-1;
		else
			currentTurn = 0;
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
		if(checkCard(tempCard)){
			//낼 수 있는 카드 일때
			//특수 카드 체크
			checkSpecialCard(tempCard);
			//낸 카드를 바닥으로 깔음
			playerList.get(currentTurn).getDec().remove(index);
			useDec.add(tempCard);
		}else{
			//맞는 카드가 아님
			currentTurn--;
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
		case 'K':
			currentTurn--;
			break;
		case 'J':
			nextTurn();
			break;
		}
	}

	/**
	 * 선택한 카드 체크하는 부분
	 * @param tempCard 낼 카드
	 * @return 내도 되는 카드면 참, 아니면 false
	 */
	private boolean checkCard(String tempCard) {
		String groundCard = useDec.get(0);
		boolean temp = false;
		if(groundCard.charAt(0) == tempCard.charAt(0)){
			temp = true;
		}else if(tempCard.charAt(0) == 'J'){
			temp = true;
		}else{
			if(groundCard.charAt(1) == tempCard.charAt(1))
				temp = true;
			temp = false;
		}
		return temp;
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
	public void createGame(ArrayList<ArrayList<String>> list,int playerCount) {
		// TODO Auto-generated method stub
		playerList = new ArrayList<Player>();
		// 플레이어 덱 셋팅
		for (int i = 0; i < playerCount; i++) {
			String name = "";
			if (i != 0) {
				name = "인공지능 " + i;
			}
			//플레이어 추가 부분
			//playerList.add(new Player(name, 0, list.get(i)));
		}
		//무덤덱 및 초기 패 셋팅
		templateDec = list.get(list.size()-1);
		useDec.add(templateDec.get(0));
		templateDec.remove(0);
		//초기 턴
		currentTurn = -1;
	}

}