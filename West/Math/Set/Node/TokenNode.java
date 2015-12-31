package West.Math.Set.Node;
import West.Math.Set.Collection;
import java.util.ArrayList;
import West.Math.Equation.EquationSystem;
import West.Math.Equation.Function.InBuiltFunction;
import West.Math.Equation.Token;
import West.Math.MathObject;
import West.Math.Equation.Equation;
import West.Math.Exception.NotDefinedException;
import static West.Math.Equation.Token.Type.*;
/**
 * TODO: JAVADOC
 * 
 * @author Sam Westerman
 * @version 0.76
 * @since 0.75
 */
public class TokenNode extends Node<Token, TokenNode> implements MathObject {
    public TokenNode(){
        super();
        token = new Token("",FUNC);
    }

    public TokenNode(ArrayList<Token> pElements){
        super();
        add(generateMasterNode(pElements));
    }
    public TokenNode(TokenNode pCollection) {
        super(pCollection);
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
            if(t.isConst() || t.isOper()){
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
                TokenNode o;
                node.add(o = ((TokenNode)temp[1]).fixNodes());
            } 
            pPos++;
        }
        return new Object[]{pPos, node};
    }

    protected TokenNode completeNodes() {
        if(isFinal())
            return this;
        TokenNode node = copy();
        TokenNode e = new TokenNode(token);
        int i = 0;
        while(i < node.size()) {
            TokenNode n = (TokenNode)node.get(i);
            assert n != null : "no subNode can be null!";
            if(n.isFinal() && !Equation.isControlChar(n.token().val())) {
                e.addD(e.depth(), n);
            } else if(n.token.isOper()) {
                for(int depth = 1; depth < e.depth(); depth++) {
                    TokenNode nD = e.getD(depth);
                    assert nD != null : "elements cannot be null!";
                    if(nD.isFinal()) {
                        n.add(nD);
                        e.setD(depth - 1, -1, n); //depth is a final node.
                        break;
                    } else if(n.token.priority() < nD.token.priority()) {
                        n.add(nD);
                        n.add(((TokenNode)node.get(i + 1)).completeNodes());
                        i++;
                        e.setD(depth - 1, -1, n);
                        break;
                    } else if (nD.token.isFunc()) {
                        n.add(nD);
                        n.add(((TokenNode)node.get(i + 1)).completeNodes());
                        i++;
                        e.setD(depth - 1, -1, n);
                        break;
                    }
                }

            } else if(n.token.isFunc()) {
                e.addD(e.depth(), n.completeNodes());
            } else {
                throw new NotDefinedException("Cannot complete the node '" + n + "' because there is no known way to!");
            }
            i++;
        }
        return e;
    }

    private TokenNode fixNodes() {
        int i = 1;
        TokenNode node = copy();
        // while(i < node.size()){
        //     Node n = node.get(i);
        //     assert n != null  : "no subNode can be null!"; // this should have been caught beforehand.
        //     if(n.token.type() == OPER && n.token.val().equals("-") && node.get(i - 1).token.type() == OPER &&
        //                            (node.get(i - 1).token.val().equals("/") ||
        //                             node.get(i - 1).token.val().equals("*") ||
        //                             node.get(i - 1).token.val().equals("^"))) {
        //         Node n2 = new Node(new Token("", FUNC));
        //         n2.add(new FinalNode(new Token("0", NUM)));
        //         n2.add(n);
        //         n2.add(node.get(i + 1));
        //         node.rem(i + 1);
        //         node.set(i, n2);
        //         i++;
        //     }
        //     i++;
        // }
        return node;
    }

    public static TokenNode generateMasterNode(ArrayList<Token> pTokens) {
        assert checkForNullTokens(pTokens);
        return ((TokenNode)(new TokenNode().condeseNodes(0, pTokens))[1]).completeNodes().fixNodes();
        // return ((TokenNode)(new TokenNode(new Token("", FUNC)).condeseNodes(0, pTokens))[1]).completeNodes().fixNodes();
    }

    private static boolean checkForNullTokens(ArrayList<Token> pTokens){
        for(Token t : pTokens)
            if(t == null)
                return false;
        return true;
    }


    @Override
    public TokenNode copy(){
        assert elements != null;
        return (TokenNode)new TokenNode(token).addAllN(elements);
    }

    @Override
    public TokenNode setToken(Token pToken){
        assert pToken != null;
        token = pToken;
        return this;
    }

    @Override
    public TokenNode getD(int i) {
        return (TokenNode)super.getD(i);
    }
    @Override
    public String toString(){
        return "Token"+super.toString();
    }
    @Override
    protected void addD(int i, Node pN) {
        assert pN != null : "Cannot addDepth null Nodes!";
        assert pN instanceof TokenNode;
        TokenNode n = (TokenNode)pN;
        if(i <= 0 || size() <= 0 ||((TokenNode)get(size() - 1)).token().isGroup()) {
            add(n);
        } else {
            if(i == 2 && get(size() - 1).isFinal()) {
                add(n);
            } else {
                get(size() - 1).addD(i - 1, n);
            }
        }

    }

    @Override
    protected void setD(int i, int p, Node pN) {
        assert pN != null : "Cannot setDepth null Nodes!";
        assert pN instanceof TokenNode;
        TokenNode n = (TokenNode)pN;
        if(i == 0) {
            assert size() > 0;
            assert size() > p && (p >= 0 || p == -1);
            if(p == - 1) {
                setN(size() - 1,n);
            } else {
                setN(p, n);
            }
        } else {
            assert !get(size() - 1).isFinal(); //shouldnt happen, methinks.
            if(((TokenNode)get(size() - 1)).token.isGroup()) {
                setN(p, n);
            } else {
                get(size() - 1).setD(i - 1, p, n);
            }
        }
    }

    public Double eval(final EquationSystem pEqSys){
        assert token != null;
        assert pEqSys != null : "Cannot evaluate a null EquationSystem!";
        if(token.type() == FUNC || token.type() == OPER) {
            if(token.val().isEmpty()){
                // assert elements.size() == 1 : "\n" + toFancyString();
                return ((TokenNode)elements.get(0)).eval(pEqSys);
            }
            else if(pEqSys.functions().get(token.val()) != null){ // if it is a function
                return pEqSys.functions().get(token.val()).exec(pEqSys, this);
            } else {
                return InBuiltFunction.exec(token.val(), pEqSys, this);
            }
        } else if (token.isGroup()){
            return ((TokenNode)get(0)).eval(pEqSys);
        } else if(isFinal()){
            String val = token.val();
            try {
                return Double.parseDouble(val);
            } catch(NumberFormatException err) {
                if(pEqSys.varExist(val)){
                    for(Equation eq : pEqSys.equations()){
                        if(((TokenNode)eq.subEquations().getD(eq.subEquations().depth())).token.val().equals(val)){
                            double dVal = eq.subEquations().get(1).get(0).eval(pEqSys); //used to be get(1).eval
                            if(pEqSys.isInBounds(token, dVal)){
                                return dVal;
                            }
                            // Print.printe(dVal + " is out of bounds for '" + val + "'. returning NaN instead!");
                            return Double.NaN;
                        }
                    }
                }
                switch(val) {
                    case "e":
                        return Math.E;
                    case "pi":
                        return Math.PI;
                    case "rand": case "random":
                        return Math.random();
                    default:
                        throw new NotDefinedException("Cannot evaluate the FinalNode '" + val + "' because there it " + 
                            "defined as a variable, and isn't an in-built variable.");
                }
            }
        } else {
            throw new NotDefinedException("This shouldn't happen! There is no way to evaluate node: " + token.val());
        }

    }

}