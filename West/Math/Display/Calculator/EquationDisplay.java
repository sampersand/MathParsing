package West.Math.Display.Calculator;

import West.Math.MathObject;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import javax.swing.JComponent;

/**
 * The component that is used to graph an equation. This is depreciated, and will be removed soon
 *
 * @author Sam Westerman
 * @version 0.90
 * @since 0.3
 * @depreciated
 */
public class EquationDisplay extends JComponent implements MathObject {  
    /**
     * Equation that will be passed to <code>CalcWindow</code> to calculate the result.
     */
    protected final String EQUATION; 

    /**
     * True if the equation's result should be expected to be imaginary.
     *  If it is, then the real / imaginary axis will be used instead of the x / y axis.
     */
    protected final boolean ISEQIMAG;

    /**
     * The amount that <code>x</code> (or <code>y</code> as well if <code>ISEQIMAG</code> 
     * is <code>true</code>) will be incremented when graphing. 
     */
    protected final double[] STEPS; 

    /**
     * The starting and ending point of <code>x</code> (and <code>y</code> if <code>ISEQIMAG</code>
     * is <code>true</code>).
     */
    protected final double[] EQUATION_BOUNDS;

    /**
     * The physical amount of pixels the window is wise and tall.
     * The array will look like the following: <code>(min X, max X, min Y, max Y)</code>
     */
    protected final int[] WINDOW_BOUNDS;

    /**
     * The min and max x/y values used for display.
     * Increasing them incrases the zoom level, and shifting them a certain direction shifts the 
     * "window" that direction.
     * <p>
     * The array will look like the following: <code>(min X, max X, min Y, max Y)</code>.
     */
    protected final int[] DISPLAY_BOUNDS;

    /**
     * The color that the graph will be drawn in
     */
    protected final Color COLOR; 


    /**
     * The element that draws the lines.
     */
    protected Graphics2D drawer;

    /**
     * Overloaded initializer, without <code>isImag</code>, that takes non-arrays for everything.
     */
    public EquationDisplay(String eq, double eqBnd, double step, int winBnd, int dispBnd,  Color color) {
        this(eq, new double[] { eqBnd }, new double[] { step }, new int[] { winBnd }, new int[] { dispBnd }, false,
             color);
    }

    /**
     * Overloaded initializer, without <code>isImag</code>, that takes arrays for only 
     * <code>winBnd</code> and <code>dispBnd</code>. 
     */
    public EquationDisplay(String eq, double eqBnd, double step, int[] winBnd, int[] dispBnd, Color color) {
        this(eq, new double[] { eqBnd }, new double[] { step }, winBnd, dispBnd, false,color);
    }

    /**
     * The class that controls the drawing of the graph.
     * 
     * @param eq            The equation that will be graphed.
     * @param eqBnd         The starting and ending points for the program to graph.
     * @param steps         The amount to incriment <code>x</code> (and <code>y</code> if 
     *                      <code>ISEQImag</code> is true) by when graphing.
     * @param winBnd        The minimum and maximum (for x and y) values that will be displayed.
     * @param dispBnd       The hight and width of the physical window. Might be removed if there is 
     *                      a function to find this w/o the input. Used to adjust coords of points.
     * @param isImag        True if the result is expected to be imaginary. Modifies how graphing is
     *                      done by incrimenting <code>y</code> along with <code>x</code>.
     *                      See <code>paintComponent</code> for details.
     * @param color         The color that the line will be painted in.
     * @throws ArrayIndexOutOfBoundsException       thrown if the input array's lengths aren't 1, 2,
     *                                              and 4 (except steps. steps is only 1 and 2).
     */
    public EquationDisplay(String eq, double[] eqBnd, double[] steps, int[] winBnd, int[] dispBnd, boolean isImag,
                           Color color) throws ArrayIndexOutOfBoundsException {
        this.EQUATION = fixEquation(eq);
        this.ISEQIMAG = isImag;
        this.COLOR=color;

        this.createToolTip(); // I thought these might be interesting, but right now they dont work.
        this.setToolTipText("Equation: "+eq);

        if (eqBnd.length == 1) {
            this.EQUATION_BOUNDS = new double[] { -eqBnd[0], eqBnd[0], -eqBnd[0], eqBnd[0] };
        } else if (eqBnd.length == 2) {
            this.EQUATION_BOUNDS = new double[] { -eqBnd[0], eqBnd[0], -eqBnd[1], eqBnd[1] };
        } else if (eqBnd.length == 4) {
            this.EQUATION_BOUNDS = eqBnd;
        } else { 
            throw new ArrayIndexOutOfBoundsException("Cannot instantiate EquationDisplay! The length of 'eqBnd' needs" +
                                                     " to be 1, 2 or 4.");
        }

        if (steps.length == 1) {
            this.STEPS = new double[] { steps[0], steps[0] };
        } else if (steps.length == 2) {
            this.STEPS = steps;
        } else {
            throw new ArrayIndexOutOfBoundsException("Cannot instantiate EquationDisplay! The length of 'steps' needs" +
                                                     " to be 1 or 2.");
        }

        if (winBnd.length == 1) {
            this.WINDOW_BOUNDS = new int[] { -winBnd[0], winBnd[0], -winBnd[0], winBnd[0] };
        } else if (winBnd.length == 2) {
            this.WINDOW_BOUNDS = new int[] { -winBnd[0], winBnd[0], -winBnd[1], winBnd[1] };
        } else if (winBnd.length == 4) {
            this.WINDOW_BOUNDS = winBnd;
        } else {
            throw new ArrayIndexOutOfBoundsException("Cannot instantiate EquationDisplay! The length of winBnd needs " +
                                                     "to be 1, 2 or 4.");
        }

        if (dispBnd.length == 1) {
            this.DISPLAY_BOUNDS = new int[] { -dispBnd[0], dispBnd[0], -dispBnd[0], dispBnd[0] };
        } else if (dispBnd.length == 2) {
            this.DISPLAY_BOUNDS = new int[] { -dispBnd[0], dispBnd[0], -dispBnd[1], dispBnd[1] };
        } else if (dispBnd.length == 4) {
            this.DISPLAY_BOUNDS = dispBnd;
        } else {
            throw new ArrayIndexOutOfBoundsException("Cannot instantiate EquationDisplay! The length of dispBnd needs" +
                                                     " to be 1, 2 or 4.");
        }
    }

