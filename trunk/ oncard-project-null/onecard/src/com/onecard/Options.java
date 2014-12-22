package com.onecard;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class Options extends Activity {
	
	RadioGroup radioGroup;
	Button mBtnConfirm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.options);
		
		radioGroup = (RadioGroup) findViewById(R.id.RadioGroup01);
		mBtnConfirm = (Button) findViewById(R.id.btnConfirm);
		
		mBtnConfirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.d("MyLog", "Confirm : ");
				setting();
				finish();
			}
		});
	} // end of onCreate()
	

	
	
	
    
	// 체크된 라디오 버튼 값 불러오기
	private void setting() {							
    	RadioButton tmpRadio;
    	int id;
    	
    	id = radioGroup.getCheckedRadioButtonId();
    	tmpRadio = (RadioButton) findViewById(id);
    	int playerNum = Integer.parseInt(tmpRadio.getTag().toString());
    	
    	((GlobalVars)getApplicationContext()).setPlayerNum(playerNum);
    	
    	Log.d("MyLog", "setting() : " + ((GlobalVars)getApplicationContext()).getPlayerNum());
    	
    }
	

} // end of class Options
