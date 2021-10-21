#include <iostream>
#include <ctime>
using namespace std;

void strassen(int **a, int **b, int **c, int tam);
void sum(int **a, int **b, int **result, int tam);
void subtract(int **a, int **b, int **result, int tam);
int  **allocate_matrix(int size);
void dealloc(int **matrix, int size); 

int main()
{
	for(int i = 0 ; i < 3; i++)
	{
		clock_t start = clock();

		int m = 512;
		int **A = new int*[m];
		int **B = new int*[m];
		int **product = new int*[m];

		A = allocate_matrix(m);
		B = allocate_matrix(m);
		product = allocate_matrix(m);

		for(int row = 0; row < m; row++) { 
			for(int inner = 0; inner < m; inner++){
				A[row][inner] = 5;
				//cout << A[row][inner] << " ";
			}
			//cout << "\n";
		}

		for(int row = 0; row < m; row++) { 
			for(int inner = 0; inner < m; inner++){
				B[row][inner] = 3;
				//cout << B[row][inner] << " ";
			}
			//cout << "\n";
		}

		/*for(int row = 0; row < m; row++) { 
		for(int inner = 0; inner < m; inner++){
		product[row][inner] = 0;
		cout << product[row][inner] << " ";
		}
		cout << "\n";
		}*/

		strassen (A,B,product,m);

		//for(int row = 0; row < m; row++) { 
		//	for(int inner = 0; inner < m; inner++){
		//		cout << product[row][inner] << " ";
		//	}
		//	cout << "\n";
		//}


		//deallocation
		dealloc(A, m);
		dealloc(B, m);
		dealloc(product, m);
		clock_t end = clock();
		double cpu_time = static_cast<double>(end - start)/CLOCKS_PER_SEC;


		cout << cpu_time << endl;
	}


	cout << "done" << endl;

	return 0;
}
void strassen(int **a, int **b, int **c, int tam) {

	// trivial case: when the matrix is 1 X 1:
	if (tam == 1) {
		c[0][0] = a[0][0] * b[0][0];
		return;
	}

	// other cases are treated here:
	int newTam = tam/2;
	int **a11, **a12, **a21, **a22;
	int **b11, **b12, **b21, **b22;
	int **c11, **c12, **c21, **c22;
	int **p1, **p2, **p3, **p4, **p5, **p6, **p7;

	// memory allocation:
	a11 = allocate_matrix(newTam);
	a12 = allocate_matrix(newTam);
	a21 = allocate_matrix(newTam);
	a22 = allocate_matrix(newTam);

	b11 = allocate_matrix(newTam);
	b12 = allocate_matrix(newTam);
	b21 = allocate_matrix(newTam);
	b22 = allocate_matrix(newTam);

	c11 = allocate_matrix(newTam);
	c12 = allocate_matrix(newTam);
	c21 = allocate_matrix(newTam);
	c22 = allocate_matrix(newTam);

	p1 = allocate_matrix(newTam);
	p2 = allocate_matrix(newTam);
	p3 = allocate_matrix(newTam);
	p4 = allocate_matrix(newTam);
	p5 = allocate_matrix(newTam);
	p6 = allocate_matrix(newTam);
	p7 = allocate_matrix(newTam);

	int **aResult = allocate_matrix(newTam);
	int **bResult = allocate_matrix(newTam);

	int i, j;

	//dividing the matrices in 4 sub-matrices:
	for (i = 0; i < newTam; i++) {
		for (j = 0; j < newTam; j++) {
			a11[i][j] = a[i][j];
			a12[i][j] = a[i][j + newTam];
			a21[i][j] = a[i + newTam][j];
			a22[i][j] = a[i + newTam][j + newTam];

			b11[i][j] = b[i][j];
			b12[i][j] = b[i][j + newTam];
			b21[i][j] = b[i + newTam][j];
			b22[i][j] = b[i + newTam][j + newTam];
		}
	}

	// Calculating p1 to p7:

	sum(a11, a22, aResult, newTam); // a11 + a22
	sum(b11, b22, bResult, newTam); // b11 + b22
	strassen(aResult, bResult, p1, newTam); // p1 = (a11+a22) * (b11+b22)

	sum(a21, a22, aResult, newTam); // a21 + a22
	strassen(aResult, b11, p2, newTam); // p2 = (a21+a22) * (b11)

	subtract(b12, b22, bResult, newTam); // b12 - b22
	strassen(a11, bResult, p3, newTam); // p3 = (a11) * (b12 - b22)

	subtract(b21, b11, bResult, newTam); // b21 - b11
	strassen(a22, bResult, p4, newTam); // p4 = (a22) * (b21 - b11)

	sum(a11, a12, aResult, newTam); // a11 + a12
	strassen(aResult, b22, p5, newTam); // p5 = (a11+a12) * (b22)   

	subtract(a21, a11, aResult, newTam); // a21 - a11
	sum(b11, b12, bResult, newTam); // b11 + b12
	strassen(aResult, bResult, p6, newTam); // p6 = (a21-a11) * (b11+b12)

	subtract(a12, a22, aResult, newTam); // a12 - a22
	sum(b21, b22, bResult, newTam); // b21 + b22
	strassen(aResult, bResult, p7, newTam); // p7 = (a12-a22) * (b21+b22)

	// calculating c21, c21, c11 e c22:

	sum(p3, p5, c12, newTam); // c12 = p3 + p5
	sum(p2, p4, c21, newTam); // c21 = p2 + p4

	sum(p1, p4, aResult, newTam); // p1 + p4
	sum(aResult, p7, bResult, newTam); // p1 + p4 + p7
	subtract(bResult, p5, c11, newTam); // c11 = p1 + p4 - p5 + p7

	sum(p1, p3, aResult, newTam); // p1 + p3
	sum(aResult, p6, bResult, newTam); // p1 + p3 + p6
	subtract(bResult, p2, c22, newTam); // c22 = p1 + p3 - p2 + p6

	// Grouping the results obtained in a single matrix:
	for (i = 0; i < newTam ; i++) {
		for (j = 0 ; j < newTam ; j++) {
			c[i][j] = c11[i][j];
			c[i][j + newTam] = c12[i][j];
			c[i + newTam][j] = c21[i][j];
			c[i + newTam][j + newTam] = c22[i][j];
		}
	}

	// deallocating memory (free):
	dealloc(a11, newTam);
	dealloc(a12, newTam);
	dealloc(a21, newTam);
	dealloc(a22, newTam);

	dealloc(b11, newTam);
	dealloc(b12, newTam);
	dealloc(b21, newTam);
	dealloc(b22, newTam);

	dealloc(c11, newTam);
	dealloc(c12, newTam);
	dealloc(c21, newTam);
	dealloc(c22, newTam);

	dealloc(p1, newTam);
	dealloc(p2, newTam);
	dealloc(p3, newTam);
	dealloc(p4, newTam);
	dealloc(p5, newTam);
	dealloc(p6, newTam);
	dealloc(p7, newTam);
	dealloc(aResult, newTam);
	dealloc(bResult, newTam);

} // end of Strassen function

void sum(int **a, int **b, int **result, int tam) {
	int i, j;
	for (i = 0; i < tam; i++) {
		for (j = 0; j < tam; j++) {
			result[i][j] = a[i][j] + b[i][j];
		}
	}
}

void subtract(int **a, int **b, int **result, int tam) {
	int i, j;
	for (i = 0; i < tam; i++) {
		for (j = 0; j < tam; j++) {
			result[i][j] = a[i][j] - b[i][j];
		}
	}   
}
int **allocate_matrix(int size) {
	int **temp = new int*[size];
	for ( int i = 0 ; i < size ; i++)
	{
		temp[i] = new int[size];
	}
	return (temp); 
} 
void dealloc (int **matrix,int size) {
	if (matrix == NULL) {
		return;
	}
	for ( int i = 0 ; i < size ; i++)
	{
		delete[] matrix[i];
	}
	delete[] matrix; 
}