import java.util.ArrayList;
/** 
 * TODO: make javadoc for this thing.
 */
public class Node {
    public ArrayList<Node> subNodes;
    public final Token TOKEN;
    public Node(){
        this(new Token());
    }
    public Node(Token pToken){
        this(pToken, new ArrayList<Node>());
    }
    public Node(Token pToken, ArrayList<Node> pSubNodes){
        subNodes = pSubNodes;
        TOKEN = pToken;
    }
    public Node(Token pToken, Node... pSubNodes){
        subNodes = new ArrayList<Node>(){{
            for(Node n : pSubNodes)
                add(n);
            }};
        TOKEN = pToken;
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
    public Node set(int i, Node n){
        return subNodes.set(i, n);
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
    public void addD(int i, Node n){
        if(this instanceof FinalNode)
            throw new TypeMisMatchException("Can't add subnodes to a FinalNode!");
        // if(i <= 0 && size == 0)
            // throw new DoesntExistException("Can't add subnodes to a FinalNode!");

        if(i > 0 && size() > 0)
            get(size() - 1).addD(i - 1, n);
        else
            add(n);
            

    }
    public void remD(int i) throws TypeMisMatchException{
        if(this instanceof FinalNode)
            return;
            // throw new TypeMisMatchException("Can't delete subnodes from a FinalNode!");
        if(i <= 0)
            rem(size() - 1);
        else
            get(size() - 1).remD(i - 1);
    }    
    public String toString(){
        String ret = "{\"" + TOKEN.VAL + "\" | " + TOKEN.TYPE + " | ";
        for(Node node : subNodes)
            ret += node + ", ";
        return ret.substring(0,ret.length()-2) + "}";
    }
}



