package Math.Equation;
import Math.Equation.Exception.TypeMisMatchException;
import Math.Equation.Exception.DoesntExistException;
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
    public final Token TOKEN;

    /** 
     * The default constructor for Node. Just passes a null value to the {@link #Node(Token)} constructor. 
     */
    public Node() {
        this(null);
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
        TOKEN = pToken;
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
        TOKEN = pToken;
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
     * Goes down i layers to get the last node in the subNode list. Note: if the current node is a FinalNode, and getD
     * is called, it will just return itself.
     * For example, getting the very last node in {@link #subNodes} (<code>subNode.get(subNode.size() - 1))</code>) is
     * "going down one layer". Don't know how to explain much better.
     * @param i    The amount of layers to go down.
     * @return The final node at layer i. Doesn't check to see if i is a valid layer.
     */
    public Node getD(int i) {
        if(this instanceof FinalNode)
            return this;
        return i <= 0 ? this : get(size() - 1).getD(i - 1);
    }

    /** 
     * Adds the node n at the maximum depth.
     * @param n    The node to add at the deepest layer.
     */
    public void addD(Node n) {
        addD(depth(), n);
    }

    /** 
     * Adds the node n to end of the layer i. If  <code>i == 2 || i &#060;=1</code>, it will just add n to the end.
     * @param i    The amount of layers to go down.
     * @param n    The node to add to the last position at layer i.
     */
    public void addD(int i, Node n) {
        if(this instanceof FinalNode) {
            throw new TypeMisMatchException("Can't add subnodes to a FinalNode!");
        }
        else if(this.TOKEN.isGroup() &&! this.TOKEN.isUni() ){ // not 100% sure
            add(n);                                             // these two fixed it, but oh well.
        } else if(i <= 0 || size() <= 0) {
            add(n);
        } else {
            if(i == 2 && get(size() - 1) instanceof FinalNode) {
                add(n);
            } else {
                get(size() - 1).addD(i - 1, n);
            }
        }

    }

    /** 
     * Sets the last node at layer i to node n.
     * @param i    The amount of layers to go down.
     * @param n    The node to set the last node to.
     */
    public void setD(int i, Node n) {
        setD(i, -1, n);
    }

    /** 
     * Sets the node at position p at layer i to node n.
     * @param i    The amount of layers to go down.
     * @param p    The position of the node that will be replaced by n.
     * @param n    The node that will replace the node at i layers down, at position p.
     */    
    public void setD(int i, int p, Node n) {
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
            } else {
                get(size() - 1).setD(i - 1, p, n);
            }
        }
    }

    /** 
     * Removes the last node at layer i.
     * @param i    The amount of layers to go down.
     * @throws TypeMisMatchException thrown when the last node at layer i is an instance of {@link FinalNode}.
     */
    public void remD(int i) throws TypeMisMatchException {
        remD(i, -1);
    }

    /** 
     * Removes the node at position p at layer i.
     * @param i    The amount of layers to go down.
     * @param p    The position of the node to remove at layer i.
     * @throws TypeMisMatchException thrown when the node at position p, layer i is an instance of {@link FinalNode}.
     */
    public void remD(int i, int p) throws TypeMisMatchException {
        if(this instanceof FinalNode)
            throw new TypeMisMatchException("Can't delete subnodes from a FinalNode!");
        if(i <= 0)
            rem(p == -1 ? size() - 1 : p);
        else
            get(size() - 1).remD(i - 1, p);
    }

    /** 
     * The more robust version of this class's {@link #toString()}.
     * @return A more detailed String representation of this.
     */    
    public String fullString() {
        String ret = "{\"" + TOKEN.VAL + "\" | " + TOKEN.TYPE + " | ";
        for(Node node : subNodes)
            ret += node.fullString() + ", ";
        return ret.substring(0,ret.length()-2) + "}";
    }
    /** 
     * A simple representation of this class.
     * @return A simple String representation of this.
     */      
    public String toString() {
        String ret = "{" + TOKEN.VAL + ": ";
        for(Node node : subNodes)
                ret += node + ", ";
        return ret.substring(0,ret.length() - 2) + "}";
    }
}



