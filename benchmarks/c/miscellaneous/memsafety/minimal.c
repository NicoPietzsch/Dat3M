#include <stdlib.h>
#include <stdio.h>
#include <pthread.h>

int *shared1, *shared2;

void *t1_task() {
    *shared1 = 42;
}

void *t2_task() {
    shared2 = malloc(sizeof(int));
    *shared2 = *shared1;
    free(shared1);
}

int main() {
    shared1 = malloc(sizeof(int));
    pthread_t t1, t2;
    pthread_create(&t1, NULL, t1_task, NULL);
    pthread_create(&t2, NULL, t2_task, NULL);
}