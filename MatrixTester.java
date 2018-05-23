public class MatrixTester {
	public static void main(String[] args) {
		Fraction [][] test1 = {{new Fraction(1), new Fraction(1), new Fraction(-2), new Fraction(4)},
							   {new Fraction(1), new Fraction(3), new Fraction(-1), new Fraction(7)},
							   {new Fraction(2), new Fraction(1), new Fraction(-5), new Fraction(7)}};
		
		Fraction [][] test2 = {{new Fraction(1), new Fraction(1), new Fraction(0)},
				   			   {new Fraction(0), new Fraction(1), new Fraction(1)},
				   			   {new Fraction(1), new Fraction(2), new Fraction(1)}};
		
		Fraction [][] test3 = {{new Fraction(1), new Fraction(1), new Fraction(-2), new Fraction(4)},
				  			   {new Fraction(0), new Fraction(1), new Fraction(1), new Fraction(1)},
				  			   {new Fraction(0), new Fraction(0), new Fraction(-1), new Fraction(1)}};
		
		Fraction [][] test4 = {{new Fraction(1), new Fraction(2), new Fraction(3)},
 				  			   {new Fraction(4), new Fraction(5), new Fraction(6)},
 				  			   {new Fraction(7), new Fraction(8), new Fraction(9)}};
		
		Fraction [][] test5 = {{new Fraction(1), new Fraction(2), new Fraction(3)},
							   {new Fraction(4), new Fraction(5), new Fraction(6)}};
		
		Fraction [][] test6 = {{new Fraction(2), new Fraction(0)},
							   {new Fraction(0), new Fraction(1)},
							   {new Fraction(1), new Fraction(0)}};
		
		Fraction [][] test7 = {{new Fraction(1), new Fraction(2), new Fraction(3), new Fraction(4)},
							   {new Fraction(5), new Fraction(6), new Fraction(7), new Fraction(8)},
							   {new Fraction(9), new Fraction(10), new Fraction(11), new Fraction(12)},
							   {new Fraction(13), new Fraction(14), new Fraction(15), new Fraction(16)}};
		
		Fraction [][] test8 = {{new Fraction(-2), new Fraction(-1)},
							   {new Fraction(-3), new Fraction(-2)}};
		
		Fraction [][] test9 = {{new Fraction(-2), new Fraction(-1), new Fraction(5)},
	   			   			   {new Fraction(-4), new Fraction(0), new Fraction(-2)},
	   			   			   {new Fraction(2), new Fraction(1), new Fraction(3)}};
		
		Fraction [][] test10 = {{new Fraction(1), new Fraction(0), new Fraction(1), new Fraction(1), new Fraction(0), new Fraction(0)},
							    {new Fraction(2), new Fraction(1), new Fraction(3), new Fraction(0), new Fraction(1), new Fraction(0)},
							    {new Fraction(1), new Fraction(0), new Fraction(2), new Fraction(0), new Fraction(0), new Fraction(1)}};
		
		Fraction [][] test11 = {{new Fraction(1), new Fraction(0), new Fraction(0), new Fraction(-3)},
	   			   				{new Fraction(-2), new Fraction(1), new Fraction(0), new Fraction(4)},
	   			   				{new Fraction(-1), new Fraction(4), new Fraction(1), new Fraction(-3)}};
		
		Fraction [][] test12 = {{new Fraction(1), new Fraction(0), new Fraction(1)},
								{new Fraction(2), new Fraction(1), new Fraction(3)},
								{new Fraction(1), new Fraction(0), new Fraction(2)}};
		
		Fraction [][] test13 = {{new Fraction(2), new Fraction(0), new Fraction(-1)},
								{new Fraction(-1), new Fraction(1), new Fraction(-1)},
								{new Fraction(-1), new Fraction(0), new Fraction(1)}};
		
		Fraction [][] test14 = {{new Fraction(1), new Fraction(0), new Fraction(0)},
								{new Fraction(0), new Fraction(1), new Fraction(0)},
								{new Fraction(0), new Fraction(0), new Fraction(1)}};
		
		AugmentedMatrix test = new AugmentedMatrix(test9, -1);
		testDet(test);
	} // main
	
	private static void testTranspose(Matrix test) {
		System.out.println("Matrix:");
		System.out.println(test);
		System.out.println();
		System.out.println("Transposed Matrix:\n" + test.findTranspose());
	} // testTranspose
	
	private static void testInverse(Matrix test) {
		System.out.println("Matrix:");
		System.out.println(test);
		System.out.println();
		System.out.println("Inverse:\n" + test.findInverse());
	} // testInverse
	
	private static void testSolve(AugmentedMatrix test) {
		System.out.println("Matrix:");
		System.out.println(test);
		System.out.println();
		System.out.println("Solution:\n" + test.solveSystem());
	} // testSolve
	
	private static void testDet(Matrix test) {
		System.out.println("Matrix:");
		System.out.println(test);
		System.out.println();
		System.out.println("Determinate: " + test.det());
	} // testDet
	
	private static void testLUDecomposition(Matrix test) {
		test.findLUDecomposition();
		System.out.println("Original Matrix:");
		System.out.println(test);
		System.out.println();
		System.out.println("L:");
		System.out.println(test.getL());
		System.out.println();
		System.out.println("U:");
		System.out.println(test.getU());
		System.out.println();
		System.out.println("Verify LU = original matrix:");
		System.out.println(test.getL().multiply(test.getU()));
	} // testSomething
	
	private static void testRowReduction(Matrix test) {
		test.changeDisplayDoubles();
		System.out.println("inREF: " + test.inREF());
		System.out.println("inRREF: " + test.inRREF());
		System.out.println("Initial Matrix:");
		System.out.println(test);
		System.out.println();
		test.convertToREF();
		System.out.println("inREF: " + test.inREF());
		System.out.println("inRREF: " + test.inRREF());
		System.out.println("Converted to Row Echelon Form:");
		System.out.println(test);
		System.out.println();
		test.convertToReducedREF();
		System.out.println("inREF: " + test.inREF());
		System.out.println("inRREF: " + test.inRREF());
		System.out.println("Converted to Reduced Row Echelon Form:");
		System.out.println(test);
		System.out.println();
		System.out.println("Rank of the matrix: " + test.rank());
		System.out.println("Nullity of the matrix: " + test.nullity());
	} // testRowReduction
	
	private static void testMultiplication(Matrix m1, Matrix m2) {
		System.out.println("Multiply:");
		System.out.println(m1 + "\nand");
		System.out.println(m2);
		System.out.println();
		System.out.println("Result:");
		System.out.println(m1.multiply(m2));
	} // testMultiplication
} // REF