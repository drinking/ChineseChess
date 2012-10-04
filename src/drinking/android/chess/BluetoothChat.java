package drinking.android.chess;




import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class BluetoothChat extends Activity {
    // Debugging
    private static final String TAG = "BluetoothChat";
    private static final boolean D = true;
    private boolean soundon=false;
    GameSound soundmanger;
    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final int MESSAGE_NEXTSTEP=6;
    public static final int MESSAGE_AGREE=7;
    public static final int MESSAGE_DISAGREE=8;
    public static final int MESSAGE_LOSE=9;
    public static final int MESSAGE_DRAW=10;
    public static final int MESSAGE_REGRET=11;
    public static final int MESSAGE_SOUND=12;
    
    //status of chess
    public static final int STATUS_RED=7;
    public static final int STATUS_BLACK=8;
    public static final int STATUS_AGREE=9;
    public static final int STATUS_DISAGREE=10;
    public static final int STATUS_NEXTSTEP=11;
    public static final int STATUS_REGRET=12;
    public static final int STATUS_ASKDRAW=13;
    public static final int STATUS_ASKLOSE=14;
    public static final int STATUS_ASKAGAIN=15;
    
    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    // Layout Views
    public static int screenheight=0;
	 public static int screenweight=0;
    //private TextView mTitle;
    BGameView gameview=null;
    private BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the chat services
    private BluetoothChatService mChatService = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        soundon=this.getIntent().getExtras().getBoolean("sound", false);
        if(soundon)
        {
        	soundmanger=new GameSound(this);
        }
        if(D) Log.e(TAG, "+++ ON CREATE +++");
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
       
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        WindowManager windowManager = getWindowManager();     
        Display display = windowManager.getDefaultDisplay();
        screenheight=display.getHeight();
        screenweight=display.getWidth();
        gameview=new BGameView(this,mHandler);
        setContentView(gameview);
        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
       if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        // Otherwise, setup the chat session
        } else {
            if (mChatService == null) setupChat();
        }
       
    }

    @Override
    public synchronized void onResume() {
        super.onResume();
       if (mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
              // Start the Bluetooth chat services
            	
              mChatService.start();
            
            }
        }
        
    }

    private void setupChat() {
        Log.d(TAG, "setupChat()");

        mChatService = new BluetoothChatService(this, mHandler);

        // Initialize the buffer for outgoing messages
    }

    @Override
    public synchronized void onPause() {
        super.onPause();
        if(D) Log.e(TAG, "- ON PAUSE -");
    }

    @Override
    public void onStop() {
        super.onStop();
        if(D) Log.e(TAG, "-- ON STOP --");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop the Bluetooth chat services
        if (mChatService != null) mChatService.stop();
        if(D) Log.e(TAG, "--- ON DESTROY ---");
    }

    private void ensureDiscoverable() {
        if(D) Log.d(TAG, "ensure discoverable");
        if (mBluetoothAdapter.getScanMode() !=
            BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    /**
     * Sends a message.
     * @param message  A string of text to send.
     */
    private void SendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
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
                if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                switch (msg.arg1) {
                case BluetoothChatService.STATE_CONNECTED:           	
                   // mConversationArrayAdapter.clear();
                    break;
                case BluetoothChatService.STATE_CONNECTING:
                  
                    break;
                case BluetoothChatService.STATE_LISTEN:
                case BluetoothChatService.STATE_NONE:
                   
                    break;
                }
                break;
            case MESSAGE_WRITE:
                break;
            case MESSAGE_READ:
                byte[] readBuf = (byte[]) msg.obj;
                // construct a string from the valid bytes in the buffer
                String readMessage = new String(readBuf, 0, msg.arg1);         
                gameview.RecieveMessage(readMessage); 
 
                break;
            case MESSAGE_DEVICE_NAME:
            	Toast.makeText(getApplicationContext(),"连接成功", 1000).show();
                break;
            case MESSAGE_TOAST:
                Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                               Toast.LENGTH_SHORT).show();
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
            	if(soundon)
            		soundmanger.sound_move();
            	break;
            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(D) Log.d(TAG, "onActivityResult " + resultCode);
        switch (requestCode) {
        case REQUEST_CONNECT_DEVICE:
            // When DeviceListActivity returns with a device to connect
            if (resultCode == Activity.RESULT_OK) {
                // Get the device MAC address
                String address = data.getExtras()
                                     .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                // Get the BLuetoothDevice object
                BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                // Attempt to connect to the device
                mChatService.connect(device);
                Toast.makeText(this, "连接设备", 1000).show();
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
                Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
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
        	playchess();
        	return true;
        case R.id.back:
        	//断开蓝牙
        	mChatService.stop();
        	this.finish();
        	return true;
      
        	
        }
        return false;
    }
    public void playchess()
    {
    	new AlertDialog.Builder(this)
    	.setTitle("下棋先后顺序选择")
    	.setMessage("红方为先手")
    	.setPositiveButton("红方", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int which) {
				
			

				SendMessage("7a0a0a0a0");
				gameview.status=STATUS_RED;
				//gameview.chooseside=STATUS_RED;
				//gameview.ChooseSide(0);
			}
    		
    	}).setNegativeButton("黑方", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int which) {
				
				SendMessage("8a0a0a0a0");
				gameview.status=STATUS_BLACK;
				
			}
    		
    	}).show();
    }

}