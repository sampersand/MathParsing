package West.Math;
import java.lang.Comparable;
import java.lang.Number;

public class ComplexNumber extends Number implements DoubleSupplier, Comparable<Number> {

    private Double real;
    private Double imag;

    public ComplexNumber(){
        this(0D, 0D);
    }

    public ComplexNumber(Double pR, Double pI){
        real = pR;
        imag = pI;
    }


    public Double real(){
        return real;
    }
    public Double imag(){
        return imag;
    }

    @Override
    public Double getAsDouble(){
        return null;
    }

    @Override
    public int compareTo(Number n){
        return 0;
    }

    @Override
    public double doubleValue(){
        return 0;
    }
    @Override
    public float floatValue(){
        return 0;
    }
    @Override
    public int intValue(){
        return 0;
    }
    @Override
    public long longValue() {
        return 0;
    }

    @Override
    public String toString(){
        return real + " + " + imag + "j";
    }
}