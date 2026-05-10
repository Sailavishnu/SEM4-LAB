# Quick Sort

## Aim

To implement Quick Sort using Divide and Conquer technique.

---

## Algorithm

### Partition(A, left, right)

1. Select first element as pivot
2. Initialize `i = left + 1`
3. Initialize `j = right`
4. Increment `i` until element greater than pivot is found
5. Decrement `j` until element smaller than pivot is found
6. If `i < j`, swap `A[i]` and `A[j]`
7. Otherwise swap pivot with `A[j]`
8. Return partition index `j`

---

### QuickSort(A, left, right)

1. Start the program
2. Check if `left < right`
3. Find partition position using Partition()
4. Recursively sort left subarray
5. Recursively sort right subarray
6. Display sorted array
7. Stop the program

---

## Pseudo Code

```text
Algorithm QuickSort(A, left, right)

if left < right then
    s ← Partition(A, left, right)

    QuickSort(A, left, s - 1)
    QuickSort(A, s + 1, right)
```

---

```text
Algorithm Partition(A, left, right)

pivot ← A[left]

i ← left + 1
j ← right

while true do

    while i ≤ right and pivot ≥ A[i] do
        increment i

    while pivot < A[j] do
        decrement j

    if i < j then
        swap A[i] and A[j]

    else
        swap A[left] and A[j]
        return j
```

---

## Test Case

### Input

```text
Enter number of elements: 6

Enter elements:
44 33 11 55 77 90
```

### Output

```text
Sorted array:
11 33 44 55 77 90
```

---

## Time Complexity Derivation

```text
Best / Average Case Recurrence:

T(n) = 2T(n/2) + n

Where,
a = 2
b = 2
f(n) = n

Using Master Theorem:

T(n) = Θ(n log n)
```

```text
Worst Case:

T(n) = T(n - 1) + n

Occurs when pivot is smallest or largest element

T(n) = O(n²)
```

---

## Time Complexity

| Case | Complexity |
|------|------------|
| Best Case | O(n log n) |
| Average Case | O(n log n) |
| Worst Case | O(n²) |

---

## Space Complexity

```text
O(log n)
```

---

## Important Points

- Uses Divide and Conquer technique
- Pivot selection affects performance
- In-place sorting algorithm
- Faster in practice for large datasets
- Not a stable sorting algorithm