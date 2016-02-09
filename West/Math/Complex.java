package West.Math;
import java.lang.Comparable;
import java.lang.Number;
import java.util.HashMap;
import West.Math.Equation.EquationSystem;
import West.Math.MathObject;
import West.Math.Equation.Token;
import West.Math.Equation.Function.Function;
import West.Math.Set.Node.TokenNode;
import West.Math.Set.MathCollection;

public class Complex extends Number implements Operable<Complex>, Comparable<Number>, MathObject {

    public static final Complex NAN = new Complex();
    public static final Complex P_INF = new Complex(1E4);
    public static final Complex N_INF = new Complex(-1E9);
    public static final Complex ONE = new Complex(1d); //Unit vector
    public static final Complex N_ONE = new Complex(-1d);
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
    public Double toDouble(HashMap<String, Operable> hm, EquationSystem eqsys){
        if(isNaN())
            return Double.NaN;
        return isOnlyReal() ? real : isOnlyImag() ? imag : ((Complex)eqsys.eval("__TEMP__",
                new EquationSystem().add("__TEMP__=hypot(" +
                                         (isReal() ? real : "") + (isBoth() ? "," : "") + 
                                         (isImag() ? imag : "") + ")"))).aIsOnlyReal().real;
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
    @Override
    public boolean isNaN(){ return equals(NAN); }//!isReal() && !isImag();}

    public static Complex parseComplex(String s){
        s = s.trim().replaceAll(" ","");
        if(!s.matches(COMPLEX_REGEX))
                throw new NumberFormatException("'" + s + "' isn't a complex number!");
        String s0 = s.replaceAll(COMPLEX_REGEX, "$1");
        String s1 = s.replaceAll(COMPLEX_REGEX, "$2");
        Double r = s0.isEmpty() ? Double.NaN : 
                TokenNode.getVarInFinal(new HashMap<String, Operable>(), s0).get(s0).toDouble();
        Double i = s1.isEmpty() ? Double.NaN : 
                TokenNode.getVarInFinal(new HashMap<String, Operable>(), s1).get(s1).toDouble();
        return new Complex(r, i);
    }

    @Override
    public int compareTo(Number n){
        if(!isOnlyReal() || (n instanceof Complex ? !((Complex)n).isOnlyReal() : false))
            System.err.println("inequalities are neg well-defined in the complex plane");
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
    public static Complex div  (Number c, Number b){
        return (Complex) (c instanceof Complex ? (Complex) c : new Complex(c)).div
            (b instanceof Complex ? (Complex) b : new Complex(b));
    }
    public static Complex mult (Number c, Number b){
        return (Complex) (c instanceof Complex ? (Complex) c : new Complex(c)).mult
            (b instanceof Complex ? (Complex) b : new Complex(b));
    }
    public static Complex plus (Number c, Number b){
        return (Complex) (c instanceof Complex ? (Complex) c : new Complex(c)).plus
            (b instanceof Complex ? (Complex) b : new Complex(b));
    }
    public static Complex minus(Number c, Number b){
        return (Complex) (c instanceof Complex ? (Complex) c : new Complex(c)).minus
            (b instanceof Complex ? (Complex) b : new Complex(b));
    }
    public static Complex pow  (Number c, Number b){
        return (Complex) (c instanceof Complex ? (Complex) c : new Complex(c)).pow 
            (b instanceof Complex ? (Complex) b : new Complex(b));
    }

    @Override
    public Complex div(Operable ds){
        if(ds instanceof MathCollection){
            assert false : "TODO: ME";
            return this;
        }        assert ds instanceof Complex;
        Complex c = (Complex)ds;
        if (isOnlyReal() && c.isOnlyReal()){
            return new Complex(real * 1f / c.real);
        }
        if(isNaN() && c.isNaN())
            return NAN;
        if(isNaN() || c.isNaN())
            return NAN; 
        // if(isNaN() ^ c.isNaN()) return isNaN() ? c : this;

        Complex top = mult(c.imagConjugate());
        Complex bottom = c.mult(c.imagConjugate()).aIsOnlyReal();
        return new Complex(top.real / bottom.real, top.imag / bottom.real);
    }

    @Override
    public Complex mult(Operable ds){
        if(ds instanceof MathCollection){
            assert false : "TODO: ME";
            return this;
        }
        assert ds instanceof Complex;
        Complex c = (Complex)ds;
        if (isOnlyReal() && c.isOnlyReal()){
            return new Complex(real * c.real);
        }
        if(isNaN() && c.isNaN())
            return NAN;
        if(isNaN() || c.isNaN())
            return NAN; 
        // if(isNaN() ^ c.isNaN()) return isNaN() ? c : this;

        Double rreal = Double.NaN, rimag = Double.NaN;

        if(isReal() && c.isReal()) rreal = real * c.real;
        if(isImag() && c.isImag()) rreal = (rreal.isNaN() ? 0 : rreal) - imag * c.imag;

        if(isReal() && c.isImag()) rimag = real * c.imag;
        if(isImag() && c.isReal()) rimag = (rimag.isNaN() ? 0 : rimag) + imag * c.real;
        return new Complex(rreal, rimag);
    }

    @Override
    public Complex plus(Operable ds){
        if(ds instanceof MathCollection){
            assert false : "TODO: ME";
            return this;
        }        assert ds instanceof Complex;
        Complex c = (Complex)ds;
        if (isOnlyReal() && c.isOnlyReal()){
            return new Complex(real + c.real);
        }
        if(isNaN() && c.isNaN())
            return NAN;
        if(isNaN() || c.isNaN())
            return NAN; 
        // if(isNaN() ^ c.isNaN()) return isNaN() ? c : this;
        return new Complex(isReal() ? c.isReal() ? real + c.real : real : c.real,
                                 isImag() ? c.isImag() ? imag + c.imag : imag : c.imag);
    }

    @Override
    public Complex minus(Operable ds){
        if(ds instanceof MathCollection){
            assert false : "TODO: ME";
            return this;
        }        assert ds instanceof Complex;
        Complex c = (Complex)ds;
        if (isOnlyReal() && c.isOnlyReal()){
            return new Complex(real - c.real);
        }
        if(isNaN() && c.isNaN())
            return NAN;
        if(isNaN() || c.isNaN())
            return NAN; 
        // if(isNaN() ^ c.isNaN()) return isNaN() ? c : this;
        return new Complex(isReal() ? c.isReal() ? real - c.real : real : -1 * c.real,
                                 isImag() ? c.isImag() ? imag - c.imag : imag : -1 * c.imag);
    }

    @Override
    public Complex pow(Operable ds){
        if(ds instanceof MathCollection){
            assert false : "TODO: ME";
            return this;
        }
        Complex c = (Complex)ds;
        if (isOnlyReal() && c.isOnlyReal()){
            return new Complex(Math.pow(real, c.real));
        }
        if(isNaN() && c.isNaN())
            return NAN;
        if(isNaN() || c.isNaN())
            return NAN; 
        // if(isNaN() ^ c.isNaN()) return isNaN() ? c : this;
        return isOnlyReal() && c.isOnlyReal() ?
                new Complex(Math.pow(aIsOnlyReal().real, c.aIsOnlyReal().real))
                :
                new Complex(Complex.mult(Math.exp(real), Math.cos(imag)),
                                  Complex.mult(Math.exp(real), Math.sin(imag)));

    }



    @Override
    public Complex sqrt(){
        return pow(this, new Complex(0.5d));
    }

    @Override
    public Complex squared(){
        return pow(this, new Complex(2d));
    }

    @Override
    public Complex cubed(){
        return pow(this, new Complex(3d));
    }

    public Complex factorial(){
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



    //logic
    public Complex and(Complex c){
        return isNaN() ? c : this;
    }
    public Complex or(Complex c){
        return isNaN() ? this : c;
    }
    public Complex xor(Complex c){
        return isNaN() ? c.isNaN() ? NAN : c : c.isNaN() ? this : NAN;
    }
    public Complex nand(Complex c){
        return and(c).not();
    }
    public Complex nor(Complex c){
        return or(c).not();
    }
    public Complex not(){
        return isNaN() ? ONE : NAN;
    }
    public Complex neg(){
        return mult(N_ONE);
    }


    public Complex eq(Complex c){
        return  equals(c) ? ONE : NAN;
    }
    public Complex neq(Complex c){
        return !equals(c) ? ONE : NAN;
    }

    public Complex lt(Complex c){
        return compareTo(c) <  0 ? ONE : NAN;
    }
    public Complex gt(Complex c){
        return compareTo(c) >  0 ? ONE : NAN;
    }
    public Complex lte(Complex c){
        return compareTo(c) <= 0 ? ONE : NAN;
    }
    public Complex gte(Complex c){
        return compareTo(c) >= 0 ? ONE : NAN;
    }

    //bitwise
    public Complex band(Complex c){
        return new Complex(intValue() & c.intValue());
    }
    public Complex bor(Complex c){
        return new Complex(intValue() | c.intValue());
    }
    public Complex bxor(Complex c){
        return new Complex(intValue() ^ c.intValue());
    }
    public Complex bneg(){ //bitwise negate
        return new Complex(~intValue());
    }
    public Complex bsl(Complex c){
        return new Complex(intValue() << c.intValue());
    }
    public Complex bsr(Complex c){
        return new Complex(intValue() >> c.intValue());
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
    public Complex clone(){
        return new Complex(this);
    }

}