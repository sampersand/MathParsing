package West.Math.Equation;

import West.Math.MathObject;
import West.Math.Equation.Function.CustomFunction;
import West.Math.Equation.Expression;
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
 * A class that represents an equation in West.Math. It really is just a collection of Expressions that are equal to
 * each other.
 * 
 * @author Sam Westerman
 * @version 0.75
 * @since 0.1
 */
public class Equation implements MathObject {

    /** This classe's list of expressions that are equal to eachother. */
    protected CompareCollection<Expression> expressions;

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
        expressions = new CompareCollection<Expression>();
    }


    /**
     * Adds all of the {@link Expression}s as defined in <code>pCol</code>.
     * @param pCol    An Collection of {@link Expression}s that will be added to {@link #expressions}.
     * @return This class, with <code>pCol</code> added.
     */
    public Equation add(CompareCollection<Expression> pCol) {
        expressions.add(pCol);
        return this;
    }

    public Equation add(String pStr){
        Collection<Token> tokens = parseTokens(pStr);
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
        expressions = new CompareCollection().setComparator(units.get(0).get(0).val());
        for(int i = 0; i < units.size(); i++){
            Collection<Token> expr = units.get(i);
            // System.out.println(i == units.size()-1 ? "tr": expr.pop().val());
            if(i == units.size() -1 || expr.pop().val().equals(expressions.comparator()))
                ; //do nothing on purpose
            expressions.add(new Expression(Node.generateMasterNode(expr)));

        }
        return this;
    }
    /**
     * Returns the {@link #expressions} that this class defines.
     * @return {@link #expressions}
     */
    public CompareCollection<Expression> expressions() {
        return expressions;
    }

    /**
     * Gets a string representing the {@link Expression#expression} for each {@link Expression} in {@link #expressions}.
     * @return A string comprised of each expression, with <code> = </code> between each one.
     */
    public String formattedExpressions(){
        String ret = "";
        for(Expression expr : expressions)
            ret += expr.formattedExpression() + " = ";
        return ret.substring(0, ret.length() - (expressions.size() > 0 ? 3 : 0));
    }


    /**
     * Fixes any terms that might be misleading to the compiler. For example, <code>sinx</code> will become
     * <code>sin(x)</code>. Note: To not have it do any fixing, put a "@" at the beginning of the input String
     * @param pEq            The expression to be corrected.
     * @return A corrected version of the expression.
     */
    public static String fixExpression(String pEq) {
        //TODO: FIX
        if(pEq.charAt(0) == '@')
            return pEq.substring(1);
        String[] trigf = new String[]{"sec", "csc", "cot", "sinh", "cosh", "tanh", "sin", "cos", "tan"};
        for(String trig : trigf) {
            pEq = pEq.replaceAll("()" + trig + "(?!h)([A-za-z]+)","$1" + trig + "($2)");
        }

        pEq = pEq.replaceAll("\\-\\(", "-1*(");
        pEq = pEq.replaceAll("([\\d.])+(\\(|(?:[A-Za-z]+))", "$1*$2");
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
        rEq = fixExpression(rEq.trim().replaceAll(" ","")); //remove all spaces
        Collection<Token> tokens = new Collection<Token>();
        String prev = ""; //used for generating things
        String s;
        for(int x = 0; x < rEq.length(); x++) {
            s = "" + rEq.charAt(x); // the character that will be used
            if(!isControlChar(s, prev)) {
                System.out.println("!isControlChar("+s+", "+prev+")");
                prev += s;
                if(x == rEq.length() - 1){
                    tokens.add(new Token(prev, Token.Type.VAR));
                    prev = "";
                }
                continue;
            }
            System.out.println("prev:"+prev+",s:"+s);
            if(isParen(s)){
                if(((Collection)CCHARS.get("paren_l")).contains(s)) //if its a left paren, make function
                    tokens.add(new Token(prev, Token.Type.FUNC));
                else
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
    
    private static boolean isOper(String s, String prev){
        if(prev.length() == 0 && ((HashMap)CCHARS.get("op_un_l")).containsKey(s))
            return true;
        if(prev.length() != 0 && ((HashMap)CCHARS.get("op_un_r")).containsKey(s))
            return true;
        if(((HashMap)CCHARS.get("op_bi")).containsKey(s))
            return true;
        return false;
    }
    private static boolean isControlChar(String s, String prev){
        return isOper(s, prev) || isParen(s) || isDelim(s) || isComp(s);
    }
    private static boolean isComp(String s){
        return ((Collection)CCHARS.get("comp")).contains(s);
    }
    private static boolean isDelim(String s){
        return ((Collection)CCHARS.get("delim")).contains(s);
    }
    private static boolean isParen(String s){
        if(((Collection)CCHARS.get("paren_l")).contains(s))
            return true;
        if(((Collection)CCHARS.get("paren_r")).contains(s))
            return true;
        return false;
    }





    @Override
    public String toString() {
        return "Equation: expressions: "  + expressions;
    }

    @Override
    public String toFancyString(int idtLvl) {
        String ret = indent(idtLvl) + "Equation:\n";
        ret += indent(idtLvl + 1) + "Comparator: " + expressions.comparator() + "\n";
        ret += indent(idtLvl + 1) + "Expressions:";
        for(Expression expr : expressions) {
            //TODO: Update this "= " here to coincide with greater than or less than equations (when introduced).
            ret += "\n" + expr.toFancyString(idtLvl + 2);
        }
        return ret + "\n" + indentE(idtLvl + 2) + "\n" + indentE(idtLvl + 1);
    }

    @Override
    public String toFullString(int idtLvl) {
        String ret = indent(idtLvl) + "Equation:\n";
        ret += indent(idtLvl + 1) + "Comparator:\n"; 
        ret += indent(idtLvl + 2) + expressions.comparator() + "\n";
        ret += indent(idtLvl + 1) + "Raw Equation:\n" + indentE(idtLvl + 2) + formattedExpressions() + "\n";
        ret += indent(idtLvl + 1) + "Expressions:";
        for(Expression expr : expressions)
            ret += "\n" + expr.toFullString(idtLvl + 2);
        return ret + "\n" + indentE(idtLvl + 2) + "\n" + indentE(idtLvl + 1);
    }

    @Override
    public Equation copy(){
        return new Equation().add(expressions);
    }

    @Override
    public boolean equals(Object pObj){
        throw new NotDefinedException();
    }
}