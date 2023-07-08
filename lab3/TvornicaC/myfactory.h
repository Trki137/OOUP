#pragma once
#include <stdbool.h>
void* myfactory(char const* libname, char const* ctorarg);
void* myFactoryStack(char const* libname, char const* ctorarg, void* memorySpace);
