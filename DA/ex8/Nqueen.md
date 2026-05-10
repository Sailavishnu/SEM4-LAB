# N Queens Problem

## Aim

To implement N Queens Problem using Backtracking.

---

## Algorithm

### Place(k, i)

1. Compare current queen position with previously placed queens
2. Check whether any queen exists in same column
3. Check diagonal conflicts using:

   ```text
   |x[j] - i| = |j - k|
   ```

4. If conflict occurs, return `false`
5. Otherwise return `true`

---

### NQueens(k, n)

1. Start the program
2. Initialize loop from `i = 1` to `n`
3. Check whether queen can be placed using `Place(k, i)`
4. If position is safe:
   - Place queen:

     ```text
     x[k] ← i
     ```

5. If all queens are placed:
   - Print solution

6. Otherwise recursively call:

   ```text
   NQueens(k + 1, n)
   ```

7. Repeat until all possible solutions are explored
8. Stop the program

---

## Pseudo Code

```text
Algorithm Place(k, i)

Returns true if a queen can be placed
in kth row and ith column,
otherwise false.

for j ← 1 to k - 1 do

    if (x[j] = i) or
       (Abs(x[j] - i) = Abs(j - k))

       then return false

return true
```

---

```text
Algorithm NQueens(k, n)

Using Backtracking, this procedure prints all
possible placements of n queens in n × n
chessboard so that they do not attack.

for i ← 1 to n do

    if Place(k, i) then

        x[k] ← i

        if k = n then
            write(x[1:n])

        else
            NQueens(k + 1, n)
```

---

## Test Case

### Input

```text
Enter number of queens: 4
```

### Output

```text
Solution 1:

0 1 0 0
0 0 0 1
1 0 0 0
0 0 1 0

Solution 2:

0 0 1 0
1 0 0 0
0 0 0 1
0 1 0 0
```

---

## Time Complexity Derivation

```text
For each row,
queen can be placed in n positions.

The recursion explores all possible arrangements.

T(n) = n × (n - 1) × (n - 2) × ... × 1

Therefore,

T(n) = O(n!)
```

---

## Time Complexity

| Case | Complexity |
|------|------------|
| Best Case | O(n!) |
| Average Case | O(n!) |
| Worst Case | O(n!) |

---

## Space Complexity

```text
O(n)
```

---

## Important Points

- Uses Backtracking technique
- Ensures no two queens attack each other
- Checks row, column and diagonal conflicts
- Recursive solution
- Efficient pruning reduces unnecessary checks