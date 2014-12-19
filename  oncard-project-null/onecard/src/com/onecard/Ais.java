package com.onecard;

import java.util.*;

import android.content.*;
import android.util.*;

import com.onecard.controll.*;

public class Ais implements com.onecard.gameinterface.AI
{
	//아이템리스트
	String[] com1item = { null, null, null };
	//최종 보낼 데이터
	String[] sends;
	//현재 색
	String patten;
	//현재 그라운드카드
	String groundcard;
	
	//연산용
	String lastcard;
	String counterattack;
	String othercard;
	String comb;
	char[] value;
	char[] coms;
	char[] attack = { '2', 'A', 'B', 'C' };
	char[] two = { 'K', 'J' };
	char[] pattens;
	int random;
	int nulls = 0;
	int sum=0;
	
	//게임 난이도
	int level = 0;
	
	//연산용
	int attacknum;
	int attackok;
	boolean attacker;
	boolean finish;
	ArrayList<String> send = new ArrayList<String>();
	ArrayList<Player> decklist = new ArrayList<Player>();
	ArrayList<String> me = new ArrayList<String>();
	ArrayList<String> com1 = new ArrayList<String>();
	ArrayList<String> com2 = new ArrayList<String>();
	ArrayList<String> com3 = new ArrayList<String>();
	ArrayList<String> playing = new ArrayList<String>();
	ArrayList<String> userdeck = new ArrayList<String>();
	ArrayList<String> attackcard = new ArrayList<String>();
	ArrayList<String> twocardlist = new ArrayList<String>();
	ArrayList<String> nottwocardlist = new ArrayList<String>();
	ArrayList<String> combocard = new ArrayList<String>();
	ArrayList<Character> same = new ArrayList<Character>();
	ArrayList<Character> notsame;
	
	private GameCurrentState Gc;
	
	private Context mContext;
	
	
	// 생성자를 private으로 선언
	// -> 다른 클래스에서 생성자를 부를 수 없다.
	private Ais(Context context)
	{
		mContext = context;
	}
	
