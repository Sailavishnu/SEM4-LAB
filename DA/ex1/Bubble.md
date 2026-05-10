# Bubble Sort

## Algorithm
1. Start the program
2. Read the number of elements `n`
3. Read the array elements
4. Initialize outer loop from `i = 0` to `n - 2`
5. Initialize inner loop from `j = 0` to `n - i - 2`
6. Compare `A[j]` and `A[j + 1]`
7. If `A[j] > A[j + 1]`, swap both elements
8. Repeat until all passes are completed
9. Display the sorted array
10. Stop the program

---

## Pseudo Code

```text
Algorithm BubbleSort(A[0...n-1])

Input  : An array A[0...n-1] of orderable elements
Output : Array A[0...n-1] sorted in ascending order

for i ← 0 to n - 2 do
    for j ← 0 to n - i - 2 do
        if A[j] > A[j + 1] then
            swap A[j] and A[j + 1]
```

---

## Test Case

### Input

```text
Enter number of elements: 5

Enter elements:
64 34 25 12 22
```

### Output

```text
Sorted array:
12 22 25 34 64
```

---

## Time Complexity Derivation

For each pass:

```text
Outer loop runs = n - 1 times

Inner loop runs:
(n - 1) + (n - 2) + (n - 3) + ... + 1

T(n) = Σ(n - i - 1)
     = n(n - 1) / 2
     = O(n²)
```

---

## Time Complexity

| Case | Complexity |
|------|------------|
| Best Case | O(n) |
| Average Case | O(n²) |
| Worst Case | O(n²) |

---

## Space Complexity

```text
O(1)
```

---

## Important Points

- Bubble Sort compares adjacent elements
- Largest element moves to the end after each pass
- It is a stable sorting algorithm
- Simple but inefficient for large datasets
- Works well for small or nearly sorted arrays