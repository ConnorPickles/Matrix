/**
 * A subclass of Matrix that allows for augmentation of a coefficient matrix.
 * 
 * @author Connor Pickles
 * @version 1.2.1
 * @since 2017-04-01
 */

public class AugmentedMatrix extends Matrix {
	private int augIndex = -1;
	
	/**
	 * Default constructor.
	 */
	AugmentedMatrix() {
		super();
	} // AugmentedMatrix
	
	/**
	 * Constructor.
	 * @param input 2D fraction array of all values
	 * @param augIndex the column where the augmented values begin
	 */
	AugmentedMatrix(Fraction[][] input, int augIndex) {
		super(input);
		this.augIndex = augIndex;
		if (augIndex >= input[0].length) {
			System.out.println("invalid value of augIndex: " + augIndex);
			System.exit(0);
		} // if
	} // AugmentedMatrix
	
	/**
	 * Solves the augmented matrix.
	 * @return a matrix of solutions x1, x2, x3, etc.
	 */
	public Matrix solveSystem() {
		if (this.numCols() - 1 != this.augIndex) {
			System.out.println("Cannot solve this system");
			System.exit(0);
		} else if (this.rank() != this.numRows()) {
			System.out.println("The system is inconsistent");
			System.exit(0);
		} // else if
		
		Matrix output = new Matrix(new Fraction[this.numRows()][1]);
		
		this.convertToReducedREF();
		for (int i = 0; i < this.numRows(); i++) {
			output.setFrac(i, 0, this.getFrac(i, augIndex));
		} // for i
		
		return output;
	} // solveSystem
	
	/**
	 * Returns a matrix of type Matrix from this AugmentedMatrix. Row and column values are inclusive:
	 * 0 <= row < numRows and 0 <= col < numCols
	 * @param firstRow the first row of the new matrix
	 * @param lastRow the last row of the new matrix
	 * @param firstCol the first column of the new matrix
	 * @param lastCol the last column of the new matrix
	 * @return a Matrix from the AugmentedMatrix
	 */
	public Matrix getMatrix(int firstRow, int lastRow, int firstCol, int lastCol) {
		Matrix output = new Matrix();
		
		for (int i = firstRow; i <= lastRow; i++) {
			for (int j = firstCol; j <= lastCol; j++) {
				output.setFrac(i - firstRow, j - firstCol, this.getFrac(i, j));
			} // for j
		} // for i
		
		return output;
	} // getMatrix
	
	/**
	 * Determines if the coefficient matrix is a square matrix.
	 * @return true if the coefficient matrix is square, false otherwise
	 */
	public boolean isSquare() {
		return getMatrix(0, 0, this.numRows() - 1, augIndex - 1).isSquare();
	} // isSquare
	
	/**
	 * Copies the augmented matrix into a new augmented matrix.
	 * @return a copy of the augmented matrix
	 */
	public AugmentedMatrix copy() {
		AugmentedMatrix output = new AugmentedMatrix(new Fraction[this.numRows()][this.numCols()], this.augIndex);
		
		for (int i = 0; i < output.numRows(); i++) {
			for (int j = 0; j < output.numCols(); j++) {
				output.setFrac(i, j, this.getFrac(i, j));
			} // for j
		} // for i
		
		return output;
	} // copy
	
	/**
	 * Gets the column where the augmented matrix begins.
	 * @return value of augIndex
	 */
	public int getAugIndex() {
		return augIndex;
	} // getAugIndex
	
} // AugmentedMatrix