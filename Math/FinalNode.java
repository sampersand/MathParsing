public class FinalNode extends Node {

    public String sVal;
    public double dVal;
    public final Token.Types TYPE;
    public FinalNode(){
        this(null);
    }
    public FinalNode(Token pToken) throws TypeMisMatchException, DoesntExistException{
        if(pToken == null){
            throw new DoesntExistException("pToken is null!");
        }
        TYPE = pToken.TYPE;

        if (TYPE == Token.Types.NUM)
            try {
                dVal = Double.parseDouble(pToken.VAL);
            } catch(NumberFormatException err) {
                throw new TypeMisMatchException("pToken.TYPE is a NUM, but pToken.VAL isn't!");
            }
        else if (TYPE == Token.Types.VAR)
            sVal = pToken.VAL;
        else
            throw new TypeMisMatchException("pToken.TYPE isn't NUM or VAR!");
        
    }
    public String toString(){
        String ret = "[";
        ret += TYPE == Token.Types.VAR ? "\"" + sVal + "\"" : dVal;
        ret += ": " + (TYPE == Token.Types.VAR ? "VAR" : TYPE == Token.Types.NUM ? "NUM" : "NULL");
        return ret + "]";

    }
    
}