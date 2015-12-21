package Math.Display;

import Math.MathObject;
import Math.Equation.Equation;
import Math.Equation.EquationSystem;
import Math.Set.Set;
import Math.Exception.MathException;
import Math.Exception.NotDefinedException;

import java.util.ArrayList;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;


import javax.swing.JLabel;
/**
 * The component that is used to graph an equation.
 *
 * @author Sam Westerman
 * @version 0.67
 * @since 0.2
 */
public class DisplayComponent extends JLabel implements MathObject {  

    /** TODO: JAVADOC */
    protected Grapher grapher;

    /** TODO: JAVADOC */
    protected Equation equation;

    /** TODO: JAVADOC */
    protected EquationSystem equationsys;

    /** TODO: JAVADOC */
    protected Set set;

    /** TODO: JAVADOC */
    protected Color color;

    /** The element that draws the lines. */
    public Graphics2D drawer;   
 
    /** TODO: JAVADOC */
    public DisplayComponent() {
        throw new IllegalArgumentException("Cannot instantiate an empty DisplayComponent!" +
                                        "A Grapher is required to base a DisplayComponent off of!");
    }

    /* 
     * I draw the axis.
     * TODO: JAVADOC
     */
    public DisplayComponent(Grapher pGrapher) {
        this(pGrapher, null, null, null, Color.BLACK);
    }

    /* 
     * I draw a set
     * TODO: JAVADOC
     */
    public DisplayComponent(Grapher pGrapher,
                            Set pSet) {
        this(pGrapher, pSet, Color.BLUE);
    }

    /** TODO: JAVADOC */
    public DisplayComponent(Grapher pGrapher,
                            Set pSet,
                            Color pColor) {
        this(pGrapher, null, null, pSet, pColor);
    }

    /*
     * I draw an equation
     * TODO: JAVADOC
     */
    public DisplayComponent(Grapher pGrapher,
                            Equation pEquation,
                            final EquationSystem pEqSys) {
        this(pGrapher, pEquation, pEqSys, Color.BLUE);
    }

    /** TODO: JAVADOC */
    public DisplayComponent(Grapher pGrapher,
                            Equation pEquation,
                            final EquationSystem pEqSys,
                            Color pColor) {
        this(pGrapher, pEquation, pEqSys, null, pColor);
    }

    /** TODO: JAVADOC */
    private DisplayComponent(Grapher pGrapher,
                            Equation pEquation,
                            final EquationSystem pEqSys,
                            Set pSet,
                            Color pColor) {
        grapher = pGrapher;
        equation = pEquation;
        equationsys = pEqSys;
        if(equationsys!=null){
            equationsys = equationsys.copy();
            equationsys.equations().add(0,equation);
        }
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
        assert pGraphics instanceof Graphics2D;
        // if(!(pGraphics instanceof Graphics2D))
        //     throw new MathException("Uh, Idek how this happened, but g has to be a Graphics2D...");
        drawer = (Graphics2D) pGraphics;
        drawer.setColor(color);

        double[] dispBounds = grapher.components().dispBounds();

        if(set != null) {
            if(set.arr2() == null) {
                throw new NotDefinedException("Set '" + set + "' doesn't have the first Array! Can't graph it.");
            } else if(set.arr2() == null) {
                throw new NotDefinedException("Set '" + set + "' doesn't have the second Array! Can't graph it.");
            } 
            set.verifySize();
            for(int x = 0; x < set.arr2().length; x++) {
                drawp(set.arr1()[x], set.arr2()[x]);
                // drawl(set.arr1()[x], set.arr2()[x], set.arr1()[x+1], set.arr2()[x+1]);
            }
        } else if(equation != null) {
            double cStep = grapher.components().cStep();
            for(double x = dispBounds[0]; x < dispBounds[2]; x += cStep) {
                drawl(x,
                      equationsys.eval("y",new EquationSystem().add("x = " + (x < 0 ? "0 " + x : x))), 
                      x + cStep,
                      equationsys.eval("y", new EquationSystem().add("x = " +
                            (x + cStep < 0 ? "0 " + (x + cStep) : x + cStep))));
            }
        }
        else {
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
 
    /** TODO: JAVADOC */
    private void drawl(double x, double y, double X, double Y) {
       double[] xy=fix(x, y);
       double[] XY=fix(X, Y);
       drawer.drawLine((int) xy[0], (int)xy[1], (int)XY[0], (int)XY[1]); // width, height
    }
  
    /** TODO: JAVADOC */
    private double[] fix(double x, double y) {
        return grapher.components().fix(x,y);
    }

    @Override
    public String toString() {
        return "Display of " + (set == null ? equation == null ? "Axis" : equation : set);
    }
 
    @Override
    public String toFancyString(int idtLvl) {
        String ret = indent(idtLvl) + "DisplayComponent:";
        ret += "\n" + indent(idtLvl + 1) + "EquationSystem for Graphing:\n";
        ret += equationsys == null ? indent(idtLvl + 2) + "null" : equationsys.toFancyString(idtLvl + 2);
        ret += "\n" + indent(idtLvl + 1) + "Equation to Graph:\n";
        ret += equation == null ? indent(idtLvl + 2) + "null" : equation.toFancyString(idtLvl + 2);

        ret += "\n" + indent(idtLvl + 1) + "Set to Graph:\n";
        ret += set == null ? indent(idtLvl + 2) + "null" : set.toFancyString(idtLvl + 2);

        ret += "\n" + indent(idtLvl + 1) + "Color:\n" + indent(idtLvl + 2) + color;
        return ret;
    }

    @Override
    public String toFullString(int idtLvl) {
        String ret = indent(idtLvl) + "DisplayComponent:";
        ret += "\n" + indent(idtLvl + 1) + "EquationSystem for Graphing:\n";
        ret += equationsys == null ? indent(idtLvl + 2) + "null" : equationsys.toFullString(idtLvl + 2);

        ret += "\n" + indent(idtLvl + 1) + "Equation to Graph:\n";
        ret += equation == null ? indent(idtLvl + 2) + "null" : equation.toFullString(idtLvl + 2);

        ret += "\n" + indent(idtLvl + 1) + "Set to Graph:\n";
        ret += set == null ? indent(idtLvl + 2) + "null" : set.toFullString(idtLvl + 2);

        ret += "\n" + indent(idtLvl + 1) + "Color:\n" + indent(idtLvl + 2) + color;
        return ret;
    }

    @Override
    public DisplayComponent copy(){
        return new DisplayComponent(grapher, equation, equationsys, set, color);
    }

    @Override
    public boolean equals(Object pObj){
        throw new NotDefinedException();
    }
}