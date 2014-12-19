package com.onecard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

public class GameMain extends Activity {
	private ImageButton btnStart, btnRank, btnOption, btnExit;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 풀스크린 모드
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
				
		setContentView(R.layout.activity_gamemain);
		btnStart = (ImageButton) findViewById(R.id.btnGameStart);
		btnRank = (ImageButton) findViewById(R.id.btnRank);
		btnOption = (ImageButton) findViewById(R.id.btnOption);
		btnExit = (ImageButton) findViewById(R.id.btnExit);
		//게임 시작 부분
		btnStart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(GameMain.this,GameStart.class));
			}
		});
		//게임 랭크 확인 부분
		btnRank.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		//게임 옵션 시작 부분
		btnOption.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		//게임 종료 부분
		btnExit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				moveTaskToBack(true);
				android.os.Process.killProcess(android.os.Process.myPid());
			}
		});
	}
	
}
