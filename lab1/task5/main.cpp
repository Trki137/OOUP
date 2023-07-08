#include <iostream>

class B{
public:
    virtual int __cdecl prva()=0;
    virtual int __cdecl druga(int)=0;
};

class D: public B{
public:
    virtual int __cdecl prva(){return 42;}
    virtual int __cdecl druga(int x){return prva()+x;}
};

void  printB(B *pb){
    printf("Adresa objekta: %p\n", pb);

    uintptr_t* virtualTable = reinterpret_cast<uintptr_t*>(*(uintptr_t*)pb);

    printf("Adresa virtualne tablice: %p\n",virtualTable);

    typedef int (*prvaVirtualna)(B*);
    typedef int (*drugaVirtualna)(B*, int);

    prvaVirtualna first = (prvaVirtualna)virtualTable[0];
    drugaVirtualna second = (drugaVirtualna)virtualTable[1];

    printf("Adresa virtualne metode prva: %p\n", first);
    printf("Adresa virtualne metode druga: %p\n", second);

    int firstResult = first(pb);
    int secondResult = second(pb, 22);

    printf("Prva: %d\n", firstResult);
    printf("Druga: %d\n", secondResult);

}

int main() {
    B *b = new D();

    printB(b);
    return 0;
}
