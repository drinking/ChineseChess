package drinking.android.chess;

import org.drinking.customui.DialogCreator;
import org.drinking.utils.ProtocolDefine;

import android.content.Context;
import android.content.DialogInterface;

public abstract class EventTackle {
	GameController gameController;
	Context mContext;

	public EventTackle(GameController controller, Context context) {
		gameController = controller;
		mContext = context;
	}

	abstract public void regret();

	abstract public void repaly();

	public void onReceiveRegretEvent() {
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (DialogInterface.BUTTON_POSITIVE == which) {
					gameController.stepBack();
				}
				gameController.onRemoteEventAck(ProtocolDefine.ACK_REGRET,
						which == DialogInterface.BUTTON_POSITIVE);
				dialog.dismiss();
			}
		};

		DialogCreator.showCustomDialog("对方请求悔棋", "是否同意", "同意", listener, "不同意",
				listener, mContext);
	}

	public void onReceiveRestartEvent() {
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				gameController.onRemoteEventAck(ProtocolDefine.ACK_RESTART,
						which == DialogInterface.BUTTON_POSITIVE);
				dialog.dismiss();
			}
		};

		DialogCreator.showCustomDialog("对方请求重新开始", "是否同意", "同意", listener,
				"不同意", listener, mContext);
	}

	public static EventTackle getTackle(boolean bluetooth,
			GameController controller, Context context) {
		if (bluetooth)
			return new BluetoothTackle(controller, context);
		else
			return new LocalTackle(controller, context);
	}
}

class LocalTackle extends EventTackle {

	public LocalTackle(GameController controller, Context context) {
		super(controller, context);
	}

	@Override
	public void regret() {
		gameController.stepBack();
	}

	@Override
	public void repaly() {
		gameController.startNewGame();
	}
}

class BluetoothTackle extends EventTackle {

	public BluetoothTackle(GameController controller, Context context) {
		super(controller, context);
	}

	@Override
	public void regret() {
		gameController.onRemoteEvent(ProtocolDefine.TYPE_REGRET);
//		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
//
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				if (which == DialogInterface.BUTTON_POSITIVE) {
//					gameController.onRemoteEvent(ProtocolDefine.TYPE_REGRET);
//				}
//				dialog.dismiss();
//			}
//		};
//
//		DialogCreator.showCustomDialog("悔棋", "确认发送？", "确定", listener, "取消",
//				listener, mContext);
	}

	@Override
	public void repaly() {
		gameController.onRemoteEvent(ProtocolDefine.TYPE_RESTART);
//		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
//
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				if (which == DialogInterface.BUTTON_POSITIVE) {
//					gameController.onRemoteEvent(ProtocolDefine.TYPE_RESTART);
//				}
//				dialog.dismiss();
//			}
//		};
//
//		DialogCreator.showCustomDialog("重玩", "确认发送？", "确定", listener, "取消",
//				listener, mContext);
	}
}
