import java.util.ArrayList;
import java.util.HashMap;
/** 
 * A class that models an equation, and its behaviour.
 * @author Sam Westerman
 * @version 0.1
 */
public class Equation {
    public static void main(String[] args) throws DoesntExistException, NotDefinedException,
            TypeMisMatchException, ClassNotFoundException, NoSuchMethodException,
            java.lang.reflect.InvocationTargetException, IllegalAccessException {
        // Equation eq = new Equation("1 + b * (2 + 3) + f(x, 4 + 1, a(5)) + 6");
        // Equation eq = new Equation("1 + fac(a) - summ(10) * c/3");
            // Equation eq = new Equation("A + B * C + D ^ E + F");
            Equation eq = new Equation("B * C + D ^ E ");
        eq.factors.setVars(new HashMap<String, Double>()
            {{
                put("A",1.0D);
                put("B",2.0D);
                put("C",3.0D);
                put("D",4.0D);
                put("E",5.0D);
                put("F",6.0D);
            }});
        eq.factors.setFuncs(new HashMap<String, Function>()
            {{
                put("summ",new Function("summation"));
            }});
        System.out.println(eq.RAW_EQ);
        System.out.println(eq.node);
        System.out.println("ANSWER: "+eq.eval());
    }

    /** The raw equation, totally untouched. Gets set right when Equation is initialized. */
    public final String RAW_EQ;

    /** 
     * An ArrayList of tokens that comprise <code>RAW_EQ</code>.
     */
    public ArrayList<Token> tokens;

    public Node node;

    public Factors factors;
    /** 
     * Default constructor. Just passes null to the main constructor.
     */
    public Equation(){
        this(null);
    }
    /** 
     * Main constructor. Takes the parameter <code>pEq</code>, and parses the tokens from it, and generates a "node"
     * model for it.
     * @param pEq       The raw Equation to be parsed.
     */ 
    public Equation(String pEq){
        //TODO: PEMDAS
        //TODO: a * -b won't crash.
        RAW_EQ = pEq;
        tokens = parseTokens(RAW_EQ);
        node = generateNodes(tokens);
        factors = new Factors();
    }

    public static double eval(Factors pFactor, Node pNode)
        throws DoesntExistException, NotDefinedException,
            TypeMisMatchException, ClassNotFoundException, NoSuchMethodException,
            java.lang.reflect.InvocationTargetException, IllegalAccessException {
        return pFactor.eval(pNode);
    }
    public double eval()
        throws DoesntExistException, NotDefinedException,
            TypeMisMatchException, ClassNotFoundException, NoSuchMethodException,
            java.lang.reflect.InvocationTargetException, IllegalAccessException {
        return eval(factors, node);
    }
    public static double eval(Factors pFactor, Node pNode, HashMap<String, Double> pVals)
        throws DoesntExistException, NotDefinedException,
            TypeMisMatchException, ClassNotFoundException, NoSuchMethodException,
            java.lang.reflect.InvocationTargetException, IllegalAccessException {
        return pFactor.eval(pNode, pVals);
    }    
    public double eval(HashMap<String, Double> pVals)
            throws DoesntExistException, NotDefinedException,
            TypeMisMatchException, ClassNotFoundException, NoSuchMethodException,
            java.lang.reflect.InvocationTargetException, IllegalAccessException {    
        return eval(factors, node, pVals);
    }

