package com.onecard.controll;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class GameDraw extends View {
	public static final String TAG = "MyLog";
	
	public GameDraw(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		canvas.drawText("게임 플레이 화면", 100, 100, paint);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			Log.d(TAG, "DOWN : ("+event.getX()+", "+event.getY() +")");
			return true;
		}
		if(event.getAction() == MotionEvent.ACTION_MOVE){
			Log.d(TAG, "MOVE : ("+event.getX()+", "+event.getY() +")");
			invalidate();
			return true;
		}
		if(event.getAction() == MotionEvent.ACTION_UP){
			Log.d(TAG, "UP : ("+event.getX()+", "+event.getY() +")");
			return true;
			
		}
		return false;
	}
	
	

}
