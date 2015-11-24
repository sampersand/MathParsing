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
        System.out.println("NODE: " + pNode);

        if(pNode instanceof FinalNode){
            FinalNode fNode = (FinalNode)pNode;
            System.out.println("\tFNODE:"+fNode);
            if(fNode.TYPE == Token.Types.NUM){
                return fNode.dVal;
            } else if(fNode.TYPE == Token.Types.VAR){
                if(vars.get(fNode.sVal) == null){
                    System.err.println("[ERROR] Varriable '" + fNode.NAME + "' isn't defined in vars! Defaulting to a value of '0' instead.");
                    return 0;
                }
                return (double)vars.get(fNode.sVal);
            }
            else{
                System.err.println("[ERROR] FinalNode '" +fNode.sVal + "/" + fNode.dVal +"' isn't a num or Var!");
                return 0;
            }
        }
        double ret = 0;
        for(int pos = 0; pos < pNode.size(); pos++){
            Node n = pNode.get(pos);
            System.out.println("\tSUBNODE: " + n);
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
                System.out.println("\t\tOPER:" + n + " | " + ret);
                System.out.println(n.get(0) + " | " + n.get(1));
                switch(n.NAME){
                    case "+": ret += (eval(n));break;//.get(0)) + eval(n.get(1))); break;
                    case "-": ret -= (eval(n));break;//.get(0)) - eval(n.get(1))); break;
                    case "*": ret *= (eval(n));break;//.get(0)) * eval(n.get(1))); break;
                    case "/": ret /= (eval(n));break;//.get(0)) / eval(n.get(1))); break;
                    case "^": ret += (Math.pow(eval(n.get(0)), eval(n.get(1)))); break;
                    default: System.out.println("[ERROR] UH OH! '" + n.NAME + "' is an OPERATOR, but doesn't have a function assaigned to it. ");
                }
            }
            else if (n instanceof FinalNode){
                ret += eval(n);
            }
            else{
                System.err.println("[ERROR] UH OH! The type of '" + n.NAME + "' (" + n.TYPE + ") doesn't have a known way to evaluate");
            }
        }
System.out.println(ret);        
        return ret;
    }
}