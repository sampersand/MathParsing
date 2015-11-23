public class FinalNode extends Node {
    public String sVal;
    public double dVal;
    public final Token.Types TYPE;
    public FinalNode(){
        this(null);
    }
    public FinalNode(Token pToken){
        if(pToken == null)
            System.err.println("[ERROR] FinalToken: pToken is null! Continuing anyways!");
        TYPE = pToken.TYPE;
        if (TYPE == Token.Types.NUM)
            try {
                dVal = Double.parseDouble(pToken.VAL);
            } catch(NumberFormatException err) {
                System.err.println("[ERROR] FinalToken: pToken.TYPE is a NUM, but pToken.VAL isn't!");
            }
        else if (TYPE == Token.Types.VAR)
            sVal = pToken.VAL;
        else
            System.err.println("[ERROR] FinalToken: pToken.TYPE isn't NUM or VAR! Continuing anyways!");
        
    }
    public String toString(){
        String ret = "[";
        ret += TYPE == Token.Types.VAR ? "\"" + sVal + "\"" : dVal;
        ret += ": " + (TYPE == Token.Types.VAR ? "VAR" : TYPE == Token.Types.NUM ? "NUM" : "NULL") + "]";
        return ret;

    }
    
}