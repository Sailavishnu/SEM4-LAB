# Huffman Coding

## Aim

To implement Huffman Coding algorithm.

---

## Algorithm

1. Start the program
2. Read characters and their frequencies
3. Create a node for each character
4. Insert all nodes into list `Q`
5. Repeat until only one node remains in `Q`
6. Sort nodes based on frequency
7. Remove two minimum frequency nodes `x` and `y`
8. Create new node `z`
9. Assign:

   ```text
   z.left  ← x
   z.right ← y
   z.freq  ← x.freq + y.freq
   ```

10. Insert `z` back into `Q`
11. Repeat process
12. Return remaining node as root of Huffman Tree
13. Generate Huffman Codes
14. Stop the program

---

## Pseudo Code

```text
Algorithm Huffman(S, F)

Input  : S set of characters
         F frequency of each character

Output : Root of Huffman Tree

Create a list Q of nodes for each
character in S

while size(Q) > 1 do

    Sort Q

    x ← first minimum node from Q

    y ← second minimum node from Q

    Create new node z

    z.left  ← x
    z.right ← y

    z.freq ← x.freq + y.freq

    Remove x and y from Q

    Insert z into Q

return the only node left in Q
```

---

## Test Case

### Input

```text
Characters : a b c d e f
Frequency  : 5 9 12 13 16 45
```

### Output

```text
Huffman Codes:

f : 0
c : 100
d : 101
a : 1100
b : 1101
e : 111
```

---

## Time Complexity Derivation

```text
For n characters:

Insertion into Priority Queue:
O(log n)

Removing minimum elements:
O(log n)

Repeated for all nodes:

T(n) = O(n log n)
```

---

## Time Complexity

| Case | Complexity |
|------|------------|
| Best Case | O(n log n) |
| Average Case | O(n log n) |
| Worst Case | O(n log n) |

---

## Space Complexity

```text
O(n)
```

---

## Important Points

- Greedy algorithm
- Used for data compression
- Generates variable length binary codes
- More frequent characters get shorter codes
- Produces optimal prefix codes