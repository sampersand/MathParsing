package West.Math; 
import java.util.HashMap;
import West.Math.Equation.EquationSystem;
public interface DoubleSupplier {
    public Double getAsDouble(HashMap<String, DoubleSupplier> hm, EquationSystem eqsys);
    public default Double getAsDouble(){
        return getAsDouble(new HashMap<String, DoubleSupplier>(), new EquationSystem());
    }
}