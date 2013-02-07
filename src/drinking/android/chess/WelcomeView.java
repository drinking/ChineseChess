package drinking.android.chess;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class WelcomeView extends View {
	public interface OnMessageSend {
		void SendMessage(int commmond);
	}

	OnMessageSend Msglistener;
	Rect singlegame;
	Rect bluetoothgame;
	Rect help;
	Rect quit;
	Bitmap singlegamepic;
	Bitmap bluetoothgamepic;
	Bitmap singlegamedpic;
	Bitmap bluetoothgamedpic;
	Bitmap startgamepic;
	Bitmap soundpic;
	Bitmap rankpic;
	Bitmap helppic;
	Bitmap quitpic;
	Bitmap startgamedownpic;
	Bitmap sounddownpic;
	Bitmap rankdownpic;
	Bitmap helpdownpic;
	Bitmap quitdownpic;
	Paint paint;
	Paint paint1;
	Paint paint2;
	Paint paint3;
	Paint paint4;
	Paint fontpaint;
	Context mycontext;
	Bitmap bg;
	int INILEFT = 80;
	int INITOP = 100;
	int INIRIGHT = 220;
	int INIBOTTOM = 140;
	int VERTICAL_Y = 50;
	int buttondown = 0;
	Handler myhandler;

	public void init() {
		INITOP = ChessActivity.screenheight / 4;
		INILEFT = (ChessActivity.screenweight - 140) / 2;
		INIBOTTOM = INITOP + 40;
		INIRIGHT = INILEFT + 140;
		// myhandler=handler;
		paint = new Paint();
		paint1 = new Paint();
		paint2 = new Paint();
		paint3 = new Paint();
		paint4 = new Paint();
		fontpaint = new Paint();
		paint.setColor(Color.BLUE);
		paint1.setColor(Color.BLUE);
		paint2.setColor(Color.BLUE);
		paint3.setColor(Color.BLUE);
		paint4.setColor(Color.BLUE);
		fontpaint.setColor(Color.WHITE);
		fontpaint.setTextSize(30);
		singlegame = new Rect(INILEFT, INITOP, INIRIGHT, INIBOTTOM);
		bluetoothgame = new Rect(INILEFT, INITOP + VERTICAL_Y, INIRIGHT,
				INIBOTTOM + VERTICAL_Y);
		help = new Rect(INILEFT, INITOP + VERTICAL_Y * 2, INIRIGHT, INIBOTTOM
				+ VERTICAL_Y * 2);
		quit = new Rect(INILEFT, INITOP + VERTICAL_Y * 3, INIRIGHT, INIBOTTOM
				+ VERTICAL_Y * 3);
//		singlegamepic = BitmapFactory.decodeResource(getResources(),
//				R.drawable.siglegamepic);
//		bluetoothgamepic = BitmapFactory.decodeResource(getResources(),
//				R.drawable.bluetoothgamepic);
//		singlegamedpic = BitmapFactory.decodeResource(getResources(),
//				R.drawable.singlegamedpic);
//		bluetoothgamedpic = BitmapFactory.decodeResource(getResources(),
//				R.drawable.bluetoothdpic);
//		helppic = BitmapFactory.decodeResource(getResources(),
//				R.drawable.helppic);
//		quitpic = BitmapFactory.decodeResource(getResources(),
//				R.drawable.quitpic);
//		helpdownpic = BitmapFactory.decodeResource(getResources(),
//				R.drawable.helpdpic);
//		quitdownpic = BitmapFactory.decodeResource(getResources(),
//				R.drawable.quitdpic);
	}

	public WelcomeView(Context context, Handler handler) {
		super(context);
		// TODO Auto-generated constructor stub\
		init();

	}

	public WelcomeView(Context context, AttributeSet attars) {
		super(context, attars);
		init();
	}

	public void SetListener(OnMessageSend lis) {
		Msglistener = lis;
	}

	public void onDraw(Canvas canvas) {

		this.setBackgroundResource(R.drawable.wbg);
		if (buttondown == 0) {
			canvas.drawBitmap(singlegamepic, null, singlegame, paint);
			canvas.drawBitmap(bluetoothgamepic, null, bluetoothgame, paint);
			canvas.drawBitmap(helppic, null, help, paint);
			canvas.drawBitmap(quitpic, null, quit, paint);
		}

		switch (buttondown) {

		case 1:

			canvas.drawBitmap(singlegamedpic, null, singlegame, paint);
			canvas.drawBitmap(bluetoothgamepic, null, bluetoothgame, paint);
			canvas.drawBitmap(helppic, null, help, paint);
			canvas.drawBitmap(quitpic, null, quit, paint);

			break;
		case 2:
			canvas.drawBitmap(singlegamepic, null, singlegame, paint);
			canvas.drawBitmap(bluetoothgamedpic, null, bluetoothgame, paint);
			canvas.drawBitmap(helppic, null, help, paint);
			canvas.drawBitmap(quitpic, null, quit, paint);
			break;
		case 3:
			canvas.drawBitmap(singlegamepic, null, singlegame, paint);
			canvas.drawBitmap(bluetoothgamepic, null, bluetoothgame, paint);
			canvas.drawBitmap(helpdownpic, null, help, paint);
			canvas.drawBitmap(quitpic, null, quit, paint);
			break;
		case 4:
			canvas.drawBitmap(singlegamepic, null, singlegame, paint);
			canvas.drawBitmap(bluetoothgamepic, null, bluetoothgame, paint);
			canvas.drawBitmap(helppic, null, help, paint);
			canvas.drawBitmap(quitdownpic, null, quit, paint);
			break;
		default:
			break;
		}

	}

	public boolean onTouchEvent(MotionEvent event) {
		if (singlegame.contains((int) event.getX(), (int) event.getY())) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				// paint.setColor(Color.GRAY);
				buttondown = 1;
				postInvalidate();

			}
			if (event.getAction() == MotionEvent.ACTION_UP) {

				// myhandler.sendEmptyMessage(StaticDate.SINGLEGAME);
				Msglistener.SendMessage(StaticDate.SINGLEGAME);

			}
		} else if (bluetoothgame.contains((int) event.getX(),
				(int) event.getY())) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				buttondown = 2;
				postInvalidate();

			}
			if (event.getAction() == MotionEvent.ACTION_UP) {
				Msglistener.SendMessage(StaticDate.BLUETOOTHGAME);
			}
		} else if (help.contains((int) event.getX(), (int) event.getY())) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				buttondown = 3;
				postInvalidate();

			}
			if (event.getAction() == MotionEvent.ACTION_UP) {
				Msglistener.SendMessage(StaticDate.GAMEHELP);
			}
		} else if (quit.contains((int) event.getX(), (int) event.getY())) {

			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				buttondown = 4;
				postInvalidate();

			}
			if (event.getAction() == MotionEvent.ACTION_UP) {

				System.exit(0);
			}
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
			buttondown = 0;
			postInvalidate();
		}
		return true;
	}

}