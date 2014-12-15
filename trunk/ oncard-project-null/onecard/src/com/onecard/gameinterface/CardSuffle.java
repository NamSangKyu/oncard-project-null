package com.onecard.gameinterface;

import java.util.ArrayList;

public interface CardSuffle {
	/**
	 * 카드 최초 셔플하는 메서드
	 * @return 각각의 플레이어 덱 및 무덤 덱
	 */
	public ArrayList<ArrayList<String>> createDec();
	
	/**
	 * 무덤에 있는 덱 셔플하는 메서드
	 * @param currentDec 현재 무덤에 있는 덱
	 * @param useDec 사용한 덱
	 * @return 새로 셔플한 덱
	 */
	public ArrayList<String> otherDecSuffle(ArrayList<String> currentDec, ArrayList<String> useDec);
}
