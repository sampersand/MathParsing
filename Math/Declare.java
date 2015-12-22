package Math;
import Math.Exception.*;

/**
 * assert, but isn't removed at compile-time. Use these when the error might be caused by a user, and use 
 * <code>assert</code> when it might be caused by a bug.
 * 
 * @author Sam Westerman
 * @version 0.1
 */
public class Declare {
    public Declare(){
        throw new NotDefinedException("Cannot instatiate Declare!");
    }
    public static void decl(boolean expr){
        decl(expr, "");
    }
    public static void decl(boolean expr, String str){
        decl(expr, new MathException(str));
    }
    public static void declP(boolean expr, String str){
        decl(expr, new IllegalArgumentException(str));
    }
    public static void declND(boolean expr, String str){
        decl(expr, new NotDefinedException(str));
    }
    public static void decl(boolean expr, RuntimeException excp){
        if(!expr)
            throw excp;
    }
}