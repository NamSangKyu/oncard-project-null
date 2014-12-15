package com.onecard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class GameMain extends Activity {
	private Button btnStart, btnRank, btnOption, btnExit;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gamemain);
		btnStart = (Button) findViewById(R.id.btnGameStart);
		btnRank = (Button) findViewById(R.id.btnRank);
		btnOption = (Button) findViewById(R.id.btnOption);
		btnExit = (Button) findViewById(R.id.btnExit);
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
