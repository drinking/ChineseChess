package drinking.android.algorithm;

public class CHESSMOVE {
	public int nChessID;  //表明是什么棋子
	//CHESSMANPOS From;//起始位置
	//CHESSMANPOS To;  //走到什么位置
	public int Fromx;
	public int Fromy;
	public int Tox;
	public int Toy;
	public int Score;


}
class CHESSMANPOS
{
	int x;
	int y;
}
class UNDOMOVE
{
	CHESSMOVE cmChessMove;
	int nChessID;//被吃掉的棋子
}
class mPOINT
{
	int x;
	int y;
}