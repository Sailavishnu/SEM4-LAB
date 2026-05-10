# Horspool String Matching

## Aim

To implement Horspool String Matching algorithm.

---

## Algorithm

### ShiftTable(P[0...m-1])

1. Initialize all table values to `m`
2. Traverse pattern from left to right
3. Store shift values using:

   ```text
   table[P[j]] = m - 1 - j
   ```

4. Return shift table

---

### Horspool(P[0...m-1], T[0...n-1])

1. Construct shift table
2. Initialize `i = m - 1`
3. Compare pattern from right to left
4. If characters match, continue comparison
5. Increment `k` for every match
6. If all characters match, return index
7. Otherwise shift pattern using shift table
8. Repeat until end of text
9. Return `-1` if pattern not found

---

## Pseudo Code

```text
Algorithm ShiftTable(P[0...m-1])

for i ← 0 to size - 1 do
    table[i] ← m

for j ← 0 to m - 2 do
    table[P[j]] ← m - 1 - j

return table
```

---

```text
Algorithm Horspool(P[0...m-1], T[0...n-1])

ShiftTable(P[0...m-1])

i ← m - 1

while i ≤ n - 1 do

    k ← 0

    while k ≤ m - 1 and
          P[m - 1 - k] = T[i - k] do

          k ← k + 1

    if k = m then
        return i - m + 1

    else
        i ← i + table[T[i]]

return -1
```

---

## Test Case

### Input

```text
Text    : JIM_SAW_ME_IN_A_BARBERSHOP
Pattern : BARBER
```

### Output

```text
Pattern found at index 15
```

---

## Time Complexity Derivation

### Best Case

```text
Large shifts occur frequently

T(n) = O(n / m)
```

---

### Worst Case

```text
All characters compared repeatedly

T(n) = O(mn)
```

---

## Time Complexity

| Case | Complexity |
|------|------------|
| Best Case | O(n / m) |
| Average Case | O(n) |
| Worst Case | O(mn) |

---

## Space Complexity

```text
O(size of alphabet)
```

---

## Important Points

- Efficient string matching algorithm
- Uses Shift Table for faster searching
- Compares pattern from right to left
- Faster than Brute Force in practice
- Based on bad character heuristic