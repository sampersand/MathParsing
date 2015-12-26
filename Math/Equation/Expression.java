package Math.Equation;

import Math.MathObject;
import Math.Print;
import Math.Exception.TypeMisMatchException;
import Math.Exception.NotDefinedException;
import Math.Equation.Function.OperationFunction;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A class that models an expression, and its behaviour.
 * @author Sam Westerman
 * @version 0.2
 */

public class Expression implements MathObject {

    /** The raw expression. */
    protected String expression;

    /**
     * The Node representing the whole expression. 
     * @see   Node#generateNodes(ArrayList)
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
        this("" + pObj, Node.generateNodes(Expression.parseTokens("" + pObj)));
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
        node = Node.generateNodes(Expression.parseTokens(pEq));
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
        char c;
        for(int x = 0; x < rEq.length(); x++) {
            c = rEq.charAt(x); // the character that will be used
            if(prev.length() > 0 && prev.charAt(0) == '\'') { //if prev starts with ' (and therefor length > 0)
                prev += c;
                if(c == '\'') { // if the current token is ', then make a new ARGS token, and set prev to 0.
                    tokens.add(new Token(prev.substring(1, prev.length() - 1), Token.Type.ARGS));
                    prev = "";
                }
                continue;
            }
            if(prev.length() > 0 && prev.charAt(0) == '\'' && c == '\'') { //used to generate variables...?
                prev = prev.substring(1);
                tokens.add(new Token(prev, Token.Type.VAR));
                prev = "";
                continue;
            }
            if(!isControlChar(c, prev)) {
                prev += c;
                if(x == rEq.length() - 1)
                    tokens.add(new Token(prev, isNumber(prev) ? Token.Type.NUM : Token.Type.VAR));                    
                continue;
            }

            // if(c == '\''){
            //     prev += c;
            //     if(prev.length() == 1)
            //         continue;
            //     if(prev.charAt(0) != '\'')
            //     assert prev.charAt(0) == '\'';
            //         // if(prev.charAt(0) == '\'') { //if prev starts with ' (and therefor length > 0)
            //     tokens.add(new Token(prev.substring(1, prev.length() - 1), Token.Type.ARGS));

            //     // }
            if(c == '(') { // This should never be preceeded by a number.

                if(prev.length() == 0 || isNumber(prev)) // if there is no preceeding function, it becomes a group
                    tokens.add(new Token(prev, Token.Type.GROUP));
                else
                    tokens.add(new Token(prev, Token.Type.FUNC));
                tokens.add(new Token("(", Token.Type.LPAR));

            } else if(c == ')') {
                if(prev.length() != 0)
                    tokens.add(new Token(prev, isNumber(prev) ? Token.Type.NUM : Token.Type.VAR));
                tokens.add(new Token(")", Token.Type.RPAR));
            } else if(OperationFunction.OPERATOR.fromString("" + c) != null) {
                if(prev.length() != 0)
                    tokens.add(new Token(prev, isNumber(prev) ? Token.Type.NUM : Token.Type.VAR));
                tokens.add(new Token("" + c, Token.Type.OPER));
            } else if(c == ','){
                if(prev.length() != 0)
                    tokens.add(new Token(prev, isNumber(prev) ? Token.Type.NUM : Token.Type.VAR));
                tokens.add(new Token("" + c, Token.Type.DELIM));
            } else {
                throw new NotDefinedException("No idea what to do with character '" + c + "'");
                // if(prev.length() != 0)
                //     tokens.add(new Token(prev, isNumber(prev) ? Token.Type.NUM : Token.Type.VAR));
                // tokens.add(new Token("" + c, Token.Type.NULL));
            }
            prev = "";
        }
        return tokens;
    }

    private static boolean isNumber(String s){
        String[] spl = s.split("E"); // for sci notation
        if(spl.length > 2 || spl.length == 0){
            return false;
        }

        spl[0] = spl[0].replaceAll("-?\\d*\\.?\\d*", "");
        if(spl.length == 1)
            return spl[0].length() == 0;
        spl[1] = spl[1].replaceAll("-?\\d*\\.?\\d*", "");
        return spl[0].length() == 0 && spl[1].length() == 0;
    }

    private static boolean isControlChar(char c, String prev){
        if(OperationFunction.OPERATOR.fromString("" + c) != null)
            return prev.length() == 0 || prev.charAt(prev.length() - 1) != 'E' ;
        switch(c){
            // case '\'':
            case '(':
            case ')':
            case ',':
                return true;
            default:
                return false;
        }
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
