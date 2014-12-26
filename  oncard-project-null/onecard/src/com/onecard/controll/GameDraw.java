package com.onecard.controll;

import java.util.ArrayList;
import java.util.HashMap;
import com.onecard.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import com.onecard.GlobalVars;
import com.onecard.MediaManager;
import com.onecard.R;
import com.onecard.SoundManager;

public class GameDraw extends SurfaceView implements Callback, OnGestureListener {
	public static final String TAG = "MyLog";
	
	GameThread mThread;							// Thread 객체
	SurfaceHolder mHolder;						// SurfaceHolder
	static Context mContext;					// Context
	
	private GestureDetector mDetector;			// GestureDetector 객체
	private Resources res;						// Resources 객체
	private BitmapDrawable bd;					// BitmapDrawable 객체
	
	// Thread에서 canvas 작업에 필요한 변수들
	private boolean checkGame;					// 게임 승패 여부
	private boolean cardIn;						// 카드 먹을 때
	private boolean cardOut;					// 카드 낼 때
	private boolean AI_In;						// AI가 카드를 먹은 경우인지
	private boolean AI_Out;						// AI가 카드를 낸 경우인지
	private int playerTurn;						// 어떤 플레이어의 턴
	private int tmpPlayerTurn;					// (계산용) 임시 플레이어 턴 
	private boolean mTurn;						// 턴방향(오른쪽 : true, 왼쪽 : false)
	private int centerCardIdx;					// 중앙에 그릴 카드 인덱스
	private String centerCardName;				// 중앙에 그릴 카드 이름
	private int cntWinPlayer;					// 플레이어가 이긴 횟수
	private int cntWinLeft;
	private int cntWinRight;
	private int cntWinTop;
	private int playerNum;						// 플레이어 인원 수
	private int cardNumPlayer;					// 플레이어의 카드갯수
	private int cardNumLeft;					// 왼쪽 AI 카드갯수
	private int cardNumRight;					// 오른쪽 AI 카드갯수
	private int cardNumTop;						// 위쪽 AI 카드갯수
	private int cardMgnTop;						// 위쪽 플레이어의 뒤집어진 카드를 겹칠 간격
	private int cardMgnLeft;					// 왼쪽 플레이어의 뒤집어진 카드를 겹칠 간격
	private int cardMgnRight;					// 오른쪽 플레이어의 뒤집어진 카드를 겹칠 간격
	private int winImgMgnLeft;					// 승리횟수 이미지 marginLeft
	private int winImgMgnTop;					// 승리횟수 이미지 marginTop
	private int mgnCardNL;						// 카드와 발바닥 사이의 거리
	private int mgnCharLeft;					// 캐릭터를 그릴 위치 marginLeft
	private int mgnCharTop;						// 캐릭터를 그릴 위치 marginTop
	private int characterRad;					// 캐릭터의 지름
	private int forWin;							// 목표 승리 횟수
	private int upIndexNum;						// 카드가 소유한 카드에서 up한 인덱스(0~14)
	private int idx;							// 사용자가 up한 카드 실제 인덱스
	private String whoWin;						// 이긴 플레이어의 이름
	private int whoWinIdx;						// 이긴 플레이어의 번호
	
	// 카드를 그릴 위치 계산용 변수
	private int lengthPlayer;					// 플레이어 카드를 그릴 위치 계산용 전체길이 변수
	private int lengthTop;						// 위쪽 카드를 그릴 위치계산용 전체길이 변수
	private int lengthLeft;						// 왼쪽 카드를 그릴 위치계산용 전체길이 변수
	private int lengthRight;					// 오른쪽 카드를 그릴 위치계산용 전체길이 변수
	private int mgnPlayer;						// 플레이어를 그리기 시작할 위치계산용 margin 변수
	private int mgnTop;							// 뒷면카드를 그리기 시작할 위치계산용 margin 변수
	private int mgnLeft;
	private int mgnRight;
	
	static Bitmap imgBackground;				// 배경 비트맵
	static Bitmap playerCharacter;				// 플레이어 캐릭터
	static Bitmap leftCharacter;				// 왼쪽 AI 캐릭터
	static Bitmap rightCharacter;				// 오른쪽 AI 캐릭터
	static Bitmap topCharacter;					// 위쪽 AI 캐릭터
	static Bitmap leaveWin;						// 발바닥(남은 승리 수)
	static Bitmap card_org;						// 카드 원본
	static Bitmap card_back_right;				// 오른쪽 플레이어 카드 뒷면
	static Bitmap card_back_left;				// 왼쪽 플레이어 카드 뒷면		
	static Bitmap card_back_top;				// 위쪽 플레이어 카드 뒷면
	static Bitmap turn_left;					// 진행방향이 왼쪽
	static Bitmap turn_right;					// 진행방향이 오른쪽
	static Bitmap card_back;
	
	Bitmap AI_CurCard;							// (계산용) AI의 카드 애니메이션용
	Bitmap card[] = new Bitmap[54];				// 카드를 저장할 비트맵 배열
	Bitmap win[] = new Bitmap[6];				// 승리횟수 비트맵 배열
	String cardName[][] = new String[54][2];	// 카드 이름과 비트맵 인덱스를 저장하는 2차원배열
	
	static int cw;								// 카드 가로길이
	static int ch;								// 카드 세로길이
	static int cmargin;							// 플레이어 카드 margin
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
	private static MediaManager mediaManager;
	private static SoundManager soundManager;
	private ArrayList<Player> playerList;		// 플레이어 리스트를 가져올 리스트 객체
	private Player player;						// 플레이어(자신)의 정보를 가져올 Player 객체
	private ArrayList<String> dec;				// 플레이어(자신)이 가진 카드를 저장할 리스트
	private HashMap<String , Integer> map;		// 카드이름과 카드번호를 저장할 HashMap
	private Paint mPaint = new Paint();
	
	private int mx, my;							// 카드를 낼때 시작위치(플레이어)
	private int mxR, myR;
	private int mxT, myT;
	private int mxL, myL;
	private int centerX, centerY;				// 카드를 먹을 때 시작위치
	private int moveX, moveY;					// 카드가 이동할 단위
	private Bitmap cardInOut;
	
	ResultDialog dialog;
	
