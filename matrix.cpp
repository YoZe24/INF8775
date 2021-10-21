#include "matrix.h"

using namespace std;

int main(int argc, char* argv[]){
    srand (time(NULL));
    int algorithm = atoi(argv[1]);
    string pathEx1 = argv[2];
    string pathEx2 = argv[3];

    int options[2] = {0,0};
    switch(argc) {
        case 5:
            if (argv[4][0] == 'p') 
                options[0] = 1;
            else
                options[1] = 1;
            break;
        case 6:
            options[0] = 1;
            options[1] = 1;
            break;
   } 
    
    size_t size = getSizeMatrix(pathEx1);
    
    int **a = createMatrix(size);
    int **b = createMatrix(size);
    size_t sizeA = readMatrix(pathEx1,a);
    size_t sizeB = readMatrix(pathEx2,b);

    int **result;
    
    auto start = std::chrono::high_resolution_clock::now();
    switch(algorithm) {
        case 1:
            result = multiplyMatrix(a,b,size);
            break;
        case 2:
            result = strassen(a,b,size);
            break;
        case 3:
            result = strassenSeuil(a,b,size);
            break;
    }
    auto end = std::chrono::high_resolution_clock::now();
    auto time = std::chrono::duration_cast<std::chrono::nanoseconds>(end-start).count();

    if(options[0] == 1)
        printMatrix(result,size);
    if(options[1] == 1)
        cout << time/1000000.0;    
    
    // computeResults(2,5,5);
    return 0;
}


size_t getSizeMatrix(string path){
    ifstream f(path);
    size_t n;
    f >> n;
    return pow(2,n);
}

int*** readMatrixe2N(int exp, int number){
    int size = pow(2,exp);

    int*** matrixes = new int**[number];
    for(int i = 0 ; i < number ; i++){
        int** x;
        readMatrix(getFileStr(exp,i),x);
        matrixes[i] = &x[0];
    }

    return matrixes;
}

int computeMoy(int ***matrixes, int exp, int number, int** (*alg)(int **a, int**b, size_t n_alg) ){
    int size = pow(2,exp);
    int cpt = 0;
    auto moy = 0;
    for(int i = 0 ; i < number-1; i++){
        for(int j = i+1 ; j < number ; j++){
            
            auto start = chrono::steady_clock::now();
            int **a = (*alg)(matrixes[i],matrixes[j],size);
            auto end = chrono::steady_clock::now();

            moy += chrono::duration_cast<chrono::milliseconds>(end - start).count();
            cpt++;

            deleteMatrix(a,size);
            cout << moy << ",";
        }
    }
    cout << endl << exp << " = " << moy/cpt << endl;
    return moy / cpt;
}

string getFileStr(int exp, int num){
    return "ex"+std::to_string(exp)+"_"+std::to_string(num);
}

size_t readMatrix(string fileName, int** m){
    ifstream MyReadFile(fileName);
    int exp = 0;

    MyReadFile >> exp;
    int n = pow(2,exp);
    for(int i = 0 ; i < n ; i++){
        for(int j = 0 ; j < n ; j++){
            MyReadFile >> m[i][j];
        }
    }

    MyReadFile.close();
    return n;
}

int** multiplyMatrix(int **a, int**b, size_t n){
    int **c = createMatrix(n);

    for(int i = 0 ; i < n ; i++){
        for(int j = 0 ; j < n ; j++){
            c[i][j] = 0;
            for(int k = 0 ; k < n ; k++){
                c[i][j] +=  a[i][k] * b[k][j];
            }
        }
    }

    return c;
}

int** addMatrix(int **a, int **b, int **c, size_t n){
    for(int i = 0; i < n ; i++){
        for(int j = 0 ; j < n ; j++){
            c[i][j] = a[i][j] + b[i][j];
        }
    }

    return c;
}

int** subMatrix(int **a, int **b, int **c, size_t n){
    for(int i = 0; i < n ; i++){
        for(int j = 0 ; j < n ; j++){
            c[i][j] = a[i][j] - b[i][j];
        }
    }

    return c;
}

int** cutMatrix(int **a, int r, int c, size_t n){
    int **sub = createMatrix(n);
    for(int i = r ; i < r+n ; i++){
        for(int j = c ; j < c+n ; j++){
            sub[i-r][j-c] = a[i][j];
        }
    }
    return sub;
}

int** strassen(int **a, int **b,  size_t n ){
    int **c = createMatrix(n);
    if(n == 1){
        c[0][0] = a[0][0] * b[0][0];
        return c;
    }else{
        int subn = n/2;
        int **a_12 = cutMatrix(a,0,subn,subn);
        int **a_22 = cutMatrix(a,subn,subn,subn);

        int **b_12 = cutMatrix(b,0,subn,subn);
        int **b_22 = cutMatrix(b,subn,subn,subn);

        int **a_res = createMatrix(subn);
        int **b_res = createMatrix(subn);

        addMatrix(a,a_22,a_res,subn);
        addMatrix(b,b_22,b_res,subn);
        int **m1 = strassen(a_res,b_res,subn);

        addMatrix(&a[subn],a_22,a_res,subn);
        int **m2 = strassen(a_res,b,subn);

        subMatrix(b_12,b_22,b_res,subn);
        int **m3 = strassen(a,b_res,subn);

        subMatrix(&b[subn],b,b_res,subn); 
        int **m4 = strassen(a_22,b_res,subn);
        
        addMatrix(a,a_12,a_res,subn);
        int **m5 = strassen(a_res,b_22,subn);

        subMatrix(&a[subn],a,a_res,subn);
        addMatrix(b,b_12,b_res,subn);
        int **m6 = strassen(a_res,b_res,subn);

        subMatrix(a_12,a_22,a_res,subn);
        addMatrix(&b[subn],b_22,b_res,subn);
        int **m7 = strassen(a_res,b_res,subn);

        for(int i = 0 ; i < subn ; i++){
            for(int j = 0 ; j < subn ; j++){
                c[i][j] = m1[i][j] + m4[i][j] - m5[i][j] + m7[i][j];
                c[i][j+subn] = m3[i][j] + m5[i][j];
                c[i+subn][j] = m2[i][j] + m4[i][j];
                c[i+subn][j+subn] = m1[i][j] - m2[i][j] + m3[i][j] + m6[i][i];
            }
        }

        deleteMatrix(a_res,subn);
        deleteMatrix(b_res,subn);

        deleteMatrix(a_12,subn);
        deleteMatrix(a_22,subn);
        deleteMatrix(b_12,subn);
        deleteMatrix(b_22,subn);

        deleteMatrix(m1,subn);
        deleteMatrix(m2,subn);
        deleteMatrix(m3,subn);
        deleteMatrix(m4,subn);
        deleteMatrix(m5,subn);
        deleteMatrix(m6,subn);
        deleteMatrix(m7,subn);
    }

    return c;
}

