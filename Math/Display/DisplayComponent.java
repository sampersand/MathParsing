package Math.Display;

import Math.MathObject;
import Math.Print;
import Math.Equation.Equation;
import Math.Equation.EquationSystem;
import Math.Set.NumberCollection;
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
 * TODO: MAKE SURE THIS WORKS WITH SETS AND EQUATIONS, AND ANY COMBINATION
 * @author Sam Westerman
 * @version 0.75
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
    protected ArrayList<NumberCollection<Number>> numc;

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
    public DisplayComponent(Grapher pGrapher, ArrayList<NumberCollection<Number>> pNC) {
        this(pGrapher, pNC, Color.BLUE);
    }

    /** TODO: JAVADOC */
    public DisplayComponent(Grapher pGrapher, ArrayList<NumberCollection<Number>> pNC, Color pColor) {
        this(pGrapher, null, null, pNC, pColor);
    }

    /*
     * I draw an equation
     * TODO: JAVADOC
     */
    public DisplayComponent(Grapher pGrapher, Equation pEquation, final EquationSystem pEqSys) {
        this(pGrapher, pEquation, pEqSys, Color.BLUE);
    }

    /** TODO: JAVADOC */
    public DisplayComponent(Grapher pGrapher, Equation pEquation, final EquationSystem pEqSys, Color pColor) {
        this(pGrapher, pEquation, pEqSys, null, pColor);
    }

    /** TODO: JAVADOC */
    private DisplayComponent(Grapher pGrapher, Equation pEquation, final EquationSystem pEqSys,
                             ArrayList<NumberCollection<Number>> pNC, Color pColor) {
        grapher = pGrapher;
        equation = pEquation;
        equationsys = pEqSys;
        if(equationsys != null){
            equationsys = equationsys.copy();
            equationsys.equations().add(0, equation);
        } 
        numc = pNC;
        assert numc == null || numc.size() == 2;
        assert numc == null || numc.get(0).size() == numc.get(1).size() : 
                "\nnumc0 (" + numc.get(0).size() + "): \n\t" + numc.get(0) + 
                "\nnumc1 (" + numc.get(1).size() + "): \n\t" + numc.get(1);
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
        assert pGraphics instanceof Graphics2D : "Uh, Idek how this happened, but g has to be a Graphics2D...";
        drawer = (Graphics2D) pGraphics;
        drawer.setColor(color);

        double[] dispBounds = grapher.components().dispBounds();

        if(numc != null) {
            assert numc.size() == 2;
            assert numc.get(0).size() == numc.get(1).size();
            for(int x = 0; x < numc.get(0).size(); x++) {
                drawp(numc.get(0).get(x), numc.get(1).get(x));
            }
        } else if(equation != null) {
            double cStep = grapher.components().cStep();
            for(double x = dispBounds[0]; x < dispBounds[2]; x += cStep) {
                drawl(x,
                      equationsys.eval("y",new EquationSystem().add("x = @" + (x < 0 ? "0 " + x : x))), 
                      x + cStep,
                      equationsys.eval("y", new EquationSystem().add("x = @" +
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
    private void drawp(Number x, Number y) {
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
    private double[] fix(Number x, Number y) {
        throw new NotDefinedException(); //TODO: THIS
        // return grapher.components().fix(x,y);
    }

    /** TODO: JAVADOC */
    public Grapher grapher(){
        return grapher;
    }

    /** TODO: JAVADOC */
    public Equation equation(){
        return equation;
    }

    /** TODO: JAVADOC */
    public EquationSystem equationsys(){
        return equationsys;
    }

    /** TODO: JAVADOC */
    public ArrayList<NumberCollection<Number>> numc(){
        return numc;
    }

    /** TODO: JAVADOC */
    public Color color(){
        return color;
    }


    @Override
    public String toString() {
        return "Display of " + (numc == null ? equation == null ? "Axis" : equation : numc +
            (equation != null ? " && " + equation : ""));
    } 

    @Override
    public String toFancyString(int idtLvl) {
        String ret = indent(idtLvl) + "DisplayComponent:";
        ret += "\n" + indent(idtLvl + 1) + "EquationSystem for Graphing:\n";
        ret += equationsys == null ? indent(idtLvl + 2) + "null" : equationsys.toFancyString(idtLvl + 2);
        ret += "\n" + indent(idtLvl + 1) + "Equation to Graph:\n";
        ret += equation == null ? indent(idtLvl + 2) + "null" : equation.toFancyString(idtLvl + 2);

        ret += "\n" + indent(idtLvl + 1) + "NumberCollection to Graph:\n";
        assert numc.size() == 2;
        assert numc.get(0).size() == numc.get(1).size();
        ret += numc == null ? indent(idtLvl + 2) + "null" :
               numc.get(0).toFancyString(idtLvl + 2) + "\n" + numc.get(1).toFancyString(idtLvl + 2);

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

        ret += "\n" + indent(idtLvl + 1) + "NumberCollection to Graph:\n";
        assert numc.size() == 2;
        assert numc.get(0).size() == numc.get(1).size();
        ret += numc == null ? indent(idtLvl + 2) + "null" :
               numc.get(0).toFullString(idtLvl + 2) + "\n" + numc.get(1).toFullString(idtLvl + 2);

        ret += "\n" + indent(idtLvl + 1) + "Color:\n" + indent(idtLvl + 2) + color;
        return ret;
    }

    @Override
    public DisplayComponent copy(){
        return new DisplayComponent(grapher, equation, equationsys, numc, color);
    }

    /**
     * Note that this doesnt consider drawer.
     * TODO: JAVADOC
     */
    @Override
    public boolean equals(Object pObj){
        if(pObj == null || !(pObj instanceof DisplayComponent))
            return false;
        if(this == pObj)
            return true;
        DisplayComponent pdisp = (DisplayComponent)pObj;
        if(!grapher.equals(pdisp.grapher()))
            return false;

        if((equation == null) ^ (pdisp.equation() == null))// TODO: ENCORPERATE -->|| !equation.equals(pdisp.equation()))
            return false;
        if((equationsys == null) ^ (pdisp.equationsys() == null)) //TODO: ENCORPERATE --> || !equationsys.equals(pdisp.equationsys()))
            return false;
        if((numc == null) != (pdisp.numc() == null) || !numc.equals(pdisp.numc()))
            return false;
        if((color == null) != (pdisp.color() == null) || !color.equals(pdisp.color()))
            return false;
        return true;
 
    }
}