package drinking.android.chess;

import android.app.Activity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

public class GameActivity extends Activity {

	GameController controller;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		controller = new GameController(this, display);
		this.setContentView(controller.getGameView());
		controller.startGame();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, Menu.FIRST, 0, "开始新一局");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		super.onOptionsItemSelected(item);
		controller.restart();
		return false;
	}
}
