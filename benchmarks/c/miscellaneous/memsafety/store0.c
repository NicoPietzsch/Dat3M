#include <stdlib.h>

int main()
{
    int *a = malloc(sizeof(int));
    free(a);
    *a = 42;
}