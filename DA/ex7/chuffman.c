#include <stdio.h>
#include <stdlib.h>

struct Node {
    char ch;
    float freq;
    struct Node *left, *right;
};

float totalSum = 0;
int maxBits = 0;

struct Node* createNode(char ch, float freq) {
    struct Node* newNode = (struct Node*)malloc(sizeof(struct Node));
    newNode->ch = ch;
    newNode->freq = freq;
    newNode->left = NULL;
    newNode->right = NULL;
    return newNode;
}

void printAndCalc(struct Node* root, char code[], int depth) {
    if (root->left == NULL && root->right == NULL) {
        code[depth] = '\0';
        printf("%c : %s\n", root->ch, code);
        totalSum += (root->freq * depth);
        if (depth > maxBits) maxBits = depth;
        return;
    }

    if (root->left) {
        code[depth] = '0';
        printAndCalc(root->left, code, depth + 1);
    }
    if (root->right) {
        code[depth] = '1';
        printAndCalc(root->right, code, depth + 1);
    }
}

int compare(const void* a, const void* b) {
    struct Node* n1 = *(struct Node**)a;
    struct Node* n2 = *(struct Node**)b;
    if (n1->freq > n2->freq) return 1;
    else if (n1->freq < n2->freq) return -1;
    return 0;
}

int main() {
    int n;
    printf("Enter number of characters: ");
    scanf("%d", &n);

    struct Node** list = (struct Node**)malloc(n * sizeof(struct Node*));

    for (int i = 0; i < n; i++) {
        char ch;
        float freq;
        printf("Char %d: ", i + 1);
        scanf(" %c", &ch);
        printf("Freq: ");
        scanf("%f", &freq);
        list[i] = createNode(ch, freq);
    }

    int size = n;

    while (size > 1) {
        qsort(list, size, sizeof(struct Node*), compare);

        struct Node* left = list[0];
        struct Node* right = list[1];

        struct Node* newNode = createNode('-', left->freq + right->freq);
        newNode->left = left;
        newNode->right = right;

        for (int i = 2; i < size; i++) {
            list[i - 2] = list[i];
        }

        size -= 2;
        list[size] = newNode;
        size++;
    }

    struct Node* root = list[0];

    printf("\nHuffman Codes:\n");
    char code[100];
    printAndCalc(root, code, 0);

    float ratio = ((maxBits - totalSum) / maxBits) * 100;
    printf("\nWeighted Sum: %f\n", totalSum);
    printf("Max Bit: %d\n", maxBits);
    printf("Compression Ratio: %f%%\n", ratio);

    free(list);
    return 0;
}