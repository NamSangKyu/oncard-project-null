package com.onecard.controll;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
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
	
	//private GameControll gameCtl;				// GameControll 객체
	
	private Resources res;						// Resources 객체
	private BitmapDrawable bd;					// BitmapDrawable 객체
	static Bitmap imgBackground;				// 배경 비트맵
	static Bitmap orgCard;						// 카드 원본
	// 
	Bitmap card[] = new Bitmap[54];				// 카드를 저장할 비트맵 배열
	String cardName[][] = new String[54][2];	// 카드 이름과 비트맵 인덱스를 저장하는 2차원배열
	static int cw;								// 카드 가로길이
	static int ch;								// 카드 세로길이
	
	static int mWidth, mHeight;					// 스크린 가로 세로
	
	private boolean mTurn;						// 턴방향(오른쪽 : true, 왼쪽 : false)
	private ArrayList<Player> playerList;		
	private Player player;
	private ArrayList<String> useDec;
	
	private int playerTurn;						// 플레이어 턴
	
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
		
		initGame();											// 게임환경 설정
		initCard();											// 게임환경에 맞도록 카드길이 설정
		
		setFocusable(true);
		
	}
	
	//----------------------------------------------
	// initCard() - 생성자에서 호출됨. 카드이미지 초기화
	//----------------------------------------------
	private void initCard() {
		bd = (BitmapDrawable) res.getDrawable(R.drawable._card);
		orgCard = bd.getBitmap();									// 카드 원본 비트맵 설정 (사진크기 : 202 * 282 )
		
		cw=1196/13*2;
		ch=641/5*2;
		
		// 클로버 카드
		int x=0;
		for(int i=0; i<13; i++) {
			card[i] = Bitmap.createBitmap(orgCard, x, 0, cw, ch);
			x += cw;
		}
		
		// 다이아 카드
		x=0;
		for(int i=0; i<13; i++) {
			card[i+13] = Bitmap.createBitmap(orgCard, x, ch, cw, ch);
			x += cw;
		}
		
		// 하트 카드
		x=0;
		for(int i=0; i<13; i++) {
			card[i+26] = Bitmap.createBitmap(orgCard, x, ch*2, cw, ch);
			x += cw;
		}
		
		// 스페이드 카드
		x=0;
		for(int i=0; i<13; i++) {
			card[i+39] = Bitmap.createBitmap(orgCard, x, ch*3, cw, ch);
			x += cw;
		}
		
		// 조커 카드
		x=0;
		for(int i=0; i<2; i++) {
			card[i+52] = Bitmap.createBitmap(orgCard, x, ch*4, cw, ch);
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
		
	}
	
	//-------------------------------------------------------
	// imageResize() - initCard()에서 호출됨. 이미지크기 재조정
	//-------------------------------------------------------
	private void imageResize() {
		// 19/4 
		// 29/6 
		int x = mWidth*4/19;				// 재조정할 가로길이 : 스크린 가로길이 * 4/19
		int y = mHeight*6/29;				// 재조정할 세로길이 : 스크린 세로길이 * 6/29
		
		for(int i=0; i<card.length; i++) {
			card[i] = Bitmap.createScaledBitmap(card[i], x, y, true);
		}
		
		cw = x;								// 변경된 카드 가로길이 저장
		ch = y;								// 변경된 카드 세로길이 저장
	}
	
	//---------------------------------------------------------
	// initGame() - 생성자에서 호출됨. 게임 환경 셋팅(배경, 스크린길이)
	//---------------------------------------------------------
	private void initGame() {
		// gameCtl = GameControll.getInstance();
		
		DisplayMetrics metrics = new DisplayMetrics();
		((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(metrics);

		mWidth = metrics.widthPixels;
		mHeight = metrics.heightPixels;
		
		// 비트맵 드로어블 생성
		bd = (BitmapDrawable) res.getDrawable(R.drawable.background);
		
		// 비트맵 드로어블 객체를 이용하여 비트맵 객체를 만듬
		imgBackground = bd.getBitmap();
	}
	
	
	//-------------------------------------------------------------
	// surfaceCreated() - SurfaceView가 생성되면 자동 호출(쓰레드 시작)
	//-------------------------------------------------------------
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			mThread.start();
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
	
	//---------------------------------
	// GameThread
	//---------------------------------
	class GameThread extends Thread {
		boolean canRun = true;
		boolean isWait = false;
		
		public GameThread(SurfaceHolder holder, Context context) {
			
		}
		
		//---------------------------------
		// DrawAll() - 게임화면 그리는 부분
		//---------------------------------
		public void DrawAll(Canvas canvas) {
			// 배경 그리기
			canvas.drawBitmap(imgBackground, null, new Rect(0,0, mWidth, mHeight), null);
			
			// 카드 그리기
			canvas.drawBitmap(card[0], 0, 0, null);
			canvas.drawBitmap(card[12], cw, 0, null);
			canvas.drawBitmap(card[53], cw*2, 0, null);
		}
		
		
		public void countTime() {
			
		}
		
		//---------------------------------
		// run() - Thread 본체
		//---------------------------------
		public void run() {
			Canvas canvas = null;
			while(canRun) {
				canvas = mHolder.lockCanvas();
				try {
					synchronized(mHolder) {
						
						// 플레이어 턴 검사
						// 중앙 내려진 카드 검사
						// 턴마다 시간간격
						// 시간 바
							
						DrawAll(canvas);
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

	
	
	

}
