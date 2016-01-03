package West.Math.Set.Node;
import West.Math.Set.Collection;
import java.util.ArrayList;
import West.Math.Equation.EquationSystem;
import West.Math.Equation.Function.InBuiltFunction;
import West.Math.Equation.Token;
import West.Math.MathObject;
import West.Math.Equation.Equation;
import static West.Math.Equation.Token.Type.*;
import java.util.HashMap;
/**
* TODO: JAVADOC
* 
* @author Sam Westerman
* @version 0.85
* @since 0.75
*/
public class TokenNode extends Node<Token, TokenNode> implements MathObject {

    public TokenNode(){
        super();
        token = new Token("",FUNC);
    }

    public TokenNode(Token pToken) {
        super(pToken);
    }

    protected Object[] condeseNodes(int pPos, ArrayList<Token> pTokens) {
        assert pTokens != null;
        assert checkForNullTokens(pTokens);
        TokenNode node = copy();
        while(pPos < pTokens.size()) {
            Token t = pTokens.get(pPos);
            assert t != null : "this should have been caught earlier.";
            if(t.isConst() || t.isBinOper()) {
                node.add(new TokenNode(t));
            } else if(t.isFunc()) {
                int paren = 0;
                int x = pPos + 1;
                do{
                    if(Token.PAREN_L.contains(pTokens.get(x).val())) paren++;
                    if(Token.PAREN_R.contains(pTokens.get(x).val())) paren--;
                    x++;
                } while(0 < paren && x < pTokens.size());
                Collection<Token> passTokens = new Collection<Token>();
                for(Token tk : pTokens.subList(pPos + 1, x ))
                     passTokens.add(tk);
                Object[] temp = new TokenNode(t).condeseNodes(0, passTokens);
                pPos += (int)temp[0];
                node.add((TokenNode)temp[1]);
            } 
            pPos++;
        }
        return new Object[]{pPos, node};
    }

    private TokenNode condense(){
        System.out.println(toFancyString());
        return this;
    }
    public TokenNode removeExtraFuncs(){
        if(!token.val().isEmpty())
            return this;
        assert size() == 1;
        return get(0).removeExtraFuncs();
    }

    @Override
    public TokenNode get(int pPos){
        return (TokenNode)super.get(pPos);
    }

    public static TokenNode generateMasterNode(ArrayList<Token> pTokens) {
        assert checkForNullTokens(pTokens);
        System.out.println(pTokens);
        return ((TokenNode)(new TokenNode().condeseNodes(0, pTokens))[1]).condense().removeExtraFuncs();
    }

    private static boolean checkForNullTokens(ArrayList<Token> pTokens){
        for(Token t : pTokens)
            if(t == null)
                return false;
        return true;
    }

    @Override
    public TokenNode setToken(Token pToken){
        assert pToken != null;
        token = pToken;
        return this;
    }

    public HashMap<String, Double> eval(final EquationSystem pEqSys){
        return eval(new HashMap<String, Double>(), pEqSys);
    }

    private HashMap<String, Double> appendHashMap(HashMap<String, Double> a, Object b){
        if(b != null)
        a.putAll((HashMap<String, Double>)b);
        return a;
    }
    private HashMap<String, Double> appendHashMap(HashMap<String, Double> a, String b, Double c){
        a.put(b,c);
        return a;
    }

    /**
     * TODO: JAVADOC
     * returns Double.NaN if the result isnt in bounds
     */
    public HashMap<String, Double> eval(HashMap<String, Double> pVars, final EquationSystem pEqSys){
        assert token != null;
        assert pEqSys != null : "Cannot evaluate a null EquationSystem!";

        if(token.type() == FUNC){
            if(pEqSys.functions().containsKey(token.val())) // if it is a function
                return pEqSys.functions().get(token.val()).exec(pEqSys, this);
            else
                return InBuiltFunction.exec(token.val(), pEqSys, this);
        } else if(isFinal()){
            String val = token.val();
            Double d;
            try{
                return appendHashMap(pVars, val, Double.parseDouble(val));
            } catch(NumberFormatException err){ 
                if(pEqSys.varExist(val))
                    for(Equation eq : pEqSys.equations()){
                        if(((TokenNode)eq.subEquations().getSD(eq.subEquations().depthS())).token.val().equals(val)){
                            pVars = appendHashMap(pVars,
                                                 eq.subEquations().getASD().get(1).eval(pVars, pEqSys));
                            appendHashMap(pVars, val, pVars.get(eq.subEquations().getASD().get(1).toString()));
                            return pVars;
                        }
                    }
                switch(val) {
                    case "e":
                        return appendHashMap(pVars, val, Math.E);
                    case "pi":
                        return appendHashMap(pVars, val, Math.PI);
                    case "NaN": case "nan":
                        return appendHashMap(pVars, val, Double.NaN);
                    case "rand": case "random":
                        return appendHashMap(pVars, val, Math.random()); 
                        // this might need work
                    default:
                        System.err.println(toFullString());
                        throw new UnsupportedOperationException("Cannot evaluate the FinalNode '" + val +
                                                      "' because it isn't defined as a variable," + 
                                                      " and isn't an in-built variable.");
                }
            }
        } else 
            throw new UnsupportedOperationException("This shouldn't happen! There is no way to evaluate node: " + token.val());
    }

    //here down are from EquationNode
    public <C extends Comparable<C>> boolean isInBounds(HashMap<String, Double> vars, String toEval){
        if(token.val().isEmpty()){
            assert size() == 1 : toFancyString();
            return get(0).isInBounds(vars, toEval);
        }
        assert size() == 2;
        Double d0, d1;
        d0 = get(0).eval((EquationSystem.fromHashMap(vars))).get(get(0).toString()); 
        d1 = get(1).eval((EquationSystem.fromHashMap(vars))).get(get(0).toString()); 
        return InBuiltFunction.FUNCTIONS.get(toString()).funcObj().exec(new Double[]{d0, d1}) == 1;
    }

    public String toExprString(){
        String ret = "";
        if(token instanceof Token){
            Token tok = (Token)token;
            ret += tok.val();
            if(tok.type() == Token.Type.FUNC){
                ret += "(";
                for(Node n : this){
                    ret += ((TokenNode)n).toExprString() + ", ";
                }
                ret = (size() == 0 ? ret : ret.substring(0, ret.length() - 2)) + ")";
            } else{
                assert size() == 0 : "I am not a function, but I have subnodes!";
            }
        } else {
            for(Node n : this){
                ret += ((TokenNode)n).toExprString();
                ret += " " + (token == null ? "" : token) + " ";
            }
            return ret.substring(0, ret.length() - 2);
        }
        return ret;
    }

    @Override
    public TokenNode copy(){
        assert elements != null;
        return (TokenNode)new TokenNode(token).setElements(elements);
    }

    @Override
    public String toString(){
        return toExprString().replaceAll("^\\((.*)\\)$", "$1");
    }

    public TokenNode getASD(){
        assert size() != 0 : "size == 0 for '" + this+"'";
        if(token.isAssign())
            return this;
        return get(0).getASD();
    }

    @Override
    public String toFancyString(){
        return super.toFancyString().replaceFirst("Node", "TokenNode");
    }
    @Override
    public String toFullString(){
        return super.toFullString().replaceFirst("Node", "TokenNode");
    }
}
