import java.io.File;
public class Function {
    public final String NAME;
    public final File FILE;
    public Function(){
        this(null);
    }
    public Function(String pFileName, String pName){
        FILE = new File(pFileName);
        NAME = pName;
    }
    public Function(String pFileName){
        FILE = new File(pFileName);
        NAME = pFileName;
    }
    public double exec(Factors pFactors, Node pNode){
        return pFactors.eval(pNode);
    };

}