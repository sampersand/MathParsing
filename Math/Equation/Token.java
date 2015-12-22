package Math.Equation;

import Math.MathObject;
import static Math.Declare.*;
import Math.Exception.NotDefinedException;

import java.util.HashMap;
/**
 * A single item from an equation String.
 * Example: "sin(x+2)" yields the tokens <code>{"sin":FUNC, "(":LPAR, "x":VAR, "+":OPER, "2":NUM, ")":RPAR}</code>.
 * @author Sam Westerman
 * @version 0.67
 * @since 0.1
 */
public class Token implements MathObject {
    /** The String that this class is based upon. */
    protected String val;

    /** The type of token. Used to distinguish between things such as functions, groups, and operations. */
    protected Type type;

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
         * <p> Note: Val should only contain letters, as anything that isn't alphanumerical, or <code>()+*-/^</code> 
         * won't be parsed.
         * @see Node
         * @see EquationSystem
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
         * <p> Note: val should only contain letters, as anything that isn't alphanumerical, or <code>()+*-/^</code> 
         * won't be parsed.
         * @see Node
         * @see EquationSystem
         */
        FUNC
        , 
        /**
         * An operation. Currently, the only operations are <code>+, -, *, /, and ^</code>. Things like "!" for factorial 
         * need to change to {@link #FUNC functions} (but "!" isn't a valid function name; Names can only have letters).
         */
        OPER
        ,
        /**
         * A null value. Duh? Only ever used when a token doesn't fit into one of the previous catagories.
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
         * @see Node
         * @see EquationSystem
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
     * The main constructor for Token. Just sets type to pType, and val to pVal. If pType is GROUP, val is insteaed set
     * to "GRP".
     * @param pVal      The String that this token is based off of. Cannot be null.
     * @param pType     The type of token that this token is. Cannot be null, but can be {@link #Type.NULL}.
     * @throws IllegalArgumentException Thrown when either pVal or pType is null.
     */
    public Token(String pVal,
                 Type pType) {
        declP(pVal != null, "Cannot instatiate a Token with a null pVal!");
        declP(pType != null, "Cannot instatiate a Token with a null pType!");
        if(pType == Type.GROUP) {
            val = "GRP";
        } else {
            val = pVal;
        }
        type = pType;
    }

    /**
     * An alternate constructor. This just passes pVal as a String and pType to the main constructor.
     * @param pVal    The character that this tokenis based off of.
     * @param pType     The type of token that this token is.
     * @deprecated As of 0.67 because it can be replaced by <code>new Token("" + char, type)</code>.
     */
    public Token(char pVal,
                 Type pType) {
        this("" + pVal, pType);
    }

    /**
     * Returns this class's {@link #type}.
     * @return this class's {@link #type}.
     */
    public Type type() {
        return type;
    }

    /**
     * Returns this class's {@link #val}.
     * @return this class's {@link #val}
     */
    public String val() {
        return val;
    }
    
    /**
     * Used to determine if a {@link Node} based on a token should be a {@link FinalNode} or a {@link Node}
     * (in this case, it's the former).
     * @return True if type is a NUM or VAR or ARGS.
     */
    public boolean isConst() {
        assert type != null : "type cannot be null, as it is immutable and the constructor doesn't allow null types!";
        return type == Type.NUM || type == Type.VAR || type == Type.ARGS;
    }

    /**
     * Used to determine if a //{@link Node} based on a token should be a {@link FinalNode} or a {@link Node}
     * (in this case, it's the latter). Also used to distinguish between {@link Token.Type#FUNC functions} / 
     * {@link Token.Type#GROUP groups} and {@link Token.Type#OPER operations}.
     * @return True if type is a GROUP or FUNC.
     */
    public boolean isGroup() {
        assert type != null : "type cannot be null, as it is immutable and the constructor doesn't allow null types!";
        return type == Type.GROUP || type == Type.FUNC;
    }

    /**
     * Used to distinguish between {@link Token.Type#FUNC functions} / 
     * {@link Token.Type#GROUP groups} and {@link Token.Type#OPER operations}.
     * @return True if type is a OPER.
     */    
    public boolean isOper() {
        assert type != null : "type cannot be null, as it is immutable and the constructor doesn't allow null types!";
        return type == Type.OPER;
    }
    /**
     * Used to figure out if a {@link Node node} based off this is the "master node". 
     * @return True if type == NULL and val == "E".
     */
    public boolean isUni() {
        assert type != null : "type cannot be null, as it is immutable and the constructor doesn't allow null types!";
        assert val != null : "val cannot be null, as it is immutable and the constructor doesn't allow null types!";
        return type == Type.UNI && val.equals("E");
    }

    /**
     * Used to figure out which operations come before others in the "order of operations". Parentheses are handeled
     * seperately.
     * <p> if type is OPER: <code>+ &amp; - ==> 0</code>, <code>* &amp; / ==> 1</code>, and  <code>^ ==> 2</code>.
     * <p> else priority is - 1.
     * @return The priority.
     */
    public int priority() {
        assert val != null : "val cannot be null! It was declared in the constructor, and has no way to be changed!";
        int ret = 0;
        switch(val) {
            case "^":
                ret ++;
            case "*": case "/":
                ret ++;
            case "+": case "-":
                assert isOper();
                return ret;
            default : 
                return -1;
        }
    }

    @Override
    public String toString() {
        return "Token: val = [" + val + "], type = [" + type + "]";
    }

    @Override
    public String toFancyString(int idtLvl) {
        String ret = indent(idtLvl) + "Token:\n";
        ret += indent(idtLvl + 1) + "Value = " + val + "\n";
        ret += indent(idtLvl + 1) + "Type = " + type;
        return ret;
    }

    @Override
    public String toFullString(int idtLvl) {
        String ret = indent(idtLvl) + "Token\n";
        ret += indent(idtLvl + 1) + "Value\n" + indentE(idtLvl + 2) + val + "\n";
        ret += indent(idtLvl + 1) + "Type\n" + indentE(idtLvl + 2) + type + "\n" + indentE(idtLvl + 1) + "\n";
        ret += indentE(idtLvl);
        return ret;
    }

    @Override
    public Token copy(){
        return new Token(val, type);
    }

    @Override
    public boolean equals(Object pObj){
        if(pObj == null || !(pObj instanceof Token))
            return false;
        if(pObj == this)
            return true;
        return val.equals(((Token)pObj).val()) && type == ((Token)pObj).type();
    }
}