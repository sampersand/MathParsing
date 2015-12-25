package Math.Equation;
import Math.MathObject;
import Math.Exception.NotDefinedException;
import java.util.HashMap;
/**
 * TODO: JAVADOC
 * 
 * @author Sam Westerman
 * @version 0.71
 * @since 0.71
 */
public class Domain implements MathObject{
    private HashMap<String, Inequality> domains;
    private double step;
    private String args;
    public Domain(){
        //TODO: DEFINE DOMAIN
    }
    public Domain(Object... pObj){
        //TODO: DEFINE DOMAIN
        for(Object o : pObj)
            args += o;
    }

    @Override
    public String toString() {
        throw new NotDefinedException();
    }
    
    @Override
    public String toFancyString(int idtLvl) {
        return args;
        
        // throw new NotDefinedException();
    }

    @Override
    public String toFullString(int idtLvl) {
        return args;
        // throw new NotDefinedException();
    }

    @Override
    public Domain copy(){
        throw new NotDefinedException();
    }

    @Override
    public boolean equals(Object pObj){
        throw new NotDefinedException();
    }
    class Inequality{

        public Inequality(){}
    }
}