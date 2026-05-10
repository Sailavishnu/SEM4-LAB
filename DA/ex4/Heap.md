# Heap Sort

## Aim

To implement Heap Sort using Heap data structure.

---

## Algorithm

### PercolateDown(arr, size, root)

1. Initialize `smallest = root`
2. Find left child using:

   ```text
   left = 2 × root + 1
   ```

3. Find right child using:

   ```text
   right = 2 × root + 2
   ```

4. Compare left child with root
5. Update `smallest` if left child is smaller
6. Compare right child with current smallest
7. Update `smallest` if right child is smaller
8. If `smallest ≠ root`
   - Swap `arr[root]` and `arr[smallest]`
   - Call `PercolateDown()` recursively

---

### RemoveMin(arr, size)

1. Store minimum element from root
2. Replace root with last element
3. Reduce heap size
4. Call `PercolateDown(arr, size, 0)`
5. Return minimum element

---

### HeapSort(arr, n)

1. Build Min Heap
2. Repeat for all elements
3. Remove minimum element
4. Store removed element into sorted array
5. Display sorted array

---

## Pseudo Code

```text
Algorithm PercolateDown(arr, size, root)

smallest ← root

left ← 2 × root + 1
right ← 2 × root + 2

if left < size and arr[left] < arr[smallest]
    smallest ← left

if right < size and arr[right] < arr[smallest]
    smallest ← right

if smallest ≠ root
    swap arr[root] and arr[smallest]

    PercolateDown(arr, size, smallest)
```

---

```text
Algorithm RemoveMin(arr, size)

min ← arr[0]

arr[0] ← arr[size - 1]

PercolateDown(arr, size - 1, 0)

return min
```

---

```text
Algorithm HeapSort(arr, n)

BuildHeap(arr, n)

for i ← 0 to n - 1
    sorted[i] ← RemoveMin(arr, size)
```

---

## Test Case

### Input

```text
Enter number of elements: 5

Enter elements:
40 10 30 50 20
```

### Output

```text
Sorted Array:
10 20 30 40 50
```

---

## Time Complexity Derivation

```text
Building Heap:
O(n)

PercolateDown:
O(log n)

RemoveMin repeated n times:

T(n) = n × log n

Therefore,

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

- Uses Binary Heap data structure
- Efficient for large datasets
- Heap can be Min Heap or Max Heap
- Not a stable sorting algorithm