package com.onecard;

import java.util.*;

import com.onecard.gameinterface.*;



import android.content.*;
import android.util.*;

public class CardModel implements CardSuffle
{
	private ArrayList<String> deck = new ArrayList<String>();
	private ArrayList<String> me = new ArrayList<String>();
	private ArrayList<String> com1 = new ArrayList<String>();
	private ArrayList<String> com2 = new ArrayList<String>();
	private ArrayList<String> com3 = new ArrayList<String>();
	private ArrayList<String> randomdeck = new ArrayList<String>();
	private ArrayList<ArrayList<String>> Alldeck = new ArrayList<ArrayList<String>>();
	private int number;
	private int get;

	
	private Context mContext;
	
	// 생성자를 private으로 선언
	// -> 다른 클래스에서 생성자를 부를 수 없다.
	private CardModel(Context context) {
		mContext = context;
	}
	
	// 대신에 PhonebookManager 타입의 인스턴스를 선언
	private static CardModel mInstance = null;
	
	// PhonebookManager 인스턴스를 리턴하는 static 메소드를 만듬
	public static CardModel getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new CardModel(context);
		}
		
		return mInstance;
	} // end of getInstance()
	
	
	
	 void CardModel()
	{
		deck.add("HA");
		deck.add("H2");
		deck.add("H3");
		deck.add("H4");
		deck.add("H5");
		deck.add("H6");
		deck.add("H7");
		deck.add("H8");
		deck.add("H9");
		deck.add("H10");
		deck.add("HK");
		deck.add("HQ");
		deck.add("HJ");
		//하트
		deck.add("SA");
		deck.add("S2");
		deck.add("S3");
		deck.add("S4");
		deck.add("S5");
		deck.add("S6");
		deck.add("S7");
		deck.add("S8");
		deck.add("S9");
		deck.add("S10");
		deck.add("SK");
		deck.add("SQ");
		deck.add("SJ");
		//스페이드
		deck.add("CA");
		deck.add("C2");
		deck.add("C3");
		deck.add("C4");
		deck.add("C5");
		deck.add("C6");
		deck.add("C7");
		deck.add("C8");
		deck.add("C9");
		deck.add("C10");
		deck.add("CK");
		deck.add("CQ");
		deck.add("CJ");
		//클로버
		deck.add("DA");
		deck.add("D2");
		deck.add("D3");
		deck.add("D4");
		deck.add("D5");
		deck.add("D6");
		deck.add("D7");
		deck.add("D8");
		deck.add("D9");
		deck.add("D10");
		deck.add("DK");
		deck.add("DQ");
		deck.add("DJ");
		//다이아
		deck.add("JC");
		deck.add("JB");
		//레드,블랙 조커
		
	};
	
	
	 void Shuffle()
	{
		
		switch (number)
		{
			case 2:
				
				for (int i = 0; i < number * 7; i++)
				{
					if (i < 7)
					{
						get = (int) (Math.random() * deck.size());
						me.add(deck.get(get));
						deck.remove(get);
						
					}
					else
					{
						
						get = (int) (Math.random() * deck.size());
						com1.add(deck.get(get));
						deck.remove(get);
					}
				}
				randomdeck(deck);
				break;
			
			case 3:
				
				for (int i = 0; i < number * 7; i++)
				{
					if (i < 7)
					{
						get = (int) (Math.random() * deck.size());
						me.add(deck.get(get));
						deck.remove(get);
						
					}
					else if (i > 6 && i < 14)
					{
						
						get = (int) (Math.random() * deck.size());
						com1.add(deck.get(get));
						deck.remove(get);
					}
					else
					{
						
						get = (int) (Math.random() * deck.size());
						com2.add(deck.get(get));
						deck.remove(get);
					}
					;
				}
				
				randomdeck(deck);
				
				break;
			case 4:
			
				
				for (int i = 0; i < number * 7; i++)
				{
					if (i < 7)
					{
						get = (int) (Math.random() * deck.size());
						me.add(deck.get(get));
						deck.remove(get);
						
					}
					else if (i > 6 && i < 14)
					{
						
						get = (int) (Math.random() * deck.size());
						com1.add(deck.get(get));
						deck.remove(get);
					}
					else if (i > 13 && i < 21)
					{
						
						get = (int) (Math.random() * deck.size());
						com2.add(deck.get(get));
						deck.remove(get);
					}
					else
					{
						
						get = (int) (Math.random() * deck.size());
						com3.add(deck.get(get));
						deck.remove(get);
					}
					;
				}
				randomdeck(deck);
				break;
			default:
				break;
				
		}
		
	};
	
	
	public void randomdeck(ArrayList<String> in)
	{

	
		while (!in.isEmpty())
	
		{
			get = (int) (Math.random() * in.size());
			Log.i("MyLog", String.valueOf(get) + " " + String.valueOf(in.size()));
			randomdeck.add(in.get(get));
			in.remove(get);
		}
		
	};
	
	
	
	public void clear(){
		deck.clear();
		me.clear();
		com1.clear();
		com2.clear();
		com3.clear();
		randomdeck.clear();
		Alldeck.clear();
		
	}



	@Override
	public ArrayList<ArrayList<String>> createDec(int playerCount)
	{
		number = playerCount;
		if(!Alldeck.isEmpty()||!randomdeck.isEmpty()){
			clear();
		};
		CardModel();
		Shuffle();
		switch (number)
		{
			case 2:
				Alldeck.add(me);
				Alldeck.add(com1);
				Alldeck.add(randomdeck);
				
				break;
			case 3:
				Alldeck.add(me);
				Alldeck.add(com1);
				Alldeck.add(com2);
				Alldeck.add(randomdeck);
				break;
			case 4:
				Alldeck.add(me);
				Alldeck.add(com1);
				Alldeck.add(com2);
				Alldeck.add(com3);
				Alldeck.add(randomdeck);
				break;
			default:
				break;
		}
		return Alldeck;
	}





	@Override
	public ArrayList<String> otherDecSuffle(ArrayList<String> currentDec, ArrayList<String> useDec)
	{
		// TODO Auto-generated method stub
		useDec.remove(0);
		for(int i = 0 ;i<currentDec.size();i++){
			useDec.add(currentDec.get(i));
		};
		if(!Alldeck.isEmpty()||!randomdeck.isEmpty()){
			clear();
		};
		randomdeck(useDec);
		// TODO Auto-generated method stub
		return randomdeck;
	};
}
