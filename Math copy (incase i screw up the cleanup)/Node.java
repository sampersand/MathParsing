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
    public String fullString(){
        String ret = "{\"" + TOKEN.VAL + "\" | " + TOKEN.TYPE + " | ";
        for(Node node : subNodes)
            ret += node.fullString() + ", ";
        return ret.substring(0,ret.length()-2) + "}";
    }
    public String toString(){
        String ret = "{" + TOKEN.VAL + ": ";
        for(Node node : subNodes)
                ret += node + ", ";
        return ret.substring(0,ret.length() - 2) + "}";
    }
}



