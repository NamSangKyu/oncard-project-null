package com.onecard.controll;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import com.onecard.R;

public class GameDraw extends SurfaceView implements Callback {
	public static final String TAG = "MyLog";
	
	GameThread mThread;							// Thread 객체
	SurfaceHolder mHolder;						// SurfaceHolder
	static Context mContext;					// Context
	
	private Resources res;						// Resources 객체
	private BitmapDrawable bd;					// BitmapDrawable 객체
	private Matrix matrix;
	
	// Thread에서 canvas 작업에 필요한 변수들
	private boolean cardIn;						// 카드 먹을 때
	private boolean cardOut;					// 카드 낼 때
	private int playerTurn;						// 어떤 플레이어의 턴
	private boolean mTurn;						// 턴방향(오른쪽 : true, 왼쪽 : false)
	private int centerCardIdx;					// 중앙에 그릴 카드 인덱스
	private String centerCardName;				// 중앙에 그릴 카드 이름
	private int cntWin;							// 이긴 횟수
	private int playerNum;						// 플레이어 인원 수
	private int cardNumPlayer;					// 플레이어의 카드갯수
	private int cardNumLeft;					// 왼쪽 AI 카드갯수
	private int cardNumRight;					// 오른쪽 AI 카드갯수
	private int cardNumTop;						// 위쪽 AI 카드갯수
	private int cardMgnTop;						// 위쪽 플레이어의 뒤집어진 카드를 겹칠 간격
	private int cardMgnLeft;					// 왼쪽 플레이어의 뒤집어진 카드를 겹칠 간격
	private int cardMgnRight;					// 오른쪽 플레이어의 뒤집어진 카드를 겹칠 간격
	
	// 계산용 변수
	private int lengthTop;						// 위쪽 카드를 그릴 위치계산용 전체길이 변수
	private int lengthLeft;						// 왼쪽 카드를 그릴 위치계산용 전체길이 변수
	private int lengthRight;					// 오른쪽 카드를 그릴 위치계산용 전체길이 변수
	private int mgnTop;							// 카드를 그릴 위치계산용 margin 변수
	private int mgnLeft;
	private int mgnRight;
	
	static Bitmap imgBackground;				// 배경 비트맵
	static Bitmap card_org;						// 카드 원본
	static Bitmap card_back_right;				// 오른쪽 플레이어 카드 뒷면
	static Bitmap card_back_left;				// 왼쪽 플레이어 카드 뒷면		
	static Bitmap card_back_top;				// 위쪽 플레이어 카드 뒷면
	static Bitmap turn_left;					// 진행방향이 왼쪽
	static Bitmap turn_right;					// 진행방향이 오른쪽
	
	Bitmap card[] = new Bitmap[54];				// 카드를 저장할 비트맵 배열
	String cardName[][] = new String[54][2];	// 카드 이름과 비트맵 인덱스를 저장하는 2차원배열
	
	static int cw;								// 카드 가로길이
	static int ch;								// 카드 세로길이
	static int bw;								// 카드 뒷면 긴쪽길이
	static int bh;								// 카드 뒷면 짧은쪽 길이
	static int tw;								// 턴표시 가로길이
	static int th;								// 턴표시 세로길이
	static int mgnCenterLeft;					// 중앙에 놓여질 카드의 위치를 계산할 marginLeft 값
	static int mgnCenterTop;					// 중앙에 놓여질 카드의 위치를 계산할 marginTop 값
	static int mgnLeftTurnImg;					// 턴 표시 비트맵을 그릴 위치를 계산할 marginLeft 값
	static int mgnBotTurnImg;					// 턴 표시 비트맵을 그릴 위치를 계산할 marginBottom 값
	static int mWidth, mHeight;					// 스크린 가로 세로
	
	
	private static GameCurrentState gameCurState;
	private static GameControll gameControll;
	private ArrayList<Player> playerList;		// 플레이어 리스트를 가져올 리스트 객체
	private Player player;						// 플레이어(자신)의 정보를 가져올 Player 객체
	private ArrayList<String> useDec;			// 사용한  덱(중앙에 겹쳐진 카드 더미)
	private ArrayList<String> dec;				// 플레이어(자신)이 가진 카드를 저장할 리스트
	
	
	
