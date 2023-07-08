#include <iostream>
#include <cstring>
#include <vector>
#include <set>

using namespace std;

template <typename Iterator, typename Predicate> Iterator myMax(Iterator first, Iterator second, Predicate pred){
    if(first == second) return second;

    Iterator res = first;

    while(first != second){
        if(pred(*first,*res) > 0)
            res = first;

        ++first;
    }

    return res;
}

int gt_int(int a, int b){
    return a - b;
}

int gt_str(const char* a, const char* b){
    return strcmp(a,b);
}
int gt_char(char a, char b){
    return a - b;
}

int empty_arr[] = {};
int arr_int[] = { 21, 3, 5, 7, 4, 6, 9, 2, 0 };
const char* arr_str[] = {"Bok", "Cao","Hello", "Bonjorno", "Zivio", "a"};
int main(){
    const int* maxint = myMax( &arr_int[0],&arr_int[sizeof(arr_int)/sizeof(*arr_int)], &gt_int);
    cout <<*maxint <<"\n";

    const char** maxString = myMax(&arr_str[0], &arr_str[sizeof(arr_str)/sizeof(*arr_str)],&gt_str);
    cout << *maxString << "\n";

    const int* maxintEmpty = myMax( &empty_arr[0],&empty_arr[sizeof(empty_arr)/sizeof(*empty_arr)], &gt_int);
    if(maxintEmpty != &empty_arr[sizeof(empty_arr)/sizeof(*empty_arr)])
        cout <<*maxintEmpty <<"\n";
    else cout << "Array is empty" << "\n";

    vector<int> intVector{10,20,30,23,100};

    auto maxVectorNumber = myMax(intVector.begin(),intVector.end(),&gt_int);
    cout << *maxVectorNumber << "\n";

    set<char> charSet{'a','v','G'};

    auto maxCharFromSet = myMax(charSet.begin(),charSet.end(),&gt_char);
    cout << *maxCharFromSet << "\n";

}
