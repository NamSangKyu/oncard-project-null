package com.onecard;


import java.util.ArrayList;

import com.onecard.db.OnecardDb.OnecardDbEntry;

import android.R.array;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class RankActivity extends Activity {

	ImageView mRankOk;
	TextView mRname1,mRname2,mRname3,mRname4,mRname5,mRname6;
	TextView mRscore1,mRscore2,mRscore3,mRscore4,mRscore5,mRscore6;
	ArrayList<String> mNameList,mScoreList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_ranking);
		mRankOk=(ImageView)findViewById(R.id.imgRankOk);
		mRname1=(TextView)findViewById(R.id.RankName1);
		mRname2=(TextView)findViewById(R.id.RankName2);
		mRname3=(TextView)findViewById(R.id.RankName3);
		mRname4=(TextView)findViewById(R.id.RankName4);
		mRname5=(TextView)findViewById(R.id.RankName5);
		mRname6=(TextView)findViewById(R.id.RankName6);
		mRscore1=(TextView)findViewById(R.id.RankScore1);
		mRscore2=(TextView)findViewById(R.id.RankScore2);
		mRscore3=(TextView)findViewById(R.id.RankScore3);
		mRscore4=(TextView)findViewById(R.id.RankScore4);
		mRscore5=(TextView)findViewById(R.id.RankScore5);
		mRscore6=(TextView)findViewById(R.id.RankScore6);
		Cursor cursor = GameLogin.mOnemgr.viewrank();
		int n=0;
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			n++;
			switch (n) {
			case 1:
				mRname1.setText(cursor.getString(cursor.getColumnIndex("nickname")));
				mRscore1.setText(cursor.getString(cursor.getColumnIndex("score")));
				break;
			case 2:
				mRname2.setText(cursor.getString(cursor.getColumnIndex("nickname")));
				mRscore2.setText(cursor.getString(cursor.getColumnIndex("score")));
				break;
			case 3:
				mRname3.setText(cursor.getString(cursor.getColumnIndex("nickname")));
				mRscore3.setText(cursor.getString(cursor.getColumnIndex("score")));
				break;
			case 4:
				mRname4.setText(cursor.getString(cursor.getColumnIndex("nickname")));
				mRscore4.setText(cursor.getString(cursor.getColumnIndex("score")));
				break;
			case 5:
				mRname5.setText(cursor.getString(cursor.getColumnIndex("nickname")));
				mRscore5.setText(cursor.getString(cursor.getColumnIndex("score")));
				break;
			case 6:
				mRname6.setText(cursor.getString(cursor.getColumnIndex("nickname")));
				mRscore6.setText(cursor.getString(cursor.getColumnIndex("score")));
				break;
			default:
				break;
			}
			cursor.moveToNext();
		}
		
		
		mRankOk.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
