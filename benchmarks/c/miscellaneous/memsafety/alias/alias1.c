#include <stdlib.h>

int main()
{
    int *p, *q;
    p = malloc(sizeof(int));
    q = p - 2;
    free(p);
    p = NULL;
    *(q + 2) = 42;    //use-after-free throug alias
}