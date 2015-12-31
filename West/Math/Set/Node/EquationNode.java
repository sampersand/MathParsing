package West.Math.Set.Node;
import West.Math.Set.Collection;
import java.util.ArrayList;
import West.Math.Equation.Equation;
import West.Math.Exception.NotDefinedException;
import java.util.HashMap;
import West.Math.MathObject;
/**
 * TODO: JAVADOC
 * 
 * @author Sam Westerman
 * @version 0.76
 * @since 0.75
 */
public class EquationNode extends Node<EquationNode.Comparator, EquationNode> implements MathObject{
    interface TokenObj{
        public Object compare(Object obj1, Object obj2);
    }
    public static class Comparator{
        public final String SYMBOL;
        public final TokenObj TOKENOBJ;
        public Comparator(String s, TokenObj t){
            SYMBOL = s;
            TOKENOBJ = t;
        }
        public String toString(){
            return SYMBOL;
        }
    }

    public static final HashMap<String, Comparator> COMPARATOR = new HashMap<String, Comparator>()
    {{
       put("<=", new Comparator("<=", (obj1, obj2) -> null));
       put(">=", new Comparator(">=", (obj1, obj2) -> null));
       put("!=", new Comparator("!=", (obj1, obj2) -> null));
       put("<",  new Comparator("<",  (obj1, obj2) -> null));
       put(">",  new Comparator(">",  (obj1, obj2) -> null));
       put("=",  new Comparator("=",  (obj1, obj2) -> null));
       put("≠",  new Comparator("≠",  (obj1, obj2) -> null));
       put("≥",  new Comparator("≥",  (obj1, obj2) -> null));
       put("≤",  new Comparator("≤",  (obj1, obj2) -> null));
       put(null, null);
   }};

    public static final HashMap<String, Comparator> BOOLEANS = new HashMap<String, Comparator>()
    {{
      put("||", new Comparator("||", (obj1, obj2) -> null));
      put("&&", new Comparator("&&", (obj1, obj2) -> null));
      put("^^", new Comparator("^^", (obj1, obj2) -> null));
    }};

    public EquationNode(){
        super();
    }

    public EquationNode(ArrayList<Equation> pElements){
        super();
        elements.add(generateMasterNode(pElements));
    }
    public EquationNode(Node pCollection) {
        super();
        elements.add(pCollection);
    }
    public EquationNode(Comparator pEquation) {
        token = pEquation;
    }

    public static Comparator getComp(String s){
        return BOOLEANS.get(s) == null ? COMPARATOR.get(s) : BOOLEANS.get(s);
    }
    // private Object[] condeseNodes(int pPos, ArrayList<Equation> pEquations) {
    //     assert pEquations != null;
    //     assert checkForNullEquations(pEquations);
    //     EquationNode node = copy();
    //     while(pPos < pEquations.size()) {
    //         Equation t = pEquations.get(pPos);
    //         assert t != null : "this should have been caught earlier.";
    //         if(t.isConst() || t.isOper()){
    //             node.add(new EquationNode(t));
    //         } else if(t.isFunc()) {
    //             int paren = 0;
    //             int x = pPos + 1;

    //             do{
    //                 if(Equation.PAREN_L.contains(pEquations.get(x).val())) paren++;
    //                 if(Equation.PAREN_R.contains(pEquations.get(x).val())) paren--;
    //                 x++;
    //             } while(0 < paren && x < pEquations.size());
    //             Collection<Equation> passEquations = new Collection<Equation>();
    //             for(Equation tk : pEquations.subList(pPos + 1, x ))
    //                  passEquations.add(tk);
    //             Object[] temp = new EquationNode(t).condeseNodes(0, passEquations);
    //             pPos += (int)temp[0];
    //             node.add(((EquationNode)temp[1]).fixNodes());
    //         } 
    //         pPos++;
    //     }
    //     return new Object[]{pPos, node};
    // }
    private Object[] condeseNodes(int pPos, ArrayList<Equation> pEquations){
        assert false;
        return null;
    }
    protected EquationNode completeNodes(){
        assert false;
        return null;
    }
    // private EquationNode completeNodes() {
    //     if(isFinal())
    //         return this;
    //     EquationNode node = copy();
    //     EquationNode e = new EquationNode(token);
    //     int i = 0;
    //     while(i < node.size()) {
    //         EquationNode n = (EquationNode)node.get(i);
    //         assert n != null : "no subNode can be null!";
    //         if(n.isFinal() && !Equation.isControlChar(n.token().val())) {
    //             e.addD(e.depth(), n);
    //         } else if(n.token.isOper()) {
    //             for(int depth = 1; depth < e.depth(); depth++) {
    //                 EquationNode nD = e.getD(depth);
    //                 assert nD != null : "elements cannot be null!";
    //                 if(nD.isFinal()) {
    //                     n.add(nD);
    //                     e.setD(depth - 1, -1, n); //depth is a final node.
    //                     break;
    //                 } else if(n.token.priority() < nD.token.priority()) {
    //                     n.add(nD);
    //                     n.add(((EquationNode)node.get(i + 1)).completeNodes());
    //                     i++;
    //                     e.setD(depth - 1, -1, n);
    //                     break;
    //                 } else if (nD.token.isFunc()) {
    //                     n.add(nD);
    //                     n.add(node.get(i + 1).completeNodes());
    //                     i++;
    //                     e.setD(depth - 1, -1, n);
    //                     break;
    //                 }
    //             }

