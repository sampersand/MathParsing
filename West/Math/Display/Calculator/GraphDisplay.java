package West.Math.Display.Calculator;

import West.Math.MathObject;
import West.Math.Exception.NotDefinedException;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import javax.swing.JComponent;

/**
 * The component that is used to draw the Axis and the names of the equations.
 *
 * @author Sam Westerman
 * @version 0.85
 * @since 0.3
 * @depreciated
 */
public class GraphDisplay extends JComponent implements MathObject {  
    /**
     * The equations to be displayed
     */
    protected final String[] EQUATIONS; 

    /**
     * The starting and ending points of <code>x</code>.
     */
    protected final double[] EQUATION_BOUNDS;

    /**
     * The physical amount of pixels the window is wide  and tall.
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
     * The colors for each equation
     */
    protected final Color[] COLORS; 


    /**
     * The element that draws the lines.
     */
    protected Graphics2D drawer;

    /**
     * Overloaded initializer, without <code>isImag</code>, that takes non-arrays for everything.
     */
    public GraphDisplay(String[] equations, double eqBnd, double step, int winBnd, int dispBnd, Color[] colors) {
        this(equations, new double[] { eqBnd }, new double[] { step }, new int[] { winBnd }, new int[] { dispBnd },
             false, colors);
    }

    /**
     * Overloaded initializer, without <code>isImag</code>, that takes arrays for only 
     * <code>winBnd</code> and <code>dispBnd</code>. 
     */
    public GraphDisplay(String[] equations, double eqBnd, double step, int[] winBnd, int[] dispBnd, Color[] colors) {
        this(equations, new double[] { eqBnd }, new double[] { step }, winBnd, dispBnd, false, colors);
    }

    /**
     * The class that controls the drawing of the graph.
     * 
     * @param equations     The equation that will be graphed.
     * @param eqBnd         The starting and ending points for the program to graph.
     * @param winBnd        The minimum and maximum (for x and y) values that will be displayed.
     * @param dispBnd       The hight and width of the physical window. Might be removed if there is 
     *                      a function to find this w/o the input. Used to adjust coords of points.
     * @param color         The color that the line will be painted in.
     * @throws ArrayIndexOutOfBoundsException       thrown if the input array's lengths aren't 1, 2,
     *                                              and 4 (except steps. steps is only 1 and 2).
     */
    public GraphDisplay(String[] equations, double[] eqBnd, double[] steps, int[] winBnd, int[] dispBnd, boolean isImag,
                        Color[] colors) throws ArrayIndexOutOfBoundsException {
        this.EQUATIONS = equations;
        this.COLORS = colors;
        if (eqBnd.length == 1) {
            this.EQUATION_BOUNDS = new double[] { -eqBnd[0], eqBnd[0], -eqBnd[0], eqBnd[0] };
        } else if (eqBnd.length == 2) {
            this.EQUATION_BOUNDS = new double[] { -eqBnd[0], eqBnd[0], -eqBnd[1], eqBnd[1] };
        } else if (eqBnd.length == 4) {
            this.EQUATION_BOUNDS = eqBnd;
        } else { 
            throw new ArrayIndexOutOfBoundsException("Cannot instatiate GraphDisplay! The length of 'eqBnd' needs " +
                                                     "to be 1, 2 or 4.");
        }

        if (winBnd.length == 1) {
            this.WINDOW_BOUNDS = new int[] { -winBnd[0], winBnd[0], -winBnd[0], winBnd[0] };
        } else if (winBnd.length == 2) {
            this.WINDOW_BOUNDS = new int[] { -winBnd[0], winBnd[0], -winBnd[1], winBnd[1] };
        } else if (winBnd.length == 4) {
            this.WINDOW_BOUNDS = winBnd;
        } else {
            throw new ArrayIndexOutOfBoundsException("Cannot instatiate GraphDisplay! The length of winBnd needs t" +
                                                     "o be 1, 2 or 4.");
        }

        if (dispBnd.length == 1) {
            this.DISPLAY_BOUNDS = new int[] { -dispBnd[0], dispBnd[0], -dispBnd[0], dispBnd[0] };
        } else if (dispBnd.length == 2) {
            this.DISPLAY_BOUNDS = new int[] { -dispBnd[0], dispBnd[0], -dispBnd[1], dispBnd[1] };
        } else if (dispBnd.length == 4) {
            this.DISPLAY_BOUNDS = dispBnd;
        } else {
            throw new ArrayIndexOutOfBoundsException("Cannot instatiate GraphDisplay! The length of dispBnd needs " +
                                                     "to be 1, 2 or 4.");
        }

    }

