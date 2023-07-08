#include <stdlib.h>
#include <stdio.h>
typedef char const* (*PTRFUN)();

struct Tiger{
    PTRFUN *vtable;
    const char *name;
};

char const* greet(void){
    return "Roooaar";
}
char const* menu(void){
    return "Meat";
}
char const* name(struct Tiger *tiger){
    return tiger->name;
}

PTRFUN TABLE[] = {name,greet,menu};

void construct(void *obj,const char *name){
    struct Tiger *tiger = (struct Tiger*) obj;
    tiger->vtable = TABLE;
    tiger->name = name;

}

struct Tiger* create(const char* name){
    struct Tiger *tiger = (struct Tiger*)malloc(sizeof(struct Tiger));
    if (tiger == NULL) {
        printf("Memory allocation failed!\n");
        exit(1);
    }
    construct(tiger,name);

    return tiger;
}


int sizeOf(){
    return sizeof(struct Tiger);
}