    //         } else if(n.token.isFunc()) {
    //             e.addD(e.depth(), n.completeNodes());
    //         } else {
    //             throw new NotDefinedException("Cannot complete the node '" + n + "' because there is no known way to!");
    //         }
    //         i++;
    //     }
    //     return e;
    // }
    // private EquationNode fixNodes() {
    //     int i = 1;
    //     EquationNode node = copy();
    //     // while(i < node.size()){
    //     //     Node n = node.get(i);
    //     //     assert n != null  : "no subNode can be null!"; // this should have been caught beforehand.
    //     //     if(n.token.type() == OPER && n.token.val().equals("-") && node.get(i - 1).token.type() == OPER &&
    //     //                            (node.get(i - 1).token.val().equals("/") ||
    //     //                             node.get(i - 1).token.val().equals("*") ||
    //     //                             node.get(i - 1).token.val().equals("^"))) {
    //     //         Node n2 = new Node(new Equation("", FUNC));
    //     //         n2.add(new FinalNode(new Equation("0", NUM)));
    //     //         n2.add(n);
    //     //         n2.add(node.get(i + 1));
    //     //         node.rem(i + 1);
    //     //         node.set(i, n2);
    //     //         i++;
    //     //     }
    //     //     i++;
    //     // }
    //     return node;
    // }

    public static EquationNode generateMasterNode(ArrayList<Equation> pEquations) {
        assert checkForNullEquations(pEquations);
        return null;
        // return ((EquationNode)(new EquationNode(new Equation()).condeseNodes(0, pEquations))[1]).completeNodes().fixNodes();
    }

    private static boolean checkForNullEquations(ArrayList<Equation> pEquations){
        for(Equation t : pEquations)
            if(t == null)
                return false;
        return true;
    }


    public boolean compare(Object val1, Object val2){ //<T extends Comparable<T>>
    //     assert token instanceof String;
    //     switch((String)token){
    //         case "<": return val1 < val2;
    //         case ">": return val1 > val2;
    //         case "=": return val1 == val2;
    //         case "≠": return val1 != val2;
    //         case "≥": case ">=":
    //             return val1 >= val2;
    //         case "≤": case "<=":
    //             return val1 <= val2;
    //         case "|":
    //             System.out.println("hi im here");
    //             assert false;
    //             // return val1 || val2;
    //         default:
    //             assert false; //shouldnt ever happen
    //     }
        return false;
    }

    // @Override
    // public EquationNode addAllN(ArrayList<Node> pNs){ // add Node
    //     return (EquationNode)super.addAllN(pNs);
    // }
  
    @Override
    public String toString(){
        return "Equaiton"+super.toString();
    }
    @Override
    public EquationNode copy(){
        return (EquationNode)new EquationNode(token).addAllN(elements);
    }

    public EquationNode setToken(String pStr){
        return (EquationNode)super.setToken(BOOLEANS.containsKey(pStr)? BOOLEANS.get(pStr) : COMPARATOR.get(pStr));
    }
  
    @Override
    public EquationNode setToken(Comparator pEquation){
        return (EquationNode)super.setToken(pEquation);
    }
  
    @Override
    protected EquationNode getD(int i) {
        return (EquationNode)super.getD(i);
    }
    
 }