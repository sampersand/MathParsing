package Math.Equation;
import Math.Exception.NotDefinedException;
import Math.Set.Collection;
import java.util.ArrayList;

public class CompareCollection<E> extends Collection<E> {
    public static enum Comparators {
        LT("<"),
        GT(">"),
        EQ("="),
        NEQ("≠"),
        LTE("≤"),
        GTE("≥");
        public String val;

        private Comparators(String pVal){
            val = pVal;
        }
        public Comparators from(String str){
            //DEFINE
            throw new NotDefinedException();
        }

    }
    protected Object comparator; // its an object so functions can also use this

    public CompareCollection(){
        super();
        comparator = Comparators.EQ;
    }
    public CompareCollection(ArrayList<E> pElements){
        super(pElements);
        comparator = Comparators.EQ;
    }

    public CompareCollection(E[] pElements) {
        super(pElements);
        comparator = Comparators.EQ;
    }

    public CompareCollection(CompareCollection<E> pCollection) {
        super(pCollection);
        comparator = Comparators.EQ;
    }
    

    public CompareCollection<E> setComparators(Object pObj){
        assert pObj != null;
        comparator = pObj;
        return this;
    }

    public boolean isFinal(){
        return elements.size() <= 1;
    }
}
