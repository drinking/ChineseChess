package drinking.android.chess;

import org.drinking.customui.DialogCreator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

@SuppressLint("NewApi")
public class BluetoothGameActivity extends Activity {
	// Debugging
	private static final String TAG = "BluetoothChat";
	private static final boolean D = true;
	// Message types sent from the BluetoothChatService Handler
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;
	
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
		
		buildConnection();
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
				gameController.setConnection(mChatService);
				if(host) gameController.chooseside();
				break;
			case MESSAGE_TOAST:
				Toast.makeText(getApplicationContext(),
						msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
						.show();
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
			buildConnection();
			return true;
		case R.id.restart:
			gameController.tackleNewGame();
			return true;
		case R.id.regret:
			gameController.tackleRegret();
			return true;

		}
		return false;
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
		regret.setText("悔棋");
		regret.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				gameController.tackleRegret();
			}
		});
		regret.setLayoutParams(params);
		Button restart=new Button(this);
		restart.setWidth(50);
		restart.setText("重玩");
		restart.setLayoutParams(params);
		restart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				gameController.tackleNewGame();
			}
		});
		
		ll.addView(regret, params);
		ll.addView(restart, params);
		this.addContentView(ll,  new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	}
	public void buildConnection(){
		DialogInterface.OnClickListener listener=new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(DialogInterface.BUTTON_POSITIVE==which){
					ensureDiscoverable();
				}else{
					Intent serverIntent = new Intent(BluetoothGameActivity.this, DeviceListActivity.class);
					startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
				}
				dialog.dismiss();
			}
		};
		DialogCreator.showCustomDialog("建立连接", "选择建立主机等待连接或连接已建立的主机", "建立主机", listener, "连接主机", listener, this);
	}
}