#include <stdlib.h>

int main()
{
    int *p, *q;
    p = malloc(sizeof(int));
    q = p;
    free(p);
    p = NULL;
    *q = 42;    //use-after-free throug alias
}