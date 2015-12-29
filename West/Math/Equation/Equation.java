package West.Math.Equation;

import West.Math.MathObject;
import West.Math.Equation.Function.CustomFunction;
import West.Math.Equation.Node;
import West.Math.Exception.NotDefinedException;
import West.Math.Set.CompareCollection;
import West.Math.Exception.TypeMisMatchException;

import java.util.HashMap;
import java.util.regex.Pattern;

import West.Math.Equation.Function.OperationFunction;
import West.Math.Set.CompareCollection;

import java.util.HashMap;
import West.Math.Set.Collection;

/**
 * A class that represents an equation in West.Math. It really is just a collection of Nodes that are equal to
 * each other.
 * 
 * @author Sam Westerman
 * @version 0.75
 * @since 0.1
 */
public class Equation implements MathObject {

    /** This classe's list of expressions that are equal to eachother. */
    protected CompareCollection<Node> expressions;

    public static final HashMap<String, Object> CCHARS = new HashMap<String, Object>()
    {{
        put("op_un_l", OperationFunction.UNARY_LEFT);
        put("op_un_r", OperationFunction.UNARY_RIGHT);
        put("op_bi", OperationFunction.BINARY);
        put("comp", CompareCollection.COMPARATOR);
        put("paren_l", Token.PAREN_L);
        put("paren_r", Token.PAREN_R);
        put("delim", Token.DELIM);
    }};


    /**
     * The default constructor. This just instantiates {@link #expressions} as an empty Collection.
     */
    public Equation() {
        expressions = new CompareCollection<Node>();
    }
    public Equation(String comp) {
        expressions = new CompareCollection<Node>().setComparator(comp);
    }

    /**
     * Adds all of the {@link Node}s as defined in <code>pCol</code>.
     * @param pCol    An Collection of {@link Node}s that will be added to {@link #expressions}.
     * @return This class, with <code>pCol</code> added.
     */
    public Equation add(CompareCollection<Node> pCol) {
        expressions.add(pCol);
        return this;
    }

    public Equation add(String pStr){
        System.out.println(pStr);
        Collection<Token> tokens = parseTokens(pStr);
        System.out.println(tokens);
        Collection<Collection<Token>> units= new Collection<Collection<Token>>(){{
            Collection<Token> prev = new Collection<Token>();
            for(Token t : tokens){
                prev.add(t);
                if(isComp(t.val())){
                    add(prev);
                    prev = new Collection<Token>();
                }
            }
            if(prev.size() != 0)
                add(prev);
        }};
        expressions = new CompareCollection().setComparator(units.get(0).get(units.get(0).size() - 1).val());
        for(int i = 0; i < units.size(); i++){
            Collection<Token> expr = units.get(i);
            if(isComp(expr.get(expr.size() - 1).val()))
                expr.pop(expr.size() - 1).val();
            expressions.add(Node.generateMasterNode(expr));

        }
        System.out.println(this.toFullString());
        return this;
    }

    public Equation setComparator(String comp) {
        expressions.setComparator(comp);
        return this;
    }
    /**
     * Returns the {@link #expressions} that this class defines.
     * @return {@link #expressions}
     */
    public CompareCollection<Node> expressions() {
        return expressions;
    }

    /**
     * Gets a string representing the {@link Node#expression} for each {@link Node} in {@link #expressions}.
     * @return A string comprised of each expression, with <code> = </code> between each one.
     */
    public String formattedNodes(){
        String ret = "";
        for(Node expr : expressions)
            ret += expr.genEqString() + " " + expressions.comparator()+ " ";
        return ret.substring(0, ret.length() - (expressions.size() > 0 ? 3 : 0));
    }

    /**
     * Fixes any terms that might be misleading to the compiler. For example, <code>sinx</code> will become
     * <code>sin(x)</code>. Note: To not have it do any fixing, put a "@" at the beginning of the input String
     * @param pEq            The expression to be corrected.
     * @return A corrected version of the expression.
     */
    public static String fixNode(String pEq) {
        //TODO: FIX
        if(pEq.charAt(0) == '@')
            return pEq.substring(1);
        String[] trigf = new String[]{"sec", "csc", "cot", "sinh", "cosh", "tanh", "sin", "cos", "tan"};
        for(String trig : trigf) {
            pEq = pEq.replaceAll("()" + trig + "(?!h)([A-za-z]+)","$1" + trig + "($2)");
        }

        //# = number    A = letter   & = BOTH
        pEq = pEq.replaceAll("\\-\\(", "-1*("); // -( → -1 * (
        pEq = pEq.replaceAll("([\\d\\w.]+)E([\\d\\w.-]+)","($1*10^(0$2))"); // &.&E-?&.& → (&.&*10^(-?&.&))
        pEq = pEq.replaceAll("([\\d.]+)(?!E)(\\(|(?:[A-Za-z]+))", "$1*$2"); // #A → #*A
        return pEq;
    }

