#include <stdlib.h>

int main(int argc, char **argv)
{
    int *p, *q;
    p = malloc(sizeof(int));
    int offset = __VERIFIER_nondet_int();
    q = p + offset;
    free(p);
    p = NULL;
    *q = 42;    //use-after-free through alias
}