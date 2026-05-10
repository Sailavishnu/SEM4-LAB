# Merge Sort

## Aim

To implement Merge Sort using Divide and Conquer technique.

---

## Algorithm

### Merge(A, l, m, r)

1. Initialize `i = l`
2. Initialize `j = m + 1`
3. Initialize `k = l`
4. Compare elements from both subarrays
5. Store smaller element into temporary array `B`
6. Increment corresponding index
7. Repeat until one subarray becomes empty
8. Copy remaining elements from left subarray
9. Copy remaining elements from right subarray
10. Copy sorted elements back to original array

---

### MergeSort(A, l, r)

1. Start the program
2. Check if `l < r`
3. Find middle element:

   ```text
   m = (l + r) / 2
   ```

4. Recursively sort left half
5. Recursively sort right half
6. Merge both sorted halves
7. Display sorted array
8. Stop the program

---

## Pseudo Code

```text
Algorithm Merge(A, l, m, r)

i ← l
j ← m + 1
k ← l

while i ≤ m and j ≤ r do
    if A[i] < A[j] then
        B[k++] ← A[i++]
    else
        B[k++] ← A[j++]

while i ≤ m do
    B[k++] ← A[i++]

while j ≤ r do
    B[k++] ← A[j++]
```

---

```text
Algorithm MergeSort(A, l, r)

if l < r then
    m ← (l + r) / 2

    MergeSort(A, l, m)
    MergeSort(A, m + 1, r)

    Merge(A, l, m, r)
```

---

## Test Case

### Input

```text
Enter number of elements: 6

Enter elements:
38 27 43 3 9 82
```

### Output

```text
Sorted array:
3 9 27 38 43 82
```

---

## Time Complexity Derivation

```text
Recurrence Relation:

T(n) = 2T(n/2) + n

Where,
a = 2
b = 2
f(n) = n

Using Master Theorem:

T(n) = Θ(n log n)
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

- Uses Divide and Conquer technique
- Stable sorting algorithm
- Requires extra memory
- Efficient for large datasets
- Recursively divides array into halves