import java.util.*;

class Node {
    char ch;
    float freq;
    Node left, right;

    Node(char ch, float freq) {
        this.ch = ch;
        this.freq = freq;
    }
}

public class HuffmanCoding {
    static float totalSum = 0;
    static int maxBits = 0;

    static void printAndCalc(Node root, String code) {
        if (root.left == null && root.right == null) {
            System.out.println(root.ch + " : " + code);
            totalSum += (root.freq * code.length());
            maxBits = Math.max(maxBits, code.length());
            return;
        }
        printAndCalc(root.left, code + "0");
        printAndCalc(root.right, code + "1");
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of characters: ");
        int n = sc.nextInt();
        List<Node> list = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            System.out.print("Char " + (i + 1) + ": ");
            char ch = sc.next().charAt(0);
            System.out.print("Freq: ");
            float freq = sc.nextFloat();
            list.add(new Node(ch, freq));
        }
        while (list.size() > 1) {
            Collections.sort(list, (a, b) -> Float.compare(a.freq, b.freq));
            Node left = list.get(0);
            Node right = list.get(1);
            Node newNode = new Node('-', left.freq + right.freq);
            newNode.left = left;
            newNode.right = right;
            list.remove(0);
            list.remove(0);
            list.add(newNode);
        }
        Node root = list.get(0);
        System.out.println("\nHuffman Codes:");
        printAndCalc(root, "");

        float ratio = ((maxBits - totalSum) / maxBits) * 100;
        System.out.println("\nWeighted Sum: " + totalSum);
        System.out.println("Max Bit: " + maxBits);
        System.out.println("Compression Ratio: " + ratio + "%");
    }
}