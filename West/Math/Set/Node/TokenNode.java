package West.Math.Set.Node;
import West.Math.Set.Collection;
import java.util.ArrayList;
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
    // protected ArrayList<TokenNode> elements;
    // protected Token token;
    public TokenNode(){
        elements = new ArrayList<TokenNode>();
    }

    public TokenNode(ArrayList<Token> pElements){
        elements = new ArrayList<TokenNode>();
        elements.add(generateMasterNode(pElements));
    }
    public TokenNode(TokenNode pCollection) {
        elements = new ArrayList<TokenNode>();
        elements.add(pCollection);
    }
    public TokenNode(Token pToken) {
        elements = new ArrayList<TokenNode>();
        token = pToken;
    }

    protected Object[] condeseNodes(int pPos, ArrayList<Token> pTokens) {
        assert pTokens != null;
        assert checkForNullTokens(pTokens);
        TokenNode node = copy();
        while(pPos < pTokens.size()) {
            Token t = pTokens.get(pPos);
            assert t != null : "this should have been caught earlier.";
            if(t.isConst() || t.isOper()){
                node.addN(new TokenNode(t));
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
                node.addN(((TokenNode)temp[1]).fixNodes());
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
            System.out.println(node+"NODE");
            TokenNode n = node.getN(i);
            assert n != null : "no subNode can be null!";
            if(n.isFinal() && !Equation.isControlChar(n.token().val())) {
                e.addD(e.depth(), n);
            } else if(n.token.isOper()) {
                for(int depth = 1; depth < e.depth(); depth++) {
                    TokenNode nD = e.getD(depth);
                    assert nD != null : "elements cannot be null!";
                    if(nD.isFinal()) {
                        n.addN(nD);
                        e.setD(depth - 1, -1, n); //depth is a final node.
                        break;
                    } else if(n.token.priority() < nD.token.priority()) {
                        n.addN(nD);
                        n.addN(((TokenNode)node.getN(i + 1)).completeNodes());
                        i++;
                        e.setD(depth - 1, -1, n);
                        break;
                    } else if (nD.token.isFunc()) {
                        n.addN(nD);
                        n.addN(node.getN(i + 1).completeNodes());
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
        System.out.println("--------");
        Token baseToken = new Token("", FUNC);
        System.out.println("baseToken:"+baseToken);
        TokenNode baseNode = new TokenNode(baseToken);
        System.out.println("baseNode:"+baseNode);
        TokenNode condNode = (TokenNode)baseNode.condeseNodes(0,pTokens)[1];
        System.out.println("condNode:" + condNode);
        TokenNode complNode = condNode.completeNodes();
        System.out.println("complNode:" + complNode);
        TokenNode fixNode = complNode.fixNodes();
        System.out.println("fixNode:" + fixNode);


        return ((TokenNode)(new TokenNode().condeseNodes(0, pTokens))[1]).completeNodes().fixNodes();
        // return ((TokenNode)(new TokenNode(new Token("", FUNC)).condeseNodes(0, pTokens))[1]).completeNodes().fixNodes();
    }

    private static boolean checkForNullTokens(ArrayList<Token> pTokens){
        for(Token t : pTokens)
            if(t == null)
                return false;
        return true;
    }

    public TokenNode getN(int pos){ // add Node
        System.out.println("TOKENNODE GETN: " + super.get(pos));
        assert false;
        // return (TokenNode)super.get(pos);
        return null;
    }

    @Override
    public TokenNode copy(){
        assert elements != null;
        System.out.println("elements:"+elements);
        return (TokenNode)new TokenNode(token).addAllN(elements);
    }

    @Override
    public TokenNode setToken(Token pToken){
        assert pToken != null;
        token = pToken;
        return this;
    }

    @Override
    protected TokenNode getD(int i) {
        return (TokenNode)super.getD(i);
    }

    @Override
    protected void addD(int i, Node pN) {
        assert pN != null : "Cannot addDepth null Nodes!";
        assert pN instanceof TokenNode;
        TokenNode n = (TokenNode)pN;
        if(i <= 0 || size() <= 0 || getN(size() - 1).token.isGroup()) {
            addN(n);
        } else {
            if(i == 2 && getN(size() - 1).isFinal()) {
                addN(n);
            } else {
                getN(size() - 1).addD(i - 1, n);
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
            assert !getN(size() - 1).isFinal(); //shouldnt happen, methinks.
            if(getN(size() - 1).token.isGroup()) {
                setN(p, n);
            } else {
                getN(size() - 1).setD(i - 1, p, n);
            }
        }
    }

}