package West.Math.Set.Node;
import West.Math.Set.Collection;
import java.util.ArrayList;
import West.Math.Equation.EquationSystem;
import West.Math.Equation.Function.Function;
import West.Math.Equation.Token;
import West.Math.MathObject;
import West.Math.Equation.Equation;
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
        token = new Token("",Token.Type.FUNC);
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


    private int priority(){
        if(token.isConst())
            return Function.DEFAULT_PRIORITY + 1;
        assert token.isFunc();
        Function i = Function.get(token.val());
        return i == null ? Function.DEFAULT_PRIORITY : i.priority();
    }
    private static int firstHighPriority(Collection<TokenNode> peles){
        int pos = 0, priority = Function.DEFAULT_PRIORITY;
        for(int i = 0; i < peles.size(); i++){
            if(peles.get(i).priority() <=priority){ //i swapped them
                priority = peles.get(i).priority();
                pos = i;
            }
        }
        return pos;
    }
    private static TokenNode condense(Collection<TokenNode> peles){
        if(peles.size() == 0)
            return null;
        if(peles.size() == 1)
            if(peles.get(0).size() == 0)
                return peles.get(0);
            else
                return new TokenNode(peles.get(0).token()){{
                    add(condense(new Collection.Builder<TokenNode>().addAll(peles.get(0).elements()).build()));
                }};
        int fhp = firstHighPriority(peles);
        TokenNode u = condense(new Collection.Builder<TokenNode>().add(peles.get(fhp)).build());
        TokenNode s = condense(new Collection.Builder<TokenNode>().addAll(peles.subList(0, fhp)).build());
        TokenNode e = condense(new Collection.Builder<TokenNode>().addAll(peles.subList(fhp + 1)).build());
        if(s != null)
            u.add(s);
        if(e != null)
            u.add(e);
        return u;
    }

    @Override
    public void addD(int i, Node<?, ?> n) {
        assert n != null : "Cannot addDepth null Nodes!";
        if(i <= 0 || size() <= 0){
            addE(n);
        } else {
            // if(i == 2 && get(-1).token().isConst()) {
            //     addE(n);
            // } else {
                get(size() - 1).addD(i - 1, n);
            // }
        }
    }

    private void addD(TokenNode n){
        super.addD(depth(), n);
    }

    public TokenNode getD(int depth){
        return (TokenNode) super.getD(depth);
    }

    private void setD(int depth, TokenNode n){
        super.setD(depth, -1, n);
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
        TokenNode tn = (TokenNode)new TokenNode().condeseNodes(0, pTokens)[1]; //just to make it easier to read.
        if(Function.USING_BIN_OPERS)
            tn = condense(new Collection.Builder<TokenNode>().addAll(tn.elements()).build());
        return tn.removeExtraFuncs();
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

    public TokenNode findExpr(String val){
        if(toExprString().equals(val))
            return this;
        assert size() > 0;
        for(int i = 0; i < size(); i++){
            TokenNode n = get(i);
            if(n != null)
                if(n.toExprString().equals(val))
                    return this;
                else
                    return n;
        }
        return null;
    }
    /**
     * TODO: JAVADOC
     * returns Double.NaN if the result isnt in bounds
     */
    public HashMap<String, Double> eval(HashMap<String, Double> pVars, final EquationSystem pEqSys){
        assert token != null;
        assert pEqSys != null : "Cannot evaluate a null EquationSystem!";
        String val = toExprString();
        if(pVars.containsKey(val)) //if the value is already in pVars, then just take a shortcut.
            return pVars;
        for(Equation eq : pEqSys.equations()) //if the current val is on the left hand side, then use that
            if(((TokenNode)eq.subEquations().getSD(0).get(0)).toExprString().equals(val)){
                TokenNode tkn = eq.subEquations().findExpr(val).get(1);
                pVars = appendHashMap(pVars, tkn.eval(pVars, pEqSys));
                appendHashMap(pVars,val, pVars.get(tkn.toString()));
                return pVars;
            }
        if(token.isFunc()){
            // System.out.println(pEqSys.functions()+"@");
            if(pEqSys.functions().containsKey(token.val())) // if it is a function
                return pEqSys.functions().get(token.val()).exec(pVars, pEqSys, this); //no work
            return Function.exec(pVars, token.val(), pEqSys, this); //no else needed
        } else if(isFinal()){
            val = token.val();
            Double d;
            try{
                return appendHashMap(pVars, val, Double.parseDouble(val));
            } catch(NumberFormatException err){ 
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
                        System.err.println(val + " doesn't exist, but returning NaN anyways");
                        return appendHashMap(pVars, val, Double.NaN);
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
        return Function.get(toString()).funcObj().exec(new Double[]{d0, d1}) == 1;
    }

    public String toExprString(){
        String ret = "";
        if(token.isFunc() && !token.isBinOper()){
            ret += token.val() + "(";
            for(Node n : this)
                ret += ((TokenNode)n).toExprString() + ", ";
            ret = (size() == 0 ? ret : ret.substring(0, ret.length() - 2)) + ")";
        } else if(token.isBinOper()){
            if(size() == 1) // TODO: FIX THIS
                ret += token.val() + " " + ((TokenNode)get(0)).toExprString();
            else{
                for(Node n : this){
                    ret += ((TokenNode)n).toExprString();
                    ret += " " + (token == null ? "" : token.val()) + " ";
                }
                ret = ret.substring(0, ret.length() - 2);
            }
        }
        else if(token.isConst()){
            ret += token.val();
        }
        else
            assert false;
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

    @Override
    public String toFancyString(){
        return super.toFancyString().replaceFirst("Node", "TokenNode");
    }
    @Override
    public String toFullString(){
        return super.toFullString().replaceFirst("Node", "TokenNode");
    }
}
