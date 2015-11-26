package Math.Equation.Exception;
public class NotDefinedException extends MathException {
    public NotDefinedException(){
        super();
    }
    public NotDefinedException(String cause){
        super(cause);
    }
}