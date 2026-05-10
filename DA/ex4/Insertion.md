# Insertion Sort

## Aim

To implement Insertion Sort.

---

## Algorithm

1. Start the program
2. Read the number of elements `n`
3. Read the array elements
4. Initialize loop from `i = 1` to `n - 1`
5. Store current element in variable `v`
6. Initialize `j = i - 1`
7. Compare `A[j]` with `v`
8. Shift elements greater than `v` one position to the right
9. Repeat until correct position is found
10. Insert `v` at correct position
11. Repeat for all elements
12. Display sorted array
13. Stop the program

---

## Pseudo Code

```text
Algorithm InsertionSort(A[0...n-1])

Input  : An array of unordered elements
Output : The array in ascending order
         using Insertion Sort

for i ← 1 to n - 1 do

    v ← A[i]
    j ← i - 1

    while j ≥ 0 and A[j] > v do
        A[j + 1] ← A[j]
        j ← j - 1

    A[j + 1] ← v
```

---

## Test Case

### Input

```text
Enter number of elements: 5

Enter elements:
9 5 1 4 3
```

### Output

```text
Sorted array:
1 3 4 5 9
```

---

## Time Complexity Derivation

### Best Case

```text
When array is already sorted

Inner loop executes only once

T(n) = Σ 1
     = n - 1

Therefore,

T(n) = O(n)
```

---

### Worst Case

```text
When array is sorted in reverse order

T(n) = 1 + 2 + 3 + ... + (n - 1)

     = n(n - 1) / 2

Therefore,

T(n) = O(n²)
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

- Builds sorted array one element at a time
- Efficient for small datasets
- Stable sorting algorithm
- Performs well for nearly sorted arrays
- In-place sorting algorithm