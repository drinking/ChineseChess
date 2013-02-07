package drinking.android.chess;

import java.util.HashMap;
import java.util.Map.Entry;

import org.drinking.utils.SelectorUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import drinking.android.chess.WelcomeView.OnMessageSend;

@SuppressLint("NewApi")
public class ChessActivity extends Activity implements OnMessageSend {
	/** Called when the activity is first created. */
	GameView gv = null;
	View currentview = null;
	String playername;
	final int MENU_START = Menu.FIRST;
	final int MENU_BACK = Menu.FIRST + 1;
	public static int screenheight = 0;
	public static int screenweight = 0;
	boolean readstreamopen = false;
	Menu mymenu = null;
	WindowManager windowManager;
	int playerindex;
	int menumark = 0;
	GameSound soundmanager;
	boolean soundon = false;
	Handler handler = new Handler() {
		public void handleMessage(Message m) {
			switch (m.what) {
			case StaticDate.SINGLEGAME:
				gv = new GameView(ChessActivity.this, handler);
				currentview = gv;
				menumark = 1;
				setContentView(gv);
				break;
			case StaticDate.BLUETOOTHGAME:
				Intent intent = new Intent(ChessActivity.this,
						BluetoothChat.class);
				intent.putExtra("sound", soundon);
				startActivity(intent);
				break;
			case StaticDate.BACKTO:
				// TODO
				break;
			case StaticDate.EXIT:
				System.exit(0);
				break;
			case StaticDate.GAMEHELP:
				HelpDialog();
				break;
			case StaticDate.SOUND_MOVE:
				if (soundon)
					soundmanager.sound_move();
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		screenheight = display.getHeight();
		screenweight = display.getWidth();

		setContentView(R.layout.home_view);
		intHomeView();

		// Display display = getWindowManager().getDefaultDisplay();
		String resolution = display.getWidth() + "x" + display.getHeight();
		SoundSwitch();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		if (menumark != 0) {
			menu.add(0, MENU_START, 0, "开始新一局");
			return true;
		}

		return false;
	}

	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		onCreateOptionsMenu(menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case MENU_START:
			if (gv != null) {
				gv.vswho();
				gv.RestartGame();
			}
			break;

		}
		return false;
	}

	public void playagain() {

		new AlertDialog.Builder(this).setTitle("真遗憾....").setMessage("是否继续？")
				.setPositiveButton("继续", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						gv.chooseside();
						gv.RestartGame();

					}

				})
				.setNegativeButton("退出", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						handler.sendEmptyMessage(StaticDate.EXIT);
					}

				}).show();
	}

	public void HelpDialog() {
		new AlertDialog.Builder(this).setTitle("  帮助")
				.setMessage(R.string.helpinfo)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

					}

				}).show();
	}

	@Override
	public void SendMessage(int commond) {
		// TODO Auto-generated method stub
		handler.sendEmptyMessage(commond);

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	public void SoundSwitch() {

		new AlertDialog.Builder(this).setTitle("打开声音吗？")
				.setNegativeButton("否", null)
				.setPositiveButton("是", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						soundmanager = new GameSound(ChessActivity.this);
						soundon = true;
					}

				}).show();
	}

	private View.OnClickListener buttonListener=new OnClickListener() {
		@Override
		public void onClick(View v) {
			
			
		}
	};
	private void intHomeView() {
		int[] buttonid = new int[] { R.id.button1, R.id.button2, R.id.button3,
				R.id.button4 };
		HashMap<Enum, Drawable> drawables = (HashMap<Enum, Drawable>) SelectorUtils
				.getButtonHomeDrawable(this);
		for (Entry<Enum, Drawable> entry : drawables.entrySet()) {
			int index = entry.getKey().ordinal();
			View button=this.findViewById(buttonid[index]);
			button.setBackgroundDrawable(entry.getValue());
			button.setOnClickListener(buttonListener);
		}
	}

}
