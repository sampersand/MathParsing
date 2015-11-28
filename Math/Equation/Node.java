package Math.Equation;
import Math.Exception.TypeMisMatchException;
import Math.Exception.DoesntExistException;
import Math.Exception.NotDefinedException;
import static Math.Equation.Token.Types.*;
import java.util.ArrayList;
/**
 * A class that represents either a function, an operator, or a group of tokens.
 * @author Sam Westerman
 * @version 0.1
 */
public class Node {

    /** A list of all nodes that are benith this one in the hierarchical structure. */
    public ArrayList<Node> subNodes;

    /** The token that this class determins how this class interacts with its {@link #subNodes}. */
    public Token token;

    /**
     * The default constructor for Node. Just passes an empty Token to the {@link #Node(Token)} constructor. 
     */
    public Node() {
        this(new Token());
    }

    /**
     * A constructor for Node, which only takes a Node as an input. Just passes pNode's token and an empty list to the
     * {@link #Node(Token,ArrayList) main} constructor.
     * @param pNode        The Node whose token will determine how this class interacts with its {@link #subNodes}.
     */
    public Node(Node pNode) {
        this(pNode.token, new ArrayList<Node>());
    }

    /**
     * A constructor for Node, which only takes a token as an input. Just passes an empty list and pToken to
     * {@link #Node(Token,ArrayList) main} constructor.
     * @param pToken        The token that will determine how this class interacts with its {@link #subNodes}.
     */
    public Node(Token pToken) {
        this(pToken, new ArrayList<Node>());
    }

    /**
     * The main constructor for Node. 
     * @param pToken        The token that will determine how this class interacts with its {@link #subNodes}.
     * @param pSubNodes     The list of subNodes this class will have.
     */
    public Node(Token pToken, ArrayList<Node> pSubNodes) {
        subNodes = pSubNodes;
        token = pToken;
    }

    /**
     * An alternate constructor for Node. This one utilizes varargs. 
     * To instantiate using this constructor, you would do somehting like:
     * <code>Node n = new Node(Token, Node1, Node2, ... , NodeN</code>.
     * @param pToken        The token that will determine how this class interacts with its {@link #subNodes}.
     * @param pSubNodes     A of subNodes this class will have.
     */
    public Node(Token pToken, Node... pSubNodes) {
        subNodes = new ArrayList<Node>() {{
            for(Node n : pSubNodes)
                add(n);
            }};
        token = pToken;
    }

    /**
     * Condenses functions and groups together into a single node. Creates 1-length nodes for operators and vars / nums.
     * Note that this is only ever used with {@link #fixNodes(Node) fixNodes}, 
     * {@link #generateNodes(ArrayList) generateNodes} and {@link #completeNodes(Node) completeNodes}.
     * @param pos       The position to start condensing nodes form.
     * @param n         The "parent" node that any newly generated nodes will be put into.
     * @param pTokens   The list of tokens that will be put into nodes.
     * @return The return type might seem odd, however, it always returns an updated pos as argument 1, and the new node
     *         to add as argument 2.
     */
    public static Object[] condeseNodes(int pos, Node n, ArrayList<Token> pTokens) {
        while(pos < pTokens.size()) {
            Token t = pTokens.get(pos);
            if(t.isConst())
                n.add(new FinalNode(t));
            if(t.isOper())
                n.add(new Node(t));
            if(t.isGroup()) {
                int paren = 0;
                int x = pos + 1;
                do{
                    if(pTokens.get(x).TYPE == LPAR) paren++;
                    if(pTokens.get(x).TYPE == RPAR) paren--;
                    x++;
                } while(0 < paren && x < pTokens.size());

                ArrayList<Token> passTokens = new ArrayList<Token>();
                for(Token tk : pTokens.subList(pos + 1, x ))
                     passTokens.add(tk);

                Object[] temp = condeseNodes(0, new Node(t), passTokens);
                pos += (int)temp[0];
                n.add(fixNodes((Node)temp[1]));
            }
            pos++;
        }
        return new Object[]{pos,n};
    }


