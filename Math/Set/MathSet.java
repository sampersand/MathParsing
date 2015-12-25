package Math.Set;

import Math.MathObject;
import Math.Print;
import java.util.ArrayList;
/**
 * The class that represents Sets in mathematics - that is, each element in them has to be unique.
 * 
 * @author Sam Westerman
 * @version 0.1
 */
public class MathSet extends Group {

    public MathSet(){
        super();
    }
    public MathSet(ArrayList<Double> pEle){
        super(pEle);
        assert isUnique();
    }
    public MathSet(Group pGroup){
        super(pGroup);
        assert isUnique() : "cannot instatiate a non-unique MathSet.";
    }

    public boolean isUnique(){
        Group ms = super.copy();
        while(ms.size() > 0)
            if(ms.elements().contains(ms.pop()))
                return false;
        return true;
    }


    @Override
    public MathSet copy(){
        return new MathSet(elements);
    }

}