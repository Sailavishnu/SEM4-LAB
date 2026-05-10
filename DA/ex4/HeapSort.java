import java.util.Scanner;

class Heap {

    void percolateDown(int arr[], int size, int root) {

        int smallest = root;

        int left = 2 * root + 1;
        int right = 2 * root + 2;

        if (left < size && arr[left] < arr[smallest]) {
            smallest = left;
        }

        if (right < size && arr[right] < arr[smallest]) {
            smallest = right;
        }

        if (smallest != root) {

            int temp = arr[root];
            arr[root] = arr[smallest];
            arr[smallest] = temp;

            percolateDown(arr, size, smallest);
        }
    }

    void buildHeap(int arr[], int n) {

        for (int i = n / 2 - 1; i >= 0; i--) {
            percolateDown(arr, n, i);
        }
    }

    int removeMin(int arr[], int size) {

        int min = arr[0];

        arr[0] = arr[size - 1];

        percolateDown(arr, size - 1, 0);

        return min;
    }

    void heapSort(int arr[], int n) {

        buildHeap(arr, n);

        int sorted[] = new int[n];

        int size = n;

        for (int i = 0; i < n; i++) {

            sorted[i] = removeMin(arr, size);

            size--;
        }

        System.out.println("Sorted Array:");

        for (int i = 0; i < n; i++) {
            System.out.print(sorted[i] + " ");
        }
    }
}

public class HeapSort {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of elements: ");

        int n = sc.nextInt();

        int arr[] = new int[n];

        System.out.println("Enter elements:");

        for (int i = 0; i < n; i++) {
            arr[i] = sc.nextInt();
        }

        Heap h = new Heap();

        h.heapSort(arr, n);

        sc.close();
    }
}