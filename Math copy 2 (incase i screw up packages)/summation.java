public class summation extends Function{
    public static String help = "Add up numbers from START to END with step STEP";
    public static String args = "([START], END, [STEP])";
    @Override
    /** 
     * Summation from START to END, with step STEP
     * Params: ([START],END,[STEP])
     * if START is omitted, then 0 is used in its place
     * if STEP is omitted, 1 is used in its place
     * @param pFactors      The factors
     * @return The summation of the numbers defined by pNode.
     */
    public static double exec(Factors pFactors, Node pNode)  throws InvalidArgsException {
        double[] vals = new double[pNode.size()];
        for(int i = 0; i < vals.length; i++) vals[i] = pFactors.eval(pNode.subNodes.get(i));
        if(vals.length == 0 || vals.length > 3)
            throw new InvalidArgsException("ERROR when parsing summation. Syntax: " + args);
        if(vals.length == 1) { vals = new double[]{0,vals[0],1};}
        if(vals.length == 2) { vals = new double[]{vals[0],vals[1],1};}
        double ret = 0;
        for(double x = vals[0]; x <= vals [1]; x+= vals[2]) ret += x; 
        return ret;
    }
}