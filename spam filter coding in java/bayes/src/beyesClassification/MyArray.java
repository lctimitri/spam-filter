package beyesClassification;

import javax.swing.table.TableColumn;


//a class can add two array or every element divide by a number directly
public class MyArray {

	private int[] array;
	
	
	public int[] getArray() {
		return array;
	}

	//set array[n] to be 1
	public void SetPos(int n)
	{
		array[n]=1;
	}

	public MyArray(int n)
	{
		array=new int[n];
	}
	
	//initialize the array
	public void InitArray(int v)
	{
		for(int i=0;i<array.length;i++)
			array[i]=v;
	}
	
	// add the element in two arrays respectly
	public void Add(MyArray arr2)
	{
		for(int i=0;i<array.length;i++)
			array[i]+=arr2.getArray()[i];
	}
	
	// return one number of those
	public int NumOfOne()
	{
		int n=0;
		for(int i=0;i<array.length;i++)
		{
			if(array[i]==1)
				n++;
		}
		return n;
	}
	
	

}