	//---------------------------------
	// 생성자
	//---------------------------------
	public GameDraw(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
		
		mHolder = holder;
		mContext = context;
		mThread = new GameThread(holder, context);
		res = getResources();								// 리소스 가져오기
		matrix = new Matrix();
		gameControll = GameControll.getInstance();
		gameCurState = gameControll.getCurrentState();
		
		initGame();											// 게임환경 설정
		initBitmap();										// 게임환경에 맞도록 카드길이 설정
		
		setFocusable(true);
		
	}
	
	
	//-------------------------------------------------
	// initCard() - 생성자에서 호출됨. Bitmap 전체 초기화
	//-------------------------------------------------
	private void initBitmap() {
		// 배경화면 비트맵 설정
		bd = (BitmapDrawable) res.getDrawable(R.drawable.background);		
		imgBackground = bd.getBitmap();
				
		// 카드 원본 비트맵 설정
		bd = (BitmapDrawable) res.getDrawable(R.drawable._card);
		card_org = bd.getBitmap();											
		
		// 카드 뒷면 비트맵 설정(오른쪽 플레이어)
		bd = (BitmapDrawable) res.getDrawable(R.drawable.back_right);
		card_back_right = bd.getBitmap();									
		
		// 카드 뒷면 비트맵 설정(왼쪽 플레이어)
		bd = (BitmapDrawable) res.getDrawable(R.drawable.back_left);		
		card_back_left = bd.getBitmap();
		
		// 카드 뒷면 비트맵 설정(위쪽 플레이어)
		bd = (BitmapDrawable) res.getDrawable(R.drawable.back_top);			
		card_back_top = bd.getBitmap();
		
		// 진행방향 왼쪽 표시 비트맵
		bd = (BitmapDrawable) res.getDrawable(R.drawable.turn_left);
		turn_left = bd.getBitmap();
		
		// 진행방향 오른쪽 표시 비트맵
		bd = (BitmapDrawable) res.getDrawable(R.drawable.turn_right);
		turn_right = bd.getBitmap();
		
		cw=1196/13*2;			// 원본 카드 이미지에서 카드조각을 잘라낼 길이 
		ch=641/5*2;
		
		// 클로버 카드
		int x=0;
		for(int i=0; i<13; i++) {
			card[i] = Bitmap.createBitmap(card_org, x, 0, cw, ch);
			x += cw;
		}
		
		// 다이아 카드
		x=0;
		for(int i=0; i<13; i++) {
			card[i+13] = Bitmap.createBitmap(card_org, x, ch, cw, ch);
			x += cw;
		}
		
		// 하트 카드
		x=0;
		for(int i=0; i<13; i++) {
			card[i+26] = Bitmap.createBitmap(card_org, x, ch*2, cw, ch);
			x += cw;
		}
		
		// 스페이드 카드
		x=0;
		for(int i=0; i<13; i++) {
			card[i+39] = Bitmap.createBitmap(card_org, x, ch*3, cw, ch);
			x += cw;
		}
		
		// 조커 카드
		x=0;
		for(int i=0; i<2; i++) {
			card[i+52] = Bitmap.createBitmap(card_org, x, ch*4, cw, ch);
			x += cw;
		}
		
		// 클로버
		cardName[0][0] = "CA";
		cardName[1][0] = "C2";
		cardName[2][0] = "C3";
		cardName[3][0] = "C4";
		cardName[4][0] = "C5";
		cardName[5][0] = "C6";
		cardName[6][0] = "C7";
		cardName[7][0] = "C8";
		cardName[8][0] = "C9";
		cardName[9][0] = "C10";
		cardName[10][0] = "CK";
		cardName[11][0] = "CQ";
		cardName[12][0] = "CJ";
		
		// 다이아
		cardName[13][0] = "DA";
		cardName[14][0] = "D2";
		cardName[15][0] = "D3";
		cardName[16][0] = "D4";
		cardName[17][0] = "D5";
		cardName[18][0] = "D6";
		cardName[19][0] = "D7";
		cardName[20][0] = "D8";
		cardName[21][0] = "D9";
		cardName[22][0] = "D10";
		cardName[23][0] = "DK";
		cardName[24][0] = "DQ";
		cardName[25][0] = "DJ";
		
		// 하트
		cardName[26][0] = "HA";
		cardName[27][0] = "H2";
		cardName[28][0] = "H3";
		cardName[29][0] = "H4";
		cardName[30][0] = "H5";
		cardName[31][0] = "H6";
		cardName[32][0] = "H7";
		cardName[33][0] = "H8";
		cardName[34][0] = "H9";
		cardName[35][0] = "H10";
		cardName[36][0] = "HK";
		cardName[37][0] = "HQ";
		cardName[38][0] = "HJ";
		
		// 스페이드
		cardName[39][0] = "SA";
		cardName[40][0] = "S2";
		cardName[41][0] = "S3";
		cardName[42][0] = "S4";
		cardName[43][0] = "S5";
		cardName[44][0] = "S6";
		cardName[45][0] = "S7";
		cardName[46][0] = "S8";
		cardName[47][0] = "S9";
		cardName[48][0] = "S10";
		cardName[49][0] = "SK";
		cardName[50][0] = "SQ";
		cardName[51][0] = "SJ";
		
		// 조커
		cardName[52][0] = "JC";
		cardName[53][0] = "JB";
		
		// 카드 이미지와 이름 매치
		for(int i=0; i<card.length; i++) {
			cardName[i][1] = Integer.toString(i);
		}
		
		// 스마트폰 가로, 세로 길이에 맞게 이미지 재조정
		imageResize();
		
	} // initBitmap()
	
