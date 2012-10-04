package drinking.android.algorithm;
import java.lang.Math;

public class MoveGenerator {
	
	public MoveGenerator()
	{
		//public CHESSMOVE [][]m_MoveList=new CHESSMOVE[8][80];
		for(int i=0;i<8;i++)
		{
			for(int j=0;j<80;j++)
			{
				m_MoveList[i][j]=null;
			}
		}
	}
	
	
	int AddMove(int nFromX,int nFromY,int nToX,int nToY,int nPly,int nChessID)
	{
		/*
		Log.i("nFromX", Integer.toString(nFromX));
		Log.i("nFromY", Integer.toString(nFromY));
		Log.i("nToX", Integer.toString(nToX));
		Log.i("nToY", Integer.toString(nToY));
		Log.i("nPly", Integer.toString(nPly));
		Log.i("nChessID", Integer.toString(nChessID));
		Log.i("m_nMoveCount", Integer.toString(m_nMoveCount));*/
		//CHESSMOVE
		if(m_MoveList[nPly][m_nMoveCount]==null)
		m_MoveList[nPly][m_nMoveCount]=new CHESSMOVE();
		
		m_MoveList[nPly][m_nMoveCount].Fromx=nFromX;
		m_MoveList[nPly][m_nMoveCount].Fromy=nFromY;
		m_MoveList[nPly][m_nMoveCount].Tox=nToX;
		m_MoveList[nPly][m_nMoveCount].Toy=nToY;
		m_MoveList[nPly][m_nMoveCount].nChessID=nChessID;
		m_nMoveCount++;
		
		return m_nMoveCount;
	}

	//nPly指明当前搜索的层数，每层将走法存在不同的位置，以免覆盖
	//nSide指明产生哪一方的走法，true为红方，false是黑方
	int CreatePossibleMove(int position[][],int nPly,int Side,int nUserChessColor)
	{
		boolean nSide;
		if(Side==1)nSide=true;
		else nSide=false;
		int nChessID;
		int i,j;
		
		m_nMoveCount=0;
		m_nUserChessColor=nUserChessColor;

		for(j=0;j<9;j++)
			for(i=0;i<10;i++)
			{
				if(position[i][j]!=NOCHESS)
				{
					nChessID=position[i][j];

					if(nUserChessColor==REDCHESS)
					{
						if(!nSide && IsRed(nChessID))
							continue;//如要产生黑棋走法，跳过红棋
						
						if(nSide && IsBlack(nChessID))
							continue;//如要产生红棋走法，跳过黑棋
					}
					else
					{
						if(nSide && IsRed(nChessID))
							continue;//如要产生黑棋走法，跳过红棋
						
						if(!nSide && IsBlack(nChessID))
							continue;//如要产生红棋走法，跳过黑棋
					}
					
					switch(nChessID)
					{
					case R_KING://红帅
					case B_KING://黑将
						Gen_KingMove(position,i,j,nPly);
						break;

					case R_BISHOP://红士
						Gen_RBishopMove(position,i,j,nPly);
						break;
					
					case B_BISHOP://黑士
						Gen_BBishopMove(position,i,j,nPly);
						break;
					
					case R_ELEPHANT://红相
					case B_ELEPHANT://黑象
						Gen_ElephantMove(position,i,j,nPly);
						break;
					
					case R_HORSE://红马
					case B_HORSE://黑马
						Gen_HorseMove(position,i,j,nPly);
						break;
					
					case R_CAR://红车
					case B_CAR://黑车
						
						Gen_CarMove(position,i,j,nPly);
						
						break;

					case R_PAWN://红兵
						Gen_RPawnMove(position,i,j,nPly);
						break;
					
					case B_PAWN://黑卒
						Gen_BPawnMove(position,i,j,nPly);
						break;
					
					case B_CANON://黑炮
					case R_CANON://红炮
						Gen_CanonMove(position,i,j,nPly);
						break;
					
					default:
						break;
					}
				}
			}
		
		return m_nMoveCount;
	}

