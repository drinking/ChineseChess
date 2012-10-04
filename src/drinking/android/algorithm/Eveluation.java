package drinking.android.algorithm;


import java.lang.Math;
public class Eveluation {
	final int BA0[][]=
	{
		{0,0,0,0,0,0,0,0,0},
		{120,120,140,150,150,150,140,120,120},
		{120,120,140,150,150,150,140,120,120},
		{100,120,140,140,140,140,140,120,100},
		{100,100,100,100,100,100,100,100,100},
		{0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0},
	};

	//����ĸ���ֵ����
	final int BA1[][]=
	{
		{0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0},
		{100,100,100,100,100,100,100,100,100},
		{100,120,140,140,140,140,140,120,100},
		{120,120,140,150,150,150,140,120,120},
		{120,120,140,150,150,150,140,120,120},
		{0,0,0,0,0,0,0,0,0},
	};
	int BASEVALUE_PAWN    =   100;
	int BASEVALUE_BISHOP	= 250;
	int BASEVALUE_ELEPHANT  = 250;
	int BASEVALUE_HORSE		= 700;
	int BASEVALUE_CANON		= 700;
	int BASEVALUE_CAR		= 1400;
	int BASEVALUE_KING	    = 10000;

	//������������ԣ�Ҳ����ÿ��һ������λ��Ӧ�ӵķ�ֵ
	//��15��ʿ1����1����6����12����6����0
	int FLEXIBILITY_PAWN	= 15;
	int FLEXIBILITY_BISHOP  = 1;
	int FLEXIBILITY_ELEPHANT= 1;
	int FLEXIBILITY_CAR		= 6;
	int FLEXIBILITY_HORSE	= 12;
	int FLEXIBILITY_CANON	= 6;
	int FLEXIBILITY_KING   =  0;
	
	int []m_BaseValue=new int[15];		 //������ӻ�����ֵ
	int []m_FlexValue=new int[15];		 //�����������Է�ֵ
	int [][]m_AttackPos= new int [10][9];	 //���ÿһλ�ñ���в����Ϣ
	int [][]m_GuardPos=new int[10][9];      //���ÿһλ�ñ���������Ϣ
	int [][] m_FlexibilityPos=new int[10][9];//���ÿһλ�������ӵ�����Է�ֵ
	int [][]m_chessValue=new int[10][9];	 //���ÿһλ�������ӵ��ܼ�ֵ
	int nPosCount;				 //��¼һ���ӵ����λ�ø���
	mPOINT []RelatePos=new mPOINT[20];		 //��¼һ���ӵ����λ��
	long m_nAccessCount;
	
	
	
	
	
	
	
	
	
	
	
	
	
	int GetBingValue(int x,int y,int CurSituation[][])
	{
		if(CurSituation[y][x]==R_PAWN)
			return BA0[y][x];

		if(CurSituation[y][x]==B_PAWN)
			return BA1[y][x];

		return 0;
	}

	public Eveluation()
	{
		//��ʼ��ÿ�����ӵĻ�����ֵ����
		m_BaseValue[B_KING]=BASEVALUE_KING;
		m_BaseValue[B_CAR]=BASEVALUE_CAR;
		m_BaseValue[B_HORSE]=BASEVALUE_HORSE;
		m_BaseValue[B_BISHOP]=BASEVALUE_BISHOP;
		m_BaseValue[B_ELEPHANT]=BASEVALUE_ELEPHANT;
		m_BaseValue[B_CANON]=BASEVALUE_CANON;
		m_BaseValue[B_PAWN]=BASEVALUE_PAWN;

		m_BaseValue[R_KING]=BASEVALUE_KING;
		m_BaseValue[R_CAR]=BASEVALUE_CAR;
		m_BaseValue[R_HORSE]=BASEVALUE_HORSE;
		m_BaseValue[R_BISHOP]=BASEVALUE_BISHOP;
		m_BaseValue[R_ELEPHANT]=BASEVALUE_ELEPHANT;
		m_BaseValue[R_CANON]=BASEVALUE_CANON;
		m_BaseValue[R_PAWN]=BASEVALUE_PAWN;

		//��ʼ������Է�ֵ����
		m_FlexValue[B_KING]=FLEXIBILITY_KING;
		m_FlexValue[B_CAR]=FLEXIBILITY_CAR;
		m_FlexValue[B_HORSE]=FLEXIBILITY_HORSE;
		m_FlexValue[B_BISHOP]=FLEXIBILITY_BISHOP;
		m_FlexValue[B_ELEPHANT]=FLEXIBILITY_ELEPHANT;
		m_FlexValue[B_CANON]=FLEXIBILITY_CANON;
		m_FlexValue[B_PAWN]=FLEXIBILITY_PAWN;

		m_FlexValue[R_KING]=FLEXIBILITY_KING;
		m_FlexValue[R_CAR]=FLEXIBILITY_CAR;
		m_FlexValue[R_HORSE]=FLEXIBILITY_HORSE;
		m_FlexValue[R_BISHOP]=FLEXIBILITY_BISHOP;
		m_FlexValue[R_ELEPHANT]=FLEXIBILITY_ELEPHANT;
		m_FlexValue[R_CANON]=FLEXIBILITY_CANON;
		m_FlexValue[R_PAWN]=FLEXIBILITY_PAWN;

		m_nAccessCount=0;
		for(int i=0;i<20;i++)
		{
			RelatePos[i]=new mPOINT();
		}
	}


