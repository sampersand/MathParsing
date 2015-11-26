import java.lang.reflect.*;
public class CustomFunction extends Function {
    public CustomFunction(){
        this(null);
    }
    public CustomFunction(String pVal){
        super(pVal);
    }
    public double exec(Factors pFactors, Node pNode) throws NotDefinedException{
        try{
            Class[] argTypes = {Factors.class, Node.class};
            Method execMethod = Class.forName(fName).getDeclaredMethod("exec",argTypes);
            Object[] argListForInvokedExec = new Object[]{pFactors, pNode};
            System.out.println("B");
            return (double)execMethod.invoke(null, argListForInvokedExec);
        } catch (ClassNotFoundException err) {
            System.err.println("[ERROR] A ClassNotFoundException happened when attempting to execute a " +
                               "custom method (File Name: " + fName + "): " + err.getCause());
        } catch (NoSuchMethodException err) {
            System.err.println("[ERROR] A NoSuchMethodException happened when attempting to execute a " +
                               "custom method (File Name: " + fName + "): " + err.getCause());
        } catch (InvocationTargetException err) {
            System.err.println("[ERROR] A InvocationTargetException happened when attempting to execute a " +
                               "custom method (File Name: " + fName + "): " + err.getCause());
        } catch (IllegalAccessException err) {
            System.err.println("[ERROR] A IllegalAccessException happened when attempting to execute a " +
                               "custom method (File Name: " + fName + "): " + err.getCause());
        } return 0;
    }
}