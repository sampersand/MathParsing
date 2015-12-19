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



public class Grapher extends JPanel implements MathObject {
    public static final Color[] COLORS = new Color[]{Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW};
    protected JLayeredPane layeredPane;
    protected ArrayList<Set> sets;
    protected EquationSystem equationsys;
    protected GraphComponents components;
    protected ArrayList<DisplayComponent> displays;

    public Grapher() {
        this(new GraphComponents());
    }
    public Grapher(Set pSet) {
        this(null, new ArrayList<Set>() {{add(pSet);}});
    }

    public Grapher(EquationSystem pEqSys) {
        this(pEqSys, null);
    }

    public Grapher(GraphComponents pComponents) {
        this(null, null, pComponents);
    }

    public Grapher(EquationSystem pEqSys, ArrayList<Set> pSets) {
        this(pEqSys, pSets, new GraphComponents());
    }
    public Grapher(EquationSystem pEqSys, ArrayList<Set> pSets, GraphComponents pGraph) {
        sets = pSets;
        equationsys = pEqSys;
        components = pGraph;
        displays = new ArrayList<DisplayComponent>();
        System.out.println(equationsys+"@@@@@@@@@@@@@@@");
        displays.add(new DisplayComponent(this)); //adds axis
        for(int i = 0; i < equationsys.size(); i++)
            displays.add(new DisplayComponent(this, equationsys.equations().get(i), equationsys, 
                                              COLORS[i % COLORS.length]));
        for(int i = 0; i < sets.size(); i++)
            displays.add(new DisplayComponent(this, sets.get(i), COLORS[i % COLORS.length]));
        graphSetup();
    }

    // public Grapher add(Set... pSets) {
    //     if(pSets != null && pSets.length != 0)
    //         for(Set set : pSets)
    //             sets.add(set);
    //     return this;
    // }
    // public Grapher addSets(ArrayList<Set> pSets) {
    //     if(pSets != null && pSets.size() != 0)
    //         sets.addAll(pSets);
    //     return this;
    // }

    // public Grapher add(EquationSystem pEqSys) {
    //     equationsys.add(pEqSys);
    //     return this;

    // }

    // public Grapher add(Equation... pEqs) {
    //     if(equationsys.size() == 0)
    //         equationsys.add(new EquationSystem());
    //     if(pEqs != null && pEqs.length != 0)
    //         for(Equation eq : pEqs)
    //             equationsys.equations().add(eq);
    //     return this;

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
        if(equationsys.size() + sets.size() > 3) {
            title += "A lot of stuff";
        } else if(equationsys.size() + sets.size() == 0) {
            title += "Nothing...? Lol why graph that.";
        } else if(equationsys.size() + sets.size() == 1) {
            title += equationsys.size() == 1 ? equationsys.equations().get(0) : sets.get(0);
        } else {
            for(int i = 0; i < equationsys.size(); i++) { // for each loop will crash if equation's size is 0.
                title += equationsys.equations().get(i) + ", ";
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
    public EquationSystem equationsys() { return equationsys; }
    public GraphComponents components() { return components; }

    @Override
    public String toString() {
        String ret = "Graph of ";
        if(sets == null && equationsys == null || (sets.size() == 0 && equationsys.size() == 0)) {
            return "Empty Graph"; 
        } else if(equationsys.size() + sets.size() == 1) {
            return ret + (equationsys.size() == 1 ? equationsys.equations().get(0) : sets.get(0));
        } else {
            for(int i = 0; i < equationsys.size(); i++) { // for each loop will crash if equation's size is 0.
                ret += equationsys.equations().get(i) + ", ";
            }
            for(int i =0; i < sets.size(); i++) {
                 ret += sets.get(i) + ", ";   
            }
            return ret.substring(0, ret.length() - 2);

        }
    }

    @Override
    public String toFancyString(int idtLvl) {
        throw new NotDefinedException();
    }

    @Override
    public String toFullString(int idtLvl) {
        String ret = indent(idtLvl) + "Grapher:";

        ret += "\n" + indent(idtLvl + 1) + "Sets:";
        if(sets.size() == 0)
            ret += "\n" + indent(idtLvl + 2) + "null";
        for(Set s : sets)
            ret += "\n" + s.toFullString(idtLvl + 2);

        ret += "\n" + equationsys.toFullString(idtLvl + 1);
        ret += "\n" + components.toFullString(idtLvl + 1);

        ret += "\n" + indent(idtLvl + 1) + "Displays:";
        if(displays.size() == 0)
            ret += "\n" + indent(idtLvl + 2) + "null";
        for(DisplayComponent d : displays)
            ret += "\n" + d.toFullString(idtLvl + 2);
        return ret;
    }


    @Override
    public Grapher copy(){
        return new Grapher(equationsys, sets, components);

    }

}