package Math.Exception;
public class TypeMisMatchException extends MathException {
    public TypeMisMatchException(){
        super();
    }
    public TypeMisMatchException(String cause){
        super(cause);
    }
}