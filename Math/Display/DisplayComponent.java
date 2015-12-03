
package Math.Display;
import Math.Equation.Equation;
import Math.Set.Set;
import Math.Exception.InvalidArgsException;
import Math.Exception.MathException;
import Math.Exception.NotDefinedException;

import java.util.ArrayList;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.Dimension;

import javax.swing.JLabel;
/**
 * The component that is used to graph an equation.
 *
 * @author Sam Westerman
 * @version 1 Sep 29, 2015.
 */
public class DisplayComponent extends JLabel{  

    private Grapher grapher;
    private Equation equation;
    private Set set;
    private Color color;

    /** The element that draws the lines. */
    public Graphics2D drawer;   
 
    public DisplayComponent(){
        throw new InvalidArgsException("You need to have a grapher to base a DisplayComponent class of!");
    }

    /* 
     * I draw the axis.
     */
    public DisplayComponent(Grapher pGrapher){
        this(pGrapher, null, null, Color.BLACK);
    }

    /* 
     * I draw a set
     */
    public DisplayComponent(Grapher pGrapher, Set pSet){
        this(pGrapher, pSet, Color.BLUE);
    }
    public DisplayComponent(Grapher pGrapher, Set pSet, Color pColor){
        this(pGrapher, null, pSet, pColor);
    }

    /*
     * I draw an equation
     */
    public DisplayComponent(Grapher pGrapher, Equation pEquation){
        this(pGrapher, pEquation, Color.BLUE);
    }
    public DisplayComponent(Object... pobj){ throw new NotDefinedException("Define me!");}
    public DisplayComponent(Grapher pGrapher, Equation pEquation, Color pColor){
        this(pGrapher, pEquation, null, pColor);
    }

    private DisplayComponent(Grapher pGrapher, Equation pEquation, Set pSet, Color pColor){
        grapher = pGrapher;
        equation = pEquation;
        set = pSet;
        color = pColor;
        this.createToolTip();
        // setPreferredSize(new Dimension(grapher.components().winBounds()[0], grapher.components().winBounds()[1]));
    }
    
    /** 
     * The function that controls the drawing of the graphics.
     *
     * <p>
     * If ISEQIMAG is true, then both x and y will be
     * incremented.
     * <p>
     * The (X, Y) coords will be:
     * <br>X: The 'real' value of the complex number yielded when X and Y are plugged into EQUATION.
     * <br>Y: The 'imaginary' value of the complex number yielded when X and Y are plugged into EQUATION.
     * The (X, Y) coords will be:
     * <br>X: The x value (which is being incremented).
     * <br>Y: The resulting value when X is plugged into theEQUATION.
     *
     * @param pGraphics          The graphics input that will be used to draw. Assumed to be Graphics2D.
     */
    public void paintComponent(Graphics pGraphics) throws MathException, NotDefinedException {
        if(!(pGraphics instanceof Graphics2D))
            throw new MathException("Uh, Idek how this happened, but g has to be a Graphics2D...");
        drawer = (Graphics2D) pGraphics;
        drawer.setColor(color);

        double[] dispBounds = grapher.components().dispBounds();

        if(set != null){
            if(set.arr2 == null){
                throw new NotDefinedException("Set '" + set + "' doesn't have the first Array! can't graph it.");
            } else if(set.arr2 == null){
                throw new NotDefinedException("Set '" + set + "' doesn't have the second Array! can't graph it.");
            } 
            set.verifySize();
            for(int x = 0; x < set.arr2.length - 1; x++){
                drawp(set.arr1[x], set.arr2[x]);
                // drawl(set.arr1[x], set.arr2[x], set.arr1[x+1], set.arr2[x+1]);
            }
        } else if(equation != null){
            double cStep = grapher.components().cStep();
            for(double x = dispBounds[0]; x < dispBounds[2]; x += cStep){
                throw new NotDefinedException("define me!");
                // try{
                    // drawl(x, equation.eval(x, "x"), x + cStep, equation.eval(x + cStep, "x"));
                // } catch (NotDefinedException err){
                    // drawl(x, equation.eval(x, "y"), x + cStep, equation.eval(x + cStep, "y"));
                // }
            }
        }
        else{
            drawl(0, dispBounds[1], 0, dispBounds[3]); //axis.
            drawl(dispBounds[0], 0, dispBounds[2], 0); //axis.
        }
    }
 
    /**
     * Makes drawing points a lot easier by modifying the coordinantes to be more like the x/y plane.
     * Without this, <code>(0, 0)</code> is in the upper left corner of the window, and positive 
     * y is downards.
     *
     * @param x          The x coordinant of the point to draw.
     * @param y          The y coordinant of the point to draw.
     */
    private void drawp(double x, double y) {
       double[] xy=fix(x, y);
       drawer.drawOval((int)xy[0] - 5, (int) xy[1] - 5, 10, 10); // width, height
    }
 
    private void drawl(double x, double y, double X, double Y) {
       double[] xy=fix(x, y);
       double[] XY=fix(X, Y);
       drawer.drawLine((int) xy[0], (int)xy[1], (int)XY[0], (int)XY[1]); // width, height
    }
  
    private double[] fix(double x, double y){
        return grapher.components().fix(x,y);
    }
    public String toString(){
        return "Display of " + (set == null ? equation == null ? "Axis" : equation : set);
    }
 
}
 