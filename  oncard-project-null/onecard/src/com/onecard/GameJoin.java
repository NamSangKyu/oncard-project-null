package com.onecard;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

public class GameJoin extends Activity {

	ImageButton mBtnJoinConfirm, mBtnJoinExit;
	// EditText 정의
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// 풀스크린 모드
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.join);
		
		mBtnJoinConfirm = (ImageButton) findViewById(R.id.btnJoinConfirm);
		mBtnJoinExit = (ImageButton) findViewById(R.id.btnJoinExit);
		
		// EditText 연결
		
		mBtnJoinConfirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(confirm()) {				// confirm()이 true 이면 데이터베이스에 등록
					join();
				} 
				
				finish();
				
			}
		}); // mBtnJoinConfirm
		
		mBtnJoinExit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
	}// end of onCreate()
	
	//------------------------------------
	// 닉네임 중복 체크 & 비밀번호 재확인 체크
	//------------------------------------
	private boolean confirm() {
		boolean ok = false;
		String nickName="";
		
		/*
		 * ----- 닉네임 체크 ------
		*/
		
		// 닉네임이 사용가능하면 ok에 true
		ok = true;
		
		// 중복이면 토스트 메시지 띄우고 리턴
		if(!ok) {
			Toast.makeText(GameJoin.this, "닉네임 중복", Toast.LENGTH_LONG).show();
			return true;
		}
		
		
		/* 
		 * ----- 비밀번호 재확인 체크 ------
		*/
		
		
		// 비밀번호 재확인이 일치하지 않으면 ok에 false, 토스트 메시지 띄우고 리턴
		ok = false;
		
		if(!ok) {
			Toast.makeText(GameJoin.this, "비밀번호 불일치", Toast.LENGTH_LONG).show();
			return true;
		} else {
			Toast.makeText(GameJoin.this, "가입 완료\n" + "닉네임 : " + nickName, Toast.LENGTH_LONG).show();
		}
		
		return ok;
	} // confirm()
	
	//------------------------------------
	// 가입
	//------------------------------------
	private void join() {
		
	} // join()
	
	
} // end of class GameJoin






