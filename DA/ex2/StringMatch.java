import java.util.*;

public class StringMatch {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int flag = 0;
        System.out.print("enter the text:");
        String text = sc.nextLine();
        System.out.print("enter the pattern to be searched:");
        String pattern = sc.nextLine();
        int n = text.length();
        int m = pattern.length();
        for (int i = 0; i <= n - m; i++) {
            int j;
            for (j = 0; j < m; j++) {
                if (text.charAt(i + j) != pattern.charAt(j)) {
                    break;
                }
            }
            if (j == m) {
                flag = i;
            }
        }
        if (flag != 0) {
            System.out.println("Pattern found at index: " + flag);
        } else {
            System.out.println("pattern not found");
        }
    }
}
