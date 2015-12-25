package Math.Equation;
import Math.Exception.NotDefinedException;
public class Collection {
    public static final enum SIGN = {
        LT("<"),
        GT(">"),
        EQ("="),
        NEQ("≠"),
        LTE("≤"),
        GTE("≥");
        public String val;

        private equalities(String pVal){
            val = pVal;
        }
        public SIGN from(String str){
            //DEFINE
            throw new NotDefinedException();
        }

    }
    //TODO: THIS CLASS
}
