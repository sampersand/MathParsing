package West.Math.Equation;

import West.Math.MathObject;
import static West.Math.Declare.*;
import West.Math.Print;
import West.Math.Equation.Equation;
import West.Math.Equation.EquationSystem;
import West.Math.Equation.Function.InBuiltFunction;
import static West.Math.Equation.Token.Type.*;
import West.Math.Exception.NotDefinedException;
import West.Math.Set.Collection;
import java.util.ArrayList;

/**
 * A class that represents either a {@link CustomFunction custom function}, an {@link InBuiltFunction inbuilt function}, 
 * an {@link OperationFunction operation}, or a group of tokens.
 * @author Sam Westerman
 * @version 0.75
 * @since 0.1
 */
public class Node implements MathObject {

    /** A list of all nodes that are benith this one in the hierarchical structure. */
    protected Collection<Node> subNodes;

    /** The token that this class determins how this class interacts with its {@link #subNodes}. */
    protected Token token;

    /**
     * The default constructor for Node. Just passes an empty Token to the {@link #Node(Token) main Node constructor}.
     */
    public Node() {
        this(new Token());
    }

    /**
     * A constructor for Node, which only takes a Node as an input. Just passes pNode's token and an empty list to the
     * {@link #Node(Token,ArrayList) main constructor} constructor.
     * @param pNode        The Node whose token will determine how this class interacts with its {@link #subNodes}.
     *                     Cannot be null.
     * @throws IllegalArgumentException Thrown when pNode is null.
     */
    public Node(Node pNode) throws IllegalArgumentException {
        this(pNode.token, new Collection<Node>());
    }

    /**
     * A constructor for Node, which only takes a token as an input. Just passes an empty list and pToken to
     * {@link #Node(Token,ArrayList) main} constructor.
     * @param pToken        The token that will determine how this class interacts with its {@link #subNodes}. Cannot be
     *                      null.
     * @throws IllegalArgumentException Thrown when pToken is null.
     */
    public Node(Token pToken) throws IllegalArgumentException{
        this(pToken, new Collection<Node>());
    }

    /**
     * The main constructor for Node. 
     * @param pToken        The token that will determine how this class interacts with its {@link #subNodes}. Cannot be
     *                      null.
     * @param pSubNodes     The list of subNodes this class will have. Cannot be null.
     * @throws IllegalArgumentException Thrown when pToken or pSubNodes is null.
     */
    public Node(Token pToken,
                Collection<Node> pSubNodes) throws IllegalArgumentException{
        declP(pToken != null, "Cannot instatiate a Node with a null token! Pass an empty Token instead.");
        declP(pSubNodes != null, "Cannot instatiate a Node with null subNodes! Pass an empty ArrayList instead.");
        subNodes = pSubNodes;
        token = pToken;
    }

    /**
     * An alternate constructor for Node. This one utilizes varargs. 
     * To instantiate using this constructor, you would do somehting like:
     * <code>Node n = new Node(Token, Node1, Node2, ... , NodeN</code>.
     * @param pToken        The token that will determine how this class interacts with its {@link #subNodes}.
     * @param pSubNodes     A of subNodes this class will have.
     * @deprecated As of v0.68; Just pass an ArrayList to {@link #Node(Token,ArrayList)}.
     */
    @Deprecated
    public Node(Token pToken,
                Node... pSubNodes) {
        assert pToken != null; // if no token, then pass an empty token
        assert pSubNodes != null; // shouldnt be calling this if they are null
        subNodes = new Collection<Node>() {{
            for(Node n : pSubNodes)
                add(n);
            }};
        token = pToken;
    }

    /**
     * Returns this class's {@link #subNodes}. Note that this should never be called for {@link FinalNode}s.
     * @return this class's {@link #subNodes}.
     */
    public Collection<Node> subNodes() {
        return subNodes;
    }

    /**
     * Returns this class's {@link #token}.
     * @return this class's {@link #token}.
     */
    public Token token() {
        return token;
    }

