package Math.Equation;
import Math.Exception.TypeMisMatchException;
import Math.Exception.NotDefinedException;
import Math.Set.Set;
import Math.Display.Grapher;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * A class that models an equation, and its behaviour.
 * @author Sam Westerman
 * @version 0.2
 */

public class Equation {
// TODO: cause '1 * -2' to NOT crash.
    /**
     * Note that if this is going to be called from the commandline, the syntax is as follows:
     * java Equation (-v v1:val v2:val) (-f f1:val f2:val) "equation"
     * @param args          The arguments to pass. See description of this method for details.
     */
    public static void main(String[] args) throws NotDefinedException, TypeMisMatchException {
        Equation eq;
        if(args.length == 0){
            eq = new Equation("@graph('y')");
            // eq = new Equation("graph('1,2,3,4,5','2,3,4,5,6')");
            // eq = new Equation("@graph('tan(f(y))','(y-2)*(y+2)*y') ");
            // eq = new Equation("sin(x+f('e,2,C')*f(4,f(D,3,pi)))");
            eq.factors = eq.factors.addVars(new String[]{"x:10","C:3","D:4"});
            eq.factors = eq.factors.addFuncs(new String[]{"f","graph","sum:summation"});
        } else {
            eq = new Equation();
            if(args.length == 1){
                eq = new Equation(args[0]);
            } else if(args.length > 1){
                int i = -1;
                char type = ' ';
                if(!args[0].equals("--f") && !args[0].equals("--v") && !args[0].equals("--e"))
                    throw new NotDefinedException("first value has to be --f, --v, or --e");
                while(i < args.length - 1){ //args.length is String.
                    i++;
                    if(args[i].equals("--v")){type = 'v'; continue;}
                    if(args[i].equals("--f")){type = 'f'; continue;}
                    if(args[i].equals("--e")){type = 'e'; continue;}
                    if(type == 'v'){
                        try{
                            eq.factors = eq.factors.addVar(args[i].split(":")[0],
                                Double.parseDouble(args[i].split(":")[1]));
                        } catch(NumberFormatException err){
                            System.err.println("Syntax: VARNAME:VARVAL (" + args[i] + ")");
                        } catch(ArrayIndexOutOfBoundsException err){
                            System.err.println("Syntax: VARNAME:VARVAL (" + args[i] + ")");
                        }
                    } else if (type == 'f'){
                        try{
                            eq.factors = eq.factors.addFunc(args[i].split(":")[0], args[i].split(":")[1]);
                        } catch(NumberFormatException err){
                            System.err.println("Syntax: FUNCNAME:FUNCVAL (" + args[i] + ")");
                        } catch(ArrayIndexOutOfBoundsException err){
                            System.err.println("Syntax: FUNCNAME:FUNCVAL (" + args[i] + ")");
                        }
                    } else if (type == 'e'){
                        eq.equation += args[i];
                    }
                }
                eq.genNode();
            }

        }
        System.out.println(eq);
        System.out.println("RESULT: " + eq.eval());
    }

    /** The raw equation. */
    public String equation;

    // /**
    //  * An ArrayList of tokens that represent {@link #equation}. Generally unused after generating {@link #node}.
    //  */
    // public ArrayList<Token> tokens;

    /**
     * The Node representing the whole equation. 
     * @see   Node#generateNodes(ArrayList)
     */
    public Node node;

    /**
     * The list of factors for this equation. This includes what values variables are, and where to 
     * find what functions do.
     */
    public Factors factors;


    /**
     * The default constructor for the Equation class. Just passes an empty String, Node, and Factors to the 
     * {@link #Equation(String,Node,Factors) main equation constructor}.
     */
    public Equation() {
        this("",new Node(), new Factors());
    }

    /**
     * The String-only constructor for the equation class. Just passes a Node created based off pEq, and an empty
     * {@link Factors} class to the {@link #Equation(String,Node,Factors) main equation constructor}.
     * @param pEq       A String containing the equation that this class will be modeled after.
     */ 
    public Equation(String pEq) {
        this(pEq, Node.generateNodes(Equation.parseTokens(pEq)), new Factors());
    }

    /**
     * The String and Factors constructor for the equation class. Just passes a Node created based off pEq, and pFactors
     * to the {@link #Equation(String,Node,Factors) main equation constructor}.
     * @param pEq       The String representation of the equation. Is only ever used to identify individual equations.
     * @param pFactors  The {@link Factors} instance for the equation.
     */ 
    public Equation(String pEq, Factors pFactors) {
        this(pEq, Node.generateNodes(Equation.parseTokens(pEq)), pFactors);
    }

    /**
     * The Node-only for the equation class. Just passes a String created based on Node's subnodes, and an empty
     * {@link Factors} class to the {@link #Equation(String,Node,Factors) main equation constructor}.
     * @param pN        The Node that the equation class will be modeled after.
     */ 
    public Equation(Node pN) {
        this(pN.genEqString(), pN, new Factors());
    }


    /**
     * The main constructor for the equation class. Takes the parameter pEq, and parses the tokens from it,
     * and generates a {@link Node} model for it.
     * @param pEq       The String representation of the equation. Is only ever used to identify individual equations.
     * @param pN        The Node that the entire equation is based off of.
     * @param pFactors  The {@link Factors} instance for the equation.
     */ 
    public Equation(String pEq, Node pN, Factors pFactors){
        equation = pEq;
        node = pN;
        factors = pFactors;
    }


