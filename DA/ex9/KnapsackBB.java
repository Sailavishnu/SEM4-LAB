import java.util.Arrays;
import java.util.Scanner;

class Item {
    int id;
    int weight;
    int profit;
    float ratio;
}

public class KnapsackBB {
    static int n, m, maxProfit = 0;
    static Item[] items;
    static int[] currentSelection;
    static int[] bestSelection;

    static void sortItems() {
        Arrays.sort(items, (a, b) -> Float.compare(b.ratio, a.ratio));
    }

    static float calculateBound(int level, int currentWeight, int currentProfit) {
        if (currentWeight >= m)
            return 0;

        float profitBound = currentProfit;
        int j = level;
        int totWeight = currentWeight;

        while (j < n && totWeight + items[j].weight <= m) {
            totWeight += items[j].weight;
            profitBound += items[j].profit;
            j++;
        }

        if (j < n)
            profitBound += (m - totWeight) * items[j].ratio;

        return profitBound;
    }

    static void solve(int level, int currentWeight, int currentProfit) {
        if (currentProfit > maxProfit) {
            maxProfit = currentProfit;
            for (int i = 0; i < n; i++) {
                bestSelection[items[i].id] = currentSelection[i];
            }
        }

        if (level == n)
            return;

        if (calculateBound(level, currentWeight, currentProfit) > maxProfit) {
            if (currentWeight + items[level].weight <= m) {
                currentSelection[level] = 1;
                solve(level + 1, currentWeight + items[level].weight, currentProfit + items[level].profit);
            }
            currentSelection[level] = 0;
            solve(level + 1, currentWeight, currentProfit);
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of items: ");
        n = sc.nextInt();
        System.out.print("Enter knapsack capacity: ");
        m = sc.nextInt();

        items = new Item[n];
        currentSelection = new int[n];
        bestSelection = new int[n];

        System.out.println("Enter weights:");
        for (int i = 0; i < n; i++) {
            items[i] = new Item();
            items[i].id = i;
            items[i].weight = sc.nextInt();
        }

        System.out.println("Enter profits:");
        for (int i = 0; i < n; i++) {
            items[i].profit = sc.nextInt();
            items[i].ratio = (float) items[i].profit / items[i].weight;
        }

        sortItems();
        solve(0, 0, 0);

        System.out.println("Maximum profit = " + maxProfit);
        System.out.print("Selected items: {");
        for (int i = 0; i < n; i++) {
            System.out.print(bestSelection[i]);
            if (i < n - 1)
                System.out.print(",");
        }
        System.out.println("}");
        sc.close();
    }
}
