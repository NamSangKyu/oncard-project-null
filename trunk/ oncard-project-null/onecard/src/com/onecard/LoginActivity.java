package com.onecard;

import com.onecard.db.*;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class LoginActivity extends Activity {
	public static  Userdata mUser;
	EditText mEditnick,mEditPw;
	ImageView mLogin,mLoginCancle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		// 풀스크린 모드
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_login);
		 mUser=new Userdata(null, null, null, null, null);//데이터보관선언
		mEditnick=(EditText)findViewById(R.id.editNick);
		mEditPw=(EditText)findViewById(R.id.editPw);
		
		mLogin=(ImageView)findViewById(R.id.imgLogin);
		mLoginCancle=(ImageView)findViewById(R.id.imgLoginCancle);
		
		mLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Login();
			}
		});
		
		mLoginCancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				
			}
		});
		
	}

	private void Login(){
		String name = mEditnick.getText().toString();
		String pw = mEditPw.getText().toString();

		if (name.equals("")) {//닉네임을입력햇는지 확인
			Toast.makeText(getApplicationContext(), "닉네임을 입력해주세요",
					Toast.LENGTH_SHORT).show();
		} else {

			Cursor cursor = GameLogin.mOnemgr.testpw(name);
			if (cursor.getCount() != 0) {//입력한 닉네임이 있는지 확인
				cursor.moveToFirst();
				String testpw =cursor.getString(0);
		if (pw.equals(testpw)) {//가입된닉네임으로 비밀번호를 조회후 로그인
			Toast.makeText(getApplicationContext(), "성공", Toast.LENGTH_SHORT).show();
			Cursor allc= GameLogin.mOnemgr.testall(name);//성공후 닉네임으로 정보조회
			allc.moveToFirst();
			String nickdw = allc.getString(1);//닉네임
			String namedw = allc.getString(2);//이름
			String pwdw = allc.getString(3);//비밀번호
			String vicdw = allc.getString(4);//최고승리
			String scordw = allc.getString(5);//최고점수
			mUser.setNick(nickdw);
			mUser.setName(namedw);
			mUser.setPw(pwdw);
			mUser.setScor(scordw);
			mUser.setVic(vicdw);//전부 저장
			Intent intent = new Intent(getApplicationContext(),
					GameMain.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			
			startActivity(intent);
			//저장된거 확인.
		} else {
			Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();
				}
			}
			else{
				Toast.makeText(getApplicationContext(), "닉네임이 조회되지않아요.", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	
	
}





