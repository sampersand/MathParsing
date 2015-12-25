package Math.Set;

import Math.MathObject;
import Math.Print;
import Math.Exception.NotDefinedException;
import java.util.ArrayList;
/**
 * The class that represents Sets in mathematics - that is, each element in them has to be unique.
 * 
 * @author Sam Westerman
 * @version 0.1
 */
public class MathSet<E extends Double> extends Group<E> {

    public MathSet(){
        super();
    }
    public MathSet(ArrayList<E> pEle){
        super(pEle);
        assert isUnique();
    }
    public MathSet(Group pGroup){
        super(pGroup);
        assert isUnique() : "cannot instatiate a non-unique MathSet.";
    }

    public MathSet(String setBuilderNotation){
        throw new NotDefinedException();
        // return new Group(new EquationSystebuildSet()
    }
    /**
     * returns false if the thing cannot add it
     */
    public boolean add(E pEle){ 
        if(elements.contains(pEle)){
            Print.printw("Trying to add a duplicate element (" + pEle +") to this. Doing nothing instead!");
            return false;
        }
        elements.add(pEle);
        return true; // for some bizarre reason, ArrayList does this too.
    }

    @Override
    public MathSet copy(){
        return new MathSet(elements);
    }

}