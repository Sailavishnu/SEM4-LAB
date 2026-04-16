import java.util.*;

public class NQueens {

    static int[] x;
    static int n;

    static boolean place(int k, int i) {
        for (int j = 1; j < k; j++) {
            if (x[j] == i || Math.abs(x[j] - i) == Math.abs(j - k)) {
                return false;
            }
        }
        return true;
    }

    static void nQueens(int k) {
        for (int i = 1; i <= n; i++) {
            if (place(k, i)) {
                x[k] = i;

                if (k == n) {
                    printSolution();
                } else {
                    nQueens(k + 1);
                }
            }
        }
    }

    static void printSolution() {
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                if (x[i] == j) {
                    System.out.print("Q ");
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of queens: ");
        n = sc.nextInt();

        x = new int[n + 1];

        System.out.println("Solutions:");
        nQueens(1);
    }
}