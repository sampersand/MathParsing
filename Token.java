import java.util.HashMap;
/** 
 * TODO: make javadoc for this thing.
 */
public class Token {
    public final String VAL;
    public final Types TYPE;
    public static enum Types{LPAR, RPAR, VAR, NUM, FUNC, OPER, NULL, DELIM, GROUP}
    public static final Token LPAR = new Token("(",Types.LPAR);
    public static final Token RPAR = new Token(")",Types.RPAR);
    public Token(){
        this(null,null);
    }
    public Token(String pVal, Types pType){
        VAL = pVal;
        TYPE = pType;
    }
    public Token(char pVal, Types pType){
        VAL = "" + pVal;
        TYPE = pType;
    }

    public String toString(){
        String ret = "('" + VAL + "': ";
        switch(TYPE){
            case LPAR: ret += "LPAR"; break;
            case RPAR: ret += "RPAR"; break;
            case VAR:  ret +=  "VAR"; break;
            case NUM:  ret +=  "NUM"; break;
            case FUNC: ret += "FUNC"; break;
            case OPER: ret += "OPER"; break;
            case NULL: ret += "NULL"; break;
            case DELIM: ret += "DELIM"; break;
            case GROUP: ret += "GROUP"; break;
            default: ret += "UHOH"; break;
        }
        return ret + ")";
    }
}