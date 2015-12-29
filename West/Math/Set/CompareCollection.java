package West.Math.Set;
import West.Math.Exception.NotDefinedException;
import West.Math.Set.Collection;
import java.util.ArrayList;
import West.Math.Equation.Token;

/**
 * TODO: JAVADOC
 * LOL i'm going to have to figure out how to spell 'comparator' correctly XD
 * 
 * @author Sam Westerman
 * @version 0.75
 * @since 0.75
 */ 
public class CompareCollection<E> extends Collection<E> {


    public static final Collection<String> COMPARATOR = new Collection<String>()
    {{
        add("<="); //not sure if this works
        add(">="); //not sure if this works
        add("!="); //not sure if this works
        add("<");
        add(">");
        add("=");
        add("≠");
        add("≥");
        add("≤");
    }};


    protected String comparator; // its an object so functions can also use this

    public CompareCollection(){
        super();
        comparator = "=";
    }
    public CompareCollection(ArrayList<E> pElements){
        super(pElements);
        comparator = "=";
    }

    public CompareCollection(E[] pElements) {
        super(pElements);
        comparator = "=";
    }

    public CompareCollection(CompareCollection<E> pCollection) {
        super(pCollection);
        comparator = "=";
    }
    public String comparator(){
        return comparator;
    }

    public CompareCollection<E> setComparator(String pObj){
        assert pObj != null;
        comparator = pObj;
        return this;
    }

    public boolean compare(double val1, double val2){
        switch(comparator){
            case "<": return val1 < val2;
            case ">": return val1 > val2;
            case "=": return val1 == val2;
            case "≠": return val1 != val2;
            case "≥": case ">=":
                return val1 >= val2;
            case "≤": case "<=":
                return val1 <= val2;
            default:
                assert false; //shouldnt ever happen
        }
        return false;
    }

    public boolean equals(Object pObj){
        return super.equals(pObj) &&
               pObj instanceof CompareCollection &&
               comparator == ((CompareCollection)pObj).comparator();
    }
    public String toString(){
        return "'='" + super.toString();
    }
    public CompareCollection copy(){
        return new CompareCollection<E>(elements).setComparator(comparator);
    }
}
