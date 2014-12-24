package com.onecard;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class GameJoin extends Activity {

	ImageView mImgcancle, mImgJoinFinish;
	EditText mTextnick, mTextname, mTextpw, mTextpw2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 풀스크린 모드
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_join);

		mImgcancle = (ImageView) findViewById(R.id.imgJoinCancle);
		mImgJoinFinish = (ImageView) findViewById(R.id.imgJoinFinish);

		mTextnick = (EditText) findViewById(R.id.textjnick);
		mTextname = (EditText) findViewById(R.id.textjname);
		mTextpw = (EditText) findViewById(R.id.textjpw);
		mTextpw2 = (EditText) findViewById(R.id.textjpw2);

		mImgJoinFinish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (makeId()) {
					finish();
				}
			}
		});

		mImgcancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

	}// end of onCreate()

	private boolean makeId() {
		boolean temp = false;
		String nick = mTextnick.getText().toString();
		String name = mTextname.getText().toString();
		String pw = mTextpw.getText().toString();
		String pw2 = mTextpw2.getText().toString();
		if (nick.equals("")) {// 닉네임을입력햇는지 확인
			Toast.makeText(getApplicationContext(), "닉네임을 입력해주세요",
					Toast.LENGTH_SHORT).show();
		} else {

			Cursor cursor = GameLogin.mOnemgr.testnick(nick);

			if (cursor.getCount() != 0) {// 입력한 닉네임이 있는지 확인
				cursor.moveToFirst();
				String testnick = cursor.getString(0);

				if (nick.equals(testnick)) {// 입력한 닉네임과 가입되어있는 닉네임과 비교
					Toast.makeText(getApplicationContext(), "이미 중복된 아이디가있어요",
							Toast.LENGTH_SHORT).show();
				}
			} else if (name.equals("")) {// 이름칸이 비엇는지 확인
				Toast.makeText(getApplicationContext(), "이름을 입력해주세요",
						Toast.LENGTH_SHORT).show();
			} else if (pw.equals("")) {// 비밀번호칸이 비엇는지 확인
				Toast.makeText(getApplicationContext(), "비밀번호을 입력해주세요",
						Toast.LENGTH_SHORT).show();
			} else if (!pw.equals(pw2)) {
				Toast.makeText(getApplicationContext(), "비밀번호를 올바르게 적어주세요",
						Toast.LENGTH_SHORT).show();
			} else {// 정상이면 회원가입을 한다
				GameLogin.mOnemgr.insert(name, nick, pw);
				Toast.makeText(getApplicationContext(), "생성 완료",
						Toast.LENGTH_SHORT).show();

				temp = true;
			}
		}
		Log.d("login", String.valueOf(temp));
		return temp;
	}

} // end of class GameJoin

