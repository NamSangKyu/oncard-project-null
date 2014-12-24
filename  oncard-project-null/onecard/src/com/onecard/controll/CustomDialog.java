package com.onecard.controll;

import com.onecard.R;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

public class CustomDialog extends Dialog implements android.view.View.OnClickListener{
	ImageButton imgH, imgC, imgD, imgS;
	
	public CustomDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.customdialog);
		imgH = (ImageButton) findViewById(R.id.imgH);
		imgC = (ImageButton) findViewById(R.id.imgC);
		imgD = (ImageButton) findViewById(R.id.imgD);
		imgS = (ImageButton) findViewById(R.id.imgS);
		
		imgH.setOnClickListener(this);
		imgC.setOnClickListener(this);
		imgD.setOnClickListener(this);
		imgS.setOnClickListener(this);
		
	}
	@Override
	public void onClick(View v) {
		
		switch(v.getId()){
		case R.id.imgC:
			GameControll.getInstance(getContext()).changePattern("C");
			break;
		case R.id.imgD:
			GameControll.getInstance(getContext()).changePattern("D");
			break;
		case R.id.imgH:
			GameControll.getInstance(getContext()).changePattern("H");
			break;
		case R.id.imgS:
			GameControll.getInstance(getContext()).changePattern("S");
			break;
		}
		dismiss();
	}

	
	

}
