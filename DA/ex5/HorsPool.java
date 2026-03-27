import java.util.Scanner;

public class HorsPool {

    static int[] shiftTable = new int[256];

    static void buildShiftTable(String pattern) {
        int m = pattern.length();

        for (int i = 0; i < 256; i++)
            shiftTable[i] = m;

        for (int i = 0; i < m - 1; i++)
            shiftTable[pattern.charAt(i)] = m - 1 - i;
    }

    static int horspoolSearch(String text, String pattern) {
        int n = text.length();
        int m = pattern.length();

        buildShiftTable(pattern);

        int i = m - 1;
        while (i <= n - 1) {
            int k = 0;
            while (k <= m - 1 && pattern.charAt(m - 1 - k) == text.charAt(i - k))
                k++;
            if (k == m)
                return i - m + 1;
            i += shiftTable[text.charAt(i)];
        }
        return -1;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter text:    ");
        String text = sc.nextLine();

        System.out.print("Enter pattern: ");
        String pattern = sc.nextLine();

        int result = horspoolSearch(text, pattern);
        System.out.println("\nResult:");
        if (result != -1)
            System.out.println("Pattern found at index: " + result);
        else
            System.out.println("Pattern NOT found in the text.");

        sc.close();
    }
}
