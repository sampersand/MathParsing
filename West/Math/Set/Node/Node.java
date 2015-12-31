package West.Math.Set.Node;
import West.Math.Exception.NotDefinedException;
import West.Math.MathObject;
import West.Math.Set.Collection;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * TODO: JAVADOC
 * LOL i'm going to have to figure out how to spell 'token' correctly XD
 * 
 * @author Sam Westerman
 * @version 0.76
 * @since 0.75
 */ 

public class Node<T, N extends Node> extends Collection<Node<?, ?>> implements MathObject {

    protected T token; // its an object so functions can also use this

    public Node(){
        super();
        token = null;
    }

    public Node(ArrayList<Node<?, ?>> pElements){
        super();
        addAll(pElements);
        token = null;
    }


    public Node(Node<?, ?> pCollection) {
        super();
        add(pCollection);
        token = null;
    }

    public Node(T pToken) {
        super();
        token = pToken;
    }
    public T token(){
        return token;
    }


    public Node<T, N> addN(N pN){ // add Node
        super.add(pN);
        // new Node<Object, N>(){{
        //     add(pN);
        // }});
        return this;
    }

    public Node<T, N> getN(int pos){ // get Node
        return (Node<T, N>)super.get(pos);
    }

    public void setN(int pos, N pN){ // set Node
        super.set(pos, pN);
    }

    public Node<T, N> addAllN(ArrayList<Node<?, ? extends Node>> pNs){ // add Node
        assert pNs != null;
        if(pNs.size() == 0)
            return this;
        for(Node n : pNs)
            addE(n);
        return this;
    }
    public Node<T, N> setToken(T pToken){
        token = pToken;
        return this;
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

    public Node copy(){
        return new Node<T, N>(elements).setToken(token);
    }

    public boolean isFinal(){
        return size() == 0;
    }

    public int depth() {
        return size() == 0 ? 1 : 1 + get(size() - 1).depth();
    }

    protected Node getD(int i) {
        if(i <= 0 || size() == 0) {
            return this;
        } else {
            return get(size() - 1).getD(i - 1);
        }
    }

    protected void addD(int i, Node<?, ?> n) {
        assert n != null : "Cannot addDepth null Nodes!";
        if(i <= 0 || size() <= 0){
            addE(n);
        } else {
            if(i == 2 && get(size() - 1).isFinal()) {
                addE(n);
            } else {
                getN(size() - 1).addD(i - 1, n);
            }
        }
    }

    protected void setD(int i, int p, Node<?, ?> n) {
        assert n != null : "Cannot setDepth null Nodes!";
        if(i == 0) {
            assert size() > 0;
            assert size() > p && (p >= 0 || p == -1);
            if(p == - 1) {
                set(size() - 1, n);
            } else {
                set(p, n);
            }
        } else {
            assert !get(size() - 1).isFinal(); //shouldnt happen, methinks.
            get(size() - 1).setD(i - 1, p, n);
        }
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
        ret += token instanceof MathObject ? ((MathObject)token).toFullString(idtLvl + 2) :  indent(idtLvl) + token;
        ret += "\n";
        ret += indent(idtLvl + 1) + "Sub Nodes:";
        for(Node node : elements) 
            ret += "\n" + node.toFullString(idtLvl + 2);
        ret += "\n" + indentE(idtLvl + 2);
        return ret + "\n" + indentE(idtLvl + 1);
    }
}
