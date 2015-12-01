package Math.Equation;
import Math.Print;

import Math.Exception.TypeMisMatchException;
import Math.Exception.NotDefinedException;
import Math.Set.Set;
import Math.Display.Grapher;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * A class that models an expression, and its behaviour.
 * @author Sam Westerman
 * @version 0.2
 */

public class Expression {
    /**
     * Note that if this is going to be called from the commandline, the syntax is as follows:
     * java Expression (-v v1:val v2:val) (-f f1:val f2:val) "expression"
     * @param args          The arguments to pass. See description of this method for details.
     */

    /** The raw expression. */
    public String expression;

    /**
     * The Node representing the whole expression. 
     * @see   Node#generateNodes(ArrayList)
     */
    public Node node;
    /**
     * The default constructor for the Expression class. Just passes an empty String, Node, and Factors to the 
     * {@link #Expression(String,Node,Factors) main expression constructor}.
     */
    public Expression() {
        this("",new Node());
    }



    /**
     * The String-only constructor for the expression class. Just passes a Node created based off pEq, and an empty
     * {@link Factors} class to the {@link #Expression(String,Node,Factors) main expression constructor}.
     * @param pEq       A String containing the expression that this class will be modeled after.
     */ 
    public Expression(String pEq) {
        this(pEq, Node.generateNodes(Expression.parseTokens(pEq)));
    }
    public Expression(Object pObj) {
        this("" + pObj);
    }
    /**
     * The Node-only for the expression class. Just passes a String created based on Node's subnodes, and an empty
     * {@link Factors} class to the {@link #Expression(String,Node,Factors) main expression constructor}.
     * @param pN        The Node that the expression class will be modeled after.
     */ 
    public Expression(Node pN) {
        this(pN.genEqString(), pN);
    }


    /**
     * The main constructor for the expression class. Takes the parameter pEq, and parses the tokens from it,
     * and generates a {@link Node} model for it.
     * @param pEq       The String representation of the expression. Is only ever used to identify individual expressions.
     * @param pN        The Node that the entire expression is based off of.
     * @param pFactors  The {@link Factors} instance for the expression.
     */ 
    public Expression(String pEq, Node pN){
        expression = pEq;
        node = pN;
    }


    /** 
     * Generates {@link #node} using {@link #expression}.
     */
    public void genNode(){
        genNode(expression);
    }

    /** 
     * Generates {@link #node} using pEq.
     * @param pEq       The expression that this class's node will be based off of.
     */
    public void genNode(String pEq){
        node = Node.generateNodes(Expression.parseTokens(pEq));
    }
    /** 
     * Fixes any terms that might be misleading to the compiler. For example, <code>sinx</code> will become
     * <code>sin(x)</code>. Note: To not have it do any fixing, put a "@" at the beginning of the input String
     * @param eq            The expression to be corrected.
     * @return A corrected version of the expression.
     */
    public static String fixExpression(String eq){
        if(eq.charAt(0) == '@')
            return eq.substring(1);
        if(eq.indexOf("=")!=-1)
            eq = eq.split("=")[1];
        String[] trigf = new String[]{"sec", "csc", "cot", "sinh", "cosh", "tanh", "sin", "cos", "tan"};
        for(String trig : trigf){
            eq = eq.replaceAll("()" + trig + "(?!h)([A-za-z]+)","$1" + trig + "($2)");
        }

        eq = eq.replaceAll("\\-\\(", "-1*(");
        eq = eq.replaceAll("([\\d.])+(\\(|(?:[A-Za-z]+))", "$1*$2");
        return eq;
    }
    /**
     * Generates an ArrayList of tokens that represent rEq.
     * Note that this removes all whitespace (including spaces) before handling the expression.
     * @param rEq    The expression to be parsed.
     * @return An ArrayList of tokens, each representing a different chunk of the expression. 
     * @see Token
     */
    public static ArrayList<Token> parseTokens(String rEq) throws TypeMisMatchException{
        rEq = fixExpression(rEq.trim().replaceAll(" ",""));
        //not so sure this is the best way to fix my minus issue:

        ArrayList<Token> tokens = new ArrayList<Token>();
        String prev = "";
        char c;
        for(int x = 0; x < rEq.length(); x++) {
            c = rEq.charAt(x);
            if(prev.length() > 0 && prev.charAt(0) == '\''){
                prev += c;
                if(c == '\''){
                    tokens.add(new Token(prev.substring(1, prev.length() -1), Token.Types.ARGS));
                    prev = "";
                }
                continue;
            }
            if(prev.length() > 0 && prev.charAt(0) == '\'' && c == '\''){
                prev = prev.substring(1);
                tokens.add(new Token(prev, Token.Types.VAR));
                prev = "";
                continue;
            }
            if(isAlphaNumPQ(c)) {
                prev += c;
                if(x == rEq.length() - 1)
                    tokens.add(new Token(prev, isNumP(prev) ? Token.Types.NUM : Token.Types.VAR));                    
                continue;
            }
            switch(c) {
                case '(': // This should never be preceeded by a number.
                    if(!isAlpha(prev)) {
                        throw new TypeMisMatchException("'" + prev + "'isn't alphabetical, but a group / function was" +
                            " attempted to be made because it is succeeded by a '('");
                    } if(prev.length() != 0) {
                        tokens.add(new Token(prev, Token.Types.FUNC));
                    } else {
                        tokens.add(new Token(prev, Token.Types.GROUP));
                    }
                    tokens.add(new Token("(",Token.Types.LPAR));
                    break;
                case ')': 
                    if(prev.length() != 0) {
                        tokens.add(new Token(prev, isNumP(prev) ? Token.Types.NUM : Token.Types.VAR));
                    }
                    tokens.add(new Token(")",Token.Types.RPAR));
                    break;
                case '-': case '+': case '*': case '/': case '^':
                    if(prev.length() != 0) {
                        tokens.add(new Token(prev, isNumP(prev) ? Token.Types.NUM : Token.Types.VAR));
                    }
                    tokens.add(new Token(c, Token.Types.OPER));
                    break;
                case ',':
                    if(prev.length() != 0) {
                        tokens.add(new Token(prev, isNumP(prev) ? Token.Types.NUM : Token.Types.VAR));
                    }
                    tokens.add(new Token(c, Token.Types.DELIM));
                    break;

                default:
                    if(prev.length() != 0) {
                        tokens.add(new Token(prev, isNumP(prev) ? Token.Types.NUM : Token.Types.VAR));
                    }
                    tokens.add(new Token(c, Token.Types.NULL));
                    break;

            }
            prev = "";
        }
        return tokens;
    }


