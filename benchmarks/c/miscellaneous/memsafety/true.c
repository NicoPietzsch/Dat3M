#include <stdlib.h>
#include <pthread.h>

#ifndef NTHREADS
#define NTHREADS 1
#endif

int* shared;

int main()
{
    shared = malloc(sizeof(int));
    pthread_t t[NTHREADS];

    for (int i = 0; i < NTHREADS; i++)
        pthread_create(&t[i], 0, thread_n, (void *)i);

    free(shared);

    for (int i = 0; i < NTHREADS; i++)
        pthread_join(t[i], 0);

    return 0;
}

void *thread_n(void* arg)
{
    *shared = 42;
}