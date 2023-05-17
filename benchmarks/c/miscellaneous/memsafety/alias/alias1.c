#include <stdlib.h>

int main(int argc, char **argv)
{
    int *p, *q;
    p = malloc(sizeof(int));
    int offset = argv[1] - '0';
    q = p + offset;
    free(p);
    p = NULL;
    *q = 42;    //use-after-free throug alias
}