package West.Math.Display.Calculator;

import West.Math.MathObject;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
 
/**
 * The frame that holds all the buttons and displays for the calculator.
 * 
 * @author Sam Westerman
 * @version 1.1
 * @since 0.3
 */ 
public class MainClass implements MathObject {
    protected CalcWindow calcWin;
    protected static JFrame graphingFrame;

    /**
     * The constructor for <code>MainClass</code>, which creates the calculator Window.
     */
    public MainClass() {
        calcWin = new CalcWindow(this,"Calculator");
        calcWin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * The main function, which creates an instance of <code>MainClass</code>, and runs it.
     * @param args          The arguments passed from the command line.
     */
    public static void main(String[] args) {
        MainClass main = new MainClass();
    }

    /**
     * Overloaded setUpAndDrawGraph function, taking only the eq argument.
     * This calls <code>setUpAndDrawGraph</code> with <code>EQUATION</code> as <code>eq</code>,
     * <code>LIMIT</code> as <code>10</code>, and <code>STEP</code> as <code>0.1D</code>.
     * @param eq            The equation that will be passed along to graph.
     */
    public void setUpAndDrawGraph(String eq) {
        setUpAndDrawGraph(eq, 10, 0.1D);
    }

    /**
     * Overloaded setUpAndDrawGraph function, taking the eq and step arguments.
     * This calls <code>setUpAndDrawGraph</code> with <code>EQUATION</code> as <code>eq</code>,
     * <code>LIMIT</code> as <code>10</code>, and <code>STEP</code> as <code>step</code>.
     * @param eq            The equation that will be passed along to graph.
     * @param step          The amount that x will be incremented when graphing.
     */
    public void setUpAndDrawGraph(String eq, double step) {
        setUpAndDrawGraph(eq, 10, step);
    }

    /**
     * Overloaded setUpAndDrawGraph function, taking the eq and limit arguments.
     * This calls <code>setUpAndDrawGraph</code> with <code>EQUATION</code> as <code>eq</code>,
     * <code>LIMIT</code> as <code>limit</code>, and <code>STEP</code> as <code>0.1D</code>.
     * @param eq            The equation that will be passed along to graph.
     * @param limit         The x and y boundries for the graph. For example, if 
     *                      <code>limit = 10</code>, the boundries would be (- 10x, 10x, - 10y, 10y).
     */
    public void setUpAndDrawGraph(String eq, int limit) {
        setUpAndDrawGraph(eq, limit, 0.1D);
    }

    /**
     * Draws a graph of the passed equation(s) on the screen, along with axis.
     * Note: <code>EquationDisplay</code> is used to display the graph.
     * @param rawEq         The equation(s) that will be graphed by <code>EquationDisplay</code>.
     * @param limit         The outermost boundries, outside of which the equation wont be graphed.
     * @param step          The amount that x will be incremented by. The smaller it is, the more
     *                      percise the graph.
     * @see EquationDisplay
     */    
    public void setUpAndDrawGraph(String rawEq, int limit, double step) {
        String[] equations = rawEq.split(";");
        Color[] colors = { Color.RED, Color.BLUE, Color.ORANGE, Color.MAGENTA, Color.PINK,
                           Color.CYAN, Color.GREEN, Color.YELLOW };
        limit = 10;
        step = 0.1D;
        JFrame frame = new JFrame();
        int xScreenLimit = 500; //  note this is the physical screen limits
        int yScreenLimit = 500; //  note this is the physical screen limits        
        frame.setSize(xScreenLimit * 2, yScreenLimit * 2);
        frame.setTitle("Graphing calculator");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        GraphDisplay gdisp = new GraphDisplay(equations, (double)limit, step,
                new int[] { xScreenLimit, yScreenLimit }, new int[] { 10, 10 },
                colors);
        EquationDisplay eqComponent;

        for(int pos = 0; pos < equations.length; pos++) {
            eqComponent = new EquationDisplay(equations[pos], (double)limit, step,
                new int[] { xScreenLimit, yScreenLimit }, new int[] { 10, 10 },
                colors[pos % colors.length]);
            frame.add(eqComponent);
            frame.setVisible(true);        
        }

        frame.add(gdisp);        
        frame.setVisible(true);

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
    public GraphDisplay copy(){
        throw new NullPointerException();
    }

    @Override
    public boolean equals(Object pObj){
        throw new NullPointerException();
    }
}
