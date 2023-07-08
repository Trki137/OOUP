#include <stdio.h>
#include <stdlib.h>

typedef struct Unary_Function Unary_Function;
typedef struct Square Square;
typedef struct Linear Linear;

typedef double (*PTRFUN)(Unary_Function *this,double x);

struct Unary_Function {
    PTRFUN *table;

    int _lower_bound;
    int _upper_bound;

    void (*tabulate)(Unary_Function *this);
};

struct Square{
    Unary_Function base;
};

struct Linear{
    Unary_Function base;

    double _a;
    double _b;
};

double linear_value_at(Unary_Function *this, double x){
    Linear *linear = (Linear*) this;
    return linear->_a * x + linear->_b;
}

double square_value_at(Unary_Function *this, double x){
    return x*x;
}

double negative_value_at(Unary_Function *this, double x){
    return -this->table[0](this,x);
}

void tabulate(Unary_Function *this){
    for(int x = this->_lower_bound; x <= this->_upper_bound; x++){
        printf("f(%d)=%lf\n",x,this->table[0](this,x));
    }
}

int same_functions_for_ints(Unary_Function *f1, Unary_Function *f2, double tolerance){
    if(f1->_lower_bound != f2->_lower_bound) return 0;
    if(f1->_upper_bound != f2->_upper_bound) return 0;

    for(int x = f1->_lower_bound; x <= f1->_upper_bound; x++){
        double delta = f1->table[0](f1,x) - f1->table[0](f2,x);
        if(delta < 0) delta = -delta;
        if(delta > tolerance) return 0;
    }

    return 1;
}

PTRFUN lineaVirtualTable[] =  {linear_value_at, negative_value_at};
PTRFUN squareVirtualTable[] = {square_value_at, negative_value_at};
PTRFUN unaryVirtualTable[] =  {NULL, negative_value_at};


Unary_Function *createUnaryFunction(int lb, int ub){
    Unary_Function *unaryFunction = malloc(sizeof(Unary_Function));

    unaryFunction->table = unaryVirtualTable;
    unaryFunction->_lower_bound = lb;
    unaryFunction->_upper_bound = ub;
    unaryFunction->tabulate = &tabulate;

    return unaryFunction;
}
Square *createSquare(int lb, int ub){
    Square *square = malloc(sizeof(Square));

    square->base = *createUnaryFunction(lb,ub);
    square->base.table = squareVirtualTable;

    return square;
}

Linear *createLinear(int lb,int ub, double a, double b){
    Linear *linear = malloc(sizeof(Linear));

    linear->base = *createUnaryFunction(lb,ub);
    linear->base.table = lineaVirtualTable;
    linear->_a = a;
    linear->_b = b;

    return linear;
}

int main() {
    Unary_Function *f1 = (Unary_Function *) createSquare(-2,2);
    f1->tabulate(f1);

    Unary_Function *f2 = (Unary_Function *) createLinear(-2,2,5,-2);
    f2->tabulate(f2);

    printf("f1==f2: %s\n", same_functions_for_ints(f1,f2,1e-6) ? "DA" : "NE");
    printf("neg_val f2(1) = %lf\n", f2->table[1](f2,1.0));

    free(f1);
    free(f2);
    return 0;
}
