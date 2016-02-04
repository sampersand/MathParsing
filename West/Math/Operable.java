package West.Math; 
import java.util.HashMap;
import West.Math.Equation.EquationSystem;
public interface Operable {
    public Double toDouble(HashMap<String, Operable> hm, EquationSystem eqsys);
    public default Double toDouble(){
        return toDouble(new HashMap<String, Operable>(), new EquationSystem());
    }
    public boolean isNaN();
    public <D extends Operable> D div(D d);
    public <D extends Operable> D mult(D d);
    public <D extends Operable> D plus(D d);
    public <D extends Operable> D minus(D d);
    public <D extends Operable> D pow(D d);
}