	void Gen_KingMove(int position[][],int i,int j,int nPly)
	{
		int x,y;
		
		for(y=0;y<3;y++)
			for(x=3;x<6;x++)
				if(IsValidMove(position,j,i,x,y,m_nUserChessColor))
					AddMove(j,i,x,y,nPly,position[i][j]);

		for(y=7;y<10;y++)
			for(x=3;x<6;x++)
				if(IsValidMove(position,j,i,x,y,m_nUserChessColor))
					AddMove(j,i,x,y,nPly,position[i][j]);
	}

	//红士
	void Gen_RBishopMove(int position[][],int i,int j,int nPly)
	{
		int x,y;
		
		for(y=7;y<10;y++)
			for(x=3;x<6;x++)
				if(IsValidMove(position,j,i,x,y,m_nUserChessColor))
					AddMove(j,i,x,y,nPly,position[i][j]);
	}

	//黑士
	void Gen_BBishopMove(int position[][],int i,int j,int nPly)
	{
		int x,y;

		for(y=0;y<3;y++)
			for(x=3;x<6;x++)
				if(IsValidMove(position,j,i,x,y,m_nUserChessColor))
					AddMove(j,i,x,y,nPly,position[i][j]);
	}

	//象
	void Gen_ElephantMove(int position[][],int i,int j,int nPly)
	{
		int x,y;
		
		//插入右下方的有效走法
		x=j+2;
		y=i+2;
		if(x<9 && y<10 && IsValidMove(position,j,i,x,y,m_nUserChessColor))
			AddMove(j,i,x,y,nPly,position[i][j]);
		
		//插入右上方的有效走法
		x=j+2;
		y=i-2;
		if(x<9 && y>=0 && IsValidMove(position,j,i,x,y,m_nUserChessColor))
			AddMove(j,i,x,y,nPly,position[i][j]);
		
		//插入左下方的有效走法
		x=j-2;
		y=i+2;
		if(x>=0 && y<10 && IsValidMove(position,j,i,x,y,m_nUserChessColor))
			AddMove(j,i,x,y,nPly,position[i][j]);
		
		//插入左上方的有效走法
		x=j-2;
		y=i-2;
		if(x>=0 && y>=0 && IsValidMove(position,j,i,x,y,m_nUserChessColor))
			AddMove(j,i,x,y,nPly,position[i][j]);
	}

	//马
	void Gen_HorseMove(int position[][], int i, int j, int nPly)
	{
		int x, y;
		
		//插入右下方的有效走法
		x=j+2;//右2
		y=i+1;//下1
		if((x<9 && y<10) && IsValidMove(position,j,i,x,y,m_nUserChessColor))
			AddMove(j,i,x,y,nPly,position[i][j]);
		
		//插入右上方的有效走法
		x=j+2;//右2
		y=i-1;//上1
		if((x<9 && y>=0) && IsValidMove(position,j,i,x,y,m_nUserChessColor))
			AddMove(j,i,x,y,nPly,position[i][j]);
		
		//插入左下方的有效走法
		x=j-2;//左2
		y=i+1;//下1
		if((x>=0 && y<10) && IsValidMove(position,j,i,x,y,m_nUserChessColor))
			AddMove(j,i,x,y,nPly,position[i][j]);

		//插入左上方的有效走法
		x=j-2;//左2
		y=i-1;//上1
		if((x>=0 && y>=0) && IsValidMove(position,j,i,x,y,m_nUserChessColor))
			AddMove(j,i,x,y,nPly,position[i][j]);
		
		//插入右下方的有效走法
		x=j+1;//右1
		y=i+2;//下2 
		if((x<9 && y<10) && IsValidMove(position,j,i,x,y,m_nUserChessColor))
			AddMove(j,i,x,y,nPly,position[i][j]);

		//插入左下方的有效走法
		x=j-1;//左1
		y=i+2;//下2
		if((x>=0 && y<10) && IsValidMove(position,j,i,x,y,m_nUserChessColor))
			AddMove(j,i,x,y,nPly,position[i][j]);    
		
		//插入右上方的有效走法
		x=j+1;//右1
		y=i-2;//上2
		if((x<9 && y >=0) && IsValidMove(position,j,i,x,y,m_nUserChessColor))
			AddMove(j,i,x,y,nPly,position[i][j]);

		//插入左上方的有效走法
		x=j-1;//左1
		y=i-2;//上2
		if((x>=0 && y>=0) && IsValidMove(position,j,i,x,y,m_nUserChessColor))
			AddMove(j,i,x,y,nPly,position[i][j]);
	}

