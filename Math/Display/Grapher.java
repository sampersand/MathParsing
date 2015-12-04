package Math.Display;

import Math.MathObject;
import Math.Equation.EquationSystem;
import Math.Equation.Equation;
import Math.Exception.NotDefinedException;
import Math.Set.Set;
import Math.Display.GraphComponents;
 
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.*;

import javax.accessibility.*;
 
import java.awt.*;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.*;

import java.util.ArrayList;



public class Grapher extends JPanel implements MathObject{
    private JLayeredPane layeredPane;

    private ArrayList<Set> sets;
    private ArrayList<EquationSystem> equations;
    private GraphComponents components;
    private ArrayList<DisplayComponent> displays;

    public Grapher() {
        this(new GraphComponents());
    }
    public Grapher(GraphComponents pGraph) {
        sets = new ArrayList<Set>();
        equations = new ArrayList<EquationSystem>();
        components = pGraph;
        displays = new ArrayList<DisplayComponent>();
        graphSetup();
    }
    public Grapher add(Set... pSets) {
        if(pSets != null && pSets.length != 0)
            for(Set set : pSets)
                sets.add(set);
        return this;
    }
    public Grapher addSets(ArrayList<Set> pSets) {
        if(pSets != null && pSets.size() != 0)
            sets.addAll(pSets);
        return this;
    }

    public Grapher add(EquationSystem... pEqSys) {
        if(pEqSys != null && pEqSys.length != 0)
            for(EquationSystem eqsys : pEqSys)
                equations.add(eqsys);
        return this;

    }
    /** Yah, this isn't in the same vein as "add<ITEMPLURAL>", but addEqSyss just seems weird... */
    public Grapher addEqSys(ArrayList<EquationSystem> pEqSys) {
        if(pEqSys != null && pEqSys.size() != 0)
            equations.addAll(pEqSys);
        return this;
    }

    public Grapher add(Equation... pEqs) {
        if(equations.size() == 0)
            equations.add(new EquationSystem());
        if(pEqs != null && pEqs.length != 0)
            for(Equation eq : pEqs)
                equations.get(equations.size() - 1).add(eq);
        return this;

    }
    public Grapher addEquations(ArrayList<Equation> pEqs) {
        if(equations.size() == 0)
            equations.add(new EquationSystem());
        equations.get(equations.size() - 1).add(pEqs);
        return this;
    }

    // public Grapher(Set pSet) {
    //     this(null, new ArrayList<Set>() {{add(pSet);}});
    // }

    // public Grapher(EquationSystem pEqSys) {
    //     this(pEqSys, null);
    // }

    // public Grapher(GraphComponents pComponents) {
    //     this(null, null, pComponents);
    // }

    // public Grapher(EquationSystem pEqSys, ArrayList<Set> pSets) {
    //     this(pEqSys, pSets, new GraphComponents());
    // }

    // public Grapher(EquationSystem pEqSys, ArrayList<Set> pSets, GraphComponents pComponents) {
    //     // pComponents = GraphComponents.TRIG;
    //     equation = pEqSys;
    //     sets = pSets;
    //     components = pComponents;
    //     // pComponentsxetDispBounds(5, 20, components.winBounds()[0], components.winBounds()[1] + 10);

    //     displays = new ArrayList<DisplayComponent>();
    //     displays.add(new DisplayComponent(this)); //adds axis
    //     Color[] colors = new Color[]{Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW};
    //     for(int i = 0; i < equations.size(); i++)
    //         displays.add(new DisplayComponent(this, equation.get(i), colors[i%colors.length]));
    //     for(int i = 0; i < sets.size(); i++)
    //         displays.add(new DisplayComponent(this, sets.get(i), colors[i%colors.length]));
    //     graphSetup();
    // }
    private void graphSetup() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
 
         //Create and set up the layered pane.
        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(components.winBounds()[0] + 10, components.winBounds()[1] + 10));
        // layeredPane.setBorder(BorderFactory.createTitledBorder("Graph"));
  
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
        label.setBounds(5, 0, components.winBounds()[0], components.winBounds()[1]);

        // label.setBounds(5, 20, components.winBounds()[0], components.winBounds()[1] - 20);
        return label;
    }
    public void graph() {
        //Create and set up the window.
        String title = "Graph of ";
        if(equations.size() + sets.size() > 5) {
            title += "A lot of stuff";
        } else if(equations.size() + sets.size() == 0) {
            title += "Nothing...? Lol why graph that.";
        } else if(equations.size() + sets.size() == 1) {
            title += equations.size() == 1 ? equations.get(0) : sets.get(0);
        } else {
            for(int i = 0; i < equations.size(); i++) { // for each loop will crash if equation's size is 0.
                title += equations.get(i) + ", ";
            }
            for(int i =0; i < sets.size(); i++) {
                 title += sets.get(i) + ", ";   
            }
            title = title.substring(0, title.length() - 2);

        }
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 
        this.setOpaque(true); 
        frame.setContentPane(this);
 
        frame.pack();
        frame.setVisible(true);
    }



    public ArrayList<Set> sets() { return sets; }
    public ArrayList<EquationSystem> equations() { return equations; }
    public GraphComponents components() { return components; }

    @Override
    public String toString() {
        String ret = "Graph of ";
        if(sets == null && equations == null || (sets.size() == 0 && equations.size() == 0)) {
            return "Empty Graph"; 
        } else if(equations.size() + sets.size() == 1) {
            return ret + (equations.size() == 1 ? equations.get(0) : sets.get(0));
        } else {
            for(int i = 0; i < equations.size(); i++) { // for each loop will crash if equation's size is 0.
                ret += equations.get(i) + ", ";
            }
            for(int i =0; i < sets.size(); i++) {
                 ret += sets.get(i) + ", ";   
            }
            return ret.substring(0, ret.length() - 2);

        }
    }

    @Override
    public String toFancyString() {
        throw new NotDefinedException();
    }

    @Override
    public String toFullString() {
        throw new NotDefinedException();
    }

}