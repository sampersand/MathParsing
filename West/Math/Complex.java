package West.Math;
import java.lang.Comparable;
import java.lang.Number;
import java.util.HashMap;
import West.Math.Equation.EquationSystem;
import West.Math.MathObject;
import West.Math.Equation.Token;
import West.Math.Equation.Function.Function;
import West.Math.Set.Node.TokenNode;

public class Complex extends Number implements DoubleSupplier, Comparable<Number>, MathObject {

    public static final Complex NAN = new Complex();
    public static final Complex INF_P = new Complex(1E4);
    public static final Complex INF_N = new Complex(-1E9);
    public static final Complex ONE = new Complex(1d); //Unit vector
    public static final Complex NEG_ONE = new Complex(-1d);
    public static final Complex ZERO = new Complex(0d);
    public static final Complex PI = new Complex(Math.PI);
    public static final Complex E = new Complex(Math.E);

    public static final String COMPLEX_REGEX= "^(.+?(?=.*[^ij]))?\\+?(?:(.*)[ij])?$";
    
    private Double real;
    private Double imag;

    public Complex(){
        this(Double.NaN, Double.NaN);
    }

    public Complex(String inp){
        this(parseComplex(inp));
    }

    public Complex(double pR){
        this(new Double(pR));
    }

    public Complex(int pR){
        this(new Double(pR));
    }

    public Complex(Number pR){
        this(pR, Double.NaN);
    }

    public Complex(Complex cn){
        this(cn.real, cn.imag);
    }

    public Complex(Number pR, Number pI){
        real = pR == null ? Double.NaN : new Double(pR.doubleValue());
        imag = pI == null ? Double.NaN : new Double(pI.doubleValue());
    }

    public Complex(Complex pR, Complex pI){
        real = pR == null ? Double.NaN : pR.aIsOnlyReal().real;
        imag = pI == null ? Double.NaN : pI.aIsOnlyReal().real;
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
        return isOnlyReal() ? real : isOnlyImag() ? imag : eqsys.eval("__TEMP__",
                new EquationSystem().add("__TEMP__=hypot(" +
                                         (isReal() ? real : "") + (isBoth() ? "," : "") + 
                                         (isImag() ? imag : "") + ")")).aIsOnlyReal().real;
    }
    public boolean isBoth(){return isImag() && isReal();}
    public boolean isOnlyReal(){ return isReal() && !isImag(); }
    public boolean isOnlyImag(){ return isImag() && !isReal(); }
    public boolean isOrigin(){ return real.equals(ZERO) && real.equals(ZERO);}
    public boolean isReal(){ return !real.isNaN();}// && !new Complex(real).equals(ZERO); }
    public boolean isImag(){ return !imag.isNaN();}// && !new Complex(imag).equals(ZERO); }
    public Complex aIsOnlyReal(){
        assert isOnlyReal() : "Complex Number '" + this + "' has to have no imaginary component!";
        return this;
    }

    public boolean isNaN(){ return equals(NAN); }//!isReal() && !isImag();}

    public static Complex parseComplex(String s){
        s = s.trim().replaceAll(" ","");
        if(!s.matches(COMPLEX_REGEX))
                throw new NumberFormatException("'" + s + "' isn't a complex number!");
        String s0 = s.replaceAll(COMPLEX_REGEX, "$1");
        String s1 = s.replaceAll(COMPLEX_REGEX, "$2");
        Double r = s0.isEmpty() ? Double.NaN : 
                TokenNode.getVarInFinal(new HashMap<String, DoubleSupplier>(), s0).get(s0).toDouble();
        Double i = s1.isEmpty() ? Double.NaN : 
                TokenNode.getVarInFinal(new HashMap<String, DoubleSupplier>(), s1).get(s1).toDouble();
        return new Complex(r, i);
    }

