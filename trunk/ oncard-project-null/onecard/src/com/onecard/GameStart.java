package com.onecard;

import com.onecard.controll.GameControll;
import com.onecard.controll.GameDraw;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;

public class GameStart extends Activity {
	private GameControll controll;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		controll = GameControll.getInstance();
		controll.start(4);
	}
	
	
	
}