    private Object[] condeseNodes(int pos, Node n, ArrayList<Token> pTokens){
        while(pos < pTokens.size()){
            Token t = pTokens.get(pos);
            if(t.isConst())
                n.add(new FinalNode(t));
            if(t.isOper())
                n.add(new Node(t));
            if(t.isGroup()){
                int paren = 0;
                int x = pos + 1;
                do{
                    if(pTokens.get(x).TYPE == Token.Types.LPAR) paren++;
                    if(pTokens.get(x).TYPE == Token.Types.RPAR) paren--;
                    x++;
                } while(paren > 0 && x < pTokens.size());

                ArrayList<Token> passTokens = new ArrayList<Token>();
                for(Token tk : pTokens.subList(pos + 1, x ))
                     passTokens.add(tk);

                Object[] temp = condeseNodes(0, new Node(t), passTokens);
                pos += (int)temp[0];
                n.add((Node)temp[1]);
            }
            pos++;
        }
        return new Object[]{pos,n};
    }
    private Node completeNodes(Node node){
        Node ret = new Node(new Token(node.TOKEN.VAL, node.TOKEN.TYPE));
        int i = 0;
        if(node instanceof FinalNode)
            return node;
        while(i < node.size()){
            Node n = node.get(i);
            if(n instanceof FinalNode)
                ret.add(n);
            else if(n.TOKEN.TYPE == Token.Types.OPER){
                for(int depth = 0; depth < ret.depth(); depth++){
                    Node nD = ret.getD(depth);
                    if(!(nD instanceof FinalNode)){
                        if(n.TOKEN.priority() > nD.TOKEN.priority()){
                            System.out.println("A|" + n + "(" + n.TOKEN.priority() + ") | " + nD 
                                + "(" + nD.TOKEN.priority() + ")");
                            n.add(nD.get(nD.size() - 1)); // Add last value of depth node to new node
                            nD.rem(nD.size() -1); // Remove the last value from the depth node
                            n.add(completeNodes(node.get(i + 1))); // Add the next node to new node
                            i++; // Have to increase i because we parsed the next node
                            nD.add(n); // Add the new node to the end of the old node
                            ret.remD(depth); // Remove the  old depth node in return node
                            ret.addD(depth, nD); // Add the new depth node in return node.
                            break;
                        }
                        // else if(n.TOKEN.priority() <= nD.TOKEN.priority() &&! nD.TOKEN.isUni()){
                        //     System.out.println("B|" + n + "(" + n.TOKEN.priority() + ") | " + nD 
                        //         + "(" + nD.TOKEN.priority() + ")");
                        //     n.add(nD); // Add last value of depth node to new node
                        //     n.add(completeNodes(node.get(i + 1))); // Add the next node to new node
                        //     ret.remD(depth); // Remove the old depth node in return node
                        //     ret.addD(depth, nD); // Add the new depth node in return node.
                        //     i++; // Have to increase i because we parsed the next node
                        //     break;
                        // }
                        else if(nD.TOKEN.isUni() &&! n.TOKEN.isUni() && ret.depth() == 1){
                            System.out.println("C|" + n + "(" + n.TOKEN.priority() + ") | " + nD
                                + "(" + nD.TOKEN.priority() + ")");
                            n.add(ret.get(ret.size() -1)); // Add last value of depth node to new node
                            n.add(completeNodes(node.get(i + 1))); // Add the next node to new node
                            ret.add(n);
                            i++; // Have to increase i because we parsed the next node
                            break;
                        }

                    }
                    else if(nD instanceof FinalNode){
                        System.out.println("D|" + n + "(" + n.TOKEN.priority() + ") | " + nD 
                                + "(" + nD.TOKEN.priority() + ")");                        
                        n.add(nD);
                        n.add(completeNodes(node.get(i + 1))); // Add the next node to new node
                        i++; // Have to increase i because we parsed the next node
                        ret.remD(depth - 1);
                        ret.addD(depth - 1, n);
                        break;
                    }
                    else if(depth == ret.depth()){
                        nD.add(n);
                        ret.remD(depth);
                        ret.addD(depth, nD);
                        break;
                    }

                }
                i++;
            }
            else if(n.TOKEN.TYPE == Token.Types.FUNC || n.TOKEN.TYPE == Token.Types.GROUP){
                n = completeNodes(n);
                ret.add(n);
                i++;
            } 
            i++;
        }
        return ret;
    }

