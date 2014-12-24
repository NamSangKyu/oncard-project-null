package com.onecard.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.onecard.db.OnecardDb.OnecardDbEntry;

public class OnecardManager {
	private Context mContext;
	private static final String TAG = "MyLog";
	private OnecardManager(Context context) {
		mContext = context;
	}
	
	private static OnecardManager mInstance = null;
	
	public static OnecardManager getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new OnecardManager(context);
		}
		
		return mInstance;
	} // end of getInstance()
	
	private SQLiteDatabase mDb;
	private OnecardDbHelper mDbHelper;
	
	public SQLiteDatabase openDatabase() {
		
		mDbHelper = new OnecardDbHelper(mContext);
		mDb = mDbHelper.getWritableDatabase();
		
		return mDb;
	} // end of openDatabase()
	public void closeDatabase() {
		
		mDb.close();
		mDb = null;
	}
	public void insert(String name,String nick,String paswd) {
		//닉네임 ,패스워드,이름을 받아 db에 저장한다.
		String vic="0";
		String scor="0";
		String sql =
				"insert into " +OnecardDbEntry.TABLE_NAME
				+ "("
				+OnecardDbEntry.COL_NICK+ ", "
				+OnecardDbEntry.COL_NAME+ ", "
				+OnecardDbEntry.COL_PASSWD+ ", "
				+OnecardDbEntry.COL_VICTORY+ ", "
				+OnecardDbEntry.COL_SCORE
				+ ") "
				+ "values("
				+ "'" +nick+"', "
				+ "'" + name + "', "
				+ "'" + paswd + "', "
				+ "'" + vic + "', "
				+ "'" + scor + "'"
				+ ")";
		Log.d(TAG, sql);
		try {
			mDb.execSQL(sql);
			Log.d(TAG, "데이터 삽입 성공");
		} catch (SQLException e) {
			Log.d(TAG, "데이터 삽입 실패");
			e.printStackTrace();
		}
				
	}
	private static final String WHERE_CLAUSE = "nickname = ?";
	//public void  update(String nick,String vitory,String score){
	//	ContentValues values = new ContentValues();
		
		//values.put(OnecardDbEntry.COL_NICK, nick);
		//values.put(OnecardDbEntry.COL_VICTORY,vitory);
		//values.put(OnecardDbEntry.COL_SCORE,score);
		//String[] whereArgs= { nick };
		// mDb.update(OnecardDbEntry.TABLE_NAME,
		//		 values, WHERE_CLAUSE, whereArgs);
//
//	}

	public Cursor viewrank() {//랭킹조회 (최고점수로 알아봄)
		Cursor cursor = mDb
				.rawQuery(
						"select * from onecarduser order by score desc",
						null);

		return cursor;
	}

	public Cursor testpw(String nick) {//닉네임으로 비밀번호 조회
		String s= "select passwd from onecarduser where nickname="+"'"+nick+"'";
		Cursor c=mDb.rawQuery(s, null);

		return c;
	}
		
	public Cursor testnick(String nick) {//닉네임 조회
		String s= "select nickname from onecarduser where nickname="+"'"+nick+"'";
		Cursor c=mDb.rawQuery(s, null);

		return c;
	}	
	public Cursor testall(String nick){//닉네임으로 전부 알기
		String s="select * from onecarduser where nickname="+"'"+nick+"'";
		Cursor c=mDb.rawQuery(s,null);
		
		return c;
	}
		
	public void updatevic(String name,String victory){//연승 저장
		ContentValues values = new ContentValues();
		values.put(OnecardDbEntry.COL_VICTORY, victory);
		
		String[] whereArgs= { name };

		 mDb.update(OnecardDbEntry.TABLE_NAME,
				 values, WHERE_CLAUSE, whereArgs);
		
	}
	
	public void updatesco(String name, String score) {//스코어 저장
		ContentValues values = new ContentValues();
		values.put(OnecardDbEntry.COL_SCORE, score);

		String[] whereArgs = { name };

		mDb.update(OnecardDbEntry.TABLE_NAME, values, WHERE_CLAUSE, whereArgs);

	}		
	
}