	//红兵
	void Gen_RPawnMove(int position[][], int i, int j, int nPly)
	{
		int x,y;
		int nChessID;
		
		nChessID=position[i][j];

		if(m_nUserChessColor==REDCHESS)
		{
			y=i-1;//向前
			x=j;
			if(y>0 && !IsSameSide(nChessID,position[y][x]))
				AddMove(j,i,x,y,nPly,position[i][j]);//前方无阻碍
			
			if(i<5)//是否已过河
			{
				y=i;
				
				x=j+1;//右边
				if(x<9 && !IsSameSide(nChessID,position[y][x]))
					AddMove(j,i,x,y,nPly,position[i][j]);
				
				x=j-1;//左边
				if(x>=0 && !IsSameSide(nChessID,position[y][x]))
					AddMove(j,i,x,y,nPly,position[i][j]);
			}
		}
		else
		{
			y=i+1;//向前
			x=j;
			if(y>0 && !IsSameSide(nChessID,position[y][x]))
				AddMove(j,i,x,y,nPly,position[i][j]);//前方无阻碍
			
			if(i>4)//是否已过河
			{
				y=i;
				
				x=j+1;//右边
				if(x<9 && !IsSameSide(nChessID,position[y][x]))
					AddMove(j,i,x,y,nPly,position[i][j]);
				
				x=j-1;//左边
				if(x>=0 && !IsSameSide(nChessID,position[y][x]))
					AddMove(j,i,x,y,nPly,position[i][j]);
			}
		}
	}

	//黑卒
	void Gen_BPawnMove(int position[][],int i,int j,int nPly)
	{
		int x,y;
		int nChessID;
		
		nChessID=position[i][j];

		if(m_nUserChessColor==REDCHESS)
		{
			y=i+1;//向前
			x=j;
			if(y<10 && !IsSameSide(nChessID,position[y][x]))
				AddMove(j,i,x,y,nPly,position[i][j]);//前方无阻碍
			
			if(i>4)//是否已过河
			{
				y=i;
				
				x=j+1;//右边
				if(x<9 && !IsSameSide(nChessID,position[y][x]))
					AddMove(j,i,x,y,nPly,position[i][j]);
				
				x=j-1;//左边
				if(x>=0 && !IsSameSide(nChessID,position[y][x]))
					AddMove(j,i,x,y,nPly,position[i][j]);
			}
		}
		else
		{
			y=i-1;//向前
			x=j;
			if(y<10 && !IsSameSide(nChessID,position[y][x]))
				AddMove(j,i,x,y,nPly,position[i][j]);//前方无阻碍
			
			if(i<5)//是否已过河
			{
				y=i;
				
				x=j+1;//右边
				if(x<9 && !IsSameSide(nChessID,position[y][x]))
					AddMove(j,i,x,y,nPly,position[i][j]);
				
				x=j-1;//左边
				if(x>=0 && !IsSameSide(nChessID,position[y][x]))
					AddMove(j,i,x,y,nPly,position[i][j]);
			}
		}
	}

