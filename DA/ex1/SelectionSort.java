import java.util.Scanner;

public class SelectionSort {
    public static void main(String[] args) {
    int i,j,min,temp;
    Scanner in = new Scanner(System.in);
    System.out.print("Enter the number of elements:");
    int n= in.nextInt();
    int A[] = new int[n];
	for (i=0;i<n;i++){
        System.out.print("enter element "+(i+1)+" :");
        A[i]=in.nextInt();
    }

    System.out.println("Before Swapping  ");
    for (i=0;i<n;i++){
        System.out.println(A[i]);
    }

	for(i=0;i<n-1;i++){
       min =i;
	   for(j=i+1;j<n;j++){
	      if(A[j]>A[min]){
            min=j;
	    }
        temp=A[j];
	    A[j]=A[min];
	    A[min]=temp;
        }
        
	}
	
    System.out.println("After Swapping  ");
    for (i=0;i<n;i++){
        System.out.println(A[i]);
    }
    }
}
