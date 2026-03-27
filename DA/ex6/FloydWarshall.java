import java.util.Scanner;

public class FloydWarshall {

    static final int INF = 99999;

    static void printMatrix(int[][] matrix, int n) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == INF)
                    System.out.print("INF ");
                else
                    System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of vertices: ");
        int n = sc.nextInt();

        int[][] D = new int[n][n];

        System.out.println("Enter adjacency matrix:");
        System.out.println("(Enter 99999 for INF)");

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                D[i][j] = sc.nextInt();
            }
        }

        System.out.println("\nInitial Matrix:");
        printMatrix(D, n);

        for (int k = 0; k < n; k++) {
            System.out.println("Iteration "+(k+1)+" : ");

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (D[i][k] + D[k][j] < D[i][j]) {
                        D[i][j] = D[i][k] + D[k][j];
                    }
                }
            }
            printMatrix(D, n);
        }

        System.out.println("Final Shortest Path Matrix:");
        printMatrix(D, n);

        sc.close();
    }
}