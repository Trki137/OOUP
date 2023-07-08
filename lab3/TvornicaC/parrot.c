#include <stdlib.h>
#include <stdio.h>
typedef char const* (*PTRFUN)();

struct Parrot{
    PTRFUN *vtable;
    const char *name;
};

char const* greet(void){
    return "parrot greet";
}
char const* menu(void){
    return "parrot menu";
}
char const* name(struct Parrot *parrot){
    return parrot->name;
}

PTRFUN TABLE[] = {name,greet,menu};

void construct(void *obj,const char *name){
    struct Parrot *parrot = (struct Parrot*) obj;
    parrot->vtable = TABLE;
    parrot->name = name;
}

struct Parrot* create(const char* name){
    struct Parrot *parrot = (struct Parrot*)malloc(sizeof(struct Parrot));
    if (parrot == NULL) {
        printf("Memory allocation failed!\n");
        exit(1);
    }
    construct(parrot,name);
    return parrot;
}


int sizeOf(){
    return sizeof(struct Parrot);
}