	//车
	void Gen_CarMove(int position[][], int i, int j, int nPly)
	{	
		int x,y;
		int nChessID;
		
		nChessID=position[i][j];
		
		//插入向右的有效的走法
		x=j+1;
		y=i;
	
		while(x<9)
		{
			if(NOCHESS==position[y][x])
			{
				
				AddMove(j,i,x,y,nPly,position[i][j]);
				
			}
			else
			{
				if(!IsSameSide(nChessID,position[y][x]))
					AddMove(j,i,x,y,nPly,position[i][j]);

				break;
			}
			x++;
		}
		
		//插入向左的有效的走法
		x=j-1;
		y=i;
		while(x>=0)
		{
			if(NOCHESS==position[y][x])
				AddMove(j,i,x,y,nPly,position[i][j]);
			else
			{
				if(!IsSameSide(nChessID,position[y][x]))
					AddMove(j,i,x,y,nPly,position[i][j]);

				break;
			}
			x--;
		}

		//插入向下的有效的走法
		x=j;
		y=i+1;
		while(y<10)
		{
			if(NOCHESS==position[y][x])
				AddMove(j,i,x,y,nPly,position[i][j]);
			else
			{
				if(!IsSameSide(nChessID,position[y][x]))
					AddMove(j,i,x,y,nPly,position[i][j]);

				break;
			}
			y++;
		}

		//插入向上的有效的走法
		x=j;
		y=i-1;
		while(y>=0)
		{
			if(NOCHESS==position[y][x])
				AddMove(j,i,x,y,nPly,position[i][j]);
			else
			{
				if(!IsSameSide(nChessID,position[y][x]))
					AddMove(j,i,x,y,nPly,position[i][j]);

				break;
			}
			y--;
		}
		
	}

	//炮
	void Gen_CanonMove(int position[][], int i, int j, int nPly)
	{
		int x,y;
		boolean flag;
		int nChessID;
		
		nChessID=position[i][j];

		//插入向右的有效的走法
		x=j+1;
		y=i;
		flag=false;
		while(x<9)
		{
			if(NOCHESS==position[y][x])
			{
				if(!flag)//隔有棋子
					AddMove(j,i,x,y,nPly,position[i][j]);
			}
			else
			{
				if(!flag)//没有隔棋子，此棋子是第一个障碍，设置标志
					flag=true;
				else     //隔有棋子，此处如为敌方棋子，则可走
				{
					if(!IsSameSide(nChessID,position[y][x]))
						AddMove(j,i,x,y,nPly,position[i][j]);
					break;
				}
			}
			x++;//继续下一个位置
		}

		//插入向左的有效的走法
		x=j-1;
		y=i;
		flag=false;
		while(x>=0)
		{
			if(NOCHESS==position[y][x])
			{
				if(!flag)//隔有棋子
					AddMove(j,i,x,y,nPly,position[i][j]);
			}
			else
			{
				if(!flag)//没有隔棋子，此棋子是第一个障碍，设置标志
					flag=true;
				else     //隔有棋子，此处如为敌方棋子，则可走
				{
					if(!IsSameSide(nChessID,position[y][x]))
						AddMove(j,i,x,y,nPly,position[i][j]);
					break;
				}
			}
			x--;//继续下一个位置
		}

		//插入向下的有效的走法
		x=j;
		y=i+1;
		flag=false;
		while(y<10)
		{
			if(NOCHESS==position[y][x])
			{
				if(!flag)//隔有棋子
					AddMove(j,i,x,y,nPly,position[i][j]);
			}
			else
			{
				if(!flag)//没有隔棋子，此棋子是第一个障碍，设置标志
					flag=true;
				else     //隔有棋子，此处如为敌方棋子，则可走
				{
					if(!IsSameSide(nChessID,position[y][x]))
						AddMove(j,i,x,y,nPly,position[i][j]);
					break;
				}
			}
			y++;//继续下一个位置
		}

		//插入向上的有效的走法
		x=j;
		y=i-1;
		flag=false;
		while(y>=0)
		{
			if(NOCHESS==position[y][x])
			{
				if(!flag)//隔有棋子
					AddMove(j,i,x,y,nPly,position[i][j]);
			}
			else
			{
				if(!flag)//没有隔棋子，此棋子是第一个障碍，设置标志
					flag=true;
				else     //隔有棋子，此处如为敌方棋子，则可走
				{
					if(!IsSameSide(nChessID,position[y][x]))
						AddMove(j,i,x,y,nPly,position[i][j]);
					break;
				}
			}
			y--;//继续下一个位置
		}
	}

