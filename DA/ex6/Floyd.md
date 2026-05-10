# Floyd Algorithm

## Aim

To implement Floyd Algorithm.

---

## Algorithm

1. Start the program
2. Read the weighted adjacency matrix `W`
3. Initialize distance matrix:

   ```text
   D ← W
   ```

4. Initialize outer loop from `k = 1` to `n`
5. Initialize loop from `i = 1` to `n`
6. Initialize loop from `j = 1` to `n`
7. Update shortest distance using:

   ```text
   D[i][j] = min(D[i][j], D[i][k] + D[k][j])
   ```

8. Repeat until all intermediate vertices are processed
9. Display shortest path matrix
10. Stop the program

---

## Pseudo Code

```text
Algorithm Floyd(W[1...n, 1...n])

D ← W

for k ← 1 to n do

    for i ← 1 to n do

        for j ← 1 to n do

            D[i,j] ← min(D[i,j],
                         D[i,k] + D[k,j])

return D
```

---

## Test Case

### Input

```text
Adjacency Matrix:

0   3   999  7
8   0   2    999
5   999 0    1
2   999 999  0
```

### Output

```text
Shortest Path Matrix:

0 3 5 6
5 0 2 3
3 6 0 1
2 5 7 0
```

---

## Time Complexity Derivation

```text
The basic operation is addition and comparison.

The algorithm contains 3 nested loops.

For each loop:
Number of iterations = n

T(n) = n × n × n

Therefore,

T(n) = O(n³)
```

---

## Time Complexity

| Case | Complexity |
|------|------------|
| Best Case | O(n³) |
| Average Case | O(n³) |
| Worst Case | O(n³) |

---

## Space Complexity

```text
O(n²)
```

---

## Important Points

- Finds shortest paths between all pairs of vertices
- Uses Dynamic Programming technique
- Works for weighted directed graphs
- Can handle negative edge weights
- Does not work for negative weight cycles