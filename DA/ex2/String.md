# String Matching Using Brute Force Technique

## Aim

To implement string matching using Brute Force Technique.

---

## Algorithm
1. Start the program
2. Read the text string `T`
3. Read the pattern string `P`
4. Find the lengths of text `n` and pattern `m`
5. Initialize loop from `i = 0` to `n - m`
6. Initialize `j = 0`
7. Compare characters `P[j]` and `T[i + j]`
8. Continue comparison while characters match
9. Increment `j` for every successful match
10. If `j = m`, pattern is found at index `i`
11. Return the index
12. If pattern is not found, return `-1`
13. Stop the program

---

## Pseudo Code

```text
Algorithm StringMatch(T[0...n-1], P[0...m-1])

Input  : A text T with length n
         A pattern P with length m

Output : The index of first character in the text
         that matches with pattern

for i ← 0 to n - m do
    j ← 0

    while j < m and P[j] = T[i + j] do
        j ← j + 1

    if j = m then
        return i

return -1
```

---

## Test Case

### Input

```text
Text    : COMPUTERENGINEERING
Pattern : ENGINE
```

### Output

```text
Pattern found at index 8
```

---

## Time Complexity Derivation

```text
Outer loop runs = (n - m + 1) times

For every position,
pattern comparison may take m comparisons

T(n) = (n - m + 1) × m

Worst Case:
T(n) = O(mn)
```

---

## Time Complexity

| Case | Complexity |
|------|------------|
| Best Case | O(n) |
| Average Case | O(n) |
| Worst Case | O(mn) |

---

## Space Complexity

```text
O(1)
```

---

## Important Points

- Simple pattern matching technique
- Compares pattern character by character
- No preprocessing required
- Inefficient for large texts
- Also called Naive String Matching