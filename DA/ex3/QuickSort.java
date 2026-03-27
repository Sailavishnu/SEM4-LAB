import java.util.Scanner;

public class QuickSort {
    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        System.out.print("Enter the number of elements:");
        int n = in.nextInt();
        int A[] = new int[n];

        for (int i = 0; i < n; i++) {
            System.out.print("enter element " + (i + 1) + " :");
            A[i] = in.nextInt();
        }
        System.out.println("Before Sorting:");
        for (int i : A) {
            System.out.print(i + " ");
        }

        QuickSort(A, 0, A.length - 1);

        System.out.println("\nAfter Sorting:");
        for (int i : A) {
            System.out.print(i + " ");
        }

    }

    private static void QuickSort(int[] array, int low, int high) {
        if (low < high) {
            int s = partition(array, low, high);
            QuickSort(array, low, s - 1);
            QuickSort(array, s + 1, high);
        }
    }

    private static int partition(int[] A, int low, int high) {
        int pivot = A[low];
        int i = low + 1;
        int j = high;

        while (true) {
            while (i <= high && pivot >= A[i]) {
                i++;
            }
            while (pivot < A[j]) {
                j--;
            }
            if (i < j) {
                int t = A[i];
                A[i] = A[j];
                A[j] = t;
            } else {
                int t = A[low];
                A[low] = A[j];
                A[j] = t;
                return j;
            }
        }

    }
}
