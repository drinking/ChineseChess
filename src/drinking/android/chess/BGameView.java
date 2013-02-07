package drinking.android.chess;

import java.util.ArrayList;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class BGameView extends View {
	final int NOCHESS = 0;// 没有棋子
	final int B_KING = 1;// 黑帅
	final int B_CAR = 2; // 黑车
	final int B_HORSE = 3; // 黑马
	final int B_CANON = 4; // 黑炮
	final int B_BISHOP = 5; // 黑士
	final int B_ELEPHANT = 6; // 黑象
	final int B_PAWN = 7; // 黑卒
	final int B_BEGIN = B_KING;
	final int B_END = B_PAWN;

	final int R_KING = 8;// 红将
	final int R_CAR = 9;// 红车
	final int R_HORSE = 10;// 红马
	final int R_CANON = 11;// 红炮
	final int R_BISHOP = 12;// 红士
	final int R_ELEPHANT = 13;// 红相
	final int R_PAWN = 14;// 红兵
	private int[][] iniposition = {
			{ B_CAR, B_HORSE, B_ELEPHANT, B_BISHOP, B_KING, B_BISHOP,
					B_ELEPHANT, B_HORSE, B_CAR },
			{ NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS,
					NOCHESS, NOCHESS },
			{ NOCHESS, B_CANON, NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS,
					B_CANON, NOCHESS },
			{ B_PAWN, NOCHESS, B_PAWN, NOCHESS, B_PAWN, NOCHESS, B_PAWN,
					NOCHESS, B_PAWN },
			{ NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS,
					NOCHESS, NOCHESS },
			// 楚河 汉界//
			{ NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS,
					NOCHESS, NOCHESS },
			{ R_PAWN, NOCHESS, R_PAWN, NOCHESS, R_PAWN, NOCHESS, R_PAWN,
					NOCHESS, R_PAWN },
			{ NOCHESS, R_CANON, NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS,
					R_CANON, NOCHESS },
			{ NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS,
					NOCHESS, NOCHESS },
			{ R_CAR, R_HORSE, R_ELEPHANT, R_BISHOP, R_KING, R_BISHOP,
					R_ELEPHANT, R_HORSE, R_CAR } };

	private final int[][] startposition = {
			{ B_CAR, B_HORSE, B_ELEPHANT, B_BISHOP, B_KING, B_BISHOP,
					B_ELEPHANT, B_HORSE, B_CAR },
			{ NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS,
					NOCHESS, NOCHESS },
			{ NOCHESS, B_CANON, NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS,
					B_CANON, NOCHESS },
			{ B_PAWN, NOCHESS, B_PAWN, NOCHESS, B_PAWN, NOCHESS, B_PAWN,
					NOCHESS, B_PAWN },
			{ NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS,
					NOCHESS, NOCHESS },
			// 楚河 汉界//
			{ NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS,
					NOCHESS, NOCHESS },
			{ R_PAWN, NOCHESS, R_PAWN, NOCHESS, R_PAWN, NOCHESS, R_PAWN,
					NOCHESS, R_PAWN },
			{ NOCHESS, R_CANON, NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS,
					R_CANON, NOCHESS },
			{ NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS,
					NOCHESS, NOCHESS },
			{ R_CAR, R_HORSE, R_ELEPHANT, R_BISHOP, R_KING, R_BISHOP,
					R_ELEPHANT, R_HORSE, R_CAR }

	};

	private int START_POINT_X;// 并非最初数据，注意考虑到图片的像素的大小
	private int START_POINT_Y;// 一般是START_POINT_X=原始点像素-图片像素的一半
	private boolean firstchoice = true;
	private int first_i;
	private int first_j;
	private int di;
	private int dj;
	int Dlgmessage;
	boolean canmove = false;
	Bitmap[] chessImage = new Bitmap[14];
	Bitmap chooseTarget;
	Bitmap chessboard;
	Bitmap rbutton;
	Bitmap lbutton;
	Bitmap dbutton;
	Bitmap rbutton2;
	Bitmap lbutton2;
	Bitmap dbutton2;
	Bitmap timebg;
	Bitmap timebg2;
	private Paint paint;
	private Resources res;
	private String message;
	private String textshow;
	public int sider = 2;
	public int status;
	public int chooseside;
	private String[] value;
	private boolean candraw = false;
	private Context mycontext;
	int chessedge;
	int cwidth, cheight;
	private ArrayList<Step> step = new ArrayList<Step>();
	// PersonRankDate gameplayer;
	Handler myhandler;
	int i = 0;
	Rect boardrect;
	Rect chessrect;
	Rect LBrect;
	Rect DBrect;
	Rect RBrect;
	Rect LBrect2;
	Rect DBrect2;
	Rect RBrect2;
	Rect Trect1;
	Rect Trect2;
	Path path;
	Path path2;
	BChessTimer timertask = null;
	int redtime = 0;
	int blacktime = 0;

	public BGameView(Context context, Handler handler) {
		super(context);
		// TODO Auto-generated constructor stub
		mycontext = context;
		myhandler = handler;
		paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setTextAlign(Align.CENTER);
		res = context.getResources();
		chooseTarget = BitmapFactory.decodeResource(res, R.drawable.choice);
		chessboard = BitmapFactory.decodeResource(res, R.drawable.line);
		int screenWidth, screenHeight;
		screenWidth = BluetoothChat.screenweight;
		screenHeight = BluetoothChat.screenheight;
		timertask = new BChessTimer();
		timertask.start();
		chessedge = (int) screenWidth / 9;
		paint.setTextSize(chessedge / 2);
		START_POINT_X = 0;
		START_POINT_Y = ((screenHeight - 9 * chessedge) / 2) - chessedge / 2;// 第一个棋子的坐标
		cwidth = START_POINT_X + 9 * chessedge;// 棋子铺满时的长宽
		cheight = START_POINT_Y + 10 * chessedge;
		boardrect = new Rect(chessedge / 2, (screenHeight - 9 * chessedge) / 2,
				chessedge / 2 + 8 * chessedge, (screenHeight - 9 * chessedge)
						/ 2 + 9 * chessedge);// 棋牌矩形
		chessrect = new Rect(0, (screenHeight / 2 - 5 * chessedge),
				9 * chessedge, screenHeight / 2 + 5 * chessedge);// 棋子点击边界
		int startbutton = (START_POINT_Y - chessedge) / 2;
		int buttonedge = chessedge;
		// 初始化按钮矩形
		LBrect = new Rect(chessedge / 2, startbutton, chessedge / 2
				+ buttonedge, startbutton + buttonedge);
		DBrect = new Rect(chessedge / 2 + 3 * buttonedge / 2, startbutton,
				chessedge / 2 + 5 * buttonedge / 2, startbutton + buttonedge);
		RBrect = new Rect(chessedge / 2 + 3 * buttonedge, startbutton,
				chessedge / 2 + 4 * buttonedge, startbutton + buttonedge);
		Trect1 = new Rect(screenWidth / 2 + chessedge / 2, startbutton,
				screenWidth - chessedge / 2, startbutton + buttonedge);

		LBrect2 = new Rect(screenWidth - (chessedge / 2 + buttonedge),
				screenHeight - (startbutton + buttonedge), screenWidth
						- chessedge / 2, screenHeight - startbutton);
		DBrect2 = new Rect(screenWidth - (chessedge / 2 + 5 * buttonedge / 2),
				screenHeight - (startbutton + buttonedge), screenWidth
						- (chessedge / 2 + 3 * buttonedge / 2), screenHeight
						- startbutton);
		RBrect2 = new Rect(screenWidth - (chessedge / 2 + 4 * buttonedge),
				screenHeight - (startbutton + buttonedge), screenWidth
						- (chessedge / 2 + 3 * buttonedge), screenHeight
						- startbutton);
		Trect2 = new Rect(chessedge / 2, screenHeight
				- (startbutton + buttonedge), screenWidth
				- (chessedge + 4 * buttonedge), screenHeight - startbutton);
		path = new Path();
		path2 = new Path();
		path.moveTo(chessedge / 2, screenHeight - startbutton - chessedge / 4);
		path.lineTo(screenWidth - (chessedge + 4 * buttonedge), screenHeight
				- startbutton - chessedge / 4);
		path2.moveTo(screenWidth - chessedge / 2, startbutton + chessedge / 4);
		path2.lineTo(screenWidth / 2 + chessedge / 2, startbutton + chessedge
				/ 4);
		// 初始图像
		rbutton = BitmapFactory.decodeResource(res, R.drawable.regret);
		lbutton = BitmapFactory.decodeResource(res, R.drawable.lose);
		dbutton = BitmapFactory.decodeResource(res, R.drawable.draw);
		rbutton2 = BitmapFactory.decodeResource(res, R.drawable.regret2);
		lbutton2 = BitmapFactory.decodeResource(res, R.drawable.lose2);
		dbutton2 = BitmapFactory.decodeResource(res, R.drawable.draw2);
		timebg = BitmapFactory.decodeResource(res, R.drawable.timebg);
		timebg2 = BitmapFactory.decodeResource(res, R.drawable.timebg2);

	}

	public void onDraw(Canvas canvas) {
		this.setBackgroundResource(R.drawable.bg);
		canvas.drawBitmap(chessboard, null, boardrect, paint);
		canvas.drawBitmap(lbutton2, null, LBrect, paint);
		canvas.drawBitmap(rbutton2, null, RBrect, paint);
		canvas.drawBitmap(dbutton2, null, DBrect, paint);
		canvas.drawBitmap(lbutton, null, LBrect2, paint);
		canvas.drawBitmap(rbutton, null, RBrect2, paint);
		canvas.drawBitmap(dbutton, null, DBrect2, paint);
		canvas.drawBitmap(timebg2, null, Trect1, paint);
		canvas.drawBitmap(timebg, null, Trect2, paint);
		canvas.drawTextOnPath("用时：" + settime(redtime), path, 0, 0, paint);
		canvas.drawTextOnPath("用时：" + settime(blacktime), path2, 0, 0, paint);

		if (candraw) {
			int i, j = 0;
			int temp;
			for (i = 0; i < 10; i++) {
				for (j = 0; j < 9; j++) {

					temp = iniposition[i][j];
					if (temp != 0) {
						canvas.drawBitmap(chessImage[temp - 1], j * chessedge
								+ START_POINT_X, i * chessedge + START_POINT_Y,
								paint);
					}

				}
				j = 0;
			}
			if (false == firstchoice) {
				canvas.drawBitmap(chooseTarget, first_j * chessedge
						+ START_POINT_X, first_i * chessedge + START_POINT_Y,
						paint);
			}
		}
	}

	public boolean onTouchEvent(MotionEvent event) {

		if (event.getAction() == MotionEvent.ACTION_DOWN) {

			if (chessrect.contains((int) event.getX(), (int) event.getY())) {
				int j = ((int) event.getX() - START_POINT_X) / chessedge;
				int i = ((int) event.getY() - START_POINT_Y) / chessedge;

				ClickandMove(i, j);
				postInvalidate();
			}

			if (LBrect2.contains((int) event.getX(), (int) event.getY())) {
				// status=BluetoothChat.STATUS_ASKLOSE;

				Dlg(BluetoothChat.MESSAGE_LOSE);
				// myhandler.sendEmptyMessage(BluetoothChat.MESSAGE_LOSE);
				// Toast.makeText(mycontext,"已发出认输请求",1000).show();

			}
			if (RBrect2.contains((int) event.getX(), (int) event.getY())) {
				// status=BluetoothChat.STATUS_REGRET;
				Dlg(BluetoothChat.MESSAGE_REGRET);
				// myhandler.sendEmptyMessage(BluetoothChat.MESSAGE_REGRET);
				// Toast.makeText(mycontext,"已发出悔棋请求",1000).show();
			}
			if (DBrect2.contains((int) event.getX(), (int) event.getY())) {
				// status=BluetoothChat.STATUS_ASKDRAW;
				Dlg(BluetoothChat.MESSAGE_DRAW);
				// myhandler.sendEmptyMessage(BluetoothChat.MESSAGE_DRAW);
				// Toast.makeText(mycontext,"已发出和棋请求",1000).show();
			}

		}
		return true;
	}

	public boolean WhoseTurn() {
		if (canmove && iniposition[first_i][first_j] > 7
				&& iniposition[first_i][first_j] < 15) {
			canmove = false;
			return true;
		} else if (canmove == false) {
			if (iniposition[first_i][first_j] < 8) {
				Toast.makeText(mycontext, "你不能碰对方的棋", 1000).show();
			} else {
				Toast.makeText(mycontext, "不是你出招的时候", 1000).show();
			}
			firstchoice = true;
			return false;
		} else if (sider == 2) {
			Toast.makeText(mycontext, "你不能碰对方的棋", 1000).show();
			firstchoice = true;
			return false;
		}
		return false;

	}

	public void ClickandMove(int x, int y) {
		if (true == firstchoice) {
			first_i = x;
			first_j = y;
			if (iniposition[x][y] != 0) {
				firstchoice = false;
			}
		} else if (false == firstchoice && first_i == x && first_j == y) {
			firstchoice = true;
		} else {

			if (CanMove(x, y) && WhoseTurn()) {
				di = x;
				dj = y;
				MessageSend(BluetoothChat.STATUS_NEXTSTEP);
				myhandler.sendEmptyMessage(BluetoothChat.MESSAGE_SOUND);
				canmove = false;
				step.add(new Step(first_i, first_j,
						iniposition[first_i][first_j], di, dj,
						iniposition[di][dj]));
				if (iniposition[x][y] == 1) {
					Toast.makeText(mycontext, "恭喜你获得胜利，请重新开棋", 1000).show();
					canmove = false;
					// candraw=false;
					timertask.Clear();
				}
				iniposition[x][y] = iniposition[first_i][first_j];
				iniposition[first_i][first_j] = 0;
				// firstchoice=true;
			}
			firstchoice = true;

		}
	}

	private boolean CanMove(int i, int j) {

		// /判断能否移动代码
		int choice = iniposition[first_i][first_j];

		if (isTheSameSide(i, j)) {// 判断是自己人
			return false;
		}
		switch (choice) {
		case 1:
		case 8:
			return Shuaimove(i, j);

		case 2:
		case 9:
			return Shimove(i, j);
		case 3:
		case 10:
			return Xiangmove(i, j);
		case 4:
		case 11:
			return Mamove(i, j);
		case 5:
		case 12:
			return Jumove(i, j);
		case 6:
		case 13:
			return Paomove(i, j);
		case 7:
		case 14:
			return Bingmove(i, j);
		default:
			return false;

		}

	}

	private boolean isTheSameSide(int i, int j)// 判断是否为自己人
	{
		if ((iniposition[first_i][first_j] < 8 && iniposition[i][j] < 8 && iniposition[i][j] > 0)
				|| (iniposition[first_i][first_j] > 7 && iniposition[i][j] > 7 && iniposition[i][j] < 15)) {
			return true;
		}
		return false;
	}

	private boolean isMoveNear(int i, int j) {

		if (j == first_j && (i == first_i + 1 || i == first_i - 1)) {

			return true;
		} else if (i == first_i && (j == first_j + 1 || j == first_j - 1)) {

			return true;
		}
		return false;
	}

	private boolean Shuaimove(int i, int j) {

		// 1.移动地方有敌人判断能否移动到2.移动地方是自己人，return false3.移动地方没人判断能否移动到
		if (iniposition[first_i][first_j] != 0) {

			if (iniposition[first_i][first_j] == B_KING)// 黑子帅
			{
				if (i > 2 || i < 0 || j > 5 || j < 3) {
					return false;
				} else if (isMoveNear(i, j)) {

					for (int x = i; x < 10; x++) {
						if (iniposition[x][j] != 0
								&& iniposition[x][j] != R_KING) {
							return true;
						} else if (iniposition[x][j] == R_KING) {
							return false;
						}
					}
					return true;

				}
			} else if (iniposition[first_i][first_j] == R_KING)// 红子帅
			{
				if (i > 9 || i < 7 || j > 5 || j < 3) {
					return false;
				} else if (isMoveNear(i, j)) {

					for (int x = i; x >= 0; x--) {
						if (iniposition[x][j] != 0
								&& iniposition[x][j] != B_KING) {
							return true;
						} else if (iniposition[x][j] == B_KING) {
							return false;
						}
					}
					return true;
				}
			}

		}

		else {

			if (i > 2 || i < 0 || j > 5 || j < 2) {
				return false;
			} else if (isMoveNear(i, j)) {

				return true;
			}
		}
		return false;
	}

	private boolean Shimove(int i, int j) {
		if (iniposition[first_i][first_j] == B_BISHOP) {
			if (first_i == 1 && first_j == 4) {
				if ((i == 0 && j == 3) || (i == 0 && j == 5)
						|| (i == 2 && j == 3) || (i == 2 && j == 5)) {

					return true;
				} else {
					return false;
				}
			} else {
				if (i == 1 && j == 4) {
					return true;
				}
			}
		} else {
			if (first_i == 8 && first_j == 4) {
				if ((i == 7 && j == 3) || (i == 7 && j == 5)
						|| (i == 9 && j == 3) || (i == 9 && j == 5)) {
					return true;
				} else {
					return false;
				}
			} else {
				if (i == 8 && j == 4) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean Xiangmove(int i, int j) {
		if (iniposition[first_i][first_j] == B_ELEPHANT && i > 4) {
			return false;
		} else if (iniposition[first_i][first_j] == R_ELEPHANT && i < 5) {
			return false;
		} else if ((i - first_i == 2 || i - first_i == -2)
				&& (j - first_j == 2 || j - first_j == -2)) {
			if (iniposition[(i + first_i) / 2][(j + first_j) / 2] == 0)
				return true;
			else
				return false;
		}

		return false;
	}

	private boolean Mamove(int i, int j) {
		if (((i - first_i == 1 || i - first_i == -1) && (j - first_j == 2 || j
				- first_j == -2))
				|| ((i - first_i == 2 || i - first_i == -2) && (j - first_j == 1 || j
						- first_j == -1))) {
			if (j - first_j == -2 && iniposition[first_i][first_j - 1] == 0) {
				return true;
			} else if (j - first_j == 2
					&& iniposition[first_i][first_j + 1] == 0) {
				return true;
			} else if (i - first_i == 2
					&& iniposition[first_i + 1][first_j] == 0) {
				return true;
			} else if (i - first_i == -2
					&& iniposition[first_i - 1][first_j] == 0) {
				return true;
			}
		}
		return false;
	}

	private boolean Jumove(int i, int j) {
		if (i == first_i) {
			if (j > first_j) {
				for (int x = first_j + 1; x < j; x++) {
					if (iniposition[i][x] != 0)
						return false;
				}
				return true;
			} else {
				for (int x = first_j - 1; x > j; x--) {
					if (iniposition[i][x] != 0)
						return false;
				}
				return true;
			}
		} else if (j == first_j) {
			if (i > first_i) {
				for (int x = first_i + 1; x < i; x++) {
					if (iniposition[x][j] != 0)
						return false;
				}
				return true;
			} else {
				for (int x = first_i - 1; x > i; x--) {
					if (iniposition[x][j] != 0)
						return false;
				}
				return true;
			}
		}
		return false;
	}

	private boolean Paomove(int i, int j) {
		int count = 0;
		if (i == first_i) {
			if (j > first_j) {
				for (int x = first_j + 1; x < j; x++) {
					if (iniposition[i][x] != 0)
						count++;
				}

			} else {
				for (int x = first_j - 1; x > j; x--) {
					if (iniposition[i][x] != 0)
						count++;
				}

			}
			if (count == 0 && iniposition[i][j] == 0)
				return true;
			else if (count == 1 && iniposition[i][j] != 0)
				return true;
		} else if (j == first_j) {
			if (i > first_i) {
				for (int x = first_i + 1; x < i; x++) {
					if (iniposition[x][j] != 0)
						count++;
				}

			} else {
				for (int x = first_i - 1; x > i; x--) {
					if (iniposition[x][j] != 0)
						count++;
				}

			}
			if (count == 0 && iniposition[i][j] == 0)
				return true;
			else if (count == 1 && iniposition[i][j] != 0)
				return true;
		}

		return false;
	}

	private boolean Bingmove(int i, int j) {
		if (iniposition[first_i][first_j] == B_PAWN) {
			if (i < 5) {
				if (i == first_i + 1 && j == first_j)
					return true;
			} else {
				if (i == first_i - 1 && j == first_j)
					return false;
				else
					return isMoveNear(i, j);
			}
		} else if (iniposition[first_i][first_j] == R_PAWN) {
			if (i > 4) {
				if (i == first_i - 1 && j == first_j)
					return true;
			} else {
				if (i == first_i + 1 && j == first_j)
					return false;
				else
					return isMoveNear(i, j);
			}
		}
		return false;
	}

	public void MessageSend(int st) {
		status = st;
		message = Integer.toString(st) + "a" + Integer.toString(first_i) + "a"
				+ Integer.toString(first_j) + "a" + Integer.toString(di) + "a"
				+ Integer.toString(dj);
		Message msg = myhandler.obtainMessage(BluetoothChat.MESSAGE_NEXTSTEP);
		Bundle bundle = new Bundle();
		bundle.putString("STEP", message);
		msg.setData(bundle);
		myhandler.sendMessage(msg);

	}

	public void RecieveMessage(String msg) {

		value = msg.split("a");
		int st = Integer.parseInt(value[0]);
		int ffi = Integer.parseInt(value[1]);
		int ffj = Integer.parseInt(value[2]);
		int ddi = Integer.parseInt(value[3]);
		int ddj = Integer.parseInt(value[4]);
		switch (st) {
		case BluetoothChat.STATUS_AGREE:
			DealWithAgreement();
			break;
		case BluetoothChat.STATUS_DISAGREE:
			Toast.makeText(mycontext, "对方不同意你的行为", 1000).show();
			break;
		case BluetoothChat.STATUS_RED:
			AskSide(BluetoothChat.STATUS_RED);
			break;
		case BluetoothChat.STATUS_BLACK:
			AskSide(BluetoothChat.STATUS_BLACK);
			break;
		case BluetoothChat.STATUS_ASKDRAW:
			textshow = "对方求和";
			Request();
			break;
		case BluetoothChat.STATUS_ASKLOSE:
			textshow = "对方认输";
			Request();
			break;
		case BluetoothChat.STATUS_NEXTSTEP:
			step.add(new Step(9 - ffi, 8 - ffj, iniposition[9 - ffi][8 - ffj],
					9 - ddi, 8 - ddj, iniposition[9 - ddi][8 - ddj]));
			canmove = true;
			if (iniposition[9 - ddi][8 - ddj] == 8) {
				Toast.makeText(mycontext, "很遗憾，你输掉了此局...", 1000).show();
				canmove = false;
				candraw = false;
				timertask.Clear();
			}
			iniposition[9 - ddi][8 - ddj] = iniposition[9 - ffi][8 - ffj];
			iniposition[9 - ffi][8 - ffj] = 0;
			myhandler.sendEmptyMessage(BluetoothChat.MESSAGE_SOUND);
			postInvalidate();
			break;
		case BluetoothChat.STATUS_ASKAGAIN:
			break;
		case BluetoothChat.STATUS_REGRET:
			textshow = "对方请求悔棋";
			Request();
			break;

		}

	}

	public void RestartGame() {

		step.clear();
		for (int ii = 0; ii < 10; ii++) {
			for (int jj = 0; jj < 9; jj++) {
				iniposition[ii][jj] = startposition[ii][jj];
			}
		}
		postInvalidate();
	}

	public void BackStep() {

		if (step.size() > 0) {
			Step temp = step.get(step.size() - 1);
			step.remove(step.size() - 1);
			iniposition[temp.i][temp.j] = temp.x;
			iniposition[temp.ii][temp.jj] = temp.xx;
			if (canmove) // 同时要把下子权给悔棋的人
			{
				canmove = false;
			} else {
				canmove = true;
			}
			postInvalidate();
		}
	}

	public void ChooseSide(int who) {
		if (who == 1)// 代表别人来申请0代表自己申请
		{
			if (chooseside == BluetoothChat.STATUS_RED) {
				chooseside = BluetoothChat.STATUS_BLACK;
			} else {
				chooseside = BluetoothChat.STATUS_RED;
			}
		}
		if (chooseside == BluetoothChat.STATUS_RED) {

			int size = BitmapFactory.decodeResource(res, R.drawable.bbishop)
					.getHeight();
			Matrix mMatrix = new Matrix();
			float Scale = (float) ((float) chessedge / (float) size);
			mMatrix.reset();
			mMatrix.postScale(Scale, Scale);
			chooseTarget = Bitmap.createBitmap(
					BitmapFactory.decodeResource(res, R.drawable.choice), 0, 0,
					size, size, mMatrix, true);
			chessImage[7] = Bitmap.createBitmap(
					BitmapFactory.decodeResource(res, R.drawable.rking), 0, 0,
					size, size, mMatrix, true);
			chessImage[8] = Bitmap.createBitmap(
					BitmapFactory.decodeResource(res, R.drawable.rcar), 0, 0,
					size, size, mMatrix, true);
			chessImage[9] = Bitmap.createBitmap(
					BitmapFactory.decodeResource(res, R.drawable.rhorse), 0, 0,
					size, size, mMatrix, true);
			chessImage[10] = Bitmap.createBitmap(
					BitmapFactory.decodeResource(res, R.drawable.rcanon), 0, 0,
					size, size, mMatrix, true);
			chessImage[11] = Bitmap.createBitmap(
					BitmapFactory.decodeResource(res, R.drawable.rbishop), 0,
					0, size, size, mMatrix, true);
			chessImage[12] = Bitmap.createBitmap(
					BitmapFactory.decodeResource(res, R.drawable.relephant), 0,
					0, size, size, mMatrix, true);
			chessImage[13] = Bitmap.createBitmap(
					BitmapFactory.decodeResource(res, R.drawable.rpawn), 0, 0,
					size, size, mMatrix, true);
			mMatrix.postRotate(180);
			chessImage[0] = Bitmap.createBitmap(
					BitmapFactory.decodeResource(res, R.drawable.bking), 0, 0,
					size, size, mMatrix, true);
			chessImage[1] = Bitmap.createBitmap(
					BitmapFactory.decodeResource(res, R.drawable.bcar), 0, 0,
					size, size, mMatrix, true);
			chessImage[2] = Bitmap.createBitmap(
					BitmapFactory.decodeResource(res, R.drawable.bhorse), 0, 0,
					size, size, mMatrix, true);
			chessImage[3] = Bitmap.createBitmap(
					BitmapFactory.decodeResource(res, R.drawable.bcanon), 0, 0,
					size, size, mMatrix, true);
			chessImage[4] = Bitmap.createBitmap(
					BitmapFactory.decodeResource(res, R.drawable.bbishop), 0,
					0, size, size, mMatrix, true);
			chessImage[5] = Bitmap.createBitmap(
					BitmapFactory.decodeResource(res, R.drawable.belephant), 0,
					0, size, size, mMatrix, true);
			chessImage[6] = Bitmap.createBitmap(
					BitmapFactory.decodeResource(res, R.drawable.bpawn), 0, 0,
					size, size, mMatrix, true);
			canmove = true;
			candraw = true;
			timertask.Clear();
		} else if (chooseside == BluetoothChat.STATUS_BLACK) {

			int size = BitmapFactory.decodeResource(res, R.drawable.bbishop)
					.getHeight();
			Matrix mMatrix = new Matrix();
			float Scale = (float) ((float) chessedge / (float) size);
			mMatrix.reset();
			mMatrix.postScale(Scale, Scale);
			chooseTarget = Bitmap.createBitmap(
					BitmapFactory.decodeResource(res, R.drawable.choice), 0, 0,
					size, size, mMatrix, true);
			chessImage[7] = Bitmap.createBitmap(
					BitmapFactory.decodeResource(res, R.drawable.bking), 0, 0,
					size, size, mMatrix, true);
			chessImage[8] = Bitmap.createBitmap(
					BitmapFactory.decodeResource(res, R.drawable.bcar), 0, 0,
					size, size, mMatrix, true);
			chessImage[9] = Bitmap.createBitmap(
					BitmapFactory.decodeResource(res, R.drawable.bhorse), 0, 0,
					size, size, mMatrix, true);
			chessImage[10] = Bitmap.createBitmap(
					BitmapFactory.decodeResource(res, R.drawable.bcanon), 0, 0,
					size, size, mMatrix, true);
			chessImage[11] = Bitmap.createBitmap(
					BitmapFactory.decodeResource(res, R.drawable.bbishop), 0,
					0, size, size, mMatrix, true);
			chessImage[12] = Bitmap.createBitmap(
					BitmapFactory.decodeResource(res, R.drawable.belephant), 0,
					0, size, size, mMatrix, true);
			chessImage[13] = Bitmap.createBitmap(
					BitmapFactory.decodeResource(res, R.drawable.bpawn), 0, 0,
					size, size, mMatrix, true);
			mMatrix.postRotate(180);
			chessImage[0] = Bitmap.createBitmap(
					BitmapFactory.decodeResource(res, R.drawable.rking), 0, 0,
					size, size, mMatrix, true);
			chessImage[1] = Bitmap.createBitmap(
					BitmapFactory.decodeResource(res, R.drawable.rcar), 0, 0,
					size, size, mMatrix, true);
			chessImage[2] = Bitmap.createBitmap(
					BitmapFactory.decodeResource(res, R.drawable.rhorse), 0, 0,
					size, size, mMatrix, true);
			chessImage[3] = Bitmap.createBitmap(
					BitmapFactory.decodeResource(res, R.drawable.rcanon), 0, 0,
					size, size, mMatrix, true);
			chessImage[4] = Bitmap.createBitmap(
					BitmapFactory.decodeResource(res, R.drawable.rbishop), 0,
					0, size, size, mMatrix, true);
			chessImage[5] = Bitmap.createBitmap(
					BitmapFactory.decodeResource(res, R.drawable.relephant), 0,
					0, size, size, mMatrix, true);
			chessImage[6] = Bitmap.createBitmap(
					BitmapFactory.decodeResource(res, R.drawable.rpawn), 0, 0,
					size, size, mMatrix, true);
			candraw = true;
			timertask.Clear();

		}
		RestartGame();
		postInvalidate();
	}

	public void AskSide(int as) {
		chooseside = as;
		new AlertDialog.Builder(mycontext)
				.setTitle("对方请求选择")
				.setMessage(as == BluetoothChat.STATUS_RED ? "红方" : "黑方")
				.setPositiveButton("同意", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						myhandler.sendEmptyMessage(BluetoothChat.MESSAGE_AGREE);
						ChooseSide(1);
					}

				})
				.setNegativeButton("不同意",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {

								myhandler
										.sendEmptyMessage(BluetoothChat.MESSAGE_DISAGREE);
							}

						}).show();

	}

	public void Request() {
		new AlertDialog.Builder(mycontext)
				.setTitle("对方有意见了")
				.setMessage(textshow)
				.setPositiveButton("同意", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						myhandler.sendEmptyMessage(BluetoothChat.MESSAGE_AGREE);
						if (textshow.equals("对方请求悔棋")) {
							BackStep();
						} else {
							candraw = false;
							canmove = false;
							timertask.Clear();
							Toast.makeText(mycontext, "请重新开棋", 1000).show();
						}

					}

				})
				.setNegativeButton("不同意",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {

								myhandler
										.sendEmptyMessage(BluetoothChat.MESSAGE_DISAGREE);
							}

						}).show();
	}

	public void Dlg(int meg) {
		Dlgmessage = meg;
		new AlertDialog.Builder(mycontext).setTitle("真的要发送送吗")
				.setMessage(textshow)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						myhandler.sendEmptyMessage(Dlgmessage);
						if (Dlgmessage == BluetoothChat.MESSAGE_LOSE) {
							status = BluetoothChat.STATUS_ASKLOSE;
						}
						if (Dlgmessage == BluetoothChat.MESSAGE_DRAW) {
							status = BluetoothChat.STATUS_ASKDRAW;
						}
						if (Dlgmessage == BluetoothChat.MESSAGE_REGRET) {
							status = BluetoothChat.STATUS_REGRET;
						}

					}

				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

					}

				}).show();
	}

	public void DealWithAgreement() {
		switch (status) {

		case BluetoothChat.STATUS_RED:
			chooseside = status;
			ChooseSide(0);
			break;
		case BluetoothChat.STATUS_BLACK:
			chooseside = status;
			ChooseSide(0);
			break;
		case BluetoothChat.STATUS_ASKDRAW:
			candraw = false;
			canmove = false;
			timertask.Clear();
			Toast.makeText(mycontext, "同意和棋", 1000).show();
			break;
		case BluetoothChat.STATUS_ASKLOSE:
			candraw = false;
			canmove = false;
			timertask.Clear();
			Toast.makeText(mycontext, "同意认输", 1000).show();
			break;
		case BluetoothChat.STATUS_REGRET:
			BackStep();
			break;

		}

	}

	public String settime(int time) {
		int sec = time % 60;
		int min = time / 60;
		if (sec < 10 && min < 10) {
			return "0" + Integer.toString(min) + "：0" + Integer.toString(sec);
		} else if (sec >= 10 && min < 10) {
			return "0" + Integer.toString(min) + "：" + Integer.toString(sec);
		} else if (sec < 10 && min >= 10) {
			return Integer.toString(min) + "：0" + Integer.toString(sec);
		} else {
			return Integer.toString(min) + "：" + Integer.toString(sec);
		}

	}

	class BChessTimer extends Thread {

		public void Clear() {
			redtime = 0;
			blacktime = 0;
		}

		public void run() {
			while (!Thread.currentThread().isInterrupted()) {

				if (canmove) {
					redtime++;
				} else if (candraw) {
					blacktime++;
				}

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
				postInvalidate();
			}
		}

	}

}
