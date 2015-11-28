package Math.Set;
import Math.Equation.Equation;
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
public class SetDisplay extends JComponent{  

   private Set set;
   private Equation[] equations;
   /** 
    * The physical amount of pixels the window is wise and tall.
    * The array will look like the following: <code>(X, Y)</code>
    */
   private int[] wBounds;

   /** 
    * The min and max x/y values used for display.
    * Increasing them incrases the zoom level, and shifting them a certain direction shifts the 
    * "window" that direction.
    * <p>
    * The array will look like the following: <code>(min X, max X, min Y, max Y)</code>.
    */
   private int[] dispBounds;

   /** 
    * The element that draws the lines.
    */
   private Graphics2D drawer;

   private double step = 100;
   
      public SetDisplay(Equation pEq, int[] winBnd) throws ArrayIndexOutOfBoundsException {
        this(new Equation[] {pEq}, winBnd);
      }
    public SetDisplay(Equation[] pEq, int[] winBnd) throws ArrayIndexOutOfBoundsException {
      equations = pEq;
      set = null;
      if (winBnd.length == 1) {
         this.wBounds = new int[] { winBnd[0], winBnd[0]};
      } else if (winBnd.length == 2) {
         this.wBounds = new int[] {winBnd[0], winBnd[1]};
      } else {
         throw new ArrayIndexOutOfBoundsException("The length of winBnd needs to be 1, or 2.");
      }
      this.dispBounds = new int[]{-10, 10, -10, 10};
      this.createToolTip(); // I thought these might be interesting, but right now they dont work.
   }
    public SetDisplay(Equation[] pEq, Set pSet, int[] winBnd) throws ArrayIndexOutOfBoundsException {
      this(pSet,winBnd);
      equations = pEq;
   }

   /** 
    * The class that controls the drawing of the graph.
    * 
    * @param pSet         The {@link Set} that contains all the graphing points
    * @param winBnd        The minimum and maximum (for x and y) values that will be displayed.
    * //@param dispBnd       The hight and width of the physical window. Might be removed if there is 
    *                      a function to find this w/o the input. Used to adjust coords of points.
    * @throws ArrayIndexOutOfBoundsException       thrown if the input array's lengths aren't 1, 2,
    *                                              and 4 (except steps. steps is only 1 and 2).
    */
   public SetDisplay(Set pSet, int[] winBnd) throws ArrayIndexOutOfBoundsException {
      set = pSet;
      equations = null;
      double[] temp = set.sort(set.matr);
      double[] temp2 = set.sort(set.matr2);
      this.dispBounds = new int[]{(int)(temp[0]*-.2 - 5),(int)(temp[temp.length-1]*1.3), //these are used ot make sure the outermost points are shown
                                      (int)(temp2[0]*-.2 - 5),(int)(temp2[temp2.length-1]*1.3)};
      // this.dispBounds = new int[]{,285,1800,2000};
      this.createToolTip(); // I thought these might be interesting, but right now they dont work.
      if(set.matr.length != set.matr2.length)
        throw new ArrayIndexOutOfBoundsException("The Set needs to have matr and matr2 be the same length.");
      if (winBnd.length == 1) {
         this.wBounds = new int[] { winBnd[0], winBnd[0]};
      } else if (winBnd.length == 2) {
         this.wBounds = new int[] {winBnd[0], winBnd[1]};
      } else {
         throw new ArrayIndexOutOfBoundsException("The length of winBnd needs to be 1, or 2.");
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
      if(set != null){
        assert set.matr.length == set.matr2.length;
        double[] m1 = set.matr;
        double[] m2 = set.matr2;
        for(int x = 0; x < set.matr.length; x++){
            drawp(m1[x], m2[x]);
        }
      }
      if(equations != null){
        double cStep = (dispBounds[1] - dispBounds[0])/step;
        for(int i = 0; i < equations.length; i++){
          Equation eq = equations[i];
          for(double x = dispBounds[0]; x < dispBounds[1]; x += cStep){
            drawl((double)x, Set.pred(x, eq, "y"), x + cStep, Set.pred(x + cStep, eq, "y"), i);
          }
        }
      }
      drawl(0,dispBounds[2],0,dispBounds[3],Color.BLACK); //axis?
      drawl(dispBounds[0],0,dispBounds[1],0,Color.BLACK); //axis?
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
      double[] xy=fix(x,y);
      drawer.drawOval((int)xy[0]- 5, (int) xy[1]-5, 10, 10); // width, height
   }

   private void drawl(double x, double y, double X, double Y, Color color) {
      drawer.setColor(color);
      double[] xy=fix(x,y);
      double[] XY=fix(X,Y);
      drawer.drawLine((int) xy[0],(int)xy[1],(int)XY[0],(int)XY[1]); // width, height
   }
   private void drawl(double x, double y, double X, double Y, int i) {
    Color[] colors = new Color[]{Color.BLUE, Color.RED, Color.YELLOW, Color.GREEN};
    drawl(x,y,X,Y,colors[i%colors.length]);
  }
   private void drawl(int x, int y, int X, int Y, Color color) { drawl((double)x,(double)y,(double)X,(double)Y,color); }


   private double[] fix(double x, double y){
    return new double[]{
      (x - dispBounds[0]) / (dispBounds[1] - dispBounds[0]) * wBounds[0],
      (1-(y - dispBounds[2]) / (dispBounds[3] - dispBounds[2])) * wBounds[1]};
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

