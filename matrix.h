#include <iostream>
#include <stdio.h>      /* printf, scanf, puts, NULL */
#include <stdlib.h>     /* srand, rand */
#include <time.h> 
#include <fstream>
#include <string>
#include <chrono>
#include <math.h>

using namespace std;

void deleteMatrix(int **, size_t);
void initMatrix(int **, size_t);
int** createMatrix(size_t,int = -1);
void printMatrix(int **, size_t);
int** multiplyMatrix(int **,int **, size_t);
int** createAndInitMatrix(size_t);
int** cutMatrix(int **, int , int , size_t );
int** addMatrix(int **, int **,int **, size_t );
int** subMatrix(int **, int **,int **, size_t );
int** strassen(int **, int **, size_t);
int** strassenSeuil(int **, int **, size_t);
size_t readMatrix(string, int**);
size_t getSizeMatrix(string);
int*** readMatrixe2N(int, int);
int computeMoy(int ***, int, int, int** (*alg)(int **, int**, size_t ) );
void computeResults(int ,int , int );
int** strassenMultiply(int** , int** , size_t );
string getFileStr(int , int );