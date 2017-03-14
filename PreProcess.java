

package abc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.BreakIterator;
import java.util.ArrayList;

import rita.RiWordNet;

public class PreProcess 
{

	public PreProcess(){}
	
	public static void main(){}
	
    public float[] start(File res) throws IOException 
    {	
    	
    	//training positive nn
    	/*File fr1=new File("C:/Project/1.txt");
    	sc(fr1,1);
    	System.out.println("\n=======================================");
    	
    	//training positive nn
    	File fr2=new File("C:/Project/0.txt");
    	sc(fr2,-1);
    	System.out.println("\n=======================================");
    	*/
    	
    	float arr[]=new float[3];
    	//testing nn
    	arr=sc(res,0);
    	
    	return arr;
    }
    
    public float[] sc(File fr1, int flag) throws IOException
    {
    	//Set path to sentiWordnet
    	String pathToSWN="C:/all jar files/SentiWordNet_3.0.0/home/swn/www/admin/dump/SentiWordNet_3.0.0_20130122.txt";
    	//String pathToSWN="SentiWordNet_3.0.0/home/swn/www/admin/dump/SentiWordNet_3.0.0_20130122.txt";
    	SentiWordNetDemoCode sentiwordnet = new SentiWordNetDemoCode(pathToSWN);
    	
    	RiWordNet wordnet = new RiWordNet("C:/all jar files/WordNet-3.0");
    	//RiWordNet wordnet = new RiWordNet("WordNet-3.0");
		int k=0;
		String s;
		
		
		//===================Store all stopwords from a file into an array=====================
		
		String sCurrentLine;
		String[] stopwords = new String[2000];
		
		FileReader fr=new FileReader("C:/Project/StopWord.txt");
		//FileReader fr=new FileReader("StopWord.txt");
		BufferedReader br= new BufferedReader(fr);
			        
		while ((sCurrentLine = br.readLine()) != null)
		{
			stopwords[k]=sCurrentLine;
			k++;
		} 
		
		//==============read an input file=====================================
		
		File paperFile =fr1;
		StringBuilder paper = new StringBuilder();
		FileInputStream fstream1 = null;
		fstream1 = new FileInputStream(paperFile);
		BufferedReader br1 = new BufferedReader(new InputStreamReader(fstream1, "UTF-8"));
		String strLine1;
		    
		while ((strLine1 = br1.readLine()) != null) 
		{
	        paper.append(strLine1).append('\n');
	    }
		    
		String paperString = paper.toString();   
		BreakIterator boundary = BreakIterator.getSentenceInstance();
	    boundary.setText(paperString);
	    int start = boundary.first();
	    
	    
	    String[] top=new String[5];
	    double[] top1=new double[5];
	    String[] bottom=new String[5];
	    double[] bottom1=new double[5];
	    String[] sent1=new String[5];
	    String[] sent2=new String[5];
	    String[] features= new String[100];
	    int length_of_features=0;	
	    
	    double[] featureStrength= new double[100];   //array where we will store the numerical value of strength of each feature we find.

	    for( int i=0;i<100;i++)
		  featureStrength[i]=0;   ///initialize array to 0;
	    
	    
	    //file to store all features
	    String fileName3="C:/Project/noun1.txt";
	    //String fileName3="abc/noun1.txt";
		File file3 = new File(fileName3);
		FileWriter fw3 = new FileWriter(file3.getAbsoluteFile());
		BufferedWriter bw3 = new BufferedWriter(fw3);
	    
	    int i=0,l=0,j,pos=0,neg=0,neutral=0,pp=0,nn=0,nocondition=0;
	    
//	    code code chnges start
	    String content;
        int count=0;
		//File filetemp = new File("C:\\files\\Comments-iterator.txt");
        File filetemp = new File("abc\\Comments-iterator.txt");

		// if file doesnt exists, then create it
		if (!filetemp.exists()) {
			filetemp.createNewFile();
		}

		FileWriter fwtemp = new FileWriter(filetemp.getAbsoluteFile());
		BufferedWriter bwtemp = new BufferedWriter(fwtemp);
		
		//FileReader frtemp=new FileReader("C:\\files\\Comments-iterator1.txt");
		FileReader frtemp=new FileReader("abc\\Comments-iterator1.txt");
		BufferedReader brtemp= new BufferedReader(fr);
			        
//code code changes end
		
	    //pre-processing for each sentence
	    for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary.next()) 
	    {
	    	s=paper.substring(start, end);
	        System.out.println("\n\n"+s);
	        	 
	        

			
	        //===========formatting of each sentence==============
	        ArrayList<String> wordsList = new ArrayList<String>();
	 
			String[] wordsList1 = s.split("\\s+");
			
			for (int t = 0; t < wordsList1.length; t++) 
			{	 
				wordsList1[t] = wordsList1[t].replaceAll("[^\\w]", "");
			    wordsList.add(wordsList1[t]);	        
			}
			
			//=========remove all stopwords=================
			for(int ii = 0; ii < wordsList.size(); ii++)
			{
				if(wordsList != null)
			   	{
				    for(int jj = 0; jj < k; jj++)
				    {
				    	if(stopwords[jj].contains(wordsList.get(ii).toLowerCase()))
				    	{
				    		wordsList.remove(ii);
					        ii--;
					        break;
				    	}
				    }
			    }
				else
		           break;           
			}
			
			double[] score=new double[wordsList.size()];
			int n=0;
			
			
			//score each sentence
			for (String str : wordsList)
			{
				
				String b="k";
				if(wordnet.getBestPos(str) != null)
				{
					//get best part of speech of a word
					b= wordnet.getBestPos(str);
				}
							
			    double m=0,m1=0;
			    int ch;
			    String b1;
			    
			    //find score of a word using sentiWordnet and handle error
			    if(b.equalsIgnoreCase("a")||b.equalsIgnoreCase("n")||b.equalsIgnoreCase("v"))
			    {
			    		m=sentiwordnet.extract(str,b);
			    }
			    
			    boolean newFeature=true;
			    //store all Nouns in a file
				if(b.equalsIgnoreCase("n") && m != 0)
				{
					System.out.print("\n Noun=====>"+str);
					for(int r=0;r<length_of_features;r++)
					{
						//this condition is to check if we have not put the feature already in the list
						if(!features[r].equalsIgnoreCase(str)) 
						{
							newFeature = false;	
						}
					}

					//if feature is not present in a list
					if(newFeature==true && length_of_features<100)
					{
						features[length_of_features]=str;
						
						//find index of noun
						int index=wordsList.indexOf(str);
						
						//check adjacent adjective to a noun
						if((index-3)>=0 && (index-3)<wordsList.size())
						{
							if(wordsList.get(index-3)!=null)
							{
								//get pos of this word.
								String adj=wordsList.get(index-3);
								//if adjective then print to a file along with score
								b1= wordnet.getBestPos(adj);
								m1=sentiwordnet.extract(adj,b1);
								if(b1!=null && b1.equalsIgnoreCase("a") && m1 != 0)
								{
									featureStrength[length_of_features]=featureStrength[length_of_features]+m1;
								}
							}
						}
						
						if((index-2)>=0 && (index-2)<wordsList.size())
						{
							if(wordsList.get(index-2)!=null)
							{
								//get pos of this word.
								String adj=wordsList.get(index-2);
								//if adjective then print to a file
								b1= wordnet.getBestPos(adj);
								m1=sentiwordnet.extract(adj,b1);
								if(b1!=null && b1.equalsIgnoreCase("a") && m1 != 0)
								{
									//bw3.write("\n"+adj+" "+m1);
									featureStrength[length_of_features]=featureStrength[length_of_features]+m1;
								}
							}
						}
						
						if((index-1)>=0 && (index-1)<wordsList.size())
						{
							if(wordsList.get(index-1)!=null)
							{
								//get pos of this word.
								String adj=wordsList.get(index-1);
								//if adjective then print to a file
								b1= wordnet.getBestPos(adj);
								m1=sentiwordnet.extract(adj,b1);
								if(b1!=null && b1.equalsIgnoreCase("a") && m1 != 0)
								{
									//bw3.write("\n"+adj+" "+m1);
									featureStrength[length_of_features]=featureStrength[length_of_features]+m1;
								}
							}
						}
						
						if((index+1)>=0 && (index+1)<wordsList.size()) 
						{
							if(wordsList.get(index+1)!=null)
							{
								//get pos of this word.
								String adj=wordsList.get(index+1);
								//if adjective then print to a file
								b1= wordnet.getBestPos(adj);
								m1=sentiwordnet.extract(adj,b1);
								if(b1!=null && b1.equalsIgnoreCase("a") && m1 != 0)
								{
									//bw3.write("\n"+adj+" "+m1);
									featureStrength[length_of_features]=featureStrength[length_of_features]+m1;
								}
							}
						}
						
						if((index+2)>=0 && (index+2)<wordsList.size()) 
						{
							if(wordsList.get(index+2)!=null)
							{
								//get pos of this word.
								String adj=wordsList.get(index+2);
								//if adjective then print to a file
								b1= wordnet.getBestPos(adj);
								m1=sentiwordnet.extract(adj,b1);
								if(b1!=null && b1.equalsIgnoreCase("a") && m1 != 0)
								{
									//bw3.write("\n"+adj+" "+m1);
									featureStrength[length_of_features]=featureStrength[length_of_features]+m1;
								}
							}
						}
						
						if((index+3)>=0 && (index+3)<wordsList.size())
						{
							if(wordsList.get(index+3)!=null)
							{
								//get pos of this word.
								String adj=wordsList.get(index+3);
								//if adjective then print to a file
								b1= wordnet.getBestPos(adj);
								m1=sentiwordnet.extract(adj,b1);
								if(b1!=null && b1.equalsIgnoreCase("a") && m1 != 0)
								{
									//bw3.write(adj+" "+m1);
									featureStrength[length_of_features]=featureStrength[length_of_features]+m1;
								}
							}
						}
						
						//bw3.write(featureStrength[length_of_features]);
				
						length_of_features++;
					}
					//bw3.write("\n-------\n");
				}

			   
			    if(m != 0)
			    {
			    	if(flag==0)
			    	{
			    		//=======store top 5 positive words and sentences========
			    		if(checkWord(top,str)==0)
			    		{
			    			if(pp < 5 && m>=0.0)
			    			{
					    		top[pp]=str;
					    		top1[pp]=m;
					    		sent1[pp]=s;
					    		pp++;
			    			}
			    			else
			    			{
			    				sortpos(top,top1,sent1,pp);
			    				if(top1[0]<m)
			    				{
						   			top[0]=str;
					    			top1[0]=m;
					    			sent1[0]=s;
					    			//System.out.println("\n==="+top[0]+"\t"+top1[0]+"\t"+"0");
				    			
			    				}
			    				sortpos(top,top1,sent1,pp);
			    			}
			    		}
			    		
				    	//=========Store top 5 negative words and sentences======
			    		if(checkWord(bottom, str)==0)
			    		{
			    			if(nn < 5 && m<0.0)
			    			{
					   			bottom[nn]=str;
					   			bottom1[nn]=m;
				    			sent2[nn]=s;
				    			nn++;	
			    			}
			    			else
			    			{
			    				sortneg(bottom,bottom1,sent2,nn);
			    				if(bottom1[0]>m)
			    				{
				    				bottom[0]=str;
						   			bottom1[0]=m;
						   			sent2[0]=s;
				    			
			    				}
			    				sortneg(bottom,bottom1,sent2,nn);
			    			}
			    		}
			    	}	
		    		score[n]=m;
	    			n++;
	    		}
				
			    else
			    {
			    	score[n]=0;
			    	n++;
			    }
			}
			
			//feature sorting
			String temp;
			double temp_strength=0;
			for(int p=0;p<length_of_features;p++)
			for(int q=0;q<length_of_features;q++)
			{
				if(featureStrength[p]<featureStrength[q])
				{
					temp=features[p];
					features[p]=features[q];
					features[q]=temp;
					temp_strength=featureStrength[p];
					featureStrength[p]=featureStrength[q];
					featureStrength[q]=temp_strength;
				}
			}
			
			for(int q=0;q<length_of_features;q++)
			{
				bw3.write("\r\n"+features[q]+"\r\t"+featureStrength[q]);
			}
			

			//Print words and its scores
			System.out.print(wordsList+" ");
			System.out.print("\n[");
			for(j=0;j<score.length;j++)
    		{
				System.out.print(score[j]);
				if(j<score.length-1)
				{
					System.out.print(" , ");
				}
    		}
			System.out.print("]");
			
			
			Integer[] arr1=new Integer[score.length];
			
			
			//Store input pattern in terms of 0,1 and -1
			if(score.length > 0)
			{	
				int counter=arr1.length;
				l=0;

				Integer[] array2=new Integer[3];
				
				for(j=0;j<score.length;j++)
	    		{
					if(score[j]>0.0)
					{
						arr1[j]=1;
					}
					else if(score[j]<0.0)
					{
						arr1[j]=-1;
					}
					else
					{
						arr1[j]=0;
					}
	    		}
				
				for(int e=0;e<array2.length;e++)
				{
					array2[e]=null;
				}
						
				System.out.println("\n Input pattern:");
				
				for(int e=0;e<arr1.length;e++)
				{
					System.out.println(arr1[e]);
				}
				
				//=====================Encoding mechanism==============================
				
				int remainder;
				Integer[] tempp1=new Integer[200];
				int sizeoftemp=arr1.length,tempsum=0;
				
				if(arr1.length >3)
				{
					while(sizeoftemp>3)
					{
						sizeoftemp=counter/3;
						remainder=counter%3;
						
						if(remainder>0)
						{
							sizeoftemp++;
						}
						for(i=0,j=0;i<sizeoftemp;i++,j=j+3)
						{	
							if(j+1<=score.length-1 && j+2<=score.length-1)
							{
								tempsum=arr1[j]+arr1[j+1]+arr1[j+2];
							}
							else
							{
								if(j+1>score.length-1)
								{
								    tempsum=arr1[j]+0;
								}
								else 
								{
								    tempsum=arr1[j]+arr1[j+1]+0;		    
								}
							}	
								
							if(tempsum>0)
							{
								tempp1[i]=1;
							}
							else if(tempsum==0)
							{
								tempp1[i]=0;
							}
							else
							{
								tempp1[i]=-1;
							}
								
						}
						counter=sizeoftemp;
					}
				}
				else
				{
					for(i=0;i<arr1.length;i++)
					{
						tempp1[i]=arr1[i];
					}
				}
					
				i=0;
				int y1=0,y2=0;
				if(tempp1[i+1]==null)
				{
					tempp1[i+1]=0;
				}
				if(tempp1[i+2]==null)
				{
					tempp1[i+2]=0;
				}
				
				System.out.println("\n After encoding the input pattern:");
				
				for( j=0;j<3;j++)
				{
					System.out.println(tempp1[j]);
				}
					
				//======================Neural Network==================================
				
				//if given input file is training dataset for positive nn
				if(flag==1)
				{
					Neural.good(tempp1,flag);
				}
				//if given input file is training dataset for negative nn
				else if(flag==-1)
				{
					Neural.bad(tempp1,flag);
				}
				//if given input file is testing dataset
				else if(flag==0)
				{
					y1=Neural.good(tempp1,flag);
					y2=Neural.bad(tempp1,flag);
					System.out.println("Positive score is:"+y1);
					System.out.println("Negative score is:"+y2);
					if(y1==0 && y2==0)
					{
						neutral++;
			//code changes begin
				        bwtemp.flush();
				        content="n--"+s;
						bwtemp.write(content+"\r\n"+"\r\n");
						count++;
			//code changes end
					}
					else if(y1>0)
					{
						pos++;
			//code changes begin
				        bwtemp.flush();
				        content="g--"+s;
						bwtemp.write(content+"\r\n"+"\r\n");
						count++;
			//code changes end						
					}
					else if(y2<0)
					{
						neg++;
			//code changes begin
				        bwtemp.flush();
				        content="b--"+s;
						bwtemp.write(content+"\r\n"+"\r\n");
						count++;
			//code changes end
					}
					else
						nocondition++;
						
				}
			}
			
		}
//code changes start			
		bwtemp.close();