    /**
     * Condenses functions and groups together into a single node. Creates 1-length nodes for operations and vars / nums.
     * Note that this is only ever used with {@link #fixNodes(Node) fixNodes}, 
     * {@link #generateNodes(ArrayList) generateNodes} and {@link #completeNodes(Node) completeNodes}.
     * @param pPos      The position to start condensing nodes form.
     * @param pNode     The "parent" node that any newly generated nodes will be put into.
     * @param pTokens   The list of tokens that will be put into nodes. Cannot be null.
     * @return The return type might seem odd, however, it always returns an updated pPos as arg 1, and the new node
     *         to add as argument 2.
     */
    private Object[] condeseNodes(int pPos,
                                  ArrayList<Token> pTokens) {
        declP(pTokens != null, "Cannot condense a null ArrayList of Tokens! Try an empty ArrayList instead.");
        declND(!(this instanceof FinalNode), "Cannot condense a FinalNode, as it has no subNodes!");
        declP(checkForNullTokens(pTokens), "Cannot condeseNodes with null Tokens in pTokens!");
        Node node = copy();
        while(pPos < pTokens.size()) {
            Token t = pTokens.get(pPos);
            assert t != null : "this should have been caught earlier.";
            if(t.isConst()){
                node.add(new FinalNode(t));
            } else if(t.isOper()){
                node.add(new Node(t));
            } else if(t.isGroup()) {
                int paren = 0;
                int x = pPos + 1;

                do{
                    if(pTokens.get(x).type() == LPAR) paren++;
                    else if(pTokens.get(x).type() == RPAR) paren--;
                    x++;
                } while(0 < paren && x < pTokens.size());

                ArrayList<Token> passTokens = new ArrayList<Token>();

                for(Token tk : pTokens.subList(pPos + 1, x )){
                     passTokens.add(tk);
                 }

                Object[] temp = new Node(t).condeseNodes(0, passTokens);
                pPos += (int)temp[0];
                node.add(((Node)temp[1]).fixNodes());
            } 
            pPos++;
        }
        return new Object[]{pPos, node};
    }


    /**
     * Creates a master node using the pNode as a starting point by applying the Order of Operations. If
     * <code>this instanceof FinalNode</code>, it will return this.
     * Note: pNode should already have been condensed via {@link #condeseNodes(int,Node,ArrayList) condeseNodes}, and
     * fixed via {@link #fixNodes(Node) fixNodes}.
     * @return The "master" node - that is, the node to control all other nodes. HAH - LOTR reference.
     */
    private Node completeNodes() {
        if(this instanceof FinalNode)
            return this;
        assert subNodes != null : "Cannot complete nodes when subNodes is null!"; //should never happen
        Node node = copy();
        Node e = new Node(token);
        int i = 0;
        while(i < node.size()) {
            Node n = node.get(i);
            assert n != null : "no subNode can be null!";
            if(n instanceof FinalNode) {
                e.addD(e.depth(), n);
            } else if(n.token.isOper()) {
                for(int depth = 1; depth < e.depth(); depth++) {
                    Node nD = e.getD(depth);
                    assert nD != null : "subNodes cannot be null!";
                    if(nD instanceof FinalNode) {
                        n.add(nD);
                        e.setD(depth - 1, -1, n); //depth is a final node.
                        break;
                    } else if(n.token.priority() < nD.token.priority()) {
                        n.add(nD);
                        n.add(node.get(i + 1).completeNodes());
                        i++;
                        e.setD(depth - 1, -1, n);
                        break;
                    } else if (nD.token.isGroup()) {
                        n.add(nD);
                        n.add(node.get(i + 1).completeNodes());
                        i++;
                        e.setD(depth - 1, -1, n);
                        break;
                    }
                }

            } else if(n.token.isGroup()) {
                e.addD(e.depth(), n.completeNodes());
            } else {
                throw new NotDefinedException("Cannot complete the node '" + n + "' because there is no known way to!");
            }
            i++;
        }
        return e;
    }

    /**
     * Fixes nodes to prevent horrible things like "1*- 1" from crashing. Cannot be used on a FinalNode.
     * @return A "fixed" version of the nodes.
     * @throws NotDefinedException Thrown when fixNodes is attempted on a FinalNode
     */
    private Node fixNodes() {
        declND(!(this instanceof FinalNode), "Cannot fixNodes of a FinalNode!");
        assert subNodes != null : "Cannot fixNodes if subNodes is null!"; //should never happen
        int i = 1;
        Node node = copy();
        while(i < node.size()){
            Node n = node.get(i);
            assert n != null  : "no subNode can be null!"; // this should have been caught beforehand.
            if(n.token.type() == OPER && n.token.val().equals("-") && node.get(i - 1).token.type() == OPER &&
                                   (node.get(i - 1).token.val().equals("/") ||
                                    node.get(i - 1).token.val().equals("*") ||
                                    node.get(i - 1).token.val().equals("^"))) {
                Node n2 = new Node(new Token("doesnt matter what I put here because it will become GRP", GROUP));
                n2.add(new FinalNode(new Token("0", NUM)));
                n2.add(n);
                n2.add(node.get(i + 1));
                node.rem(i + 1);
                node.set(i, n2);
                i++;
            }
            i++;
        }
        return node;
    }
    /**
     * Generates a "master node" from a list of tokens.
     * @param pTokens   The list of tokens that the master node will be based off of. Cannot be null.
     * @return The new master node.
     * @throws IllegalArgumentException Thrown when <code>pTokens == null</code>.
     */
    public static Node generateNodes(ArrayList<Token> pTokens) {
        decl(pTokens != null, "Cannot generate nodes from a null ArrayList of Tokens!");
        decl(checkForNullTokens(pTokens), "Cannot generate nodes with null Tokens in pTokens!");
        return ((Node)(new Node(Token.UNI).condeseNodes(0, pTokens))[1]).completeNodes().fixNodes();
    }


