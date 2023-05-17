#include <stdlib.h>

int main()
{
    int *p, *q;
    p = malloc(sizeof(int));
    int offset = 1;
    q = p + offset;
    free(p);
    p = NULL;
    *q = 42;    //this is an error, but not a use-after-free
}