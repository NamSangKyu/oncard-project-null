package com.onecard;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// 풀스크린 모드
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.login);
		
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
				startActivity(new Intent(GameLogin.this, GameMain.class));
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
				moveTaskToBack(true);
				android.os.Process.killProcess(android.os.Process.myPid());
				
			}
		});
		
	} // end of onCreate()
	
} // end of class GameLogin