	int Eveluate(int position[][], boolean bIsRedTurn,int nUserChessColor)
	{
		int i,j,k;
		int nChessType,nTargetType;
		
		m_nAccessCount++;//ÿ����һ�ξ�����һ��

		
		//��ʼ��
		iniarray(m_chessValue);
		iniarray(m_AttackPos);
		iniarray(m_GuardPos);
		iniarray(m_FlexibilityPos);
		//Arrays.fill(m_chessValue, 0);
		//Arrays.fill(m_AttackPos, 0);
		//Arrays.fill(m_GuardPos, 0);
		//Arrays.fill(m_FlexibilityPos, 0);
		/*
		int []m_BaseValue=new int[15];		 //������ӻ�����ֵ
		int []m_FlexValue=new int[15];		 //�����������Է�ֵ
		int [][]m_AttackPos= new int [10][9];	 //���ÿһλ�ñ���в����Ϣ
		int [][]m_GuardPos=new int[10][9];      //���ÿһλ�ñ���������Ϣ
		int [][] m_FlexibilityPos=new int[10][9];//���ÿһλ�������ӵ�����Է�ֵ
		int [][]m_chessValue=new int[10][9];	 //���ÿһλ�������ӵ��ܼ�ֵ
		int nPosCount;				 //��¼һ���ӵ����λ�ø���
		mPOINT []RelatePos=new mPOINT[20];		 //��¼һ���ӵ����λ��
		long m_nAccessCount;
		*/
		
		//ɨ�����̣��ҳ�ÿһ�����ӣ�������в/���������ӣ������������
		for(i=0;i<10;i++)
			for(j=0;j<9;j++)
			{
			
				if(position[i][j]!=NOCHESS)
				{
					
					nChessType=position[i][j];   //ȡ��������
					GetRelatePiece(position,j,i);//�ҳ��������������λ��
					for(k=0;k<nPosCount;k++)     //��ÿһĿ��λ��
					{		
						nTargetType=position[RelatePos[k].y][RelatePos[k].x];//ȡĿ����������
						if(nTargetType==NOCHESS)//����ǿհף����������
						{
									
							m_FlexibilityPos[i][j]++;
															
						}
						else//������
						{
							
							if(IsSameSide(nChessType,nTargetType))
							{
								//�������ӣ�Ŀ���ܱ���
								m_GuardPos[RelatePos[k].y][RelatePos[k].x]++;
							}
							else
							{
								//�з����ӣ�Ŀ������в
								m_AttackPos[RelatePos[k].y][RelatePos[k].x]++;
								m_FlexibilityPos[i][j]++;//���������
								switch(nTargetType)
								{
									case R_KING://��˧
										if(!bIsRedTurn)//�ֵ�������
											return 18888;//����ʧ�ܼ�ֵ
										break;
									
									case B_KING://�ڽ�
										if(bIsRedTurn)//�ֵ�������
											return 18888;//����ʧ�ܼ�ֵ
										break;
										
									default:
										//������в�����Ӽ�����в��ֵ
										m_AttackPos[RelatePos[k].y][RelatePos[k].x]+=(30 +(m_BaseValue[nTargetType]- m_BaseValue[nChessType])/10)/10;
										break;
								}
							}
							
						}
					}
				}
				
			}
		
		//�����ѭ��ͳ��ɨ�赽������
		for(i=0;i<10;i++)
			for(j=0;j<9;j++)
			{
				if(position[i][j]!=NOCHESS)
				{
					nChessType=position[i][j];
					m_chessValue[i][j]++;
					//������Ӵ������ֵ��Ϊ0����ÿһ���ӵ�����Լ�ֵ�ӽ����Ӽ�ֵ
					m_chessValue[i][j]+=m_FlexValue[nChessType]*m_FlexibilityPos[i][j];
					//���ϱ��ĸ���ֵ
					m_chessValue[i][j]+=GetBingValue(j,i,position);
				}
			}
		
		//�����ѭ������ͳ��ɨ�赽������
		int nHalfvalue;
		for(i=0;i<10;i++)
			for(j=0;j<9;j++)
			{
				if(position[i][j]!=NOCHESS)
				{
					nChessType=position[i][j];			
					nHalfvalue=m_BaseValue[nChessType]/16;      //���ӻ�����ֵ��1/16��Ϊ��в/��������			
					m_chessValue[i][j]+=m_BaseValue[nChessType];//ÿ�����ӵĻ�����ֵ�������ܼ�ֵ
					if(IsRed(nChessType))//����
					{
						if(m_AttackPos[i][j]!=0)//��ǰ�����������в
						{
							if(bIsRedTurn)//�ֵ�������
							{						
								if(nChessType==R_KING)//����Ǻ콫
									m_chessValue[i][j]-=20;//��ֵ����20
								else
								{
									//��ֵ��ȥ2��nHalfvalue
									m_chessValue[i][j]-=nHalfvalue*2;
									if(m_GuardPos[i][j]!=0)//�Ƿ񱻼������ӱ���
										m_chessValue[i][j]+=nHalfvalue;//�������ټ���nHalfvalue
								}
							}
							else//��ǰ���屻��в���ֵ�������
							{
								if(nChessType==R_KING)//�Ƿ��Ǻ�˧
									return 18888;//����ʧ�ܼ�ֵ	
								m_chessValue[i][j]-=nHalfvalue*10;//��ȥ10����nHalfvalue,��ʾ��в�̶ȸ�
								if(m_GuardPos[i][j]!=0)//���������
									m_chessValue[i][j]+=nHalfvalue*9;//�������ټ���9����nHalfvalue
							}
							//����в�����Ӽ�����в���ֹһ������в
							//һ���������ĳ�������ֵ����û�з�ӳ��������
							m_chessValue[i][j]-=m_AttackPos[i][j];
						}
						else
						{
							//û����в
							if(m_GuardPos[i][j]!=0)
								m_chessValue[i][j]+=5;//�ܱ�������һ���
						}
					}
					else
					{
						//����Ǻ���
						if(m_AttackPos[i][j]!=0)
						{
							//����в
							if(!bIsRedTurn)
							{
								//�ֵ�������
								if(nChessType==B_KING)//����Ǻڽ�
									m_chessValue[i][j]-=20;//���Ӽ�ֵ����20
								else
								{ 
									//���Ӽ�ֵ����2��nHalfvalue
									m_chessValue[i][j]-=nHalfvalue*2;
									if(m_GuardPos[i][j]!=0)//����ܱ���									
										m_chessValue[i][j] +=nHalfvalue;//���Ӽ�ֵ����nHalfvalue
								}
							}
							else
							{
								//�ֵ�������
								if(nChessType==B_KING)//�Ǻڽ�
									return 18888;//����ʧ�ܼ�ֵ							
								m_chessValue[i][j]-=nHalfvalue*10;//���Ӽ�ֵ����10��nHalfvalue
								if(m_GuardPos[i][j]!=0)//�ܱ���
									m_chessValue[i][j]+=nHalfvalue*9;//�������ټ���9��nHalfvalue
							}
							//����в�������ټ�����в��
							//��ֹһ������вһ���������ĳ�,����ֵ����û�з�ӳ���������
							m_chessValue[i][j]-=m_AttackPos[i][j];
						}
						else
						{
							//������в
							if(m_GuardPos[i][j]!=0)
								m_chessValue[i][j]+=5;//�ܱ�������һ���
						}
					}
				}
			}
			
		//����ͳ����ÿ�����ӵ��ܼ�ֵ
		//����ͳ�ƺ�������ܷ�
		int nRedValue=0;int nBlackValue=0;
		for(i=0;i<10;i++)
			for(j=0;j<9;j++)
			{
				nChessType=position[i][j];
				if(nChessType!=NOCHESS)
				{
					if(IsRed(nChessType))
						nRedValue+=m_chessValue[i][j];  //�Ѻ����ֵ����
					else
						nBlackValue+=m_chessValue[i][j];//�Ѻ����ֵ����
				}
			}

		if(nUserChessColor==REDCHESS)
		{
			if(bIsRedTurn)
				return nRedValue-nBlackValue;//����ֵ������߷��ع�ֵ

			return nBlackValue-nRedValue;//����ֵ������߷��ظ���ֵ
		}

		if(bIsRedTurn)
			return nBlackValue-nRedValue;//����ֵ������߷��ظ���ֵ			
		
		return nRedValue-nBlackValue;//����ֵ������߷��ع�ֵ
	}