    @Override
    public int compareTo(Number n){
        if(!isOnlyReal() || (n instanceof Complex ? !((Complex)n).isOnlyReal() : false))
            System.err.println("inequalities are not well-defined in the complex plane");
        Complex cn = (Complex)n;
        return isReal() && cn.isReal()
               ?
               isImag() && cn.isImag()
                   ?
                   toDouble().compareTo(cn.toDouble())
                   :
                   real.compareTo(cn.real)
                :
                imag.compareTo(cn.imag);
        // return toDouble().compareTo(n.toDouble());
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
    public static Complex div(Number c, Number b){
        return new Complex(c).div(new Complex(b));
    }

    public static Complex mult(Double c, Double b){
        return new Complex(c).mult(new Complex(b));
    }

    public static Complex plus(Double c, Double b){
        return new Complex(c).plus(new Complex(b));
    }
    
    public static Complex minus(Double c, Double b){
        return new Complex(c).minus(new Complex(b));
    }
    public Complex div(Complex c){
        if (isOnlyReal() && c.isOnlyReal()){
            return new Complex(real * 1f / c.real);
        }
        if(isNaN() && c.isNaN()) return NAN;
        if(isNaN() || c.isNaN()) return NAN; 
        // if(isNaN() ^ c.isNaN()) return isNaN() ? c : this;

        Complex top = mult(c.imagConjugate());
        Complex bottom = c.mult(c.imagConjugate()).aIsOnlyReal();
        return new Complex(top.real / bottom.real, top.imag / bottom.real);
    }

    public Complex mult(Complex c){
        if (isOnlyReal() && c.isOnlyReal()){
            return new Complex(real * c.real);
        }
        if(isNaN() && c.isNaN()) return NAN;
        if(isNaN() || c.isNaN()) return NAN; 
        // if(isNaN() ^ c.isNaN()) return isNaN() ? c : this;

        Double rreal = Double.NaN, rimag = Double.NaN;

        if(isReal() && c.isReal()) rreal = real * c.real;
        if(isImag() && c.isImag()) rreal = (rreal.isNaN() ? 0 : rreal) - imag * c.imag;

        if(isReal() && c.isImag()) rimag = real * c.imag;
        if(isImag() && c.isReal()) rimag = (rimag.isNaN() ? 0 : rimag) + imag * c.real;
        return new Complex(rreal, rimag);
    }

    public Complex plus(Complex c){
        if (isOnlyReal() && c.isOnlyReal()){
            return new Complex(real + c.real);
        }
        if(isNaN() && c.isNaN()) return NAN;
        if(isNaN() || c.isNaN()) return NAN; 
        // if(isNaN() ^ c.isNaN()) return isNaN() ? c : this;
        return new Complex(isReal() ? c.isReal() ? real + c.real : real : c.real,
                                 isImag() ? c.isImag() ? imag + c.imag : imag : c.imag);
    }

    public Complex minus(Complex c){
        if (isOnlyReal() && c.isOnlyReal()){
            return new Complex(real - c.real);
        }
        if(isNaN() && c.isNaN()) return NAN;
        if(isNaN() || c.isNaN()) return NAN; 
        // if(isNaN() ^ c.isNaN()) return isNaN() ? c : this;
        return new Complex(isReal() ? c.isReal() ? real - c.real : real : -1 * c.real,
                                 isImag() ? c.isImag() ? imag - c.imag : imag : -1 * c.imag);
    }

    // Non-Standard Arithmetic
    public Complex pow(Double d){
        return pow(new Complex(d));
    }

    public static Complex pow(Double c, Double b){
        return new Complex(c).pow(new Complex(b));
    }
    public Complex pow(Complex c){
        if (isOnlyReal() && c.isOnlyReal()){
            return new Complex(Math.pow(real, c.real));
        }
        if(isNaN() && c.isNaN()) return NAN;
        if(isNaN() || c.isNaN()) return NAN; 
        // if(isNaN() ^ c.isNaN()) return isNaN() ? c : this;
        return isOnlyReal() && c.isOnlyReal() ?
                new Complex(Math.pow(aIsOnlyReal().real, c.aIsOnlyReal().real))
                :
                new Complex(Complex.mult(Math.exp(real), Math.cos(imag)),
                                  Complex.mult(Math.exp(real), Math.sin(imag)));

    }
    public Complex sqrt(){
        return pow(0.5d);
    }

    public Complex factorial(){
        // System.out.println("TODO: LN");
        return isNaN() ? this : compareTo(ONE) <= 0 ? ONE : this.mult(minus(ONE).factorial());
    }

    public Complex abs(){
        return new Complex(isReal() ? Math.abs(real) : Double.NaN,
                                 isImag() ? Math.abs(imag) : Double.NaN);
    }

    public Complex magnitude(){
        return new Complex(toDouble());
    }

    public Complex imagConjugate(){
        return new Complex(real, -1 * imag);
    }
    public Complex realConjugate(){
        return new Complex(real * -1, imag);
    }
    public Complex modulo(Complex c){
        // if(compareTo(c) <= 0)
        //     return this
        return plus(c.mult(div(c).realConjugate().ceil()));
    }

    // Trigonometric Functions
        // Standard Trigonometric Functions
    public Complex sin() {
        return isOnlyReal() ?
                new Complex(Math.sin(aIsOnlyReal().toDouble()))
                :
                new Complex(Complex.mult(+Math.sin(real), +Math.cosh(imag)),
                                  Complex.mult(+Math.cos(real), +Math.sinh(imag)));
    }

    public Complex cos() {
        return isOnlyReal() ?
                new Complex(Math.cos(aIsOnlyReal().toDouble()))
                :
                new Complex(Complex.mult(+Math.cos(real), +Math.cosh(imag)),
                                  Complex.mult(-Math.sin(real), +Math.sinh(imag)));
    }

    // public Complex sin(){
    //     return isOnlyReal() ? new Complex(Math.sin(real)) : new EquationSystem().eval("__TEMP0__",
    //             new EquationSystem().add("__TEMP1__="+this).add(
    //                 "__TEMP0__=Σ(__TEMP2__, 0, ∞, __TEMP3__, "+
    //                 "__TEMP3__=(–1)^__TEMP2__·__TEMP1__^(2·__TEMP2__+1)/fac(2·__TEMP2__+1))"));
    // }
    // public Complex cos(){
    //     // return magnitude().plus(new Equ    ationSystem().add("__TEMP__=¼·π").eval("__TEMP__")).sin();
    //     // return isOnlyReal() ? new Complex(Math.sin(real)) : new EquationSystem().eval("__TEMP0__",
    //     return new EquationSystem().eval("__TEMP0__",
    //             new EquationSystem().add("__TEMP1__=" + this).add(
    //                 "__TEMP0__=Σ(__TEMP2__, 0, ∞, __TEMP3__, " +
    //                 "__TEMP3__=(–1)^__TEMP2__·__TEMP1__^(2·__TEMP2__)/fac(2·__TEMP2__))"));
    // }
    public Complex tan(){
        return isOnlyReal() ?
               new Complex(Math.tan(real))
               :
               sin().div(cos());
    }

        // Inverse Trigonometric Functions
    public Complex csc(){
        return ONE.div(sin());
    }
    public Complex sec(){
        return ONE.div(cos());
    }
    public Complex cot(){
        return ONE.div(tan());
    }

        // Hyperbolic Trigonometric Functions
    public Complex sinh(){
        return new Complex(Math.sinh(aIsOnlyReal().real));
    }
    public Complex cosh(){
        return new Complex(Math.cosh(aIsOnlyReal().real));
    }
    public Complex tanh(){
        return new Complex(Math.tanh(aIsOnlyReal().real));
    }

        // Arc- Trigonometric Functions
    public Complex asin(){
        return new Complex(Math.asin(aIsOnlyReal().real));
    }
    public Complex acos(){
        return new Complex(Math.acos(aIsOnlyReal().real));
    }
    public Complex atan(){
        return new Complex(Math.atan(aIsOnlyReal().real));
    }


    // Byte shifting
    public Complex byteShiftLeft(Complex c){
        return new Complex(intValue() << c.intValue());
    }
    public Complex byteShiftRight(Complex c){
        return new Complex(intValue() >> c.intValue());
    }

    // Misc
        // Rounding
    public Complex ceil(){
        return new Complex(isReal() ? Math.ceil(real) : Double.NaN, isImag() ? Math.ceil(imag) : Double.NaN);
    }
    public Complex floor(){
        return new Complex(isReal() ? Math.floor(real) : Double.NaN, isImag() ? Math.floor(imag) : Double.NaN);
    }
    public Complex round(){
        return round(0);
    }

    public Complex round(int decimals){
        Complex dec = ONE.minus(new Complex(new Double(Math.pow(10, 0-decimals))));
        return new Complex(mult(dec).intValue()).div(dec);
    }

        // Logarithms
    public Complex ln(){
        return new Complex(Math.log(aIsOnlyReal().real));
    }
    public Complex log10(){
        return new Complex(Math.log10(aIsOnlyReal().real));
    }
    public Complex log(Complex c){
        return new Complex(Math.log(aIsOnlyReal().real) / (1F * Math.log(c.aIsOnlyReal().real)));
    }

        // Changing
    public Complex toDegrees(){
        return aIsOnlyReal().mult(new Complex(180)).div(PI);
    }
    public Complex toRadians(){
        return aIsOnlyReal().mult(PI).div(new Complex(180));
    }


    @Override
    public boolean equals(Object o){
        return o instanceof Complex &&
                ((Complex)o).real.equals(real) &&  ((Complex)o).imag.equals(imag);
    }

    @Override
    public String toString(){
        return (isReal() ? real + (isImag() ? " + " : "") : "") + (isImag() ? imag + "j" : isReal() ? "" : 
                                                                   isOrigin() ? real : "NAN");
    }

    @Override
    public String toFancyString(int idtlevel){
        return indent(idtlevel) + this;
    }

    @Override
    public String toFullString(int idtlevel){
        String ret = indent(idtlevel) + "Complex '"+this+"':\n";
        ret += indent(idtlevel + 1) + "Real: " + real + "\n";
        ret += indentE(idtlevel + 1) + "Imag: " + imag + "\n";
        return ret + indentE(idtlevel);

    }

    @Override
    public Complex copy(){
        return new Complex(this);
    }

}