	//-------------------------------------------------------
	// imageResize() - initCard()에서 호출됨. 이미지크기 재조정
	//-------------------------------------------------------
	private void imageResize() {
		// 19/4 
		// 29/6 
		
		// 변경된 길이로 카드 비트맵 재생성
		for(int i=0; i<card.length; i++) {
			card[i] = Bitmap.createScaledBitmap(card[i], cw, ch, true);		
		}
		
		// 카드뒷면 사이즈 재조정
		bw = mHeight/7;
		bh = mWidth/13;
		
		// 카드뒷면 비트맵 재생성
		card_back_left = Bitmap.createScaledBitmap(card_back_left, bh, bw, true);
		card_back_right = Bitmap.createScaledBitmap(card_back_right, bh, bw, true);
		card_back_top = Bitmap.createScaledBitmap(card_back_top, bw, bh, true);
		
		// 턴표시 비트맵 사이즈 재조정 (가로 : 카드 가로길이의 2.5배, 세로 : 가로길이의 0.5배
		tw = doubleToInt(cw*2.5);			// double -> int
		th = doubleToInt(cw*0.5);			// double -> int
		turn_left = Bitmap.createScaledBitmap(turn_left, tw, th, true);
		turn_right = Bitmap.createScaledBitmap(turn_right, tw, th, true);
		
	} // imageResize()
	
	
	//---------------------------------------------------------
	// initGame() - 생성자에서 호출됨. 게임 환경 셋팅(스크린길이)
	//---------------------------------------------------------
	private void initGame() {
		DisplayMetrics metrics = new DisplayMetrics();
		((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(metrics);

		mWidth = metrics.widthPixels;						// 스크린 가로길이
		mHeight = metrics.heightPixels;						// 스크린 세로길이
		
		cw = mWidth*4/19;									// 카드 가로길이(비율은 스크린 가로의 4/19가 적당했음)
		ch = mHeight*6/29;									// 카드 세로길이
		
		mgnCenterLeft = (mWidth-cw)/2;						// 중앙에 놓을 카드 왼쪽 여백 
		mgnCenterTop = (mHeight-ch)/3;						// 중앙에 놓을 카드 위쪽 여백
		
		mgnLeftTurnImg = (mWidth-tw)/2;						// 턴표시 이미지 왼쪽 여백
		mgnBotTurnImg = th/2;								// 턴표시 이미지 아래 여백
		
		playerList = gameCurState.getPlayerList();			// 플레이어 리스트를 가져온다.
		player = playerList.get(0);							// 플레이어 리스트에서 플레이어(자신)의 정보(Player 타입)를 가져옴.
		
		playerNum = playerList.size();						// 플레이어 인원 수
		
		
		
	}
	
	
	//-------------------------------------------------------------
	// surfaceCreated() - SurfaceView가 생성되면 자동 호출(쓰레드 시작)
	//-------------------------------------------------------------
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			mThread.start();							// surfaceView가 생성되면 쓰레드 시작
		} catch (Exception e) {
			RestartGame(); 
		}
		
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		mThread.stopThread();
		Log.d("MyLog", "surfaceDestroyed");
		
	}
	
