import java.util.Scanner;

public class HeapSort {
    static int[] arr;
    static int size;

    static void percolateDown(int root) {
        int smallest = root;
        int left = 2 * root + 1;
        int right = 2 * root + 2;
        if (left < size && arr[left] < arr[smallest])
            smallest = left;
        if (right < size && arr[right] < arr[smallest])
            smallest = right;
        if (smallest != root) {
            int temp = arr[root];
            arr[root] = arr[smallest];
            arr[smallest] = temp;
            percolateDown(smallest);
        }
    }

    static void buildHeap() {
        for (int i = (size / 2) - 1; i >= 0; i--)
            percolateDown(i);
    }

    static int removeMin() {
        int min = arr[0];
        arr[0] = arr[size - 1];
        size--;
        percolateDown(0);
        return min;
    }

    static void heapSort(int n) {
        int[] sorted = new int[n];
        size = n;
        buildHeap();

        for (int i = 0; i < n; i++)
            sorted[i] = removeMin();

        for (int i = 0; i < n; i++)
            arr[i] = sorted[i];
    }

    static void printArray(int n) {
        for (int i = 0; i < n; i++)
            System.out.print(arr[i] + " ");
        System.out.println();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of elements: ");
        int n = sc.nextInt();

        arr = new int[n];
        System.out.print("Enter " + n + " elements: ");
        for (int i = 0; i < n; i++)
            arr[i] = sc.nextInt();

        System.out.print("Before Sorting: ");
        printArray(n);

        size = n;
        heapSort(n);

        System.out.print("After Sorting:  ");
        printArray(n);

        sc.close();
    }
}
