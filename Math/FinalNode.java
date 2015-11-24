public class FinalNode extends Node {

    public String sVal;
    public double dVal;
    public FinalNode(){
        this(null);
    }
    public FinalNode(Token pToken) throws TypeMisMatchException, DoesntExistException{
        super(pToken);        
        if (TOKEN.TYPE == Token.Types.NUM)
            try {
                dVal = Double.parseDouble(pToken.VAL);
            } catch(NumberFormatException err) {
                throw new TypeMisMatchException("pToken.TOKEN.TYPE is a NUM, but pToken.VAL isn't!");
            }
        else if (TOKEN.TYPE == Token.Types.VAR)
            sVal = pToken.VAL;
        else
            throw new TypeMisMatchException("pToken.TOKEN.TYPE isn't NUM or VAR!");
        
    }
    public String toString(){
        String ret = "[";
        ret += TOKEN.TYPE == Token.Types.VAR ? "\"" + sVal + "\"" : dVal;
        ret += ": " + (TOKEN.TYPE == Token.Types.VAR ? "VAR" : TOKEN.TYPE == Token.Types.NUM ? "NUM" : "NULL");
        return ret + "]";

    }
    
}