# Knapsack Using Branch and Bound

## Aim

To implement Knapsack problem using Branch and Bound technique.

---

## Algorithm

1. Start the program
2. Read number of items `n`
3. Read knapsack capacity `m`
4. Read weights and profits of all items
5. Calculate profit/weight ratio for each item
6. Sort items in decreasing order of ratio
7. Initialize:
   - `maxProfit = 0`
   - `currentSelection[]`
   - `bestSelection[]`
8. Calculate upper bound using fractional knapsack method
9. Start recursive Branch and Bound process
10. Include current item if weight permits
11. Update current profit and selection
12. Recursively explore next level
13. Exclude current item and explore remaining possibilities
14. Update maximum profit whenever better solution is found
15. Store best item selection
16. Display maximum profit and selected items
17. Stop the program

---

## Pseudo Code

```text
Algorithm KnapsackBB()

Sort items according to
profit / weight ratio

maxProfit ← 0

solve(level, currentWeight, currentProfit)

return maxProfit
```

---

```text
Algorithm calculateBound(level,
                         currentWeight,
                         currentProfit)

if currentWeight ≥ m
    return 0

profitBound ← currentProfit

while items can be added do

    add complete item profit

if remaining capacity exists

    add fractional profit

return profitBound
```

---

```text
Algorithm solve(level,
                currentWeight,
                currentProfit)

if currentProfit > maxProfit then

    update maxProfit

if level = n
    return

if calculateBound(...) > maxProfit then

    include current item

    solve(next level)

    exclude current item

    solve(next level)
```

---

## Test Case

### Input

```text
Enter number of items: 4

Enter knapsack capacity: 10

Enter weights:
7 5 3 4

Enter profits:
40 42 25 12
```

### Output

```text
Maximum profit = 67

Selected items:
{0,1,1,0}
```

---

## Time Complexity Derivation

```text
Branch and Bound generates
state space tree recursively.

Each item has 2 possibilities:
Include or Exclude

Total possible subsets:

T(n) = 2^n

Bounding reduces unnecessary branches.

Therefore,

Worst Case:

T(n) = O(2^n)
```

---

## Time Complexity

| Case | Complexity |
|------|------------|
| Best Case | O(n log n) |
| Average Case | Less than O(2^n) |
| Worst Case | O(2^n) |

---

## Space Complexity

```text
O(n)
```

---

## Important Points

- Uses Branch and Bound technique
- Uses recursion instead of queue
- Bounding reduces unnecessary exploration
- Items sorted based on profit/weight ratio
- Produces optimal solution