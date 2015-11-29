package Math.Display;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.util.ArrayList;

import Math.Equation.Equation;
import Math.Set.Set;
import Math.Display.GraphComponents;

public class Grapher {
    private JFrame frame;
    private ArrayList<Set> sets;
    private ArrayList<Equation> equations;
    private GraphComponents components;
    private ArrayList<Display> displays;
    public Grapher(){
        this(null, null);
    }
    
    public Grapher(Set pSet){
        this(null, new ArrayList<Set>(){{add(pSet);}});
    }

    public Grapher(Equation pEquation){
        this(new ArrayList<Equation>(){{add(pEquation);}}, null);
    }

    public Grapher(GraphComponents pComponents){
        this(null, null, pComponents);
    }

    public Grapher(ArrayList<Equation> pEquations, ArrayList<Set> pSets) {
        this(pEquations, pSets, new GraphComponents());
    }

    public Grapher(ArrayList<Equation> pEquations, ArrayList<Set> pSets, GraphComponents pComponents){
        equations = pEquations;
        sets = pSets;
        components = pComponents;
        for(Equation equation : pEquations)
            displays.add(new Display(this, equation));
        for(Set set : pSets)
            displays.add(new Display(this, set));
    }

    public ArrayList<Set> sets(){ return sets; }
    public ArrayList<Equation> equations(){ return equations; }
    public GraphComponents components(){ return components; }

    public void graph(){
        JFrame frame = new JFrame();
        int xScreenLimit = 1000; // note this is the physical screen limits
        int yScreenLimit = 1000; // note this is the physical screen limits

        frame.setSize(components.winBounds()[0], components.winBounds()[1]);
        frame.setTitle("Graph of stuff");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        frame.add(gdisp);        
        frame.setVisible(true);

    }
}