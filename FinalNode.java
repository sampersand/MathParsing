public class FinalNode extends Node {
    private String sVal;
    private double dVal;
    public final Token.Types TYPE;
    public FinalNode(){
        this(null);
    }
    public FinalNode(Token pToken){
        if(pToken == null)
            System.err.println("[ERROR] FinalToken: pToken is null! Continuing anyways!");
        TYPE = pToken.TYPE;
        if(TYPE != Token.Types.NUM && TYPE != Token.Types.VAR)
            System.err.println("[ERROR] FinalToken: pToken.TYPE isn't NUM or VAR! Continuing anyways!");
        if (TYPE == Token.Types.NUM)
            try {
                dVal = Double.parseDouble(pToken.VAL);
            } catch(NumberFormatException err) {
                System.err.println("[ERROR] FinalToken: pToken.TYPE is a NUM, but pToken.VAL isn't!");
            }
        if (TYPE == Token.Types.VAR)
            sVal = pToken.VAL;
        
    }
    public Object getVal(){return "";}
    public String toString(){
        // String ret = "[";
        // ret += TYPE == Token.Types.VAR ? "\"" + sVal + "\"" : dVal;
        // ret += ": " + (TYPE == Token.Types.VAR ? "VAR" : "NUM") + "]";
        return TYPE == Token.Types.VAR ? sVal : "" + dVal;

    }
    
}