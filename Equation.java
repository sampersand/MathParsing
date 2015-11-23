import java.util.ArrayList;
/** 
 * A class that models an equation, and its behaviour.
 * @author Sam Westerman
 * @version 0.1
 */
public class Equation {
    public static void main(String[] args) {
        // Equation eq = new Equation("1 + b * (2 + 3) + f(x, 4 + 1, a(5)) + 6");
        Equation eq = new Equation("1 + a(b, 2) * (c/3)^4");
        // Equation eq = new Equation("b * (2 + 3 - (4 * 5)) / f( 6 ) ");
        // Equation eq = new Equation("3 / f(4) - 5");
    }

    /** The raw equation, totally untouched. Gets set right when Equation is initialized. */
    public final String RAW_EQ;

    /** An ArrayList of tokens that comprise <code>RAW_EQ</code>. */
    public final ArrayList<Token> TOKENS;

    public final Node EQ_NODE;

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
        RAW_EQ = pEq;
        System.out.println(RAW_EQ);
        System.out.println("--");        
        TOKENS = parseTokens(RAW_EQ);
        System.out.println(TOKENS);
        System.out.println("--");        
        EQ_NODE = generateNodes(TOKENS);
        System.out.println(EQ_NODE);
    }

    // private Object[] decant(int pos, ArrayList<Token> pTokens){
    //     Node n = new Node();
    //     while(pos < pTokens.size()){
    //         Token t = pTokens.get(pos);
    //         if(t.isConst()){
    //             n.add(new FinalNode(t));
    //         }
    //         if(t.isOper()){
    //             Object[] temp = decant(pos, pTokens);
    //             pos = (int)temp[0];
    //             n.subNodes.set(n.subNodes.size() - 1, new Node(t, nodes.get(nodes.size()-1), (Node) temp[1]));
    //         }
    //         pos++;
    //     } 
    //     return new Object[]{pos,n};

    // }
    private Node generateNodes(ArrayList<Token> pTokens){
        return (Node)generateNodes(0, new Node(new Token("Equation", Token.Types.NULL)), pTokens)[1];
    }
    private Object[] generateNodes(int pos, Node n, ArrayList<Token> pTokens){
        while(pos < pTokens.size()){
            Token t = pTokens.get(pos);
            if(t.isConst()){
                n.add(new FinalNode(t));
            }
            if(t.isGroup()){
                if(pTokens.get(pos+1).TYPE != Token.Types.LPAR)
                    System.err.println("[ERROR] Pos is FUNC / GROUP, but POS + 1 isn't an LPAR. Continuing anyway.");
                int paren = 1;
                int x;
                for(x = pos + 2; paren > 0 && x < pTokens.size(); x++){
                    if(pTokens.get(x).TYPE == Token.Types.RPAR) paren--;
                    if(pTokens.get(x).TYPE == Token.Types.LPAR) paren++;
                }
                ArrayList<Token> passTokens = new ArrayList<Token>();
                for(Token token : pTokens.subList(pos , x)) passTokens.add(token);
                System.out.println("GRP @@@@@ " + passTokens + " @@@@@ " + t + " @@@@@ " + n);
                Object[] temp = generateNodes(1, new Node(t), passTokens);
                pos = pos + (int)temp[0] - 1;
                Node n2 = (Node)temp[1];
                n.add(n2);
            }
            if(t.isOper()){
                // System.out.println(n.get(n.size()-1));
                Object[] temp = generateNodes(pos + 1, t.isConst() ? new FinalNode(t) : new Node(t), pTokens);
                pos = (int)temp[0];
                System.out.println("OPR @@@@@ " + pTokens + " @@@@@ " + t + " @@@@@ " + n);                
                Node n2 = (Node)temp[1];
                n2.add(n.get(n.size()-1));
                n2.add(n2.get(0)); // these two swap
                n2.remove(0); //the order
                n.add(n2);
                n.subNodes.remove(n.subNodes.size()-2);                
            }
            pos++;
        }
        return new Object[]{pos, n};
    }
    /** 
     * Generates an ArrayList of tokens that make up the inputted equation. 
     * Note that this removes all whitespace (including spaces) before handling the equation.
     * @param rEq    The equation to be parsed.
     * @return An ArrayList of tokens, each representing a different chunk of the equation. 
     * @see Token.Tokens
     */
    private ArrayList<Token> parseTokens(String rEq){
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
                    if(!isAlpha(prev))
                        System.err.println("[ERROR] Uh oh! '" + prev +
                        "' isn't Alphabetical, but is succeeded by '('. Continuing anyway.");
                    if(prev.length() != 0)
                        tokens.add(new Token(prev, Token.Types.FUNC));
                    else
                        tokens.add(new Token(prev, Token.Types.GROUP));
                    tokens.add(Token.LPAR);
                    break;
                case ')': 
                    if(prev.length() != 0)
                        tokens.add(new Token(prev, isNumP(prev) ? Token.Types.NUM : Token.Types.VAR));
                    tokens.add(Token.RPAR);
                    break;
                case '-': case '+': case '*': case '/': case '^':
                    if(prev.length() != 0)
                        tokens.add(new Token(prev, isNumP(prev) ? Token.Types.NUM : Token.Types.VAR));
                    tokens.add(new Token(c, Token.Types.OPER));
                    break;
                case ',':
                    if(prev.length() != 0)
                        tokens.add(new Token(prev, isNumP(prev) ? Token.Types.NUM : Token.Types.VAR));
                    tokens.add(new Token(c, Token.Types.DELIM));
                    break;

                default:
                    if(prev.length() != 0)
                        tokens.add(new Token(prev, isNumP(prev) ? Token.Types.NUM : Token.Types.VAR));
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

