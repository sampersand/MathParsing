package Math.Exceptions;
public class DoesntExistException extends MathException {
    public DoesntExistException(){
        super();
    }
    public DoesntExistException(String cause){
        super(cause);
    }
}