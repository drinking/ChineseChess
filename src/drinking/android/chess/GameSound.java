package drinking.android.chess;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class GameSound {

	private SoundPool msoundpool = new SoundPool(5, AudioManager.STREAM_MUSIC,
			0);
	private int GO_ID;
	private int EAT_ID;

	GameSound(Context context) {
		msoundpool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		GO_ID = msoundpool.load(context, R.raw.go, 1);
		EAT_ID = msoundpool.load(context, R.raw.eat, 1);
	}

	public void sound_move() {
		msoundpool.play(GO_ID, 1, 1, 0, 0, 1);
	}

	public void sound_eat() {
		msoundpool.play(EAT_ID, 1, 1, 0, 0, 1);
	}
}
