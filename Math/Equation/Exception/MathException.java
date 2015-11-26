package Math.Equation.Exception;
public class MathException extends RuntimeException {
    public MathException(){
        super();
    }
    public MathException(String cause){
        super(cause);
    }
}