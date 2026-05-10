# Closest Pair Problem

## Algorithm
1. Start the program
2. Read the number of points `n`
3. Read all coordinate points `(x, y)`
4. Initialize minimum distance `d = ∞`
5. Initialize outer loop from `i = 1` to `n - 1`
6. Initialize inner loop from `j = i + 1` to `n`
7. Calculate distance between points using:

   ```text
   √((x₂ - x₁)² + (y₂ - y₁)²)
   ```

8. Compare calculated distance with `d`
9. Update `d` if smaller distance is found
10. Repeat for all point pairs
11. Display the minimum distance
12. Stop the program

---

## Pseudo Code

```text
Algorithm ClosestPair(P)

Input  : A list P of n points P1(x1,y1), P2(x2,y2), ... , Pn(xn,yn)
Output : The distance between closest pair of points

d ← ∞

for i ← 1 to n - 1 do
    for j ← i + 1 to n do
        d ← min(d,
                √((xi - xj)² + (yi - yj)²))

return d
```

---

## Test Case

### Input

```text
Enter number of points: 4

Points:
(2,3)
(12,30)
(40,50)
(5,1)
```

### Output

```text
Minimum distance = 3.605
```

---

## Time Complexity Derivation

```text
Outer loop runs = n - 1 times

Inner loop runs:
(n - 1) + (n - 2) + ... + 1

T(n) = Σ Σ 1
     = Σ(n - i)
     = n(n - 1) / 2
     = O(n²)
```

---

## Time Complexity

| Case | Complexity |
|------|------------|
| Best Case | O(n²) |
| Average Case | O(n²) |
| Worst Case | O(n²) |

---

## Space Complexity

```text
O(1)
```

---

## Important Points

- Finds the minimum distance between any two points
- Uses Euclidean distance formula
- Brute force approach compares all pairs
- Simple but inefficient for large datasets
- Divide and Conquer approach improves it to O(n log n)