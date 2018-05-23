/**
 * A Matrix object with many functions that perform standard matrix operations.
 * Utilizes a fraction object to store entries, which can be converted to doubles for output.
 *
 * @author Connor Pickles
 * @version 1.7.0
 * @since 2017-04-21
 */

import java.util.ArrayList;

public class Matrix {
	protected Fraction [][] m = null;
	protected static boolean showSteps = false;
	protected boolean displayDoubles = false;
	protected boolean calledRREF = false;
	protected Matrix L = null;
	protected Matrix U = null;
	protected Matrix inverse = null;
	protected Matrix T = null;

	/**
	 * Default constructor.
	 */
	Matrix () {
		m = new Fraction [2][2];
	} // Matrix

	/**
	 * Constructor.
	 * @param input 2D fraction array
	 */
	Matrix (Fraction [][] input) {
		m = new Fraction [input.length][input[0].length];

		for (int i = 0; i < input.length; i++) {
			for (int j = 0; j < input[0].length; j++) {
				m[i][j] = input[i][j];
			} // for j
		} // for i
	} // Matrix

	/**
	 * Convert matrix to Row Echelon Form. This method serves as a driver for the actual calculations.
	 * @return matrix in REF
	 */
	public Matrix convertToREF() {
		if (showSteps) {
			System.out.println("Converting to REF:\n");
		} // if
		return convertToREFCalculations(this);
	} // convertToREF

	/**
	 * Recursive method that converts a matrix to Row Echelon Form.
	 * @param matrix
	 * @return matrix in REF
	 */
	private Matrix convertToREFCalculations(Matrix matrix) {
		if ((matrix.numRows() == 0 || matrix.numCols() == 0) || matrix.isZero() || matrix.inREF()) {
			if (showSteps) {
				System.out.println("No changes can be made to this matrix; converting is complete.\n");
			} // if
			return matrix;
		} else {
			int col = 0;
			int startingRow = 0;
			int startingCol = 0;

			// ignore 0 columns
			while (matrix.isZeroCol(col)) {
				col++;
			} // while

			startingCol = col;

			// ensure the first row starts with a non-zero number
			// if the matrix is being converted to RREF, 0 rows can be ignored
			if (matrix.getFrac(0, col).getNumerator() == 0 && !calledRREF) {
				for (int i = 1; i < matrix.numRows(); i++) {
					if (matrix.getFrac(i, col).getNumerator() != 0) {
						matrix.swapRows(0, i);
						break;
					} // if
				} // for
				if (showSteps) {
					System.out.println("Swap rows:");
					System.out.println(matrix);
				} // if
			} else if (matrix.getFrac(0, col).getNumerator() == 0 && calledRREF) {
				while (startingRow < matrix.numRows() && matrix.getFrac(startingRow, col).getNumerator() == 0) {
					startingRow++;
					startingCol++;
				} // while
			} // else if

			if (showSteps) {
				System.out.println("Begin cancelling leading entries:");
			} // if

			// cancels the leading entry of all rows except the first
			for (int i = startingRow + 1; i < matrix.numRows(); i++) {
				matrix.cancelLeadingEntry(i, col, startingRow, startingCol);
			} // for i

			if (showSteps) {
				System.out.println("Converted rows:");
				System.out.println(matrix);
			} // if

			// copy converted matrix to a (m-1)(n-1) sized matrix
			Matrix temp = matrix.subMatrix(1, matrix.numRows() - 1, 1, matrix.numCols() - 1);

			if (showSteps) {
				System.out.println("Working on new sub-matrix:");
				System.out.println(temp);
			} // if

			// convert the smaller matrix to REF and merge with original
			return insertMatrix(matrix, convertToREFCalculations(temp));
		} // else
	} // convertToREFCalculations

