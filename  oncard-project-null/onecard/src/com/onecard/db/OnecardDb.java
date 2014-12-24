package com.onecard.db;


import android.provider.BaseColumns;

public class OnecardDb {

	public static abstract class OnecardDbEntry implements
	BaseColumns {
		public static final String TABLE_NAME ="onecarduser";
		public static final String COL_NICK="nickname";
		public static final String COL_NAME="name";
		public static final String COL_PASSWD="passwd";
		public static final String COL_VICTORY="victory";
		public static final String COL_SCORE="score";
		
	}//end OnecardDbEntry
	
	private String mNickname;
	private String mName;
	private String mPasswd;
	private String mVictory;
	private String mScore;
	
	
	public OnecardDb(){}
	public OnecardDb(String nickname,String name,String passwd,String victory,String score,String rank){
		
		mNickname=nickname;
		mName=name;
		mPasswd=passwd;
		mVictory=victory;
		mScore=score;
		
		
	}
	public String getNickname() {
		return mNickname;
	}
	public void setNickname(String nickname) {
		mNickname = nickname;
	}
	
	public String getName() {
		return mName;
	}
	public void setName(String name) {
		mName = name;
	}
	public String getPasswd() {
		return mPasswd;
	}
	public void setPasswd(String passwd) {
		mPasswd = passwd;
	}
	public String getVictory() {
		return mVictory;
	}
	public void setVictory(String victory) {
		mVictory = victory;
	}
	public String getScore() {
		return mScore;
	}
	public void setScore(String score) {
		mScore = score;
	}
	
	
}//end OnecardDb