    /**
     * Checks if a character is alphanumeric, period, or a single quote (').
     * @param c     The character to test.
     * @return      True if the character is alphanumeric, period, or a single quote ('). False otherwise.
     */
    public static boolean isAlphaNumPQ(char c) {
        return isAlphaNumP(c) || c == '\'';
    }

    /**
     * Checks if a character is alphanumeric or a period.
     * @param c     The character to test.
     * @return      True if the character is alphanumeric or a period. False otherwise.
     */
    public static boolean isAlphaNumP(char c) {
        return Character.isAlphabetic(c) || Character.isDigit(c) || c == '.';
    }

    /**
     * Checks if a String consists only of letters, digits, and / or periods.
     * @param str   The String to test.
     * @return      True if the String consists only of letters, digits, and / or periods. False otherwise.
     */
    public static boolean isAlphaNumP(String str) {
        for(char c : str.toCharArray())
            if(!isAlphaNumP(c))
                return false;
        return true; //note also uses '.'
    }

    /**
     * Checks if a character is a letter.
     * @param c     The character to test.
     * @return      True if the character is a letter. False otherwise.
     */
    public static boolean isAlpha(char c) {
        return Character.isAlphabetic(c);
    }

    /**
     * Checks if a String consists only of letters. 
     * @param str   The String to test.
     * @return      True if the String is only letters. False otherwise.
     */
    public static boolean isAlpha(String str) {
        for(char c : str.toCharArray())
            if(!isAlpha(c))
                return false;
        return true;
    }
   /**
     * Checks if a character is a digit or period.
     * @param c     The character to test.
     * @return      True if the character is a digit or period. False otherwise.
     */
    public static boolean isNumP(char c) {
        return Character.isDigit(c) || c == '.';
    }

    /**
     * Checks if a String consists only of digits and / or periods. 
     * Please note that this doesn't check to make sure there _are_ digits. So "...." will return true.
     * @param str   The String to test.
     * @return      True if the String is only digits and / or periods. False otherwise.
     */        
    public static boolean isNumP(String str) {
        for(char c : str.toCharArray())
            if(!isNumP(c))
                return false;
        return true;
    } 

    /** 
     * Gives a String representation of this expression. Comprised of {@link #expression}, {@link #factors}, and
     * {@link #node}.
     * @return A String representation of this expression. 
     */
    public String toFullString(){
        return "--Expression--\n--RawEq--\n" + expression + "\n--Nodes--\n" + node.toStringL(1);
    }
    /** 
     * Just returns {@link #expression}.
     * @return A basic String representation of this expression.
     */
    public String toString(){
        return "Expression '" + expression + "'";
    }

}
