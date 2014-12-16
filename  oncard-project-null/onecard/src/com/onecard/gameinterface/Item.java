package com.onecard.gameinterface;

import java.util.ArrayList;
import java.util.Arrays;

public class Item {
	private static Item instance = new Item(); 
	private String []item={
		"x2",
		"+2",
		"+1",
		"shield",
		"turnBack",
		"/2"
	};
	
	private ArrayList<String> itemList;

	private Item() {
		super();
		// TODO Auto-generated constructor stub
		itemList = new ArrayList<String>(Arrays.asList(item));
	}

	public ArrayList<String> getItemList() {
		return itemList;
	}

	public void setItemList(ArrayList<String> itemList) {
		this.itemList = itemList;
	}

	public static Item getInstance() {
		if(instance == null)
			instance = new Item();
		return instance;
	}
}
	
	
	
	
	
	
	
