package West.Math; 
import java.util.HashMap;
import West.Math.Equation.EquationSystem;
public interface Operable<O> {
    public Double toDouble(HashMap<String, Operable> hm, EquationSystem eqsys);
    public default Double toDouble(){
        return toDouble(new HashMap<String, Operable>(), new EquationSystem());
    }

    public boolean isNaN();
    public O div(Operable d);
    public O mult(Operable d);
    public O plus(Operable d);
    public O minus(Operable d);
    public O pow(Operable d);
    public O sqrt();
    public O squared();
    public O cubed();

}