    // private Node completeNodes(Node node){
    //     Node ret = new Node(new Token(node.NAME, node.TYPE));
    //     int i = 0;
    //     if(node instanceof FinalNode)
    //         return node;
    //     while(i < node.size()){
    //         System.err.println(ret);
    //         Node n = node.get(i);
    //         if(n instanceof FinalNode)
    //             ret.add(n);
    //         else if(n.TYPE == Token.Types.OPER){
    //             for(int p = i; p < node.size(); p ++){
    //                     //TODO: IMPORTANCE
    //             }
    //             n.add(ret.get(ret.size() - 1));
    //             n.add(completeNodes(node.get(i + 1)));
    //             ret.rem(ret.size() - 1);
    //             ret.add(n);
    //             i++;
    //         }
    //         else if(n.TYPE == Token.Types.FUNC || n.TYPE == Token.Types.GROUP){
    //             n = completeNodes(n);
    //             ret.add(n);
    //             i++;
    //         } 
    //         i++;
    //     }
    //     return ret;
    // }
    private Node generateNodes(ArrayList<Token> pTokens){
        return completeNodes((Node)condeseNodes(0, new Node(new Token("E", Token.Types.NULL)), pTokens)[1]);
    }
    
    /** 
     * Generates an ArrayList of tokens that make up the inputted equation. 
     * Note that this removes all whitespace (including spaces) before handling the equation.
     * @param rEq    The equation to be parsed.
     * @return An ArrayList of tokens, each representing a different chunk of the equation. 
     * @see Token.Tokens
     */
    private ArrayList<Token> parseTokens(String rEq) throws TypeMisMatchException{
        rEq = rEq.trim().replaceAll(" ","");
        ArrayList<Token> tokens = new ArrayList<Token>();
        String prev = "";
        char c;
        for(int x = 0; x < rEq.length(); x++) {
            c = rEq.charAt(x);
            if(isAlphaNumP(c)){
                prev += c;
                if(x == rEq.length() - 1)
                    tokens.add(new Token(prev, isNumP(prev) ? Token.Types.NUM : Token.Types.VAR));                    
                continue;
            }
            switch(c){
                case '(': // This should never be preceeded by a number.
                    if(!isAlpha(prev)){
                        throw new TypeMisMatchException("'" + prev + "'isn't alphabetical, but a group / function was" +
                            "attempted to be made because it is succeeded by a '('");
                    } if(prev.length() != 0){
                        tokens.add(new Token(prev, Token.Types.FUNC));
                    } else{
                        tokens.add(new Token(prev, Token.Types.GROUP));
                    }
                    tokens.add(Token.LPAR);
                    break;
                case ')': 
                    if(prev.length() != 0){
                        tokens.add(new Token(prev, isNumP(prev) ? Token.Types.NUM : Token.Types.VAR));
                    }
                    tokens.add(Token.RPAR);
                    break;
                case '-': case '+': case '*': case '/': case '^':
                    if(prev.length() != 0){
                        tokens.add(new Token(prev, isNumP(prev) ? Token.Types.NUM : Token.Types.VAR));
                    }
                    tokens.add(new Token(c, Token.Types.OPER));
                    break;
                case ',':
                    if(prev.length() != 0){
                        tokens.add(new Token(prev, isNumP(prev) ? Token.Types.NUM : Token.Types.VAR));
                    }
                    tokens.add(new Token(c, Token.Types.DELIM));
                    break;

                default:
                    if(prev.length() != 0){
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
     * Checks if a character is alphanumeric or a period.
     * @param c     The character to test.
     * @return      True if the character is alphanumeric or a period. False otherwise.
     */
    public static boolean isAlphaNumP(char c){
        return Character.isAlphabetic(c) || Character.isDigit(c) || c == '.';
    }

    /** 
     * Checks if a string consists only of letters, digits, and / or periods.
     * @param str   The string to test.
     * @return      True if the string consists only of letters, digits, and / or periods. False otherwise.
     */
    public static boolean isAlphaNumP(String str){
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
    public static boolean isAlpha(char c){
        return Character.isAlphabetic(c);
    }

    /** 
     * Checks if a string consists only of letters. 
     * @param str   The string to test.
     * @return      True if the String is only letters. False otherwise.
     */
    public static boolean isAlpha(String str){
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
    public static boolean isNumP(char c){
        return Character.isDigit(c) || c == '.';
    }

    /** 
     * Checks if a string consists only of digits and / or periods. 
     * Please note that this doesn't check to make sure there _are_ digits. So "...." will return true.
     * @param str   The string to test.
     * @return      True if the String is only digits and / or periods. False otherwise.
     */        
    public static boolean isNumP(String str){
        for(char c : str.toCharArray())
            if(!isNumP(c))
                return false;
        return true;
    } 
}