	//---------------------------------
	// 생성자
	//---------------------------------
	public GameDraw(Context context, AttributeSet attrs) {
		super(context, attrs);
		Log.d("MyLog", "재호출 시 생성자 호출되나?");
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
		
		mHolder = holder;
		mContext = context;
		mThread = new GameThread(holder, context);
		res = getResources();									// 리소스 가져오기
		map = new HashMap<String , Integer>();					// 해쉬맵 생성
		mDetector = new GestureDetector(getContext(), this);	// Detector 객체 생성
		
		gameControll = GameControll.getInstance(context);
		
		// 어플리케이션 데이터에 저장된 playerNum을 가져와서 실행한다. (디폴트 = 4)
		playerNum = ((GlobalVars) context.getApplicationContext()).getPlayerNum();
		if(playerNum == 2 || playerNum == 3 || playerNum == 4) {
			gameControll.start(playerNum);							// 설정된 게임 인원으로 시작
		} else {
			Log.d("MyLog", "playerNum : " + playerNum);
			gameControll.start(4);									// 디폴트 4명으로 시작
		}
		
		gameCurState = GameControll.getCurrentState();
		mediaManager = MediaManager.getInstance();
		soundManager = SoundManager.getInstance();
		mediaManager.init(mContext);
		soundManager.init(mContext);
		
		dialog = new ResultDialog(mContext);
		dialog.setCancelable(false);

		initGame();												// 게임환경 설정
		initBitmap();											// 게임환경에 맞도록 카드길이 설정
		
		setFocusable(true);
		
	} // 생성자
	
	
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
		
		// 플레이어의 캐릭터
		bd = (BitmapDrawable) res.getDrawable(R.drawable.c1);
		playerCharacter = bd.getBitmap();
		
		if(playerNum == 2) {
			// 위쪽 AI의 캐릭터
			bd = (BitmapDrawable) res.getDrawable(R.drawable.c4);
			topCharacter = bd.getBitmap();
		} else if(playerNum == 3) {
			// 왼쪽 AI의 캐릭터
			bd = (BitmapDrawable) res.getDrawable(R.drawable.c2);
			leftCharacter = bd.getBitmap();
			
			// 오른쪽 AI의 캐릭터
			bd = (BitmapDrawable) res.getDrawable(R.drawable.c3);
			rightCharacter = bd.getBitmap();
		} else if(playerNum == 4) {
			// 왼쪽 AI의 캐릭터
			bd = (BitmapDrawable) res.getDrawable(R.drawable.c2);
			leftCharacter = bd.getBitmap();
			
			// 오른쪽 AI의 캐릭터
			bd = (BitmapDrawable) res.getDrawable(R.drawable.c3);
			rightCharacter = bd.getBitmap();
			
			// 위쪽 AI의 캐릭터
			bd = (BitmapDrawable) res.getDrawable(R.drawable.c4);
			topCharacter = bd.getBitmap();
		}
		
		// 발바닥
		bd = (BitmapDrawable) res.getDrawable(R.drawable.b1);
		leaveWin = bd.getBitmap();
		
		// 승리 횟수 비트맵
		for(int i=0; i<win.length; i++) {
			bd = (BitmapDrawable) res.getDrawable(R.drawable.w0 + i);
			win[i] = bd.getBitmap();
		}
		
		// 원본 카드 이미지에서 카드조각을 잘라낼 길이 
		int cw2=card_org.getWidth()/13;														
		int ch2=card_org.getHeight()/5;
		
		// 클로버 카드
		int x=0;
		for(int i=0; i<13; i++) {
			card[i] = Bitmap.createBitmap(card_org, x, 0, cw2, ch2);
			x += cw2;
		}
		
		// 다이아 카드
		x=0;
		for(int i=0; i<13; i++) {
			card[i+13] = Bitmap.createBitmap(card_org, x, ch2, cw2, ch2);
			x += cw2;
		}
		
		// 하트 카드
		x=0;
		for(int i=0; i<13; i++) {
			card[i+26] = Bitmap.createBitmap(card_org, x, ch2*2, cw2, ch2);
			x += cw2;
		}
		
		// 스페이드 카드
		x=0;
		for(int i=0; i<13; i++) {
			card[i+39] = Bitmap.createBitmap(card_org, x, ch2*3, cw2, ch2);
			x += cw2;
		}
		
