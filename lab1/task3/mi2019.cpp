#include <iostream>

class Unary_Function {
public:
    virtual int value_at(int x) = 0;;
    virtual int neg_value_at(int x){return -x;}
};
class Identify : public Unary_Function {
public:
    int value_at(int x) {return x*2;}
};

class Square{
public:
    int value_at(int x) {return x;};
    int neg_value_at(int x) {return -x;}
};


int main() {
    //printf("%llu\n", sizeof(Square));
    Square *f1 = new Square();
    f1->neg_value_at(5);
    Identify *f2 = new Identify();
    f2->neg_value_at(2);
}