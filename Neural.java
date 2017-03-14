package abc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Neural 
{
	static Integer[][] wt1=new Integer[3][4];
	
	//================function to read updated matrix from a file==================
	public static Integer[][] readMatrix(File file1) throws IOException
	{
		FileReader in1= new FileReader(file1);
		BufferedReader bf1=new BufferedReader(in1);
		for(int i=0;i<3;i++)
		{
			String str=bf1.readLine();
			String[] strtemp=str.split("\t");
			for(int j=0;j<4;j++)
			{
				wt1[i][j]=Integer.valueOf(strtemp[j]);
			}
		}
		return wt1;
	}
	
	
	//===========================Positive Neural network============================
	//nn to check positive sentiment of a sentence
	public static int good(Integer[] input,int flag) throws IOException
	{
		Integer[] array3=new Integer[4];
		Integer[] input1=new Integer[3];
		input1=input;
			
		Integer[][] wt1=new Integer[4][4];
		//File file1= new File("C:/Project/weight.txt");
		File file1= new File("abc/weight.txt");
		wt1=readMatrix(file1);
			
		//output from hidden layer
		for(int j=0;j<4;j++)
		{
			array3[j]=0;
		}
			
		for(int i=0;i<3;i++)
		{
			for(int j=0;j<4;j++)
			{
				array3[j]=array3[j]+input1[i]*wt1[i][j];
			}
		}
			
		//activation function
		int y1=array3[0]+array3[1]+array3[2]+array3[3];
		
		//final 1 bit output from nn
			
		if(y1>0)
			y1=1;
		else if(y1<=0)
			y1=0;
				
		//=====================error recovery function=====================
			
		Integer[] deltaW=new Integer[3];
				
		if(flag!=0)
		{
			if(y1 != 1)
			{
				System.out.println("It is not recognizing....so calculate new weight matrix:");
				//calculate deltaW for good
				for(int j=0;j<3;j++)
				{
					deltaW[j]=y1-input1[j];
				}
					
				System.out.println("\n New weight matrix is:");
				//calculate new wt matrix
				for(int i=0;i<3;i++)
				{
					for(int j=0;j<4;j++)
					{
						wt1[i][j]=wt1[i][j]+deltaW[i];
						//System.out.println("---"+wt1[i][j]);
					}
				}
					
				//================write updated wt matrix into a file==============
				//File file = new File("C:/Project/weight.txt");
				File file= new File("abc/weight.txt");
				if (!file.exists()) 
				{
					file.createNewFile();
				}

				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				for(int i=0;i<3;i++)
				{
					for(int j=0;j<4;j++)
					{
						bw.write(wt1[i][j].toString());
						bw.write("\t");
					}
					bw.write("\n");
				}
				bw.close();
			}
		}
		return y1;
	}
	
	//=================================Negative Neural Network==================================
	//nn to check negative sentiment of a sentence
	public static int bad(Integer[] input, int flag) throws IOException
	{
		Integer[] array3=new Integer[4];
		Integer[] input1=new Integer[3];
		input1=input;
		
		Integer[][] wt1=new Integer[4][4];
		//File file1= new File("C:/Project/weight1.txt");
		File file1= new File("abc/weight1.txt");
		wt1=readMatrix(file1);
		
		//output from hidden layer
		for(int j=0;j<4;j++)
		{
			array3[j]=0;
		}
		
		for(int i=0;i<3;i++)
		{
			for(int j=0;j<4;j++)
			{
				array3[j]=array3[j]+input1[i]*wt1[i][j];
			}
		}
		
		//activation function
		int y1=array3[0]+array3[1]+array3[2]+array3[3];
	
		//final 1 bit output from nn
		
		if(y1>=0)
			y1=0;	
		else if(y1<0)
			y1=-1;
		
			
		//===============================error recovery function======================
		
		Integer[] deltaW=new Integer[3];
			
		if(flag!=0)
		{
			if(y1 != -1)
			{
				System.out.println("It is not recognizing....so calculate new weight matrix:");
				
				//calculate deltaW for good
				for(int j=0;j<3;j++)
				{
					deltaW[j]=y1-input1[j];
				}
				
				System.out.println("\n New weight matrix is:");
				//calculate new wt matrix
				for(int i=0;i<3;i++)
				{
					for(int j=0;j<4;j++)
					{
						wt1[i][j]=wt1[i][j]+deltaW[i];
						//System.out.println("---"+wt1[i][j]);
					}
				}
				
				//======================write updated wt matrix into a file============
				//File file = new File("C:/Project/weight1.txt");
				File file= new File("abc/weight1.txt");

				if (!file.exists()) 
				{
					file.createNewFile();
				}

				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				for(int i=0;i<3;i++)
				{
					for(int j=0;j<4;j++)
					{
						bw.write(wt1[i][j].toString());
						bw.write("\t");
					}
					bw.write("\n");
				}
				bw.close();
			}
		}
		return y1;
	}
	
}
