package com.onecard;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class Options extends Activity {
	
	RadioGroup radioGroup;
	Intent resultIntent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.options);
		
		 resultIntent = new Intent();
		
		radioGroup = (RadioGroup) findViewById(R.id.RadioGroup01);
		
		
	} // end of onCreate()
	
	
	//-----------------------------------
    // Button Click
    //-----------------------------------
	Button.OnClickListener OnButtonClick = new Button.OnClickListener() {
		public void onClick(View v) {
			
			switch (v.getId()) {
			case R.id.btnExit :		
					setting();
					finish();
			
			} // switch
		}

    };
    
	// 체크된 라디오 버튼 값 불러오기
	private void setting() {							
    	RadioButton tmpRadio;
    	int id;
    	
    	id = radioGroup.getCheckedRadioButtonId();
    	tmpRadio = (RadioButton) findViewById(id);
    	int playerNum = Integer.parseInt(tmpRadio.getTag().toString());
    	
    	resultIntent.putExtra("playerNum", playerNum);
		
		setResult(RESULT_OK, resultIntent);
	
    }
	

} // end of class Options
