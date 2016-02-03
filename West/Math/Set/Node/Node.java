package West.Math.Set.Node;
import West.Math.MathObject;
import West.Math.Set.Collection;
import West.Math.Equation.Token;
import java.util.List;
/**
 * TODO: JAVADOC
 * LOL i'm going to have to figure out how to spell 'token' correctly XD
 * There is a reason this isn't merge with {@link TokenNode}. It's incase I want to use this later on in another project
 * 
 * @author Sam Westerman
 * @version 1.1
 * @since 0.75
 */ 

public class Node<T, N extends Node> extends Collection<Node<?, ?>> implements MathObject {

    protected T token; //  its an object so functions can also use this

    public Node(){
        super();
        token = null;
    }

    public Node(List<Node<?, ?>> pElements){
        super();
        addAll(pElements);
        token = null;
    }

    public Node(T pToken) {
        super();
        setToken(pToken);
    }

    public T token(){
        return token;
    }

    public Node<T, N> setToken(T pToken){
        token = pToken;
        return this;
    }

    public boolean isFinal(){
        return size() == 0;
    }

    public boolean isLone(){
        return isFinal() || (size() == 1 ? get(0).isLone() : false);
    }

    public int depth() {
        return size() == 0 ? 1 : 1 + get(-1).depth();
    }
    public int depthS() {
        return size() == 0 ? 1 : 1 + get(0).depthS();
    }

    @Deprecated
    public Node getD(int i) {
        if(i <= 0 || size() == 0) {
            return this;
        } else {
            return get(-1).getD(i - 1);
        }
    }

    public Node getSD(int i) {
        if(i <= 0 || size() == 0) {
            return this;
        } else {
            return get(0).getSD(i - 1);
        }
    }

    @Deprecated
    public void addD(int i, Node<?, ?> n) {
        assert n != null : "Cannot addDepth null Nodes!";
        if(i <= 0 || size() <= 0)
            addE(n);
        else 
            get(size() - 1).addD(i - 1, n);
    }

    @Deprecated
    public void setD(int i, int p, Node<?, ?> n) {
        assert n != null : "Cannot setDepth null Nodes!";
        if(i == 0) {
            assert size() > 0;
            assert size() > p && (p >= 0 || p == -1);
            set(p, n);
        } else {
            assert !get(-1).isFinal(); // shouldnt happen, methinks.
            get(-1).setD(i - 1, p, n);
        }
    }

    @Override
    public boolean equals(Object pObj){
        return super.equals(pObj) &&
               pObj instanceof Node &&
               token.equals(((Node<T, N>)pObj).token());
    }
    
    @Override
    public String toString(){
        return "Node '" + token + "' elements = " + elements;
    }

    @Override
    public Node clone(){
        return new Node<T, N>(elements).setToken(token);
    }

    @Override
    public String toFancyString(int idtLvl) {
        String ret = indent(idtLvl) + "Node '" + token + "':\n";
        ret += indent(idtLvl + 1) + "Sub Nodes:";
        for(Node<?, ?> node : elements) 
            ret += "\n" + node.toFancyString(idtLvl + 2);
        return ret + "\n" +  indentE(idtLvl + 2) + "\n" + indentE(idtLvl + 1);
    }

    @Override
    public String toFullString(int idtLvl) {
        String ret = indent(idtLvl) + "Node:\n";
        ret += indent(idtLvl + 1) + "Token:\n";
        ret += token instanceof MathObject ? ((MathObject)token).toFullString(idtLvl + 2) :  indent(idtLvl + 2) + token;
        ret += "\n";
        ret += indent(idtLvl + 1) + "Sub Nodes:";
        for(Node node : elements) 
            ret += "\n" + node.toFullString(idtLvl + 2);
        ret += "\n" + indentE(idtLvl + 2);
        return ret + "\n" + indentE(idtLvl + 1);
    }

}
