package com.onecard.gameinterface;

import java.util.ArrayList;

public interface AI {
	public String[] play(String groundCard, ArrayList<ArrayList<String>> playerDec, String item, String state);
}
