package drinking.android.algorithm;



public class SearchEngine {
	int MakeMove(CHESSMOVE move)
	{
		int nChessID;
		//Log.i("xxxxxxxxxxxxxx", "ccccccccccccccccccccccccc");
		nChessID=CurPosition[move.Toy][move.Tox];   //取目标位置棋子
		//Log.i("move chess", Integer.toString(nChessID));
	    CurPosition[move.Toy][move.Tox]=CurPosition[move.Fromy][move.Fromx];//把棋子移动到目标位置	
		CurPosition[move.Fromy][move.Fromx]=0;//将原位置清空NOCHESS=0
		//Log.i("xxxxxxxxxxxxxx", "ccccccccccccccccccccccccc");
		return nChessID;//返回被吃掉的棋子
	}

	void RedoChessMove(int position[][],CHESSMOVE move)
	{
	    position[move.Toy][move.Tox]=position[move.Fromy][move.Fromx];
		position[move.Fromy][move.Fromx]=0;//nochess
	}

	public void SearchAGoodMove(int position[][])
	{//注意

		m_nMaxDepth=m_nSearchDepth; //设定搜索层数		
		arrayfill(position,CurPosition);
		NegaMax(m_nMaxDepth);
		//Log.i("ttttttttttttttttttttt", "ttttttttttttttttttt");
		//Log.i("ttttttttttttttttttttt",Integer.toString(m_cmBestMove.nChessID));
		//调用负极大值搜索函数找最佳走法
		m_umUndoMove.cmChessMove=m_cmBestMove;
		//Log.i("ttttttttttttttttttttt", "ttttttttttttttttttt");
		m_umUndoMove.nChessID=MakeMove(m_cmBestMove);	
	
										//将棋盘修改为已走过的	

		//memcpy(position,CurPosition,90);//将修改后的棋盘复制到传入的棋盘中，传去
		//System.arraycopy(CurPosition, 0, position, 0, CurPosition.length);
		//arrayfill(CurPosition,position);
	}

	void UnMakeMove(CHESSMOVE move, int nChessID)
	{
		
		CurPosition[move.Fromy][move.Fromx]=CurPosition[move.Toy][move.Tox];//将目标位置棋子拷回原位  	
		CurPosition[move.Toy][move.Tox]=nChessID;	//恢复目标位置的棋子
	}

	void UndoChessMove(int position[][],CHESSMOVE move, int nChessID)
	{
		position[move.Fromy][move.Fromx]=position[move.Toy][move.Tox];//将目标位置棋子拷回原位  	
		position[move.Toy][move.Tox]=nChessID;							  //恢复目标位置的棋子
	}

