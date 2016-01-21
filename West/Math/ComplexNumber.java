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
    public static final ComplexNumber INF_P = new ComplexNumber(1E9);
    public static final ComplexNumber INF_N = new ComplexNumber(-1E9);
    public static final ComplexNumber UNIT = new ComplexNumber(1); //Unit vector
    public static final String COMPLEX_REGEX= "^((.+)(?![\\d.]*(?:i|j)))?"+
                                              "(?:(.*)(?:i|j))?$";

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
        assert eqsys.eval("__TEMP__",new EquationSystem().add("__TEMP__=<" + real + "," + imag + ">")).isOnlyReal();
        return eqsys.eval("__TEMP__",new EquationSystem().add("__TEMP__=<" + real + "," + imag + ">")).real();
    }

    public boolean isOnlyReal(){ return isReal() && !isImag(); }
    public boolean isOnlyImag(){ return isImag() && !isReal(); }
    public boolean isReal(){ return !real.isNaN() &&! real.equals(0D); }
    public boolean isImag(){ return !imag.isNaN() &&! imag.equals(0D); }
    public ComplexNumber aIsOnlyReal(){
        assert isOnlyReal() : "Complex Number '"+this+"' has to have no imaginary component!";
        return this;
    }

    public boolean isNaN(){ return equals(NaN); }

    public static ComplexNumber parseComplex(String s){
        s = s.trim().replaceAll(" ","");
        System.out.println("parsing '"+s+"'");
        assert s.matches(COMPLEX_REGEX) : "'" + s + "' isn't a complex number!";
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
        assert false;
        return 0;
    }

    @Override
    public double doubleValue(){
        assert false;
        return 0;
    }

    @Override
    public float floatValue(){
        assert false;
        return 0;
    }

    @Override
    public int intValue(){
        assert false;
        return 0;
    }

    @Override
    public long longValue() {
        assert false;
        return 0;
    }


    // Standard Arithmetic
    public ComplexNumber div(ComplexNumber c){
        ComplexNumber top = times(c.conj());
        ComplexNumber bottom = c.times(c.conj()).aIsOnlyReal();
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
        if(isNaN() || c.isNaN()) return NaN;
        return new ComplexNumber(isReal() ? c.isReal() ? real + c.real : real : c.real,
                                 isImag() ? c.isImag() ? imag + c.imag : imag : c.imag);
    }

    public ComplexNumber minus(ComplexNumber c){
        return new ComplexNumber(isReal() ? c.isReal() ? real - c.real : real : -1 * c.real,
                                 isImag() ? c.isImag() ? imag - c.imag : imag : -1 * c.imag);
    }


    // Non-Standard Arithmetic
    public ComplexNumber pow(Double d){
        return pow(new ComplexNumber(d));
    }

    public ComplexNumber pow(ComplexNumber b){
        return ln().times(b).cosh().minus(ln().times(b).sinh());
    }

    public ComplexNumber factorial(){
        assert false;
        return null;
    }

    public ComplexNumber abs(){
        return new ComplexNumber(Math.abs(real), Math.abs(imag));
    }

    public ComplexNumber conj(){
        return new ComplexNumber(real, -1 * imag);
    }

    public ComplexNumber modulo(ComplexNumber c){
        assert false;
        return null;
    }

    // Trigonometric Functions
        // Standard Trigonometric Functions
    public ComplexNumber sin(){
        assert false;
        return null;
    }
    public ComplexNumber cos(){
        assert false;
        return null;
    }
    public ComplexNumber tan(){
        assert false;
        return null;
    }

        // Inverse Trigonometric Functions
    public ComplexNumber csc(){
        assert false;
        return null;
    }
    public ComplexNumber sec(){
        assert false;
        return null;
    }
    public ComplexNumber cot(){
        assert false;
        return null;
    }

        // Hyperbolic Trigonometric Functions
    public ComplexNumber sinh(){
        sinh(a,-i 5.9) = 2 sum_(k=0)^infinity I_(1+2 k)(a,-i 5.9)
        return Function.get("sinh").eval("n",0,);
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
        assert false;
        return null;
    }
    public ComplexNumber floor(){
        assert false;
        return null;
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
        return UNIT;
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
        return (isReal() ? real + (isImag() ? " + " : "") : "") + (isImag() ? imag + "j" : isReal() ? "" : "NaN");
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