		// 조커 카드
		x=0;
		for(int i=0; i<3; i++) {
			if(i<2) {
				card[i+52] = Bitmap.createBitmap(card_org, x, ch2*4, cw2, ch2);
				x += cw2;
			} else { // 뒷면 카드
				card_back = Bitmap.createBitmap(card_org, x, ch2*4, cw2, ch2);
			}
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
		
		// 해쉬맵 초기화
		for(int i=0; i<card.length; i++) {
			map.put(cardName[i][0], i);
		}
		
		
		mgnCardNL = leaveWin.getWidth();						// 뒷면카드와 발바닥 사이의 거리
		mgnCharLeft = doubleToInt(leaveWin.getWidth()*1.5);		// 캐릭터의 marginLeft
		mgnCharTop = mgnCenterTop + leaveWin.getHeight()/2;		// 캐릭터의 marginTop
		
		
		// 스마트폰 가로, 세로 길이에 맞게 이미지 재조정
		imageResize();
		
	} // initBitmap()
	
	
	//-------------------------------------------------------
	// imageResize() - initCard()에서 호출됨. 이미지크기 재조정
	//-------------------------------------------------------
	private void imageResize() {
		// 19/4 스크린 가로 : mWidth
		// 29/6 스크린 세로 : mHeight
		
		// 변경된 길이로 카드 비트맵 재생성
		for(int i=0; i<card.length; i++) {
			card[i] = Bitmap.createScaledBitmap(card[i], cw, ch, true);		
		} // for
		
		// 변경된 길이로 카드 뒷면 재생성
		card_back = Bitmap.createScaledBitmap(card_back, cw, ch, true);
		
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
		
		mgnLeftTurnImg = (mWidth-tw)/2;					// 턴표시 이미지 왼쪽 여백
		mgnBotTurnImg = th;								// 턴표시 이미지 아래 여백
		
		// 캐릭터의 지름
		characterRad = tw/4;
		
		// 캐릭터 비트맵 사이즈 재조정
		playerCharacter = Bitmap.createScaledBitmap(playerCharacter, characterRad, characterRad, true);
		
		if(playerNum == 2) {
			topCharacter = Bitmap.createScaledBitmap(topCharacter, characterRad, characterRad, true);
		} else if(playerNum == 3) {
			leftCharacter = Bitmap.createScaledBitmap(leftCharacter, characterRad, characterRad, true);
			rightCharacter = Bitmap.createScaledBitmap(rightCharacter, characterRad, characterRad, true);
		} else if(playerNum == 4) {
			topCharacter = Bitmap.createScaledBitmap(topCharacter, characterRad, characterRad, true);
			leftCharacter = Bitmap.createScaledBitmap(leftCharacter, characterRad, characterRad, true);
			rightCharacter = Bitmap.createScaledBitmap(rightCharacter, characterRad, characterRad, true);
		} // if
		
		// 발바닥 사이즈 재조정
		leaveWin = Bitmap.createScaledBitmap(leaveWin, ch/6, ch/6, true);					// 발바닥 이미지 : 캐릭터 1/3
		
		// 승리 횟수 비트맵 사이즈 재조정
		for(int i=0; i<win.length; i++) {
			win[i] = Bitmap.createScaledBitmap(win[i], ch/4, ch/3, true);
		}
		
		winImgMgnLeft = mWidth - doubleToInt(win[0].getWidth()*1.1);						// 승리 횟수 이미지 margin값 설정
		winImgMgnTop = win[0].getWidth()/4;
		
	} // imageResize()
	
	
	//---------------------------------------------------------
	// initGame() - 생성자에서 호출됨. 게임 환경 셋팅(스크린 길이)
	//---------------------------------------------------------
	private void initGame() {
		playerList = gameCurState.getPlayerList();			// 플레이어 리스트를 가져온다.
		player = playerList.get(0);							// 플레이어 리스트에서 플레이어(자신)의 정보(Player 타입)를 가져옴.
		playerNum = playerList.size();						// 플레이어 인원 수 설정
		
		DisplayMetrics metrics = new DisplayMetrics();
		((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(metrics);
		mWidth = metrics.widthPixels;						// 스크린 가로길이
		mHeight = metrics.heightPixels;						// 스크린 세로길이
		
		cw = mWidth*4/19;									// 카드 가로길이(비율은 스크린 가로의 4/19가 적당했음)
		ch = mHeight*6/29;									// 카드 세로길이
		
		moveX = cw/2;										// 카드가 이동할 단위거리
		moveY = ch/2;										// 카드가 이동할 단위거리
		
		mgnCenterLeft = (mWidth-cw)/2;						// 중앙에 놓을 카드 왼쪽 여백 
		mgnCenterTop = (mHeight-ch)/3 + ch/4;				// 중앙에 놓을 카드 위쪽 여백
		
		mx = (mWidth - cw)/2;								// 카드 움직일 위치 초기 셋팅
		my = (mHeight - ch);								
		
		mxR = (mWidth - cw);								// 카드 움직일 위치 초기 셋팅(오른쪽)
		myR = mgnCenterTop;
		
		mxT = mx;											// 카드 움직일 위치 초기 셋팅(위쪽)
		myT = 0;
		
		mxL = 0;											// 카드 움직일 위치 초기 셋팅(왼쪽)
		myL = mgnCenterTop;
		
		centerX = mgnCenterLeft;							// 카드먹을때 움직일 위치 초기 셋팅(센터)
		centerY = mgnCenterTop;
		
		forWin = 2;											// 목표 승리 수 2회
		upIndexNum = 0;										// upIndexNum 초기값 0, - 1번째 카드;
		
		gameControll.nextTurn();							// 턴 초기값이 -1 이므로 플레이어 턴(0)으로 바꾼다.
		
		mPaint.setColor(Color.BLACK);						// 자신의 턴이 아닐때 비활성화 시킬 Paint 속성
		mPaint.setAlpha(150);
		
		soundManager.play(2);
		
	} // initGame()
	
	
	//-------------------------------------------------------------
	// surfaceCreated() - SurfaceView가 생성되면 자동 호출(쓰레드 시작)
	//-------------------------------------------------------------
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			mediaManager.play(1);
			mThread.start();							// surfaceView가 생성되면 쓰레드 시작
		} catch (Exception e) {
			RestartGame(); 
		}
	} // surfaceCreated()

	
	//-------------------------------------------------------------
	// surfaceChanged() - SurfaceView가 변경되면 자동 호출
	//-------------------------------------------------------------
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		
	} // surfaceChanged()

	//-------------------------------------------------------------
	// surfaceDestroyed() - SurfaceView가 제거되면 자동 호출(쓰레드 멈춤)
	//-------------------------------------------------------------
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		mThread.stopThread();  
		boolean done = true;
		while(done) {
			try {
				mThread.join();
				done = false;
				mediaManager.stop();
				
				// 사용한 비트맵 자원 클리어
				imgBackground.recycle();				// 배경 비트맵
				playerCharacter.recycle();				// 플레이어 캐릭터
				leftCharacter.recycle();				// 왼쪽 AI 캐릭터
				rightCharacter.recycle();				// 오른쪽 AI 캐릭터
				topCharacter.recycle();					// 위쪽 AI 캐릭터
				leaveWin.recycle();						// 발바닥(남은 승리 수)
				card_org.recycle();						// 카드 원본
				card_back_right.recycle();				// 오른쪽 플레이어 카드 뒷면
				card_back_left.recycle();				// 왼쪽 플레이어 카드 뒷면		
				card_back_top.recycle();				// 위쪽 플레이어 카드 뒷면
				turn_left.recycle();					// 진행방향이 왼쪽
				turn_right.recycle();					// 진행방향이 오른쪽
				card_back.recycle();
				cardInOut.recycle();
			} catch(Exception e) {
				
			}
		}
		Log.d("MyLog", "surfaceDestroyed");
		
	} // surfaceDestroyed()
	
	
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
	} // turnCheck()
	
	
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
		cntWinPlayer = player.getWin();								// 플레이어의 이긴 횟수 읽어오기
		
		if(playerNum == 2) {
			cntWinTop = playerList.get(1).getWin();
		} else if(playerNum == 3) {
			cntWinRight = playerList.get(1).getWin();
			cntWinLeft = playerList.get(2).getWin();
		} else if(playerNum == 4) {
			cntWinRight = playerList.get(1).getWin();
			cntWinTop = playerList.get(2).getWin();
			cntWinLeft = playerList.get(3).getWin();
		}
		
	} // winCheck()
	
	
	//----------------------------------------------------------------
	// decCheck() - run()에서 호출됨, (자신의 카드정보, AI의 카드갯수 알아오기)
	//----------------------------------------------------------------
	public void decCheck() {
		dec = player.getDec();										// 플레이어가 가진 카드정보 읽어오기
		cardNumPlayer = dec.size();									// 플레이어의 카드 갯수
		
		//upIndex = new boolean[cardNumPlayer];
		
		if(cardNumPlayer >= 11) {									// 플레이어 카드가 9장 이상이면
			cmargin = doubleToInt(cw*0.25);							// 간격 1/3
		} else {
			cmargin = doubleToInt(cw*0.4);							// 아니면 1/2
		}
		
		lengthPlayer = cw + cmargin*(cardNumPlayer-1);				// 플레이어의 카드 길이
		mgnPlayer = (mWidth - lengthPlayer)/2;						// 플레이어 카드 왼쪽 마진
		
		switch(playerNum) {											// 플레이어 인원에 따라서
		case 2:														// 인원이 2명이면
			cardNumTop = playerList.get(1).getDec().size();			// 위쪽 AI의 카드갯수를 읽어온다.
			
			if(cardNumTop >= 11) {									// 위쪽 플레이어 카드가 9장 이상이면
				cardMgnTop = doubleToInt(bw*0.25);					// 뒷면카드 간격을 줄임.
			} else {
				cardMgnTop = doubleToInt(bw*0.4);
			}
			
			lengthTop = cw + cardMgnTop*(cardNumTop-1);				// 위쪽에 그릴 카드 전체길이 구하기
			mgnTop = (mWidth - lengthTop)/2;						// 위쪽 카드를 그리기 시작할 위치
			
			break;
			
		case 3:														// 인원이 3명이면
			cardNumRight = playerList.get(1).getDec().size();		// Index 1번이 오른쪽 AI
			cardNumLeft = playerList.get(2).getDec().size();		// Index 2번이 왼쪽 AI
			
			if(cardNumRight >= 11) {								// 오른쪽 플레이어 카드가 11장 이상이면
				cardMgnRight = doubleToInt(bw*0.3);				// 뒷면카드 간격을 줄임
			} else {
				cardMgnRight = doubleToInt(bw*0.4);
			}
			
			if(cardNumLeft >= 11) {									// 왼쪽 플레이어 카드가 11장 이상이면
				cardMgnLeft = doubleToInt(bw*0.3);					// 뒷면카드 간격을 줄임
			} else {
				cardMgnLeft = doubleToInt(bw*0.4);
			} 

			lengthLeft = cw + cardMgnLeft*(cardNumLeft-1);			// 왼쪽에 그릴 카드 전체길이 구하기
			lengthRight = cw + cardMgnRight*(cardNumRight-1);		// 오른쪽에 그릴 카드 전체길이 구하기
			mgnLeft = (mHeight - lengthLeft)/2;						// 왼쪽 카드를 그리기 시작할 위치
			mgnRight = (mHeight - lengthRight)/2;					// 오른쪽 카드를 그리기 시작할 위치
			break;
			
		case 4:														// 인원이 4명이면
			cardNumRight = playerList.get(1).getDec().size();		// Index 1번이 오른쪽 AI
			cardNumTop = playerList.get(2).getDec().size();			// Index 2번이 위쪽 AI
			cardNumLeft = playerList.get(3).getDec().size();		// Index 3번이 왼쪽 AI
			
			if(cardNumTop >= 10) {									// 위쪽 플레이어 카드가 9장 이상이면
				cardMgnTop = doubleToInt(bw*0.25);					// 뒷면카드 간격을 줄임.
			} else {
				cardMgnTop = doubleToInt(bw*0.4);
			}
			
			if(cardNumLeft >= 11) {									// 왼쪽 플레이어 카드가 11장 이상이면
				cardMgnLeft = doubleToInt(bw*0.25);					// 뒷면카드 간격을 줄임
			} else {
				cardMgnLeft = doubleToInt(bw*0.4);
			} 

			if(cardNumRight >= 11) {								// 오른쪽 플레이어 카드가 11장 이상이면
				cardMgnRight = doubleToInt(bw*0.25);					// 뒷면카드 간격을 줄임
			} else {
				cardMgnRight = doubleToInt(bw*0.4);
			}
			
			lengthTop = bw + cardMgnTop*(cardNumTop-1);					// 위쪽에 그릴 카드 전체길이 구하기
			lengthLeft = bw + cardMgnLeft*(cardNumLeft-1);				// 왼쪽에 그릴 카드 전체길이 구하기
			lengthRight = bw + cardMgnRight*(cardNumRight-1);				// 오른쪽에 그릴 카드 전체길이 구하기
			mgnTop = (mWidth - lengthTop)/2;							// 위쪽 카드를 그리기 시작할 위치
			mgnLeft = (mHeight - ch - lengthLeft)/2;					// 왼쪽 카드를 그리기 시작할 위치
			mgnRight = (mHeight - ch - lengthRight)/2;					// 오른쪽 카드를 그리기 시작할 위치
			
			break;
		} // switch
		
		checkGame = gameCurState.checkGame();							// 게임 승패 확인
		
	} // decCheck()
	
	
	//---------------------------------------------
	// whoWin() - run()에서 호출됨. 이긴사람 정보 획득
	//---------------------------------------------
	public void whoWin() {
		gameControll.checkGame();
		winCheck();
		
		for(int i=0; i<playerNum; i++) {
			if(gameCurState.getGameResultList().get(i).getWin() != 0) {
				whoWin = gameCurState.getGameResultList().get(i).getName();
				whoWinIdx = i;
			} // if
		} // for
	} // whoWin()
	
	//---------------------------------
	// GameThread
	//---------------------------------
	class GameThread extends Thread {
		boolean canRun = true;
		boolean isWait = false;
		
		public GameThread(SurfaceHolder holder, Context context) {}
		
		//------------------------------------------------
		// DrawAll() - run()에서 호출, (게임화면 그리는 부분)
		//------------------------------------------------
		public void DrawAll(Canvas canvas) {
			if(canvas != null) {
				// 배경 그리기
				canvas.drawBitmap(imgBackground, null, new Rect(0,0, mWidth, mHeight), null);
				
				int mgnBotB = mHeight - ch - doubleToInt(leaveWin.getHeight()*2);							// 플레이어의 발바닥 계산 margin
				int mgnBotC = mHeight - ch - doubleToInt(leaveWin.getHeight()*2.5) - characterRad;			// 플레이어의 캐릭터 계산 margin
				// 플레이어 캐릭터 그리기
				canvas.drawBitmap(playerCharacter, (mWidth-(playerCharacter.getWidth()))/2, mgnBotC, null);	
				
				// 플레이어의 발바닥 그리기
				int mgnWidthPlayer = (mWidth-doubleToInt(leaveWin.getWidth()*(forWin-cntWinPlayer)))/2;			// 발바닥 전체 길이
				for(int i=0; i<forWin - cntWinPlayer; i++) {														// 발바닥 그리기
					canvas.drawBitmap(leaveWin, mgnWidthPlayer, mgnBotB, null);
					mgnWidthPlayer += leaveWin.getWidth();
				} // for
				
				int mgn = mgnPlayer;
				// 플레이어의 카드 그리기(앞면 카드)
				for(int i=0; i<dec.size(); i++) {
					String cName = dec.get(i);
					
					for(int j=0; j<card.length; j++) {
						if(cardName[j][0].equals(cName)) {
							idx = j;
						}
					}
					
					if(upIndexNum == i && playerTurn == 0) {
						canvas.drawBitmap(card[idx], mgn, mHeight-ch, null);
					} else {
						canvas.drawBitmap(card[idx], mgn, mHeight-(ch-ch/4), null);
					}
					mgn += cmargin;
				} // for
				
				
				
				// AI가 소유한 카드 그리기 & 캐릭터 그리기 & 발바닥 그리기
				if(playerNum == 2) {																		// 2인용이면
					// 카드갯수를 가져와서 나열할 길이를 계산하여 적절한 위치에 겹쳐 그린다.
					// 뒷면카드를 전부 그렸을때 나오는 길이 double -> int 
					mgn = mgnTop;
					// 위쪽 AI 카드 그리기
					for(int i=0; i<cardNumTop; i++) {
						canvas.drawBitmap(card_back_top, mgn, 0, null);
						mgn += cardMgnTop;
					} // for
					
					// 캐릭터 그리기
					canvas.drawBitmap(topCharacter, (mWidth-topCharacter.getWidth())/2, mgnCharLeft, null);
					
					// 발바닥 그리기
					int mgnWidthTop = (mWidth-doubleToInt(leaveWin.getWidth()*(forWin-cntWinTop)))/2;				// 발바닥 전체 길이
					for(int i=0; i<forWin - cntWinTop; i++) {												// 발바닥 그리기
						canvas.drawBitmap(leaveWin, mgnWidthTop, mgnCardNL, null);
						mgnWidthTop += leaveWin.getWidth();
					} // for
						
					// 비활성화 표현 그리기
					if(playerTurn == 0) {	// 플레이어 턴이면
						canvas.drawRect(mgnTop, 0, mgnTop+lengthTop, bh, mPaint);																	// 위쪽 카드 어둡게
						canvas.drawCircle((mWidth-playerCharacter.getWidth())/2 + characterRad/2, mgnCharLeft+characterRad/2, characterRad/2, mPaint); // 위쪽 캐릭터 어둡게
					
					} else if(playerTurn == 1) {	// 위쪽 AI 턴이면
						canvas.drawRect(mgnPlayer, mHeight-(ch-ch/4), mgnPlayer+lengthPlayer, mHeight, mPaint);										// 플레이어 카드 어둡게
						canvas.drawCircle((mWidth-(playerCharacter.getWidth()))/2 + characterRad/2, mgnBotC+characterRad/2, characterRad/2, mPaint);	// 플레이어 캐릭터 어둡게
					
					} 
				} else if(playerNum == 3) {																	// 3인용이면
					mgn = mgnLeft;

					// 왼쪽 AI 카드 그리기
					for(int i=0; i<cardNumLeft; i++) {
						canvas.drawBitmap(card_back_left, 0, mgn, null);
						mgn += cardMgnLeft;
					} // for
					
					mgn = mgnRight;
					// 오른쪽 AI 카드 그리기
					for(int i=0; i<cardNumRight; i++) {
						canvas.drawBitmap(card_back_right, mWidth-bh, mgn, null);
						mgn += cardMgnRight;
					} // for
					
					// 캐릭터 그리기
					canvas.drawBitmap(leftCharacter, mgnCharLeft, mgnCharTop, null);
					canvas.drawBitmap(rightCharacter, mWidth - mgnCharLeft - characterRad, mgnCharTop, null);
					
					// 발바닥 그리기
					int mgnHeightLeft = (mHeight - ch - doubleToInt(leaveWin.getHeight()*(forWin-cntWinLeft)))/2;		// 발바닥 전체 길이
					int mgnHeightRight = (mHeight - ch - doubleToInt(leaveWin.getHeight()*(forWin-cntWinRight)))/2;		// 발바닥 전체 길이
					
					// 왼쪽 발바닥
					for(int i=0; i<forWin - cntWinLeft; i++) {
						canvas.drawBitmap(leaveWin, mgnCardNL, mgnHeightLeft, null);
						mgnHeightLeft += leaveWin.getHeight();
					} // for
					
					// 오른쪽 발바닥
					for(int i=0; i<forWin - cntWinRight; i++) {
						canvas.drawBitmap(leaveWin, mWidth - mgnCardNL - leaveWin.getWidth(), mgnHeightRight, null);
						mgnHeightRight += leaveWin.getHeight();
					} // for
						
					// 비활성화 표현 그리기
					if(playerTurn == 0) {	// 플레이어 턴이면
						canvas.drawRect(0, mgnLeft, bh, mgnLeft+lengthLeft, mPaint);																// 왼쪽 카드 어둡게
						canvas.drawRect(mWidth-bh, mgnRight, mWidth, mgnRight+lengthRight, mPaint);													// 오른쪽 카드 어둡게
						canvas.drawCircle(mgnCharLeft+characterRad/2, mgnCharTop+characterRad/2, characterRad/2, mPaint);							// 왼쪽 캐릭터 어둡게
						canvas.drawCircle(mWidth-mgnCharLeft-characterRad/2, mgnCharTop+characterRad/2, characterRad/2, mPaint);					// 오른쪽 캐릭터 어둡게
					
					} else if(playerTurn == 1) {	// 오른쪽 AI 턴이면
						canvas.drawRect(mgnPlayer, mHeight-(ch-ch/4), mgnPlayer+lengthPlayer, mHeight, mPaint);										// 플레이어 카드 어둡게
						canvas.drawRect(0, mgnLeft, bh, mgnLeft+lengthLeft, mPaint);																// 왼쪽 카드 어둡게
						canvas.drawCircle((mWidth-(playerCharacter.getWidth()))/2 + characterRad/2, mgnBotC+characterRad/2, characterRad/2, mPaint);// 플레이어 캐릭터 어둡게
						canvas.drawCircle(mgnCharLeft+characterRad/2, mgnCharTop+characterRad/2, characterRad/2, mPaint);							// 왼쪽 캐릭터 어둡게
					
					} else if(playerTurn == 2) {	// 왼쪽 AI 턴이면
						canvas.drawRect(mgnPlayer, mHeight-(ch-ch/4), mgnPlayer+lengthPlayer, mHeight, mPaint);										// 플레이어 카드 어둡게
						canvas.drawRect(mWidth-bh, mgnRight, mWidth, mgnRight+lengthRight, mPaint);													// 오른쪽 카드 어둡게
						canvas.drawCircle((mWidth-(playerCharacter.getWidth()))/2 + characterRad/2, mgnBotC+characterRad/2, characterRad/2, mPaint);	// 플레이어 캐릭터 어둡게
						canvas.drawCircle(mWidth-mgnCharLeft-characterRad/2, mgnCharTop+characterRad/2, characterRad/2, mPaint);					// 오른쪽 캐릭터 어둡게
					}
					
				} else if(playerNum == 4) {																// 4인용이면
					mgn = mgnLeft;

					// 왼쪽 AI 카드 그리기
					for(int i=0; i<cardNumLeft; i++) {
						canvas.drawBitmap(card_back_left, 0, mgn, null);
						mgn += cardMgnLeft;
					} // for
					
					mgn = mgnRight;
					// 오른쪽 AI 카드 그리기
					for(int i=0; i<cardNumRight; i++) {
						canvas.drawBitmap(card_back_right, mWidth-bh, mgn, null);
						mgn += cardMgnRight;
					} // for
					
					mgn = mgnTop;
					// 위쪽 AI 카드 그리기
					for(int i=0; i<cardNumTop; i++) {
						canvas.drawBitmap(card_back_top, mgn, 0, null);
						mgn += cardMgnTop;
					} // for
					
					// 캐릭터 그리기
					canvas.drawBitmap(leftCharacter, mgnCharLeft, mgnCharTop, null);
					canvas.drawBitmap(rightCharacter, mWidth - mgnCharLeft - characterRad, mgnCharTop, null);
					canvas.drawBitmap(topCharacter, (mWidth-topCharacter.getWidth())/2, mgnCharLeft, null);
					
					// 발바닥 그리기
					int mgnHeightLeft = (mHeight - ch - doubleToInt(leaveWin.getHeight()*(forWin-cntWinLeft)))/2;			// 발바닥 전체 길이
					int mgnHeightRight = (mHeight - ch - doubleToInt(leaveWin.getHeight()*(forWin-cntWinRight)))/2;		// 발바닥 전체 길이
					int mgnWidthTop = (mWidth-doubleToInt(leaveWin.getWidth()*(forWin-cntWinTop)))/2;					// 발바닥 전체 길이
					
					// 왼쪽 발바닥
					for(int i=0; i<forWin - cntWinLeft; i++) {
						canvas.drawBitmap(leaveWin, mgnCardNL, mgnHeightLeft, null);
						mgnHeightLeft += leaveWin.getHeight();
					} // for
					
					// 오른쪽 발바닥
					for(int i=0; i<forWin - cntWinRight; i++) {
						canvas.drawBitmap(leaveWin, mWidth - mgnCardNL - leaveWin.getWidth(), mgnHeightRight, null);
						mgnHeightRight += leaveWin.getHeight();
					} // for
					
					// 위쪽 발바닥
					for(int i=0; i<forWin - cntWinTop; i++) {														// 발바닥 그리기
						canvas.drawBitmap(leaveWin, mgnWidthTop, mgnCardNL, null);
						mgnWidthTop += leaveWin.getWidth();
					} // for
					
					// 비활성화 표현 그리기
					if(playerTurn == 0) {	// 플레이어 턴이면
						canvas.drawRect(0, mgnLeft, bh, mgnLeft+lengthLeft, mPaint);																// 왼쪽 카드 어둡게
						canvas.drawRect(mWidth-bh, mgnRight, mWidth, mgnRight+lengthRight, mPaint);													// 오른쪽 카드 어둡게
						canvas.drawRect(mgnTop, 0, mgnTop+lengthTop, bh, mPaint);																	// 위쪽 카드 어둡게
						canvas.drawCircle(mgnCharLeft+characterRad/2, mgnCharTop+characterRad/2, characterRad/2, mPaint);							// 왼쪽 캐릭터 어둡게
						canvas.drawCircle(mWidth-mgnCharLeft-characterRad/2, mgnCharTop+characterRad/2, characterRad/2, mPaint);					// 오른쪽 캐릭터 어둡게
						canvas.drawCircle((mWidth-topCharacter.getWidth())/2 + characterRad/2, mgnCharLeft+characterRad/2, characterRad/2, mPaint); // 위쪽 캐릭터 어둡게
					
					} else if(playerTurn == 1) {	// 오른쪽 AI 턴이면
						canvas.drawRect(mgnPlayer, mHeight-(ch-ch/4), mgnPlayer+lengthPlayer, mHeight, mPaint);										// 플레이어 카드 어둡게
						canvas.drawRect(0, mgnLeft, bh, mgnLeft+lengthLeft, mPaint);																// 왼쪽 카드 어둡게
						canvas.drawRect(mgnTop, 0, mgnTop+lengthTop, bh, mPaint);																	// 위쪽 카드 어둡게
						canvas.drawCircle((mWidth-(topCharacter.getWidth()))/2 + characterRad/2, mgnBotC+characterRad/2, characterRad/2, mPaint);	// 플레이어 캐릭터 어둡게
						canvas.drawCircle(mgnCharLeft+characterRad/2, mgnCharTop+characterRad/2, characterRad/2, mPaint);							// 왼쪽 캐릭터 어둡게
						canvas.drawCircle((mWidth-topCharacter.getWidth())/2 + characterRad/2, mgnCharLeft+characterRad/2, characterRad/2, mPaint);	// 위쪽 캐릭터 어둡게
					
					} else if(playerTurn == 2) {	// 위쪽 AI 턴이면
						canvas.drawRect(mgnPlayer, mHeight-(ch-ch/4), mgnPlayer+lengthPlayer, mHeight, mPaint);										// 플레이어 카드 어둡게
						canvas.drawRect(0, mgnLeft, bh, mgnLeft+lengthLeft, mPaint);																// 왼쪽 카드 어둡게
						canvas.drawRect(mWidth-bh, mgnRight, mWidth, mgnRight+lengthRight, mPaint);													// 오른쪽 카드 어둡게
						canvas.drawCircle((mWidth-(topCharacter.getWidth()))/2 + characterRad/2, mgnBotC+characterRad/2, characterRad/2, mPaint);	// 플레이어 캐릭터 어둡게
						canvas.drawCircle(mWidth-mgnCharLeft-characterRad/2, mgnCharTop+characterRad/2, characterRad/2, mPaint);					// 오른쪽 캐릭터 어둡게
						canvas.drawCircle(mgnCharLeft+characterRad/2, mgnCharTop+characterRad/2, characterRad/2, mPaint);							// 왼쪽 캐릭터 어둡게
					
					} else if(playerTurn == 3) {	// 왼쪽 AI 턴이면
						canvas.drawRect(mgnPlayer, mHeight-(ch-ch/4), mgnPlayer+lengthPlayer, mHeight, mPaint);										// 플레이어 카드 어둡게
						canvas.drawRect(mWidth-bh, mgnRight, mWidth, mgnRight+lengthRight, mPaint);													// 오른쪽 카드 어둡게
						canvas.drawRect(mgnTop, 0, mgnTop+lengthTop, bh, mPaint);																	// 위쪽 카드 어둡게
						canvas.drawCircle((mWidth-(topCharacter.getWidth()))/2 + characterRad/2, mgnBotC+characterRad/2, characterRad/2, mPaint);	// 플레이어 캐릭터 어둡게
						canvas.drawCircle(mWidth-mgnCharLeft-characterRad/2, mgnCharTop+characterRad/2, characterRad/2, mPaint);					// 오른쪽 캐릭터 어둡게
						canvas.drawCircle((mWidth-topCharacter.getWidth())/2 + characterRad/2, mgnCharLeft+characterRad/2, characterRad/2, mPaint);	// 위쪽 캐릭터 어둡게
					}
				} // if(), AI와 관련된 그리기 끝
				
				// 연승 횟수 그리기
				canvas.drawBitmap(win[cntWinPlayer], winImgMgnLeft, winImgMgnTop, null);
				
				// 턴 표시 그리기
				if(mTurn) {
					// mTurn이 true이면 오른쪽방향
					canvas.drawBitmap(turn_right, mgnLeftTurnImg, mgnCenterTop-doubleToInt((mgnBotTurnImg*1.5)), null);
				} else {
					// mTurn이 false이면 왼쪽방향
					canvas.drawBitmap(turn_left, mgnLeftTurnImg, mgnCenterTop-doubleToInt((mgnBotTurnImg*1.5)), null);
				}
				
				// 카드 내보내는 애니메이션
				if(cardOut) {
					moveCardOut(canvas, cardInOut);
				}
				
				// 카드 먹는 애니메이션
				if(cardIn) {
					moveCardIn(canvas);
				}
				
				// AI가 카드를 먹는 애니메이션
				if(AI_In) {
					moveAI_In(canvas);
				}
				
				// AI가 카드를 내는 애니메이션
				if(AI_Out) {
					moveAI_Out(canvas);
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
						
						winCheck();					// 플레이어들이 이긴 횟수 검사
						turnCheck();				// 플레이어 턴 검사, 턴 방향 검사
						centerCardCheck();			// 중앙에 내려진 카드 검사
						decCheck();					// 플레이어들이 들고있는 카드 검사
					
						if(checkGame) {				// 게임의 승패 확인
							if(cntWinPlayer == forWin || cntWinLeft == forWin || cntWinRight == forWin || cntWinTop == forWin) {
								stopThread();
								dialog.show();
							} else {
								mediaManager.stop();
								whoWin();				// 누가 승리했는지 확인
								mediaManager.play((cntWinPlayer)%3+1);
								gameControll.restart();
							}
						}
						
						DrawAll(canvas);			// 전부 그리기
						
						
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
	
	//----------------------------------------------------------
	// AI_CardCheck() - onDown()에서 호출됨. AI가 낸 카드비트맵 저장
	//----------------------------------------------------------
	public void AI_CardCheck() {
		centerCardCheck();
		AI_CurCard = card[centerCardIdx];
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
			mDetector.onTouchEvent(event);
			return true;
	}


	@Override
	public boolean onDown(MotionEvent e) {
		// 플레이어 턴이 아니면 터치할때마다 AI턴을 실행한다.
		if(playerTurn != 0) {
			tmpPlayerTurn = playerTurn;
			int AI_CurCardNum = playerList.get(tmpPlayerTurn).getDec().size(); 	// 현재 카드수 저장
			int _attackCard = gameCurState.getAttackCard();						// 현재 저장된 공격카드 수
			
			gameControll.playAI();												// AI가 카드플레이를 하고
			
			if(AI_CurCardNum > playerList.get(tmpPlayerTurn).getDec().size()) {	// 카드갯수가 증가? 감소?
				// 카드수가 감소(카드를 냈을경우)
				AI_CardCheck();
				AI_Out = true;
				
			} else {
				// 카드수가 증가(카드를 먹었을 경우)
				AI_In = true;
				
				if(_attackCard > 0) {							// AI가 공격카드를 먹으면 폭발음 플레이
					soundManager.play(3);
				} 
				if(playerList.get(tmpPlayerTurn).isWorkout()) {	// AI가 파산되면 좌절음 플레이
					soundManager.play(4);		// 좌절음 플레이
				}
			}
			
			soundManager.play(1);				// 카드 이동음
			upIndexNum = 0;
		} // if(playerTurn)
		
		return false;
	} // onDown()


	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		
		// 왼쪽으로 이동 : +
		// 오른쪽으로 이동 : -
		// 아래로 이동하면 : +
		// 위로 이동하면 : -
		return false;
	}


	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			Log.d("MyLog", "onFling 호출, velocityY = " + velocityY);
			
			int move = (int) e1.getX() - (int) e2.getX();
			int inOut = (int) e1.getY() - (int) e2.getY();
			
			// 플레이어 카드 좌우로 선택
			if(Math.abs(move) >= mWidth/6 && playerTurn == 0) {				// 카드 좌우로 선택, 플레이어 턴일때만 카드터치 인식을 받겠다.
				if(move >= 0) {
					// 왼쪽으로 카드 선택
					if((upIndexNum != 0)) {
						upIndexNum--;
					}
				} else {
					// 오른쪽으로 카드 선택
					if((upIndexNum < cardNumPlayer-1)) {
						upIndexNum++;
					}
				} // if
			} // if
			
			// 플레이어가 카드를 내거나 먹거나 
			if(Math.abs(inOut) >= mHeight/6 && playerTurn == 0) {			// 플레이어 턴일때만 카드터치 인식을 받겠다.
				
					if(inOut >= 0) {
						// 카드 내기
						gameControll.cardInputOutput(false, upIndexNum);		// 카드를 내고, 사용자 소유의 카드 인덱스를 넘긴다. (0~14)
						if(cardNumPlayer > player.getDec().size()) {
							soundManager.play(1);
							
							String cardInOutName = gameCurState.getUseDec().get(0);
							for(int j=0; j<card.length; j++) {
								if(cardName[j][0].equals(cardInOutName)) {
									cardInOut = card[j];
									break;
								} // if
							} // for
							
							cardOut = true;
						} // if
						
//						if(gameCurState.getUseDec().get(0) == "H7" || 
//							gameCurState.getUseDec().get(0) == "S7" ||
//							gameCurState.getUseDec().get(0) == "D7" ||
//							gameCurState.getUseDec().get(0) == "C7" ) {
//							dialog.show();
//						}
						
						// up할 카드는 항상 0번째 카드로
						if(upIndexNum == player.getDec().size()) {
							upIndexNum = 0;
						}
						
					} else {
						int _attackCard = gameCurState.getAttackCard();		// 공격카드가 있나 확인
						if(_attackCard > 0) {								// 있으면
							//soundManager.play(3);		// 폭발음
						} else {
							soundManager.play(1);		// 일반 카드음
						}
						
						// 카드 먹기
						gameControll.cardInputOutput(true, 0);
						cardIn = true;
						if(playerList.get(0).isWorkout()) {
							Log.d("MyLog", "player workout");
							// 좌절음 플레이
							mediaManager.stop();
							soundManager.play(4);
						}
					}
					
					gameControll.nextTurn();									// 다음 턴으로 넘긴다.
					turnCheck();												// 플레이어 턴 재검사
					
			} // if
			
			Log.d("MyLog", "현재상황 : " + gameCurState.toString());
			
		return false;
	} // end of onFling()
	
	//-------------------------------------------------
	// moveCardOut() - DrawAll()에서 호출. 카드 내는 움직임
	//-------------------------------------------------
	public void moveCardOut(Canvas canvas, Bitmap cardInOut) {
		canvas.drawBitmap(cardInOut, mx, my, null);
		my -= moveY;
		
		// 중앙카드를 넘어가면 멈춤
		if(my <= mgnCenterTop) {
			cardOut = false;
			my = mHeight - ch;
		}
	} // moveCardOut()
	
	//-------------------------------------------------
	// moveCardIn() - DrawAll()에서 호출. 카드 먹는 움직임
	//-------------------------------------------------
	public void moveCardIn(Canvas canvas) {
		canvas.drawBitmap(card_back, centerX, centerY, null);
		centerY += moveY;
		
		// 자기 카드덱 위치를 넘어가면
		if(centerY >= mHeight-ch) {
			cardIn = false;
			centerY = mgnCenterTop;
		}
	} // moveCardIn()
	
	
	//------------------------------------------------------
	// moveAI_In() - DrawAll()에서 호출. AI가 카드를 먹는 움직임
	//------------------------------------------------------
	public void moveAI_In(Canvas canvas) {
		switch(playerNum) {
		case 2:
			if(tmpPlayerTurn == 1) { // 위쪽 AI
				canvas.drawBitmap(card_back, centerX, centerY, null);
				centerY -= moveY;
				
				// AI 카드덱 위치를 넘어가면
				if(centerY <= bh) {
					AI_In = false;
					centerY = mgnCenterTop;
					gameControll.nextTurn();
				} // if(centerY)
			} // if(tmpPlayerTurn)
			break;
			
		case 3:
			if(tmpPlayerTurn == 1) { // 오른쪽 AI
				canvas.drawBitmap(card_back, centerX, centerY, null);
				centerX += moveX;
				
				if(centerX >= bh) {
					AI_In = false;
					centerX = mgnCenterLeft;
					gameControll.nextTurn();
				} // if(centerX)
			} else if(tmpPlayerTurn == 2) { // 왼쪽 AI
				canvas.drawBitmap(card_back, centerX, centerY, null);
				centerX -= moveX;
				
				if(centerX <= bh) {
					AI_In = false;
					centerX = mgnCenterLeft;
					gameControll.nextTurn();
				} // if(centerX)
			} // if(tmpPlayerTurn)
			break;
			
		case 4:
			if(tmpPlayerTurn == 1) { // 오른쪽 AI
				canvas.drawBitmap(card_back, centerX, centerY, null);
				centerX += moveX;
				
				if(centerX >= mWidth-bh) {
					AI_In = false;
					centerX = mgnCenterLeft;
					gameControll.nextTurn();
				} // if(centerX)
			} else if(tmpPlayerTurn == 2) { // 위쪽 AI
				canvas.drawBitmap(card_back, centerX, centerY, null);
				centerY -= moveY;
				
				// AI 카드덱 위치를 넘어가면
				if(centerY <= bh) {
					AI_In = false;
					centerY = mgnCenterTop;
					gameControll.nextTurn();
				} // if(centerY)
			} else if(tmpPlayerTurn == 3) { // 왼쪽 AI
				canvas.drawBitmap(card_back, centerX, centerY, null);
				centerX -= moveX;
				
				if(centerX <= bh) {
					AI_In = false;
					centerX = mgnCenterLeft;
					gameControll.nextTurn();
				} // if(centerX)
			} // if(tmpPlayerTurn)
			break;
			
		} // switch(playerNum)
		
	} // moveAI_In()
	
	
	//-------------------------------------------------------
	// moveAI_Out() - DrawAll()에서 호출. AI가 카드를 내는 움직임
	//-------------------------------------------------------
	public void moveAI_Out(Canvas canvas) {
		switch(playerNum) {
		case 2:
			if(tmpPlayerTurn == 1) { // 위쪽 AI
				canvas.drawBitmap(AI_CurCard, mxT, myT, null);
				myT += moveY;
				
				if(myT >= mgnCenterTop) {
					AI_Out = false;
					myT = 0;
					gameControll.nextTurn();
				}// if(myT)
			} // if(tmpPlayerTurn)
			break;
			
		case 3:
			if(tmpPlayerTurn == 1) { // 오른쪽 AI
				canvas.drawBitmap(AI_CurCard, mxR, myR, null);
				mxR -= moveX;
				
				if(mxR <= mgnCenterLeft) {
					AI_Out = false;
					mxR = mWidth - cw;
					gameControll.nextTurn();
				} // if(mxR)
			} else if(tmpPlayerTurn == 2) { // 왼쪽 AI
				canvas.drawBitmap(AI_CurCard, mxL, myL, null);
				mxL += moveX;
				
				if(mxL >= mgnCenterTop) {
					AI_Out = false;
					mxL = 0;
					gameControll.nextTurn();
				} // if(mxL)
			} // if(tmpPlayerTurn)
			break;
			
		case 4:
			if(tmpPlayerTurn == 1) { // 오른쪽 AI
				canvas.drawBitmap(AI_CurCard, mxR, myR, null);
				mxR -= moveX;
				
				if(mxR <= mgnCenterLeft) {
					AI_Out = false;
					mxR = mWidth - cw;
					gameControll.nextTurn();
				} // if(mxR)
			} else if(tmpPlayerTurn == 2) { // 위쪽 AI
				canvas.drawBitmap(AI_CurCard, mxT, myT, null);
				myT += moveY;
				
				if(myT >= mgnCenterTop) {
					AI_Out = false;
					myT = 0;
					gameControll.nextTurn();
				}// if(myT)ll.nextTurn();
			} else if(tmpPlayerTurn == 3) { // 왼쪽 AI
				canvas.drawBitmap(AI_CurCard, mxL, myL, null);
				mxL += moveX;
				
				if(mxL >= mgnCenterTop) {
					AI_Out = false;
					mxL = 0;
					gameControll.nextTurn();
				} // if(mxL)ntroll.nextTurn();
			} // if(tmpPlayerTurn)
			break;
			
		} // switch
	} // moveAI_Out()
	
} // end of class GameDraw


/* <남은 작업들>
 * 결과창
 * 컴퓨터 애니메이션
 */


