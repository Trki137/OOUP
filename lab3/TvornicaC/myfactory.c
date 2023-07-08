#include <stdio.h>
#include <windows.h>
#include <stdint.h>


void* myfactory(char const* libname, char const* ctorarg){
    char libPath[100];
    snprintf(libPath, sizeof(libPath), ".\\%s.dll", libname);
    HINSTANCE hmodule = LoadLibrary(libPath);
    if(hmodule == NULL){
        printf("Invalid file.\n");
        return NULL;
    }

    FARPROC construct = GetProcAddress(hmodule, "create");

    if(!construct){
        printf("Function create doesn't exist");
        return NULL;
    }


    return (void*)(intptr_t)(*construct)(ctorarg);
}
void* myFactoryStack(char const* libname, char const* ctorarg, void* memorySpace){
    char libPath[100];
    snprintf(libPath, sizeof(libPath), ".\\%s.dll", libname);
    HINSTANCE hmodule = LoadLibrary(libPath);
    if(hmodule == NULL){
        printf("Invalid file.\n");
        return NULL;
    }

    FARPROC construct = GetProcAddress(hmodule, "construct");
    FARPROC sizeOf = GetProcAddress(hmodule, "sizeOf");

    if(!construct){
        printf("Function construct doesn't exist");
        return NULL;
    }

    if(!sizeOf){
        printf("Function sizeOf doesn't exist");
        return NULL;
    }

    return (void*)(intptr_t)(*construct)(memorySpace,ctorarg);
}