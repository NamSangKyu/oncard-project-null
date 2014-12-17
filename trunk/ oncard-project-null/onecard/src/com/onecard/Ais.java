package com.onecard;



import java.util.*;

import android.content.*;

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
	String counterattack;
	String othercard;	
	char[] value;
	char[] coms;	
	char[] attack = { '2', 'A', 'B', 'C' };
	char[] two = { 'K', 'J' };
	char[] pattens;
	int random;
	int nulls = 0;
	
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
	ArrayList<Character> same = new ArrayList<Character>();
	ArrayList<Character> notsame;
	
	private  GameCurrentState Gc;
	
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
		patten = currentState.getPattern();
		pattens = patten.toCharArray();
		if (!send.isEmpty())
		{
			clear();
		}
		;
		usernum();
		newpatten();
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
	
	
	public void easy()
	{
		if (userattack() == null)
		{
			eazyprototipe();
		}
		else
		{
			//유저가 공격을 했을때
			send.add(userattack());
			for (int i = 0; i < send.size(); i++)
			{
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
			for (int i = 0; i < send.size(); i++)
			{
				sends[i] = send.get(i);
			}
			
		}
		;
		
	}
	
	
	public String userattack()
	{
		value = userdeck.get(0).toCharArray();
		if (value[1] == attack[0])
		{
			countercard(3);
			countercard(2);
			countercard(1);
			countercard(0);
			
		}
		else if (value[1] == attack[1])
		{
			countercard(3);
			countercard(2);
			countercard(1);
		}
		else if (value[1] == attack[2])
		{
			countercard(3);
			countercard(2);
		}
		else if (value[1] == attack[3])
		{
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
		for (int i = 0; i < whatattackcards().size(); i++)
		{
			if (attack[num] == whatattackcards().get(i).toCharArray()[1])
			{
				counterattack = whatattackcards().get(i);
				break;
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
		
		//유저가 공격을 안했을때
		value = groundcard.toCharArray();
		for (int i = 0; i < playing.size(); i++)
		{
			coms = playing.get(i).toCharArray();
			if (value[0] == coms[0])
			{
				send.add(playing.get(i).toString());
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
			else if (value[0] == 'J' || value[0] == 'C'
					||value[0] == 'H' || value[0] == 'D'|| value[0] == 'A')
			{
				
				if (coms[1] == attack[2])
				{
					//조커유무
					send.add(playing.get(i).toString());
					itemuse();
					break;
				}
				
			}
			else if (value[0] == 'J' || value[0] == 'C'
					||value[0] == 'H' || value[0] == 'D'|| value[0] == 'A')
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
				send.add(null);
				nulls = 0;
			}
			;
			
		}
		;
		
		for (int i = 0; i < send.size(); i++)
		{
			sends[i] = send.get(i);
		}
		
	}
	
	
	public void hardprototipe()
	{
		
		//공격타이밍이 아닐때
		value =groundcard.toCharArray();
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
			else if (value[0] == 'J' || value[0] == 'C'
					||value[0] == 'H' || value[0] == 'D'|| value[0] == 'A')
			{
				
				if (coms[1] == attack[2])
				{
					//조커유무
					send.add(playing.get(i).toString());
					itemuse();
					break;
				}
				
			}
			else if (value[0] == 'J' || value[0] == 'C'
					||value[0] == 'H' || value[0] == 'D'|| value[0] == 'A')
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
		nulls = 0;
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
			else if (value[0] == 'J' || value[0] == 'C'
					||value[0] == 'H' || value[0] == 'D'|| value[0] == 'A')
			{
				
				if (coms[1] == attack[2])
				{
					//조커유무
					send.add(playing.get(i).toString());
					itemuse();
					break;
				}
				
			}
			else if (value[0] == 'J' || value[0] == 'C'
					||value[0] == 'H' || value[0] == 'D'|| value[0] == 'A')
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
	
	
	public String finish0()
	{
		int what = 0;
		if (finish1())
		{
			//처음 조건충족시
			if (finish2())
			{
				//겹치는게 있을시
				if (finish4())
				{
					//겹치는개 1개
					if (finish5())
					{
						
						//내는로직실행
						what = 1;
						
					}
					else
					{
						what = 0;
					}
					
				}
				else
				{
					//겹치는개 2개이상
					
					//내는로직실행
					what = 2;
					
				}
				
			}
			else
			{
				//겹치는게 없을시
				if (finish3())
				{
					if(finish31()){
						what = 3;
					}else{
						what = 4;
					}
					//남은것들 k
					//내는로직실행
					
					
				}
				else
				{
					//남은것들 j
					
					//내는로직실행
					what = 0;
					
				}
				
			}
			
		}
		else
		{
			//통상진행
			what = 0;
		}
		
		return finishAll(what);
	}
	
	
	public boolean finish1()
	{
		int num = 0;
		finish = true;
		if (!twocardlist.isEmpty() || !nottwocardlist.isEmpty())
		{
			twocardlist.clear();
			nottwocardlist.clear();
		}
		for (int i = 0; i < playing.size(); i++)
		{
			if (playing.get(i).toCharArray()[1] == two[0] || playing.get(i).toCharArray()[1] == two[1])
			{
				twocardlist.add(playing.get(i).toString());
				
			}
			else
			{
				nottwocardlist.add(playing.get(i).toString());
				
			}
			;
		}
		
		if (nottwocardlist.size() < 2 && twocardlist.size() > 0)
		{
			for (int i = 0; i < nottwocardlist.size(); i++)
			{
				othercard = nottwocardlist.get(i);
			}
			
			for (int i = 0; i < twocardlist.size(); i++)
			{
				if (twocardlist.get(i).toCharArray()[0] == othercard.toCharArray()[0])
				{
					num += 1;
				}
			}
			if (num == 0)
			{
				finish = false;
				//더블카드와 남은 한개의 카드의 색이 하나도 같지 않으면 false
				
			}
			
		}
		else
		{
			finish = false;
			
		}
		return finish;
		//j,k를 제외한카드가 1장이남고 j,k가 1장 이상이고 j,k중 1장과 문양이 같은게 있을때 true
	}
	
	
	public boolean finish2()
	{
		finish = true;
		for (int i = 0; i < twocardlist.size(); i++)
		{
			for (int n = 0; n < twocardlist.size(); n++)
			{
				if (twocardlist.get(i) != twocardlist.get(n))
				{
					if (twocardlist.get(i).toCharArray()[0] == twocardlist.get(n).toCharArray()[0])
					{
						
						same.add(twocardlist.get(i).toCharArray()[0]);
						
					}
					
				}
			}
			
		}
		
		notsame = new ArrayList<Character>(new HashSet<Character>(same));
		
		if (notsame.size() < 1)
		{
			finish = false;
		}
		
		return finish;
		
		//j,k가 겹치는 문양이 하나도 없으면 false
		
	}
	
	
	public boolean finish3()
	{
		finish = false;
		int j = 0;
		int k = 0;
		
		for(int i = 0 ; i<twocardlist.size();i++){
			if(twocardlist.get(i).toCharArray()[1]==two[0]){
				k+=1;
			}else if(twocardlist.get(i).toCharArray()[1]==two[1])
			{
				j+=1;
			}
		}
		
		//jkjk방지 if문
		if(k==0||j==0){
		
		
			finish = true;
			
		
		}
		return finish;
		
	}
	public boolean finish31()
	{
		finish = true;

		
		if (twocardlist.get(0).toCharArray()[1] == two[0])
		{
			//남은것들이 k일때
			
		}
		else
		{
			//남은것들이 j일때
			finish = false;
			
		}
		
		return finish;
		
	}
	
	
	public boolean finish4()
	{
		finish = true;
		if (notsame.size() > 1)
		{
			finish = false;
		}
		
		return finish;
		//j,k가 겹치는 문양이 2개이상일시 false
		
	}
	
	
	public boolean finish5()
	{
		//남은문양이 필드카드와 겹치면안됨
		finish = true;
		if (userdeck.get(0).toCharArray()[0] == othercard.toCharArray()[0])
		{
			finish = false;
		}
		return finish;
		
	}
	
	
	public String finishAll(int what)
	{

		String comb = null;
		switch (what)
		{
			case 0:
				
				break;
			case 1:
				
				break;
			case 2:
				
				break;
			case 3:
				
				break;
			case 4:
				
				break;
			
			default:
				break;
		}
		return comb;
	}



	public void newpatten(){
		char [] grounds = groundcard.toCharArray();
		grounds[0] = pattens[0];
		
	}
	;
}
