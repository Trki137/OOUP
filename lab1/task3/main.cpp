#include <iostream>

class CoolClass {
public:
    virtual void set(int x) { x_ = x; };
    virtual int get(){return x_;};
private:
    int x_;
    int y_;
    char c;
};

class PlainOldClass {
public:
    void set(int x) { x_ = x; };
    int get() { return x_; };
private:
    int x_;
};

class A{
public:
    virtual void set(){};
    virtual void a(){};
    int d[3];
};

class EmptyClass{};

int main() {

    printf("%zu bytes\n",sizeof(A));
    printf("Size of CoolClass: %zu bytes\n", sizeof(CoolClass));
    printf("Size of PlainOldClass: %zu bytes\n", sizeof(PlainOldClass));
    printf("Size of EmptyClass: %zu bytes\n", sizeof(EmptyClass));
    printf("%zu\n", sizeof(int));
    std::cout << "Size of vtp: " << sizeof(void*) << " bytes\n";
    return 0;
}
