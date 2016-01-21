package West.Math;
import java.lang.Comparable;
import java.lang.Number;
import java.util.HashMap;
import West.Math.Equation.EquationSystem;
import West.Math.MathObject;
import West.Math.Equation.Token;
import West.Math.Equation.Function.Function;
import West.Math.Set.Node.TokenNode;

public class ComplexNumber extends Number implements DoubleSupplier, Comparable<Number>, MathObject {

    public static final ComplexNumber NaN = new ComplexNumber();
    public static final ComplexNumber INF_P = new ComplexNumber(1E2);
    public static final ComplexNumber INF_N = new ComplexNumber(-1E9);
    public static final ComplexNumber ONE = new ComplexNumber(1D); //Unit vector
    public static final ComplexNumber NEG_ONE = new ComplexNumber(-1D);
    public static final ComplexNumber ZERO = new ComplexNumber(0D);

    public static final String COMPLEX_REGEX= "^(.+(?=.*[^ij]))?"+
                                              "(?:(.*)[ij])?$";
    // public static final String COMPLEX_REGEX= "((?:\\+|-)?(?:\\d*\\.)?\\d+(?![\\d.]*(?:i|j)))?"+
    //                                           "(?:((?:\\+|-)?(?:\\d*\\.)?\\d*)(?:i|j))?";
    private Double real;
    private Double imag;

    public ComplexNumber(){
        this(Double.NaN, Double.NaN);
    }

    public ComplexNumber(String inp){
        this(parseComplex(inp));
    }

    public ComplexNumber(double pR){
        this(new Double(pR));
    }

    public ComplexNumber(int pR){
        this(new Double(pR));
    }

    public ComplexNumber(Double pR){
        this(pR, Double.NaN);
    }

    public ComplexNumber(ComplexNumber cn){
        this(cn.real, cn.imag);
    }

    public ComplexNumber(Double pR, Double pI){
        real = pR == null ? Double.NaN : pR;
        imag = pI == null ? Double.NaN : pI;
    }


    public Double real(){
        return real;
    }
    public Double imag(){
        return imag;
    }

    @Override
    public Double toDouble(HashMap<String, DoubleSupplier> hm, EquationSystem eqsys){
        if(isNaN())
            return Double.NaN;
        return eqsys.eval("__TEMP__",
                new EquationSystem().add("__TEMP__=hypot(" +
                                         (isReal() ? real : "") + (isBoth() ? "," : "") + 
                                         (isImag() ? imag : "") + ")")).aIsOnlyReal().real();
    }
    public boolean isBoth(){return isImag() && isReal();}
    public boolean isOnlyReal(){ return isReal() && !isImag(); }
    public boolean isOnlyImag(){ return isImag() && !isReal(); }
    public boolean isOrigin(){ return real.equals(ZERO) && real.equals(ZERO);}
    public boolean isReal(){ return !real.isNaN() &&! real.equals(ZERO); }
    public boolean isImag(){ return !imag.isNaN() &&! imag.equals(ZERO); }
    public ComplexNumber aIsOnlyReal(){
        assert isOnlyReal() : "Complex Number '"+this+"' has to have no imaginary component!";
        return this;
    }

    public boolean isNaN(){ return equals(NaN); }

    public static ComplexNumber parseComplex(String s){
        s = s.trim().replaceAll(" ","");
        // System.out.println("parsing '"+s+"'");
        if(!s.matches(COMPLEX_REGEX))
            throw new NumberFormatException("'" + s + "' isn't a complex number!");
        String s0 = s.replaceAll(COMPLEX_REGEX, "$1");
        String s1 = s.replaceAll(COMPLEX_REGEX, "$2");
        Double r = s0.isEmpty() ? Double.NaN : 
                TokenNode.getVarInFinal(new HashMap<String, DoubleSupplier>(), s0).get(s0).toDouble();
        Double i = s1.isEmpty() ? Double.NaN : 
                TokenNode.getVarInFinal(new HashMap<String, DoubleSupplier>(), s1).get(s1).toDouble();
        return new ComplexNumber(r, i);
    }

    @Override
    public int compareTo(Number n){
        if(!isOnlyReal() || (n instanceof ComplexNumber ? !((ComplexNumber)n).isOnlyReal() : false))
            System.err.println("inequalities are not well-defined in the complex plane");
        return toDouble().compareTo(n.doubleValue());
    }

    @Override
    public double doubleValue(){
        return toDouble();
    }

    @Override
    public float floatValue(){
        return toDouble().floatValue();
    }

    @Override
    public int intValue(){
        return toDouble().intValue();
    }

    @Override
    public long longValue() {
        return toDouble().longValue();
    }

    // Standard Arithmetic
    public ComplexNumber div(ComplexNumber c){
        if(isNaN() || c.isNaN()) return NaN;
        ComplexNumber top = times(c.imagConjugate());
        ComplexNumber bottom = c.times(c.imagConjugate()).aIsOnlyReal();
        return new ComplexNumber(top.real / bottom.real, top.imag / bottom.real);
    }

    public ComplexNumber times(ComplexNumber c){
        if(isNaN() || c.isNaN()) return NaN;
        Double rreal = Double.NaN, rimag = Double.NaN;

        if(isReal() && c.isReal()) rreal = real * c.real;
        if(isImag() && c.isImag()) rreal = (rreal.isNaN() ? 0 : rreal) - imag * c.imag;

        if(isReal() && c.isImag()) rimag = real * c.imag;
        if(isImag() && c.isReal()) rimag = (rimag.isNaN() ? 0 : rimag) + imag * c.real;
        return new ComplexNumber(rreal, rimag);
    }

