package Math.Set;
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

   private final Set set;

   /** 
    * The physical amount of pixels the window is wise and tall.
    * The array will look like the following: <code>(X, Y)</code>
    */
   private final int[] WINDOW_BOUNDS;

   /** 
    * The min and max x/y values used for display.
    * Increasing them incrases the zoom level, and shifting them a certain direction shifts the 
    * "window" that direction.
    * <p>
    * The array will look like the following: <code>(min X, max X, min Y, max Y)</code>.
    */
   private final int[] DISPLAY_BOUNDS;

   /** 
    * The element that draws the lines.
    */
   private Graphics2D drawer;

   private final double STEP = 20;
   
   /** 
    * The class that controls the drawing of the graph.
    * 
    * @param set         The {@link Set} that contains all the graphing points
    * @param winBnd        The minimum and maximum (for x and y) values that will be displayed.
    * //@param dispBnd       The hight and width of the physical window. Might be removed if there is 
    *                      a function to find this w/o the input. Used to adjust coords of points.
    * @throws ArrayIndexOutOfBoundsException       thrown if the input array's lengths aren't 1, 2,
    *                                              and 4 (except steps. steps is only 1 and 2).
    */
   public SetDisplay(Set set, int[] winBnd) throws ArrayIndexOutOfBoundsException {
      this.set = set;
      double[] temp = set.sort(set.getMatr());
      double[] temp2 = set.sort(set.getMatr2());
      // this.DISPLAY_BOUNDS = new int[]{(int)(temp[0]*0.9),(int)(temp[temp.length-1]*1.1), //these are used ot make sure the outermost points are shown
      //                                 (int)(temp2[0]*0.9),(int)(temp2[temp2.length-1]*1.1)};
      this.DISPLAY_BOUNDS = new int[]{5,285,1800,2000};
      this.createToolTip(); // I thought these might be interesting, but right now they dont work.
      if(set.getMatr().length != set.getMatr2().length)
        throw new ArrayIndexOutOfBoundsException("The Set needs to have matr and matr2 be the same length.");
      if (winBnd.length == 1) {
         this.WINDOW_BOUNDS = new int[] { winBnd[0], winBnd[0]};
      } else if (winBnd.length == 2) {
         this.WINDOW_BOUNDS = new int[] {winBnd[0], winBnd[1]};
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
      assert set.getMatr().length == set.getMatr2().length;
      drawer = (Graphics2D) g;
      double[] m1 = set.getMatr();
      double[] m2 = set.getMatr2();
      for(int x = 0; x < set.getMatr().length; x++){
          drawp(m1[x], m2[x]);
      }
      double step = (DISPLAY_BOUNDS[1] - DISPLAY_BOUNDS[0])/STEP;
      for(double x = DISPLAY_BOUNDS[0]; x < DISPLAY_BOUNDS[1];
          x += step){
        drawl((double)x,set.pred(x),x+step,set.pred(x+step));
      }
      drawl(0,DISPLAY_BOUNDS[2],0,DISPLAY_BOUNDS[3],Color.BLACK);
      drawl(DISPLAY_BOUNDS[0],0,DISPLAY_BOUNDS[1],0,Color.BLACK);
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
      drawer.drawOval((int)xy[0]- 10, (int) xy[1]-10, 20, 20); // width, height
   }

   private void drawl(double x, double y, double X, double Y, Color color) {
      drawer.setColor(color);
      double[] xy=fix(x,y);
      double[] XY=fix(X,Y);
      drawer.drawLine((int) xy[0],(int)xy[1],(int)XY[0],(int)XY[1]); // width, height
   }
   private void drawl(double x, double y, double X, double Y) { drawl(x,y,X,Y,Color.BLUE); }
   private void drawl(int x, int y, int X, int Y, Color color) { drawl((double)x,(double)y,(double)X,(double)Y,color); }


   private double[] fix(double x, double y){
    return new double[]{
      (x - DISPLAY_BOUNDS[0]) / (DISPLAY_BOUNDS[1] - DISPLAY_BOUNDS[0]) * WINDOW_BOUNDS[0],
      (1-(y - DISPLAY_BOUNDS[2]) / (DISPLAY_BOUNDS[3] - DISPLAY_BOUNDS[2])) * WINDOW_BOUNDS[1]};
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

