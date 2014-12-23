package com.onecard;

import java.util.HashMap;

import android.content.Context;
import android.media.MediaPlayer;

public class MediaManager {
	
	private static MediaManager instance = new MediaManager();

	private MediaPlayer mp;
	private Context context;
	private HashMap<Integer, Integer> hashMap;
	private MediaManager() {
		super();
		// TODO Auto-generated constructor stub
	}

	public static MediaManager getInstance() {
		if(instance == null)
			instance = new MediaManager();
		return instance;
	}	
	
	public void init(Context context){
		this.context = context;
		hashMap = new HashMap<Integer, Integer>();
		initBGM();
	}
	private void initBGM() {
		// TODO Auto-generated method stub
		addBGM(0, R.raw.intro);
		addBGM(1, R.raw.playing1);
		addBGM(2, R.raw.playing2);
		addBGM(3, R.raw.playing3);
		addBGM(4, R.raw.wait);
		addBGM(5, R.raw.gameover);
		addBGM(6, R.raw.win);
	}
/**
 * 게임 배경음 재생
 * @param index 재생할 트랙번호
 * 0 - intro
 * 1 - playing1
 * 2 - playing2
 * 3 - playing3
 * 4 - wait
 * 5 - gameover
 * 6 - win
 */
	public void play(int index){
		if(mp != null)
			mp.stop();
		mp = MediaPlayer.create(context, hashMap.get(index));
		mp.start();
	}
	public void addBGM(int index, int bgmId){
		hashMap.put(index, bgmId);
	}
	public void stop(){
		mp.stop();
	}
}
