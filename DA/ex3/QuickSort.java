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

    private static void QuickSort(int[] array, int left, int right) {
        if (left < right) {
            int s = partition(array, left, right);
            QuickSort(array, left, s - 1);
            QuickSort(array, s + 1, right);
        }
    }

    private static int partition(int[] A, int left, int right) {
        int pivot = A[left];
        int i = left + 1;
        int j = right;

        while (true) {
            while (i <= right && pivot >= A[i]) {
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
                int t = A[left];
                A[left] = A[j];
                A[j] = t;
                return j;
            }
        }

    }
}
