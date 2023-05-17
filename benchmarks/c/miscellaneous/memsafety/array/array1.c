#include <stdlib.h>
#include <stdio.h>

int main()
{
    int *p = malloc(4 * sizeof(int));
    *p = 42;
    *(p+1) = 12;

    free(p);

    return *(p+1) + 100;
}