	/**
	 * Cancels the leading entry in a row by adding a multiple of a different row.
	 * @param row the row to cancel
	 * @param col the column location of the first entry in the row
	 * @param cofRow the row location of the entry that will be used to create the coefficient
	 * @param cofCol the col location of the entry that will be used to create the coefficient
	 */
	private void cancelLeadingEntry(int row, int col, int cofRow, int cofCol) {
		Fraction negative = new Fraction(-1, 1);

		// m[row][cofCol] / m[cofRow][cofCol]
		Fraction coefficient = new Fraction((m[row][cofCol].getNumerator() * m[cofRow][cofCol].getDenominator()), (m[row][cofCol].getDenominator() * m[cofRow][cofCol].getNumerator()));

		if (showSteps) {
			System.out.println("Calculating coefficient: " + (m[row][cofCol].getNumerator() + "*" + m[cofRow][cofCol].getDenominator()) + "/" + (m[row][cofCol].getDenominator() + "*" + m[cofRow][cofCol].getNumerator()) + " = " + coefficient);
		} // if

		// Ri = Ri - (m[i][j]/m[cofRow][j])(Coefficient Row)
		for (int j = col; j < m[0].length; j++) {
			m[row][j] = m[row][j].add((negative.multiply(coefficient)).multiply(m[cofRow][j]));
			if (showSteps) {
				System.out.print("Row " + row + ", Col " + j + ": " + m[row][j] + " - (" + coefficient + ")(" + m[cofRow][j] + ") = ");
				System.out.println(m[row][j]);
			} // if
		} // for j
		if (showSteps) {
			System.out.println();
		} // if
	} // cancelLeadingEntry

	/**
	 * Convert matrix to Row Echelon Form and save each step as an elementary matrix.
	 * This method serves as a driver for the actual calculations.
	 * @return matrix in REF
	 */
	private void convertToREFSaveCof(ArrayList<ElementaryMatrix> steps) {
		if (showSteps) {
			System.out.println("Converting to REF:\n");
		} // if
		convertToREFCalculationsSaveCof(this, steps);
	} // convertToREF

	/**
	 * Recursive method that converts a matrix to Row Echelon Form while saving each step as an elementary matrix.
	 * @param matrix
	 * @return matrix in REF
	 */
	private Matrix convertToREFCalculationsSaveCof(Matrix matrix, ArrayList<ElementaryMatrix> steps) {
		if ((matrix.numRows() == 0 || matrix.numCols() == 0) || matrix.isZero() || matrix.inREF()) {
			if (showSteps) {
				System.out.println("No changes can be made to this matrix; converting is complete.\n");
			} // if
			return matrix;
		} else {
			int col = 0;
			int startingRow = 0;
			int startingCol = 0;

			// ignore 0 columns
			while (matrix.isZeroCol(col)) {
				col++;
			} // while

			startingCol = col;

			// ensure the first row starts with a non-zero number
			// if the matrix is being converted to RREF, 0 rows can be ignored
			if (matrix.getFrac(0, col).getNumerator() == 0 && !calledRREF) {
				for (int i = 1; i < matrix.numRows(); i++) {
					if (matrix.getFrac(i, col).getNumerator() != 0) {
						matrix.swapRows(0, i);
						break;
					} // if
				} // for
				if (showSteps) {
					System.out.println("Swap rows:");
					System.out.println(matrix);
				} // if
			} else if (matrix.getFrac(0, col).getNumerator() == 0 && calledRREF) {
				while (startingRow < matrix.numRows() && matrix.getFrac(startingRow, col).getNumerator() == 0) {
					startingRow++;
					startingCol++;
				} // while
			} // else if

			if (showSteps) {
				System.out.println("Begin cancelling leading entries:");
			} // if

			// cancels the leading entry of all rows except the first
			for (int i = startingRow + 1; i < matrix.numRows(); i++) {
				matrix.cancelLeadingEntrySaveCof(i, col, startingRow, startingCol, steps);
			} // for i

			if (showSteps) {
				System.out.println("Converted rows:");
				System.out.println(matrix);
			} // if

			// copy converted matrix to a (m-1)(n-1) sized matrix
			Matrix temp = matrix.subMatrix(1, matrix.numRows() - 1, 1, matrix.numCols() - 1);

			if (showSteps) {
				System.out.println("Working on new sub-matrix:");
				System.out.println(temp);
			} // if

			// convert the smaller matrix to REF and merge with original
			return insertMatrix(matrix, convertToREFCalculationsSaveCof(temp, steps));
		} // else
	} // convertToREFCalculationsSaveCof

