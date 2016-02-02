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

    public static final ComplexNumber NAN = new ComplexNumber();
    public static final ComplexNumber INF_P = new ComplexNumber(1E4);
    public static final ComplexNumber INF_N = new ComplexNumber(-1E9);
    public static final ComplexNumber ONE = new ComplexNumber(1d); //Unit vector
    public static final ComplexNumber NEG_ONE = new ComplexNumber(-1d);
    public static final ComplexNumber ZERO = new ComplexNumber(0d);
    public static final ComplexNumber PI = new ComplexNumber(Math.PI);
    public static final ComplexNumber E = new ComplexNumber(Math.E);

    public static final String COMPLEX_REGEX= "^(.+?(?=.*[^ij]))?\\+?(?:(.*)[ij])?$";
    // public static final String COMPLEX_REGEX= "^(.+?(?=.*[^ij]))?\\+?(?:(.*)[ij])?$";
    
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

    public ComplexNumber(Number pR){
        this(pR, Double.NaN);
    }

    public ComplexNumber(ComplexNumber cn){
        this(cn.real, cn.imag);
    }

    public ComplexNumber(Number pR, Number pI){
        real = pR == null ? Double.NaN : (Double)pR;
        imag = pI == null ? Double.NaN : (Double)pI;
    }
    public ComplexNumber(ComplexNumber pR, ComplexNumber pI){
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
        return eqsys.eval("__TEMP__",
                new EquationSystem().add("__TEMP__=hypot(" +
                                         (isReal() ? real : "") + (isBoth() ? "," : "") + 
                                         (isImag() ? imag : "") + ")")).aIsOnlyReal().real;
    }
    public boolean isBoth(){return isImag() && isReal();}
    public boolean isOnlyReal(){ return isReal() && !isImag(); }
    public boolean isOnlyImag(){ return isImag() && !isReal(); }
    public boolean isOrigin(){ return real.equals(ZERO) && real.equals(ZERO);}
    public boolean isReal(){ return !real.isNaN();}// && !new ComplexNumber(real).equals(ZERO); }
    public boolean isImag(){ return !imag.isNaN();}// && !new ComplexNumber(imag).equals(ZERO); }
    public ComplexNumber aIsOnlyReal(){
        assert isOnlyReal() : "Complex Number '" + this + "' has to have no imaginary component!";
        return this;
    }

    public boolean isNaN(){  return equals(NAN);}//!isReal() && !isImag();}

    public static ComplexNumber parseComplex(String s){
        s = s.trim().replaceAll(" ","");
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
        ComplexNumber cn = (ComplexNumber)n;
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
    public static ComplexNumber div(Number c, Number b){
        return new ComplexNumber(c).div(new ComplexNumber(b));
    }

    public static ComplexNumber mult(Double c, Double b){
        return new ComplexNumber(c).mult(new ComplexNumber(b));
    }

    public static ComplexNumber plus(Double c, Double b){
        return new ComplexNumber(c).plus(new ComplexNumber(b));
    }
    
    public static ComplexNumber minus(Double c, Double b){
        return new ComplexNumber(c).minus(new ComplexNumber(b));
    }
    public ComplexNumber div(ComplexNumber c){
        if (isOnlyReal() && c.isOnlyReal()){
            return new ComplexNumber(real * 1f / c.real);
        }
        if(isNaN() && c.isNaN()) return NAN;
        if(isNaN() || c.isNaN()) return NAN; 
        // if(isNaN() ^ c.isNaN()) return isNaN() ? c : this;

        ComplexNumber top = mult(c.imagConjugate());
        ComplexNumber bottom = c.mult(c.imagConjugate()).aIsOnlyReal();
        return new ComplexNumber(top.real / bottom.real, top.imag / bottom.real);
    }

    public ComplexNumber mult(ComplexNumber c){
        if (isOnlyReal() && c.isOnlyReal()){
            return new ComplexNumber(real * c.real);
        }
        if(isNaN() && c.isNaN()) return NAN;
        if(isNaN() || c.isNaN()) return NAN; 
        // if(isNaN() ^ c.isNaN()) return isNaN() ? c : this;

        Double rreal = Double.NaN, rimag = Double.NaN;

        if(isReal() && c.isReal()) rreal = real * c.real;
        if(isImag() && c.isImag()) rreal = (rreal.isNaN() ? 0 : rreal) - imag * c.imag;

        if(isReal() && c.isImag()) rimag = real * c.imag;
        if(isImag() && c.isReal()) rimag = (rimag.isNaN() ? 0 : rimag) + imag * c.real;
        return new ComplexNumber(rreal, rimag);
    }

    public ComplexNumber plus(ComplexNumber c){
        if (isOnlyReal() && c.isOnlyReal()){
            return new ComplexNumber(real + c.real);
        }
        if(isNaN() && c.isNaN()) return NAN;
        if(isNaN() || c.isNaN()) return NAN; 
        // if(isNaN() ^ c.isNaN()) return isNaN() ? c : this;
        return new ComplexNumber(isReal() ? c.isReal() ? real + c.real : real : c.real,
                                 isImag() ? c.isImag() ? imag + c.imag : imag : c.imag);
    }

    public ComplexNumber minus(ComplexNumber c){
        if (isOnlyReal() && c.isOnlyReal()){
            return new ComplexNumber(real - c.real);
        }
        if(isNaN() && c.isNaN()) return NAN;
        if(isNaN() || c.isNaN()) return NAN; 
        // if(isNaN() ^ c.isNaN()) return isNaN() ? c : this;
        return new ComplexNumber(isReal() ? c.isReal() ? real - c.real : real : -1 * c.real,
                                 isImag() ? c.isImag() ? imag - c.imag : imag : -1 * c.imag);
    }

    // Non-Standard Arithmetic
    public ComplexNumber pow(Double d){
        return pow(new ComplexNumber(d));
    }

    public static ComplexNumber pow(Double c, Double b){
        return new ComplexNumber(c).pow(new ComplexNumber(b));
    }
    public ComplexNumber pow(ComplexNumber c){
        if (isOnlyReal() && c.isOnlyReal()){
            return new ComplexNumber(Math.pow(real, c.real));
        }
        if(isNaN() && c.isNaN()) return NAN;
        if(isNaN() || c.isNaN()) return NAN; 
        // if(isNaN() ^ c.isNaN()) return isNaN() ? c : this;
        return isOnlyReal() && c.isOnlyReal() ?
                new ComplexNumber(Math.pow(aIsOnlyReal().real, c.aIsOnlyReal().real))
                :
                new ComplexNumber(ComplexNumber.mult(Math.exp(real), Math.cos(imag)),
                                  ComplexNumber.mult(Math.exp(real), Math.sin(imag)));

    }
    public ComplexNumber sqrt(){
        return pow(0.5d);
    }

    public ComplexNumber factorial(){
        // System.out.println("TODO: LN");
        return isNaN() ? this : compareTo(ONE) <= 0 ? ONE : this.mult(minus(ONE).factorial());
    }

    public ComplexNumber abs(){
        return new ComplexNumber(isReal() ? Math.abs(real) : Double.NaN,
                                 isImag() ? Math.abs(imag) : Double.NaN);
    }

    public ComplexNumber magnitude(){
        return new ComplexNumber(toDouble());
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
        return plus(c.mult(div(c).realConjugate().ceil()));
    }

    // Trigonometric Functions
        // Standard Trigonometric Functions
    public ComplexNumber sin() {
        return isOnlyReal() ?
                new ComplexNumber(Math.sin(aIsOnlyReal().toDouble()))
                :
                new ComplexNumber(ComplexNumber.mult(+Math.sin(real), +Math.cosh(imag)),
                                  ComplexNumber.mult(+Math.cos(real), +Math.sinh(imag)));
    }

    public ComplexNumber cos() {
        return isOnlyReal() ?
                new ComplexNumber(Math.cos(aIsOnlyReal().toDouble()))
                :
                new ComplexNumber(ComplexNumber.mult(+Math.cos(real), +Math.cosh(imag)),
                                  ComplexNumber.mult(-Math.sin(real), +Math.sinh(imag)));
    }

    // public ComplexNumber sin(){
    //     return isOnlyReal() ? new ComplexNumber(Math.sin(real)) : new EquationSystem().eval("__TEMP0__",
    //             new EquationSystem().add("__TEMP1__="+this).add(
    //                 "__TEMP0__=Σ(__TEMP2__, 0, ∞, __TEMP3__, "+
    //                 "__TEMP3__=(–1)^__TEMP2__·__TEMP1__^(2·__TEMP2__+1)/fac(2·__TEMP2__+1))"));
    // }
    // public ComplexNumber cos(){
    //     // return magnitude().plus(new Equ    ationSystem().add("__TEMP__=¼·π").eval("__TEMP__")).sin();
    //     // return isOnlyReal() ? new ComplexNumber(Math.sin(real)) : new EquationSystem().eval("__TEMP0__",
    //     return new EquationSystem().eval("__TEMP0__",
    //             new EquationSystem().add("__TEMP1__=" + this).add(
    //                 "__TEMP0__=Σ(__TEMP2__, 0, ∞, __TEMP3__, " +
    //                 "__TEMP3__=(–1)^__TEMP2__·__TEMP1__^(2·__TEMP2__)/fac(2·__TEMP2__))"));
    // }
    public ComplexNumber tan(){
        return isOnlyReal() ?
               new ComplexNumber(Math.tan(real))
               :
               sin().div(cos());
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
        return new ComplexNumber(Math.sinh(aIsOnlyReal().real));
    }
    public ComplexNumber cosh(){
        return new ComplexNumber(Math.cosh(aIsOnlyReal().real));
    }
    public ComplexNumber tanh(){
        return new ComplexNumber(Math.tanh(aIsOnlyReal().real));
    }

        // Arc- Trigonometric Functions
    public ComplexNumber asin(){
        return new ComplexNumber(Math.asin(aIsOnlyReal().real));
    }
    public ComplexNumber acos(){
        return new ComplexNumber(Math.acos(aIsOnlyReal().real));
    }
    public ComplexNumber atan(){
        return new ComplexNumber(Math.atan(aIsOnlyReal().real));
    }


    // Byte shifting
    public ComplexNumber byteShiftLeft(ComplexNumber c){
        return new ComplexNumber(intValue() << c.intValue());
    }
    public ComplexNumber byteShiftRight(ComplexNumber c){
        return new ComplexNumber(intValue() >> c.intValue());
    }

    // Misc
        // Rounding
    public ComplexNumber ceil(){
        return new ComplexNumber(isReal() ? Math.ceil(real) : Double.NaN, isImag() ? Math.ceil(imag) : Double.NaN);
    }
    public ComplexNumber floor(){
        return new ComplexNumber(isReal() ? Math.floor(real) : Double.NaN, isImag() ? Math.floor(imag) : Double.NaN);
    }
    public ComplexNumber round(){
        return round(0);
    }

    public ComplexNumber round(int decimals){
        ComplexNumber dec = ONE.minus(new ComplexNumber(new Double(Math.pow(10, 0-decimals))));
        return new ComplexNumber(mult(dec).intValue()).div(dec);
    }

        // Logarithms
    public ComplexNumber ln(){
        return new ComplexNumber(Math.log(aIsOnlyReal().real));
    }
    public ComplexNumber log10(){
        return new ComplexNumber(Math.log10(aIsOnlyReal().real));
    }
    public ComplexNumber log(ComplexNumber c){
        return new ComplexNumber(Math.log(aIsOnlyReal().real) / (1F * Math.log(c.aIsOnlyReal().real)));
    }

        // Changing
    public ComplexNumber toDegrees(){
        return aIsOnlyReal().mult(new ComplexNumber(180)).div(PI);
    }
    public ComplexNumber toRadians(){
        return aIsOnlyReal().mult(PI).div(new ComplexNumber(180));
    }


    @Override
    public boolean equals(Object o){
        return o instanceof ComplexNumber &&
                ((ComplexNumber)o).real.equals(real) &&  ((ComplexNumber)o).imag.equals(imag);
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