	// 대신에 PhonebookManager 타입의 인스턴스를 선언
	private static Ais mInstance = null;
	
	
	// PhonebookManager 인스턴스를 리턴하는 static 메소드를 만듬
	public static Ais getInstance(Context context)
	{
		if (mInstance == null)
		{
			mInstance = new Ais(context);
		}
		
		return mInstance;
	} // end of getInstance()
	
	
	public void AI()
	{
	};
	
	
	public void clear()
	{
		send.clear();
		sends = new String[0];
		
	}
	
	
	@Override
	public String[] play(GameCurrentState currentState)
	{
		Gc = currentState;
		Log.i("MyLog", "t1:::"+Gc.getTemplateDec().get(0));
		patten = currentState.getPattern();
		pattens = patten.toCharArray();
		if (!send.isEmpty())
		{
			clear();
		}
		;
		usernum();
	
		trunnum(Gc.getCurrentTurn());
		switch (level)
		{
			case 0:
				//이지난이도
				easy();
				
				break;
			case 1:
				//노멀난이도
				
				break;
			case 2:
				//하드난이도
				
				switch (decklist.size())
				{
					case 2:
						//1:1
						//j,k와 공격카드를 모음
						
						hardtwo(Gc.getTemplateDec().get(0));
						
						break;
					case 3:
						//2:1
						//공격카드만 모음
						
						break;
					case 4:
						//3:1
						//공격카드만 모음 
						break;
					
					default:
						break;
				}
				
				break;
			
			default:
				break;
		}
		
		Log.i("MyLog","sends[0] : : : " +  sends[0]);
		
		return sends;
		
	};
	
	
	public void usernum()
	{
		decklist = Gc.getPlayerList();
		userdeck = Gc.getUseDec();
		groundcard = userdeck.get(0);
		switch (decklist.size())
		{
			case 2:
				me = decklist.get(0).getDec();
				com1 = decklist.get(1).getDec();
				
				break;
			case 3:
				me = decklist.get(0).getDec();
				com1 = decklist.get(1).getDec();
				com2 = decklist.get(2).getDec();
				break;
			case 4:
				me = decklist.get(0).getDec();
				com1 = decklist.get(1).getDec();
				com2 = decklist.get(2).getDec();
				com3 = decklist.get(3).getDec();
				break;
			
			default:
				break;
		}
		
	};
	
	
	public void trunnum(int a)
	{
		switch (a)
		{
			case 1:
				playing = com1;
				
				break;
			case 2:
				playing = com2;
				break;
			case 3:
				playing = com3;
				break;
			
			default:
				break;
		}
	};
	
	
	public boolean usercard()
	{
		attacker = false;
		attacknum = 0;
		for (int i = 0; i < me.size(); i++)
		{
			if (me.get(i).toCharArray()[1] == attack[0] || me.get(i).toCharArray()[1] == attack[1] || me.get(i).toCharArray()[1] == attack[2] || me.get(i).toCharArray()[1] == attack[3])
			{
				attacknum += 1;
			}
		}
		attackok = me.size() - attacknum;
		if (attackok < 3 && attacknum > 3)
		{
			attacker = true;
		}
		
		return attacker;
		
	}
	
	
	public boolean comscard()
	{
		attacker = false;
		attacknum = 0;
		for (int i = 0; i < playing.size(); i++)
		{
			if (playing.get(i).toCharArray()[1] == attack[0] || playing.get(i).toCharArray()[1] == attack[1] || playing.get(i).toCharArray()[1] == attack[2]
					|| playing.get(i).toCharArray()[1] == attack[3])
			{
				attacknum += 1;
			}
		}
		attackok = playing.size() - attacknum;
		if (attackok < 3 && attacknum > 3)
		{
			attacker = true;
		}
		
		return attacker;
		
	}
	
	
	public boolean attack()
	{
		attacker = false;
		if (usercard() == false && comscard() == true)
		{
			attacker = true;
		}
		else
		{
			attacker = false;
		}
		
		return attacker;
		
	}
	
	
	public ArrayList<String> whatattackcards()
	{
		if (!attackcard.isEmpty())
		{
			attackcard.clear();
		}
		if (attack() == true)
		{
			
			for (int i = 0; i < playing.size(); i++)
			{
				if (playing.get(i).toCharArray()[1] == attack[0])
				{
					attackcard.add(playing.get(i));
					
					break;
				}
				else if (playing.get(i).toCharArray()[1] == attack[1])
				{
					attackcard.add(playing.get(i));
					
					break;
				}
				else if (playing.get(i).toCharArray()[1] == attack[2])
				{
					attackcard.add(playing.get(i));
					
					break;
				}
				else if (playing.get(i).toCharArray()[1] == attack[3])
				{
					
					attackcard.add(playing.get(i));
					
					break;
				}
				else
				{
					nulls += 1;
					
				}
				;
				if (nulls == playing.size())
				{
					
					attackcard.clear();
				}
				;
			}
			
		}
		return attackcard;
		
	}
	
	
	public ArrayList<String> whatattackcard()
	{
		if (!attackcard.isEmpty())
		{
			attackcard.clear();
		}

			
			for (int i = 0; i < playing.size(); i++)
			{
				if (playing.get(i).toCharArray()[1] == attack[0])
				{
					attackcard.add(playing.get(i));
					
					break;
				}
				else if (playing.get(i).toCharArray()[1] == attack[1])
				{
					attackcard.add(playing.get(i));
					
					break;
				}
				else if (playing.get(i).toCharArray()[1] == attack[2])
				{
					attackcard.add(playing.get(i));
					
					break;
				}
				else if (playing.get(i).toCharArray()[1] == attack[3])
				{
					
					attackcard.add(playing.get(i));
					
					break;
				}
				else
				{
					nulls += 1;
					
				}
				;
				if (nulls == playing.size())
				{
					
					attackcard.clear();
				}
				;
			}
			
		
		return attackcard;
		
	}
	
