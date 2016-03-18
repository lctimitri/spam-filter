package beyesClassification;

import java.io.*;
import java.util.ArrayList;
import java.util.*;
import java.util.Scanner;


public class MailClassify {

	static double[] hamvect;//store log（P（Wi|ham）*p（ham））
	static double[] spamvect;//store log（P（Wi|spam）*p（spam））
	static double pSpam;//the spam probability in all emails
	static ArrayList<String> wordList;//all words in training set
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MailClassify mc=new MailClassify();
		try {
			//training algorithm
			System.out.println("=====training starts=====");
			mc.Train();
			System.out.println("=====training complete=====");
			System.out.println("=====test starts=====");
			mc.Test();
			System.out.println("=====test complete=====");
			//read one line, let console not quit
			Scanner sc=new Scanner(System.in);
			String ts=sc.nextLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	//training process
	private void Train() throws IOException{
		int hamNum=ReadFiles.GetFileNum("C:/Users/lctimitri/Desktop/MailClassify/dataset/training set/goodemail");//number of hams in training
		System.out.println("good emails for training "+hamNum);
		
		int spamNum=ReadFiles.GetFileNum("C:/Users/lctimitri/Desktop/MailClassify/dataset/training set/spam");//number of spams in training
		System.out.println("spams for training "+spamNum);
		
		wordList=ReadFiles.GetWordsList("C:/Users/lctimitri/Desktop/MailClassify/dataset/training set");
		int wordNum=wordList.size();//total words number
		
		int mailNum=hamNum+spamNum;//total email number
		pSpam=spamNum/(mailNum+0.0);//the spam probability in all emails
		
		
		MyArray hamArray=new MyArray(wordNum);// counter to record all word in hams
		hamArray.InitArray(1);
		
		MyArray spamArray=new MyArray(wordNum);// counter to record all word in spams
		spamArray.InitArray(1);
		
		System.out.println(wordNum);
		
		int hamWords=2,spamWords=2;// counters for word number in hams and spams respectly
		
		// get the word vector in training emails
		ArrayList<MyArray> trainMatrix=ReadFiles.GetMatrix("C:/Users/lctimitri/Desktop/MailClassify/dataset/training set", wordList);
		// process the hams in trainMatrix first half
		for(int i=0;i<hamNum;i++)
		{
			hamArray.Add(trainMatrix.get(i));// the sum of vectors
			hamWords+=trainMatrix.get(i).NumOfOne();//how many kinds of different words in this ham
		}
		
		// process the spams in trainMatrix second half
		for(int i=hamNum;i<mailNum;i++)
		{
			spamArray.Add(trainMatrix.get(i));//the sum of vectors
			spamWords+=trainMatrix.get(i).NumOfOne();//how many kinds of different words in this ham
		}
		
		hamvect=new double[wordNum];//store log（P（Wi|ham）*p（ham））
		spamvect=new double[wordNum];//store log（P（Wi|spam）*p（spam））
		
		//compute log（P（Wi|ham）*p（ham））和log（P（Wi|spam）*p（spam））
		// same as p（w1|ham）*p（w2|ham）*·····p（wn|ham）
		for(int i=0;i<wordNum;i++)
		{
			hamvect[i]=Math.log(hamArray.getArray()[i]/(hamWords+0.0));
			spamvect[i]=Math.log(spamArray.getArray()[i]/(spamWords+0.0));
		}
		
/*		for(int i=0;i<wordNum;i++)
		{
			System.out.print(hamvect[i]+"  ");
			if(i%20==0)
				System.out.println();
				
		}*/
	}
	
	//test process
	public void Test() throws IOException {
		File file = new File("C:/Users/lctimitri/Desktop/bayes result.txt");
		
		int tHamNum=ReadFiles.GetFileNum("C:/Users/lctimitri/Desktop/MailClassify/dataset/test set/goodemail");//the number of hams in test
		System.out.println("good emails for test "+tHamNum);
		
		int tSpamNum=ReadFiles.GetFileNum("C:/Users/lctimitri/Desktop/MailClassify/dataset/test set/spam");//the number of spams in test
		System.out.println("spams for test "+tSpamNum);
		
		int tMailNum=tHamNum+tSpamNum;// total emails in test
		
		
		int tRightNum=0;//correct decision emails number

		//test set's data
		ArrayList<MyArray> testMatrix=ReadFiles.GetMatrix("C:/Users/lctimitri/Desktop/MailClassify/dataset/test set", wordList);
		//System.out.println(testMatrix.size());
		// get the file path in order 
		ArrayList<String> fileName=ReadFiles.GetFileName("C:/Users/lctimitri/Desktop/MailClassify/dataset/test set");
		//System.out.println(fileName.size());
		try{
			FileWriter fw = new FileWriter(file);
			BufferedWriter bufw = new BufferedWriter(fw);
			bufw.write("=====The bayesian result=====");
			bufw.newLine();
			
		for(int i=0;i<tMailNum;i++)
		{
			double tPHam=0;// the probability to be ham
			double tPSpam=0;// the probability to be spam
			
			//compute testMatrix.get(i)*hamvect
			for(int j=0;j<wordList.size();j++)
			{
				tPHam+=testMatrix.get(i).getArray()[j]*hamvect[j];
				tPSpam+=testMatrix.get(i).getArray()[j]*spamvect[j];
			}
			//use log
			tPHam+=Math.log(1-pSpam);
			tPSpam+=Math.log(pSpam);
			//System.out.println("tPHam "+tPHam);
			//System.out.println("tPSpam "+tPSpam);
			
			// if the probability to be ham > the probability to be spam, it's spam; else, it's ham
			if(tPHam>tPSpam)
			{
				System.out.println("good emails:  "+fileName.get(i));
				
				bufw.write("good emails: "+fileName.get(i));
				bufw.newLine();
				
				if(i<tHamNum)
					tRightNum++;
			}
			else
			{
				System.out.println("spams:  "+fileName.get(i));
				bufw.write("spams: "+fileName.get(i));
				bufw.newLine();
				
				if(i>=tHamNum)
					tRightNum++;
			}
			
		}
		
		bufw.newLine();
		bufw.write("The well-judged email number is "+tRightNum);
		bufw.newLine();
		
		bufw.write("The total email for test is: "+Integer.toString(tMailNum));
		bufw.newLine();
		
		bufw.write("classification accuracy="+tRightNum/(tHamNum+tSpamNum+0.0));
		bufw.newLine();
		
		bufw.close();
		fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("The well-judged email number is "+tRightNum);
		System.out.println("The total email number for test is "+(tHamNum+tSpamNum));
		System.out.println("classification accuracy="+tRightNum/(tHamNum+tSpamNum+0.0));

	}

}