	int GetRelatePiece(int position[][], int j, int i)
	{
		
		nPosCount=0;
		int nChessID=0;
		boolean flag;
		int x,y;
		
		nChessID=position[i][j];
		
		switch(nChessID)
		{
		case R_KING://��˧
		case B_KING://�ڽ�
			//ѭ�����Ź�֮����Щλ�ÿɵ���/����
			//ɨ�����߾͹���������������
			
			for(y=0;y<3;y++)
			{
				for(x=3;x<6;x++)
				{
					if(CanTouch(position,j,i,x,y))//�ܷ񵽴�
						AddPoint(x,y);//�ɴﵽ/������λ�ü�������
				}
			}
			
			//ѭ�����Ź�֮����Щλ�ÿɵ���/����
			//ɨ�����߾͹���������������
			for(y=7;y<10;y++)
			{
				for(x=3;x<6;x++)
				{
					if(CanTouch(position,j,i,x,y))
					{//�ܷ񵽴�
						
						AddPoint(x,y);//�ɴﵽ/������λ�ü�������
						
					}
				}
			}
			break;

		case R_BISHOP://��ʿ
			//ѭ�����Ź�֮����Щλ�ÿɵ���/����
			for(y=7;y<10;y++)
				for(x=3;x<6;x++)
					if(CanTouch(position,j,i,x,y))
						AddPoint(x,y);//�ɴﵽ/������λ�ü�������
			
			break;

		case B_BISHOP://��ʿ
			//ѭ�����Ź�֮����Щλ�ÿɵ���/����
			for(y=0;y<3;y++)
				for(x=3;x<6;x++)
					if(CanTouch(position,j,i,x,y))
						AddPoint(x,y);//�ɴﵽ/������λ�ü�������
			
			break;

		case R_ELEPHANT://����
		case B_ELEPHANT://����
			//����
			x=j+2;
			y=i+2;
			if(x<9 && y<10 && CanTouch(position,j,i,x,y))
				AddPoint(x,y);

			//����
			x=j+2;
			y=i-2;
			if(x<9 && y>=0 && CanTouch(position,j,i,x,y))
				AddPoint(x,y);

			//����
			x=j-2;
			y=i+2;
			if(x>=0 && y<10 && CanTouch(position,j,i,x,y))
				AddPoint(x,y);

			//����
			x=j-2;
			y=i-2;
			if(x>=0 && y>=0 && CanTouch(position,j,i,x,y))
				AddPoint(x,y);

			break;

			case R_HORSE://����
			case B_HORSE://����
				//������·��ܷ񵽴�/����
				
				x=j+2;
				y=i+1;
				
				if((x<9 && y<10))
				{
					if(CanTouch(position,j,i,x,y))
					{
					
					AddPoint(x,y);
					}
				}
			
				//������Ϸ��ܷ񵽴�/����
				x=j+2;
				y=i-1;
			
				if((x<9 && y>=0))
				{
					if(CanTouch(position,j,i,x,y))
					{
					AddPoint(x,y);			
					}
				}
				
				//������·��ܷ񵽴�/����
				x=j-2;
				y=i+1;
				if((x>=0 && y<10))
				{
					if(CanTouch(position,j,i,x,y))
					AddPoint(x,y);
				}
				
				//������Ϸ��ܷ񵽴�/����
				x=j-2;
				y=i-1;
				if((x>=0 && y>=0))
				{if(CanTouch(position,j,i,x,y))
					AddPoint(x,y);
				}
				//������·��ܷ񵽴�/����
				x=j+1;
				y=i+2;
				if((x<9 && y<10))
				{if(CanTouch(position,j,i,x,y))
					AddPoint(x,y);
				}
				//������Ϸ��ܷ񵽴�/����
				x=j+1;
				y=i-2;
				if((x<9 && y>=0))
				{
					if(CanTouch(position,j,i,x,y))
					AddPoint(x,y);
				}
				//������·��ܷ񵽴�/����
				x=j-1;
				y=i+2;
				if((x>=0 && y<10))
				{if(CanTouch(position,j,i,x,y))
					AddPoint(x,y);
				}
				//������Ϸ��ܷ񵽴�/����
				x=j-1;
				y=i-2;
				if((x>=0 && y>=0))
				{if(CanTouch(position,j,i,x,y))
					AddPoint(x,y);
				}
				
				break;

			case R_CAR://�쳵
			case B_CAR://�ڳ�
				//��������ܷ񵽴�/����
				
				x=j+1;
				y=i;
				while(x<9)
				{
					if(NOCHESS==position[y][x])//�հ�
					{
						
						AddPoint(x,y);
						
					}
					else{
						//������һ������					
						AddPoint(x,y);
						break;//�����λ�ò�������
					}
					x++;
				}
				
				//��������ܷ񵽴�/����
				x=j-1;
				y=i;
				while(x>=0)
				{
					if(NOCHESS==position[y][x])//�հ�
						AddPoint(x,y);
					else{
						//������һ������
						AddPoint(x,y);
						break;//�����λ�ò�������
					}
					x--;
				}

				//��������ܷ񵽴�/����
				x=j;
				y=i+1;
				while(y<10)
				{
					if(NOCHESS==position[y][x])//�հ�
						AddPoint(x,y);
					else{
						//������һ������
						AddPoint(x,y);
						break;//�����λ�ò�������
					}
					y++;
				}

				//��������ܷ񵽴�/����
				x=j;
				y=i-1;
				while(y>=0)
				{
					if(NOCHESS==position[y][x])//�հ�
						AddPoint(x,y);
					else{
						//������һ������
						AddPoint(x,y);
						break;//�����λ�ò�������
					}
					y--;
				}
				
				break;

			case R_PAWN://���
				//�ۿ���ǰ�Ƿ񵽵�
				y=i-1;
				x=j;
				if(y>=0)
					AddPoint(x,y);//û���ף�����
				if(i<5)
				{
					//���ѹ���
					y=i;
					x=j+1;//����
					if(x<9)
						AddPoint(x,y);//δ���ұߣ�����
					x=j-1;//����
					if(x>=0)
						AddPoint(x,y);//δ����ߣ�����
				}

				break;

			case B_PAWN://����
				//�ۿ���ǰ�Ƿ񵽵�
				y=i+1;
				x=j;
				if(y<10)
					AddPoint(x,y);//û���ף�����
				if(i>4)
				{
					//���ѹ���
					y=i;
					x=j+1;//����
					if(x<9)
						AddPoint(x,y);//δ���ұߣ�����
					x=j-1;//����
					if(x>=0)
						AddPoint(x,y);//δ����ߣ�����
				}

				break;

			case B_CANON://����
			case R_CANON://����
				//��������ܷ񵽴�/������λ��
				x=j+1;
				y=i;
				flag=false;
				while(x<9)
				{
					if(NOCHESS==position[y][x])
					{
						//�հ�λ��
						if(!flag)
							AddPoint(x,y);
					}
					else
					{
						if(!flag)
							flag=true;//�ǵ�һ������
						else
						{
							//�ǵڶ�������
							AddPoint(x,y);
							break;
						}
					}
					x++;//��������
				}

				//��������ܷ񵽴�/������λ��
				x=j-1;
				y=i;
				flag=false;
				while(x>=0)
				{
					if(NOCHESS==position[y][x])
					{
						//�հ�λ��
						if(!flag)
							AddPoint(x,y);
					}
					else
					{
						if(!flag)
							flag=true;//�ǵ�һ������
						else
						{
							//�ǵڶ�������
							AddPoint(x,y);
							break;
						}
					}
					x--;//��������
				}

				//��������ܷ񵽴�/������λ��
				x=j;
				y=i+1;
				flag=false;
				while(y<10)
				{
					if(NOCHESS==position[y][x])
					{
						//�հ�λ��
						if(!flag)
							AddPoint(x,y);
					}
					else
					{
						if(!flag)
							flag=true;//�ǵ�һ������
						else
						{
							//�ǵڶ�������
							AddPoint(x,y);
							break;
						}
					}
					y++;//��������
				}

				//��������ܷ񵽴�/������λ��
				x=j;
				y=i-1;
				flag=false;
				while(y>=0)
				{
					if(NOCHESS==position[y][x])
					{
						//�հ�λ��
						if(!flag)
							AddPoint(x,y);
					}
					else
					{
						if(!flag)
							flag=true;//�ǵ�һ������
						else
						{
							//�ǵڶ�������
							AddPoint(x,y);
							break;
						}
					}
					y--;//��������
				}

				break;

			default:
				break;
			}
		
		return nPosCount;
	}

