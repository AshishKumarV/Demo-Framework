package tv.accedo.TVAndroid.Test;

import java.io.BufferedReader;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class Practice {  
		 public void index(int arr[],int size){
		 
		 for(int i=0;i<size;i++){
			 int leftSum=0;
		       for (int j=0;j<=i;j++){
		           leftSum= leftSum+ arr[j];
		       }
		       int rightSum =0;
		       for (int j=i;j<size;j++){
		     rightSum= rightSum+arr[j];
		       }
		       
		       if (leftSum==rightSum){
		            System.out.println("Equilibrium index is "+ i);
		       }
		 	}
		 }
		 
		 public void prime(int n) {
			 for(int i=2;i<=n/2+1;i++) {
				 if(n%i==0) {
					 System.out.println("Given no is not prime :"+n );
					 break;
				 }else {
					 System.out.println(n+ "is a prime no.");
					 break;
				 }
			 }
			 
		 }
		 
		 
		 public void HashFunction() {
			 Hashtable<Integer, String> table= new Hashtable<>();
			 table.put(8, "Ashu");
			 table.put(3, "Ashish");
			 table.put(5, "Verma");
			 table.put(6, "kumar");
			 
			 Enumeration<String> e =table.elements();
			 while(e.hasMoreElements()) {
			 System.out.println(e.nextElement());
			 }
			 
			 Set tab =table.entrySet();
			 Iterator itr =tab.iterator();
			 while(itr.hasNext())
			 {
				 System.out.println(itr.next());
			 }
		 }
		 
		 public void fibonacci(int n) {
			 int n1=0, n2=1,num=0;
			 System.out.print("fibonacci series is :"+n1+","+n2);
			 for(;;) {
				 num=n1+n2;
				 n1=n2;
				 n2=num;
				 if (num>=n) {
					 break;
				 }
				 System.out.print(","+num);
			 }
		 }
		 
		 public void armstrong(int num) {
			 int oriNo=num;
			 double count =0, result =0;
			 for(;;) {
				 count+=1;
				 oriNo=oriNo/10;
				 if(oriNo==0) {
					 break;
				 }
			 }
			 oriNo=num;
			 for(;;) {
				 double d= oriNo%10;
				 result= result +(Math.pow(d, count));
				 oriNo =oriNo/10;
				 if(oriNo==0) {
					 break;
				 }
			 }
			 
			 if(result==num) {
				 System.out.println("Given no is an armstrong  no.");
			 }else {
				 System.out.println("Given no is not an armstrong no.");
			 }
				 
		 }
		 
		 
		 public void revereStr() {
			 System.out.println("Enter a string to reverse:");
//			 BufferedReader br= new BufferedReader(System.in);
			 StringBuilder br= new StringBuilder();
			 String str= new Scanner(System.in).nextLine();
			 char[] a=str.toCharArray();
			 for(int i= a.length-1;i>=0;i--) {
				 br.append(a[i]);
			 }
			 System.out.print(br.toString());
		 }
		 
		 public void largest(int a[]) {
			 int largest= a[0];
			 for(int i=0;i<a.length;i++) {
				 if(a[i]> largest) {
					 largest=a[i];
				 }
			 }
			 System.out.println("largest no is:"+largest);
		 }
		 
		 
		 public void printMissingNumber(int[] numbers, int count) {
		        int missingCount = count - numbers.length;
		        BitSet bitSet = new BitSet(count);
		        	
		        for (int number : numbers) {
		            bitSet.set(number - 1);
		        }
		 
		        System.out.printf("Missing numbers in integer array %s, with total number %d is %n", Arrays.toString(numbers), count);
		        int lastMissingIndex = 0;

		        for (int i = 0; i < missingCount; i++) {
		            lastMissingIndex = bitSet.nextClearBit(lastMissingIndex);
		            System.out.println(++lastMissingIndex);
		        }
		 
		    }
		 
		 
		 public static String reverseRecursively(String str) {

		        //base case to handle one char string and empty string
		        if (str.length() < 2) {
		            return str;
		        }

		        return reverseRecursively(str.substring(1)) + str.charAt(0);

		    }


		 public static int removeDuplicates(int res[], int a){
				
				if(a == 0 || a ==1 ){
					return a;
				}
				
				// to store the index of next unique element
				int j= 0 ;
				for(int i= 0; i <a-1; i++ )
					// if the current element is not equal to next element
					// then store the next element
					if(res[i] !=res [i+1])
						res[j++]= res [i];
				
				res[j++] = res [a-1];
				
				return j;
				
					
				}

		 
		 public void checkNumber(int n) {
			 
			 int result=n;
			 for(int i=0;i<n;i++)
			 if(result%2==0) {
				 result =result/2;
				
			 }
			 if(result==1)
			 {
				 System.out.println("Given no. is power of 2");
				
			 }else {
				 System.out.println("Given no is not power of 2");
			 }
		 }

		 public void check() {
			 Integer a=10000;
	    	 Integer b= 10000;
	    	 
	    	 
//	    	 System.out.println("c is"+c+"d is:"+d);
	    	 if (a==b) {
	    		 System.out.println("True");
	    	 }
	    	 else {System.out.println("False");}
		 }
		 
		     public static void main(String []args){
		    	 Practice eq =new Practice();
		         
//		         int [] arr ={5, 2, 3, 1, 6};
//		         int size=arr.length;
//		         eq.index(arr,size);
		    	
//		    	 @@@@@@@@@@@@ 
//		    	 eq.prime(7);
		    	 
//		    	 eq.HashFunction();
//		        eq.fibonacci(100);
//		    	 eq.armstrong(9474);
		    	 
//		    	 eq.revereStr();
//		    	 int arr[]={5,4,10,2,1,6};
//		    	 eq.largest(arr);
		    	 
//		    	 eq.printMissingNumber(new int[] {1,2,3,4,6,9,8}, 10);
		    	 
//		    	 String reverseStr = eq.reverseRecursively("MY Name is");
//		         System.out.println("Reverse String in Java using Recursion: " + reverseStr);

//		    	 eq.removeDuplicates(new int[] {1,1,4,9,9,10,10,71,81},9 );
		    	 
		    	 
		    	 eq.checkNumber(88);
		     }
	}  