    /** 
     * Generates {@link #node} using {@link #equation}.
     */
    public void genNode(){
        genNode(equation);
    }

    /** 
     * Generates {@link #node} using pEq.
     * @param pEq       The equation that this class's node will be based off of.
     */
    public void genNode(String pEq){
        node = Node.generateNodes(Equation.parseTokens(pEq));
    }
    /**
     * Evaluates pNode using pFactors.
     * @param pFactors  The factors (variable values and function definitions) that will be used to evaluate pNode.
     * @param pNode     The node that will be evaluated using pFactors.
     * @return A numerical representation of pNode, when evaluated using pFactors.     
     * @throws NotDefinedException      Thrown if either a variable or a function wasn't defined.
     * @see  Factors#eval(Node)
     */
    public static double eval(Factors pFactors, Node pNode) throws NotDefinedException {
        return pFactors.eval(pNode);
    }

    /**
     * Evaluates pNode using pFactors and pVars.
     * @param pFactors  The factors (variable values and function definitions) that will be used to evaluate pNode.
     * @param pNode     The node that will be evaluated using pFactors.
     * @param pVars     Used instead of pFactors's vars if a conflict between the two arises. 
     * @return A numerical representation of pNode, when evaluated using pFactors, and pVars.
     * @throws NotDefinedException      Thrown if either a variable or a function wans't defined.
     * @see  Factors#eval(Node, HashMap)
     */
    public static double eval(Factors pFactors, Node pNode, HashMap<String, Double> pVars) throws NotDefinedException {
        return pFactors.eval(pNode, pVars);
    }

    /**
     * Evaluates {@link #node} using {@link #factors}.
     * @return A numerical representation of {@link #node}, when evaluated using {@link #factors}.
     * @throws NotDefinedException      Thrown if either a variable or a function wasn't defined.
     * @see  #eval(Factors, Node)
     */
    public double eval() throws  NotDefinedException {
        return eval(factors, node);
    }

    /**
     * Evaluates {@link #node} using {@link #factors} and pVars.
     * @param pVars     Used instead of {@link #factors}'s vars if a conflict between the two arises.      
     * @return A numerical representation of {@link #node}, when evaluated using {@link #factors} and pVars.
     * @throws NotDefinedException      Thrown if either a variable or a function wasn't defined.     
     * @see  #eval(Factors, Node, HashMap)
     */    
    public double eval(HashMap<String, Double> pVars) throws NotDefinedException {
        return eval(factors, node, pVars);
    }

    /** 
     * Fixes any terms that might be misleading to the compiler. For example, <code>sinx</code> will become
     * <code>sin(x)</code>. Note: To not have it do any fixing, put a "@" at the beginning of the input String
     * @param eq            The equation to be corrected.
     * @return A corrected version of the equation.
     */
    public static String fixEquation(String eq){
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
     * Note that this removes all whitespace (including spaces) before handling the equation.
     * @param rEq    The equation to be parsed.
     * @return An ArrayList of tokens, each representing a different chunk of the equation. 
     * @see Token
     */
    public static ArrayList<Token> parseTokens(String rEq) throws TypeMisMatchException{
        rEq = fixEquation(rEq.trim().replaceAll(" ",""));
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
     * Gives a String representation of this equation. Comprised of {@link #equation}, {@link #factors}, and
     * {@link #node}.
     * @return A String representation of this equation. 
     */
    public String toFullString(){
        return "--Equation--\n--RawEq--\n" + equation + "\n--Factors--\n" + factors + "\n--Nodes--\n" + node.toStringL(1);
    }
    /** 
     * Just returns {@link #equation}.
     * @return A basic String representation of this equation.
     */
    public String toString(){
        return "Equation '" + equation + "'.";
    }
    /** 
     * Gives a pretty-looking representation of this equation. comprised of {@link #equation}, and {@link #factors}' 
     * {@link Factors#vars} and {@link Factors#funcs}.
     * @return A fancy String representation of this equation.
     */
    public String toFancyString(){
        String ret = "------=[" + equation + "]=------";
        if(factors.vars.size() > 0) {
            ret += "\nVarriables:";
            for(String var : factors.vars.keySet()){
                ret += "\n\t" + var + " = " + factors.getVar(var);
            }
        } if(factors.funcs.size() > 0){
            ret += "\nFunctions: (stuff in [] are optional)";
            Object[] keys = factors.funcs.keySet().toArray();
            for(int i = 0; i < keys.length; i++){
                String func = (String) keys[i];
                ret += "\n\t" + func + ": \n\t\tHelp   : " + factors.getFunc(func).getHelp()  + 
                                          "\n\t\tSyntax : " + func + "(" + factors.getFunc(func).getSyntax() + ")";
                if(i != keys.length - 1)
                ret += "\n\t---\t---";
            }
        }
        ret += "\n";
        for(char c : equation.toCharArray()) ret += ("-");
        ret += "----------------\n";
        return ret;
    }

    public double solve(){return solve("");}
    public double solve(String varToSolveFor){ return solve("", new HashMap<String, Double>());}
    public double solve(String varToSolveFor, HashMap<String, Double> pVars){
        return factors.eval(node, pVars);
    }
    public void graph(){
        System.err.println("Currently, solve isn't very good. Oh well.");
        Grapher grapher = new Grapher(this);
        grapher.graph();
    }
}
