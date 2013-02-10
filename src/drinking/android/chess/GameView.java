package drinking.android.chess;

import java.util.ArrayList;

import org.drinking.utils.DrawUtils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class GameView extends View {

	enum EVENT {
		LOSE, DRAW, REGRET
	}

	GameController gameController;
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

	final int vspeople = 0;
	int vsmodle;
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

	private int START_POINT_X = 13;// 并非最初数据，注意考虑到图片的像素的大小
	private int START_POINT_Y = 18;// 一般是START_POINT_X=原始点像素-图片像素的一半
	private boolean firstchoice = true;
	private int first_i;
	private int first_j;
	private int di;
	private int dj;
	int redtime = 0;
	int blacktime = 0;
	boolean canmove = false;
	Bitmap[] chessImage = new Bitmap[14];
	Bitmap chooseTarget;
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
	Role role;

	private boolean candraw = false;
	private Context mycontext;
	int chessedge;
	int cwidth, cheight;
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
	private ArrayList<Step> step = new ArrayList<Step>();
	int i = 0;
	Bitmap chessBoard;

	public GameView(Context context, GameController controller) {
		super(context);
		gameController = controller;
		mycontext = context;
		paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setTextAlign(Align.CENTER);
		res = context.getResources();
		int screenWidth = controller.getScreenWidth();
		int screenHeight = controller.getScreenHeight();

		chessedge = (int) screenWidth / 9;
		paint.setTextSize(chessedge / 2);
		START_POINT_X = (screenWidth - 9 * chessedge) / 2;
		START_POINT_Y = ((screenHeight - 9 * chessedge) / 2) - chessedge / 2;// 第一个棋子的坐标
		cwidth = START_POINT_X + 9 * chessedge;// 棋子铺满时的长宽
		cheight = START_POINT_Y + 10 * chessedge;
		boardrect = new Rect(chessedge / 2, (screenHeight - 9 * chessedge) / 2,
				chessedge / 2 + 8 * chessedge, (screenHeight - 9 * chessedge)
						/ 2 + 9 * chessedge);// 棋牌矩形
		chessrect = new Rect(0, (screenHeight / 2 - 5 * chessedge),
				9 * chessedge, screenHeight / 2 + 5 * chessedge);// 棋牌矩形
		int startbutton = (START_POINT_Y - chessedge) / 2;
		int buttonedge = chessedge;
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
		// //////////////////////////////////////////////////////////////////
		rbutton = BitmapFactory.decodeResource(res, R.drawable.regret);
		lbutton = BitmapFactory.decodeResource(res, R.drawable.lose);
		dbutton = BitmapFactory.decodeResource(res, R.drawable.draw);
		rbutton2 = BitmapFactory.decodeResource(res, R.drawable.regret2);
		lbutton2 = BitmapFactory.decodeResource(res, R.drawable.lose2);
		dbutton2 = BitmapFactory.decodeResource(res, R.drawable.draw2);
		timebg = BitmapFactory.decodeResource(res, R.drawable.timebg);
		timebg2 = BitmapFactory.decodeResource(res, R.drawable.timebg2);
		chessBoard = new DrawUtils(screenWidth, screenHeight).drawChessboard();
	}

	public void onDraw(Canvas canvas) {
		this.setBackgroundResource(R.drawable.yangpi);
		// canvas.drawBitmap(chessboard, null, boardrect, paint);
		canvas.drawBitmap(chessBoard, 0, 0, paint);
		// canvas.drawBitmap(lbutton2, null, LBrect, paint);
		// canvas.drawBitmap(rbutton2, null, RBrect, paint);
		// canvas.drawBitmap(dbutton2, null, DBrect, paint);
		// canvas.drawBitmap(lbutton, null, LBrect2, paint);
		// canvas.drawBitmap(rbutton, null, RBrect2, paint);
		// canvas.drawBitmap(dbutton, null, DBrect2, paint);
		// canvas.drawBitmap(timebg2, null, Trect1, paint);
		// canvas.drawBitmap(timebg, null, Trect2, paint);
		//
		// canvas.drawTextOnPath("用时：" + settime(redtime), path, 0, 0, paint);
		// canvas.drawTextOnPath("用时：" + settime(blacktime), path2, 0, 0,
		// paint);

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

			if (LBrect.contains((int) event.getX(), (int) event.getY())
					|| LBrect2.contains((int) event.getX(), (int) event.getY())) {
				gameController.onEvent(EVENT.LOSE);
			}
			if (RBrect.contains((int) event.getX(), (int) event.getY())
					|| RBrect2.contains((int) event.getX(), (int) event.getY())) {
				gameController.onEvent(EVENT.REGRET);
			}
			if (DBrect.contains((int) event.getX(), (int) event.getY())
					|| DBrect2.contains((int) event.getX(), (int) event.getY())) {
				gameController.onEvent(EVENT.DRAW);
			}
		}
		return true;
	}

	public boolean WhoseTurn() {
		if (role.empty()) {
			Toast.makeText(mycontext, "有没有选过顺序啊", Toast.LENGTH_SHORT).show();
			return false;
		} else if (role.canMove(iniposition[first_i][first_j])&&gameController.canMove(iniposition[first_i][first_j])) {
			role.changeRole();
			return true;
		} else {
			Toast.makeText(mycontext, "不是你出招的时候", Toast.LENGTH_SHORT).show();
			return false;
		}
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
				gameController.playMoveSound();
				gameController.onMove(first_i, first_j, di, dj);
				step.add(new Step(first_i, first_j,
						iniposition[first_i][first_j], di, dj,
						iniposition[di][dj]));
				if (iniposition[x][y] == 1 || iniposition[x][y] == 8) { // 将军判定

					iniposition[x][y] = iniposition[first_i][first_j];
					iniposition[first_i][first_j] = 0;
					postInvalidate();

					// String title = (temp == 1 && chooseside == 0 || temp == 8
					// && chooseside == 1) ? "红方胜利" : "黑方胜利";
					String title = "比赛结束";
					gameController.onEndDialog(title);

				}
				iniposition[x][y] = iniposition[first_i][first_j];
				iniposition[first_i][first_j] = 0;
			}
			firstchoice = true;
		}
	}

	public void moveStep(Point[] p) {
		role.changeRole();
		step.add(new Step(p[0].x, p[0].y, iniposition[p[0].x][p[0].y], p[1].x,
				p[1].y, iniposition[p[1].x][p[1].y]));
		canmove = true;
		if (iniposition[p[1].x][p[1].y] == 8) {
			//in bluetooth model
			Toast.makeText(mycontext, "很遗憾，你输掉了此局...", 1000).show();
		}
		iniposition[p[1].x][p[1].y] = iniposition[p[0].x][p[0].y];
		iniposition[p[0].x][p[0].y] = 0;
		postInvalidate();
	}

	private boolean CanMove(int i, int j) {

		// /判断能否移动代码
		int choice = iniposition[first_i][first_j];

		if (isTheSameSide(i, j)) {// 判断是自己人
			return false;
		}
		switch (choice) {
		case B_KING:
		case R_KING:
			return Shuaimove(i, j);

		case B_BISHOP:
		case R_BISHOP:
			return Shimove(i, j);

		case B_ELEPHANT:
		case R_ELEPHANT:
			return Xiangmove(i, j);

		case B_HORSE:
		case R_HORSE:
			return Mamove(i, j);

		case B_CAR:
		case R_CAR:
			return Jumove(i, j);

		case B_CANON:
		case R_CANON:
			return Paomove(i, j);
		case B_PAWN:
		case R_PAWN:
			return Bingmove(i, j);
		default:
			return false;

		}

	}

	private boolean isTheSameSide(int i, int j) { // 判断是否为自己人
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

			if (iniposition[first_i][first_j] == B_KING) { // 黑子帅
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
			} else if (iniposition[first_i][first_j] == R_KING) { // 红子帅
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

	public void initChess() {
		redtime = 0;
		blacktime = 0;
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
			role.changeRole();
			postInvalidate();
		}
	}

	private int[] redResource = new int[] { R.drawable.rking, R.drawable.rcar,
			R.drawable.rhorse, R.drawable.rcanon, R.drawable.rbishop,
			R.drawable.relephant, R.drawable.rpawn };
	private int[] blackResource = new int[] { R.drawable.bking,
			R.drawable.bcar, R.drawable.bhorse, R.drawable.bcanon,
			R.drawable.bbishop, R.drawable.belephant, R.drawable.bpawn };

	private void renderChess(boolean toRed) {
		int[] chessRes = toRed ? redResource : blackResource;
		int size = BitmapFactory.decodeResource(res, R.drawable.bbishop)
				.getHeight();
		Matrix mMatrix = new Matrix();
		float Scale = (float) ((float) chessedge / (float) size);
		mMatrix.reset();
		mMatrix.postScale(Scale, Scale);
		chooseTarget = Bitmap.createBitmap(
				BitmapFactory.decodeResource(res, R.drawable.choice), 0, 0,
				size, size, mMatrix, true);
		for (int i = 0; i < chessRes.length; i++) {
			chessImage[i + 7] = Bitmap.createBitmap(
					BitmapFactory.decodeResource(res, chessRes[i]), 0, 0, size,
					size, mMatrix, true);
		}
		chessRes = !toRed ? redResource : blackResource;
		mMatrix.postRotate(180);
		for (int i = 0; i < chessRes.length; i++) {
			chessImage[i] = Bitmap.createBitmap(
					BitmapFactory.decodeResource(res, chessRes[i]), 0, 0, size,
					size, mMatrix, true);
		}

		if(toRed){
			role=new Role(7, 15);
		}else{
			role=new Role(0,8);
		}
		candraw = true;
		postInvalidate();
	}

	public void setFirstGo(boolean first) {
		renderChess(first);
	}

}

class Step// 可以作为发送的类
{
	public int i, j, x, ii, jj, xx;

	public Step(int i, int j, int x, int ii, int jj, int xx) {
		this.i = i;
		this.j = j;
		this.x = x;
		this.ii = ii;
		this.jj = jj;
		this.xx = xx;
	}

}
