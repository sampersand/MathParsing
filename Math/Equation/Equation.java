package Math.Equation;
import Math.Equation.Exception.TypeMisMatchException;
import Math.Equation.Exception.NotDefinedException;
import java.util.ArrayList;
import java.util.HashMap;
/** 
 * A class that models an equation, and its behaviour.
 * @author Sam Westerman
 * @version 0.2
 */

public class Equation {
// TODO: cause '1 * -2' to NOT crash.
// TODO: toString for all classes
    /** 
     * Note that if this is going to be called from the commandline, the syntax is as follows:
     * java Equation (-v v1:val v2:val) (-f f1:val f2:val) "equation"
     */
    public static void main(String[] args) throws NotDefinedException, TypeMisMatchException {
        Equation eq;
        if(args.length == 0){
            eq = new Equation("graph((1,2),(2,3)) + e ");
            eq.factors.addVars(new HashMap<String, Double>()
                {{
                    put("A",1.0D);
                    put("B",2.0D);
                    put("C",3.0D);
                    put("D",4.0D);
                    put("E",5.0D);
                    put("F",6.0D);
                    put("x",10D);
                }});
            eq.factors.addFuncs(new HashMap<String, CustomFunction>()
                {{
                    put("f", new CustomFunction("f"));
                    put("graph", new CustomFunction("graph"));
                    put("sum", new CustomFunction("summation"));
                }});
        }
        else{
            String rEq = args[args.length - 1];
            eq = new Equation(rEq);
            if(args.length > 0){
                int i = 0;
                boolean isArg = args[0].equals("-v");
                if(!args[0].equals("-f") && !isArg)
                    throw new NotDefinedException("first value has to be -f or -v");
                while(i < args.length - 2){ //args.length is string.
                    i++;
                    if(args[i].equals("-v")){isArg = true; continue;}
                    if(args[i].equals("-f")){isArg = false; continue;}
                    if(isArg){
                        try{
                            eq.factors.addVar(args[i].split(":")[0], Double.parseDouble(args[i].split(":")[1]));
                        } catch(NumberFormatException err){
                            System.err.println("Syntax: VARNAME:VARVAL (" + args[i] + ")");
                        } catch(ArrayIndexOutOfBoundsException err){
                            System.err.println("Syntax: VARNAME:VARVAL (" + args[i] + ")");
                        }                    } else {
                        try{
                            eq.factors.addFunc(args[i].split(":")[0], args[i].split(":")[1]);
                        } catch(NumberFormatException err){
                            System.err.println("Syntax: FUNCNAME:FUNCVAL (" + args[i] + ")");
                        } catch(ArrayIndexOutOfBoundsException err){
                            System.err.println("Syntax: FUNCNAME:FUNCVAL (" + args[i] + ")");
                        }
                    }       
                }
            }

        }
        System.out.println("RAW EQUATION: "+ eq.RAW_EQ);
        System.out.println("NODES:" + eq.node);
        System.out.println("RESULT: "+eq.eval());
        System.err.println();
        System.err.println(eq.factors.getFunc("f"));
    }

    /** The raw equation. */
    public final String RAW_EQ;

    /** 
     * An ArrayList of tokens that represent {@link #RAW_EQ}. Generally unused after generating {@link #node}.
     */
    public ArrayList<Token> tokens;

    /**
     * The Node representing the whole equation. It is {@link #tokens}, but put into a hierarchy.
     * @see   #generateNodes(ArrayList)
     */
    public Node node;

    /**
     * The list of factors for this equation. This includes what values varriables are, and where to 
     * find what functions do.
     */
    public Factors factors;


    /** 
     * The default constructor for the Equation class. Just passes null to the main constructor.
     */
    public Equation() {
        this(null);
    }

    /** 
     * The main constructor for the equaiton class. Takes the parameter pEq, and parses the tokens from it,
     * and generates a {@link Node} model for it.
     * @param pEq       A string containing the equation that this class will be modeled after.
     */ 
    public Equation(String pEq) {
        RAW_EQ = pEq;
        tokens = parseTokens(RAW_EQ);
        node = generateNodes(tokens);
        factors = new Factors();
    }


    /** 
     * Evaluates pNode using pFactor.
     * @param pFactor  The factors (varriable values and function definitions) that will be used to evaluate pNode.
     * @param pNode     The node that will be evaluated using pFactor.
     * @return A numerical representation of pNode, when evaluated using pFactor.     
     * @throws NotDefinedException      Thrown if either a varriable or a function wasn't defined.
     * @see  Factors#eval(Node)
     */
    public static double eval(Factors pFactor, Node pNode) throws NotDefinedException {
        return pFactor.eval(pNode);
    }

    /** 
     * Evaluates pNode using pFactor and pVars.
     * @param pFactor  The factors (varriable values and function definitions) that will be used to evaluate pNode.
     * @param pNode     The node that will be evaluated using pFactor.
     * @param pVars     Used instead of pFactor's vars if a conflict between the two arises. 
     * @return A numerical representation of pNode, when evaluated using pFactor, and pVars.
     * @throws NotDefinedException      Thrown if either a varriable or a function wans't defined.
     * @see  Factors#eval(Node, HashMap)
     */
    public static double eval(Factors pFactor, Node pNode, HashMap<String, Double> pVars) throws NotDefinedException {
        return pFactor.eval(pNode, pVars);
    }

