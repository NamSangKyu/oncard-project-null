package com.onecard.db;


public class Userdata {

	
	private String mNick, mName,mPw,mVic,mScor;
	
	public Userdata(String nick,String name,String pw,String vic,String scor){
		mNick=nick;
		mName=name;
		mPw=pw;
		mVic=vic;
		mScor=scor;
		
	}
	//private static Userdata mInstance = null;
	
	//public static Userdata getInstance(String nick,String name,String pw,String vic,String scor){
	//	if (mInstance == null) {
	//		mInstance = new Userdata(nick, name, pw, vic, scor);
	//	}
	//	return mInstance;
	//}
	
	public String getNick() {
		return mNick;
	}

	public void setNick(String nick) {
		mNick = nick;
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}

	public String getPw() {
		return mPw;
	}

	public void setPw(String pw) {
		mPw = pw;
	}

	public String getVic() {
		return mVic;
	}

	public void setVic(String vic) {
		mVic = vic;
	}

	public String getScor() {
		return mScor;
	}

	public void setScor(String scor) {
		mScor = scor;
	}

	@Override
	public String toString() {
		return "현 유저의 정보 [닉네임=" + mNick + ", 이름=" + mName + ", 비밀번호=" + mPw
				+ ", 최고연승=" + mVic + ", 최고승리=" + mScor + "]";
	}
	
}