	boolean IsValidMove(int position[][], int nFromX, int nFromY, int nToX, int nToY,int nUserChessColor)
	{
		int i=0,j=0;
		int nMoveChessID,nTargetID;

		if(nFromX==nToX && nFromY==nToY)
			return false;//目的与源相同，非法

		nMoveChessID=position[nFromY][nFromX];
		nTargetID=position[nToY][nToX];

		if(IsSameSide(nMoveChessID,nTargetID))
			return false;

		switch(nMoveChessID)
		{
		case B_KING://黑将
			if(nUserChessColor==REDCHESS)
			{
				if(nTargetID==R_KING)//判断是否将帅见面
				{
					if(nFromX!=nToX)//横坐标不相等
						return false;//将帅不在同一列
					
					for(i=nFromY+1;i<nToY;i++)
						if(position[i][nFromX]!=NOCHESS)
							return false;//中间隔有棋子
				}
				else
				{
					if(nToY>2 || nToX>5 || nToX<3)
						return false;//目标点在九宫之外
					
					if(Math.abs(nFromY-nToY)+Math.abs(nFromX-nToX)>1)
						return false;//将帅只走一步直线
				}
			}
			else
			{
				if(nTargetID==R_KING)//判断是否将帅见面
				{
					if(nFromX!=nToX)//横坐标不相等
						return false;//将帅不在同一列
					
					for(i=nFromY-1;i>nToY;i--)
						if(position[i][nFromX]!=NOCHESS)
							return false;//中间隔有棋子
				}
				else
				{
					if(nToY<7 || nToX>5 || nToX<3)
						return false;//目标点在九宫之外
					
					if(Math.abs(nFromY-nToY)+Math.abs(nFromX-nToX)>1)
						return false;//将帅只走一步直线
				}
			}

			break;

		case R_KING://红帅
			if(nUserChessColor==REDCHESS)
			{
				if(nTargetID==B_KING)//判断是否将帅见面
				{
					if(nFromX!=nToX)//横坐标不相等
						return false;//将帅不在同一列
					
					for(i=nFromY-1;i>nToY;i--)
						if(position[i][nFromX]!=NOCHESS)
							return false;//中间隔有棋子
				}
				else
				{
					if(nToY<7 || nToX>5 || nToX<3)
						return false;//目标点在九宫之外
					
					if(Math.abs(nFromY-nToY)+Math.abs(nFromX-nToX)>1)
						return false;//将帅只走一步直线
				}
			}
			else
			{
				if(nTargetID==B_KING)//判断是否将帅见面
				{
					if(nFromX!=nToX)//横坐标不相等
						return false;//将帅不在同一列
					
					for(i=nFromY+1;i<nToY;i++)
						if(position[i][nFromX]!=NOCHESS)
							return false;//中间隔有棋子
				}
				else
				{
					if(nToY>2 || nToX>5 || nToX<3)
						return false;//目标点在九宫之外
					
					if(Math.abs(nFromY-nToY)+Math.abs(nFromX-nToX)>1)
						return false;//将帅只走一步直线
				}
			}

			break;

		case R_BISHOP://红士
			if(nUserChessColor==REDCHESS)
			{
				if(nToY<7 || nToX>5 || nToX<3)
					return false;//士出九宫
			}
			else
			{
				if(nToY>2 || nToX>5 || nToX<3)
					return false;//士出九宫
			}

			if(Math.abs(nFromX-nToX)!=1 || Math.abs(nFromY-nToY)!=1)
					return false;//士走斜线

			break;

		case B_BISHOP://黑士
			if(nUserChessColor==REDCHESS)
			{
				if(nToY>2 || nToX>5 || nToX<3)
					return false;//士出九宫
			}
			else
			{
				if(nToY<7 || nToX>5 || nToX<3)
				return false;//士出九宫
			}

			if(Math.abs(nFromX-nToX)!=1 || Math.abs(nFromY-nToY)!=1)
				return false;//士走斜线

			break;

		case R_ELEPHANT://红相
			if(nUserChessColor==REDCHESS)
			{
				if(nToY<5)
					return false;//相不能过河
			}
			else
			{
				if(nToY>4)
					return false;//相不能过河
			}

			if(Math.abs(nFromX-nToX)!=2 || Math.abs(nFromY-nToY)!=2)
				return false;//相走田字

			if(position[(nFromY +nToY)/2][(nFromX +nToX)/2]!=NOCHESS)
				return false;//相眼被塞

			break;

		case B_ELEPHANT://黑象
			if(nUserChessColor==REDCHESS)
			{
				if(nToY>4)
					return false;//象不能过河
			}
			else
			{
				if(nToY<5)
					return false;//象不能过河
			}

			if(Math.abs(nFromX-nToX)!=2 || Math.abs(nFromY-nToY)!=2)
				return false;//象走田字

			if(position[(nFromY +nToY)/2][(nFromX +nToX)/2]!=NOCHESS)
				return false;//象眼被塞

			break;

		case B_PAWN://黑卒
			if(nUserChessColor==REDCHESS)
			{
				if(nToY<nFromY)
					return false;//卒不能回头
				
				if(nFromY<5 && nFromY==nToY)
					return false;//卒过河前只能直走
			}
			else
			{
				if(nToY>nFromY)
					return false;//卒不能回头
				
				if(nFromY>4 && nFromY==nToY)
					return false;//卒过河前只能直走
			}

			if(nToY-nFromY+Math.abs(nToX-nFromX)>1)
				return false;//卒只走一步直线

			break;

		case R_PAWN://红兵
			if(nUserChessColor==REDCHESS)
			{
				if(nToY>nFromY)
					return false;//兵不能回头
				
				if(nFromY>4 && nFromY==nToY)
					return false;//兵过河前只能直走
			}
			else
			{
				if(nToY<nFromY)
					return false;//兵不能回头
				
				if(nFromY<5 && nFromY==nToY)
					return false;//兵过河前只能直走
			}

			if(nFromY-nToY+Math.abs(nToX-nFromX)>1)
				return false;//兵只走一步直线

			break;

		case B_CAR://黑车
		case R_CAR://红车
			if(nFromY!=nToY && nFromX!=nToX)
				return false;//车走直线

			if(nFromY==nToY)
			{
				if(nFromX<nToX)
				{
					for(i=nFromX+1;i<nToX;i++)
						if(position[nFromY][i]!=NOCHESS)
							return false;
				}
				else
				{
					for(i=nToX+1;i<nFromX;i++)
						if(position[nFromY][i]!=NOCHESS)
							return false;
				}
			}
			else
			{
				if(nFromY<nToY)
				{
					for(j=nFromY+1;j<nToY;j++)
						if(position[j][nFromX]!=NOCHESS)
							return false;
				}
				else
				{
					for(j=nToY+1;j<nFromY;j++)
						if(position[j][nFromX]!=NOCHESS)
							return false;
				}
			}
			
			break;

		case B_HORSE://黑马
		case R_HORSE://红马
			if(!((Math.abs(nToX-nFromX)==1 && Math.abs(nToY -nFromY)==2) || (Math.abs(nToX-nFromX)==2&&Math.abs(nToY -nFromY)==1)))
				return false;//马走日字

			if(nToX-nFromX==2)
			{
				i=nFromX+1;
				j=nFromY;
			}
			else
				if(nFromX-nToX==2)
				{
					i=nFromX-1;
					j=nFromY;
				}
				else
					if(nToY-nFromY==2)
					{
						i=nFromX;
						j=nFromY+1;
					}
					else
						if(nFromY-nToY==2)
						{
							i=nFromX;
							j=nFromY-1;
						}

			if(position[j][i]!=NOCHESS)
				return false;//绊马腿
			
			break;

		case B_CANON://黑炮
		case R_CANON://红炮
			if(nFromY!=nToY && nFromX!=nToX)
				return false;//炮走直线

			//炮吃子时经过的路线中不能有棋子
			if(position[nToY][nToX]==NOCHESS)
			{
				if(nFromY==nToY)
				{
					if(nFromX<nToX)
					{
						for(i=nFromX+1;i<nToX;i++)
							if(position[nFromY][i]!=NOCHESS)
								return false;
					}
					else
					{
						for(i=nToX+1;i<nFromX;i++)
							if(position[nFromY][i]!=NOCHESS)
								return false;
					}
				}
				else
				{
					if(nFromY<nToY)
					{
						for(j=nFromY+1;j<nToY;j++)
							if(position[j][nFromX]!=NOCHESS)
								return false;
					}
					else
					{
						for(j=nToY+1;j<nFromY;j++)
							if(position[j][nFromX]!=NOCHESS)
								return false;
					}
				}
			}		
			else//炮吃子时
			{
				int jj=0;
				if(nFromY==nToY)
				{
					if(nFromX<nToX)
					{
						for(i=nFromX+1;i<nToX;i++)
							if(position[nFromY][i]!=NOCHESS)
								jj++;
							if(jj!=1)
								return false;
					}
					else
					{
						for(i=nToX+1;i<nFromX;i++)
							if(position[nFromY][i]!=NOCHESS)
								jj++;
							if(jj!=1)
								return false;
					}
				}
				else
				{
					if(nFromY<nToY)
					{
						for(i=nFromY+1;i<nToY;i++)
							if(position[i][nFromX]!=NOCHESS)
								jj++;
						if(jj!=1)
							return false;
					}
					else
					{
						for(i=nToY+1;i<nFromY;i++)
							if(position[i][nFromX]!=NOCHESS)
								jj++;
						if(jj!=1)
							return false;
					}
				}
			}
			
			break;

		default:
			return false;
		}
			
		return true;
	}
																		 //在m_MoveList中插入一个走法,nPly表明插入到List第几层

	
	public CHESSMOVE [][]m_MoveList=new CHESSMOVE[8][80];//存放CreatePossibleMove产生的所有走法的队列

