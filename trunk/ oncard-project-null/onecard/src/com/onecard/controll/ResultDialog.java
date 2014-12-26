package com.onecard.controll;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.onecard.GameMain;
import com.onecard.LoginActivity;
import com.onecard.R;
import com.onecard.db.OnecardManager;
import com.onecard.gameinterface.GameResult;

public class ResultDialog extends Activity{
	
	TextView mName, mScore, mRank;
	ImageButton mBtnResultOk;
	
	String winnerName;
	String winnerScore;
	String winnerRank;
	
	GameControll gameControll;
	GameCurrentState gameCurState;
	ArrayList<GameResult> gameResult;
	OnecardManager oneManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		gameControll = GameControll.getInstance(getApplicationContext());
		gameCurState = GameControll.getCurrentState();
		gameResult = gameCurState.getGameResultList();
		oneManager = OnecardManager.getInstance(getApplicationContext());
		
		if(gameResult.get(0).getWin() == 0) {
			Log.d("MyLog", "result dialog 패배");
			setContentView(R.layout.result_fail);			// 패배
		} else {
			Log.d("MyLog", "result dialog 승리");
			setContentView(R.layout.result);				// 승리
		}
		
		mName = (TextView) findViewById(R.id.vName);
		mScore = (TextView) findViewById(R.id.vScore);
		mRank = (TextView) findViewById(R.id.vRank);
		mBtnResultOk = (ImageButton) findViewById(R.id.btnResultOk);
		
		winnerName = LoginActivity.mUser.getNick();
		winnerScore = LoginActivity.mUser.getScor();
		winnerRank = oneManager.viewrank(winnerName);
		
		mName.setText(winnerName);
		mScore.setText(winnerScore);
		mRank.setText(winnerRank);
		
		mBtnResultOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(ResultDialog.this, GameMain.class));
			}
		});
		
	} // onCreate()
	
		
		
	

} // end of class GameResult