	boolean CanTouch(int position[][], int nFromX, int nFromY, int nToX, int nToY)
	{
		
		int i=0,j=0;
		int nMoveChessID,nTargetID;

		if(nFromX==nToX && nFromY==nToY)
			return false;//Ŀ����Դ��ͬ���Ƿ�
	
		if(nFromY==9)
		{
			nFromY=0;
			nFromX++;
		}
		nMoveChessID=position[nFromX][nFromY];
		
		if(nToY==9)
		{
			nToX++;
			nToY=0;
		}
		nTargetID=position[nToX][nToY];
		if(IsSameSide(nMoveChessID,nTargetID))
			return false;//���Լ����壬�Ƿ�

		switch(nMoveChessID)
		{
		case B_KING://�ڽ�
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

			break;

		case R_KING://��˧
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

			break;

		case R_BISHOP://��ʿ
			if(nToY<7 || nToX>5 || nToX<3)
				return false;//ʿ���Ź�

			if(Math.abs(nFromX-nToX)!=1 || Math.abs(nFromY-nToY)!=1)
				return false;//ʿ��б��

			break;

		case B_BISHOP://��ʿ
			if(nToY>2 || nToX>5 || nToX<3)
				return false;//ʿ���Ź�

			if(Math.abs(nFromX-nToX)!=1 || Math.abs(nFromY-nToY)!=1)
				return false;//ʿ��б��

			break;

		case R_ELEPHANT://����
			if(nToY<5)
				return false;//�಻�ܹ���

			if(Math.abs(nFromX-nToX)!=2 || Math.abs(nFromY-nToY)!=2)
				return false;//��������

			if(position[(nFromY +nToY)/2][(nFromX +nToX)/2]!=NOCHESS)
				return false;//���۱���

			break;

		case B_ELEPHANT://����
			if(nToY>4)
				return false;//���ܹ���

			if(Math.abs(nFromX-nToX)!=2 || Math.abs(nFromY-nToY)!=2)
				return false;//��������

			if(position[(nFromY +nToY)/2][(nFromX +nToX)/2]!=NOCHESS)
				return false;//���۱���

			break;

		case B_PAWN://����
			if(nToY<nFromY)
				return false;//�䲻�ܻ�ͷ

			if(nFromY<5 && nFromY==nToY)
				return false;//�����ǰֻ��ֱ��

			if(nToY-nFromY+Math.abs(nToX -nFromX)>1)
				return false;//��ֻ��һ��ֱ��

			break;

		case R_PAWN://���
			if(nToY<nFromY)
				return false;//�����ܻ�ͷ

			if(nFromY>4 && nFromY==nToY)
				return false;//������ǰֻ��ֱ��

			if(nToY-nFromY+Math.abs(nToX -nFromX)>1)
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
						for(jj=nFromY+1;jj<nToY;jj++)
							if(position[jj][nFromX]!=NOCHESS)
								jj++;
						if(jj!=1)
							return false;
					}
					else
					{
						for(jj=nToY+1;jj<nFromY;jj++)
							if(position[jj][nFromX]!=NOCHESS)
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

	void AddPoint(int x, int y)
	{
		//RelatePos[nPosCount]=new mPOINT();
		RelatePos[nPosCount].x=x;
		RelatePos[nPosCount].y=y;

		nPosCount++;
	}
	///////////////////////////
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
	 public long GetAccessCount(){return m_nAccessCount;};//�õ����ʽڵ���
	public void ClearAccessCount(){m_nAccessCount=0;};  //���÷��ʽڵ���Ϊ0
	public void iniarray(int array[][])
	{	int i,j;
		for(i=0;i<10;i++)
		{for(j=0;j<9;j++)
		{
			array[i][j]=0;
		}
		}
	}

}
