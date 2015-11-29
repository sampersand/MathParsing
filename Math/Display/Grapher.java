package Math.Display;

import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import java.awt.FlowLayout;


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
        displays = new ArrayList<Display>();
        displays.add(new Display(this)); //adds axis
        Color[] colors = new Color[]{Color.BLUE, Color.RED, Color.YELLOW, Color.GREEN};

        for(int i = 0; i < equations.size(); i++)
            displays.add(new Display(this, equations.get(i), colors[i%colors.length]));
        for(int i = 0; i < sets.size(); i++)
            displays.add(new Display(this, sets.get(i), colors[i%colors.length]));
    }

    public ArrayList<Set> sets(){ return sets; }
    public ArrayList<Equation> equations(){ return equations; }
    public GraphComponents components(){ return components; }

    public void graph(){
        //this one function is killing me
        JFrame frame = new JFrame();

        frame.setSize(components.winBounds()[0], components.winBounds()[1]);
        frame.setTitle("Graph of stuff");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JLayeredPane pane = new JLayeredPane();

        for(int i = 0; i < displays.size(); i++){
            pane.setLayer(displays.get(i), i);
        }
                pane.setVisible(true);

        frame.add(pane);
        frame.setVisible(true);

    }
}