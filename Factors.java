import java.util.HashMap;
public class Factors {
    public HashMap<String, Double> vars;
    public HashMap<String, Function> funcs;
    public Factors(){
        this(new HashMap<String, Double>(),new HashMap<String, Function>());
    }
    public Factors(HashMap<String, Double> pVars, HashMap<String, Function> pFuncs){
        vars = pVars;
        funcs = pFuncs;
    }
    public void setVars(HashMap<String, Double> pVars){
        vars = pVars;
    }
    public void setFuncs(HashMap<String, Function> pFuncs){
        funcs = pFuncs;
    }    
    public double eval(Node pNode){
        if(pNode instanceof FinalNode){
            FinalNode fNode = (FinalNode)pNode;
            System.out.println(fNode);
            if(fNode.TYPE == Token.Types.NUM){
                return fNode.dVal;
            } else if(fNode.TYPE == Token.Types.VAR){
                if(vars.get(fNode.NAME) == null){
                    System.err.println("[ERROR] Varriable '" + fNode.NAME + "' isn't defined in vars! Defaulting to a value of '0' instead.");
                    return 0;
                }
                return (double)vars.get(fNode.NAME);
            }
            else{
                System.err.println("[ERROR] FinalNode '" +fNode.sVal + "/" + fNode.dVal +"' isn't a num or Var!");
            }
        }
        double ret = 0;
        for(Node n : pNode.subNodes){
            if(n.TYPE == Token.Types.FUNC ){
                if(funcs.get(n.NAME) == null){
                    System.err.println("[ERROR] Function '" + n.NAME + "' isn't defined in functions! Defaulting to a value of '0' instead.");
                    return 0;
                }
                else{
                    ret += funcs.get(n.NAME).exec(this, n);
                }
            }
            else if(n.TYPE == Token.Types.GROUP || (n.TYPE == Token.Types.NULL && n.NAME == "E")){
                ret += eval(n);
            }
            else if(n.TYPE == Token.Types.OPER){                
                switch(n.NAME){
                    case "+": for(Node n2 : n.subNodes) ret += eval(n2); break;
                    case "-": for(Node n2 : n.subNodes) ret -= eval(n2); break;
                    case "*": for(Node n2 : n.subNodes) ret *= eval(n2); break;
                    case "/": for(Node n2 : n.subNodes) ret /= eval(n2); break;
                    case "^": for(Node n2 : n.subNodes) ret = Math.pow(ret, eval(n2)); break;
                }
            }
        }
        return ret;
    }
}