    /**
     * Creates a master node using the pNode as a starting point by applying the Order of Operations.
     * Note: pNode should already have been condensed via {@link #condeseNodes(int,Node,ArrayList) condeseNodes}, and
     * fixed via {@link #fixNodes(Node) fixNodes}.
     * @param pNode    The node that will be used to generate the new hierarchically-structured master node.
     * @return The "master" node - that is, the node to control all other nodes. HAH - LOTR reference.
     */
    public static Node completeNodes(Node pNode){
        if(pNode instanceof FinalNode)
            return pNode;      
        Node e = new Node(pNode.token);
        int i = 0;
        while(i < pNode.size()){
            Node n = pNode.get(i);
            if(n instanceof FinalNode){
                e.addD(n);
            }
            else if(n.token.isOper()){
                for(int depth = 1; depth < e.depth(); depth++){
                    Node nD = e.getD(depth, true);
                    if(nD instanceof FinalNode){
                        n.add(nD);
                        e.setD(depth - 1, n); //depth is a final node.
                        break;
                    }
                    else if(n.token.priority() < nD.token.priority()){
                        n.add(nD);
                        n.add(completeNodes(pNode.get(i + 1)));
                        i++;
                        e.setD(depth - 1, n);
                        break;
                    }
                    else if (nD.token.isGroup()){
                        n.add(nD);
                        n.add(completeNodes(pNode.get(i + 1)));
                        i++;
                        e.setD(depth - 1, n);
                        break;
                    }
                }

            }
            else if(n.token.isGroup()){
                e.addD(e.depth(), completeNodes(n), false);
            }
            else{
                throw new NotDefinedException("There is no known way to complete the node '" + n + "'.");
            }
            i++;
        }
        return e;
    }

    /** 
     * Fixes nodes to prevent horrible things like "1*-1" from crashing.
     * @param pNode         The node whose subnodes will be fixed.
     * @return A "fixed" version of the nodes.
     */
    public static Node fixNodes(Node pNode){
        int i = 0;
        while(i < pNode.size()){
            Node n = pNode.get(i);
            if(n.type() == OPER && n.token.VAL.equals("-") && pNode.get(i - 1).type() == OPER &&
                                   (pNode.get(i - 1).token.VAL.equals("/") || pNode.get(i - 1).token.VAL.equals("*") ||
                                    pNode.get(i - 1).token.VAL.equals("^"))){
                Node n2 = new Node(new Token("doesnt matter what I put here", GROUP));
                n2.add(new FinalNode(new Token("0", NUM)));
                n2.add(n);
                n2.add(pNode.get(i + 1));
                pNode.rem(i + 1);
                pNode.set(i, n2);
                i++;
            }
            i++;
        }

        return pNode;
    }
    /**
     * Generates a "master node" from a list of tokens.
     * @param pTokens   The list of tokens that the master node will be based off of.
     * @return The new master node - usually {@link Equation#node} is set to this.
     */
    public static Node generateNodes(ArrayList<Token> pTokens) {
        return completeNodes(fixNodes((Node)condeseNodes(0, new Node(Token.UNI), pTokens)[1]));
    }


    /**
     * Appends Node n to the end of {@link #subNodes}.
     * @param n             The node to append.
     */
    public void add(Node n) {
        subNodes.add(n);
    }

    /**
     * Creates a {@link Node}(or {@link FinalNode}) for t, and then appends that to the end of {@link #subNodes}.
     * @param t             The token to create a new Node for, and then append.
     */    
    public void add(Token t) {
        add(t.isConst() ? new FinalNode(t) : new Node(t));
    }

    /**
     * Returns the size of {@link #subNodes}.
     * @return The size of {@link #subNodes}.
     */
    public int size() {
        return subNodes.size();
    }

    /**
     * Returns the node at position i in {@link #subNodes}.
     * @param i         the position of the node to get.
     * @return The node at position i in {@link #subNodes}.
     */
    public Node get(int i) {
        return subNodes.get(i);
    }
    /**
     * Sets the node at position i in {@link #subNodes} to n.
     * Note: this doesn't check if i is out of bounds.
     * @param i         the position that n will be set to.
     * @param n         the node to replace the current one at position i.
     */    
    public void set(int i, Node n) {
        subNodes.set(i, n);
    }

    /**
     * Removes the node at position i.
     * Note: this doesn't check if i is out of bounds.
     * @param i         the position of the node to remove.
     */
    public void rem(int i) {
        subNodes.remove(i);
    }

    /**
     * Returns how many layers are in this node.
     * See {@link #addD(Node) addD} for more information on what depth is.
     * @return The "depth" of this node.
     */
    public int depth() {
        if(this instanceof FinalNode)
            return 1;
        return size() == 0 ? 1 : 1 + get(size() - 1).depth();
    }

