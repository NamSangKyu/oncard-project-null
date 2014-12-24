package com.onecard;

import com.onecard.db.OnecardManager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint.Join;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

public class GameLogin extends Activity{
	
	ImageButton mBtnLogin, mBtnJoin, mBtnLoginExit;
	private SoundManager soundManager;
	private MediaManager mediaManager;

	public static  OnecardManager mOnemgr;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// 풀스크린 모드
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.login_main);
		
		mBtnLogin = (ImageButton) findViewById(R.id.btnLogin);
		mBtnJoin = (ImageButton) findViewById(R.id.btnJoin);
		mBtnLoginExit = (ImageButton) findViewById(R.id.btnLoginExit);
		
		soundManager = SoundManager.getInstance();
		mediaManager = MediaManager.getInstance();
		
		soundManager.init(getApplicationContext());
		mediaManager.init(getApplicationContext());
		
		mediaManager.play(0);
		
		mBtnLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				soundManager.play(0);
				startActivity(new Intent(GameLogin.this, LoginActivity.class));
			}
		}); // mBtnLogin
		
		mBtnJoin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				soundManager.play(0);
				startActivity(new Intent(GameLogin.this, GameJoin.class));
			}
		});
		
		mBtnLoginExit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				soundManager.play(0);
				mediaManager.stop();
				mOnemgr.closeDatabase();
				moveTaskToBack(true);
				android.os.Process.killProcess(android.os.Process.myPid());
				
			}
		});
		
		
		
	} // end of onCreate()
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		moveTaskToBack(true);
		android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	@Override
	protected void onResume() {
		super.onResume();

		// DB 생성/오픈
		mOnemgr = OnecardManager.getInstance(GameLogin.this);
		mOnemgr.openDatabase();

	} // end of onResume()
    
	@Override
	protected void onPause() {
		super.onPause();

		// DB 닫기
		
	} // end of onPause()
	
} // end of class GameLogin
