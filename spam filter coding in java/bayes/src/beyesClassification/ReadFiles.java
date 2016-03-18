package beyesClassification;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;

import java.util.regex.Pattern;  
import java.util.regex.Matcher;  

public class ReadFiles {

	// load the file path, return number of files, to compute the number of spams and hams in training set
	//eg：".\dataset\training set\goodemail"
	public static int GetFileNum(String pathName)
	{
		File file=new File(pathName);
		File[] nextFiles=file.listFiles();
		return nextFiles.length;
	}
	
	public static ArrayList<String> GetFileName(String pathName) throws IOException
	{
		File fileHam=new File(pathName+"\\goodemail");
		File fileSpam=new File(pathName+"\\spam");
		
		File[] hamFiles=fileHam.listFiles();
		File[] spamFiles=fileSpam.listFiles();
		
		ArrayList<String> fileName=new ArrayList<String>();
		for(int i=0;i<hamFiles.length;i++)
		{
			fileName.add(hamFiles[i].getPath());
		}
		
		for(int i=0;i<spamFiles.length;i++)
		{
			fileName.add(spamFiles[i].getPath());
		}
		
		return fileName;
	}
	
	
	// return the words in training set
	//eg: ".\dataset\training set"
	public static ArrayList<String> GetWordsList(String pathName) throws IOException
	{
		File fileHam=new File(pathName+"\\goodemail");
		File fileSpam=new File(pathName+"\\spam");
		
		File[] hamFiles=fileHam.listFiles();
		File[] spamFiles=fileSpam.listFiles();
		
		HashSet<String> set=new HashSet<String>();
		for(int i=0;i<hamFiles.length;i++)
		{
			BufferedReader in=new BufferedReader(new InputStreamReader(new FileInputStream(hamFiles[i])));
			String s=null;
			while((s=in.readLine())!=null)
			{
				
				String sMatch = "\\d+.\\d+|\\w+|\\$";
		        Pattern  pattern=Pattern.compile(sMatch);  
		        Matcher  ma=pattern.matcher(s);  
		   
		        while(ma.find()){  
		        	set.add(ma.group().toLowerCase());
		        } 
		        
			}
			in.close();
		}
		
		for(int i=0;i<spamFiles.length;i++)
		{
			BufferedReader in=new BufferedReader(new InputStreamReader(new FileInputStream(spamFiles[i])));
			String s=null;
			while((s=in.readLine())!=null)
			{
				
				String sMatch = "\\d+.\\d+|\\w+|\\$";
		        Pattern  pattern=Pattern.compile(sMatch);  
		        Matcher  ma=pattern.matcher(s);  
		   
		        while(ma.find()){  
		        	set.add(ma.group().toLowerCase());
		        } 
		        
			}
			in.close();
		}
		
		ArrayList<String> wordList=new ArrayList<String>(set);
		return wordList;
		
	}
	
	// return array to display if every word exists in emails in training set
	public static ArrayList<MyArray> GetMatrix(String pathName,ArrayList<String> wordList) throws IOException
	{
		ArrayList<MyArray> trainMatrix=new ArrayList<MyArray>();
		
		File fileHam=new File(pathName+"\\goodemail");
		File fileSpam=new File(pathName+"\\spam");
		
		File[] hamFiles=fileHam.listFiles();
		File[] spamFiles=fileSpam.listFiles();
		
		// firstly, add hams
		for(int i=0;i<hamFiles.length;i++)
		{
			BufferedReader in=new BufferedReader(new InputStreamReader(new FileInputStream(hamFiles[i])));
			MyArray wordArray=new MyArray(wordList.size());// if word appears at the respective position, set 1; else, set 0.
			wordArray.InitArray(0);
			String s=null;
			while((s=in.readLine())!=null)
			{
				
				String sMatch = "\\d+.\\d+|\\w+|\\$";
		        Pattern  pattern=Pattern.compile(sMatch);  
		        Matcher  ma=pattern.matcher(s);  
		        
		        while(ma.find()){  
		        	int pos=wordList.indexOf(ma.group().toLowerCase());
		        	if(pos!=-1)
		        		wordArray.SetPos(pos);
		        	
		        } 
		        
			}
			trainMatrix.add(wordArray);
			in.close();
		}
		
		//then, add spams
		for(int i=0;i<spamFiles.length;i++)
		{
			BufferedReader in=new BufferedReader(new InputStreamReader(new FileInputStream(spamFiles[i])));
			MyArray wordArray=new MyArray(wordList.size());//if word appears at the respective position, set 1; else, set 0.
			wordArray.InitArray(0);
			String s=null;
			while((s=in.readLine())!=null)
			{
				
				String sMatch = "\\d+.\\d+|\\w+|\\$";
		        Pattern  pattern=Pattern.compile(sMatch);  
		        Matcher  ma=pattern.matcher(s);  
		        
		        while(ma.find()){  
		        	int pos=wordList.indexOf(ma.group().toLowerCase());
		        	if(pos!=-1)
		        		wordArray.SetPos(pos);
		        	
		        } 
		        
			}
			trainMatrix.add(wordArray);
			in.close();
		}
		
		return trainMatrix;
		
	}
}
