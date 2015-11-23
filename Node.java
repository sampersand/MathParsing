import java.util.ArrayList;
/** 
 * TODO: make javadoc for this thing.
 */
public class Node {
    public static enum NodeTypes {FUNC, OPER}
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
        String ret = "{\"" + NAME + "\" | " + TYPE + " | ";
        for(Node node : subNodes)
            ret += node + ", ";
        return ret.substring(0,ret.length()-2) + "}";
    }
}
/*
integral(1,2,f(x),dx)^-2.0
NODE ^:
    NODE integral:
        VALS: 
        1
        2
        NODE f:
            VALS:
            x
        dx
    -2.0
*/