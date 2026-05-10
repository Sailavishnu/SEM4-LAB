# Selection Sort

## Algorithm
1. Start the program
2. Read the number of elements `n`
3. Read the array elements
4. Initialize outer loop from `i = 0` to `n - 2`
5. Assume the current position as minimum (`min = i`)
6. Initialize inner loop from `j = i + 1` to `n - 1`
7. Compare `A[j]` with `A[min]`
8. If `A[j] < A[min]`, update `min = j`
9. After each pass, swap `A[i]` and `A[min]`
10. Repeat until array becomes sorted
11. Display the sorted array
12. Stop the program

---

## Pseudo Code

```text
Algorithm SelectionSort(A[0...n-1])

Input  : An array A[0...n-1] of orderable elements
Output : Array A[0...n-1] sorted in ascending order

for i ← 0 to n - 2 do
    min ← i

    for j ← i + 1 to n - 1 do
        if A[j] < A[min] then
            min ← j

    swap A[i] and A[min]
```

---

## Test Case

### Input

```text
Enter number of elements: 5

Enter elements:
64 25 12 22 11
```

### Output

```text
Sorted array:
11 12 22 25 64
```

---

## Time Complexity Derivation

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

- Selection Sort repeatedly selects the minimum element
- Performs fewer swaps compared to Bubble Sort
- In-place sorting algorithm
- Not stable in general implementation
- Suitable for small datasets