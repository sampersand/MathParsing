package Math.Set;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.util.ArrayList;

import Math.Equation.Equation;
import Math.Set.Set;
import Math.Set.GraphComponents;

public class Grapher {
    JFrame frame;
    public Grapher(){}
    public Grapher(Equation eq){}
    public Grapher(ArrayList<Set> pSets, ArrayList<Equation> pEquations, GraphComponents pComponent){
        JFrame frame = new JFrame();
        int xScreenLimit = 1000; // note this is the physical screen limits
        int yScreenLimit = 1000; // note this is the physical screen limits
        frame.setSize(xScreenLimit, yScreenLimit);
        frame.setTitle("Graph of stuff");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Display gdisp = new Display(pSets, pEquations, pComponent);
        frame.add(gdisp);        
        frame.setVisible(true);

    }
    public void graph(){}
}