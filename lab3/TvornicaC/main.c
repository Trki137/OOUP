#include <stdio.h>
#include <stdlib.h>
#include <malloc.h>
#include "myfactory.h"

typedef char const* (*PTRFUN)();
struct Animal{
    PTRFUN* vtable;
    const char *name;
};

void animalPrintGreeting(struct Animal *animal){
    printf("%s pozdravlja: %s\n", animal->vtable[0](animal),animal->vtable[1]());
}
void animalPrintMenu(struct Animal *animal){
    printf("%s voli: %s\n", animal->vtable[0](animal),animal->vtable[2]());
}


int main(int argc, char *argv[]){

    for (int i=0; i<argc/2; ++i){
        struct Animal *p = NULL;
        if(i%2 == 0) {
            p = alloca(sizeof(struct Animal));
            myFactoryStack(argv[1 + 2 * i], argv[1 + 2 * i + 1], p);
        }else p=(struct Animal*)myfactory(argv[1+2*i], argv[1+2*i+1]);

        if (!p){
            printf("Creation of plug-in object %s failed.\n", argv[1+2*i]);
            continue;
        }

        animalPrintGreeting(p);
        animalPrintMenu(p);
        free(p);
    }
}