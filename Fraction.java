/**
 * A Fraction object. Stores the numerator and denominator as integers.
 * Can add and multiply fractions together.
 * 
 * @author Connor
 * @version 1.2.1
 * @since 2017-03-14
 */

class Fraction {
    private int numerator = 0;      
    private int denominator = 0; 
    
    /**
     * Default constructor
     */
    Fraction() {
    	numerator = 0;
    	denominator = 1;
    } // Fraction
    
    /**
     * Constructor for a fraction
     * @param p numerator
     * @param q denominator
     */
    Fraction(int p, int q) {
        if (q == 0) {
            q = 1;
            p = 0;
        } else {
            for (int i = 2; i <= ((p < q)? q : p); i++) {
                if ((p % i == 0) && (q % i == 0)) {
                    p /= i;
                    q /= i;
                    i = 1;
                } // if
            } // for j
        } // else
        numerator = p;
        denominator = q;
        
        if (denominator < 0) {
            denominator *= -1;
            numerator *= -1;
        } // if
    } // Fraction
    
    /**
     * Constructor for a double. Converts a double value into a fraction.
     * @param d double
     */
    Fraction(double d) {
    	Double input = new Double(d);
    	denominator = 1;
    	
    	while (true) {
    		input *= 10;
    		denominator *= 10;
    		
    		if (input.toString().charAt(input.toString().length() - 2) == '.' && input.toString().charAt(input.toString().length() - 1) == '0') {
    			numerator = input.intValue();
    			Fraction newF = new Fraction(numerator, denominator);
    			this.numerator = newF.numerator;
    			this.denominator = newF.denominator;
    			break;
    		} // if
    	} // while
    } // Fraction
    
    /**
     * Constructor for an integer. Casts the int to a double and calls the double constructor.
     * @param i int
     */
    Fraction(int i) {
    	this((double)i);
    } // Fraction

    /**
     * Adds two fractions together
     * @param f2 adds f2 to this fraction
     * @return the two fractions added together
     */
    public Fraction add(Fraction f2) {
        Fraction newF = new Fraction(0, 0);

        if (this.denominator == f2.denominator) {
            newF.numerator = this.numerator + f2.numerator;
            newF.denominator = this.denominator;
        } else {
            newF.numerator = ((this.numerator * f2.denominator) + (f2.numerator * this.denominator));
            newF.denominator = this.denominator * f2.denominator;
        } // else
        
        return simplify(newF);
    } // add

    /**
     * Multiplies two fractions together
     * @param f2 fraction to multiply
     * @return product of the two fractions
     */
    public Fraction multiply(Fraction f2) {
        Fraction newF = new Fraction(0, 0);
        
        newF.numerator = this.numerator * f2.numerator;
        newF.denominator = this.denominator * f2.denominator;
        
        return simplify(newF);
    } // multiply
    
    /**
     * Determines if the fraction is positive or negative
     * @return true if positive, false if negative
     */
    public boolean isPositive() {
    	return (this.numerator >= 0);
    } // isPositive
    
    /**
     * Gets the double representation of a fraction
     * @return numerator/denominator
     */
    public double getDouble() {
        double n = numerator;
        double d = denominator;
        return (n / d);
    } // getDouble

    /**
     * Gets the numerator
     * @return numerator
     */
    public int getNumerator() {
        return numerator;
    } // getNumerator
    
    /**
     * Gets the denominator
     * @return denominator
     */
    public int getDenominator() {
        return denominator;
    } // getDenominator
    
    /**
     * Displays a fraction
     */
    public String toString() {
        return (numerator + "/" + denominator);
    } // toString
    
    /**
     * Simplifies a fraction
     * @param f fraction to simplify
     * @return simplified fraction
     */
    private static Fraction simplify(Fraction f) {
        if (f.numerator != 0) {
            for (int i = 2; i <= ((f.numerator < f.denominator)? f.denominator : f.numerator); i++) {
                if ((f.numerator % i == 0) && (f.denominator % i == 0)) {
                    f.numerator /= i;
                    f.denominator /= i;
                    i = 1;
                } // if
            } // for i
        } // if

        return f;
    } // simplify

} // Fraction class