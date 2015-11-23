import java.util.ArrayList;
/** 
 * TODO: make javadoc for this thing.
 */
public class Node {
    public ArrayList<Node> subNodes;
    public final String NAME;
    public final Token.Types TYPE;
    public Node(){
        this(new Token());
    }
    public Node(Token pToken){
        this(pToken, new ArrayList<Node>());
    }
    public Node(Token pToken, ArrayList<Node> pSubNodes){
        subNodes = pSubNodes;
        NAME = pToken.VAL;
        TYPE = pToken.TYPE;
    }
    public Node(Token pToken, Node... pSubNodes){
        subNodes = new ArrayList<Node>(){{
            for(Node n : pSubNodes)
                add(n);
            }};
        NAME = pToken.VAL;
        TYPE = pToken.TYPE;
    }    
    public void add(Node n){
        subNodes.add(n);
    }
    public void add(Token t){
        add(t.isConst() ? new FinalNode(t) : new Node(t));
    }
    public int size(){
        return subNodes.size();
    }
    public Node get(int i){
        return subNodes.get(i);
    }
    public Node remove(int i){
        return subNodes.remove(i);
    }    
    public String toString(){
        String ret = "{" + NAME + ": ";
        for(Node node : subNodes)
            ret += node + ", ";
        return ret.substring(0,ret.length()- (subNodes.size()==0 ? 0 : 2)) + "}";

        // String ret = "{\"" + NAME + "\" | " + TYPE + " | ";
        // for(Node node : subNodes)
        //     ret += node + ", ";
        // return ret.substring(0,ret.length()-2) + "}";
    }
    public Node copy(){
        return new Node(new Token(NAME, TYPE), subNodes);
    }
}



