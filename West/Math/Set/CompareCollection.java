package West.Math.Set;
import West.Math.Exception.NotDefinedException;
import West.Math.Set.Collection;
import java.util.ArrayList;
import West.Math.Equation.Token;

import java.util.HashMap;
/**
 * TODO: JAVADOC
 * LOL i'm going to have to figure out how to spell 'comparator' correctly XD
 * 
 * @author Sam Westerman
 * @version 0.75
 * @since 0.75
 */ 
public class CompareCollection<E> extends Collection<E> {


    public static final HashMap<String, Comparator> COMPARATOR = new HashMap<String, Comparator>()
    {{
        put("<", Comparator.LT);
        put(">", Comparator.GT);
        put("=", Comparator.EQ);
        put("≠", Comparator.EQ);
        put("≥", Comparator.EQ);
        put("=", Comparator.EQ);
    }};


    public static enum Comparator {
        LT("<"),
        GT(">"),
        EQ("="),
        NEQ("≠"),
        LTE("≤"),
        GTE("≥");
        private String val;

        private Comparator(String pVal){
            val = pVal;
        }
        public String val(){
            return val;
        }

        public Comparator from(String str){
            //DEFINE
            throw new NotDefinedException();
        }

    }
    protected Comparator comparator; // its an object so functions can also use this

    public CompareCollection(){
        super();
        comparator = Comparator.EQ;
    }
    public CompareCollection(ArrayList<E> pElements){
        super(pElements);
        comparator = Comparator.EQ;
    }

    public CompareCollection(E[] pElements) {
        super(pElements);
        comparator = Comparator.EQ;
    }

    public CompareCollection(CompareCollection<E> pCollection) {
        super(pCollection);
        comparator = Comparator.EQ;
    }
    public Comparator comparator(){
        return comparator;
    }

    public CompareCollection<E> setComparator(Comparator pObj){
        assert pObj != null;
        comparator = pObj;
        return this;
    }

}
