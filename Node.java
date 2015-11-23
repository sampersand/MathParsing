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

    public String toString(){
        String ret = "{" + NAME + ":";
        for(Node node : subNodes)
            ret += node + ", ";
        return ret.substring(0,ret.length()-2) + "}";

        // String ret = "{\"" + NAME + "\" | " + TYPE + " | ";
        // for(Node node : subNodes)
        //     ret += node + ", ";
        // return ret.substring(0,ret.length()-2) + "}";
    }
    public Node copy(){
        return new Node(new Token(NAME, TYPE), subNodes);
    }
}



