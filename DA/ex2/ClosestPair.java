import java.util.Scanner;

public class ClosestPair {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of points: ");
        int n = sc.nextInt();

        int[][] arr = new int[n][2];


        for (int i = 0; i < n; i++) {
            System.out.println("enter the x and y coordinates of point " + (i + 1) + ":");
            arr[i][0] = sc.nextInt(); 
            arr[i][1] = sc.nextInt(); 
        }

        double minDist = Double.MAX_VALUE;
        int p1 = 0, p2 = 0;

        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {

                int x1 = arr[i][0];
                int y1 = arr[i][1];
                int x2 = arr[j][0];
                int y2 = arr[j][1];

                double dist = Math.sqrt(
                        Math.pow(x2 - x1, 2) +
                        Math.pow(y2 - y1, 2)
                );

                if (dist < minDist) {
                    minDist = dist;
                    p1 = i;
                    p2 = j;
                }
            }
        }

        System.out.println("\nClosest Pair of Points:");
        System.out.println("(" + arr[p1][0] + ", " + arr[p1][1] + ") and (" +
                           arr[p2][0] + ", " + arr[p2][1] + ")");

        System.out.println("Minimum Distance = " + minDist);
    }
}
