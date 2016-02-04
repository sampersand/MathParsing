package West.Math; 
import java.util.HashMap;
import West.Math.Equation.EquationSystem;
public interface DoubleSupplier {
    public Double toDouble(HashMap<String, DoubleSupplier> hm, EquationSystem eqsys);
    public default Double toDouble(){
        return toDouble(new HashMap<String, DoubleSupplier>(), new EquationSystem());
    }
    public boolean isNaN();
    public DoubleSupplier div(DoubleSupplier d);
    public DoubleSupplier mult(DoubleSupplier d);
    public DoubleSupplier plus(DoubleSupplier d);
    public DoubleSupplier minus(DoubleSupplier d);
    public DoubleSupplier pow(DoubleSupplier d);
}