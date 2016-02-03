package West.Math.Set.Node;
import West.Math.Set.Collection;
import java.util.AbstractList;
import West.Math.Equation.EquationSystem;
import West.Math.Equation.Function.Function;
import West.Math.Equation.Token;
import West.Math.MathObject;
import West.Math.Equation.Equation;
import java.util.HashMap;
import West.Math.DoubleSupplier;
import West.Math.Complex;

/**
* TODO: JAVADOC
* 
* @author Sam Westerman
* @version 0.85
* @since 0.75
*/
public class TokenNode extends Node<Token, TokenNode> implements MathObject {

    protected String[] parens;

    public TokenNode(){
        this(new Token("",Token.Type.FUNC));
    }

    public TokenNode(Token pToken) {
        this(pToken, new String[]{"", ""});
    }

    public TokenNode(Token pToken, String[] pParens) {
        super(pToken);
        parens = pParens;
    }

    public TokenNode(TokenNode pTokenNode) {
        this(pTokenNode.token);
        parens = pTokenNode.parens;
    }

    protected Object[] condeseNodes(AbstractList<Token> pTokens) {
        int pos = 0;
        TokenNode node = clone();
        while(pos < pTokens.size()) {
            Token t = pTokens.get(pos);
            if(t.isConst() || t.isBinOper() || t.isUNL() || t.isUNR())
                node.add(new TokenNode(t));
            else if(t.isFunc() || t.isDelim()) {

                Collection<Token> passTokens = new Collection<Token>();
                String[] toAddParens = new String[]{"",""};
                int x = pos + 1;
                int paren = 0;
                do{
                    if(Token.PAREN_L.contains(pTokens.get(x).val())) paren++;
                    if(Token.PAREN_R.contains(pTokens.get(x).val())) paren--;
                    if(t.isDelim() && Token.DELIM.contains(pTokens.get(x).val()) && paren == 0) break;
                    x++;
                } while((t.isDelim() ? 0 <= paren : 0 < paren) && x < pTokens.size());
                for(Token tk : pTokens.subList(pos + 1, x))
                    passTokens.add(tk);
                if(t.isFunc() && passTokens.get(passTokens.size()-1).isParen() && passTokens.get(0).isParen()){
                    toAddParens[1] = pTokens.get(pos+++passTokens.size()).val();
                    toAddParens[0] = pTokens.get(pos).val();
                    passTokens.remove(0);
                    passTokens.remove(passTokens.size()-1);
                    assert Token.PAREN_L.contains(toAddParens[0]) &&
                           Token.PAREN_R.contains(toAddParens[1]) : toAddParens[0]+" "+toAddParens[1];
                    passTokens.add(0, new Token("", Token.Type.DELIM));
                }

                Object[] temp = new TokenNode(t).condeseNodes(passTokens);
                pos += (int)temp[0];// (x==pTokens.size()-1?1:0);
                ((TokenNode)temp[1]).parens = toAddParens;
                node.add((TokenNode)temp[1]);
            } else if(t.isParen()){
                if(Token.isParenL(t.val()) != null){
                    assert node.parens[0].isEmpty() : "Uh oh! adding '"+t+"' to\n" + node.toFancyString();
                    node.parens[0] = t.val();
                } else{
                    assert node.parens[1].isEmpty() && !node.parens[0].isEmpty() : t+"\n"+toFancyString();
                    node.parens[1] = t.val();
                }
            }
            pos++;
        }
        return new Object[]{pos, node.removeExtraFuncs()};
    }


    private int priority(){
        if(token.isConst())
            return Function.DEFAULT_PRIORITY + 1;
        if(token.isDelim())
            return -1;
        assert token.isFunc();
        Function i = Function.get(token.val());
        return i == null ? Function.DEFAULT_PRIORITY : i.priority();
    }
    private static int firstHighPriority(Collection<TokenNode> peles){
        int pos = 0, priority = Function.DEFAULT_PRIORITY;
        for(int i = 0; i < peles.size(); i++){
            if(peles.get(i).priority() <= priority){ // i swapped them
                priority = peles.get(i).priority();
                pos = i;
            }
        }
        return priority == -1 ? -1 : pos;
    }
    private static TokenNode condense(final Collection<TokenNode> peles){
        if(peles.size() == 0)
            return null;
        if(peles.size() == 1)
            if(peles.get(0).size() == 0)
                return peles.get(0);
            else
                return new TokenNode(peles.get(0)){{
                    add(condense(new Collection<TokenNode>().addAllE(peles.get(0).elements)));
                }};

        int fhp = firstHighPriority(peles);
        if(fhp == -1)
            return new TokenNode(new Token()){{
                peles.forEach(e -> add(condense(new Collection<TokenNode>().addE(e))));
            }};
        TokenNode u = condense(new Collection<TokenNode>().addE(peles.get(fhp)));
        TokenNode s = condense(new Collection<TokenNode>().addAllE(peles.subList(0, fhp)));
        TokenNode e = condense(new Collection<TokenNode>().addAllE(peles.subList(fhp + 1)));
        if(s != null)
            u.add(s);
        if(e != null)
            u.add(e);
        return u;
    }