	/**
	 * Cancels the leading entry in a row by adding a multiple of a different row.
	 * Saves the operation as an elementary matrix.
	 * @param row the row to cancel
	 * @param col the column location of the first entry in the row
	 * @param cofRow the row location of the entry that will be used to create the coefficient
	 * @param cofCol the col location of the entry that will be used to create the coefficient
	 */
	private void cancelLeadingEntrySaveCof(int row, int col, int cofRow, int cofCol, ArrayList<ElementaryMatrix> steps) {
		Fraction negative = new Fraction(-1);

		// m[row][cofCol] / m[cofRow][cofCol]
		Fraction coefficient = new Fraction((m[row][cofCol].getNumerator() * m[cofRow][cofCol].getDenominator()), (m[row][cofCol].getDenominator() * m[cofRow][cofCol].getNumerator()));

		// save the coefficient
		ElementaryMatrix temp = new ElementaryMatrix();
		temp = createIdentity(m.length);
		temp.setFrac(row, col, negative.multiply(coefficient));
		steps.add(temp);

		if (showSteps) {
			System.out.println("Calculating coefficient: " + (m[row][cofCol].getNumerator() + "*" + m[cofRow][cofCol].getDenominator()) + "/" + (m[row][cofCol].getDenominator() + "*" + m[cofRow][cofCol].getNumerator()) + " = " + coefficient);
		} // if

		// Ri = Ri - (m[i][j]/m[cofRow][j])(Coefficient Row)
		for (int j = col; j < m[0].length; j++) {
			m[row][j] = m[row][j].add((negative.multiply(coefficient)).multiply(m[cofRow][j]));
			if (showSteps) {
				System.out.print("Row " + row + ", Col " + j + ": " + m[row][j] + " - (" + coefficient + ")(" + m[cofRow][j] + ") = ");
				System.out.println(m[row][j]);
			} // if
		} // for j
		if (showSteps) {
			System.out.println();
		} // if
	} // cancelLeadingEntrySaveCof

	/**
	 * Converts a matrix to Reduced Row Echelon Form.
	 * @return matrix in RREF
	 */
	public Matrix convertToReducedREF() {
		calledRREF = true;
		if (showSteps) {
			System.out.println("Converting to Reduced Row Echelon Form:\n");
		} // if

		if (!this.inREF()) {
			this.convertToREF();
		} // if
		this.rotateMatrix();
		this.convertToREF();
		this.rotateMatrix();
		makeLeadingEntriesOne();

		// ensures all columns with a leading 1 have no other values
		for (int i = 0; i < m.length && i < m[0].length; i++) {
			if (m[i][i].getDouble() == 1) {
				for (int j = i - 1; j >= 0; j--) {
					if (m[j][i].getDouble() != 0) {
						cancelLeadingEntry(j, i, i, i);
					} // if
				} // for j
			} // if
		} // for i

		calledRREF = false;
		return this;
	} // convertToReducedREF

	/**
	 * Multiplies two matrices together. This matrix is multiplied on the left, the given matrix is multiplied on the right.
	 * @param m right multiplied matrix
	 * @return A new matrix
	 */
	public Matrix multiply(Matrix m) {
		Matrix output = new Matrix(new Fraction[this.numRows()][m.numCols()]);

		if (this.numCols() != m.numRows()) {
			System.out.println("error: cannot multiply a " + this.numRows() + "x" + this.numCols() + " matrix by a " + m.numRows() + "x" + m.numCols() + " matrix");
			System.exit(0);
		} // if
		
		if (showSteps) {
			System.out.println("Multiply:\n" + this + "\nby\n" + m);
			System.out.println();
		} // if
		
		for (int i = 0; i < output.numRows(); i++) {
			for (int j = 0; j < output.numCols(); j++) {
				output.setFrac(i, j, dotProduct(this, i, m, j));
				if (showSteps) {
					System.out.println("Dot product of row " + i + " and column " + j + ": " + output.getFrac(i, j));
				} // if
			} // for j
		} // for i

		return output;
	} // multiply

