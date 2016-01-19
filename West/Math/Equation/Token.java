package West.Math.Equation;

import West.Math.MathObject;
import West.Math.Set.Collection;
import West.Math.Equation.Function.Function;
import West.Math.Equation.Equation;

/**
 * A single item from an equation String.
 * Example: "sin(x+2)" yields the tokens <code>{"sin":FUNC, "(":LPAR, "x":VAR, "+":OPER, "2":NUM, ")":RPAR}</code>.
 * @author Sam Westerman
 * @version 1.0
 * @since 0.1
 */
public class Token implements MathObject {
    /** The String that this class is based upon. */
    protected String val;

    /** The type of token. Used to distinguish between things such as functions, groups, and operations. */
    protected Type type;

    public static enum Type { 
        VAR, FUNC, PAREN, DELIM, BINOPER, UNL, UNR
    }

    public static final Collection<String> PAREN_L = new Collection<String>()
    {{
        add("(");
        add("[");
        add("{");
    }};

    public static String isParenL(String s){
        return Equation.isInLast(s, PAREN_L);
    }

    public static final Collection<String> PAREN_R = new Collection<String>()
    {{
        add(")");
        add("]");
        add("}");
    }};

    public static String isParenR(String s){
        return Equation.isInLast(s, PAREN_R);
    }

    public static final Collection<String> DELIM = new Collection<String>()
    {{
        add(",");
        add(":");
        add("|");
    }};

    public static String isDelim(String s){
        return Equation.isInLast(s, DELIM);
    }

    public Token() {
        this("", Type.FUNC);
    }

    public Token(String pVal, Type pType) {
        assert pVal != null : "Cannot instatiate a Token with a null pVal!";
        assert pType != null :"Cannot instatiate a Token with a null pType!";
        val = pVal;
        type = pType;
    }

    public Type type() {
        return type;
    }

    public String val() {
        return val;
    }
    
    public boolean isConst() {
        return type == Type.VAR;
    }

    public boolean isBinOper() {
        return type == Type.BINOPER;
    }
    public boolean isDelim() {
        return type == Type.DELIM;
    }
    
    public boolean isUNL() {
        return type == Type.UNL;
    }
    
    public boolean isUNR() {
        return type == Type.UNR;
    }

    public boolean isFunc() {
        return type == Type.FUNC || Function.get(val) != null;
    }
    
    public boolean isGroup() {
        return type == Type.FUNC && val.equals("");
    }

    public boolean isParen() {
        return type == Type.PAREN;
    }

    @Override
    public String toString() {
        return "["+val+":"+type.toString().substring(0,3)+"]";
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