    /**
     * Goes down i layers, or until a group / function is hit 
     * to get the last node in the subNode list. Note: if the current node is a FinalNode, and getD is called, it will
     * just return itself.
     * <br>For example, getting the very last node in {@link #subNodes} (<code>subNode.get(subNode.size() - 1))</code>) is
     * "going down one layer". Don't know how to explain much better.
     * @param i         The amount of layers to go down.
     * @return The final node at layer i. Doesn't check to see if i is a valid layer.
     */
    public Node getD(int i) {
        return getD(i, false);
    }
    /**
     * Goes down i layers, or until a group / function is hit if pOver is false.
     * to get the last node in the subNode list. Note: if the current node is a FinalNode, and getD is called, it will
     * just return itself.
     * <br>For example, getting the very last node in {@link #subNodes} (<code>subNode.get(subNode.size() - 1))</code>) is
     * "going down one layer". Don't know how to explain much better.
     * @param i         The amount of layers to go down.
     * @param pOver     Whether or not it will "override", and continue going down if a group is encountered.
     * @return The final node at layer i. Doesn't check to see if i is a valid layer.
     */
    public Node getD(int i, boolean pOver) {
        if(this instanceof FinalNode)
            return this;
        if(i <= 0 || (get(size() - 1).type() == GROUP &&! pOver)){
            return this;
        } else {
            return get(size() - 1).getD(i - 1, pOver);
        }
    }
    /**
     * Adds the node n at the maximum depth, or until a group or function is hit.
     * @param n         The node to add at the deepest possible layer.
     */
    public void addD(Node n) {
        addD(depth(), n);
    }
    /**
     * Adds the node n to end of the layer i, or until a group / function is hit.
     * @param i         The amount of layers to go down.
     * @param n         The node to add to the last position at layer i.
     */
    public void addD(int i, Node n) {
        addD(i, n, false);
    }
    /**
     * Adds the node n to end of the layer i. If  <code>i &#060;=2</code>, it will just add n to the end. Additionally,
     * if pOver isn't true, it will stop when it encounters a group or function.
     * @param i         The amount of layers to go down.
     * @param n         The node to add to the last position at layer i.
     * @param pOver     Whether or not it will "override", and continue going down if a group is encountered.
     */
    public void addD(int i, Node n, boolean pOver) {
        if(this instanceof FinalNode) {
            throw new TypeMisMatchException("Can't add subnodes to a FinalNode!");
        }
        // else if(this.token.isUni()){ // not 100% sure
            // System.out.println("pos 1");
            // add(n); }                  // these two fixed it, but oh well.
        else if(i <= 0 || size() <= 0 || (get(size() - 1).type() == GROUP &&! pOver)) {
            add(n);
        } else {
            if(i == 2 && get(size() - 1) instanceof FinalNode) {
                add(n);
            } else {
                get(size() - 1).addD(i - 1, n, pOver);
            }
        }

    }

    /**
     * Sets the last node at layer i to node n, or until a group / functionis hit.
     * @param i         The amount of layers to go down.
     * @param n         The node to set the last node to.
     */
    public void setD(int i, Node n) {
        setD(i, -1, n);
    }

    /**
     * Sets the node at position p at layer i to node n, or until a group / function is hit
     * @param i         The amount of layers to go down.
     * @param p         The position of the node that will be replaced by n.
     * @param n         The node that will replace the node at i layers down, at position p.
     */
    public void setD(int i, int p, Node n) {
        setD(i, p, n, false);
    }

    /**
     * Sets the node at position p at layer i to node n, or until a group / function is hit (except if pOver is true).
     * @param i         The amount of layers to go down.
     * @param p         The position of the node that will be replaced by n.
     * @param n         The node that will replace the node at i layers down, at position p.
     * @param pOver     Whether or not it will "override", and continue going down if a group is encountered.
     */   
    public void setD(int i, int p, Node n, boolean pOver) {
        if(this instanceof FinalNode) {
            throw new TypeMisMatchException("Can't set subnodes of a FinalNode!");
        } else if(i == 0) {
            if(size() <= 0) {
                throw new DoesntExistException("Can't set subnodes of a Node with no size!");
            } else if(size() <= p || (p < 0 && p != -1)) {
                throw new DoesntExistException("p has to be between 0 and Node's length -1 (" + size() + "-1)");
            } else{
                if(p == -1) {
                    set(size() - 1,n);
                } else{
                    set(p, n);
                }
            }
        } else {
            if(i == 2 && get(size() - 1) instanceof FinalNode ) {
                System.err.println("Trying to setD to a FinalNode. Going one level up instead.");
                set(size() - 1,n);
            } else if(get(size() - 1).type() == GROUP &&! pOver){
                set(p, n);
            } else {
                get(size() - 1).setD(i - 1, p, n, pOver);
            }
        }
    }

