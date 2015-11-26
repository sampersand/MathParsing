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
    public Node(){
        this(null);
    }

    /** 
     * A constructor for Node, which only takes a token as an input. Just passes an empty list and pToken to
     * {@link #Node(Token,ArrayList) main} constructor.
     * @param pToken        The token that will determine how this class interacts with its {@link #subNodes}.
     */
    public Node(Token pToken){
        this(pToken, new ArrayList<Node>());
    }

    /** 
     * The main constructor for Node. 
     * @param pToken        The token that will determine how this class interacts with its {@link #subNodes}.
     * @param pSubNodes     The list of subNodes this class will have.
     */
    public Node(Token pToken, ArrayList<Node> pSubNodes){
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
    public Node(Token pToken, Node... pSubNodes){
        subNodes = new ArrayList<Node>(){{
            for(Node n : pSubNodes)
                add(n);
            }};
        TOKEN = pToken;
    }

    /** 
     * Appends Node n to the end of {@link #subNodes}.
     * @param n             The node to append.
     */
    public void add(Node n){
        subNodes.add(n);
    }

    /** 
     * Creates a {@link Node}(or {@link FinalNode}) for t, and then appends that to the end of {@link #subNodes}.
     * @param t             The token to create a new Node for, and then append.
     */    
    public void add(Token t){
        add(t.isConst() ? new FinalNode(t) : new Node(t));
    }

    /** 
     * Returns the size of {@link #subnodes}.
     * @return The size of {@link #subNodes}.
     */
    public int size(){
        return subNodes.size();
    }

    /** 
     * Returns the node at position i in {@link #subnodes}.
     * @return The node at position i in {@link #subNodes}.
     */
    public Node get(int i){
        return subNodes.get(i);
    }
    /** 
     * sets the node at position i in {@link #subnodes} to n.
     */    
    public void set(int i, Node n){
        subNodes.set(i, n);
    }

    public void rem(int i){
        subNodes.remove(i);
    }    
    public int depth(){
        if(this instanceof FinalNode)
            return 1;
        return size() == 0 ? 1 : 1 + get(size() - 1).depth();
    }
    public Node getD(int i){
        if(this instanceof FinalNode)
            return this;
        return i <= 0 ? this : get(size() - 1).getD(i - 1);
    }
    public void addD(Node n){
        addD(depth(), n);
    }
    public void setD(int i, Node n){
        setD(i, -1, n);
    }
    public void setD(int i, int pos, Node n){
        if(this instanceof FinalNode)
            throw new TypeMisMatchException("Can't set subnodes of a FinalNode!");
        else if(i == 0){
            if(size() <= 0){
                throw new DoesntExistException("Can't set subnodes of a Node with no size!");
            } else if(size() <= pos || (pos < 0 && pos != -1)){
                throw new DoesntExistException("Pos has to be between 0 and Node's length -1 (" + size() + "-1)");
            } else{
                if(pos == -1)
                    set(size() - 1,n);
                else
                    set(pos, n);
            }

        }
        else{
            if(i == 2 && get(size() - 1) instanceof FinalNode ){
                System.err.println("Trying to setD to a FinalNode. Going one level up instead.");
                set(size() - 1,n);
                return;
            }
            get(size() - 1).setD(i - 1, pos, n);
            
        }

    }
    public void addD(int i, Node n){
        if(this instanceof FinalNode)
            throw new TypeMisMatchException("Can't add subnodes to a FinalNode!");
        else if(i <= 0 || size() <= 0)
            add(n);
        else{
            if(i == 2 && get(size() - 1) instanceof FinalNode ){
                add(n);
                return;
            }
            get(size() - 1).addD(i - 1, n);
            
        }
            

    }
    public void remD(int i) throws TypeMisMatchException{
        if(this instanceof FinalNode)
            // return;
            throw new TypeMisMatchException("Can't delete subnodes from a FinalNode!");
        if(i <= 0)
            rem(size() - 1);
        else
            get(size() - 1).remD(i - 1);
    }
    /** 
     * The more robust version of this class's {@link #toString()}.
     * @return A more detailed String representation of this.
     */    
    public String fullString(){
        String ret = "{\"" + TOKEN.VAL + "\" | " + TOKEN.TYPE + " | ";
        for(Node node : subNodes)
            ret += node.fullString() + ", ";
        return ret.substring(0,ret.length()-2) + "}";
    }
    /** 
     * A simple representation of this class.
     * @return A simple String representation of this.
     */      
    public String toString(){
        String ret = "{" + TOKEN.VAL + ": ";
        for(Node node : subNodes)
                ret += node + ", ";
        return ret.substring(0,ret.length() - 2) + "}";
    }
}



