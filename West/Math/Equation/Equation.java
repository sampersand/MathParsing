package West.Math.Equation;

import West.Math.MathObject;
import West.Math.Equation.Function.CustomFunction;

import West.Math.Set.Node.EquationNode;
import West.Math.Set.Node.TokenNode;
import West.Math.Set.Node.Node;
import West.Math.Exception.TypeMisMatchException;
import West.Math.Exception.NotDefinedException;

import java.util.HashMap;
import java.util.regex.Pattern;

import West.Math.Equation.Function.OperationFunction;

import java.util.HashMap;
import West.Math.Set.Collection;

/**
 * A class that represents an equation in West.Math. It really is just a collection of Nodes that are equal to
 * each other.
 * 
 * @author Sam Westerman
 * @version 0.80
 * @since 0.1
 */
public class Equation implements MathObject {

    /** This classe's list of subEquations that are equal to eachother. */
    protected EquationNode subEquations;
    public static final HashMap<String, Object> CCHARS = new HashMap<String, Object>()
    {{
        put("op_un_l", OperationFunction.UNARY_LEFT);
        put("op_un_r", OperationFunction.UNARY_RIGHT);
        put("op_bi", OperationFunction.BINARY);
        put("comp", EquationNode.COMPARATOR);
        put("bool", EquationNode.BOOLEANS);
        put("paren_l", Token.PAREN_L);
        put("paren_r", Token.PAREN_R);
        put("delim", Token.DELIM);
    }};


    /**
     * The default constructor. This just instantiates {@link #subEquations} as an empty Collection.
     */
    public Equation() {
        subEquations = new EquationNode();
    }
    public Equation(String comp) {
        subEquations = new EquationNode().setToken(comp);
    }

    /**
     * Adds all of the {@link Node}s as defined in <code>pCol</code>.
     * @param pCol    An Collection of {@link Node}s that will be added to {@link #subEquations}.
     * @return This class, with <code>pCol</code> added.
     */
    public Equation add(EquationNode pCol) {
        subEquations.add(pCol);
        return this;
    }

    public Equation add(String pStr){
        Collection<Token> tokens = parseTokens(pStr);
        subEquations = segmentTokens(tokens);
        return this;
    }

    public Equation setToken(String comp) {
        subEquations.setToken(comp);
        return this;
    }
    /**
     * Returns the {@link #subEquations} that this class defines.
     * @return {@link #subEquations}
     */
    public EquationNode subEquations() {
        return subEquations;
    }

