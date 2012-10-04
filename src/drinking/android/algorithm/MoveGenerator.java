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

	//nPlyָ����ǰ�����Ĳ�����ÿ�㽫�߷����ڲ�ͬ��λ�ã����⸲��
	//nSideָ��������һ�����߷���trueΪ�췽��false�Ǻڷ�
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
							continue;//��Ҫ���������߷�����������
						
						if(nSide && IsBlack(nChessID))
							continue;//��Ҫ���������߷�����������
					}
					else
					{
						if(nSide && IsRed(nChessID))
							continue;//��Ҫ���������߷�����������
						
						if(!nSide && IsBlack(nChessID))
							continue;//��Ҫ���������߷�����������
					}
					
					switch(nChessID)
					{
					case R_KING://��˧
					case B_KING://�ڽ�
						Gen_KingMove(position,i,j,nPly);
						break;

					case R_BISHOP://��ʿ
						Gen_RBishopMove(position,i,j,nPly);
						break;
					
					case B_BISHOP://��ʿ
						Gen_BBishopMove(position,i,j,nPly);
						break;
					
					case R_ELEPHANT://����
					case B_ELEPHANT://����
						Gen_ElephantMove(position,i,j,nPly);
						break;
					
					case R_HORSE://����
					case B_HORSE://����
						Gen_HorseMove(position,i,j,nPly);
						break;
					
					case R_CAR://�쳵
					case B_CAR://�ڳ�
						
						Gen_CarMove(position,i,j,nPly);
						
						break;

					case R_PAWN://���
						Gen_RPawnMove(position,i,j,nPly);
						break;
					
					case B_PAWN://����
						Gen_BPawnMove(position,i,j,nPly);
						break;
					
					case B_CANON://����
					case R_CANON://����
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

	//��ʿ
	void Gen_RBishopMove(int position[][],int i,int j,int nPly)
	{
		int x,y;
		
		for(y=7;y<10;y++)
			for(x=3;x<6;x++)
				if(IsValidMove(position,j,i,x,y,m_nUserChessColor))
					AddMove(j,i,x,y,nPly,position[i][j]);
	}

	//��ʿ
	void Gen_BBishopMove(int position[][],int i,int j,int nPly)
	{
		int x,y;

		for(y=0;y<3;y++)
			for(x=3;x<6;x++)
				if(IsValidMove(position,j,i,x,y,m_nUserChessColor))
					AddMove(j,i,x,y,nPly,position[i][j]);
	}

	//��
	void Gen_ElephantMove(int position[][],int i,int j,int nPly)
	{
		int x,y;
		
		//�������·�����Ч�߷�
		x=j+2;
		y=i+2;
		if(x<9 && y<10 && IsValidMove(position,j,i,x,y,m_nUserChessColor))
			AddMove(j,i,x,y,nPly,position[i][j]);
		
		//�������Ϸ�����Ч�߷�
		x=j+2;
		y=i-2;
		if(x<9 && y>=0 && IsValidMove(position,j,i,x,y,m_nUserChessColor))
			AddMove(j,i,x,y,nPly,position[i][j]);
		
		//�������·�����Ч�߷�
		x=j-2;
		y=i+2;
		if(x>=0 && y<10 && IsValidMove(position,j,i,x,y,m_nUserChessColor))
			AddMove(j,i,x,y,nPly,position[i][j]);
		
		//�������Ϸ�����Ч�߷�
		x=j-2;
		y=i-2;
		if(x>=0 && y>=0 && IsValidMove(position,j,i,x,y,m_nUserChessColor))
			AddMove(j,i,x,y,nPly,position[i][j]);
	}

	//��
	void Gen_HorseMove(int position[][], int i, int j, int nPly)
	{
		int x, y;
		
		//�������·�����Ч�߷�
		x=j+2;//��2
		y=i+1;//��1
		if((x<9 && y<10) && IsValidMove(position,j,i,x,y,m_nUserChessColor))
			AddMove(j,i,x,y,nPly,position[i][j]);
		
		//�������Ϸ�����Ч�߷�
		x=j+2;//��2
		y=i-1;//��1
		if((x<9 && y>=0) && IsValidMove(position,j,i,x,y,m_nUserChessColor))
			AddMove(j,i,x,y,nPly,position[i][j]);
		
		//�������·�����Ч�߷�
		x=j-2;//��2
		y=i+1;//��1
		if((x>=0 && y<10) && IsValidMove(position,j,i,x,y,m_nUserChessColor))
			AddMove(j,i,x,y,nPly,position[i][j]);

		//�������Ϸ�����Ч�߷�
		x=j-2;//��2
		y=i-1;//��1
		if((x>=0 && y>=0) && IsValidMove(position,j,i,x,y,m_nUserChessColor))
			AddMove(j,i,x,y,nPly,position[i][j]);
		
		//�������·�����Ч�߷�
		x=j+1;//��1
		y=i+2;//��2 
		if((x<9 && y<10) && IsValidMove(position,j,i,x,y,m_nUserChessColor))
			AddMove(j,i,x,y,nPly,position[i][j]);

		//�������·�����Ч�߷�
		x=j-1;//��1
		y=i+2;//��2
		if((x>=0 && y<10) && IsValidMove(position,j,i,x,y,m_nUserChessColor))
			AddMove(j,i,x,y,nPly,position[i][j]);    
		
		//�������Ϸ�����Ч�߷�
		x=j+1;//��1
		y=i-2;//��2
		if((x<9 && y >=0) && IsValidMove(position,j,i,x,y,m_nUserChessColor))
			AddMove(j,i,x,y,nPly,position[i][j]);

		//�������Ϸ�����Ч�߷�
		x=j-1;//��1
		y=i-2;//��2
		if((x>=0 && y>=0) && IsValidMove(position,j,i,x,y,m_nUserChessColor))
			AddMove(j,i,x,y,nPly,position[i][j]);
	}

	//���
	void Gen_RPawnMove(int position[][], int i, int j, int nPly)
	{
		int x,y;
		int nChessID;
		
		nChessID=position[i][j];

		if(m_nUserChessColor==REDCHESS)
		{
			y=i-1;//��ǰ
			x=j;
			if(y>0 && !IsSameSide(nChessID,position[y][x]))
				AddMove(j,i,x,y,nPly,position[i][j]);//ǰ�����谭
			
			if(i<5)//�Ƿ��ѹ���
			{
				y=i;
				
				x=j+1;//�ұ�
				if(x<9 && !IsSameSide(nChessID,position[y][x]))
					AddMove(j,i,x,y,nPly,position[i][j]);
				
				x=j-1;//���
				if(x>=0 && !IsSameSide(nChessID,position[y][x]))
					AddMove(j,i,x,y,nPly,position[i][j]);
			}
		}
		else
		{
			y=i+1;//��ǰ
			x=j;
			if(y>0 && !IsSameSide(nChessID,position[y][x]))
				AddMove(j,i,x,y,nPly,position[i][j]);//ǰ�����谭
			
			if(i>4)//�Ƿ��ѹ���
			{
				y=i;
				
				x=j+1;//�ұ�
				if(x<9 && !IsSameSide(nChessID,position[y][x]))
					AddMove(j,i,x,y,nPly,position[i][j]);
				
				x=j-1;//���
				if(x>=0 && !IsSameSide(nChessID,position[y][x]))
					AddMove(j,i,x,y,nPly,position[i][j]);
			}
		}
	}

	//����
	void Gen_BPawnMove(int position[][],int i,int j,int nPly)
	{
		int x,y;
		int nChessID;
		
		nChessID=position[i][j];

		if(m_nUserChessColor==REDCHESS)
		{
			y=i+1;//��ǰ
			x=j;
			if(y<10 && !IsSameSide(nChessID,position[y][x]))
				AddMove(j,i,x,y,nPly,position[i][j]);//ǰ�����谭
			
			if(i>4)//�Ƿ��ѹ���
			{
				y=i;
				
				x=j+1;//�ұ�
				if(x<9 && !IsSameSide(nChessID,position[y][x]))
					AddMove(j,i,x,y,nPly,position[i][j]);
				
				x=j-1;//���
				if(x>=0 && !IsSameSide(nChessID,position[y][x]))
					AddMove(j,i,x,y,nPly,position[i][j]);
			}
		}
		else
		{
			y=i-1;//��ǰ
			x=j;
			if(y<10 && !IsSameSide(nChessID,position[y][x]))
				AddMove(j,i,x,y,nPly,position[i][j]);//ǰ�����谭
			
			if(i<5)//�Ƿ��ѹ���
			{
				y=i;
				
				x=j+1;//�ұ�
				if(x<9 && !IsSameSide(nChessID,position[y][x]))
					AddMove(j,i,x,y,nPly,position[i][j]);
				
				x=j-1;//���
				if(x>=0 && !IsSameSide(nChessID,position[y][x]))
					AddMove(j,i,x,y,nPly,position[i][j]);
			}
		}
	}

	//��
	void Gen_CarMove(int position[][], int i, int j, int nPly)
	{	
		int x,y;
		int nChessID;
		
		nChessID=position[i][j];
		
		//�������ҵ���Ч���߷�
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
		
		//�����������Ч���߷�
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

		//�������µ���Ч���߷�
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

		//�������ϵ���Ч���߷�
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

	//��
	void Gen_CanonMove(int position[][], int i, int j, int nPly)
	{
		int x,y;
		boolean flag;
		int nChessID;
		
		nChessID=position[i][j];

		//�������ҵ���Ч���߷�
		x=j+1;
		y=i;
		flag=false;
		while(x<9)
		{
			if(NOCHESS==position[y][x])
			{
				if(!flag)//��������
					AddMove(j,i,x,y,nPly,position[i][j]);
			}
			else
			{
				if(!flag)//û�и����ӣ��������ǵ�һ���ϰ������ñ�־
					flag=true;
				else     //�������ӣ��˴���Ϊ�з����ӣ������
				{
					if(!IsSameSide(nChessID,position[y][x]))
						AddMove(j,i,x,y,nPly,position[i][j]);
					break;
				}
			}
			x++;//������һ��λ��
		}

		//�����������Ч���߷�
		x=j-1;
		y=i;
		flag=false;
		while(x>=0)
		{
			if(NOCHESS==position[y][x])
			{
				if(!flag)//��������
					AddMove(j,i,x,y,nPly,position[i][j]);
			}
			else
			{
				if(!flag)//û�и����ӣ��������ǵ�һ���ϰ������ñ�־
					flag=true;
				else     //�������ӣ��˴���Ϊ�з����ӣ������
				{
					if(!IsSameSide(nChessID,position[y][x]))
						AddMove(j,i,x,y,nPly,position[i][j]);
					break;
				}
			}
			x--;//������һ��λ��
		}

		//�������µ���Ч���߷�
		x=j;
		y=i+1;
		flag=false;
		while(y<10)
		{
			if(NOCHESS==position[y][x])
			{
				if(!flag)//��������
					AddMove(j,i,x,y,nPly,position[i][j]);
			}
			else
			{
				if(!flag)//û�и����ӣ��������ǵ�һ���ϰ������ñ�־
					flag=true;
				else     //�������ӣ��˴���Ϊ�з����ӣ������
				{
					if(!IsSameSide(nChessID,position[y][x]))
						AddMove(j,i,x,y,nPly,position[i][j]);
					break;
				}
			}
			y++;//������һ��λ��
		}

		//�������ϵ���Ч���߷�
		x=j;
		y=i-1;
		flag=false;
		while(y>=0)
		{
			if(NOCHESS==position[y][x])
			{
				if(!flag)//��������
					AddMove(j,i,x,y,nPly,position[i][j]);
			}
			else
			{
				if(!flag)//û�и����ӣ��������ǵ�һ���ϰ������ñ�־
					flag=true;
				else     //�������ӣ��˴���Ϊ�з����ӣ������
				{
					if(!IsSameSide(nChessID,position[y][x]))
						AddMove(j,i,x,y,nPly,position[i][j]);
					break;
				}
			}
			y--;//������һ��λ��
		}
	}

	boolean IsValidMove(int position[][], int nFromX, int nFromY, int nToX, int nToY,int nUserChessColor)
	{
		int i=0,j=0;
		int nMoveChessID,nTargetID;

		if(nFromX==nToX && nFromY==nToY)
			return false;//Ŀ����Դ��ͬ���Ƿ�

		nMoveChessID=position[nFromY][nFromX];
		nTargetID=position[nToY][nToX];

		if(IsSameSide(nMoveChessID,nTargetID))
			return false;

		switch(nMoveChessID)
		{
		case B_KING://�ڽ�
			if(nUserChessColor==REDCHESS)
			{
				if(nTargetID==R_KING)//�ж��Ƿ�˧����
				{
					if(nFromX!=nToX)//�����겻���
						return false;//��˧����ͬһ��
					
					for(i=nFromY+1;i<nToY;i++)
						if(position[i][nFromX]!=NOCHESS)
							return false;//�м��������
				}
				else
				{
					if(nToY>2 || nToX>5 || nToX<3)
						return false;//Ŀ����ھŹ�֮��
					
					if(Math.abs(nFromY-nToY)+Math.abs(nFromX-nToX)>1)
						return false;//��˧ֻ��һ��ֱ��
				}
			}
			else
			{
				if(nTargetID==R_KING)//�ж��Ƿ�˧����
				{
					if(nFromX!=nToX)//�����겻���
						return false;//��˧����ͬһ��
					
					for(i=nFromY-1;i>nToY;i--)
						if(position[i][nFromX]!=NOCHESS)
							return false;//�м��������
				}
				else
				{
					if(nToY<7 || nToX>5 || nToX<3)
						return false;//Ŀ����ھŹ�֮��
					
					if(Math.abs(nFromY-nToY)+Math.abs(nFromX-nToX)>1)
						return false;//��˧ֻ��һ��ֱ��
				}
			}

			break;

		case R_KING://��˧
			if(nUserChessColor==REDCHESS)
			{
				if(nTargetID==B_KING)//�ж��Ƿ�˧����
				{
					if(nFromX!=nToX)//�����겻���
						return false;//��˧����ͬһ��
					
					for(i=nFromY-1;i>nToY;i--)
						if(position[i][nFromX]!=NOCHESS)
							return false;//�м��������
				}
				else
				{
					if(nToY<7 || nToX>5 || nToX<3)
						return false;//Ŀ����ھŹ�֮��
					
					if(Math.abs(nFromY-nToY)+Math.abs(nFromX-nToX)>1)
						return false;//��˧ֻ��һ��ֱ��
				}
			}
			else
			{
				if(nTargetID==B_KING)//�ж��Ƿ�˧����
				{
					if(nFromX!=nToX)//�����겻���
						return false;//��˧����ͬһ��
					
					for(i=nFromY+1;i<nToY;i++)
						if(position[i][nFromX]!=NOCHESS)
							return false;//�м��������
				}
				else
				{
					if(nToY>2 || nToX>5 || nToX<3)
						return false;//Ŀ����ھŹ�֮��
					
					if(Math.abs(nFromY-nToY)+Math.abs(nFromX-nToX)>1)
						return false;//��˧ֻ��һ��ֱ��
				}
			}

			break;

		case R_BISHOP://��ʿ
			if(nUserChessColor==REDCHESS)
			{
				if(nToY<7 || nToX>5 || nToX<3)
					return false;//ʿ���Ź�
			}
			else
			{
				if(nToY>2 || nToX>5 || nToX<3)
					return false;//ʿ���Ź�
			}

			if(Math.abs(nFromX-nToX)!=1 || Math.abs(nFromY-nToY)!=1)
					return false;//ʿ��б��

			break;

		case B_BISHOP://��ʿ
			if(nUserChessColor==REDCHESS)
			{
				if(nToY>2 || nToX>5 || nToX<3)
					return false;//ʿ���Ź�
			}
			else
			{
				if(nToY<7 || nToX>5 || nToX<3)
				return false;//ʿ���Ź�
			}

			if(Math.abs(nFromX-nToX)!=1 || Math.abs(nFromY-nToY)!=1)
				return false;//ʿ��б��

			break;

		case R_ELEPHANT://����
			if(nUserChessColor==REDCHESS)
			{
				if(nToY<5)
					return false;//�಻�ܹ���
			}
			else
			{
				if(nToY>4)
					return false;//�಻�ܹ���
			}

			if(Math.abs(nFromX-nToX)!=2 || Math.abs(nFromY-nToY)!=2)
				return false;//��������

			if(position[(nFromY +nToY)/2][(nFromX +nToX)/2]!=NOCHESS)
				return false;//���۱���

			break;

		case B_ELEPHANT://����
			if(nUserChessColor==REDCHESS)
			{
				if(nToY>4)
					return false;//���ܹ���
			}
			else
			{
				if(nToY<5)
					return false;//���ܹ���
			}

			if(Math.abs(nFromX-nToX)!=2 || Math.abs(nFromY-nToY)!=2)
				return false;//��������

			if(position[(nFromY +nToY)/2][(nFromX +nToX)/2]!=NOCHESS)
				return false;//���۱���

			break;

		case B_PAWN://����
			if(nUserChessColor==REDCHESS)
			{
				if(nToY<nFromY)
					return false;//�䲻�ܻ�ͷ
				
				if(nFromY<5 && nFromY==nToY)
					return false;//�����ǰֻ��ֱ��
			}
			else
			{
				if(nToY>nFromY)
					return false;//�䲻�ܻ�ͷ
				
				if(nFromY>4 && nFromY==nToY)
					return false;//�����ǰֻ��ֱ��
			}

			if(nToY-nFromY+Math.abs(nToX-nFromX)>1)
				return false;//��ֻ��һ��ֱ��

			break;

		case R_PAWN://���
			if(nUserChessColor==REDCHESS)
			{
				if(nToY>nFromY)
					return false;//�����ܻ�ͷ
				
				if(nFromY>4 && nFromY==nToY)
					return false;//������ǰֻ��ֱ��
			}
			else
			{
				if(nToY<nFromY)
					return false;//�����ܻ�ͷ
				
				if(nFromY<5 && nFromY==nToY)
					return false;//������ǰֻ��ֱ��
			}

			if(nFromY-nToY+Math.abs(nToX-nFromX)>1)
				return false;//��ֻ��һ��ֱ��

			break;

		case B_CAR://�ڳ�
		case R_CAR://�쳵
			if(nFromY!=nToY && nFromX!=nToX)
				return false;//����ֱ��

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

		case B_HORSE://����
		case R_HORSE://����
			if(!((Math.abs(nToX-nFromX)==1 && Math.abs(nToY -nFromY)==2) || (Math.abs(nToX-nFromX)==2&&Math.abs(nToY -nFromY)==1)))
				return false;//��������

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
				return false;//������
			
			break;

		case B_CANON://����
		case R_CANON://����
			if(nFromY!=nToY && nFromX!=nToX)
				return false;//����ֱ��

			//�ڳ���ʱ������·���в���������
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
			else//�ڳ���ʱ
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
																		 //��m_MoveList�в���һ���߷�,nPly�������뵽List�ڼ���

	
	public CHESSMOVE [][]m_MoveList=new CHESSMOVE[8][80];//���CreatePossibleMove�����������߷��Ķ���

	protected int m_nMoveCount;//��¼m_MoveList���߷�������
	protected int m_nUserChessColor;
	//define �ж�������
	 int BLACKCHESS=1;//�ڷ�
	 int REDCHESS=2;//�췽
	 int DS_DEFAULTSET=1;
	 int DS_USERDEFINE=2;

	 int CS_PCCHESS=1;//�˻�����
	 int CS_PPCHESS	 =2;//���˶���
	 int CS_CCCHESS   =3;//��������
	 int CS_HASHCHESS =4;//���Ӷ���

	//--------����--------
	 final int NOCHESS=0 ;//û������

	 final int B_KING=1 ;//��˧
	 final int B_CAR=2; //�ڳ�
	 final int B_HORSE=3; //����
	 final int B_CANON =4; //����
	 final int B_BISHOP   =5; //��ʿ
	 final int B_ELEPHANT =6; //����
	 final int B_PAWN     =7; //����
	 final int B_BEGIN    =B_KING;
	 final int B_END      =B_PAWN;

	 final int R_KING	 =  8 ;//�콫
	 final int R_CAR    =  9 ;//�쳵
	 final int R_HORSE   = 10;//����
	 final int R_CANON   = 11;//����
	 final int R_BISHOP   =12;//��ʿ
	 final int R_ELEPHANT =13;//����
	 final int R_PAWN     =14;//���
	 final int R_BEGIN    =R_KING;
	 final int R_END      =R_PAWN;
	//--------------------

	 boolean IsBlack(int x)
	 {return (x>=B_BEGIN && x<=B_END);}//�ж�ĳ�������ǲ��Ǻ�ɫ
	 boolean IsRed(int x) 
	 {return (x>=R_BEGIN && x<=R_END);}//�ж�ĳ�������ǲ��Ǻ�ɫ

	//�ж����������ǲ���ͬɫ
	 boolean IsSameSide(int x,int y) 
	 {
		 return ((IsBlack(x) && IsBlack(y)) || (IsRed(x) && IsRed(y)));
	 }
}

