package West.Math.Equation;

import West.Math.MathObject;
import West.Math.Print;
import West.Math.Exception.TypeMisMatchException;
import West.Math.Exception.NotDefinedException;
import West.Math.Equation.Function.OperationFunction;
import West.Math.Set.CompareCollection;

import java.util.ArrayList;
import java.util.HashMap;
import West.Math.Set.Collection;

/**
 * A class that models an expression, and its behaviour.
 * @author Sam Westerman
 * @version 0.2
 */

public class Expression implements MathObject {

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

    /** The raw expression. */
    protected String expression;

    /**
     * The Node representing the whole expression. 
     * @see   Node#generateMasterNode(ArrayList)
     */
    protected Node node;

    /**
     * The default constructor for the Expression class. Just passes an empty String, and Node to the 
     * {@link #Expression(String,Node) main expression constructor}.
     */
    public Expression() {
        this("", new Node());
    }



    /**
     * The Object - only constructor for the expression class. Just passes <code>pObj</code>, and a Node based off
     * <code>pObj</code>, {@link #Expression(String,Node) main expression constructor}.
     * @param pObj       An Object that this class will be modeled after. 
     */ 
    public Expression(Object pObj) {
        this("" + pObj, Node.generateMasterNode(Expression.parseTokens("" + pObj)));
    }

    /**
     * The Node-only for the expression class. Just passes a String created based on Node's subnodes to the
     * {@link #Expression(String,Node) main expression constructor}.
     * @param pNode        The Node that the expression class will be modeled after.
     */ 
    public Expression(Node pNode) {
        this(pNode.genEqString(), pNode);
    }


    /**
     * The main constructor for the expression class. Just sets {@link #expression} and {@link #node} to their
     * respective constructors
     * @param pEq       The String representation of the expression. Is only ever used to identify individual expressions.
     * @param pNode        The Node that the entire expression is based off of.
     */ 
    public Expression(String pEq,
                      Node pNode) {
        expression = pEq.trim().replaceAll(" ","");
        node = pNode;
    }


    /**
     * Returns {@link #node} - the {@link Node} that represents this class's {@link #expression}.
     * @return {@link #node} - the {@link Node} that represents this class's {@link #expression}.
     */
    public Node node(){
        return node;
    }

    /**
     * Returns {@link #expression} - the String that this class is modeled after.
     * @return {@link #expression} - the String that this class is modeled after.
     */
    public String expression(){
        return expression;
    }

    /**
     * Generates {@link #node} using {@link #expression}.
     */
    public void genNode() {
        genNode(expression);
    }

    /**
     * Generates {@link #node} using pEq.
     * @param pEq       The expression that this class's node will be based off of.
     */
    public void genNode(String pEq) {
        node = Node.generateMasterNode(Expression.parseTokens(pEq));
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

        pEq = pEq.replaceAll("\\-\\(", "- 1*(");
        pEq = pEq.replaceAll("([\\d.])+(\\(|(?:[A-Za-z]+))", "$1*$2");
        return pEq;
    }

    /**
     * Generates an ArrayList of tokens that represent rEq.
     * Note that this removes all whitespace (including spaces) before handling the expression.
     * @param rEq    The expression to be parsed.
     * @return An ArrayList of tokens, each representing a different chunk of the expression. 
     * @see Token
     */
    public static ArrayList<Token> parseTokens(String rEq) throws TypeMisMatchException{
        rEq = fixExpression(rEq.trim().replaceAll(" ","")); //remove all spaces

        ArrayList<Token> tokens = new ArrayList<Token>();
        String prev = ""; //used for generating things
        String s;
        for(int x = 0; x < rEq.length(); x++) {
            s = "" + rEq.charAt(x); // the character that will be used
            if(!isControlChar(s, prev)) {
                prev += s;
                if(x == rEq.length() - 1)
                    tokens.add(new Token(prev, Token.Type.VAR));
                continue;
            }
            if(isParen(s)){
                // if(prev.length() == 0 || isControlChar(prev)) // if there is no preceeding function, it becomes a group
                //     tokens.add(new Token("", Token.Type.FUNC));
                // else
                tokens.add(new Token(prev, Token.Type.FUNC));
                tokens.add(new Token(s, Token.Type.VAR));
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
        return ((HashMap)CCHARS.get("comp")).containsKey(s);
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
    /**
     * Takes {@link #expession} and returns it formatted cleanly. <code>A+B</code> becomes <code>A + B</code>, etc.
     * @return A cleanly formatted {@link #expression}.
     */
    public String formattedExpression(){
        return expression.replaceAll("(\\+|\\-|\\*|/|\\^)", " $1 ").replaceAll(",", ", ");
    }

    @Override
    public String toString() {
        return "Expression '" + formattedExpression() + "'";
    }

    @Override
    public String toFancyString(int idtLvl) {
        return indent(idtLvl) + "Expression:\n" + indent(idtLvl + 1 ) +
                "Raw Expression = " + formattedExpression() + "\n" + indentE(idtLvl + 1) + "Generated Expression = " +
                node.genEqString().replaceAll("(\\+|\\-|\\*|/|\\^)", " $1 ").replaceAll(",", ", ");

    }

    @Override
    public String toFullString(int idtLvl) {
        String ret = indent(idtLvl) + "Expression:\n";
        ret += indent(idtLvl + 1) + "Raw Expression:\n" + indentE(idtLvl + 2) + expression + "\n";
        ret += indent(idtLvl + 1) + "Expression Node:\n" + node.toFullString(idtLvl + 2);
        return ret + "\n" + indentE(idtLvl + 2) + "\n" + indentE(idtLvl + 1);
    }

    @Override
    public Expression copy(){
        return new Expression(expression, node);
    }

    @Override
    public boolean equals(Object pObj){
        throw new NotDefinedException();
    }
}
