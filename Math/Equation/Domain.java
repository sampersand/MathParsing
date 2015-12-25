package Math.Equation;
import Math.MathObject;
import Math.Exception.NotDefinedException;
/**
 * TODO: JAVADOC
 * 
 * @author Sam Westerman
 * @version 0.71
 * @since 0.71
 */
public class Domain implements MathObject{

    public Domain(){

    }

    @Override
    public String toString() {
        throw new NotDefinedException();
    }
    
    @Override
    public String toFancyString(int idtLvl) {
        throw new NotDefinedException();
    }

    @Override
    public String toFullString(int idtLvl) {
        throw new NotDefinedException();
    }

    @Override
    public Domain copy(){
        throw new NotDefinedException();
    }

    @Override
    public boolean equals(Object pObj){
        throw new NotDefinedException();
    }
}