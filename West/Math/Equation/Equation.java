package West.Math.Equation;

import West.Math.MathObject;
import West.Math.Equation.Function.InBuiltFunction;

import West.Math.Set.Node.TokenNode;
import West.Math.Set.Node.Node;
import West.Math.Exception.TypeMisMatchException;
import West.Math.Exception.NotDefinedException;

import java.util.HashMap;
import java.util.regex.Pattern;

import West.Math.Set.Collection;

/**
 * A class that represents an equation in West.Math. It really is just a collection of Nodes that are equal to
 * each other.
 * 
 * @author Sam Westerman
  * @version 0.89
 * @since 0.1
 */
public class Equation implements MathObject {

    /** This classe's list of subEquations that are equal to eachother. */
    protected TokenNode subEquations;
    public static final HashMap<String, Object> CCHARS = new HashMap<String, Object>()
    {{
        put("assign", InBuiltFunction.ASSIGNMENT);
        put("paren_l", Token.PAREN_L);
        put("paren_r", Token.PAREN_R);
        put("delim", Token.DELIM);
    }};


    /**
     * The default constructor. This just instantiates {@link #subEquations} as an empty Collection.
     */
    public Equation() {
        subEquations = new TokenNode();
    }
    public Equation(String comp) {
        subEquations = new TokenNode().setToken(new Token(comp, Token.Type.FUNC));
    }

    /**
     * Adds all of the {@link Node}s as defined in <code>pCol</code>.
     * @param pCol    An Collection of {@link Node}s that will be added to {@link #subEquations}.
     * @return This class, with <code>pCol</code> added.
     */
    public Equation add(TokenNode pCol) {
        subEquations.add(pCol);
        return this;
    }

    public Equation add(String pStr){
        Collection<Token> tokens = parseTokens(pStr);
        TokenNode tkn = segmentTokens(tokens);
        subEquations.add(tkn);
        return this;
    }

    public Equation setToken(String comp) {
        subEquations.setToken(new Token(comp, Token.Type.FUNC));
        return this;
    }
    /**
     * Returns the {@link #subEquations} that this class defines.
     * @return {@link #subEquations}
     */
    public TokenNode subEquations() {
        return subEquations;
    }

   private TokenNode segmentTokens(Collection<Token> tokens){
        // TokenNode tkn = new TokenNode(TokenNode.getBool(""));
        // Collection<Token> prev = new Collection<Token>();
        // for(Token t : tokens){
        //     if(isBool(t.val()) != null){
        //         tkn.addCD(TokenNode.generateMasterNode(prev));
        //         tkn.addED(new TokenNode(TokenNode.getBool(t.val())));
        //         prev.empty();
        //     } else if(isComp(t.val()) != null){
        //         tkn.addBD(new TokenNode(TokenNode.generateMasterNode(prev)).
        //                                      setToken(TokenNode.getComp(t.val())));
        //         prev.empty();
        //     } else {
                // prev.add(t);
        //     }
        // }
        // if(prev.size() != 0)
            // tkn.addCD(TokenNode.generateMasterNode(prev));
        System.out.println("TODO: REMOVE SEGMENTTOKENS");
        return TokenNode.generateMasterNode(tokens);//tkn;

    }

