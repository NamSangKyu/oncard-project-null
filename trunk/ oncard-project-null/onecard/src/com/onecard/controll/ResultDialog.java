package com.onecard.controll;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.onecard.GameMain;
import com.onecard.LoginActivity;
import com.onecard.R;
import com.onecard.db.OnecardManager;
import com.onecard.gameinterface.GameResult;

public class ResultDialog extends Dialog implements android.view.View.OnClickListener{
	
	TextView mName, mScore, mRank;
	ImageButton mBtnResultOk;
	
	String winnerName;
	String winnerScore;
	String winnerRank;
	
	GameControll gameControll;
	GameCurrentState gameCurState;
	ArrayList<GameResult> gameResult;
	OnecardManager oneManager;
	
	public ResultDialog(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		gameControll = GameControll.getInstance(context);
		gameCurState = GameControll.getCurrentState();
		gameResult = gameCurState.getGameResultList();
		oneManager = OnecardManager.getInstance(context);
		
		if(gameResult.get(0).getWin() == 0) {
			Toast.makeText(getContext(), "패배!", Toast.LENGTH_SHORT).show();
			Log.d("MyLog", "result dialog 패배");
			setContentView(R.layout.result_fail);			// 패배
		} else {
			Toast.makeText(getContext(), "승리!", Toast.LENGTH_SHORT).show();
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
		
		mBtnResultOk.setOnClickListener(this);
		
		
	} // 생성자

	
	@Override
	public void onClick(View v) {
		
		switch(v.getId()){
		case R.id.btnResultOk:
			getContext().startActivity(new Intent(getContext(), GameMain.class));
			break;
		
		}
		dismiss();
	} // onClick()
	
		
		
	

} // end of class GameResult
