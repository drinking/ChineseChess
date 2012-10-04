package drinking.android.algorithm;



public class SearchEngine {
	int MakeMove(CHESSMOVE move)
	{
		int nChessID;
		//Log.i("xxxxxxxxxxxxxx", "ccccccccccccccccccccccccc");
		nChessID=CurPosition[move.Toy][move.Tox];   //ȡĿ��λ������
		//Log.i("move chess", Integer.toString(nChessID));
	    CurPosition[move.Toy][move.Tox]=CurPosition[move.Fromy][move.Fromx];//�������ƶ���Ŀ��λ��	
		CurPosition[move.Fromy][move.Fromx]=0;//��ԭλ�����NOCHESS=0
		//Log.i("xxxxxxxxxxxxxx", "ccccccccccccccccccccccccc");
		return nChessID;//���ر��Ե�������
	}

	void RedoChessMove(int position[][],CHESSMOVE move)
	{
	    position[move.Toy][move.Tox]=position[move.Fromy][move.Fromx];
		position[move.Fromy][move.Fromx]=0;//nochess
	}

	public void SearchAGoodMove(int position[][])
	{//ע��

		m_nMaxDepth=m_nSearchDepth; //�趨��������		
		arrayfill(position,CurPosition);
		NegaMax(m_nMaxDepth);
		//Log.i("ttttttttttttttttttttt", "ttttttttttttttttttt");
		//Log.i("ttttttttttttttttttttt",Integer.toString(m_cmBestMove.nChessID));
		//���ø�����ֵ��������������߷�
		m_umUndoMove.cmChessMove=m_cmBestMove;
		//Log.i("ttttttttttttttttttttt", "ttttttttttttttttttt");
		m_umUndoMove.nChessID=MakeMove(m_cmBestMove);	
	
										//�������޸�Ϊ���߹���	

		//memcpy(position,CurPosition,90);//���޸ĺ�����̸��Ƶ�����������У���ȥ
		//System.arraycopy(CurPosition, 0, position, 0, CurPosition.length);
		//arrayfill(CurPosition,position);
	}

	void UnMakeMove(CHESSMOVE move, int nChessID)
	{
		
		CurPosition[move.Fromy][move.Fromx]=CurPosition[move.Toy][move.Tox];//��Ŀ��λ�����ӿ���ԭλ  	
		CurPosition[move.Toy][move.Tox]=nChessID;	//�ָ�Ŀ��λ�õ�����
	}

	void UndoChessMove(int position[][],CHESSMOVE move, int nChessID)
	{
		position[move.Fromy][move.Fromx]=position[move.Toy][move.Tox];//��Ŀ��λ�����ӿ���ԭλ  	
		position[move.Toy][move.Tox]=nChessID;							  //�ָ�Ŀ��λ�õ�����
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
		//���췽�Ź��Ƿ���˧
		for(i=7;i<10;i++)
			for(j=3;j<6;j++)
			{
				if(position[i][j]==1)
					BlackLive=true;
				if(position[i][j]==8)
					RedLive=true;
			}

		//���ڷ��Ź��Ƿ��н�
		for(i=0;i<3;i++)
			for(j=3;j<6;j++)
			{
				if(position[i][j]==1)
					BlackLive=true;
				if(position[i][j]==8)
					RedLive=true;
			}

		i=(m_nMaxDepth-nDepth+1)%2;//ȡ��ǰ��ż��־,������Ϊ���Է�,ż����Ϊ�û���
		//if(RedLive==true&&BlackLive==true)
		//{
		//	Log.i("isgameover", "xxxxxxxxxxxxxxxxxxx");
	//	}
		//�췽����
		if(!RedLive)//ע��
		{
			if(i==0)
				return 19990+nDepth; //�����㷵�ؼ���ֵ
			else
				return -19990-nDepth;//ż���㷵�ؼ�Сֵ
		}
		//�ڷ�����
		if(!BlackLive)
		{
			if(i==0)
				return -19990-nDepth;//�����㷵�ؼ�Сֵ
			else
				return 19990+nDepth; //ż���㷵�ؼ���ֵ
		}
	//	Log.i("isgameover", "xxxxxxxxxxxxxxxxxxx");
		return 0;//��˧���ڣ�����0
	}
	int NegaMax(int nDepth)
	{
		int current=-20000;
		 
		int score;
		int Count,i;
		int type;

		i=IsGameOver(CurPosition,nDepth);//�������Ƿ����
		//Log.i("00000000000000000", Integer.toString(i));
		if(i!=0)
		{
			return i;//��ֽ��������ؼ���/��Сֵ
		}
		//Log.i("00000000000000000", "xxxxxxxxxxxxxxxxxxxxx");
		if(nDepth<=0)//Ҷ�ӽڵ�ȡ��ֵ
		{
			//ע��
			
			int x=m_pEval.Eveluate(CurPosition,((m_nMaxDepth-nDepth)%2==1),m_nUserChessColor);
		
			return x;
		}
		
		/////////////
		//�оٵ�ǰ�����һ�����п��ܵ��߷�
		Count=m_pMG.CreatePossibleMove(CurPosition,nDepth,(m_nMaxDepth-nDepth)%2,m_nUserChessColor);
		//Log.i("next step", Integer.toString(Count));
	//////////////////
	
		for(i=0;i<Count;i++)
		{	
			type=MakeMove(m_pMG.m_MoveList[nDepth][i]);     //�����߷������¾���    	
			score=-NegaMax(nDepth-1);	//�ݹ���ø�����ֵ������һ��ڵ�		
			UnMakeMove(m_pMG.m_MoveList[nDepth][i],type);   //�ָ���ǰ����
			
			if(score>current)							      //���score������֪�����ֵ
			{
				current=score;								  //�޸ĵ�ǰ���ֵΪscore
				if(nDepth==m_nMaxDepth)		
					{
						m_cmBestMove=m_pMG.m_MoveList[nDepth][i];
					}//��������ʱ��������߷�
			}
		}
		
		return current;//���ؼ���ֵ
	}
	
//		CGradientProgressCtrl* m_pThinkProgress;
									    //������ʾ˼�����ȵĽ�����ָ��
	protected int [][]CurPosition=new int[10][9];		//����ʱ���ڼ�¼��ǰ�ڵ�����״̬������
	protected CHESSMOVE m_cmBestMove=new CHESSMOVE();			//��¼����߷�
	protected UNDOMOVE m_umUndoMove=new UNDOMOVE();
	protected MoveGenerator m_pMG;			//�߷�������
	protected Eveluation m_pEval;			//��ֵ����
	protected int m_nSearchDepth;				//����������
	protected int m_nMaxDepth;	//��ǰ����������������
	public int m_nUserChessColor;
	public CHESSMOVE GetBestMove(){return m_cmBestMove;};			//�õ�����߷�
	UNDOMOVE GetUndoMove(){return m_umUndoMove;};			//�õ������߷�
	public void SetSearchDepth(int nDepth){m_nSearchDepth=nDepth;};//�趨����������
	public void SetEveluator(Eveluation pEval){m_pEval=pEval;};  //�趨��ֵ����
	public void SetMoveGenerator(MoveGenerator pMG){m_pMG =pMG;};//�趨�߷�������
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
