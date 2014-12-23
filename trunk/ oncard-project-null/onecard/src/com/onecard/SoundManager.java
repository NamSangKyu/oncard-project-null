package com.onecard;

import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
/**
 * 효과음 재생하는 컨트롤러
  * 0 - 버튼 클릭음
  * 1 - 카드 inout
  * 2 - 카드 섞는음
  * 3 - 폭발음
 *
 */
public class SoundManager {
	private static SoundManager instance;
	private SoundPool soundPool;
	private HashMap<Integer, Integer> soundPoolMap;
	private AudioManager audioManager;
	private Context context;
	
	private SoundManager() {
		super();
		// TODO Auto-generated constructor stub
	}

	public static SoundManager getInstance() {
		if(instance==null)
			instance = new SoundManager();
		return instance;
	}
	
	public void init(Context context){
		soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
		
		soundPoolMap = new HashMap<Integer, Integer>();
		audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
		this.context = context;
		initEffect();
	}
	
	private void initEffect() {
		// TODO Auto-generated method stub
		//버튼 클릭
		addSound(0, R.raw.btn);
		addSound(1, R.raw.cardinout);
		addSound(2, R.raw.cardsuffle);
		addSound(3, R.raw.boom);
	}
	
	public void addSound(int index, int soundId){
		int id = soundPool.load(context, soundId,1);
		soundPoolMap.put(index, id);
	}
	/**
	 * 
	 * @param index 재생할 음원 번호 
	 * 0 - 버튼 클릭음
	 * 1 - 카드 inout
	 * 2 - 카드 섞는음
	 * 3 - 폭발음
	 */
	public void play(int index){
		float streamVoulme = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		streamVoulme = streamVoulme / audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		soundPool.play(soundPoolMap.get(index), streamVoulme, streamVoulme, 0, 1, 1f);
	}
	
	public void playLooped(int index){
		float streamVoulme = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		streamVoulme = streamVoulme / audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		soundPool.play(soundPoolMap.get(index), streamVoulme, streamVoulme, 1, -1, 1f);
	}
	
	
}
