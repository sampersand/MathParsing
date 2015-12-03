package Math.Equation;
import Math.Exception.NotDefinedException;
import java.util.HashMap;
/**
 * A single item from an equation String.
 * Example: "sin(x+2)" yields the tokens <code>{"sin":FUNC, "(":LPAR, "x":VAR, "+":OPER, "2":NUM, ")":RPAR}</code>.
 * @author Sam Westerman
 * @version 0.1
 */
public class Token {
    /** The String that this class is based upon. */
    private String val;

    /** The type of token. Used to distinguish between things such as functions, groups, and operators. */
    private Type type;

    /**
     * The different Type of tokens. They are used to determine what to do with the tokens (and, eventually, the
     * {@link Node}s they go into.
     */
    public static enum Type {  //im doing the "," on the next line so as to make Sublime Text 3 not hate me.
        /**
         * A left parenthesis. It's val is only ever equal to "(". 
         * Used when creating {@link Node nodes} to determine what goes inside a {@link #FUNC function} / 
         * {@link #GROUP group}.         
         */
        LPAR
        ,
        /**
         * A right parenthesis. It's val is only ever equal to ")".
         * Used when creating {@link Node nodes} to determine what goes inside a {@link #FUNC function} / 
         * {@link #GROUP group}.
         */ 
        RPAR
        ,
        /**
         * A variable. Distinguished from a function when parsing only because it isn't immediately before "(". 
         * <p> If a {@link Node} is being {@link Factors#eval(Node) evaluated}, and it's {@link Node#token token}
         * is of this type, the {@link Factors} evaluating it will check it's {@link Factors#vars} for a variable with
         * the same name as {@link #val}.
         * <p> Note: val should only contain letters, as anything that isn't alphanumerical, or <code>()+*-/^</code> 
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
         * A function. Distinguished from a variable when parsing only because it immediately preceeds "(". 
         * <p> If a {@link Node} is being {@link Factors#eval(Node) evaluated}, and it's {@link Node#token token}
         * is of this type, the {@link Factors} evaluating it will check it's {@link Factors#funcs} for a function with
         * the same name as {@link #val}.
         * <p> Note: val should only contain letters, as anything that isn't alphanumerical, or <code>()+*-/^</code> 
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
         * <p> If a {@link Node} is being {@link Factors#eval(Node) evaluated}, and it's {@link Node#token token}
         * is of this type, the {@link Factors} will just evaluate it's contents normally, as one would expect.
         * @see Node
         * @see Factors
         */
        GROUP
        ,
        /**
         * A raw String. Used only to keep track of what is between a pair of single quotes (').
         */
        ARGS
        ,
        /** This type given to the master node. */
        UNI
    }

    public static final Token UNI = new Token("E", Type.UNI);
    /**
     * Default constructor. Just passes null, null to the main constructor.
     */
    public Token() {
        this("", Type.NULL);
    }
    /**
     * The main constructor.
     * Just sets type to ptype, and val to PVL. If ptype is GROUP, val is insteaed set to "GRP".
     * @param pVal      The String that this token is based off of.
     * @param ptype     The type of token that this token is.
     */
    public Token(String pVal, Type ptype) {
        if(ptype == Type.GROUP) {
            val = "GRP";
        } else {
            val = pVal;
        }
        type = ptype;
    }

    /**
     * An alternate constructor. This just passes pVal as a String and ptype to the main constructor.
     * @param pVal    The character that this tokenis based off of.
     * @param ptype     The type of token that this token is.
     */
    public Token(char pVal, Type ptype) {
        this("" + pVal, ptype);
    }

    public Type type(){return type;}
    public String val(){return val;}
    /**
     * Used to determine if a {@link Node} based on a token should be a {@link FinalNode} or a {@link Node}
     * (in this case, it's the former).
     * @return True if type is a NUM or VAR or ARGS.
     */
    public boolean isConst() {
        return type == Type.NUM || type == Type.VAR || type == Type.ARGS;
    }

    /**
     * Used to determine if a //{@link Node} based on a token should be a {@link FinalNode} or a {@link Node}
     * (in this case, it's the latter). Also used to distinguish between {@link Token.Type#FUNC functions} / 
     * {@link Token.Type#GROUP groups} and {@link Token.Type#OPER operators}.
     * @return True if type is a GROUP or FUNC.
     */
    public boolean isGroup() {
        return type == Type.GROUP || type == Type.FUNC;
    }

    /**
     * Used to distinguish between {@link Token.Type#FUNC functions} / 
     * {@link Token.Type#GROUP groups} and {@link Token.Type#OPER operators}.
     * @return True if type is a OPER.
     */    
    public boolean isOper() {
        return  type == Type.OPER;
    }
    /**
     * Used to figure out if a {@link Node node} based off this is the "master node". 
     * @return True if type == NULL and val == "E".
     */
    public boolean isUni() {
        return type == Type.UNI && val.equals("E");
    }

    /**
     * Used to figure out which operators come before others in the "order of operations". Parentheses are handeled
     * seperately.
     * if <code>isUni()</code>, priority is 4.
     * <p> else if type is FUNC or GROUP, priority is 3.
     * <p> else if type is OPER: <code>+,- == 0</code>, <code>*,/ == 1</code>,and  <code>^ == 2</code>.
     * <p> else priority is -1.
     * @return The priority.
     */
    public int priority() {
        if(isUni())
            return 4;
        if(!isOper()){// && ! isGroup()) {
            return -1;
        }
        switch(val) {
            case "+": return 0;
            case "-": return 0;
            case "*": return 1;
            case "/": return 1;
            case "^": return 2;
            default:
                throw new NotDefinedException("[ERROR] Operator '" + val + "' doesn't have a priority!");
        }
    }

    /**
     * A String representation of a Token. 
     * @return A String representation corresponding to the val and the type of the Token.
     */
    public String toString() {
        return "('" + val + "': " + type + ")";
    }
}