	//---------------------------------
	// RestartGame() - 쓰레드 정지
	//---------------------------------
	public void StopGame() {
		mThread.stopThread(); 
	}
	
	//---------------------------------
	// PauseGame() - 게임 일시정지
	//---------------------------------
	public void PauseGame() {
		mThread.pauseNResume(true); 
	}

	//---------------------------------
	// ResumeGame() - 게임 재개
	//---------------------------------
	public void ResumeGame() {
		mThread.pauseNResume(false); 
	}
	
	//---------------------------------
	// RestartGame() - 게임 다시시작
	//---------------------------------
	public void RestartGame() {
		mThread.stopThread();		
		
	    mThread = null;	  
		mThread = new GameThread(mHolder, mContext); 
		mThread.start(); 
	}
	
	//----------------------------------------------------
	// doubleToInt() - double형 변수를 int형으로 변환 후 리턴
	//----------------------------------------------------
	public int doubleToInt(double d) {
		int i = Integer.parseInt(String.valueOf(Math.round(d)));
		
		return i;
	}
	
	//---------------------------------------------------------------
	// turnCheck() - run()에서 호출됨, (누구의 턴인지 체크 & 턴 방향 체크)
	//---------------------------------------------------------------
	public void turnCheck() {
		playerTurn = gameCurState.getCurrentTurn();				// 누구의 턴인가(0 ~ 3)
		mTurn = gameCurState.isTurn();							// 오른쪽 : true, 왼쪽 : false
	}
	
	
	//----------------------------------------------------------------
	// centerCardDraw() - run()에서 호출됨, (중앙에 내려놓은 카드 검색)
	//----------------------------------------------------------------
	public void centerCardCheck() {
		// 내려놓은 카드이름 읽기
		centerCardName = gameCurState.getUseDec().get(0);
		
		// 카드이름으로 카드 인덱스 찾기
		for(int i=0; i<card.length; i++) {
			if(cardName[i][0].equals(centerCardName)) {
				centerCardIdx = Integer.parseInt(cardName[i][1]);
				break;
			}
		} // for
	} // centerCardCheck()
	
	
	//----------------------------------------------------------------
	// winCheck() - run()에서 호출됨, (이긴 횟수 알아오기)
	//----------------------------------------------------------------
	public void winCheck() {
		cntWin = player.getWin();								// 플레이어의 이긴 횟수 읽어오기
	}
	
	
	//----------------------------------------------------------------
	// decCheck() - run()에서 호출됨, (자신의 카드정보, AI의 카드갯수 알아오기)
	//----------------------------------------------------------------
	public void decCheck() {
		dec = player.getDec();									// 플레이어가 가진 카드정보 읽어오기
		cardNumPlayer = dec.size();								// 플레이어의 카드 갯수
		
		switch(playerNum) {
		case 2:													// 인원이 2명이면
			cardNumTop = playerList.get(1).getDec().size();		// 위쪽 AI의 카드갯수를 읽어온다.
			
			if(cardNumTop >= 9) {								// 위쪽 플레이어 카드가 9장 이상이면
				cardMgnTop = doubleToInt(bw*0.3);				// 뒷면카드 간격을 줄임.
			} else {
				cardMgnTop = doubleToInt(bw*0.5);
			}
			
			lengthTop = doubleToInt(cardNumTop*bw - ((cardMgnTop) * (cardNumTop-1)));		// 위쪽에 그릴 카드 전체길이 구하기
			mgnTop = (mWidth - lengthTop)/2;												// 위쪽 카드를 그리기 시작할 위치
			
			break;
			
		case 3:													// 인원이 3명이면
			cardNumLeft = playerList.get(1).getDec().size();	// Index 1번이 왼쪽 AI
			cardNumRight = playerList.get(2).getDec().size();	// Index 2번이 오른쪽 AI
			
			if(cardNumLeft >= 11) {								// 왼쪽 플레이어 카드가 11장 이상이면
				cardMgnLeft = doubleToInt(bw*0.3);				// 뒷면카드 간격을 줄임
			} else {
				cardMgnLeft = doubleToInt(bw*0.5);
			} 

			if(cardNumRight >= 11) {							// 오른쪽 플레이어 카드가 11장 이상이면
				cardMgnRight = doubleToInt(bw*0.3);				// 뒷면카드 간격을 줄임
			} else {
				cardMgnRight = doubleToInt(bw*0.5);
			}
			
			lengthLeft = doubleToInt(cardNumTop*bw - ((cardMgnLeft) * (cardNumTop-1)));			// 왼쪽에 그릴 카드 전체길이 구하기
			lengthRight = doubleToInt(cardNumTop*bw - ((cardMgnRight) * (cardNumTop-1)));		// 오른쪽에 그릴 카드 전체길이 구하기
			mgnLeft = (mHeight - lengthLeft)/2;													// 왼쪽 카드를 그리기 시작할 위치
			mgnRight = (mHeight - lengthRight)/2;												// 오른쪽 카드를 그리기 시작할 위치
			break;
			
		case 4:													// 인원이 4명이면
			cardNumLeft = playerList.get(1).getDec().size();	// Index 1번이 왼쪽 AI
			cardNumRight = playerList.get(2).getDec().size();	// Index 2번이 오른쪽 AI
			cardNumTop = playerList.get(3).getDec().size();		// Index 3번이 위쪽 AI
			
			if(cardNumTop >= 9) {								// 위쪽 플레이어 카드가 9장 이상이면
				cardMgnTop = doubleToInt(bw*0.3);				// 뒷면카드 간격을 줄임.
			} else {
				cardMgnTop = doubleToInt(bw*0.5);
			}
			
			if(cardNumLeft >= 11) {								// 왼쪽 플레이어 카드가 11장 이상이면
				cardMgnLeft = doubleToInt(bw*0.3);				// 뒷면카드 간격을 줄임
			} else {
				cardMgnLeft = doubleToInt(bw*0.5);
			} 

			if(cardNumRight >= 11) {							// 오른쪽 플레이어 카드가 11장 이상이면
				cardMgnRight = doubleToInt(bw*0.3);				// 뒷면카드 간격을 줄임
			} else {
				cardMgnRight = doubleToInt(bw*0.5);
			}
			
			lengthTop = doubleToInt(cardNumTop*bw - ((cardMgnTop) * (cardNumTop-1)));			// 위쪽에 그릴 카드 전체길이 구하기
			lengthLeft = doubleToInt(cardNumTop*bw - ((cardMgnLeft) * (cardNumTop-1)));			// 왼쪽에 그릴 카드 전체길이 구하기
			lengthRight = doubleToInt(cardNumTop*bw - ((cardMgnRight) * (cardNumTop-1)));		// 오른쪽에 그릴 카드 전체길이 구하기
			mgnTop = (mWidth - lengthTop)/2;													// 위쪽 카드를 그리기 시작할 위치
			mgnLeft = (mHeight - lengthLeft)/2;													// 왼쪽 카드를 그리기 시작할 위치
			mgnRight = (mHeight - lengthRight)/2;												// 오른쪽 카드를 그리기 시작할 위치
			
			break;
		} // switch
	} // decCheck()
	