    /**
     * Fixes any terms that might be misleading to the compiler. For example, <code>sinx</code> will become
     * <code>sin(x)</code>. Note: To not have it do any fixing, put a "@" at the beginning of the input String
     * @param pEq            The expression to be corrected.
     * @return A corrected version of the expression.
     */
    public static String fixNode(String pEq) {
        if(pEq.charAt(0) == '@')
            return pEq.substring(1);
        String[] trigf = new String[]{"sec", "csc", "cot", "sinh", "cosh", "tanh", "sin", "cos", "tan"};
        for(String trig : trigf) {
            pEq = pEq.replaceAll("()" + trig + "(?!h)([A-za-z]+)","$1" + trig + "($2)");
        }

        //# = number    A = letter   & = BOTH
        // pEq = pEq.replaceAll("\\-\\(", "*(-1,"); // -( → -1 * (
        // pEq = pEq.replaceAll("^(\\w)(.)\\-", "$1$20-"); // THIS IS JSUT THROWN TOGETHER
        // pEq = pEq.replaceAll("([\\d\\w.]+)E([\\d\\w.-]+)","($1*10^(0$2))"); // &.&E-?&.& → (&.&*10^(-?&.&))
        // pEq = pEq.replaceAll("([\\d.]+)(?!E)(\\(|(?:[A-Za-z]+))", "$1*$2"); // #A → #*A
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
        String all = "", s;
        for(int x = 0; x < rEq.length(); x++) {
            s = "" + rEq.charAt(x);
            all += s;
            if(!isControlChar(all)) {
                if(x == rEq.length() - 1){
                    tokens.add(new Token(all, Token.Type.VAR));
                    all = "";
                }
                continue;
            }
            String repl;
            if(isParen(all) != null){
                repl = isParen(all);
                if(isInLast(s,(Collection<String>)CCHARS.get("paren_l")) != null) //if its a left paren, make function
                    tokens.add(new Token(replLast(all, repl), Token.Type.FUNC));
                if(isInLast(s,(Collection<String>)CCHARS.get("paren_r")) != null && !replLast(all, repl).isEmpty())
                    tokens.add(new Token(replLast(all, repl), Token.Type.VAR));
                tokens.add(new Token(repl, Token.Type.PAREN));
            } else{
                if(isAssign(all) != null){
                    repl = isAssign(all);
                    if(!replLast(all, repl).isEmpty()) tokens.add(new Token(replLast(all, repl), Token.Type.VAR));
                    tokens.add(new Token(isAssign(all), Token.Type.ASSIGN));
                } else if(isDelim(all) != null){
                    repl = isDelim(all);
                    if(!replLast(all, repl).isEmpty()) tokens.add(new Token(replLast(all, repl), Token.Type.VAR));
                    tokens.add(new Token(isDelim(all), Token.Type.DELIM));
                } else
                    assert false;
            }
            all = "";
        }
        return tokens;
    }
    private static String replLast(String str, String last){
        return str.replaceAll("\\Q" + last + "\\E$", "");
    }
    public static boolean isControlChar(String s){
        return isParen(s) != null || isDelim(s) != null || isAssign(s) != null;
    }
    
    public static String isAssign(String s){
        return isInLast(s, (Collection<String>)CCHARS.get("assign"));
    }

    public static String isDelim(String s){
        return isInLast(s, (Collection<String>)CCHARS.get("delim"));
    }

    public static String isParen(String s){
        String ret;
        if((ret = isInLast(s, (Collection<String>)CCHARS.get("paren_l"))) != null)
            return ret;
        return isInLast(s, (Collection<String>)CCHARS.get("paren_r"));
    }
    private static String isInLast(String pStr, java.util.Collection<String> pList){
        if(pStr.isEmpty())
            return null;
        for(String s : pList){
            if(pStr.replaceAll("\\Q" + s + "\\E$","").length() != pStr.length())
                return s;
        }
        return null;
    }
    public String exprstoStr(){
        return exprstoStr(subEquations);
    }

    public String exprstoStr(Node<?, ?> node){
        return node.toExprString();
    }

    @Override
    public String toString() {
        return  "eq:" + exprstoStr();
    }

    @Override
    public String toFancyString(int idtLvl) {
        String ret = indent(idtLvl) + "Equation:\n";
        ret += indentE(idtLvl + 1) + "Expressions:" + exprstoStr();
        return ret;
    }

    @Override
    public String toFullString(int idtLvl) {
        String ret = indent(idtLvl) + "Equation:\n";
        ret += indent(idtLvl + 1) + "Comparator:\n"; 
        ret += indent(idtLvl + 2) + subEquations.token() + "\n";
        ret += indent(idtLvl + 1) + "Raw Equation:\n" + indentE(idtLvl + 2) + exprstoStr() + "\n";
        ret += indent(idtLvl + 1) + "Expressions:\n";
        for(Node<?, ?> tkn : subEquations){
            ret += tkn.toFullString(idtLvl + 2) + "\n";
            // ret += "\n" + indent(idtLvl + 3) + "Comparator:" + "\n" + indentE(idtLvl + 4) + cc.token();
            // ret += "\n" + indent(idtLvl + 3) + "TokenNodes:";
            // for(TokenNode n : cc)
            //     ret += "\n" + n.toFullString(idtLvl + 4);
        }
        return ret + indentE(idtLvl + 2) + "\n" + indentE(idtLvl + 1);
    }

    @Override
    public Equation copy(){
        return new Equation("" + subEquations.token()).add(subEquations);
    }

    @Override
    public boolean equals(Object pObj){
        if(pObj == null || !(pObj instanceof Equation))
            return false;
        if(this == pObj)
            return true;
        Equation peq = (Equation)pObj;
        for(int i = 0; i < subEquations.size(); i++)
            if(!subEquations.get(i).equals(peq.subEquations().get(i)))
                return false;
        return true;

    }
}