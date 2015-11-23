import java.util.HashMap;
/** 
 * TODO: make javadoc for this thing.
 */
public class Node {
    public String val;
    public Types type;
    public static enum Types{LPAR, RPAR, VAR, NUM, FUNC, OPER, NULL, DELIM}
    public static final Node LPAR = new Node("(",Types.LPAR);
    public static final Node RPAR = new Node(")",Types.RPAR);
    public Node(){
        this(null,null);
    }
    public Node(String pVal, Types pType){
        val = pVal;
        type = pType;
    }
    public Node(char pVal, Types pType){
        val = "" + pVal;
        type = pType;
    }

    public String toString(){
        String ret = "('" + val + "': ";
        switch(type){
            case LPAR: ret += "LPAR"; break;
            case RPAR: ret += "RPAR"; break;
            case VAR:  ret +=  "VAR"; break;
            case NUM:  ret +=  "NUM"; break;
            case FUNC: ret += "FUNC"; break;
            case OPER: ret += "OPER"; break;
            case NULL: ret += "NULL"; break;
            case DELIM: ret += "DELIM"; break;
            default: ret += "UHOH"; break;
        }
        return ret + ")";
    }
}