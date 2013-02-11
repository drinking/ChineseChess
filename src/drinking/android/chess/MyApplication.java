package drinking.android.chess;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

	private static Context instance;

	@Override
	public void onCreate() {
		super.onCreate();
		instance=this;
	}

	public static Context getInstance() {
		return instance;
	}

	public static void Toast(String msg) {
		android.widget.Toast.makeText(instance, msg,
				android.widget.Toast.LENGTH_SHORT).show();
	}

	public static void Toast(int msg) {
		android.widget.Toast.makeText(instance, msg,
				android.widget.Toast.LENGTH_SHORT).show();
	}
}