   private EquationNode segmentTokens(Collection<Token> tokens){
        EquationNode eqnod = new EquationNode(EquationNode.getBool(""));
        System.out.println(tokens);
        Collection<Token> prev = new Collection<Token>();
        for(Token t : tokens){
            System.out.println("~~~BEFORE~~~\ntoken:\n"+t.toFancyString()+"\n\neqnod:\n"+
                               eqnod.toFancyString()+"\n\nprev:\n"+prev.toFancyString()+"\n\n---\n\n");
            if(isBool(t.val()) != null){
                eqnod.addCD(TokenNode.generateMasterNode(prev));
                eqnod.addED(new EquationNode(EquationNode.getBool(t.val())));
                prev.empty();
            } else if(isComp(t.val()) != null){
                eqnod.addBD(new EquationNode(TokenNode.generateMasterNode(prev)).
                                             setToken(EquationNode.getComp(t.val())));
                prev.empty();
            } else {
                prev.add(t);
            }
            System.out.println("~~~AFTER~~~\neqnod:\n"+
                               eqnod.toFancyString()+"\n\nprev:\n"+prev.toFancyString()+"\n---\n");
        }
        if(prev.size() != 0)
            eqnod.addCD(TokenNode.generateMasterNode(prev));
            System.out.println("~~~END~~~\neqnod:\n"+
                               eqnod.toFancyString()+"\n\nprev:\n"+prev.toFancyString()+"\n---\n");

        // EquationNode eqnod = new EquationNode(EquationNode.getBool(""));
        // TokenNode prev = new TokenNode();
        // for(Token t : tokens){
        //     System.out.println("~~~BEFORE~~~\ntoken:\n"+t.toFancyString()+"\n\neqnod:\n"+
        //                        eqnod.toFancyString()+"\n\nprev:\n"+prev.toFancyString()+"\n\n---\n\n");
        //     if(isBool(t.val()) != null){
        //         eqnod.addBD(eqnod.depth(), new EquationNode(prev).setToken(EquationNode.getComp(t.val())));
        //         eqnod.addED(eqnod.depth(),new EquationNode(EquationNode.getBool(t.val())));
        //     } else if(isComp(t.val()) != null){
        //         eqnod.addBD(eqnod.depth(), new EquationNode(prev).setToken(EquationNode.getComp(t.val())));
        //         prev = new TokenNode();
        //     } else {
        //         prev.add(new TokenNode(t));
        //     }
        //     System.out.println("~~~AFTER~~~\neqnod:\n"+
        //                        eqnod.toFancyString()+"\n\nprev:\n"+prev.toFancyString()+"\n---\n");
        // }
        // if(prev.size() != 0)
        //     eqnod.addD(eqnod.depth(), prev);




        // Collection<Collection<Token>> units = new Collection<Collection<Token>>();
        // Collection<Token> prev = new Collection<Token>();
        // for(Token t : tokens){
        //     prev.add(t);
        //     if(isComp(t.val()) != null){
        //         units.add(prev);
        //         prev = new Collection<Token>();
        //     }
        // }
        // System.out.println(tokens);
        // System.out.println(units);
        // if(prev.size() != 0)
        //     units.add(prev);
        // EquationNode eqnod = new EquationNode().setToken(units.get(-1).val());
        // for(int i = 0; i < units.size(); i++){
        //     Collection<Token> expr = units.get(i);
        //     String comp = null;
        //     if(isComp(expr.get(-1).val()) != null)
        //         comp = expr.pop(expr.size() - 1).val();
        //     if(comp == null)
        //         eqnod.add(TokenNode.generateMasterNode(expr));
        //     else
        //         eqnod.add(new EquationNode(TokenNode.generateMasterNode(expr)).setToken(comp));
        // }
        // System.out.println(eqnod.toFancyString());
        System.out.println(eqnod.toFancyString());
        return eqnod;

    }

   // private EquationNode segmentTokens(Collection<Token> tokens){
   //      Collection<Collection<Token>> units = new Collection<Collection<Token>>();
   //      Collection<Token> prev = new Collection<Token>();
   //      for(Token t : tokens){
   //          prev.add(t);
   //          if(isComp(t.val()) != null){
   //              units.add(prev);
   //              prev = new Collection<Token>();
   //          }
   //      }
   //      System.out.println(tokens);
   //      System.out.println(units);
   //      if(prev.size() != 0)
   //          units.add(prev);
   //      EquationNode eqnod = new EquationNode().setToken(units.get(-1).val());
   //      for(int i = 0; i < units.size(); i++){
   //          Collection<Token> expr = units.get(i);
   //          String comp = null;
   //          if(isComp(expr.get(-1).val()) != null)
   //              comp = expr.pop(expr.size() - 1).val();
   //          if(comp == null)
   //              eqnod.add(TokenNode.generateMasterNode(expr));
   //          else
   //              eqnod.add(new EquationNode(TokenNode.generateMasterNode(expr)).setToken(comp));
   //      }
   //      System.out.println(eqnod.toFancyString());
   //      return eqnod;

   //  }

