#include <stdlib.h>
#include <stdio.h>

typedef struct item {
    int value;
    struct item *next;
} item_t;

void push(item_t **stack, int value) {
    item_t *new_tos = malloc(sizeof(item_t));

    new_tos->value = value;
    new_tos->next = *stack;
    *stack = new_tos;
}

int pop(item_t **stack) {
    int value = -1;
    item_t *new_tos;

    if (!*stack) {
        return -1;
    }

    new_tos = (*stack)->next;
    value = (*stack)->value;
    free(*stack);
    *stack = new_tos;

    return value;
}

int main(int argc, char **argv) {
    item_t *stack;
    push(&stack, 42);
    push(&stack, 12);
    printf("%d\n", pop(&stack));
    push(&stack, 74);
    printf("%d\n", pop(&stack));
    printf("%d\n", pop(&stack));
}