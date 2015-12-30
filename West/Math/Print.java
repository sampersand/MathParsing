package West.Math;

/**
 * A class that handles all printing to the console done by the West.Math.package.
 * 
 * @author Sam Westerman
 * @version 0.76
 * @since 0.1
 */
public class Print {
 

    /** Different levels for what should be printed out */
    public static enum Level {INFO, PRINT, WARNING, ERROR};

    /**
     * Passes <code>objs</code> to {@link #print(Object[],String,Level) the main print function} with the parameters 
     * <code>append = "\n"</code> and <code>pLevel = PRINT</code>.
     * @param objs      All the Objects to print out to the console
     */
    public static void print(Object... objs) {
        print(objs, "\n", Level.PRINT);
    }

    /**
     * Passes <code>objs</code> to {@link #print(Object[],String,Level) the main print function} with the parameters 
     * <code>append = "\n"</code> and <code>pLevel = INFO</code>.
     * @param objs      All the Objects to print out to the console
     */
    public static void printi(Object... objs) {
        print(objs, "\n", Level.INFO);
    }

    /**
     * Passes <code>objs</code> to {@link #print(Object[],String,Level) the main print function} with the parameters 
     * <code>append = "\n"</code> and <code>pLevel = WARNING</code>.
     * @param objs      All the Objects to print out to the console
     */
    public static void printw(Object... objs) {
        print(objs, "\n", Level.WARNING);
    }

    /**
     * Passes <code>objs</code> to {@link #print(Object[],String,Level) the main print function} with the parameters 
     * <code>append = "\n"</code> and <code>pLevel = ERROR</code>.
     * @param objs      All the Objects to print out to the console
     */
    public static void printe(Object... objs) {
        print(objs, "\n", Level.ERROR);
    }

    /**
     * Passes <code>objs</code> to {@link #print(Object[],String,Level) the main print function} with the parameters 
     * <code>append = ""</code> and <code>pLevel = PRINT</code>.
     * @param objs      All the Objects to print out to the console
     */
    public static void printnl(Object... objs) {
        print(objs, "", Level.PRINT);
    }

    /**
     * Passes <code>objs</code> to {@link #print(Object[],String,Level) the main print function} with the parameters 
     * <code>append = ""</code> and <code>pLevel = INFO</code>.
     * @param objs      All the Objects to print out to the console
     */
    public static void printnli(Object... objs) {
        print(objs, "", Level.INFO);
    }

    /**
     * Passes <code>objs</code> to {@link #print(Object[],String,Level) the main print function} with the parameters 
     * <code>append = ""</code> and <code>pLevel = WARNING</code>.
     * @param objs      All the Objects to print out to the console
     */
    public static void printnlw(Object... objs) {
        print(objs, "", Level.WARNING);
    }

    /**
     * Passes <code>objs</code> to {@link #print(Object[],String,Level) the main print function} with the parameters 
     * <code>append = ""</code> and <code>pLevel = ERROR</code>.
     * @param objs      All the Objects to print out to the console
     */
    public static void printnle(Object... objs) {
        print(objs, "", Level.ERROR);
    }

    /**
     * Prints out each value in <code>objs</code> via <code>System.out.println</code> (or System.err.println in the case
     * of <code>pLevel == WARNING || pLevel == ERROR</code> swith <code>[pLevel]</code> before it, and 
     * <code>append</code> afterwards.
     * @param objs      All the objects that 
     * @param append    The string to append to the end; This is "\n" for the normal
     *                  {@link #print(Object...) print functions}, and "" for the
     *                  {@link #printnl(Object...) println functions}.
     * @param pLevel    The {@link #Level} that dictates what and how to print out <code>objs</code>.
     */
    public static void print(Object[] objs,
                             String append,
                             Level pLevel) {
        String ret = pLevel != Level.PRINT ? "[" + pLevel + "] " : "";
        for(int i = 0; i < objs.length; i++) {
            ret += objs[i] + " ";
        }
        if(pLevel == Level.WARNING || pLevel == Level.ERROR) {
            System.err.print(ret.substring(0, ret.length() - (objs.length == 0 ? 0 : 1)) + append);
        } else {
            System.out.print(ret.substring(0, ret.length() - (objs.length == 0 ? 0 : 1)) + append);
        }
    }

}