	public void easy()
	{
		Log.i("MyLog", "useratteack + " + String.valueOf(userattack()));
		if (userattack() == null)
		{
			Log.i("MyLog", "eazyprototipe");
		
				eazyprototipe();
				
			
		}
		else if(userattack().equals("Nocounter")){
			send.add(null);
			send.add(null);
			sends = new String[send.size()];
			for (int i = 0; i < send.size(); i++)
			{
			Log.i("MyLog", "useratteack");
		
				sends[i] = send.get(i);
			}
		}
		else
		{
			//유저가 공격을 했을때
			send.add(userattack());
			send.add(null);
			sends = new String[send.size()];
			for (int i = 0; i < send.size(); i++)
			{
			Log.i("MyLog", "useratteack");
		
				sends[i] = send.get(i);
			}
			
		}
		;
	}
	
	
	public void hardtwo(String temp)
	{
		
	
		if (userattack() == null)
		{//공격을 받았는지 유무
			//k,j패턴 추가
			
			if (whatattackcards().isEmpty())
			{//공격할지말지유무
			
				if (temp.toCharArray()[1] == attack[0] || temp.toCharArray()[1] == attack[1] || temp.toCharArray()[1] == attack[2] || temp.toCharArray()[1] == attack[3]
						|| temp.toCharArray()[1] == two[0] || temp.toCharArray()[1] == two[1])
				{
					//강제드로우 유무
					//드로우할 패가 공격카드일시 무조건 드로우
					if (playing.size() < 14)
					{
						send.add(null);
						send.add(null);
					}
					else
					{
						hardprototipe();
					}
					
				}
				else
				{
					hardprototipe();
					
				}
				
			}
			else
			//공격카드가 있고 컴퓨터가 강할때
			{
				
				hardattackorder();
				
			}
		}
		else
		{
			//유저가 공격을 했을때
			send.add(userattack());
			send.add(null);
			sends = new String[send.size()];
			for (int i = 0; i < send.size(); i++)
			{
			
			
				sends[i] = send.get(i);
			}
			
		}
		;
		
	}
	
	
	public String userattack()
	{
		counterattack = null;
		value = userdeck.get(0).toCharArray();
		if (value[1] == attack[0])
		{
			Log.i("MyLog", "2attack");
			counterattack = "Nocounter";
			countercard(3);
			countercard(2);
			samecountercard(1);
			countercard(0);
			
		}
		else if (value[1] == attack[1])
		{
			Log.i("MyLog", "Aattack");
			counterattack = "Nocounter";
			countercard(3);
			countercard(2);
			countercard(1);
			samecountercard(0);
		}
		else if (value[1] == attack[2])
		{
			Log.i("MyLog", "Battack");
			counterattack = "Nocounter";
			countercard(3);
			countercard(2);
		}
		else if (value[1] == attack[3])
		{
			Log.i("MyLog", "Cattack");
			counterattack = "Nocounter";
			countercard(3);
		}
		else
		{
			counterattack = null;
		}
		
		return counterattack;
		
	};
	
	
	public void countercard(int num)
	{
		Log.i("MyLog", "whatattackcard().size()  " + String.valueOf(whatattackcard().size()));
		for (int i = 0; i < whatattackcard().size(); i++)
		{
			Log.i("MyLog", "whatattackcard().get(i)1  " + whatattackcard().get(i));
			if (attack[num] == whatattackcard().get(i).toCharArray()[1])
							
			{
				counterattack = whatattackcard().get(i);
				Log.i("MyLog", "whatattackcard().get(i)2  " + whatattackcard().get(i));
				break;
				
			}
		}
		
	}
	public void samecountercard(int num)
	{
		Log.i("MyLog", "whatattackcard().size()  " + String.valueOf(whatattackcard().size()));
		for (int i = 0; i < whatattackcard().size(); i++)
		{
			Log.i("MyLog", "whatattackcard().get(i)1  " + whatattackcard().get(i));
			if (attack[num] == whatattackcard().get(i).toCharArray()[1])
			{
			
				
				
				Log.i("MyLog", "value[0]  " + value[0]);
				if(whatattackcard().get(i).toCharArray()[0]==value[0]){
				counterattack = whatattackcard().get(i);
				Log.i("MyLog", "whatattackcard().get(i)2  " + whatattackcard().get(i));
				break;
				}
			}
		}
		
	}
	
	
	public void itemuse()
	{
		if (com1item.length != 0)
		{
			random = (int) (Math.random() * com1item.length);
			send.add(com1item[random]);
		}
		else
		{
			send.add(null);
		}
		
	}
	
	
	public void eazyprototipe()
	{
		nulls = 1;
		//유저가 공격을 안했을때
		value = groundcard.toCharArray();
		
		for (int i = 0; i < playing.size(); i++)
		{
			Log.i("MyLog", "for!");
			Log.i("MyLog", "size!" + playing.size());
			Log.i("MyLog", "i!" + String.valueOf(i));
			coms = playing.get(i).toCharArray();
			if (value[0] == coms[0])
			{
				send.add(playing.get(i).toString());
				Log.i("MyLog", "put card" +playing.get(i).toString());
				if (value[1] == attack[0] || value[1] == attack[1])
				{
					random = (int) (Math.random() * com1item.length);
					send.add(com1item[random]);
				}
				else
				{
					send.add(null);
				}
				break;
				
			}
			else if (value[1] == coms[1])
			{
				send.add(playing.get(i).toString());
				Log.i("MyLog", "3333333333");
				if (value[1] == attack[0] || value[1] == attack[1])
				{
					random = (int) (Math.random() * com1item.length);
					send.add(com1item[random]);
				}
				else
				{
					send.add(null);
				}
				break;
				
			}
			else if (coms[1] == attack[2])
			{
				
				
					//조커유무
					Log.i("MyLog", "jk");
					send.add(playing.get(i).toString());
					itemuse();
					break;
				
				
			}
			else if (coms[1] == attack[3])
			{
				
				
					//조커유무
					Log.i("MyLog", "jk");
					send.add(playing.get(i).toString());
					itemuse();
					break;
				
			}else{
				nulls += 1;
				Log.i("MyLog", "nulls");
				
			}
			;
			if ((i+1) == playing.size())
			{
				Log.i("MyLog", "111111111");
				send.add(null);
				send.add(null);
				nulls = 1;
			}
			;
			
		}
		;
		sends = new String[send.size()];
		for (int i = 0; i < send.size(); i++)
		{
			
			sends[i] = send.get(i);
			Log.i("MyLog","send : : : " +  send.get(i));
			Log.i("MyLog","sends : : : " +  sends[i]);
		}
		
	}
	
	
	public void hardprototipe()
	{
		
		//공격타이밍이 아닐때
		value = groundcard.toCharArray();
		for (int i = 0; i < playing.size(); i++)
		{
			coms = playing.get(i).toCharArray();
			if (value[0] == coms[0])
			//낼수 있는 카드가 있는지 검사
			{
				send.add(playing.get(i).toString());
				
				break;
				
			}
			else if (value[1] == coms[1])
			{
				send.add(playing.get(i).toString());
				
				break;
				
			}
			else if (value[0] == 'J' || value[0] == 'C' || value[0] == 'H' || value[0] == 'D' || value[0] == 'A')
			{
				
				if (coms[1] == attack[2])
				{
					//조커유무
					send.add(playing.get(i).toString());
					itemuse();
					break;
				}
				
			}
			else if (value[0] == 'J' || value[0] == 'C' || value[0] == 'H' || value[0] == 'D' || value[0] == 'A')
			{
				if (coms[1] == attack[3])
				{
					//조커유무
					send.add(playing.get(i).toString());
					itemuse();
					break;
				}
			}
			else
			{
				nulls += 1;
				
			}
			;
			if (nulls == playing.size())
			{
				send.add(null);
				nulls = 0;
			}
			;
			
		}
		;
		
		/////리턴값 출력
		for (int i = 0; i < send.size(); i++)
		{
			sends[i] = send.get(i);
		}
		/////리턴값 출력
		
	}
	
	
	public void hardattackorder()
	{
		nulls = 1;
		///////공격명령을 받았을시
		value = groundcard.toCharArray();
		for (int i = 0; i < whatattackcards().size(); i++)
		{
			coms = whatattackcards().get(i).toCharArray();
			if (value[0] == coms[0])
			//낼수 있는 카드가 있는지 검사
			{
				send.add(whatattackcards().get(i).toString());
				
				break;
				
			}
			else if (value[1] == coms[1])
			{
				send.add(whatattackcards().get(i).toString());
				
				break;
				
			}
			else if (value[0] == 'J' || value[0] == 'C' || value[0] == 'H' || value[0] == 'D' || value[0] == 'A')
			{
				
				if (coms[1] == attack[2])
				{
					//조커유무
					send.add(playing.get(i).toString());
					itemuse();
					break;
				}
				
			}
			else if (value[0] == 'J' || value[0] == 'C' || value[0] == 'H' || value[0] == 'D' || value[0] == 'A')
			{
				if (coms[1] == attack[3])
				{
					//조커유무
					send.add(whatattackcards().get(i).toString());
					itemuse();
					
					break;
				}
			}
			else
			{
				nulls += 1;
				
			}
			;
			if (nulls == whatattackcards().size())
			{
				hardprototipe();
				nulls = 999;
			}
			;
			
		}
		;
		
		/////리턴값 출력
		if (nulls != 999)
		{
			for (int i = 0; i < send.size(); i++)
			{
				sends[i] = send.get(i);
			}
			
		}
	}
	
	
	
	
	
	
	
	
		
	}