    /**
     * Removes the last node at layer i, or until a group / function is hit.
     * @param i         The amount of layers to go down.
     * @throws TypeMisMatchException thrown when the last node at layer i is an instance of {@link FinalNode}.
     */
    public void remD(int i) throws TypeMisMatchException {
        remD(i, -1);
    }

    /**
     * Removes the last node at layer i, or until a group / function is hit.
     * @param i         The amount of layers to go down.
     * @param p         The position of the node to remove at layer i.
     * @throws TypeMisMatchException thrown when the node at position p, layer i is an instance of {@link FinalNode}.
     */
    public void remD(int i, int p) throws TypeMisMatchException {
        remD(i, p, false);
    }
    /**
     * Removes the last node at layer i, or until a group / function is hit (except if pOver is true).
     * @param i         The amount of layers to go down.
     * @param p         The position of the node to remove at layer i.
     * @param pOver     Whether or not it will "override", and continue going down if a group is encountered.
     * @throws TypeMisMatchException thrown when the node at position p, layer i is an instance of {@link FinalNode}.
     */
    public void remD(int i, int p, boolean pOver) throws TypeMisMatchException {
        if(this instanceof FinalNode)
            throw new TypeMisMatchException("Can't delete subnodes from a FinalNode!");
        if(i <= 0)
            rem(p == -1 ? size() - 1 : p);
        else
            get(size() - 1).remD(i - 1, p, pOver);
    }

    /** 
     * Gets this node's type (like group, function, var, etc).
     * @return This node's type, as defined by {@link #token}.
     */
    public Token.Types type(){
        return token.TYPE;
    }

    /** 
     * Gets an exact copy of this current node
     * @return An exact duplicate of this node, except for its position in memory.
     */
    public Node copy(){
        return new Node(token, subNodes);
    }
    /**
     * The more robust version of this class's {@link #toString()}, but without the indentation.
     * @return A more detailed String representation of this.
     */    
    public String fullString() {
        String ret = "{\"" + token.VAL + "\" | " + type() + " | ";
        for(Node node : subNodes)
            ret += node.fullString() + ", ";
        return ret.substring(0,ret.length()-2) + "}";
    }
    /**
     * A simple representation of this class.
     * @return A simple String representation of this.
     */      
    public String toString() {
        String ret = '{' + token.VAL + ':';
        for(Node node : subNodes){
            ret += node + ", ";
        }
        return ret.substring(0, ret.length() - (size() > 0 ? 2 : 0)) + '}';

    }
    /** 
     * Effectively {@link #toString} but allows for indentations
     * @param pos    The amount of tabs out each line should be.
     * @return A simple String representation of this.
     */
    public String toStringL(int pos){
        String ret = '{' + token.VAL + ':';
        for(Node node : subNodes){
            ret += '\n';
            for(int x = 0; x < pos; x++)
                ret += '\t';
            ret += node.toStringL(pos+1);
        }
        if(size() == 0){
            ret += '\n';
            for(int x = 0; x < pos; x++)
                ret += '\t';
            ret += "null";
        }
        ret += '\n';
        for(int x = 0; x < pos - 1; x++)
                ret += '\t';
        return ret + '}';

    }
    /** 
     * Takes all the {@link #subNodes} and creates its best guess at what a function comprised of them would look like.
     * @return The best guess as to what a function comprised of the subnodes would look like.
     */
    public String genEqString(){
        String ret = "";
        switch(type()){
            case FUNC: ret += token.VAL;
            case GROUP: ret += "("; break;
            case ARGS: return "'" + token.VAL + "'";
            case NUM: case VAR: return token.VAL;
        }
        for(Node n : subNodes){
            ret += n.genEqString();
            switch(type()){
                case FUNC: case GROUP: ret += ", "; break;
                case OPER: ret += " " + token.VAL + " ";
            }
        }
        if(type() == FUNC || type() == GROUP){
            return ret.substring(0, ret.length() - (size() > 0 ? 2 : 0)) + ")";
        } else if(type() == OPER){
            return ret.substring(0, ret.length() - (size() > 0 ? 3 : 0));
        } else
            return ret;
    }
}



