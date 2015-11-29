package Math.Set;
import Math.Equation.Equation;
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

import javax.swing.JComponent;

/**
 * The component that is used to graph an equation.
 *
 * @author Sam Westerman
 * @version 1 Sep 29, 2015.
 */
public class Display extends JComponent{  

    public ArrayList<Set> sets;
    public ArrayList<Equation> equations;
    public GraphComponents components;
    /** 
     * The element that draws the lines.
     */
    public Graphics2D drawer;   
 
    public Display(){
        this(null, null);
    }
    
    public Display(Set pSet){
        this(new ArrayList<Set>(){{add(pSet);}}, null);
    }

    public Display(Equation pEquation){
        this(null, new ArrayList<Equation>(){{add(pEquation);}});
    }

    public Display(GraphComponents pComponent){
        this(null, null, pComponent);
    }

    public Display(ArrayList<Set> pSets, ArrayList<Equation> pEquations) {
        this(pSets, pEquations, new GraphComponents());
    }

    public Display(ArrayList<Set> pSets, ArrayList<Equation> pEquations, GraphComponents pComponent) {
        sets = pSets;
        equations = pEquations;
        components = pComponent;
        this.createToolTip(); 
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
     * The (<code>X</code>, <code>Y</code>) coords will be:
     * <ul><code>X</code>: The <code>x</code> value (which is being incremented). </ul>
     * <ul><code>Y</code>: The resulting value when <code>X</code> is plugged into the
     * <code>EQUATION</code>. </ul>
     *
     * @param g          The graphics input that will be used to draw. Assumed to be 
     *                   <code>Graphics2D</code>.
     */
    public void paintComponent(Graphics g) throws MathException, NotDefinedException {
        if(!(g instanceof Graphics2D))
            throw new MathException("Uh, Idek how this happened, but g has to be a Graphics2D...");
        double[] dispBounds = components.dispBounds();
        drawer = (Graphics2D) g;
        if(sets != null){
            for(Set set : sets){
                if(set == null){
                    throw new NotDefinedException("Set '" + set + "' is null!");
                } else if(set.matr == null){
                    throw new NotDefinedException("Set '" + set + "' doesn't have the first Matrix! can't graph it.");
                } else if(set.matr2 == null){
                    throw new NotDefinedException("Set '" + set + "' doesn't have the second Matrix! can't graph it.");
                } else if(set.matr.length != set.matr2.length){
                    throw new NotDefinedException("The first Matrix's length and the Second Matrix's length of Set '"
                                                  + set + "' aren't equal!");
                }
                for(int x = 0; x < set.matr.length; x++){
                    drawp(set.matr[x], set.matr2[x]);
                }
            }
        }
        if(equations != null){
            double cStep;
            for(int i = 0; i < equations.size(); i++){
                Equation equation = equations.get(i);
                if(equation == null){
                    throw new NotDefinedException("Can't graph equation '" + equation + "' because it is null!");
                }
                cStep = components.cStep();
                for(double x = dispBounds[0]; x < dispBounds[2]; x += cStep){
                    drawl((double)x, Set.pred(x, equation, "y"), x + cStep, Set.pred(x + cStep, equation, "y"), i);
                }
            }
        }
        drawl(0, dispBounds[1], 0, dispBounds[3], Color.BLACK); //axis.
        drawl(dispBounds[0], 0, dispBounds[2], 0, Color.BLACK); //axis.
    }
 
    /**
     * Makes drawing points a lot easier by modifying the coordinantes to be more like the x/y plane.
     * Without this, <code>(0, 0)</code> is in the upper left corner of the window, and positive 
     * y is downards.
     *
     * @param x          The x coordinant of the point to draw.
     * @param y          The y coordinant of the point to draw.
     * @param color      The color of the point.
     */
    private void drawp(double x, double y, Color color) {
       drawer.setColor(color);
       double[] xy=fix(x, y);
       drawer.drawOval((int)xy[0] - 5, (int) xy[1] - 5, 10, 10); // width, height
    }
 
    private void drawl(double x, double y, double X, double Y, Color color) {
       drawer.setColor(color);
       double[] xy=fix(x, y);
       double[] XY=fix(X, Y);
       drawer.drawLine((int) xy[0], (int)xy[1], (int)XY[0], (int)XY[1]); // width, height
    }

    private void drawl(double x, double y, double X, double Y, int i) {
        Color[] colors = new Color[]{Color.BLUE, Color.RED, Color.YELLOW, Color.GREEN};
        drawl(x, y, X, Y, colors[i%colors.length]);
    }

    private void drawl(int x, int y, int X, int Y, Color color) {
        drawl((double)x, (double)y, (double)X, (double)Y, color);
    }
 
 
    private double[] fix(double x, double y){
        return components.fix(x,y);
    }
    /**
     * Overloaded version of drawp with ints
     */
     private void drawp(int x, int y){
       drawp( (double) x, (double) y);
     }
     private void drawp(double x, double y){
       drawp(x, y, Color.RED);
    }   
 
}
 