	int IsGameOver(int position[][], int nDepth)
	{
		int i,j;
		boolean RedLive=false,BlackLive=false;
		/*Log.i("isgameover", Integer.toString(position[0][0])+"-"+
				Integer.toString(position[0][1])+"-"+
				Integer.toString(position[0][2])+"-"+
				Integer.toString(position[0][3])+"-"+
				Integer.toString(position[0][4])+"-"+
				Integer.toString(position[0][5])+"-"+
				Integer.toString(position[0][6])+"-"+
				Integer.toString(position[0][7])+"-"+
				Integer.toString(position[0][8])+"-"
				);*/
		//检查红方九宫是否有帅
		for(i=7;i<10;i++)
			for(j=3;j<6;j++)
			{
				if(position[i][j]==1)
					BlackLive=true;
				if(position[i][j]==8)
					RedLive=true;
			}

		//检查黑方九宫是否有将
		for(i=0;i<3;i++)
			for(j=3;j<6;j++)
			{
				if(position[i][j]==1)
					BlackLive=true;
				if(position[i][j]==8)
					RedLive=true;
			}

		i=(m_nMaxDepth-nDepth+1)%2;//取当前奇偶标志,奇数层为电脑方,偶数层为用户方
		//if(RedLive==true&&BlackLive==true)
		//{
		//	Log.i("isgameover", "xxxxxxxxxxxxxxxxxxx");
	//	}
		//红方不在
		if(!RedLive)//注意
		{
			if(i==0)
				return 19990+nDepth; //奇数层返回极大值
			else
				return -19990-nDepth;//偶数层返回极小值
		}
		//黑方不在
		if(!BlackLive)
		{
			if(i==0)
				return -19990-nDepth;//奇数层返回极小值
			else
				return 19990+nDepth; //偶数层返回极大值
		}
	//	Log.i("isgameover", "xxxxxxxxxxxxxxxxxxx");
		return 0;//将帅都在，返回0
	}
	int NegaMax(int nDepth)
	{
		int current=-20000;
		 
		int score;
		int Count,i;
		int type;

		i=IsGameOver(CurPosition,nDepth);//检查棋局是否结束
		//Log.i("00000000000000000", Integer.toString(i));
		if(i!=0)
		{
			return i;//棋局结束，返回极大/极小值
		}
		//Log.i("00000000000000000", "xxxxxxxxxxxxxxxxxxxxx");
		if(nDepth<=0)//叶子节点取估值
		{
			//注意
			
			int x=m_pEval.Eveluate(CurPosition,((m_nMaxDepth-nDepth)%2==1),m_nUserChessColor);
		
			return x;
		}
		
		/////////////
		//列举当前棋局下一步所有可能的走法
		Count=m_pMG.CreatePossibleMove(CurPosition,nDepth,(m_nMaxDepth-nDepth)%2,m_nUserChessColor);
		//Log.i("next step", Integer.toString(Count));
	//////////////////
	
		for(i=0;i<Count;i++)
		{	
			type=MakeMove(m_pMG.m_MoveList[nDepth][i]);     //根据走法产生新局面    	
			score=-NegaMax(nDepth-1);	//递归调用负极大值搜索下一层节点		
			UnMakeMove(m_pMG.m_MoveList[nDepth][i],type);   //恢复当前局面
			
			if(score>current)							      //如果score大于已知的最大值
			{
				current=score;								  //修改当前最大值为score
				if(nDepth==m_nMaxDepth)		
					{
						m_cmBestMove=m_pMG.m_MoveList[nDepth][i];
					}//靠近根部时保存最佳走法
			}
		}
		
		return current;//返回极大值
	}
	
//		CGradientProgressCtrl* m_pThinkProgress;
									    //用以显示思考进度的进度条指针
	protected int [][]CurPosition=new int[10][9];		//搜索时用于记录当前节点棋盘状态的数组
	protected CHESSMOVE m_cmBestMove=new CHESSMOVE();			//记录最佳走法
	protected UNDOMOVE m_umUndoMove=new UNDOMOVE();
	protected MoveGenerator m_pMG;			//走法产生器
	protected Eveluation m_pEval;			//估值核心
	protected int m_nSearchDepth;				//最大搜索深度
	protected int m_nMaxDepth;	//当前搜索的最大搜索深度
	public int m_nUserChessColor;
	public CHESSMOVE GetBestMove(){return m_cmBestMove;};			//得到最佳走法
	UNDOMOVE GetUndoMove(){return m_umUndoMove;};			//得到悔棋走法
	public void SetSearchDepth(int nDepth){m_nSearchDepth=nDepth;};//设定最大搜索深度
	public void SetEveluator(Eveluation pEval){m_pEval=pEval;};  //设定估值引擎
	public void SetMoveGenerator(MoveGenerator pMG){m_pMG =pMG;};//设定走法产生器
	public void SetUserChessColor(int nUserChessColor){m_nUserChessColor=nUserChessColor;};
	public void arrayfill(int src[][],int des[][])
	{
		int i=0;
		int j=0;
		for(i=0;i<10;i++)
			{for(j=0;j<9;j++)
			{
				des[i][j]=src[i][j];
			}
			}
	}
}
