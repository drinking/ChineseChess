package org.drinking.utils;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import drinking.android.chess.R;

public class SelectorUtils<T extends Enum> {

	// 0 means state_enabled ,1 means state_pressed
	public enum BUTTON_HOME {
		single, bluetooth, help, quit
	};

	static int[] button_home_0 = { R.drawable.button_single_0,
			R.drawable.button_bluetooth_0, R.drawable.button_help_0,
			R.drawable.button_quit_0 };
	static int[] button_home_1 = { R.drawable.button_single_1,
			R.drawable.button_bluetooth_1, R.drawable.button_help_1,
			R.drawable.button_quit_1 };

	// AnimationDrawable, LevelListDrawable, StateListDrawable

	public static StateListDrawable get(Context context, int res_enable,
			int res_pressed) {
		final StateListDrawable drawable = new StateListDrawable();

		drawable.addState(new int[] { android.R.attr.state_pressed }, context
				.getResources().getDrawable(res_pressed));
		drawable.addState(new int[] { android.R.attr.state_enabled }, context
				.getResources().getDrawable(res_enable));

		return drawable;
	}

	public static Map<BUTTON_HOME, Drawable> getStateDrawables(Context context) {
		HashMap<BUTTON_HOME, Drawable> drawables = new HashMap<BUTTON_HOME, Drawable>();

		for (BUTTON_HOME value : BUTTON_HOME.values()) {
			int index = value.ordinal();
			Drawable drawable = get(context, button_home_0[index],
					button_home_1[index]);
			drawables.put(value, drawable);
		}
		return drawables;
	}

	private static Map<Enum, Drawable> getStateDrawable(Context context,
			Class<? extends Enum> e, int[] res0, int[] res1) {
		HashMap<Enum, Drawable> drawables = new HashMap<Enum, Drawable>();

		EnumSet set = EnumSet.allOf(e);
		Iterator<? extends Enum> iter = set.iterator();
		while (iter.hasNext()) {
			Enum t = iter.next();
			int index = t.ordinal();
			Drawable drawable = get(context, res0[index], res1[index]);
			drawables.put(t, drawable);
		}

		return drawables;
	}

	public static Map<Enum, Drawable> getButtonHomeDrawable(Context context) {
		return getStateDrawable(context, BUTTON_HOME.class, button_home_0,
				button_home_1);
	}

	public Map<T, Drawable> getStateDrawable(Context context, T enumName) {
		HashMap<T, Drawable> drawables = new HashMap<T, Drawable>();

		EnumSet set = EnumSet.allOf(enumName.getClass());
		Iterator<T> iter = set.iterator();
		while (iter.hasNext()) {
			T t = iter.next();
			int index = t.ordinal();
			Drawable drawable = get(context, button_home_0[index],
					button_home_1[index]);
			drawables.put(t, drawable);
		}

		return drawables;
	}
}
