package West.Math;
import java.lang.Comparable;
import java.lang.Number;
import java.util.HashMap;
import West.Math.Equation.EquationSystem;
import static java.lang.Double.NaN;
public class ComplexNumber extends Number implements DoubleSupplier, Comparable<Number> {

    private Double real;
    private Double imag;

    public ComplexNumber(){
        this(NaN, NaN);
    }

    public ComplexNumber(Double pR){
        this(pR, NaN);
    }

    public ComplexNumber(double pR){
        this(new Double(pR) , NaN);
    }
    public ComplexNumber(Double pR, Double pI){
        real = pR == null ? NaN : pR;
        imag = pI == null ? NaN : pI;
    }


    public Double real(){
        return real;
    }
    public Double imag(){
        return imag;
    }

    @Override
    public Double getAsDouble(HashMap<String, DoubleSupplier> hm, EquationSystem eqsys){
        assert eqsys.eval("__TEMP__",new EquationSystem().add("__TEMP__=<" + real + "," + imag + ">")).isOnlyReal();
        return eqsys.eval("__TEMP__",new EquationSystem().add("__TEMP__=<" + real + "," + imag + ">")).real();
    }

    public ComplexNumber aIsOnlyReal(){
        assert isOnlyReal();
        return this;
    }
    public boolean isOnlyReal(){
        return real.equals(0D) && (imag == 0 || imag.isNaN());
    }
    public boolean isOnlyImag(){
        return imag.equals(0D) && (real == 0 || real.isNaN());
    }

    public boolean isNaN(){
        return imag.isNaN() && real.isNaN();
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

    @Override
    public String toString(){
        return real + " + " + imag + "j";
    }


    // Standard Arithmetic
    public ComplexNumber div(ComplexNumber c){
        ComplexNumber top = times(c.conj());
        return new ComplexNumber(top.real / c.times(c.conj()).aIsOnlyReal().real, top.imag / c.times(c.conj()).real);
    }

    public ComplexNumber times(ComplexNumber c){
        return new ComplexNumber(real * c.real - imag * c.imag, real * c.imag + imag * c.real);
    }

    public ComplexNumber plus(ComplexNumber c){
        return new ComplexNumber(real + c.real, imag + c.imag);
    }

    public ComplexNumber minus(ComplexNumber c){
        return new ComplexNumber(real - c.real, imag - c.imag);
    }


    // Non-Standard Arithmetic
    public ComplexNumber pow(Double d){
        return pow(new ComplexNumber(d));
    }

    public ComplexNumber pow(ComplexNumber d){
        assert false;
        return null;
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


}