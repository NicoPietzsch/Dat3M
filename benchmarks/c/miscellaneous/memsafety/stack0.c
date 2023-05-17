#include <stdlib.h>
#include <pthread.h>

typedef struct item {
    int value;
    struct item *next;
} item_t;

item_t *stack;

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

void *thread_pop(void* arg)
{
    pop(&stack);
    return NULL;
}

int main(int argc, char **argv) {
    push(&stack, 42);
    push(&stack, 12);
    
    pthread_t t1, t2;
    pthread_create(&t1, NULL, thread_pop, NULL);
    pthread_create(&t2, NULL, thread_pop, NULL);
    return 0;
}