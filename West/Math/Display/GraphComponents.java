package West.Math.Display;

import West.Math.MathObject;
import West.Math.Exception.NotDefinedException;

/**
 * Keeps track of different thigns about graphing - like window sizes
 * 
 * @author Sam Westerman
 * @version 0.85
 * @since 0.2
 */
public class GraphComponents implements MathObject {

    /** TODO: JAVADOC */
    public static final GraphComponents TRIG = new GraphComponents(new int[]{1250 ,750}, 
             new double[]{ -3 * Math.PI, -2, 3 * Math.PI, 2}, Math.PI/12);

    /**
     * The physical amount of pixels the window is wide and tall.
     * The array will look like the following: <code>(X, Y)</code>
     */
    protected int[] winBounds;

    /**
     * The min and max x/y values used for display.
     * Increasing them incrases the zoom level, and shifting them a certain direction shifts the 
     * "window" that direction.
     * <p>
     * The array will look like the following: <code>(min X, min Y, max X, max Y)</code>.
     */
    protected double[] dispBounds;
 
    /**
     * The amount of lines that will be drawn. "step" is a bit of a misnomer, but I couldnt think of anything better.
     */
    protected double step;

    /**
     * TODO: JAVADOC
     */
    public GraphComponents() {

        //TODO: JAVADOC

        this(new int[]{1250, 750}, new double[]{-10, -10, 10, 10}, 250);
    }
    /**
     * TODO: JAVADOC
     */
    public GraphComponents(int[] pWinBounds, double[] pDispBounds, double pStep) throws IllegalArgumentException{

        //TODO: JAVADOC

        if(pWinBounds.length != 2)
            throw new IllegalArgumentException("Cannot instantiate GraphComponents! pWinBounds must be in format (X, Y)!");
        if(pWinBounds[0] <= 0  || pWinBounds[1] <= 0)
            throw new IllegalArgumentException("Cannot instatiate GraphComponents! Both the X and Y components of "+
                                           "pWinBounds need to be positive!");
        if(pDispBounds.length != 4)
            throw new IllegalArgumentException("Cannot instatiate GraphComponents! pDispBounds needs to be in format "+
                                           "(min X, min Y, max X, max Y)!");
        winBounds = pWinBounds;
        dispBounds = pDispBounds;
        step = pStep;

    }
    /** TODO: JAVADOC */
    public void setWinBounds(int[] pBounds) { winBounds = pBounds; }

    /** TODO: JAVADOC */
    public void setDispBounds(double[] pBounds) { dispBounds = pBounds; }

    /** TODO: JAVADOC */
    public void setDispBounds(double mx, double my, double Mx, double My) { dispBounds = new double[]{mx, my, Mx, My};}
    /** TODO: JAVADOC */
    public void setStep(double pStep) { step = pStep; }

    /** TODO: JAVADOC */
    public int[] winBounds() { return winBounds; }

    /** TODO: JAVADOC */
    public double[] dispBounds() { return dispBounds; }

    /** TODO: JAVADOC */
    public double step() { return step; }

    /** TODO: JAVADOC */
    public double cStep() { return (dispBounds[2] - dispBounds[0]) / step;}

    /** TODO: JAVADOC */
    public double[] fix(double x, double y) {
        return fix(x, y, dispBounds[0],  dispBounds[1],  dispBounds[2], dispBounds[3],  winBounds[0],  winBounds[1]);
    }

    /** TODO: JAVADOC */
    public static double[] fix(double x, double y, double minX, double minY, double maxX,
                               double maxY, double winX, double winY) {
            return new double[]{ (x - minX) / (maxX - minX) * winX,
                                 (1 - (y - minY) / (maxY - minY)) * winY};

    }

    @Override
    public String toString() {
        assert winBounds.length == 2;
        assert dispBounds.length == 4;
        return "GraphingComponent: winBounds = [" + winBounds[0] + ", " + winBounds[1] + "], dispBounds = [" + 
            dispBounds[0] + ", " + dispBounds[1] + ", " + dispBounds[2] + ", " + dispBounds[3] + "], step = " + step +
            " (cStep = " + cStep() + ")";
    }

    @Override
    public String toFancyString(int idtLvl) {
        assert winBounds.length == 2;
        assert dispBounds.length == 4;
        String ret = indent(idtLvl) + "GraphComponents:\n";
        ret += indent(idtLvl + 1) + "Window Bounds (X, Y) = [" + winBounds[0] + ", " + winBounds[1] + "]\n";
        ret += indent(idtLvl + 1) + "Display Bounds (x, y, X, Y) = [" + dispBounds[0] + ", " + dispBounds[1] + ", " +
               dispBounds[2] + ", " + dispBounds[3] + "]\n";
        ret += indent(idtLvl + 1) + "Step = " + step + " (cStep = " + cStep() + ")";
        return ret;
    }

    @Override
    public String toFullString(int idtLvl) {
        assert winBounds.length == 2;
        assert dispBounds.length == 4;
        String ret = indent(idtLvl) + "GraphComponents:\n";
        ret += indent(idtLvl + 1) + "Window Bounds (X, Y):\n" + indent(idtLvl + 2) + "[" + winBounds[0] + ", " +
               winBounds[1] + "]\n";
        ret += indent(idtLvl + 1) + "Display Bounds (x, y, X, Y):\n" + indent(idtLvl + 2) + "[" + dispBounds[0] + ", " +
               dispBounds[1] + ", " + dispBounds[2] + ", " + dispBounds[3] + "]\n";
        ret += indent(idtLvl + 1) + "Step:\n" + indent(idtLvl + 2) + "\n";
        ret += indent(idtLvl + 2) + "(cStep: " + cStep() + ")";
        return ret;
    }

    @Override
    public GraphComponents copy(){
        return new GraphComponents(winBounds, dispBounds, step);
    }

    @Override
    public boolean equals(Object pObj){
        throw new NotDefinedException();
    }
}