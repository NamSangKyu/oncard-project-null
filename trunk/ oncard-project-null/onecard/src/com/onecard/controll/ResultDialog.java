package com.onecard.controll;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

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
		if(gameResult.get(0).getWin() == 0) {
			setContentView(R.layout.result_fail);
		} else {
			setContentView(R.layout.result);
		}
		
		gameControll = GameControll.getInstance(context);
		
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
