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
import drinking.android.chess.GameView.EVENT;

public class GameController {

	GameView gameView;
	GameSound soundmanager;
	Display screenSize;
	BluetoothService connection;
	Context mContext;

	public GameController(Context context, Display screenSize) {
		this.screenSize = screenSize;
		mContext = context;
		gameView = new GameView(context, this);
		soundmanager = new GameSound(context);
	}

	public void receviePacket(byte[] packet) {
		try {
			int type = PacketUtils.getType(packet);
			Toast.makeText(mContext, "got type " + type, Toast.LENGTH_SHORT)
					.show();
			switch (type) {
			case ProtocolDefine.TYPE_NEXTSTEP:
				gameView.moveStep(PacketUtils.getStep(packet));
				break;
			case ProtocolDefine.TYPE_SIDE_RED:
			case ProtocolDefine.TYPE_SIDE_BLACK:
				setside(type!=ProtocolDefine.TYPE_SIDE_RED);
				break;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void setConnection(BluetoothService conn) {
		connection = conn;
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

	interface EventListener {
		public void onDone();
	}

	public void onEvent(EVENT event) {
		switch (event) {
		case LOSE:
			requestDialog("�ǻ�������", new EventListener() {

				@Override
				public void onDone() {
					playAgainDialog();
				}
			});
			break;
		case DRAW:
			requestDialog("�ǻ������", new EventListener() {

				@Override
				public void onDone() {
					playAgainDialog();
				}
			});
			break;
		case REGRET:
			requestDialog("�ǻ������", new EventListener() {

				@Override
				public void onDone() {
					gameView.BackStep();
				}
			});
			break;
		}

	}

	public void onEndDialog(String title) {
		DialogCreator.showCustomDialog(title, "�Ƿ������", "����", restartListener,
				"�˳�", restartListener, mContext);
	}

	private void requestDialog(String title, final EventListener eventListener) {
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				if (DialogInterface.BUTTON_POSITIVE == which) {
					eventListener.onDone();

				}
				dialog.dismiss();
			}
		};
		DialogCreator.showCustomDialog(title, "�Ƿ�ͬ��", "ͬ��", listener, "��Ҫ",
				listener, mContext);

	}

	DialogInterface.OnClickListener restartListener = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			if (which == DialogInterface.BUTTON_POSITIVE) {
				chooseside();
				restart();
			} else {
				finishGame();
			}
			dialog.dismiss();
		}
	};

	private void playAgainDialog() {
		DialogCreator.showCustomDialog("ιιι", "����������", "����", restartListener,
				"�ݱ�", restartListener, mContext);
	}

	private void setside(boolean isRed) {
		gameView.initChess();
		gameView.setFirstGo(isRed);
	}

	public void chooseside() {
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				boolean isRed=DialogInterface.BUTTON_POSITIVE == which;
				setside(isRed);
				onRemoteEvent(isRed ? ProtocolDefine.TYPE_SIDE_RED
						: ProtocolDefine.TYPE_SIDE_BLACK);
				dialog.dismiss();
			}

		};
		DialogCreator.showCustomDialog("�����Ⱥ�˳��ѡ��", "�췽Ϊ����", "�췽", listener,
				"�ڷ�", listener, mContext);

	}

	public void playMoveSound() {
		soundmanager.sound_move();
	}

	public void finishGame() {

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

	public void startGame() {
		chooseside();
	}

	public void restart() {
		gameView.initChess();
	}
	public boolean canMove(int range){
		//we can't move upside when in bluetooth model
		if(connection!=null&&range>0&&range<8){
			return false;
		}
		return true;
	}
}