	/**
	 * Calculates the LU decomposition of the matrix.
	 */
	public void findLUDecomposition() {
		if (!this.isSquare()) {
			System.out.println("error: LU decomposition must be performed on a square matrix");
			System.exit(0);
		} else if (this.rank() != this.numRows()) {
			System.out.println("error: LU decomposition must be performed on a matrix with rank = n");
			System.exit(0);
		} else {
			L = createIdentity(this.numRows());
			U = this.copy();
			ArrayList<ElementaryMatrix> steps = new ArrayList<ElementaryMatrix>();
			
			if (showSteps) {
				System.out.println("Finding LU decomposition of:\n" + this);
			} // if

			// calculates U, saving the elementary matrices used
			U.convertToREFSaveCof(steps);
			
			if (showSteps) {
				System.out.println("Finding U while saving row operations as elementary matrices:\n" + U);
				System.out.println("Inverting elementary matrices");
			} // if

			// invert the elementary matrices
			for (int i = 0; i < steps.size(); i++) {
				steps.get(i).invert();
			} // for i

			// calculate L
			for (int i = steps.size() - 1; i >= 0; i--) {
				L = L.multiply(steps.get(i));
			} // for
			
			if (showSteps) {
				System.out.println("Multiplying elementary matrices together to get L:\n" + L);
			} // if
		} // else
	} // findLUDecomposition

	/**
	 * Finds the determinate of a matrix. Uses LU decomposition and the fact that det(AB) = (detA)(detB).
	 * If the matrix has zero rows, automatically returns 0.
	 * @return the determinate of the given matrix
	 */
	public Fraction det() {
		if (this.numRows() != this.numCols()) {
			System.out.println("error: cannot get the determinate of a matrix that is not square");
			System.exit(0);
			return null;
		} else if (this.rank() != this.numRows()) {
			if (showSteps) {
				System.out.println("det = 0 because rank < number of rows");
			} // if
			return new Fraction(0);
		} else {
			Fraction output = new Fraction(1);

			if (this.isLowerTriangular() || this.isUpperTriangular()) {
				for (int i = 0; i < this.numRows(); i++) {
					output = output.multiply(this.getFrac(i, i));
				} // for i
			} else {
				if (this.getL() == null) {
					this.findLUDecomposition();
				} // if
				output = output.multiply(this.getL().det());
				output = output.multiply(this.getU().det());
				if (showSteps) {
					System.out.println("det(L) = " + this.getL().det());
					System.out.println("det(U) = " + this.getU().det());
					System.out.println("det(L) * det(U) = " + output);
				} // if
			} // else
			return output;
		} // else
	} // det

	/**
	 * Finds the inverse of the matrix.
	 * Augments the identity then row reduces to find the inverse.
	 * Does not alter the matrix object.
	 * @return The inverse of the matrix
	 */
	public Matrix findInverse() {
		if (this.det().getNumerator() == 0) {
			System.out.println("This matrix is not invertible.");
			System.exit(0);
		} // if

		Fraction[][] input = new Fraction[this.numRows()][this.numCols() * 2];

		for (int i = 0; i < this.numRows(); i++) {
			for (int j = 0; j < this.numCols(); j++) {
				input[i][j] = this.getFrac(i, j);
				if (i == j) {
					input[i][j + this.numCols()] = new Fraction(1);
				} else {
					input[i][j + this.numCols()] = new Fraction(0);
				}
			} // for j
		} // for i

		Matrix temp = new Matrix(input);
		temp.convertToReducedREF();
		Matrix output = new Matrix(new Fraction[this.numRows()][this.numCols()]);
		for (int i = 0; i < this.numRows(); i++) {
			for (int j = 0; j < this.numCols(); j++) {
				output.setFrac(i, j, temp.getFrac(i, j + this.numCols()));
			} // for j
		} // for i

		inverse = output;
		return output;
	} // findInverse
	
	/**
	 * Finds the transpose of this matrix.
	 * @return the transpose of this matrix
	 */
	public Matrix findTranspose() {
		T = new Matrix(new Fraction[this.numCols()][this.numRows()]);
		
		for (int i = 0; i < this.numRows(); i++) {
			for (int j = 0; j < this.numCols(); j++) {
				T.setFrac(j, i, this.getFrac(i, j));
			} // for j
		} // for i
		
		return T;
	} // findTranspose

