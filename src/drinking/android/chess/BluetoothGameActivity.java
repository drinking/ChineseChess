package drinking.android.chess;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

@SuppressLint("NewApi")
public class BluetoothGameActivity extends Activity {
	// Debugging
	private static final String TAG = "BluetoothChat";
	private static final boolean D = true;
	private boolean soundon = false;
	// Message types sent from the BluetoothChatService Handler
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;
	
	public static final int MESSAGE_NEXTSTEP = 6;
	public static final int MESSAGE_AGREE = 7;
	public static final int MESSAGE_DISAGREE = 8;
	public static final int MESSAGE_LOSE = 9;
	public static final int MESSAGE_DRAW = 10;
	public static final int MESSAGE_REGRET = 11;
	public static final int MESSAGE_SOUND = 12;

	// status of chess
	public static final int STATUS_RED = 7;
	public static final int STATUS_BLACK = 8;
	public static final int STATUS_AGREE = 9;
	public static final int STATUS_DISAGREE = 10;
	public static final int STATUS_NEXTSTEP = 11;
	public static final int STATUS_REGRET = 12;
	public static final int STATUS_ASKDRAW = 13;
	public static final int STATUS_ASKLOSE = 14;
	public static final int STATUS_ASKAGAIN = 15;

	// Key names received from the BluetoothChatService Handler
	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST = "toast";

	// Intent request codes
	private static final int REQUEST_CONNECT_DEVICE = 1;
	private static final int REQUEST_ENABLE_BT = 2;

	// private TextView mTitle;
	private BluetoothAdapter mBluetoothAdapter = null;
	// Member object for the chat services
	private BluetoothService mChatService = null;
	private GameController gameController;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		// If the adapter is null, then Bluetooth is not supported
		if (mBluetoothAdapter == null) {
			Toast.makeText(this, "Bluetooth is not available",
					Toast.LENGTH_LONG).show();
			finish();
			return;
		}
		gameController = new GameController(this, getWindowManager()
				.getDefaultDisplay());
		setContentView(gameController.getGameView());
	}

	@Override
	public void onStart() {
		super.onStart();
		// If BT is not on, request that it be enabled.
		// setupChat() will then be called during onActivityResult
		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
			// Otherwise, setup the chat session
		} else {
			if (mChatService == null)
				setupChat();
		}

	}

	@Override
	public synchronized void onResume() {
		super.onResume();

		if (mChatService != null
				&& mChatService.getState() == BluetoothService.STATE_NONE) {
			// Only if the state is STATE_NONE, do we know that we haven't
			// started already
			mChatService.start();
		}

	}

	private void setupChat() {
		mChatService = new BluetoothService(this, mHandler);
	}

	@Override
	public synchronized void onPause() {
		super.onPause();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// Stop the Bluetooth chat services
		if (mChatService != null)
			mChatService.stop();
	}

	private void ensureDiscoverable() {
		if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
			Intent discoverableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoverableIntent.putExtra(
					BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
			startActivity(discoverableIntent);
		}
	}

	/**
	 * Sends a message.
	 * 
	 * @param message
	 *            A string of text to send.
	 */
	private void SendMessage(String message) {
		// Check that we're actually connected before trying anything
		if (mChatService.getState() != BluetoothService.STATE_CONNECTED) {
			Toast.makeText(this, "连接不存在", Toast.LENGTH_SHORT).show();
			return;
		}

		// Check that there's actually something to send
		if (message.length() > 0) {
			byte[] send = message.getBytes();
			mChatService.write(send);
		}
	}

	// The Handler that gets information back from the BluetoothChatService
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_STATE_CHANGE:
				if (D)
					Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
				switch (msg.arg1) {
				case BluetoothService.STATE_CONNECTED:
					// mConversationArrayAdapter.clear();
					break;
				case BluetoothService.STATE_CONNECTING:

					break;
				case BluetoothService.STATE_LISTEN:
				case BluetoothService.STATE_NONE:

					break;
				}
				break;
			case MESSAGE_WRITE:
				break;
			case MESSAGE_READ:
				gameController.receviePacket((byte[]) msg.obj);
				break;
			case MESSAGE_DEVICE_NAME:
				Toast.makeText(getApplicationContext(), "连接成功", Toast.LENGTH_SHORT).show();
				gameController.setConnection(mChatService);
				if(host) gameController.chooseside();
				break;
			case MESSAGE_TOAST:
				Toast.makeText(getApplicationContext(),
						msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
						.show();
				break;
			case MESSAGE_NEXTSTEP:

				SendMessage(msg.getData().getString("STEP"));
				break;
			case MESSAGE_AGREE:
				SendMessage("9a0a0a0a0");
				break;
			case MESSAGE_DISAGREE:
				SendMessage("10a0a0a0a0");
				break;
			case MESSAGE_LOSE:
				SendMessage("14a0a0a0a0");
				break;
			case MESSAGE_DRAW:
				SendMessage("13a0a0a0a0");
				break;
			case MESSAGE_REGRET:
				SendMessage("12a0a0a0a0");
				break;
			case MESSAGE_SOUND:
				break;
			}
		}
	};

	boolean host=true;
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (D)
			Log.d(TAG, "onActivityResult " + resultCode);
		switch (requestCode) {
		case REQUEST_CONNECT_DEVICE:
			// When DeviceListActivity returns with a device to connect
			if (resultCode == Activity.RESULT_OK) {
				// Get the device MAC address
				String address = data.getExtras().getString(
						DeviceListActivity.EXTRA_DEVICE_ADDRESS);
				// Get the BLuetoothDevice object
				BluetoothDevice device = mBluetoothAdapter
						.getRemoteDevice(address);
				// Attempt to connect to the device
				mChatService.connect(device);
				Toast.makeText(this, "连接设备", 1000).show();
				host=false;
			}
			break;
		case REQUEST_ENABLE_BT:
			// When the request to enable Bluetooth returns
			if (resultCode == Activity.RESULT_OK) {
				// Bluetooth is now enabled, so set up a chat session
				setupChat();
			} else {
				// User did not enable Bluetooth or an error occured
				Log.d(TAG, "BT not enabled");
				Toast.makeText(this, R.string.bt_not_enabled_leaving,
						Toast.LENGTH_SHORT).show();
				finish();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.option_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.scan:
			// Launch the DeviceListActivity to see devices and do scan
			Intent serverIntent = new Intent(this, DeviceListActivity.class);
			startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
			return true;
		case R.id.discoverable:
			// Ensure this device is discoverable by others
			ensureDiscoverable();
			return true;
		case R.id.play:
			gameController.chooseside();
			return true;
		case R.id.back:
			// 断开蓝牙
			mChatService.stop();
			this.finish();
			return true;

		}
		return false;
	}

}