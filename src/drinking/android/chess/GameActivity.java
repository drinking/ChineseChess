package drinking.android.chess;

import android.app.Activity;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

public class GameActivity extends Activity {

	GameController controller;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		controller = new GameController(this, display);
		this.setContentView(controller.getGameView());
		
		controller.startNewGame();
	}
	private void addMenuView(){
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.gravity=Gravity.BOTTOM;
		LinearLayout ll=new LinearLayout(this);
		ll.setGravity(Gravity.RIGHT);
		ll.setPadding(0, 10, 0, 10);
		Button regret=new Button(this);
		
		regret.setWidth(50);
		regret.setText("»ÚÆå");
		regret.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				controller.stepBack();
			}
		});
		regret.setLayoutParams(params);
		Button restart=new Button(this);
		restart.setWidth(50);
		restart.setText("ÖØÍæ");
		restart.setLayoutParams(params);
		restart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				controller.startNewGame();
			}
		});
		
		ll.addView(regret, params);
		ll.addView(restart, params);
		this.addContentView(ll,  new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	}
}
