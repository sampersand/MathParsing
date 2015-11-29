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
    private ArrayList<Display> displays;
    private String[] layerStrings = { "Yellow (0)", "Magenta (1)",
                                      "Cyan (2)",   "Red (3)",
                                      "Green (4)" };
    private Color[] layerColors = { Color.yellow, Color.magenta,
                                    Color.cyan,   Color.red,
                                    Color.green };
 

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
        graphSetup();
    }
    private void graphSetup(){
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
 
         //Create and set up the layered pane.
        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(components.winBounds()[0], components.winBounds()[1]));
        // layeredPane.setBorder(BorderFactory.createTitledBorder(
                                    // "Equations"));
 
  
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
        label.setVerticalAlignment(JLabel.TOP);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setOpaque(true);
        label.setBorder(BorderFactory.createLineBorder(Color.black));
        label.setBounds(5, 5, components.winBounds()[0], components.winBounds()[1]);//origin.x, origin.y, 140, 140);
        return label;
    }
    public void graph() {
        //Create and set up the window.
        JFrame frame = new JFrame("Graph of Stuff");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        this.setOpaque(true); 
        frame.setContentPane(this);
 
        frame.pack();
        frame.setVisible(true);
    }


    // public void graph(){
    //     //this one function is killing me
    //     JFrame frame = new JFrame();

    //     frame.setSize(components.winBounds()[0], components.winBounds()[1]);
    //     frame.setTitle("Graph of stuff");
    //     frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    //     JLayeredPane pane = new JLayeredPane();

    //     for(int i = 0; i < displays.size(); i++){
    //         pane.setLayer(displays.get(i), i);
    //     }
    //             pane.setVisible(true);

    //     frame.add(pane);
    //     frame.setVisible(true);

    // }
    public ArrayList<Set> sets(){ return sets; }
    public ArrayList<Equation> equations(){ return equations; }
    public GraphComponents components(){ return components; }


}