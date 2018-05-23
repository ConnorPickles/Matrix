/**
 * A subclass of Matrix that represents a single row operation.
 * 
 * @author Connor Pickles
 * @version 1.1.1
 * @since 2017-04-01
 */

public class ElementaryMatrix extends Matrix {
	
	/**
	 * Default constructor.
	 */
	ElementaryMatrix() {
		super();
	} // ElementaryMatrix
	
	/**
	 * Constructor.
	 * @param input 2D fraction array of all values
	 * @param augIndex the column where the augmented values begin
	 */
	ElementaryMatrix(Fraction[][] input) {
		super(input);
		if (!this.isElementary()) {
			System.out.println("error: cannot create elementary matrix from input:");
			System.out.println(new Matrix(input));
			System.exit(0);
		} // if
	} // ElementaryMatrix
	
	/**
	 * Inverts the elementary matrix.
	 * @return the inversed elementary matrix
	 */
	public Matrix invert() {
		for (int i = 0; i < this.numRows(); i++) {
			for (int j = 0; j < this.numCols(); j++) {
				if (i == j && m[i][j].getNumerator() != 1) {
					m[i][j] = new Fraction(m[i][j].getDenominator(), m[i][j].getNumerator());
				} else if (i != j && m[i][j].getNumerator() != 0) {
					m[i][j] = m[i][j].multiply(new Fraction(-1));
				} // else if
			} // for j
		} // for i
		
		return this;
	} // invert
	
	/**
	 * Copies the elementary matrix into a new elementary matrix.
	 * @return a copy of the elementary matrix
	 */
	public ElementaryMatrix copy() {
		ElementaryMatrix output = new ElementaryMatrix(new Fraction[this.numRows()][this.numCols()]);
		
		for (int i = 0; i < output.numRows(); i++) {
			for (int j = 0; j < output.numCols(); j++) {
				output.setFrac(i, j, this.getFrac(i, j));
			} // for j
		} // for i
		
		return output;
	} // copy
	
} // ElementaryMatrix