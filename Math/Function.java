import java.lang.reflect.*;
import java.io.*;
public class Function {
    public final String FILE;
    public Function(){
        this(null);
    }
    public Function(String pFileName){
        FILE = pFileName;
    }
    public double exec(Factors pFactors, Node pNode) throws
            ClassNotFoundException, NoSuchMethodException, InvocationTargetException,
            IllegalAccessException, ArrayIndexOutOfBoundsException {
        double[] args = new double[pNode.size()];
        for(int i = 0; i < args.length; i++) args[i] = pFactors.eval(pNode.get(i));
        switch(pNode.NAME.toLowerCase()){
            case "sin": return Math.sin(args[0]);
            case "cos": return Math.cos(args[0]);
            case "tan": return Math.tan(args[0]);

            case "sinh": return Math.sinh(args[0]);
            case "cosh": return Math.cosh(args[0]);
            case "tanh": return Math.tanh(args[0]);

            case "asin": return Math.asin(args[0]);
            case "acos": return Math.acos(args[0]);
            case "atan": return Math.atan(args[0]);

            case "abs": return Math.abs(args[0]);
            case "ceil": return Math.ceil(args[0]);
            case "floor": return Math.floor(args[0]);
            case "hypot": return Math.hypot(args[0], args[1]);
            case "ln": return Math.log(args[0]);
            case "log": return Math.log10(args[0]);
            case "random":
            case "rand": return Math.random();
            case "round": return Math.round(args[0]);
            case "sqrt": return Math.sqrt(args[0]);
            case "degr": return Math.toDegrees(args[0]);
            case "radi": return Math.toRadians(args[0]);

            case "fac": double ret = 1; for(int x = 1; x <= (int)args[0]; x++) ret *= x; return ret;
            default:
                Class[] argTypes = {Factors.class, Node.class};
                Method execMethod = Class.forName(FILE).getDeclaredMethod("exec",argTypes);
                Object[] argListForInvokedExec = new Object[]{pFactors, pNode};
                return (double)execMethod.invoke(null, argListForInvokedExec);
            }
      }

}