int** strassenSeuil(int **a, int **b, size_t n ){
    int** c = createMatrix(n);
    if(n <= 16){
        c = multiplyMatrix(a,b,n);
        return c;
    }else{
        int subn = n/2;
        int **a_12 = cutMatrix(a,0,subn,subn);
        int **a_22 = cutMatrix(a,subn,subn,subn);

        int **b_12 = cutMatrix(b,0,subn,subn);
        int **b_22 = cutMatrix(b,subn,subn,subn);

        int **a_res = createMatrix(subn);
        int **b_res = createMatrix(subn);

        addMatrix(a,a_22,a_res,subn);
        addMatrix(b,b_22,b_res,subn);
        int **m1 = strassenSeuil(a_res,b_res,subn);

        addMatrix(&a[subn],a_22,a_res,subn);
        int **m2 = strassenSeuil(a_res,b,subn);

        subMatrix(b_12,b_22,b_res,subn);
        int **m3 = strassenSeuil(a,b_res,subn);

        subMatrix(&b[subn],b,b_res,subn); 
        int **m4 = strassenSeuil(a_22,b_res,subn);
        
        addMatrix(a,a_12,a_res,subn);
        int **m5 = strassenSeuil(a_res,b_22,subn);

        subMatrix(&a[subn],a,a_res,subn);
        addMatrix(b,b_12,b_res,subn);
        int **m6 = strassenSeuil(a_res,b_res,subn);

        subMatrix(a_12,a_22,a_res,subn);
        addMatrix(&b[subn],b_22,b_res,subn);
        int **m7 = strassenSeuil(a_res,b_res,subn);
        
        for(int i = 0 ; i < subn ; i++){
            for(int j = 0 ; j < subn ; j++){
                c[i][j] = m1[i][j] + m4[i][j] - m5[i][j] + m7[i][j];
                c[i][j+subn] = m3[i][j] + m5[i][j];
                c[i+subn][j] = m2[i][j] + m4[i][j];
                c[i+subn][j+subn] = m1[i][j] - m2[i][j] + m3[i][j] + m6[i][i];
            }
        }

        deleteMatrix(a_res,subn);
        deleteMatrix(b_res,subn);

        deleteMatrix(a_12,subn);
        deleteMatrix(a_22,subn);
        deleteMatrix(b_12,subn);
        deleteMatrix(b_22,subn);
        
        deleteMatrix(m1,subn);
        deleteMatrix(m2,subn);
        deleteMatrix(m3,subn);
        deleteMatrix(m4,subn);
        deleteMatrix(m5,subn);
        deleteMatrix(m6,subn);
        deleteMatrix(m7,subn);
    }

    return c;
}

void deleteMatrix(int **matr, size_t n){
    for(int i = 0 ; i < n ; i++){
        delete[] matr[i];
    }
    delete[] matr;
}

int** createAndInitMatrix(size_t n){
    int **matr = createMatrix(n);
    initMatrix(matr,n);
    return matr;
}

void initMatrix(int **matr, size_t n){
    for(int i = 0 ; i < n ; i++){
        for(int j = 0 ; j < n ; j++){
            matr[i][j] = rand() % 100 + 1;
        }
    }
}

int** createMatrix(size_t n, int c){
    if(c == -1) c = n;

    int **matr = new int*[n];
    for(int i = 0 ; i < n ; i++){
        matr[i] = new int[c];
    }
    return matr;
}

void printMatrix(int **matr, size_t n){
    for(int i = 0 ; i < n ; i++){
        for(int j = 0; j < n ; j++){
            cout << matr[i][j] << " ";
        }
        cout << endl;
    }
}

void computeResults(int exp,int t, int n){
    int** results = new int*[t];

    for(int i = 0 ; i < 3 ; i++)
        results[i] = new int[n];
    
    for(int i = 0 ; i < t ; i++){
        int*** matrixes = readMatrixe2N(exp+i,n);

        cout << "Normal n^3 : ";
        results[0][i] = computeMoy(matrixes,exp+i,n,multiplyMatrix);
        cout << "Strassen : ";
        results[1][i] = computeMoy(matrixes,exp+i,n,strassen);
        cout << "Strassen seuil 16 : ";
        results[2][i] = computeMoy(matrixes,exp+i,n,strassenSeuil);
            
        for(int m = 0; m < n ; m++){
            deleteMatrix(matrixes[m],n);
        }
    }

    for(int i = 0 ; i < 3 ; i++){
        for(int j = 0 ; j < t ; j++){
            cout << results[i][j] << " ";
        }
        cout << endl;
    }
}
