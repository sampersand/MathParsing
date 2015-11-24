public class summation {
    public static String help = "Add up numbers from START to END with step STEP";
    public static String args = "([START], END, [STEP])";
    // @Override
    /** 
     * Summation from START to END, with step STEP
     * Params: ([START],END,[STEP])
     * if START is omitted, then 0 is used in its place
     * if STEP is omitted, 1 is used in its place
     */
    public static double exec(Factors pFactors, Node pNode) 
    throws ClassNotFoundException, NoSuchMethodException,
    java.lang.reflect.InvocationTargetException, IllegalAccessException,
    InvalidArgsException {
        double[] vals = new double[pNode.size()];
        for(int i = 0; i < vals.length; i++) vals[i] = pFactors.eval(pNode.subNodes.get(i));
        if(vals.length == 0 || vals.length > 3)
            throw new InvalidArgsException("ERROR when parsing summation. Syntax: " + args);
        if(vals.length == 1){ vals = new double[]{0,vals[0],1};}
        if(vals.length == 2){ vals = new double[]{vals[0],vals[1],1};}
        double ret = 0;
        for(double x = vals[0]; x <= vals [1]; x+= vals[2]) ret += x; 
        return ret;
    }
}