	/**
	 * Rotates a matrix 180 degrees.
	 */
	private void rotateMatrix() {
		if (showSteps) {
			System.out.println("Rotate matrix:");
		} // if

		// reverse the entries of each row
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[0].length / 2; j++) {
				swapEntries(i, j, i, m[0].length - 1 - j);
			} // for j
		} // for i

		// swap opposite rows
		for (int i = 0; i < m.length / 2; i++) {
			swapRows(i, m.length - 1 - i);
		} // for i

		if (showSteps) {
			System.out.println(this);
		} // if
	} // rotateMatrix

	/**
	 * Returns a submatrix. Row and column values are inclusive:
	 * 0 <= row < numRows and 0 <= col < numCols
	 * @param firstRow
	 * @param lastRow
	 * @param firstCol
	 * @param lastCol
	 * @return a submatrix of the matrix
	 */
	protected Matrix subMatrix(int firstRow, int lastRow, int firstCol, int lastCol) {
		Matrix output = new Matrix();
		
		if (this instanceof AugmentedMatrix) {
			int newAugIndex = -1;
			if (((AugmentedMatrix)this).getAugIndex() <= lastCol) {
				newAugIndex = ((AugmentedMatrix)this).getAugIndex() - firstCol;
			} // if
			output = new AugmentedMatrix(new Fraction[lastRow - firstRow + 1][lastCol - firstCol + 1], newAugIndex);
		} else {
			output = new Matrix(new Fraction[lastRow - firstRow + 1][lastCol - firstCol + 1]);
		} // else

		for (int i = firstRow; i <= lastRow; i++) {
			for (int j = firstCol; j <= lastCol; j++) {
				output.setFrac(i - firstRow, j - firstCol, this.getFrac(i, j));
			} // for j
		} // for i

		return output;
	} // subMatrix

	/**
	 * Takes a mxn matrix and inserts a (m-1)x(n-1) matrix in the bottom right corner.
	 * @param big the big matrix
	 * @param small the small matrix
	 * @return the larger matrix with the smaller matrix inserted in the bottom right corner
	 */
	private static Matrix insertMatrix(Matrix big, Matrix small) {
		for (int i = 0; i < small.numRows(); i++) {
			for (int j = 0; j < small.numCols(); j++) {
				big.setFrac(i + 1, j + 1, small.getFrac(i, j));
			} // for j
		} // for i

		return big;
	} // insertMatrix

	/**
	 * Finds the dot product of a given row and column of two matrices. Used to multiply two matrices.
	 * @param m1
	 * @param row
	 * @param m2
	 * @param col
	 * @return dot product of the matrices
	 */
	private static Fraction dotProduct(Matrix m1, int row, Matrix m2, int col) {
		Fraction output = new Fraction();

		for (int i = 0; i < m1.numRows(); i++) {
			output = output.add(m1.getFrac(row, i).multiply(m2.getFrac(i, col)));
		} // for i

		return output;
	} // Fraction

	/**
	 * Makes all leading entries in a matrix 1.
	 */
	private void makeLeadingEntriesOne() {
		if (showSteps) {
			System.out.println("Make leading entries 1:");
		} // if

		for (int i = 0; i < m.length; i++) {
			int j = 0;
			while (j < m[0].length && m[i][j].getNumerator() == 0) {
				j++;
			} // while

			if (j != m[0].length) {
				Fraction coefficient = new Fraction(m[i][j].getDenominator(), m[i][j].getNumerator());

				for (; j < m[0].length; j++) {
					m[i][j] = m[i][j].multiply(coefficient);
				} // for j
			} // if
		} // for i


		if (showSteps) {
			System.out.println(this);
		} // if
	} // makeLeadingEntriesOne

	/**
	 * Swaps two rows in a matrix.
	 * @param row1
	 * @param row2
	 */
	private void swapRows(int row1, int row2) {
		Fraction temp = new Fraction(0, 0);

		for (int j = 0; j < m[0].length; j++) {
			temp = m[row1][j];
			m[row1][j] = m[row2][j];
			m[row2][j] = temp;
		} // for j
	} // swapRows

	/**
	 * Swaps two entries in a matrix.
	 * @param row1
	 * @param col1
	 * @param row2
	 * @param col2
	 */
	private void swapEntries(int row1, int col1, int row2, int col2) {
		Fraction temp = new Fraction(0, 0);

		temp = m[row1][col1];
		m[row1][col1] = m[row2][col2];
		m[row2][col2] = temp;
	} // swapEntries

	/**
	 * Checks if a column contains only 0 entries.
	 * @param col
	 * @return true if the column contains only 0 entries, false otherwise
	 */
	private boolean isZeroCol(int col) {
		boolean output = true;

		for (int i = 0; i < m.length; i++) {
			if (m[i][col].getNumerator() != 0) {
				output = false;
			} // if
		} // for i

		return output;
	} // isZeroCol

	/**
	 * Checks if a row contains only 0 entries.
	 * @param row
	 * @return true if the row contains only 0 entries, false otherwise
	 */
	private boolean isZeroRow(int row) {
		boolean output = true;

		for (int j = 0; j < m[0].length; j++) {
			if (m[row][j].getNumerator() != 0) {
				output = false;
			} // if
		} // int j

		return output;
	} // isZeroRow

	/**
	 * Copies the matrix into a new matrix.
	 * @return a copy of the matrix
	 */
	public Matrix copy() {
		Matrix output = new Matrix(new Fraction[this.numRows()][this.numCols()]);

		for (int i = 0; i < output.numRows(); i++) {
			for (int j = 0; j < output.numCols(); j++) {
				output.setFrac(i, j, this.getFrac(i, j));
			} // for j
		} // for i

		return output;
	} // copy

	/**
	 * Creates an identity elementary matrix of size nxn.
	 * @param n dimension of identity
	 * @return the identity matrix
	 */
	public static ElementaryMatrix createIdentity(int n) {
		Fraction [][] temp = new Fraction[n][n];

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (i == j) {
					temp[i][j] = new Fraction(1);
				} else {
					temp[i][j] = new Fraction(0);
				} // else
			} // for j
		} // for i

		return new ElementaryMatrix(temp);
	} // createIdentity

	/**
	 * Finds the rank of the matrix.
	 * @return rank of the matrix
	 */
	public int rank() {
		int output = this.numRows();
		Matrix temp = this.copy();

		if (temp instanceof AugmentedMatrix && ((AugmentedMatrix)temp).getAugIndex() >= 1) {
			output = (temp.subMatrix(0, temp.numRows() - 1, 0, ((AugmentedMatrix)temp).getAugIndex() - 1)).rank();
		} // if

		if (!temp.inREF()) {
			temp.convertToREF();
		} // if

		for (int i = 0; i < m.length; i++) {
			if (temp.isZeroRow(i)) {
				output--;
			} // if
		} // for i

		return output;
	} // rank

	/**
	 * Finds the nullity of the matrix. Uses rank(A) + nullity(A) = n, for a mxn matrix.
	 * @return nullity of the matrix
	 */
	public int nullity() {
		return this.numCols() - this.rank();
	} // nullity

	/**
	 * Determines if a matrix is in Row Echelon Form.
	 * @return true if in REF, false otherwise
	 */
	public boolean inREF() {
		boolean output = true;
		boolean [] zeroRows = new boolean [m.length];

		// check first condition: if all entries are 0, this row is at the bottom
		for (int i = 0; i < m.length; i++) {
			zeroRows[i] = true;
			for (int j = 0; j < m[0].length; j++) {
				if (m[i][j].getNumerator() != 0) {
					zeroRows[i] = false;
				} // if
			} // for j
		} // for i

		for (int i = 0; i < zeroRows.length; i++) {
			if (zeroRows[i]) {
				for (int j = ++i; j < zeroRows.length; j++) {
					if (!zeroRows[j]) {
						output = false;
					} // if
				} // for j
			} // if
		} // for i

		// check second condition: upper leading entries are always to the left of lower leading entries
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[0].length; j++) {
				if (m[i][j].getNumerator() != 0) {
					for (int k = i + 1; k < m.length; k++) {
						if (m[k][j].getNumerator() != 0) {
							output = false;
						} // if
					} // for k
					break;
				} // if
			} // for j
		} // for i

		return output;
	} // inREF

	/**
	 * Determines if a matrix is in Reduced Row Echelon Form.
	 * @return true if in RREF, false otherwise
	 */
	public boolean inRREF() {
		boolean output = this.inREF();

		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[0].length; j++) {
				if (m[i][j].getNumerator() != 0) {
					if (m[i][j].getNumerator() != 1) {
						output = false;
					} // if
					for (int k = i - 1; k >= 0; k--) {
						if (m[k][j].getNumerator() != 0) {
							output = false;
						} // if
					} // for k
					break;
				} // if
			} // for j
		} // for i

		return output;
	} // inRREF

	/**
	 * Checks if the matrix is the zero matrix.
	 * @return true if the matrix is the zero matrix, false otherwise
	 */
	public boolean isZero() {
		boolean output = true;

		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[0].length; j++) {
				if (m[i][j].getNumerator() != 0) {
					output = false;
				} // if
			} // for j
		} // for i

		return output;
	} // isZero

	/**
	 * Checks if the matrix is the identity matrix.
	 * @return true if the matrix is the identity matrix, false otherwise
	 */
	public boolean isIdentity() {
		boolean output = true;

		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[0].length; j++) {
				if (i != j && m[i][j].getNumerator() != 0) {
					output = false;
				} else if (i == j && m[i][j].getNumerator() != 1) {
					output = false;
				} // else if
			} // for j
		} // for i

		return output;
	} // isIdentity

	/**
	 * Determines if a matrix is an elementary matrix.
	 * @return true if elementary, false otherwise
	 */
	public boolean isElementary() {
		boolean output = false;
		boolean changeFound = false;

		if (!this.isSquare()) {
			return false;
		} else if (this.isIdentity()) {
			return true;
		} // else if

		// see if two rows have been swapped
		for (int i = 0; i < this.numRows() - 1; i++) {
			for (int j = i + 1; j < this.numCols(); j++) {
				this.swapRows(i, j);
				if (this.isIdentity()) {
					if (!changeFound) {
						changeFound = true;
						output = true;
					} else {
						this.swapRows(i, j);
						return false;
					} // else
				} // if
				this.swapRows(i, j);
			} // for j
		} // for i

		if (output) {
			return true;
		} else {
			output = true;
			changeFound = false;
		} // else

		// see if a row has been multiplied or been added to another row
		for (int i = 0; i < this.numRows(); i++) {
			for (int j = 0; j < this.numCols(); j++) {
				if (i == j) {
					if (m[i][j].getNumerator() != 1) {
						if (!changeFound) {
							changeFound = true;
						} else {
							output = false;
						} // else
					} // if
				} else {
					if (m[i][j].getNumerator() != 0) {
						if (!changeFound) {
							changeFound = true;
						} else {
							output = false;
						} // else
					} // if
				} // else
			} // for j
		} // for i

		return output;
	} // isElementary

	/**
	 * Determines if the matrix is a square matrix.
	 * @return true if the matrix is square, false otherwise
	 */
	public boolean isSquare() {
		return (this.numRows() == this.numCols());
	} // isSquare

	/**
	 * Determines if the matrix is upper triangular.
	 * @return true if the matrix is upper triangular, false otherwise
	 */
	public boolean isUpperTriangular() {
		boolean output = true;

		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[0].length; j++) {
				if (i > j && m[i][j].getNumerator() != 0) {
					output = false;
				} // if
			} // for j
		} // for i

		return output;
	} // isUpperTriangular

	/**
	 * Determines if the matrix is lower triangular.
	 * @return true if the matrix is lower triangular, false otherwise
	 */
	public boolean isLowerTriangular() {
		boolean output = true;

		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[0].length; j++) {
				if (i < j && m[i][j].getNumerator() != 0) {
					output = false;
				} // if
			} // for j
		} // for i

		return output;
	} // isLowerTriangular

	/**
	 * Gets an entry from the matrix.
	 * @param row row location
	 * @param col column location
	 * @return the fraction in the given location
	 */
	public Fraction getFrac(int row, int col) {
		return m[row][col];
	} // getFrac

	/**
	 * Sets an entry in the matrix.
	 * @param row row location
	 * @param col column location
	 * @param f Fraction value of the new entry
	 */
	public void setFrac(int row, int col, Fraction f) {
		m[row][col] = f;
	} // setFrac

	/**
	 * Gets the lower triangular matrix in the LU decomposition of this matrix.
	 * @return lower triangular matrix
	 */
	public Matrix getL() {
		if (L == null) {
			this.findLUDecomposition();
		} // if
		return L;
	} // getL

	/**
	 * Gets the upper triangular matrix in the LU decomposition of this matrix.
	 * @return upper triangular matrix
	 */
	public Matrix getU() {
		if (U == null) {
			this.findLUDecomposition();
		} // if
		return U;
	} // Matrix

	/**
	 * Gets the number of rows in the matrix.
	 * @return number of rows
	 */
	public int numRows() {
		return m.length;
	} // numRows

	/**
	 * Gets the number of columns in the matrix.
	 * @return number of columns
	 */
	public int numCols() {
		return m[0].length;
	} // numCols

	/**
	 * Switches the value of the variable showSteps.
	 */
	public void changeShowSteps() {
		showSteps = !showSteps;
	} // changeShowSteps

	/**
	 * Switches the value of the variable displayDoubles.
	 */
	public void changeDisplayDoubles() {
		displayDoubles = !displayDoubles;
	} // changeDisplayDoubles

	/**
	 * Displays a matrix.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		boolean ints = true;
		int maxChars = 0;
		boolean negativeFirstRow = false;

		// determines if the entire matrix is integers
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[0].length; j++) {
				if (m[i][j].getNumerator() % m[i][j].getDenominator() != 0) {
					ints = false;
				} // if
				if (m[i][0].getNumerator() < 0) {
					negativeFirstRow = true;
				}
			} // for j
		} // for i

		// finds the highest numbers of characters in any entry
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[0].length; j++) {
				if (m[i][j].toString().length() > maxChars && !displayDoubles) {
					maxChars = m[i][j].toString().length();
				} else if (m[i][j].toString().length() > maxChars && displayDoubles) {
					maxChars = (m[i][j].getDouble() + "").toString().length();
				} // else if
			} // for j
		} // for i

		// if all the entries are integers, remove the "/1"
		if (ints) {
			maxChars -= 2;
		} // if

		// print the matrix row by row
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[0].length; j++) {
				if (negativeFirstRow && j == 0 && m[i][j].isPositive()) {
					sb.append(" ");
				} // if

				if (ints) {
					sb.append(m[i][j].getNumerator());
					if (!m[i][j].isPositive()) {
						sb.append(fillSpaces(maxChars + 1, (m[i][j].getNumerator() + "").toString().length()));
					} else {
						sb.append(fillSpaces(maxChars, (m[i][j].getNumerator() + "").toString().length()));
					} // else
				} else {
					if (displayDoubles) {
						sb.append(m[i][j].getDouble());
						if (!m[i][j].isPositive()) {
							sb.append(fillSpaces(maxChars + 1, (m[i][j].getDouble() + "").length()));
						} else {
							sb.append(fillSpaces(maxChars, (m[i][j].getDouble() + "").length()));
						} // else
					} else {
						sb.append(m[i][j]);
						if (!m[i][j].isPositive()) {
							sb.append(fillSpaces(maxChars, m[i][j].toString().length()));
						} else {
							sb.append(fillSpaces(maxChars - 1, m[i][j].toString().length()));
						} // else
					} // else
					sb.append(" ");
				} // else

				if (this instanceof AugmentedMatrix && j == ((AugmentedMatrix)this).getAugIndex() - 1) {
					if (j + 1 < m[0].length && m[i][j+1].getNumerator() >= 0) {
						sb.append(" | ");
					} else {
						sb.append(" |");
					} // else
				} else if (j + 1 < m[0].length && m[i][j+1].getNumerator() >= 0) {
					sb.append(" ");
				} // else if
			} // for j
			if (i != m.length - 1) {
				sb.append("\n");
			} // if
		} // for i


		return sb.toString();
	} // toString

	/**
	 * Used by toString() to display items with the correct amount of spaces to ensure entries line up.
	 * @param maxChars number of characters in the largest entry
	 * @param length number characters in the largest entry
	 * @return string with enough blanks so that length == maxChars
	 */
	private String fillSpaces(int maxChars, int length) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < (maxChars - length); i++) {
			sb.append(" ");
		} // for i

		return sb.toString();
	} // fillSpaces
} // Matrix
