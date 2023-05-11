#include <stdlib.h>

int main()
{
    int *a = malloc(sizeof(int));
    *a = 42;
    free(a);
    
    int b = *a + 5;

    return b;
}