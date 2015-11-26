package Math.Equation;
import java.util.HashMap;
/** 
 * A single item from an equation String.
 * Example: "sin(x+2)" yields the tokens <code>{"sin":FUNC, "(":LPAR, "x":VAR, "+":OPER, "2":NUM, ")":RPAR}</code>.
 * @author Sam Westerman
 * @version 0.1
 */
public class Token {
    /** The string that this class is based upon. */
    public final String VAL;

    /** The type of token. Used to distinguish between things such as functions, groups, and operators. */
    public final Types TYPE;

    /**
     * The different types of tokens. They are used to determine what to do with the tokens (and, eventually, the
     * {@link Node}s they go into.
     */
    public static enum Types {  //im doing the "," on the next line so as to make Sublime Text 3 not hate me.
        /**
         * A left parenthesis. It's VAL is only ever equal to "(". 
         * Used when creating {@link Node nodes} to determine what goes inside a {@link #FUNC function} / 
         * {@link #GROUP group}.         
         */
        LPAR
        ,
        /**
         * A right parenthesis. It's VAL is only ever equal to ")".
         * Used when creating {@link Node nodes} to determine what goes inside a {@link #FUNC function} / 
         * {@link #GROUP group}.
         */ 
        RPAR
        ,
        /**
         * A varriable. Distinguished from a function when parsing only because it isn't immediately before "(". 
         * <p> If a {@link Node} is being {@link Factors#eval(Node) evaluated}, and it's {@link Node#TOKEN token}
         * is of this type, the {@link Factors} evaluating it will check it's {@link Factors#vars} for a varriable with
         * the same name as {@link #VAL}.
         * <p> Note: VAL should only contain letters, as anything that isn't alphanumerical, or <code>()+*-/^</code> 
         * won't be parsed.
         * @see Node
         * @see Factors
         */
        VAR
        ,
        /**
         * A number. Can have a decimal point.
         */
        NUM
        ,
        /**
         * A function. Distinguished from a varriable when parsing only because it immediately preceeds "(". 
         * <p> If a {@link Node} is being {@link Factors#eval(Node) evaluated}, and it's {@link Node#TOKEN token}
         * is of this type, the {@link Factors} evaluating it will check it's {@link Factors#funcs} for a function with
         * the same name as {@link #VAL}.
         * <p> Note: VAL should only contain letters, as anything that isn't alphanumerical, or <code>()+*-/^</code> 
         * won't be parsed.
         * @see Node
         * @see Factors
         */
        FUNC
        , 
        /**
         * An operator. Currently, the only operators are <code>+, -, *, /, and ^</code>. Things like "!" for factorial 
         * need to change to {@link #FUNC functions} (but "!" isn't a valid function name; Names can only have letters).
         */
        OPER
        ,
        /** 
         * A null value. Duh? Only ever used when a token doesn't fit into one of the previous catagories, or is the
         * "master {@link Node node}".
         */
        NULL
        ,
        /** 
         * A deliminator. Used to space out arguments for functions. Currently, the only one is ",".
         */
        DELIM
        , 
        /**
         * A group. Distinguished from a function when parsing only because it no letters preceeds "(". 
         * <p> If a {@link Node} is being {@link Factors#eval(Node) evaluated}, and it's {@link Node#TOKEN token}
         * is of this type, the {@link Factors} will just evaluate it's contents normally, as one would expect.
         * @see Node
         * @see Factors
         */
        GROUP
    }
    /**
     * Default constructor. Just passes null, null to the main constructor.
     */
    public Token() {
        this(null, null);
    }
    /**
     * The main constructor.
     * Just sets TYPE to pType, and VAL to PVL. If pType is GROUP, VAL is insteaed set to "GRP".
     * @param pVal      The String that this token is based off of.
     * @param pType     The type of token that this token is.
     */
    public Token(String pVal, Types pType) {
        if(pType == Types.GROUP) {
            VAL = "GRP";
        } else {
            VAL = pVal;
        }
        TYPE = pType;
    }

    /** 
     * An alternate constructor. This just passes pVal as a string and pType to the main constructor.
     * @param pVal    The character that this tokenis based off of.
     * @param pType     The type of token that this token is.
     */
    public Token(char pVal, Types pType) {
        this("" + pVal, pType);
    }

    /** 
     * Used to determine if a {@link Node} based on a token should be a {@link FinalNode} or a {@link Node}
     * (in this case, it's the former).
     * @return True if TYPE is a NUM or VAR.
     */
    public boolean isConst() { return TYPE == Types.NUM || TYPE == Types.VAR;}

    /** 
     * Used to determine if a //{@link Node} based on a token should be a {@link FinalNode} or a {@link Node}
     * (in this case, it's the latter). Also used to distinguish between {@link Token.Types#FUNC functions} / 
     * {@link Token.Types#GROUP groups} and {@link Token.Types#OPER operators}.
     * @return True if TYPE is a GROUP or FUNC.
     */
    public boolean isGroup() { return TYPE == Types.GROUP || TYPE == Types.FUNC;}

    /** 
     * Used to distinguish between {@link Token.Types#FUNC functions} / 
     * {@link Token.Types#GROUP groups} and {@link Token.Types#OPER operators}.
     * @return True if TYPE is a OPER.
     */    
    public boolean isOper() {return  TYPE == Types.OPER;}
    /**
     * Used to figure out if a {@link Node node} based off this is the "master node". 
     * @return True if TYPE == NULL and VAL == "E".
     */
    public boolean isUni() {return TYPE == Types.NULL && VAL == "E";}

    /** 
     * Used to figure out which operators come before others in the "order of operations". Parentheses are handeled
     * seperately.
     * if <code>isUni()</code>, priority is 4.
     * <p> else if TYPE is FUNC or GROUP, priority is 3.
     * <p> else if TYPE is OPER: <code>+,- == 0</code>, <code>*,/ == 1</code>,and  <code>^ == 2</code>.
     * <p> else priority is -1.
     * @return The priority.
     */
    public int priority() {
        if(TYPE == Types.NULL && VAL == "E")
            return 4;
        if(TYPE != Types.OPER && TYPE != Types.FUNC && TYPE != Types.GROUP) {
            return -1;
        }
        switch(VAL) {
            case "+": return 0;
            case "-": return 0;
            case "*": return 1;
            case "/": return 1;
            case "^": return 2;
            default:
                return 3; //used for groups
        }
    }
    /** 
     * A String representation of a Token. 
     * @return A String representation corresponding to the VAL and the TYPE of the Token.
     */
    public String toString() {
        String ret = "('" + VAL + "': ";
        switch(TYPE) {
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