    /**
     * The function that draws all the components, barring the equations themselves. 
     * More specifically, it draws the equations names in their correct colors, the box around them,
     * and the axis.
     *
     * @param g          The graphics input that will be used to draw. Assumed to be 
     *                   <code>Graphics2D</code>.
     */
    public void paintComponent(Graphics g) {  
        assert g instanceof Graphics2D;
        drawer = (Graphics2D) g;
        drawAxis();
        int largestSize = 0;
        for(String eq : this.EQUATIONS) {
            if(eq.length()>largestSize) {
                largestSize=eq.length();
            }
        }
        drawer.drawRect(13,5,(largestSize)*10,5+15*this.EQUATIONS.length); //x, y, width, height
        drawer.setColor(Color.WHITE);
        drawer.fillRect(13,5,(largestSize)*10,5+15*this.EQUATIONS.length);
        drawer.setColor(Color.BLACK);

        for(int pos = 0; pos < this.EQUATIONS.length; pos++) {
            drawName(pos);
        }
    }

    /**
     * Draws the X and Y (or Real / imag) axis with the tick marks.
     * The tick marks are one 40th the size of the absolute value of the <code>y</code> bounds
     * (for the x axis. It is the <code>x</code> bounds for the y axis).
     * <p>
     * They are spaced one 140th the size of the absolute value of the <code>x</code> bounds
     * (for the x axis. It is the <code>y</code> bounds for the y axis).
     */
    private void drawAxis() {
        drawer.setStroke(new BasicStroke(2));
        drawl(DISPLAY_BOUNDS[0], 0, DISPLAY_BOUNDS[1], 0); // x
        drawl(0, DISPLAY_BOUNDS[2], 0, DISPLAY_BOUNDS[3]); // y
        drawer.setStroke(new BasicStroke(1));
        double eqyb = (Math.abs(EQUATION_BOUNDS[2]) + Math.abs(EQUATION_BOUNDS[3]));
        double eqxb = (Math.abs(EQUATION_BOUNDS[0]) + Math.abs(EQUATION_BOUNDS[1]));

        for(double x = EQUATION_BOUNDS[0] - eqxb / 40; x < EQUATION_BOUNDS[1]; x += eqxb / 40) {
            drawl(x, -eqyb / 140, x, eqyb / 140);
        }
        for(double y = EQUATION_BOUNDS[0] - eqyb / 40; y < EQUATION_BOUNDS[1]; y += eqyb / 40) {
            drawl(-eqxb / 140, y, eqxb / 140, y);
        }      
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

    /**
     * Draws the name of <code>this.EQUATION</code>.
     * @param index      The index of the string equation in Colors.
     */
    private void drawName(int index) {
        this.drawer.setColor(this.COLORS[index%this.COLORS.length]);
        this.drawer.drawString(this.EQUATIONS[index],15,20+15*index);
    }

    @Override
    public String toString() {
        throw new NotDefinedException();
    }

    @Override
    public String toFancyString(int idtLvl) {
        throw new NotDefinedException();
    }

    @Override
    public String toFullString(int idtLvl) {
        throw new NotDefinedException();
    }

    @Override
    public GraphDisplay copy(){
        return new GraphDisplay(EQUATIONS, EQUATION_BOUNDS, new double[]{0}, WINDOW_BOUNDS,
                                DISPLAY_BOUNDS, false, COLORS);
    }
    
    @Override
    public boolean equals(Object pObj){
        throw new NotDefinedException();
    }
}

