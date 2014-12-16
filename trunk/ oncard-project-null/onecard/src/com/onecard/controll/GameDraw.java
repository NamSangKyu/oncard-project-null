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
	
	static Bitmap imgBackground;				// 배경 비트맵
	static int mWidth, mHeight;					// 스크린 가로 세로
	
	private boolean mTurn;						// 턴방향(오른쪽 : true, 왼쪽 : false)
	private ArrayList<Player> playerList;		
	private Player player;
	private ArrayList<String> useDec;
	
	private int playerTurn;						// 플레이어 턴
	
	
	public GameDraw(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
		
		mHolder = holder;
		mContext = context;
		mThread = new GameThread(holder, context);
		
		initGame();
		
		setFocusable(true);
		
	}
	
	
	private void initGame() {
		// gameCtl = GameControll.getInstance();
		
		DisplayMetrics metrics = new DisplayMetrics();
		((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(metrics);

		mWidth = metrics.widthPixels;
		mHeight = metrics.heightPixels;
		
		// 리소스 가져오기
		Resources res = getResources();
		
		// 비트맵 드로어블 생성
		BitmapDrawable bd = (BitmapDrawable) res.getDrawable(R.drawable.background);
		
		// 비트맵 드로어블 객체를 이용하여 비트맵 객체를 만듬
		imgBackground = bd.getBitmap();
	}
	
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