    /**
     * Looks for null tokens, returns false if there are null tokens.
     */
    private static boolean checkForNullTokens(ArrayList<Token> pTokens){
        for(Token t : pTokens)
            if(t == null)
                return false;
        return true;
    }

    /**
     * Appends n to the end of {@link #subNodes}.
     * <br>Note: <code>this</code> cannot be an instance of {@link FinalNode}.
     * @param n             The node to append. Cannot be null.
     * @throws NotDefinedException Thrown if this function is attempted to be executed on a {@link FinalNode}.
     * @throws IllegalArgumentException Thrown if n is null.
     */
    private void add(Node n) throws
                         NotDefinedException,
                         IllegalArgumentException {
        declP(n != null, "Cannot add a null Node!");
        declND(!(this instanceof FinalNode), "Cannot add subNodes to a FinalNode!");
        assert subNodes != null; // should have been checked already.
        subNodes.add(n);
    }

    /**
     * Returns the size of {@link #subNodes}. Note there is no restriction on this for FinalNodes, because there is no
     * need for it as of yet.
     * @return The size of {@link #subNodes}.
     */
    public int size() {
        assert subNodes != null; // should have been checked already.
        return subNodes.size();
    }

    /**
     * Returns the node at position i in {@link #subNodes}. Note there are no safeguards for this;
     * this means that i can be beyond {@link #size()}.
     * <br>Note: <code>this</code> cannot be an instance of {@link FinalNode}.
     * @param i         The position of the node to get.
     * @return The node at position i in {@link #subNodes}.
     * @throws NotDefinedException Thrown if this function is attempted to be executed on a {@link FinalNode}.
     */
    public Node get(int i) throws NotDefinedException{
        declND(!(this instanceof FinalNode), "Cannot get subNodes of a FinalNode!");
        assert subNodes != null; // should have been checked already.
        return subNodes.get(i);
    }

    /**
     * Sets the node at position i in {@link #subNodes} to n.
     * Note: <code>this</code> cannot be an instance of {@link FinalNode}.
     * <br>Note: this doesn't check if i is out of bounds.
     * @param i         The position that n will be set to.
     * @param n         The node to replace the current one at position i. Cannot be null.
     * @throws NotDefinedException Thrown if this function is attempted to be executed on a {@link FinalNode}.
     * @throws IllegalArgumentException Thrown if n is null.
     */    
    private void set(int i,
                    Node n) throws
                        NotDefinedException, 
                        IllegalArgumentException {
        declND(!(this instanceof FinalNode), "Cannot set subNodes of a FinalNode!");
        declP(n != null, "Cannot set a subNode to a null Node!");
        assert subNodes != null; // should have been checked already.
        subNodes.set(i, n);
    }

    /**
     * Removes the node at position i. Note: this doesn't check if i is out of bounds.
     * <br>Note: <code>this</code> cannot be an instance of {@link FinalNode}.
     * @param i         The position of the node to remove.
     * @throws NotDefinedException Thrown if this function is attempted to be executed on a {@link FinalNode}.
     */
    private void rem(int i) throws NotDefinedException{
        declND(!(this instanceof FinalNode), "Cannot remove subNodes from a FinalNode!");
        assert subNodes != null; // should have been checked already.
        subNodes.remove(i);
    }

    /**
     * Returns how many layers are in this node. <br>Note: if <code>this</code> is an instance of {@link FinalNode},
     * the depth is 1. <br>Note: if {@link #size()} is 0, the depth is also 1.
     * See {@link #addD(Node) addD} for more information on what depth is.
     * @return The "depth" of this node.
     */
    private int depth() {
        if(this instanceof FinalNode)
            return 1;
        return size() == 0 ? 1 : 1 + get(size() - 1).depth();
    }

    private Node getD(int i) {
        if(this instanceof FinalNode)
            return this;
        if(i <= 0) {
            return this;
        } else {
            return get(size() - 1).getD(i - 1);
        }
    }

    private void addD(int i, Node n) throws  NotDefinedException, IllegalArgumentException {
        declND(!(this instanceof FinalNode), "Cannot addDepth subNodes to a FinalNode!");
        declP(n != null, "Cannot addDepth null Nodes!");
        if(i <= 0 || size() <= 0 || get(size() - 1).token.type() == GROUP) {
            add(n);
        } else {
            if(i == 2 && get(size() - 1) instanceof FinalNode) {
                add(n);
            } else {
                get(size() - 1).addD(i - 1, n);
            }
        }

    }

