package drinking.android.chess;

import java.util.HashMap;
import java.util.Map.Entry;

import org.drinking.utils.SelectorUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

@SuppressLint("NewApi")
public class ChessActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.home_layout);
		intHomeView();
	}

	private View.OnClickListener buttonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(v.getId()==R.id.button1){
				Intent intent = new Intent(ChessActivity.this, GameActivity.class);
				startActivity(intent);
			}else{
				Intent intent = new Intent(ChessActivity.this, BluetoothGameActivity.class);
				startActivity(intent);
			}
		}
	};

	private void intHomeView() {
		int[] buttonid = new int[] { R.id.button1, R.id.button2, R.id.button3,
				R.id.button4 };
		HashMap<Enum, Drawable> drawables = (HashMap<Enum, Drawable>) SelectorUtils
				.getButtonHomeDrawable(this);
		for (Entry<Enum, Drawable> entry : drawables.entrySet()) {
			int index = entry.getKey().ordinal();
			View button = this.findViewById(buttonid[index]);
			button.setBackgroundDrawable(entry.getValue());
			button.setOnClickListener(buttonListener);
		}
	}

}
