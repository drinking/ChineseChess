package drinking.android.chess;

import org.drinking.customui.DialogCreator;
import org.drinking.utils.PacketUtils;
import org.drinking.utils.ProtocolDefine;
import org.json.JSONException;

import android.content.Context;
import android.content.DialogInterface;
import android.view.Display;
import android.view.View;
import android.widget.Toast;

public class GameController {

	GameView gameView;
	GameSound soundmanager;
	Display screenSize;
	BluetoothService connection;
	Context mContext;
	EventTackle tackle;

	public GameController(Context context, Display screenSize) {
		this.screenSize = screenSize;
		mContext = context;
		gameView = new GameView(context, this);
		soundmanager = new GameSound(context);
		tackle = EventTackle.getTackle(false, this, mContext);
	}

	public void receviePacket(byte[] packet) {
		try {
			int type = PacketUtils.getType(packet);
			switch (type) {
			case ProtocolDefine.TYPE_NEXTSTEP:
				gameView.moveStep(PacketUtils.getStep(packet));
				break;
			case ProtocolDefine.TYPE_SIDE_RED:
			case ProtocolDefine.TYPE_SIDE_BLACK:
				setside(type != ProtocolDefine.TYPE_SIDE_RED);
				break;
			case ProtocolDefine.TYPE_REGRET:
				tackle.onReceiveRegretEvent();
				break;
			case ProtocolDefine.TYPE_RESTART:
				tackle.onReceiveRestartEvent();
				break;
			case ProtocolDefine.ACK_REGRET:
				ackRegret(PacketUtils.getAckResult(packet));
				break;
			case ProtocolDefine.ACK_RESTART:
				ackRestart(PacketUtils.getAckResult(packet));
				break;
				
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void setConnection(BluetoothService conn) {
		connection = conn;
		tackle = EventTackle.getTackle(true, this, mContext);
	}

	public void onMove(int fromx, int fromy, int tox, int toy) {
		if (connection != null) {
			connection.write(PacketUtils
					.createMoveEvent(fromx, fromy, tox, toy));
		}
	}

	public void onRemoteEvent(int type) {
		if (connection != null) {
			connection.write(PacketUtils.createCommand(type));
		}
	}
	public void onRemoteEventAck(int type,boolean value){
		if (connection != null) {
			connection.write(PacketUtils.createAct(type, value));
		}
	}

	interface EventListener {
		public void onDone();
	}

	public void onEndDialog(String title) {
		DialogCreator.showCustomDialog(title, "是否继续？", "继续", restartListener,
				"退出", restartListener, mContext);
	}

	public void requestDialog(String title, final EventListener eventListener) {
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				if (DialogInterface.BUTTON_POSITIVE == which) {
					eventListener.onDone();

				}
				dialog.dismiss();
			}
		};
		DialogCreator.showCustomDialog(title, "是否同意", "同意", listener, "不要",
				listener, mContext);

	}

	DialogInterface.OnClickListener restartListener = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			if (which == DialogInterface.BUTTON_POSITIVE) {
				chooseside();
			} else {
				finishGame();
			}
			dialog.dismiss();
		}
	};

	private void playAgainDialog() {
		DialogCreator.showCustomDialog("喂喂喂", "还来不来？", "搞起", restartListener,
				"休兵", restartListener, mContext);
	}

	private void setside(boolean isRed) {
		gameView.initChess();
		gameView.setFirstGo(isRed);
	}

	public void chooseside() {
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				boolean isRed = DialogInterface.BUTTON_POSITIVE == which;
				setside(isRed);
				onRemoteEvent(isRed ? ProtocolDefine.TYPE_SIDE_RED
						: ProtocolDefine.TYPE_SIDE_BLACK);
				dialog.dismiss();
			}

		};
		DialogCreator.showCustomDialog("颜色选择", "红方为先手", "红方", listener,
				"黑方", listener, mContext);

	}

	public void playMoveSound() {
		soundmanager.sound_move();
	}

	public void finishGame() {
		System.exit(0);
	}

	public View getGameView() {
		return gameView;
	}

	public int getScreenWidth() {
		return screenSize.getWidth();
	}

	public int getScreenHeight() {
		return screenSize.getHeight();
	}

	public void startNewGame() {
		this.chooseside();
	}
	public void tackleNewGame(){
		tackle.repaly();
	}
	public void tackleRegret(){
		tackle.regret();
	}

	public void stepBack() {
		gameView.stepBack();
	}

	public void ackRegret(boolean result){
		if(result){
			MyApplication.Toast("对方同意悔棋...");
			gameView.stepBack();
		}else{
			MyApplication.Toast("对方不同意悔棋...");
		}
	}
	public void ackRestart(boolean result){
		if(result){
			MyApplication.Toast("对方同意重新开始...");
			startNewGame();
		}else{
			MyApplication.Toast("对方不同意重新开始...");
		}
	}
	public boolean canMove(int range) {
		// we can't move upside when in bluetooth model
		if (connection != null && range > 0 && range < 8) {
			return false;
		}
		return true;
	}
}