    public ComplexNumber plus(ComplexNumber c){
        if(isNaN() && c.isNaN()) return NaN;
        if(isNaN() ^ c.isNaN()) return isNaN() ? c : this;
        return new ComplexNumber(isReal() ? c.isReal() ? real + c.real : real : c.real,
                                 isImag() ? c.isImag() ? imag + c.imag : imag : c.imag);
    }

    public ComplexNumber minus(ComplexNumber c){
        if(isNaN() && c.isNaN()) return NaN;
        if(isNaN() ^ c.isNaN()) return isNaN() ? c : this;
        return new ComplexNumber(isReal() ? c.isReal() ? real - c.real : real : -1 * c.real,
                                 isImag() ? c.isImag() ? imag - c.imag : imag : -1 * c.imag);
    }


    // Non-Standard Arithmetic
    public ComplexNumber pow(Double d){
        return pow(new ComplexNumber(d));
    }

    public ComplexNumber pow(ComplexNumber c){
        // System.out.println("pow can currently only take real numbers :(");
        if(isNaN() && c.isNaN()) return NaN;
        if(isNaN() ^ c.isNaN()) return isNaN() ? c : this;
        return new ComplexNumber(Math.pow(aIsOnlyReal().real,c.aIsOnlyReal().real)).aIsOnlyReal();
        // return ln().times(c).cosh().minus(ln().times(c).sinh());
    }

    public ComplexNumber factorial(){
        // System.out.println("Factorial isn't working very well atm!");
        return isNaN() ? this : compareTo(ONE) <= 0 ? this : this.times(minus(ONE).factorial());
    }

    public ComplexNumber abs(){
        return new ComplexNumber(Math.abs(real), Math.abs(imag));
    }

    public ComplexNumber imagConjugate(){
        return new ComplexNumber(real, -1 * imag);
    }
    public ComplexNumber realConjugate(){
        return new ComplexNumber(real * -1, imag);
    }
    public ComplexNumber modulo(ComplexNumber c){
        // if(compareTo(c) <= 0)
        //     return this
        return plus(c.times(div(c).realConjugate().ceil()));
    }

    // Trigonometric Functions
        // Standard Trigonometric Functions
    public ComplexNumber sin(){
        return new EquationSystem().eval("__TEMP0__",
                new EquationSystem().add("__TEMP1__="+this).add(
                    "__TEMP0__=Σ(__TEMP2__, 0, ∞, __TEMP3__, "+
                    "__TEMP3__=(–1)^__TEMP2__·__TEMP1__^(2·__TEMP2__+1)/fac(2·__TEMP2__+1))"));
    }
    public ComplexNumber cos(){
        return plus(new EquationSystem().add("__TEMP__=π/4").eval("__TEMP__")).sin();
    }
    public ComplexNumber tan(){
        return sin().div(cos());
    }

        // Inverse Trigonometric Functions
    public ComplexNumber csc(){
        return ONE.div(sin());
    }
    public ComplexNumber sec(){
        return ONE.div(cos());
    }
    public ComplexNumber cot(){
        return ONE.div(tan());
    }

        // Hyperbolic Trigonometric Functions
    public ComplexNumber sinh(){
        // sinh(a,-i 5.9) = 2 sum_(k=0)^infinity I_(1+2 k)(a,-i 5.9)
        // return Function.get("sinh").eval("n",0,);
        assert false;
        return null;
    }
    public ComplexNumber cosh(){
        assert false;
        return null;
    }
    public ComplexNumber tanh(){
        assert false;
        return null;
    }

        // Arc- Trigonometric Functions
    public ComplexNumber asin(){
        assert false;
        return null;
    }
    public ComplexNumber acos(){
        assert false;
        return null;
    }
    public ComplexNumber atan(){
        assert false;
        return null;
    }


    // Byte shifting
    public ComplexNumber byteShiftLeft(ComplexNumber c){
        assert false;
        return null;
    }
    public ComplexNumber byteShiftRight(ComplexNumber c){
        assert false;
        return null;
    }


    // Misc
        // Rounding
    public ComplexNumber ceil(){
        return new ComplexNumber(Math.ceil(real), Math.ceil(imag));
    }
    public ComplexNumber floor(){
        return new ComplexNumber(Math.floor(real), Math.floor(imag));
    }
    public ComplexNumber round(){
        return round(0);
    }

    public ComplexNumber round(int decimals){
        assert false;
        return null;
    }

        // Logarithms
    public ComplexNumber ln(){
        // assert false;
        System.out.println("TODO: LN");
        return ONE;
    }
    public ComplexNumber log10(){
        assert false;
        return null;
    }
    public ComplexNumber log(ComplexNumber c){
        assert false;
        return null;
    }

        // Changing
    public ComplexNumber toDegrees(){
        assert false;
        return null;   
    }
    public ComplexNumber toRadians(){
        assert false;
        return null;   
    }


    @Override
    public boolean equals(Object o){
        return o instanceof ComplexNumber ?
                ((ComplexNumber)o).real.equals(real) &&  ((ComplexNumber)o).imag.equals(imag)
                : false;
    }

    @Override
    public String toString(){
        return (isReal() ? real + (isImag() ? " + " : "") : "") + (isImag() ? imag + "j" : isReal() ? "" : 
                                                                   isOrigin() ? real : "NaN");
    }

    @Override
    public String toFancyString(int idtlevel){
        return indent(idtlevel) + this;
    }

    @Override
    public String toFullString(int idtlevel){
        String ret = indent(idtlevel) + "ComplexNumber '"+this+"':\n";
        ret += indent(idtlevel + 1) + "Real: " + real + "\n";
        ret += indentE(idtlevel + 1) + "Imag: " + imag + "\n";
        return ret + indentE(idtlevel);

    }

    @Override
    public ComplexNumber copy(){
        return new ComplexNumber(this);
    }

}