	protected int m_nMoveCount;//记录m_MoveList中走法的数量
	protected int m_nUserChessColor;
	//define 中定义内容
	 int BLACKCHESS=1;//黑方
	 int REDCHESS=2;//红方
	 int DS_DEFAULTSET=1;
	 int DS_USERDEFINE=2;

	 int CS_PCCHESS=1;//人机对弈
	 int CS_PPCHESS	 =2;//人人对弈
	 int CS_CCCHESS   =3;//机机对弈
	 int CS_HASHCHESS =4;//混杂对弈

	//--------棋子--------
	 final int NOCHESS=0 ;//没有棋子

	 final int B_KING=1 ;//黑帅
	 final int B_CAR=2; //黑车
	 final int B_HORSE=3; //黑马
	 final int B_CANON =4; //黑炮
	 final int B_BISHOP   =5; //黑士
	 final int B_ELEPHANT =6; //黑象
	 final int B_PAWN     =7; //黑卒
	 final int B_BEGIN    =B_KING;
	 final int B_END      =B_PAWN;

	 final int R_KING	 =  8 ;//红将
	 final int R_CAR    =  9 ;//红车
	 final int R_HORSE   = 10;//红马
	 final int R_CANON   = 11;//红炮
	 final int R_BISHOP   =12;//红士
	 final int R_ELEPHANT =13;//红相
	 final int R_PAWN     =14;//红兵
	 final int R_BEGIN    =R_KING;
	 final int R_END      =R_PAWN;
	//--------------------

	 boolean IsBlack(int x)
	 {return (x>=B_BEGIN && x<=B_END);}//判断某个棋子是不是黑色
	 boolean IsRed(int x) 
	 {return (x>=R_BEGIN && x<=R_END);}//判断某个棋子是不是红色

	//判断两个棋子是不是同色
	 boolean IsSameSide(int x,int y) 
	 {
		 return ((IsBlack(x) && IsBlack(y)) || (IsRed(x) && IsRed(y)));
	 }
}

