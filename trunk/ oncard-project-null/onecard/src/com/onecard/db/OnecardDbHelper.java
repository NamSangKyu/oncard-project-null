package com.onecard.db;


import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.onecard.db.OnecardDb.OnecardDbEntry;



public class OnecardDbHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "onecard.db";
	private static final int DB_VERSION = 1;

	public OnecardDbHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}
	private static final String TAG = "MyLog";
	private static final String SQL_CREATE_TABLE =
			"create table " +OnecardDbEntry.TABLE_NAME
			+ " ("
			+OnecardDbEntry._ID+" integer primary key autoincrement, "
			+OnecardDbEntry.COL_NICK + " text, "
			+OnecardDbEntry.COL_NAME +  " text, "
			+OnecardDbEntry.COL_PASSWD +  " text, "
			+OnecardDbEntry.COL_VICTORY+ " text, "
			+OnecardDbEntry.COL_SCORE+" text"
			+")";
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		Log.d(TAG, "DbHelper onCreate()");
		
		// 테이블 생성
		Log.d(TAG, SQL_CREATE_TABLE);
		try {
			db.execSQL(SQL_CREATE_TABLE);
			Log.d(TAG, "테이블 생성 성공");
		} catch (SQLException e) {
			Log.d(TAG, "테이블 생성 실패");
			e.printStackTrace();
		}
	}
	private static final String SQL_DROP_TABLE =
			"drop table if exists " 
			+ OnecardDbEntry.TABLE_NAME;
	@Override
	public void onUpgrade(SQLiteDatabase db,
			int oldVersion, int newVersion) {
		Log.d(TAG, "DbHelper onUpgrade()");
		Log.d(TAG, "old: " + oldVersion
				+ ", new: " + newVersion);

		// DB 테이블 업그레이드
		try {
			Log.d(TAG, SQL_DROP_TABLE);
			// 기존 테이블 삭제
			db.execSQL(SQL_DROP_TABLE);
			
			Log.d(TAG, "테이블 삭제 성공");
			
			// 테이블 새로 생성
			onCreate(db);
		} catch (SQLException e) {
			Log.d(TAG, "테이블 삭제 실패");
			e.printStackTrace();
		}
		
	} // end of onUpgrade()

	@Override
	public void onDowngrade(SQLiteDatabase db,
			int oldVersion, int newVersion) {
		Log.d(TAG, "DbHelper onDowngrade()");
		Log.d(TAG, "old: " + oldVersion 
				+ ", new: " + newVersion);
		
		// DB 다운그레이드에서 필요한 코드 작성
		onUpgrade(db, oldVersion, newVersion);
	} 
	@Override
	public void onOpen(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		super.onOpen(db);
	}
}
