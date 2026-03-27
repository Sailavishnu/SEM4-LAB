import java.util.Scanner;

public class BubbleSort {
    public static void main(String[] args) {
    int i,j,temp;
    Scanner in = new Scanner(System.in);
    System.out.print("Enter the number of elements:");
    int n= in.nextInt();
    int A[] = new int[n];
	for (i=0;i<n;i++){
        System.out.print("enter element "+(i+1)+" :");
        A[i]=in.nextInt();
    }

    System.out.println("Before Sorting  ");
    for (i=0;i<n;i++){
        System.out.println(A[i]);
    }

	for(i=0;i<=n-2;i++){
	   for(j=0;j<=n-2-i;j++){
	      if(A[j]>A[j+1]){
		    temp=A[j];
		    A[j]=A[j+1];
		    A[j+1]=temp;
	      }
        }
	}
	
    System.out.println("After Sorting  ");
    for (i=0;i<n;i++){
        System.out.println(A[i]);
    }
    }
}
