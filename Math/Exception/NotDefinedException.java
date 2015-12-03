package Math.Exception;
public class NotDefinedException extends MathException {
    public NotDefinedException(){
        super("Define me!");
    }
    public NotDefinedException(String cause){
        super(cause);
    }
}