	//---------------------------------
	// GameThread
	//---------------------------------
	class GameThread extends Thread {
		boolean canRun = true;
		boolean isWait = false;
		
		public GameThread(SurfaceHolder holder, Context context) {
			
		}
		
		//------------------------------------------------
		// DrawAll() - run()에서 호출, (게임화면 그리는 부분)
		//------------------------------------------------
		public void DrawAll(Canvas canvas) {
			if(canvas != null) {
				// 배경 그리기
				canvas.drawBitmap(imgBackground, null, new Rect(0,0, mWidth, mHeight), null);
				
				// 연승 횟수 그리기
				// cntWin에 따라 비트맵에서 해당숫자 가져오기
				
				// AI가 소유한 카드 그리기(뒷면 카드)
				if(playerNum == 2) {														// 2인용
					// 카드갯수를 가져와서 나열할 길이를 계산하여 적절한 위치에 겹쳐 그린다.
					// 뒷면카드를 전부 그렸을때 나오는 길이 double -> int 
					
					for(int i=0; i<cardNumTop; i++) {
						canvas.drawBitmap(card_back_top, mgnTop, 0, null);
						mgnTop += cardMgnTop;
					} // for
					
				} else if(playerNum == 3) {													// 3인용
					for(int i=0; i<cardNumLeft; i++) {
						canvas.drawBitmap(card_back_left, 0, mgnLeft, null);
						mgnLeft += cardMgnLeft;
					} // for
					
					for(int i=0; i<cardNumRight; i++) {
						canvas.drawBitmap(card_back_right, mWidth-bh, mgnRight, null);
						mgnRight += cardMgnRight;
					} // for
					
				} else if(playerNum == 4) {													// 4인용
					for(int i=0; i<cardNumTop; i++) {
						canvas.drawBitmap(card_back_top, mgnTop, 0, null);
						mgnTop += cardMgnTop;
					} // for
					
					for(int i=0; i<cardNumLeft; i++) {
						canvas.drawBitmap(card_back_left, 0, mgnLeft, null);
						mgnLeft += cardMgnLeft;
					} // for
					
					for(int i=0; i<cardNumRight; i++) {
						canvas.drawBitmap(card_back_right, mWidth-bh, mgnRight, null);
						mgnRight += cardMgnRight;
					} // for
				} // if
				
				// 플레이어의 카드 그리기(앞면 카드)
				// 
				
				// 턴 표시 그리기
				if(mTurn) {
					// mTurn이 true이면 오른쪽방향
					canvas.drawBitmap(turn_right, mgnLeftTurnImg, mgnCenterTop - mgnBotTurnImg - th, null);
				} else {
					// mTurn이 false이면 왼쪽방향
					canvas.drawBitmap(turn_left, mgnLeftTurnImg, mgnCenterTop - mgnBotTurnImg - th, null);
				}
				
				// 중앙카드 그리기
				canvas.drawBitmap(card[centerCardIdx], mgnCenterLeft, mgnCenterTop, null);
			} // if(canvas != null)
			
		} // DrawAll() 끝
		
		
		//---------------------------------
		// run() - Thread 본체
		//---------------------------------
		public void run() {
			Canvas canvas = null;
			while(canRun) {
				canvas = mHolder.lockCanvas();
				try {
					synchronized(mHolder) {
						
						// 턴마다 시간간격
						// 시간 바
						
						decCheck();						// 플레이어들이 들고있는 카드 검사
						winCheck();						// 자신이 이긴 횟수 검사
						turnCheck();					// 플레이어 턴 검사, 턴 방향 검사
						centerCardCheck();				// 중앙에 내려진 카드 검사
						DrawAll(canvas);				// 전부 그리기
						
					}
				} finally {
					if(canvas != null) {
						mHolder.unlockCanvasAndPost(canvas);
					}
				} // try
				
				synchronized (this) {
					if(isWait) 
						try {
							wait();
						} catch(Exception e) {}
				
				} // sync
			} // while
		}// run
		
		//------------------------------
		// stopThread() - 쓰레드 중지
		//------------------------------
		public void stopThread() {
			canRun = false;
			synchronized (this) {
				this.notify();
			}
		}
		
		//-------------------------------
		// pauseNResume() - 쓰레드 일시정지
		//-------------------------------
		public void pauseNResume(boolean wait) {
			isWait = wait;
			synchronized (this) {
				this.notify();
			}
		}
		
	} // GameThread 끝
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			Log.d(TAG, "DOWN : ("+event.getX()+", "+event.getY() +")");
			return true;
		}
		if(event.getAction() == MotionEvent.ACTION_MOVE){
			Log.d(TAG, "MOVE : ("+event.getX()+", "+event.getY() +")");
			invalidate();
			return true;
		}
		if(event.getAction() == MotionEvent.ACTION_UP){
			Log.d(TAG, "UP : ("+event.getX()+", "+event.getY() +")");
			return true;
			
		}
		return false;
	}

	
	
	

} // end of class GameDraw



