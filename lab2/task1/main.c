#include <stdio.h>
#include<stdlib.h>
#include<string.h>

typedef int (*COMPARE)(const void *, const void *);



int gt_int(const void* a, const void* b){
    int num1 = *((int*)a);
    int num2 = *((int*)b);

    if(num1 > num2) return 1;
    return 0;
}

int gt_char(const void* a, const void* b){
    char a1 = *(char *)a;
    char b1 = *(char *)b;
    return a1 - b1;
}

int gt_str(const void* a, const void *b){
    char* a1 =*(char **)a;
    char* b1 = *(char **)b;
    return strcmp(a1,b1);
}

const COMPARE intCompare = gt_int;
const COMPARE charCompare = gt_char;
const COMPARE stringCompare = gt_str;

const void *myMax(
        const void *base,
        size_t nmemb,
        size_t size,
        int(*compare)(const void *, const void *)
){
    size_t i = 0;
    size_t j = nmemb - 1;
    char *qi = (char *) base;
    char *res = NULL;
    while(i < j){
        if(res == NULL || compare(qi,res) > 0) res = qi;
        i++;
        qi = qi + size;
    }


    return res;
}

int main() {
    int arr_int[] = {1,3,5,10,4,6,9,2,0};
    char arr_char[]="Suncana strana ulice";
    const char* arr_str[] = {
            "Gle", "malu", "vocku", "poslije", "kise",
            "Puna", "je", "kapi", "pa", "ih", "njise"
    };

    int sizeOfIntArray = sizeof(arr_int) / sizeof(arr_int[0]);
    int sizeOfCharArray = sizeof(arr_char)/sizeof(arr_char[0]);
    int sizeOfStringArray = sizeof(arr_str)/ sizeof(arr_str[0]);

    int *int_res =(int*)myMax(arr_int,sizeOfIntArray,sizeof(int),intCompare);
    char *char_res = (char*)myMax(arr_char,sizeOfCharArray,sizeof(char),charCompare);
    const char* str_res =*(const char **)myMax(arr_str,sizeOfStringArray,sizeof(arr_str[0]),stringCompare);

    printf("Biggest element in arr_int is: %d\n", *int_res);
    printf("Biggest element in arr_char is: %c\n", *char_res);
    printf("Biggest element in arr_str is: %s", str_res);

    return 0;
}