    /** 
     * Evaluates {@link #node} using {@link #factors}.
     * @return A numerical representation of {@link #node}, when evaluated using {@link #factors}.
     * @throws NotDefinedException      Thrown if either a varriable or a function wasn't defined.
     * @see  #eval(Factors, Node)
     */
    public double eval() throws  NotDefinedException {
        return eval(factors, node);
    }

    /** 
     * Evaluates {@link #node} using {@link #factors} and pVars.
     * @param pVars     Used instead of {@link #factors}'s vars if a conflict between the two arises.      
     * @return A numerical representation of {@link #node}, when evaluated using {@link #factors} and pVars.
     * @throws NotDefinedException      Thrown if either a varriable or a function wasn't defined.     
     * @see  #eval(Factors, Node, HashMap)
     */    
    public double eval(HashMap<String, Double> pVars) throws NotDefinedException {
        return eval(factors, node, pVars);
    }

    /** 
     * Condenses functions and groups together into a single node. Creates 1-length nodes for operators and vars / nums.
     * Note that this is only ever used with {@link #generateNodes(ArrayList) generateNodes} and 
     * {@link #completeNodes(Node) completeNodes}.
     * @param pos       The position to start condensing nodes form.
     * @param n         The "parent" node that any newly generated nodes will be put into.
     * @param pTokens   The list of tokens that will be put into nodes.
     * @return The return type might seem odd, however, it always returns an updated pos as argument 1, and the new node
     *         to add as argument 2.
     */
    private Object[] condeseNodes(int pos, Node n, ArrayList<Token> pTokens) {
        while(pos < pTokens.size()) {
            Token t = pTokens.get(pos);
            if(t.isConst())
                n.add(new FinalNode(t));
            if(t.isOper())
                n.add(new Node(t));
            if(t.isGroup()) {
                int paren = 0;
                int x = pos + 1;
                do{
                    if(pTokens.get(x).TYPE == Token.Types.LPAR) paren++;
                    if(pTokens.get(x).TYPE == Token.Types.RPAR) paren--;
                    x++;
                } while(0 < paren && x < pTokens.size());

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


    /**
     * Creates a master node using the pNode as a starting point by applying the Order of Operations.
     * Note: pNode should already have been condensed via {@link #condeseNodes(int,Node,ArrayList) condeseNodes}. 
     * @param pNode    The node that will be used to generate the new hierarchically-structured master node.
     * @return The "master" node - that is, the node to control all other nodes. HAH - LOTR reference.
     */
private Node completeNodes(Node pNode){
        if(pNode instanceof FinalNode)
            return pNode;      
        Node e = new Node(pNode.TOKEN);
        int i = 0;
        while(i < pNode.size()){
            Node n = pNode.get(i);
            if(n instanceof FinalNode){
                e.addD(n);
            }
            else if(n.TOKEN.isOper()){
                for(int depth = 1; depth < e.depth(); depth++){
                    Node nD = e.getD(depth);
                    if(nD instanceof FinalNode){
                        n.add(nD);
                        e.set(depth - 1, n); //depth is a final node.
                        break;
                    }
                    else if(n.TOKEN.priority() < nD.TOKEN.priority()){
                        n.add(nD);
                        n.add(completeNodes(pNode.get(i + 1)));
                        i++;
                        e.setD(depth - 1, n);
                        break;
                    }
                }

            }
            else if(n.TOKEN.isGroup()){
                e.addD(completeNodes(n));
            }
            else{
                throw new NotDefinedException("There is no known way to complete the node '" + n + "'.");
            }
            i++;
        }

        return e;
    }
    /** 
     * Generates a "master node" from a list of tokens.
     * @param pTokens   The list of tokens that the master node will be based off of.
     * @return The new master node - usually set to {@link #node}.
     */
    private Node generateNodes(ArrayList<Token> pTokens) {
        return completeNodes((Node)condeseNodes(0, new Node(new Token("E", Token.Types.NULL)), pTokens)[1]);
    }
    
    /** 
     * Generates an ArrayList of tokens that represent rEq.
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
            System.err.println(prev);
            c = rEq.charAt(x);
            if(prev.length() != 0 && prev.charAt(0) == '\''){
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
        System.err.println(tokens);
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
     * Checks if a string consists only of letters, digits, and / or periods.
     * @param str   The string to test.
     * @return      True if the string consists only of letters, digits, and / or periods. False otherwise.
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
     * Checks if a string consists only of letters. 
     * @param str   The string to test.
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
     * Checks if a string consists only of digits and / or periods. 
     * Please note that this doesn't check to make sure there _are_ digits. So "...." will return true.
     * @param str   The string to test.
     * @return      True if the String is only digits and / or periods. False otherwise.
     */        
    public static boolean isNumP(String str) {
        for(char c : str.toCharArray())
            if(!isNumP(c))
                return false;
        return true;
    } 
}

