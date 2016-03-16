package android;

import java.util.Arrays;

public class Misto {
	
	
	public static void main(String args[])
	{
		
		 int n=30000;
		 int v[]=new int[n];
		for(int i=0;i<n;i++)
		{
			v[i]=(int) (Math.random()*256);
		}
		long b=System.currentTimeMillis();

//	    Arrays.sort(v);
		long sum=0;
		for(int j=0;j<=100000;j++){
		for(int i=0;i<n;i++)
		{
			if(v[i]>=120)
			{
				sum+=v[i];
			}
		}
		}
		System.out.println(System.currentTimeMillis()-b);
		
		
	}

}