    /**
     * The function that controls the drawing of the graphics.
     *
     * <p>
     * If <code>ISEQIMAG</code> is <code>true</code>, then both <code>x</code> and <code>y</code> will be
     * incremented.
     * <p>
     * The (<code>X</code>, <code>Y</code>) coords will be:
     * <ul><code>X</code>: The 'real' value of the complex number yielded when <code>X</code>
     *        and <code>Y</code> are plugged into <code>EQUATION</code>. </ul>
     * <ul><code>Y</code>: The 'imaginary' value of the complex number yielded when
     *        <code>X</code> and <code>Y</code> are plugged into <code>EQUATION</code>. </ul>
     * <p>
     * If <code>ISEQIMAG</code> is <code>false</code>, then only <code>x</code> will be incremented.
     * <p>
     * The (<code>X</code>, <code>Y</code>) coords will be:
     * <ul><code>X</code>: The <code>x</code> value (which is being incremented). </ul>
     * <ul><code>Y</code>: The resulting value when <code>X</code> is plugged into the
     * <code>EQUATION</code>. </ul>
     *
     * @param g          The graphics input that will be used to draw. Assumed to be 
     *                   <code>Graphics2D</code>.
     */
    public void paintComponent(Graphics g) {  
        assert g instanceof Graphics2D;
        drawer = (Graphics2D) g;
        drawer.setColor(this.COLOR);
        double x = EQUATION_BOUNDS[0]-STEPS[0]; 
        double y = 0;

        if (ISEQIMAG) {
            while (x <= EQUATION_BOUNDS[1]) {
                x += STEPS[0];
                y = -EQUATION_BOUNDS[2];
                while (y <= EQUATION_BOUNDS[3]) {
                    y += STEPS[1];
                    // TODO: Make this work
                    drawl( (int) x, (int) y, (int) x, (int) y);
                }
            }
        } else {
            double X; // The other end of the line; X is "x+step".
            double Y; // The other end of the line. Y is the equation with X plugged into it.
            while (x <= EQUATION_BOUNDS[1]) {
                y = Double.parseDouble(CalcWindow.getResult(new String(EQUATION).replaceAll("x", "(" + x + ")")));
            X = x + STEPS[0];
            Y = Double.parseDouble(CalcWindow.getResult(new String(EQUATION).replaceAll("x", "(" + X + ")")));
            drawl(x, y, X, Y);
            x += STEPS[0];
            }
        }
    }