    /**
     * Fixes any terms that might be misleading to the compiler. For example, <code>sinx</code> will become
     * <code>sin(x)</code>. Note: To not have it do any fixing, put a "@" at the beginning of the input String
     * @param pEq            The expression to be corrected.
     * @return A corrected version of the expression.
     */
    public static String fixNode(String pEq) {
        //TODO: FIX
        if(pEq.charAt(0) == '@')
            return pEq.substring(1);
        String[] trigf = new String[]{"sec", "csc", "cot", "sinh", "cosh", "tanh", "sin", "cos", "tan"};
        for(String trig : trigf) {
            pEq = pEq.replaceAll("()" + trig + "(?!h)([A-za-z]+)","$1" + trig + "($2)");
        }

        //# = number    A = letter   & = BOTH
        pEq = pEq.replaceAll("\\-\\(", "-1*("); // -( → -1 * (
        pEq = pEq.replaceAll("([\\d\\w.]+)E([\\d\\w.-]+)","($1*10^(0$2))"); // &.&E-?&.& → (&.&*10^(-?&.&))
        pEq = pEq.replaceAll("([\\d.]+)(?!E)(\\(|(?:[A-Za-z]+))", "$1*$2"); // #A → #*A
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
                if(isBool(all) != null){
                    repl = isBool(all);
                    if(!replLast(all, repl).isEmpty()) tokens.add(new Token(replLast(all, repl), Token.Type.VAR));
                    tokens.add(new Token(isBool(all), Token.Type.BOOL));
                } else if(isComp(all) != null){
                    repl = isComp(all);
                    if(!replLast(all, repl).isEmpty()) tokens.add(new Token(replLast(all, repl), Token.Type.VAR));
                    tokens.add(new Token(isComp(all), Token.Type.COMP));
                } else if(isOper(all) != null){
                    repl = isOper(all);
                    if(!replLast(all, repl).isEmpty()) tokens.add(new Token(replLast(all, repl), Token.Type.VAR));
                    tokens.add(new Token(isOper(all), Token.Type.OPER));
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
        return isOper(s) != null || isParen(s) != null || isDelim(s) != null || isComp(s) != null || isBool(s) != null;
    }
    public static String isOper(String s){
        String ret = null;
        if((s.length() == 1 || (s.length() > 0 && s.charAt(s.length() - 1) == 'E')) &&
                            (ret = isInLast(s,((HashMap)CCHARS.get("op_un_l")).keySet())) != null)
            return ret;
        if(s.length() != 1 && (ret = isInLast(s,((HashMap)CCHARS.get("op_un_r")).keySet())) != null)
            return ret;
        return isInLast(s, ((HashMap)CCHARS.get("op_bi")).keySet());
    }
    

    public static String isBool(String s){
        return isInLast(s, ((HashMap<String,Object>)CCHARS.get("bool")).keySet());
    }
    public static String isComp(String s){
        return isInLast(s, ((HashMap<String,Object>)CCHARS.get("comp")).keySet());
    }

    public static String isDelim(String s){
        return isInLast(s,(Collection<String>)CCHARS.get("delim"));
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
            System.out.println("S:"+s+", pStr:"+pStr+", repl:"+pStr.replaceAll("\\Q" + s + "\\E$",""));
            if(pStr.replaceAll("\\Q" + s + "\\E$","").length() != pStr.length())
                return s;
        }
        return null;
    }
    public String exprstoStr(){
        return exprstoStr(subEquations);
    }

    public String exprstoStr(Node<?, ?> node){
        String ret = "";
        if(node.token() instanceof Token){
            Token token = (Token)node.token();
            ret += token.val();
            if(token.type() == Token.Type.OPER || token.type() == Token.Type.FUNC){
                ret += "(";
                for(Node n : node){
                    ret += exprstoStr(n) + ", ";
                }
                ret = ret.substring(0, ret.length() - 2) + ")";
            } else{
                assert node.size() == 0 : "node is not a function, but has subnodes: " + node;
            }
        } else {
            for(Node<?, ?> n : node){
                String es;
                ret += exprstoStr(n);
                ret += " " + (node.token() == null ? "" : node.token()) + " ";
            }
            return ret.substring(0, ret.length() - 2);
        }
        return ret;
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
        for(Node<?, ?> eqn : subEquations){
            ret += eqn.toFullString(idtLvl + 2) + "\n";
            // ret += "\n" + indent(idtLvl + 3) + "Comparator:" + "\n" + indentE(idtLvl + 4) + cc.token();
            // ret += "\n" + indent(idtLvl + 3) + "EquationNodes:";
            // for(EquationNode n : cc)
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