    private void setD(int i, int p, Node n) throws NotDefinedException, IllegalArgumentException {
        declND(!(this instanceof FinalNode), "Cannot setDepth subNodes of a FinalNode!");
        declP(n != null, "Cannot setDepth null Nodes!");
        if(i == 0) {
            assert size() > 0;
            assert size() > p && (p >= 0 || p == -1);
            if(p == - 1) {
                set(size() - 1,n);
            } else {
                set(p, n);
            }
        } else {
            assert !(get(size() - 1) instanceof FinalNode); //shouldnt happen, methinks.
            if(i == 2 && get(size() - 1) instanceof FinalNode ) {
                Print.printi("Trying to setD to a FinalNode. Going one level up instead.");
                set(size() - 1,n);
            } else if(get(size() - 1).token.type() == GROUP) {
                set(p, n);
            } else {
                get(size() - 1).setD(i - 1, p, n);
            }
        }
    }

    public String genEqString() {
        String ret = "";
        switch(token.type()) {
            case FUNC:
                ret += token.val(); //fall thru
            case GROUP:
                ret += "(";
                break;
            case NUM: case VAR:
                return token.val();
        }
        for(Node n : subNodes) {
            ret += n.genEqString();
            switch(token.type()) {
                case FUNC: case GROUP:
                    ret += ", ";
                    break;
                case OPER:
                    ret += " " + token.val() + " ";
            }
        }
        if(token.type() == FUNC || token.type() == GROUP) {
            return ret.substring(0, ret.length() - (size() > 0 ? 2 : 0)) + ")";
        } else if(token.type() == OPER) {
            return ret.substring(0, ret.length() - (size() > 0 ? 3 : 0));
        } else {
            return ret;
        }
    }

    public double eval(final EquationSystem pEqSys) throws NotDefinedException {
        //TODO: IMPLEMENT DOMAIN
        assert !(this instanceof FinalNode) : "This is implemented in FinalNode... How was i triggered...?";
        assert token != null;
        declP(pEqSys != null, "Cannot evaluate a null EquationSystem!");
        double ret = 0;
        if(token.type() == FUNC || token.type() == OPER) {
            if(pEqSys.functions().get(token.val()) != null){ // if it is a function
                ret = pEqSys.functions().get(token.val()).exec(pEqSys, this);
            } else {
                ret = InBuiltFunction.exec(token.val(), pEqSys, this);
            }
        } else if (token.type() == GROUP || token.isUni() || this instanceof FinalNode){
            ret = get(0).eval(pEqSys);
        } else {
            throw new NotDefinedException("This shouldn't happen! There is no way to evaluate node: " + token.val());
        }
        return ret;
    }

    @Override
    public String toString() {
        String ret = "Node: token = [" + token + "], subNodes = [";
        for(Node node : subNodes) {
            ret += "(" + node + "), ";
        }
        return ret.substring(0, ret.length() - (size() > 0 ? 2 : 0)) + "]";

    }
    
    @Override
    public String toFancyString(int idtLvl) {
        String ret = indent(idtLvl) + "Node '" + token.val() + "' (type = " + token.type() + "):\n";
        ret += indent(idtLvl + 1) + "Sub Nodes:";
        for(Node node : subNodes) 
            ret += "\n" + node.toFancyString(idtLvl + 2);
        if(size() == 0) 
            ret += "\n" + indent(idtLvl + 2) + "null";
        return ret;
    }

    @Override
    public String toFullString(int idtLvl) {
        String ret = indent(idtLvl) + "Node:\n";
        ret += indent(idtLvl + 1) + "Token:\n";
        ret += token.toFullString(idtLvl + 2) + "\n";
        ret += indent(idtLvl + 1) + "Sub Nodes:";
        for(Node node : subNodes) 
            ret += "\n" + node.toFullString(idtLvl + 2);
        ret += "\n" + indentE(idtLvl + 2);
        if(size() == 0) 
            ret += "\n" + indent(idtLvl + 2) + "null";
        return ret + "\n" + indentE(idtLvl + 1);
    }

    @Override
    public boolean equals(Object pObj){
        if(pObj == null || !(pObj instanceof  Node))
            return false;
        if(this == pObj)
            return true;
        Node pnode = (Node)pObj;
        if(!token.equals(pnode.token()))
            return false;
        if(size() != pnode.size())
            return false;
        for(int i = 0; i < size(); i++){
            if(!get(i).equals(pnode.get(i)))
                return false;
        }
        return true;
    }

    @Override
    public Node copy(){
        return new Node(new Token(token.val(), token.type()), subNodes.copy());
    }
}