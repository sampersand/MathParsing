public class f {

    // @Override
    public static double exec(Factors pFactors, Node pNode) 
    throws ClassNotFoundException, NoSuchMethodException,
    java.lang.reflect.InvocationTargetException, IllegalAccessException {
        System.out.println("FACTORS: " + pNode);
        double[] vals = new double[pNode.size()];
        for(int i = 0; i < vals.length; i++) vals[i] = pFactors.eval(pNode.subNodes.get(i));

        double ret = 1D;
    
        for(double d : vals) ret *= d;
        return ret;
    }
}