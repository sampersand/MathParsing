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


 
import javax.swing.*;
import javax.swing.border.*;
import javax.accessibility.*;
 
import java.awt.*;
import java.awt.event.*;




public class Grapher extends JPanel{
    private JLayeredPane layeredPane;

    private ArrayList<Set> sets;
    private ArrayList<Equation> equations;
    private GraphComponents components;
    private ArrayList<DisplayComponent> displays;

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
        // pComponents = GraphComponents.TRIG;
        equations = pEquations;
        sets = pSets;
        components = pComponents;
        displays = new ArrayList<DisplayComponent>();
        displays.add(new DisplayComponent(this)); //adds axis
        Color[] colors = new Color[]{Color.BLUE, Color.RED, Color.YELLOW, Color.GREEN};
        for(int i = 0; i < equations.size(); i++)
            displays.add(new DisplayComponent(this, equations.get(i), colors[i%colors.length]));
        for(int i = 0; i < sets.size(); i++)
            displays.add(new DisplayComponent(this, sets.get(i), colors[i%colors.length]));
        graphSetup();
    }
    private void graphSetup(){
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
 
         //Create and set up the layered pane.
        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(components.winBounds()[0] + 10, components.winBounds()[1] + 10));
        layeredPane.setBorder(BorderFactory.createTitledBorder("Graph"));
  
        for (int i = 0; i < displays.size(); i++) {
            JLabel label = createDisplay(i);
            layeredPane.add(label, new Integer(i));
        }


        //Add control pane and layered pane to this JPanel.
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(layeredPane);
    }

    private JLabel createDisplay(int i) {
        JLabel label = displays.get(i);
        label.setVerticalAlignment(JLabel.CENTER);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setOpaque(false);
        label.setBorder(BorderFactory.createLineBorder(Color.black));
        label.setBounds(0, 10, components.winBounds()[0], components.winBounds()[1]);
        return label;
    }
    public void graph() {
        //Create and set up the window.
        String title = "Graph of ";
        if(equations.size() + sets.size() > 5){
            title += "A lot of stuff";
        } else if(equations.size() + sets.size() == 0){
            title += "Nothing...? Lol why graph that.";
        } else if(equations.size() + sets.size() == 1){
            title += equations.size() == 1 ? equations.get(0).equation : sets.get(0);
        } else {
            for(int i = 0; i < equations.size(); i++){ // for each loop will crash if equation's size is 0.
                title += equations.get(i) + ", ";
            }
            for(int i =0; i < sets.size(); i++){
                 title += sets.get(i) + ", ";   
            }
            title = title.substring(0, title.length() - 2);

        }
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        this.setOpaque(true); 
        frame.setContentPane(this);
 
        frame.pack();
        frame.setVisible(true);
    }



    public ArrayList<Set> sets(){ return sets; }
    public ArrayList<Equation> equations(){ return equations; }
    public GraphComponents components(){ return components; }

    public String toString(){
        String ret = "Graph of ";
        if(sets == null && equations == null || (sets.size() == 0 && equations.size() == 0)){
            return "Empty Graph"; 
        } else if(equations.size() + sets.size() == 1){
            return ret + (equations.size() == 1 ? equations.get(0).equation : sets.get(0));
        } else {
            for(int i = 0; i < equations.size(); i++){ // for each loop will crash if equation's size is 0.
                ret += equations.get(i) + ", ";
            }
            for(int i =0; i < sets.size(); i++){
                 ret += sets.get(i) + ", ";   
            }
            return ret.substring(0, ret.length() - 2);

        }
    }

}