    /**
     * This fixes the input equation to make it usable be the computer.
     * <b>Warning</b>: This is fairly unstable. It's not at all ready for use; Therefor, all
     * equations passed to it should start with <code>=@</code> and be JavaScript readable.
     *
     * @param eq         The equation that was passed, which will fixed. If the equation starts with
     *                   <code>=@</code>, then it will execute the exact string following.
     * @return           The JavaScript-executable version of the string
     */
    public String fixEquation(String eq) {
        // This could always have work done to it.
        if(eq.charAt(1) == '@') { // if the equation is '=@...', then return '=...' without formatting.
            return "=" + eq.substring(2);
        }
        eq = eq.replaceAll("pi","Math.PI");                         //pi
        eq = eq.replaceAll("\u03C0","Math.PI");                     //π
        eq = eq.replaceAll("\u221A","sqrt");                        //√
        eq = eq.replaceAll("sqrt\\((.*)\\)","Math.sqrt($1)");       //sqrt
        eq = eq.replaceAll("sqrt([^(])","Math.sqrt($1)");           //sqrt
        eq = eq.replaceAll("(?<!Math\\.)\\b(e|E)\\b","Math.E");     //e
        eq = eq.replaceAll("log([^(]|\\(.*)","Math.log10($1)");     //log
        eq = eq.replaceAll("ln([^(]|\\(.*)","Math.log($1)");        //ln
        eq = eq.replaceAll("(\\d)x", "$1*x");                       //#X -> #*X
        eq = eq.replaceAll(
                            "((?:.*[+-/^ *.])|(?:^=))" + // The beginning of the expression can be '+-/^ *' or '^='.
                            "(\\((?:.*)\\)|.*)" + // The term is either surrounded by parenthesis or not. 
                            "(?:\\*\\*|\\^)"    + // The exponent can either be '**' or '^'.
                            "(\\((?:.*)\\)|.*)" + // The power is either surrounded by parenthesis or not.
                            "([+-/^ *.]|(?:$))"    // The end of the expression is either '+-/^ *' or '$'.
                            ,"$1Math.pow($2,$3)"); 

        for(String trig : new String[] { "cos", "sin", "tan", "csc", "sec", "cot"} ) {
            eq = eq.replaceAll(trig + "\\((.*)\\)","Math." + trig + "($1)");
            eq = eq.replaceAll(trig + "([^hH(])","Math." + trig + "($1)");
        }

        return eq;
    }


    /**
     * Makes drawing lines a lot easier by modifying the coordinantes to be more like the x/y plane.
     * Without this, <code>(0, 0)</code> is in the upper left corner of the window, and positive 
     * y is downards.
     * <p>
     * With this, <code>(0, 0)</code> is in the centre of the screen, and positive y is upwards.
     *
     * @param x          The x coordinant of the start of the line to draw.
     * @param y          The y coordinant of the start of the line to draw.
     * @param X          The x coordinant of the end of the line to draw.
     * @param Y          The y coordinant of the end of the line to draw.
     *
     */
    private void drawl(double x, double y, double X, double Y) {
        double xSBound = Math.abs(WINDOW_BOUNDS[0]) + Math.abs(WINDOW_BOUNDS[1]);
        double ySBound = Math.abs(WINDOW_BOUNDS[2]) + Math.abs(WINDOW_BOUNDS[3]);
        double xEBound = Math.abs(DISPLAY_BOUNDS[0]) + Math.abs(DISPLAY_BOUNDS[1]);
        double yEBound = Math.abs(DISPLAY_BOUNDS[2]) + Math.abs(DISPLAY_BOUNDS[3]);

        //  |-------1st-Term------|  |-2nd-Term-|
        x =  x * xSBound / xEBound + xSBound / 2; // 1st term is adjusting x/y to fit in the bounds.
        X =  X * xSBound / xEBound + xSBound / 2; // 2nd term is shifting x/y right/down to be centred
        y = -y * ySBound / yEBound + ySBound / 2; // Y is inverted b/c positive y is down for pixels,
        Y = -Y * ySBound / yEBound + ySBound / 2; // and should be upwards for graphing.
        drawer.drawLine( (int) x, (int) y, (int) X, (int) Y);
    }

    /**
     * Overloaded version of drawl with ints
     */
    private void drawl(int x, int y, int X, int Y) {
        drawl( (double) x, (double) y, (double) X, (double) Y);
    }

    @Override
    public String toString() {
        throw new NullPointerException();
    }

    @Override
    public String toFancyString(int idtLvl) {
        throw new NullPointerException();
    }

    @Override
    public String toFullString(int idtLvl) {
        throw new NullPointerException();
    }

    @Override
    public EquationDisplay copy(){
        return new EquationDisplay(EQUATION, EQUATION_BOUNDS, STEPS, WINDOW_BOUNDS, DISPLAY_BOUNDS, ISEQIMAG, COLOR);
    }
    
    @Override
    public boolean equals(Object pObj){
        throw new NullPointerException();
    }
}