		System.out.println("Done"+count);
		System.out.println("POS"+pos);
		System.out.println("NEG"+neg);
		System.out.println("NEU"+neutral);
		System.out.println("Features count:-"+length_of_features);
//code changes end	
	    
	    bw3.close();
		fw3.close();
    
	    System.out.println("\n=======================================");
	    
	    float arr[]=new float[3];
	    
	    //for testing dataset
	    if(flag==0)
	    {
	    	try {
	    	//String fileName="C:/Project/words.txt";
	    	String fileName="abc/words.txt";
			File file = new File(fileName);
			
			//Write into file
			//if (!file.exists()) 
			//{
			//	file.createNewFile();
			
				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
   
				System.out.println("\n Top 5 positive words:");
				for(i=0;i<5;i++)
				{	
					j=i+1;
					System.out.println("\n"+j+"\t"+top[i]+"\t"+top1[i]);
					bw.write(top[i]+"\t"+top1[i]);
					bw.write("\n");
				}
   
				System.out.println("\n Top 5 negative words:");
				for(i=0;i<5;i++)
				{
					j=i+1;
					System.out.println("\n"+j+"\t"+bottom[i]+"\t"+bottom1[i]);
					bw.write(bottom[i]+"\t"+bottom1[i]);
					bw.write("\n");
				}
				bw.close();
				fw.close();
					    
				
				//String fileName1="C:/Project/sentpos.txt";
				String fileName1="abc/sentpos.txt";
				File file1 = new File(fileName1);
				FileWriter fw1 = new FileWriter(file1.getAbsoluteFile());
				BufferedWriter bw1 = new BufferedWriter(fw1);
				System.out.println("\n Top 5 positive sentences:");
				for(i=0;i<5;i++)
				{
					j=i+1;
					System.out.println(j+"\t"+sent1[i]);
					bw1.write(" "+j+". ");
					bw1.write(sent1[i]);
					bw1.write("\n");
				}
				bw1.close();
				fw1.close();	
					
				
				//String fileName2="C:/Project/sentneg.txt";
				String fileName2="abc/sentneg.txt";
				File file2 = new File(fileName2);
				FileWriter fw2 = new FileWriter(file2.getAbsoluteFile());
				BufferedWriter bw2 = new BufferedWriter(fw2);
				System.out.println("\n Top 5 negative sentences:");
				for(i=0;i<5;i++)
				{
					j=i+1;
					System.out.println(j+"\t"+sent2[i]);
					bw2.write(" "+j+". ");
					bw2.write(sent2[i]);
					bw2.write("\n");
				}
				
				bw2.close();
				fw2.close();
				
				//String fileName4="C:/Project/noun-score.txt";
				String fileName4="abc/noun-score.txt";
				File file4 = new File(fileName4);
				FileWriter fw4 = new FileWriter(file4.getAbsoluteFile());
				BufferedWriter bw4 = new BufferedWriter(fw4);
				for(i=0;i<length_of_features;i++)
				{
					bw4.write("\r\n"+featureStrength[i]);
				}
				bw4.close();
				fw4.close();
				
				//String fileName5="C:/Project/noun1.txt";
				String fileName5="abc/noun1.txt";
				File file5 = new File(fileName5);
				FileWriter fw5 = new FileWriter(file5.getAbsoluteFile());
				BufferedWriter bw5 = new BufferedWriter(fw5);
				for(i=0;i<length_of_features;i++)
				{
					bw5.write("\n"+features[i]);
				}
				bw5.close();
				fw5.close();
					
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    
		    //=============Calculate % of positive, negative and neutral sentences==========
		    
		    float total=pos+neg+neutral;
		    float ppos=(pos*100)/total;
		    float pneg=(neg*100)/total;
		    float pneutral=(neutral*100)/total;
		    
		    arr[0]=ppos;
		    arr[1]=pneg;
		    arr[2]=pneutral;
		    
		    System.out.println("\n Total: "+total);
		    System.out.println("\n Percentage of positive sentence:"+ppos+" "+pos);
		    System.out.println("\n Percentage of negative sentence:"+pneg+" "+neg);
		    System.out.println("\n Percentage of neutral sentence:"+pneutral+" "+neutral);
		    
		    
		    try {
		    //write suggestions into a file
		    //String fileName5="C:/Project/suggestions.txt";
		    String fileName5="abc/suggestions.txt";
			File file5 = new File(fileName5);
			FileWriter fw5 = new FileWriter(file5.getAbsoluteFile());
			BufferedWriter bw5 = new BufferedWriter(fw5);
			float re=ppos+pneg;
			
			if(re<50)
				bw5.write("\n\t Your social footprint needs to improve. The chatter about the product/brand/entity you searched for suggests that people not talking about you much.");
			if(pneg>2)
				bw5.write("\n\t Your overall perception is on the decline!!!. Please look further down below on what you need to do.");
			if(featureStrength[0]>0)
				bw5.write("\n\t People like "+features[0]+" the most, please dont change it.");
			if(featureStrength[1]>0)
				bw5.write("\n\t People think "+features[1]+" is the second best thing about your product/brand. Dont change it either.");
			if(length_of_features>1)
			{
			if (featureStrength[length_of_features-1]<0)
				bw5.write("\n\t People just abhor "+features[length_of_features-1]+" as a feature of ur product. Need to change it fast. Dont worry, we are here to help, look at the databse we have collected to know what and how u need to change. Pls drop us a mail and we will provide you all the chatter we collected for you.");
			if (featureStrength[length_of_features-2]<0)
				bw5.write("\n\t In our research, the data we have found suggests that "+features[length_of_features-2]+" is the second most bad thing @ ur product/brand."); 
			bw5.write("\n\t Please have a look at the data insights we collected in form of above comments to know what u can do @ it!! \n We suggest strongly that you use our software to perform periodical analysis for your product/brand/entity.\n This is will help you understand how you have improved on above suggestions.");
			}	
			bw5.close();
			fw5.close();
		    } catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    
	    return arr;
    }

    //to sort top 5 positive words
	public void sortpos(String[] top,double top1[],String[] sent1, int i)
    {
    	double temp;
    	String temp1,temp2;
    	int j;
    	
	    for(j=0;j<i;j++)
	    {
	   		for(int f=j+1;f<i;f++)
	  		{
	    		if(top1[j]>top1[f])
	    		{
	    			temp=top1[j];
	   				top1[j]=top1[f];
	   				top1[f]=temp;
	    				
	    			temp1=top[j];
	    			top[j]=top[f];
	    			top[f]=temp1;
	    				
	   				temp2=sent1[j];
	   				sent1[j]=sent1[f];
	   				sent1[f]=temp2;
	   			}	
	   		}
	   	}
    }
    
	//to sort top 5 negative words
    public void sortneg(String[] bottom,double bottom1[],String[] sent2, int i)
    {
    	double temp;
    	String temp1,temp2;
    	int j;
    	
	    for(j=0;j<i;j++)
		{
			for(int f=j+1;f<i;f++)
			{	
				if(bottom1[j]<bottom1[f])
				{
					temp=bottom1[j];
					bottom1[j]=bottom1[f];
					bottom1[f]=temp;
						
					temp1=bottom[j];
					bottom[j]=bottom[f];
					bottom[f]=temp1;
						
					temp2=sent2[j];
					sent2[j]=sent2[f];
					sent2[f]=temp2;
				}
			}	
		}
    }
	
    public int checkWord(String[] top,String str)
    {
    	for(int i=0;i<5;i++)
    	{
    		if(str.equalsIgnoreCase(top[i]))
    			return 1;	
    	}
		return 0;	
    }
    
}