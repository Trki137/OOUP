#include <stdio.h>
#include <stdlib.h>
#include <string.h>

char const* dogGreet(void){
    return "vau!";
}
char const* dogMenu(void){
    return "kuhanu govedinu";
}
char const* catGreet(void){
    return "mijau!";
}
char const* catMenu(void){
    return "konzerviranu tunjevinu";
}

typedef char const* (*PTRFUN)();

PTRFUN dogVirtualTable[] = {dogGreet,dogMenu};
PTRFUN catVirtualTable[] = {catGreet,catMenu};

struct Animal {
    PTRFUN *virtualTable;
    const char *name;
};

void constructDog(struct Animal *dog, const char* name){
    dog->name=name;
    dog->virtualTable = dogVirtualTable;
}

void constructCat(struct Animal *cat, const char* name){
    cat->name = name;
    cat->virtualTable = catVirtualTable;
}

struct Animal* createDog(const char* name){
    struct Animal *dog = malloc(sizeof(struct Animal));
    constructDog(dog,name);
    return dog;
}

struct Animal* createCat(const char* name){
    struct Animal *cat = malloc(sizeof(struct Animal));
    constructCat(cat,name);
    return cat;
}

void animalPrintGreeting(struct Animal* animal){
    printf("%s pozdravlja: %s\n", animal->name,animal->virtualTable[0]());
}

void animalPrintMenu(struct Animal* animal) {
    printf("%s voli: %s\n", animal->name,animal->virtualTable[1]());
}
void testAnimals(void){
    struct Animal* p1=createDog("Hamlet");
    struct Animal* p2=createCat("Ofelija");
    struct Animal* p3=createDog("Polonije");

    animalPrintGreeting(p1);
    animalPrintGreeting(p2);
    animalPrintGreeting(p3);

    animalPrintMenu(p1);
    animalPrintMenu(p2);
    animalPrintMenu(p3);

    free(p1);
    free(p2);
    free(p3);
}




void createNDogs(int n){
    struct Animal *dogs =(struct Animal*) malloc(sizeof(struct Animal) * n);
    const char *dogNames[] = {"Ben", "Rex", "Dakota", "Meggy", "Penny","Winny"};

    if(dogs == NULL){
        printf("Memory allocation failed");
        exit(1);
    }

    for (int i = 0; i < n; i++) {
        dogs[i] = *createDog(dogNames[i % 6]);
    }

    for(int i = 0; i < n; i++){
        animalPrintGreeting(&dogs[i]);
        animalPrintMenu(&dogs[i]);
    }
}


int main(){
    testAnimals();

    printf("\nAnimal on stack\n");
    struct Animal dog = {dogVirtualTable,"Ben"};
    animalPrintGreeting(&dog);
    animalPrintMenu(&dog);

    int n = 5;

    printf("\nCreating n dogs, where n is %d\n",n);

    createNDogs(n);
    return 0;
}

