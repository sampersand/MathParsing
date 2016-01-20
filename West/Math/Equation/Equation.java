package West.Math.Equation;

import West.Math.MathObject;
import West.Math.Equation.Function.Function;

import West.Math.Set.Node.TokenNode;
import West.Math.Set.Node.Node;

import java.util.HashMap;

import West.Math.Set.Collection;

/**
 * A class that represents an equation in West.Math. It really is just a collection of Nodes that are equal to
 * each other.
 * 
 * @author Sam Westerman
 * @version 1.1
 * @since 0.1
 */
public class Equation implements MathObject {

    /** This classe's list of subEquations that are equal to eachother. */
    protected TokenNode subEquations;

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
        subEquations.add(TokenNode.generateMasterNode(parseTokens(pStr)));
        subEquations = subEquations.removeExtraFuncs();
        return this;
    }

    public Equation setToken(String comp) {
        subEquations.setToken(new Token(comp, Token.Type.FUNC));
        return this;
    }

    public String getVar(){
        return ((Token)subEquations.getSD(subEquations.depthS()).token()).val();
    }
    /**
     * Returns the {@link #subEquations} that this class defines.
     * @return {@link #subEquations}
     */
    public TokenNode subEquations() {
        return subEquations;
    }




    private static String[] TRIG = {"sec", "csc", "cot", "asin", "acos", "atan", 
                                    "sinh", "cosh", "tanh", "sin", "cos", "tan"};
    /**
     * Fixes any terms that might be misleading to the compiler. For example, <code>sinx</code> will become
     * <code>sin(x)</code>. Note: To not have it do any fixing, put a "@" at the beginning of the input String
     * @param pEq            The expression to be corrected.
     * @return A corrected version of the expression.
     */
    public static String fixNode(String pEq) {
        if(pEq.charAt(0) == '@')
            return pEq.substring(1);
        pEq = pEq.replaceAll("^([\\wΘπ]+)=-","$1=–"); // makes parsing better. thrown together, however.
        if(pEq.matches(".*([0-9.–-]+)[Ee]([0-9.–-]+).*"))
            pEq = pEq.replaceAll("([\\d.–-]+)[Ee]([\\d.–-]+)","$1*10^$2").replaceAll("-","–"); //  sci notation
        pEq = pEq.replaceAll("²", "^2"); //  exponents
        pEq = pEq.replaceAll("³", "^3"); //  i.e '²' --> ^2 
        pEq = pEq.replaceAll("⁴", "^4"); //  ^^^
        pEq = pEq.replaceAll("⅟","1/"); //  ^^^s
        for(String trig : TRIG) //  Trig i.e. 'coshΘ' --> cosh(Θ)
            pEq = pEq.replaceAll("(?!<a)("+trig+")(?!h)(\\^[\\d.-]+)?(?!\\()([\\w\\d().]+)", "$1($3)$2"); 
        pEq = pEq.replaceAll("(?<![0-9.])([0-9.]+)(?!\\d|\\.)(\\w|Θ|π)","$1·$2");
        //  assert false : pEq;
        //  pEq = pEq.replaceAll("(?<!\\w)([0-9]+(?:\\.[0-9]+)?)([A-Za-z])","$1·$2");
        return pEq;
    }

    private static String getOpersList(){
        String ret = "((?!=$";
        for(String s : Function.BIN_OPERS)
            ret += "|\\Q" + s + "\\E";
        //  assert false : ret + ").)";
        return ret + ")[^0-9])";
    }
    /**
     * Generates an Collection of tokens that represent rEq.
     * Note that this removes all whitespace (including spaces) before handling the expression.
     * @param rEq    The expression to be parsed.
     * @return An Collection of tokens, each representing a different chunk of the expression. 
     * @see Token
     */
    public static Collection<Token> parseTokens(String rEq) {
        rEq = fixNode(rEq.trim().replaceAll(" ","")); // remove all spaces
        Collection<Token> tokens = new Collection<Token>();
        String all = "", s;

        for(int x = 0; x < rEq.length(); x++) {
            s = "" + rEq.charAt(x);
            all += s;
            if(!isControlChar(all)) {
                if(x == rEq.length()-1){
                    tokens.add(new Token(all, Token.Type.VAR));
                    all = "";
                }
                continue;
            }
            String repl;
            if((repl = isUNL(all)) != null){
                tokens.add(new Token(isUNL(all), Token.Type.UNL));
                all=all.substring(isUNL(all).length());
            }
            if((repl = isParenL(all)) != null){
                if(isInLast(s,Token.PAREN_L) != null) // if its a left paren, make function
                    tokens.add(new Token(replLast(all, repl), Token.Type.FUNC));
                else if(!replLast(all, repl).isEmpty())
                    tokens.add(new Token(replLast(all, repl), Token.Type.VAR));
                tokens.add(new Token(repl, Token.Type.PAREN));
            }
            else if((repl = isParenR(all)) != null){
                if(!replLast(all, repl).isEmpty()) tokens.add(new Token(replLast(all, repl), Token.Type.VAR));
                tokens.add(new Token(repl, Token.Type.PAREN));
            } else if((repl = isBinOper(all)) != null){
                if(!replLast(all, repl).isEmpty()) tokens.add(new Token(replLast(all, repl), Token.Type.VAR));
                tokens.add(new Token(isBinOper(all), Token.Type.BINOPER));
            } else if((repl = isDelim(all)) != null){
                if(!replLast(all, repl).isEmpty()) tokens.add(new Token(replLast(all, repl), Token.Type.VAR));
                tokens.add(new Token(isDelim(all), Token.Type.DELIM));
            } else if((repl = isUNR(all)) != null){
                if(!replLast(all, repl).isEmpty()) tokens.add(new Token(replLast(all, repl), Token.Type.VAR));
                tokens.add(new Token(isUNR(all), Token.Type.UNR));

            } else if (isUNL(all)!=null){
                assert false;
            }

            all = "";
        }
        return tokens;
    }

    private static String replLast(String str, String last){
        return str.replaceAll("\\Q" + last + "\\E$", "");
    }
    private static String replFirst(String str, String first){
        return str.replaceAll("^\\Q" + first + "\\E", "");
    }
    public static boolean isControlChar(String s){
        return isParenL(s) != null ||
               isParenR(s) != null ||
               isDelim(s) != null ||
               isBinOper(s) != null ||
               isUNR(s) != null || 
               isUNL(s) != null;
    }
    
    public static String isDelim(String s){
        return Token.isDelim(s);
    }

    public static String isBinOper(String s){
        return Function.isBinOper(s);
    }

    public static String isUNR(String s){
        return Function.isUNR(s);
    }

    public static String isUNL(String s){
        return Function.isUNL(s);
    }

    public static String isParenR(String s){
        return Token.isParenR(s);
    }

    public static String isParenL(String s){
        return Token.isParenL(s);
    }


    public static String isInLast(String pStr, java.util.Collection<String> pList){
        if(pStr.isEmpty()) // just makes it faster so it doesnt have to iterate thru everything
            return null;
        for(String s : pList){
            if(pStr.replaceAll("\\Q" + s + "\\E$","").length() != pStr.length())
                return s;
        }
        return null;
    }

    public static String isInFirst(String pStr, java.util.Collection<String> pList){
        if(pStr.isEmpty()) // just makes it faster so it doesnt have to iterate thru everything
            return null;
        for(String s : pList){
            if(pStr.replaceAll("^\\Q" + s + "\\E","").length() != pStr.length())
                return s;
        }
        return null;
    }
    public String exprstoStr(){
        return exprstoStr(subEquations);
    }

    public String exprstoStr(TokenNode node){
        return node.toExprString();
    }

    @Override
    public String toString() {
        return  "eq:" + exprstoStr();
    }

    @Override
    public String toFancyString(int idtLvl) {
        String ret = indent(idtLvl) + "Equation:\n";
        ret += indent(idtLvl + 1) + "Expression Str: " + exprstoStr()+"\n";
        ret += indent(idtLvl + 1) + "Expressions:\n";
        for(Node<?, ?> tkn : subEquations)
            ret += ((TokenNode)tkn).toFancyString(idtLvl + 2) + "\n";
        return ret + indentE(idtLvl + 2) + "\n" + indentE(idtLvl + 1);
    }

    @Override
    public String toFullString(int idtLvl) {
        String ret = indent(idtLvl) + "Equation:\n";
        ret += indent(idtLvl + 1) + "Comparator:\n"; 
        ret += indent(idtLvl + 2) + subEquations.token() + "\n";
        ret += indent(idtLvl + 1) + "Raw Equation:\n" + indentE(idtLvl + 2) + exprstoStr() + "\n";
        ret += indent(idtLvl + 1) + "Expressions:\n";
        for(Node<?, ?> tkn : subEquations)
            ret += ((TokenNode)tkn).toFullString(idtLvl + 2) + "\n";
        return ret + indentE(idtLvl + 2) + "\n" + indentE(idtLvl + 1);
    }

    @Override
    public Equation copy(){
        return new Equation(subEquations.token().toString()).add(subEquations);
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