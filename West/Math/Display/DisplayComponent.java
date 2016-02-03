package West.Math.Display;

import West.Math.MathObject;
import West.Print;
import West.Math.Equation.Equation;
import West.Math.Equation.EquationSystem;
import West.Math.Set.NumberCollection;
import West.Math.Set.Collection;
import West.Math.ComplexNumber;

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
 * @version 1.1
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
    protected Collection<NumberCollection<ComplexNumber>> numc;

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
    public DisplayComponent(Grapher pGrapher, Collection<NumberCollection<ComplexNumber>> pNC) {
        this(pGrapher, pNC, Color.BLUE);
    }

    /** TODO: JAVADOC */
    public DisplayComponent(Grapher pGrapher, Collection<NumberCollection<ComplexNumber>> pNC, Color pColor) {
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
                             Collection<NumberCollection<ComplexNumber>> pNC, Color pColor) {
        grapher = pGrapher;
        equation = pEquation;
        equationsys = pEqSys;
        numc = pNC;
        assert numc == null || numc.size() == 2;
        assert numc == null || numc.get(0).size() == numc.get(1).size() : 
                "\nnumc0 (" + numc.get(0).size() + "): \n\t" + numc.get(0) + 
                "\nnumc1 (" + numc.get(1).size() + "): \n\t" + numc.get(1);
        color = pColor;
        this.createToolTip();
        //  setPreferredSize(new Dimension(grapher.components().winBounds()[0], grapher.components().winBounds()[1]));
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
    public void paintComponent(Graphics pGraphics) {
        assert pGraphics instanceof Graphics2D : "Uh, Idek how this happened, but g has to be a Graphics2D...";
        drawer = (Graphics2D) pGraphics;
        drawer.setColor(color);

        double[] dispBounds = grapher.components().dispBounds();
        double[] stepInfo = grapher.components().stepInfo();
        if(numc != null) {
            assert numc.size() == 2;
            assert numc.get(0).size() == numc.get(1).size();
            for(int x = 0; x < numc.get(0).size(); x++) {
                double d1 = numc.get(0).get(x).toDouble();
                double d2 = numc.get(1).get(x).toDouble();
                if(d1 == Double.NaN){
                    Print.printi("d1 is " + d1 + ". Not graphing (" + d1 + ", " + d2 + ").");
                } if(d2 == Double.NaN){
                    Print.printi("d2 is " + d2 + ". Not graphing (" + d1 + ", " + d2 + ").");
                }
                drawp(d1, d2);
            }
        } else if(equation != null) {
            double cStep = grapher.components().cStep();
            for(double x = stepInfo[0]; x < stepInfo[1]; x += cStep) {
                drawl(new ComplexNumber(x),
                      equationsys.eval(equation.getVar(),
                                       new EquationSystem().add(
                                                                grapher.components().indepVar()
                                                                + "=" + x)), 
                      new ComplexNumber(x + cStep),
                      equationsys.eval(equation.getVar(),
                                       new EquationSystem().add(
                                                                grapher.components().indepVar()
                                                                + "=" + (x + cStep))),
                      false);
            }
        } else {
            drawl(0d, dispBounds[1], 0d, dispBounds[3], true); // axis.
            drawl(dispBounds[0], 0d, dispBounds[2], 0d, true); // axis.
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
       drawer.drawOval((int)xy[0] - 5, (int) xy[1] - 5, 10, 10); //  width, height
    }

    private void drawl(Double x, Double y, Double X, Double Y, boolean fix) {
        drawl(new ComplexNumber(x), new ComplexNumber(y), new ComplexNumber(X), new ComplexNumber(Y), fix);
    }

    /** TODO: JAVADOC */
    private void drawl(ComplexNumber x, ComplexNumber y, ComplexNumber X, ComplexNumber Y, boolean fix) {
        assert y != null;
        assert Y != null;
        assert !x.isNaN();
        assert !X.isNaN();
        if(y.isNaN() || Y.isNaN())
            return;
        if(!fix && grapher.components().type().equals(West.Math.Display.GraphComponents.GraphTypes.POLAR)){
            assert y.isOnlyReal() : y + " can only be real when graphing!";
            assert Y.isOnlyReal() : Y + " can only be real when graphing!";
            ComplexNumber r = y;
            y = y.mult(x.sin()); // y is r, x is theta
            x = r.mult(x.cos()); // y is r, x is theta
            r = Y;
            Y = Y.mult(X.sin()); // Y is r, X is theta
            X = r.mult(X.cos()); // Y is r, X is theta
        }
        if(!fix && grapher.components().type().equals(West.Math.Display.GraphComponents.GraphTypes.IMAG)){
            assert false;
        }
        double[] xy=fix(x.aIsOnlyReal().real(), y.aIsOnlyReal().real());
        double[] XY=fix(X.aIsOnlyReal().real(), Y.aIsOnlyReal().real());
        drawer.drawLine((int) xy[0], (int)xy[1], (int)XY[0], (int)XY[1]); //  width, height
    }
  
    /** TODO: JAVADOC */
    private double[] fix(double x, double y) {
        return grapher.components().fix(x,y);
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
    public Collection<NumberCollection<ComplexNumber>> numc(){
        return numc;
    }

    /** TODO: JAVADOC */
    public Color color(){
        return color;
    }


    @Override
    public String toString() {
        return "Display of " + (numc == null ? equation == null ? "Axis" : equation : "(" + numc + ")" +
            (equation != null ? " && " + equation : ""));
    } 

    @Override
    public String toFancyString(int idtLvl) {
        String ret = indent(idtLvl) + "DisplayComponent:";
        ret += "\n" + indent(idtLvl + 1) + "EquationSystem for Graphing:\n";
        ret += equationsys == null ? indentE(idtLvl + 2) + "null" : equationsys.toFancyString(idtLvl + 2);
        ret += "\n" + indent(idtLvl + 1) + "Equation to Graph:\n";
        ret += equation == null ? indentE(idtLvl + 2) + "null" : equation.toFancyString(idtLvl + 2);

        ret += "\n" + indent(idtLvl + 1) + "NumberCollection to Graph:\n";
        if(numc != null){
            assert numc.size() == 2;
            assert numc.get(0).size() == numc.get(1).size();
        }
        ret += numc == null ? indentE(idtLvl + 2) + "null" :
               numc.get(0).toFancyString(idtLvl + 2) + "\n" + numc.get(1).toFancyString(idtLvl + 2);

        ret += "\n" + indent(idtLvl + 1) + "Color:\n" + indentE(idtLvl + 2) + color;
        ret += "\n" + indentE(idtLvl + 1);

        return ret;
    }

    @Override
    public String toFullString(int idtLvl) {
        String ret = indent(idtLvl) + "DisplayComponent:";
        ret += "\n" + indent(idtLvl + 1) + "EquationSystem for Graphing:\n";
        ret += equationsys == null ? indentE(idtLvl + 2) + "" : equationsys.toFullString(idtLvl + 2);

        ret += "\n" + indent(idtLvl + 1) + "Equation to Graph:\n";
        ret += equation == null ? indentE(idtLvl + 2) + "" : equation.toFullString(idtLvl + 2);

        ret += "\n" + indent(idtLvl + 1) + "NumberCollection to Graph:\n";

        // assert numc.size() == 2;
        // assert numc.get(0).size() == numc.get(1).size();
        ret += numc == null ? indent(idtLvl + 2) + "" :
               numc.get(0).toFullString(idtLvl + 2) + "\n" + numc.get(1).toFullString(idtLvl + 2);

        ret += "\n" + indent(idtLvl + 1) + "Color:\n" + indentE(idtLvl + 2) + color;
        ret += "\n" + indentE(idtLvl + 1);
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
        if(pObj == null || !(pObj instanceof DisplayComponent)) return false;
        if(this == pObj) return true;
        DisplayComponent pdisp = (DisplayComponent)pObj;
        if(!grapher.equals(pdisp.grapher)) return false;
        if(equation == null ^ pdisp.equation == null) return false;
        if(equationsys == null ^ pdisp.equationsys == null) return false;
        if(numc == null ^ pdisp.numc == null) return false;
        if(color == null ^ pdisp.color == null) return false;
        return (equation == null || equation.equals(pdisp.equation)) &&
               (equationsys == null || equationsys.equals(pdisp.equationsys)) &&
               (numc == null || numc.equals(pdisp.numc)) &&
               (color == null || color.equals(pdisp.color));
 
    }
}