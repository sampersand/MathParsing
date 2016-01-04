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

    // public boolean isOper(){
    //     return token.isBinOper();
    // }

    private int priority(){
        if(token.isConst())
            return -2; // -1 is unknown priroity 
        assert token.isFunc();
        InBuiltFunction i = InBuiltFunction.get(token.val());
        return i == null ? -1 : i.priority();
    }
    private static int firstHighPriority(Collection<TokenNode> peles){
        int pos = 0, priority = -2;
        for(int i = 0; i < peles.size(); i++){
            if(peles.get(i).priority() > priority){
                priority = peles.get(i).priority();
                pos = i;
            }
        }
        return pos;
    }
    private static TokenNode condense(Collection<TokenNode> peles){
        // if(priority() == -2) // cannot condense a constant node.
            // return this;
        if(peles.size() == 0)
            return null;
        // assert peles.size() != 0: peles;
        if(peles.size() == 1)
            return peles.get(0);
        int fhp = firstHighPriority(peles);
        TokenNode u = peles.get(fhp); //new node has same token because condense is just reorganizing subnodes.
        System.out.println(peles + " | " + fhp + " <-fhp");
        System.out.println(peles.subList(0, fhp) + " | " + u + " | " + peles.subList(fhp + 1));
        TokenNode s = condense(new Collection.Builder<TokenNode>().addAll(peles.subList(0, fhp)).build());
        // System.out.println(fhp + " | " + peles.size() + " | " + peles);
        TokenNode e = condense(new Collection.Builder<TokenNode>().addAll(peles.subList(fhp + 1)).build());
        if(s != null)
            u.add(s);
        if(e != null)
            u.add(e);
        return u;
    }
    /* private TokenNode condense(){
        System.out.println(toFancyString());
        if(this.isFinal())
            return this;      
        TokenNode e = new TokenNode(token);
        int i = 0;
        while(i < size()){
            TokenNode n = get(i);
            if(n.token().isConst()){
                e.addD(n);
            } else if(n.isOper()){
                InBuiltFunction nibf = InBuiltFunction.get(n.token().val());
                assert nibf != null;
                System.out.println("n is an oper!");
                for(int depth = 1; depth < e.depth(); depth++){
                    TokenNode nD = e.getD(depth);
                    if(!nD.isOper()){
                        System.out.println(nD + " ! an oper");
                        n.add(nD);
                        e.setD(depth - 1, n); //depth is a final node.
                        break;
                    }
                    InBuiltFunction nDibf = InBuiltFunction.get(nD.token().val());
                    if(nDibf == null){
                        System.out.println("nDibf is null!");
                        continue;
                    }  
                    if(nibf.priority() <= nDibf.priority()){
                        System.out.println(n+"'s priority = "+nibf.priority()+", and "+nD+"'s = " + nDibf.priority());
                        n.add(nD);
                        n.add(get(i + 1).condense());
                        i++;
                        e.setD(depth - 1, n);
                        break;
                    }
                    // else if (nD.token().isGroup()){
                    //     n.add(nD);
                    //     n.add(get(i + 1).condense());
                    //     i++;
                    //     e.setD(depth - 1, n);
                    //     break;
                    // }
                }

            }
            // else if(n.token.isGroup()){
            //     e.addD(e.depth(), n.condense(), false);
            // }
            else{
                throw new NullPointerException("There is no known way to complete the node '" + n + "'.");
            }
            i++;
        }
        System.out.println("condensed:\n"+e.toFancyString());
        return e;
    } */

    @Override
    public void addD(int i, Node<?, ?> n) {
        assert n != null : "Cannot addDepth null Nodes!";
        System.out.println("addD in TokenNode might not work");
        if(i <= 0 || size() <= 0){
            addE(n);
        } else {
            if(i == 2 && get(-1).token().isConst()) {
                System.out.println("im here in adDD TokenNode!\n"+get(-1).toFancyString());
                addE(n);
            } else {
                System.out.println(i + " | " + get(-1) + " | " + get(-1).token().isConst());
                System.out.println("going down a level:\n"+toFancyString()+"\nto add:\n"+n.toFancyString()+"\n\n\n");
                get(size() - 1).addD(i - 1, n);
            }
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
        System.out.println(pTokens);
        TokenNode tn = (TokenNode)new TokenNode().condeseNodes(0, pTokens)[1];
        TokenNode tn2 = condense(new Collection.Builder<TokenNode>().addAll(tn.elements()).build());
        return tn2.removeExtraFuncs();
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
            ret += token.val();
            if(tok.isFunc()){
                ret += "(";
                for(Node n : this){
                    ret += ((TokenNode)n).toExprString() + ", ";
                }
                ret = (size() == 0 ? ret : ret.substring(0, ret.length() - 2)) + ")";
            } else{
                assert size() == 0 : "I am not a function, but I have subnodes!";
            }
        } else {
            assert false;
            // for(Node n : this){
            //     ret += ((TokenNode)n).toExprString();
            //     ret += " " + (token == null ? "" : token) + " ";
            // }
            // return ret.substring(0, ret.length() - 2);
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
