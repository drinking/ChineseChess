package drinking.android.chess;

import com.adchina.android.ads.AdListener;
import com.adchina.android.ads.AdManager;
import com.adchina.android.ads.AdView;
import drinking.android.chess.WelcomeView.OnMessageSend;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


public class Chess extends Activity implements OnMessageSend,AdListener{
    /** Called when the activity is first created. */
	 WelcomeView vcview=null;
	 GameView gv=null;
	 View currentview=null;
	 String playername;
	 final int MENU_START=Menu.FIRST;
	 final int MENU_BACK=Menu.FIRST+1;
	 public static int screenheight=0;
	 public static int screenweight=0;
	 boolean readstreamopen=false;
	 Menu mymenu=null;
	 WindowManager windowManager;
	 int playerindex;
	 AdView mAdView;
	 int menumark=0;
	 GameSound soundmanager;
	 boolean soundon=false;
	 Handler handler=new Handler()
		{
			public void handleMessage(Message m)
			{
				switch(m.what)
				{
				case StaticDate.SINGLEGAME:
					gv=new GameView(Chess.this,handler);
					currentview=gv;
					menumark=1;
					setContentView(gv);
					break;
				case StaticDate.BLUETOOTHGAME:
					Intent intent=new Intent(Chess.this,BluetoothChat.class);
					intent.putExtra("sound", soundon);
					startActivity(intent);
					break;
				case StaticDate.BACKTO:
					setContentView(vcview);
					break;
				case StaticDate.EXIT:
					System.exit(0);
					break;
				case StaticDate.GAMEHELP:
					HelpDialog();
					break;
				case StaticDate.SOUND_MOVE:
					if(soundon)
					soundmanager.sound_move();
					break;
					default:
						break;
				}
			}
		};
    @Override
   
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
   
      
       windowManager = getWindowManager();     
       Display display = windowManager.getDefaultDisplay();
       screenheight=display.getHeight();
       screenweight=display.getWidth();

       setContentView(R.layout.welcomeview);
       vcview=(WelcomeView)findViewById(R.id.firstview);
       vcview.SetListener(this);
       currentview=vcview;
       ////ads
       //Display display = getWindowManager().getDefaultDisplay();
       String resolution = display.getWidth() + "x" + display.getHeight();
       AdManager.setResolution(resolution);
       AdManager.setAdspaceId("70255"); 
       /* Set ad view */
   		mAdView = (AdView)findViewById(R.id.adview);
   		mAdView.setAdListener(this);
   		mAdView.start();
   		SoundSwitch();
   		
   		
    }

  
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {

    		if(menumark!=0)
    		{
    		menu.add(0,MENU_START, 0, "开始新一局");
    		return true;
    		}

		return false;
	}
    public boolean onPrepareOptionsMenu(Menu menu)
    {
    	menu.clear();
    	onCreateOptionsMenu(menu);
    	
    	return true;
    }
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
	
	   super.onOptionsItemSelected(item);
	   switch(item.getItemId()){
	   case MENU_START:
		   if(gv!=null)
		   {  
		 gv.vswho();
		 gv.RestartGame();
		   }
		   break;
	 
	   }
	   return false;
	}
   
    public void playagain()
    {
    	
		new AlertDialog.Builder(this)
    	.setTitle("真遗憾....")
    	.setMessage("是否继续？")
    	.setPositiveButton("继续", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int which) {	
				gv.chooseside();
				gv.RestartGame();
				
			}
    		
    	}).setNegativeButton("退出", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int which) {
				
				handler.sendEmptyMessage(StaticDate.EXIT);
			}
    		
    	}).show();
    }
    public void HelpDialog()
    {
    	new AlertDialog.Builder(this)
        .setTitle("  帮助")
        .setMessage(R.string.helpinfo)
        .setPositiveButton("确定", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int which) {
				
				
			}
    		
    	}).show();
    }

	@Override
	public void SendMessage(int commond) {
		// TODO Auto-generated method stub
		handler.sendEmptyMessage(commond);
		
	}
	@Override     
	public void onBackPressed() {
	    if(currentview!=vcview){
	    	
		new AlertDialog.Builder(this)
        .setTitle("返回主菜单吗?")
        .setNegativeButton("取消", null)
        .setPositiveButton("确定", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int which) {
				  setContentView(R.layout.welcomeview);
		          vcview=(WelcomeView)findViewById(R.id.firstview);
		          vcview.SetListener(Chess.this);
		          currentview=vcview;
		          gv=null;
		          menumark=0;
				
			}
    		
    	}).show();

	    	
	         return;      
	    }
	    super.onBackPressed();
	    
	}
	public void SoundSwitch()
	{
	
		new AlertDialog.Builder(this)
        .setTitle("打开声音吗？")
        .setNegativeButton("否", null)
        .setPositiveButton("是", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int which) {
				soundmanager=new GameSound(Chess.this);
				soundon=true;
			}
    		
    	}).show();
	}


	@Override
	public boolean OnRecvSms(AdView arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void onFailedToReceiveAd(AdView arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onFailedToReceiveFullScreenAd(AdView arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onFailedToRefreshAd(AdView arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onReceiveAd(AdView arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onReceiveFullScreenAd(AdView arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onRefreshAd(AdView arg0) {
		// TODO Auto-generated method stub
		
	} 
   
}