    public TokenNode removeExtraFuncs(){
        if(isFinal())
            return this;
        TokenNode ret = new TokenNode(this);
        for(Node<?, ?> n : elements){
            TokenNode tn = (TokenNode)n;
            if(tn.size() == 1 && tn.get(0).token.isGroup())
                ret.add(new TokenNode(tn){{
                    tn.get(0).forEach(e -> this.add(((TokenNode)e).removeExtraFuncs()));
                }});
            else
                ret.add(tn.removeExtraFuncs());
        }
        return ret;
    }

    public boolean add(TokenNode tn){
        assert tn != null : "attempting to add null to '"+toFullString()+"'";
        return super.add(tn);
    }
    public static TokenNode generateMasterNode(AbstractList<Token> pTokens) {
        TokenNode tn = (TokenNode)new TokenNode().condeseNodes(pTokens)[1]; // just to make it easier to read.
        tn = condense(new Collection<TokenNode>().addAllE(tn.elements));
        return tn.removeExtraFuncs();
    }

    @Override
    public void addD(int i, Node<?, ?> n) {
        assert n != null : "Cannot addDepth null Nodes!";
        if(i <= 0 || size() <= 0){
            add(n);
        } else {
            get(size() - 1).addD(i - 1, n);
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

    @Override
    public TokenNode get(int pPos){
        return (TokenNode)super.get(pPos);
    }

    
    public String[] parens(){
        return parens;
    }

    @Override
    public TokenNode setToken(Token pToken){
        assert pToken != null;
        token = pToken;
        return this;
    }

    private static HashMap<String, DoubleSupplier> appendHashMap(HashMap<String, DoubleSupplier> a, Object b){
        if(b != null)
            a.putAll((HashMap<String, DoubleSupplier>)b);
        return a;
    }

    private static HashMap<String, DoubleSupplier> appendHashMap(HashMap<String, DoubleSupplier> a,
                                                          String b,
                                                          DoubleSupplier c){
        a.put(b, c);
        return a;
    }
    private static HashMap<String, DoubleSupplier> appendHashMap(HashMap<String, DoubleSupplier> a, String b, Double c){
        a.put(b, new Complex(c));
        return a;
    }


    public TokenNode findExpr(String val){
        if(toExprString().equals(val))
            return this;
        if(true)return this;
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
    public HashMap<String, DoubleSupplier> eval(HashMap<String, DoubleSupplier> pVars, final EquationSystem pEqSys){
        assert pEqSys != null : "Cannot evaluate a null EquationSystem!";
        String val = toExprString();
        if(pVars.containsKey(val)) // if the value is already in pVars, then just take a shortcut.
            return pVars;
        for(Equation eq : pEqSys.equations()){ // if the current val is on the left hand side, then use that
            if(((TokenNode)eq.subEquations().getSD(1).get(0)).toExprString().equals(val)){
                TokenNode tkn = (((TokenNode)eq.subEquations().getSD(1).get(-1)));
                pVars = appendHashMap(pVars, tkn.eval(pVars, pEqSys));
                appendHashMap(pVars,val, pVars.get(tkn.toString()));
                return pVars;
            }
        }
        if(token.isFunc() || token.isDelim()){
            if(pEqSys.functions().containsKey(token.val())) //  if it is a function
                return pEqSys.functions().get(token.val()).exec(pVars, pEqSys, this); // no work
            return Function.exec(pVars, token.val(), pEqSys, this); // no else needed
        } else if(isFinal()){
            return getVarInFinal(pVars, token.val());
        } else
            throw new UnsupportedOperationException("This shouldn't happen! There is no way to evaluate node: " +
                                                    token.val());
    }

    public DoubleSupplier evald(){
        return evald(new HashMap<String, DoubleSupplier>(), new EquationSystem());
    }
    
    public DoubleSupplier evald(HashMap<String, DoubleSupplier> hm, final EquationSystem pEqSys){
        return eval(hm, pEqSys).get(toString());
    }

    public static HashMap<String, DoubleSupplier> getVarInFinal(HashMap<String, DoubleSupplier> pVars, String val){
        switch(val) {
            case "e":
                return appendHashMap(pVars, val, Math.E);

            case "k": // Coloumn's constant
                return appendHashMap(pVars, val, 8.9875517873681764E9);

            case "pi": case "π": case "Π":
                return appendHashMap(pVars, val, Math.PI);

            case "i": case "j":
                return appendHashMap(pVars, val, new Complex(0d, 1d));

            case "NAN": case "nan":
                return appendHashMap(pVars, val, Double.NaN);

            case "inf": case "∞":
                return appendHashMap(pVars, val, Complex.INF_P);

            case "rand": case "random":
                return appendHashMap(pVars, val, Math.random()); 

            //  X / 2
            case "½": return appendHashMap(pVars, val, 1/2d);

            //  X / 3
            case "⅓": return appendHashMap(pVars, val, 1/3d);
            case "⅔": return appendHashMap(pVars, val, 2/3d);

            //  X / 4
            case "¼": return appendHashMap(pVars, val, 1/4d);
            case "¾": return appendHashMap(pVars, val, 3/4d);
            
            //  X / 5
            case "⅕": return appendHashMap(pVars, val, 1/5d);
            case "⅖": return appendHashMap(pVars, val, 2/5d);
            case "⅗": return appendHashMap(pVars, val, 3/5d);
            case "⅘": return appendHashMap(pVars, val, 4/5d);

            //  X / 6
            case "⅙": return appendHashMap(pVars, val, 1/6d);
            case "⅚": return appendHashMap(pVars, val, 5/6d);

            //  X / 7
            case "⅐": return appendHashMap(pVars, val, 1/7d);

            //  X / 8
            case "⅛": return appendHashMap(pVars, val, 1/8d);
            case "⅜": return appendHashMap(pVars, val, 3/8d);
            case "⅝": return appendHashMap(pVars, val, 5/8d);
            case "⅞": return appendHashMap(pVars, val, 7/8d);

            // X / 9
            case "⅑": return appendHashMap(pVars, val, 1/9d);

            // X / 10
            case "⅒": return appendHashMap(pVars, val, 1/10d);


            default:
                try{
                    return appendHashMap(pVars, val, new Complex(Double.parseDouble(val)));
                } catch(NumberFormatException err){ 
                    try{
                        return appendHashMap(pVars, val, new Complex(val));
                    } catch (NumberFormatException err2){
                        // System.err.println("Variable '" + val +"' doesn't exist in '"+pVars+"'; returning NAN instead");
                        return appendHashMap(pVars, val, Double.NaN);
                    }
                }
        }
    }

    public String toExprString(){
        String ret = "";
        if(size() == 0){
            assert token.isConst() ? isFinal() : true;
            return token.val();
        }
        if((token.isFunc() && !token.isBinOper()) || token.isDelim()){
            ret += token.val() + parens[0];
            for(Node n : this)
                ret += ((TokenNode)n).toExprString()+" ";
            return (ret.length() > 0 ? ret.substring(0, ret.length() - 1) : ret) + parens[1];
        } else if(token.isBinOper()){
            if(size() == 1) //  TODO: FIX THIS
                ret += token.val() + " " + ((TokenNode)get(0)).toExprString();
            else{
                for(Node n : this){
                    ret += ((TokenNode)n).toExprString();
                    ret += " " + (token == null ? "" : token.val()) + " ";
                }
                return ret.substring(0, ret.length() >=3 ? ret.length() - 3 : ret.length());
            }
        }
        else if(token.isConst())
            return token.val();
        assert false : token+":"+elements;
        return null;
    }

    @Override
    public TokenNode clone(){
        assert elements != null;
        return (TokenNode)new TokenNode(this).setElements(elements);
    }

    @Override
    public String toString(){
        return toExprString().replaceAll("^\\((.*)\\)$", "$1");
    }

    @Override
    public String toFancyString(){
        return super.toFancyString().replaceFirst("(?<!Token)Node(?!s)", "TokenNode ('"+parens[0]+"', '"+parens[1]+"')");
    }

    @Override
    public String toFancyString(int i){
        return super.toFancyString(i).replaceFirst("(?<!Token)Node", "TokenNode ('"+parens[0]+"', '"+parens[1]+"')");
    }


    @Override
    public String toFullString(){
        return super.toFancyString().replaceFirst("(?<!Token)Node", "TokenNode ('"+parens[0]+"', '"+parens[1]+"')");
    }

    @Override
    public String toFullString(int i){
        return super.toFancyString(i).replaceFirst("(?<!Token)Node", "TokenNode ('"+parens[0]+"', '"+parens[1]+"')");
    }
}
