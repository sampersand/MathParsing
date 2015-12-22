package Math;
import Math.Exception.NotDefinedException;
public class Declare {
    public Declare(){
        throw new NotDefinedException("Cannot instatiate Declare!");
    }
    public static void decl(boolean expr){
        decl(expr, "");
    }
    public static void decl(boolean expr, String str){
        decl(expr, new IllegalArgumentException(str));
    }
    public static void decl(boolean expr, RuntimeException excp){
        if(!expr)
            throw excp;
    }
}