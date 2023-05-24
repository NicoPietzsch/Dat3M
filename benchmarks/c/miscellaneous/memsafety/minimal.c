#include <stdlib.h>
#include <stdio.h>
#include <pthread.h>

int *shared;

void *t1_task() {
    *shared = 42;
}

void *t2_task() {
    int temp = *shared;
    free(shared);
}

int main() {
    shared = malloc(sizeof(int));
    pthread_t t1, t2;
    pthread_create(&t1, NULL, t1_task, NULL);
    pthread_create(&t2, NULL, t2_task, NULL);
}