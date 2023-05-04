#include <stdlib.h>
#include <pthread.h>

#ifndef NTHREADS
#define NTHREADS 1
#endif

int* shared;

void *thread_n(void* arg)
{
    *shared = 42;
}

int main()
{
    shared = malloc(sizeof(int));
    *shared = 17;
    pthread_t t[NTHREADS];

    /*for (int i = 0; i < NTHREADS; i++)
        //pthread_create(&t[i], 0, thread_n, (void *)i);*/

    free(shared);

    /*for (int i = 0; i < NTHREADS; i++)
        pthread_join(t[i], 0);*/

    return 0;
}