    /**
     * Generates an Collection of tokens that represent rEq.
     * Note that this removes all whitespace (including spaces) before handling the expression.
     * @param rEq    The expression to be parsed.
     * @return An Collection of tokens, each representing a different chunk of the expression. 
     * @see Token
     */
    public static Collection<Token> parseTokens(String rEq) throws TypeMisMatchException{
        rEq = fixNode(rEq.trim().replaceAll(" ","")); //remove all spaces
        Collection<Token> tokens = new Collection<Token>();
        String prev = ""; //used for generating things
        String s;
        for(int x = 0; x < rEq.length(); x++) {
            s = "" + rEq.charAt(x); // the character that will be used
            if(!isControlChar(s, prev)) {
                prev += s;
                if(x == rEq.length() - 1){
                    tokens.add(new Token(prev, Token.Type.VAR));
                    prev = "";
                }
                continue;
            }
            if(isParen(s)){
                if(((Collection)CCHARS.get("paren_l")).contains(s)) //if its a left paren, make function
                    tokens.add(new Token(prev, Token.Type.FUNC));
                else if(prev.length() != 0)
                    tokens.add(new Token(prev, Token.Type.VAR));
                tokens.add(new Token(s, Token.Type.PAREN));
            } else if(isOper(s, prev)){
                if(prev.length() != 0)
                    tokens.add(new Token(prev, Token.Type.VAR));
                tokens.add(new Token(s, Token.Type.OPER));
            } else if(isDelim(s)){
                if(prev.length() != 0)
                    tokens.add(new Token(prev, Token.Type.VAR));
                tokens.add(new Token(s, Token.Type.DELIM));
            } else if(isComp(s)){
                if(prev.length() != 0)
                    tokens.add(new Token(prev, Token.Type.VAR));
                tokens.add(new Token(s, Token.Type.COMP));
            } else
                assert false;
            // } else if(OperationFunction.OPERATOR.fromString(c) != null) {
            //     if(prev.length() != 0)
            //         tokens.add(new Token(prev, Token.Type.VAR));
            //     tokens.add(new Token(c, Token.Type.OPER));
            // } else if(c == ','){
            //     if(prev.length() != 0)
            //         tokens.add(new Token(prev, Token.Type.VAR));
            //     tokens.add(new Token(c, Token.Type.DELIM));
            // } else {
            //     throw new NotDefinedException("No idea what to do with character '" + c + "'");
            //     // if(prev.length() != 0)
            //     //     tokens.add(new Token(prev, Token.Type.VAR));
            //     // tokens.add(new Token(c, Token.Type.NULL));
            // }
            prev = "";
        }
        return tokens;
    }
    
    public static boolean isOper(String s, String prev){
        if((prev.length() == 0 || prev.charAt(prev.length() - 1) == 'E') &&
                            ((HashMap)CCHARS.get("op_un_l")).containsKey(s))
            return true;
        if(prev.length() != 0 && ((HashMap)CCHARS.get("op_un_r")).containsKey(s))
            return true;
        if(((HashMap)CCHARS.get("op_bi")).containsKey(s))
            return true;
        return false;
    }
    public static boolean isControlChar(String s, String prev){
        return isOper(s, prev) || isParen(s) || isDelim(s) || isComp(s);
    }
    public static boolean isComp(String s){
        return ((Collection<String>)CCHARS.get("comp")).contains(s);
    }
    public static boolean isDelim(String s){
        return ((Collection)CCHARS.get("delim")).contains(s);
    }
    public static boolean isParen(String s){
        if(((Collection)CCHARS.get("paren_l")).contains(s))
            return true;
        if(((Collection)CCHARS.get("paren_r")).contains(s))
            return true;
        return false;
    }



    public String exprtoStr(){
        String ret = "";
        for(Node n : expressions){
            ret += " " +expressions.comparator() + " " +n.genEqString();
        }
        return expressions.size() >0 ? ret.substring(3) : "empty equation";
    }

    @Override
    public String toString() {
        return  "eq:" + exprtoStr();
    }

    @Override
    public String toFancyString(int idtLvl) {
        String ret = indent(idtLvl) + "Equation:\n";
        ret += indentE(idtLvl + 1) + "Expressions:" + exprtoStr();
        return ret;
    }

    @Override
    public String toFullString(int idtLvl) {
        String ret = indent(idtLvl) + "Equation:\n";
        ret += indent(idtLvl + 1) + "Comparator:\n"; 
        ret += indent(idtLvl + 2) + expressions.comparator() + "\n";
        ret += indent(idtLvl + 1) + "Raw Equation:\n" + indentE(idtLvl + 2) + formattedNodes() + "\n";
        ret += indent(idtLvl + 1) + "Nodes:";
        for(Node expr : expressions)
            ret += "\n" + expr.toFullString(idtLvl + 2);
        return ret + "\n" + indentE(idtLvl + 2) + "\n" + indentE(idtLvl + 1);
    }

    @Override
    public Equation copy(){
        return new Equation(expressions.comparator()).add(expressions);
    }

    @Override
    public boolean equals(Object pObj){
        if(pObj == null || !(pObj instanceof Equation))
            return false;
        if(this == pObj)
            return true;
        Equation peq = (Equation)pObj;
        for(int i = 0; i < expressions.size(); i++)
            if(!expressions.get(i).equals(peq.expressions().get(i)))
                return false;
        return true;

    }
}