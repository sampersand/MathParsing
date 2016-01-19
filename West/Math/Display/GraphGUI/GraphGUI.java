package West.Math.Display.GraphGUI;
import West.Math.Equation.Equation;
import West.Math.Equation.EquationSystem;
import West.Math.Display.GraphComponents;
import static West.Math.Display.GraphComponents.GraphTypes.*;
import static West.Math.Display.GraphComponents.GraphTypes;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Hashtable;
import java.util.HashMap;

import java.util.Enumeration;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import java.util.ArrayList;


/**
 * The graphing GUI inteface - where you plug in all the equations
 * 
 * @author Sam Westerman
 * @version 1.1
 * @since 0.90
 */
public class GraphGUI extends JFrame implements ActionListener{
    private ArrayList<JTextField> equations;
    private ArrayList<JTextField> dep;
    private JTextField indep;
    private ArrayList<JTextField> winBounds;
    private ArrayList<JTextField> eqBounds;
    private ArrayList<JTextField> step;
    private GraphTypes gtype;
    public GraphGUI(){
        this("Graphing Calculator");
    }
    public GraphGUI(String title){
        super(title);
        // this.setPreferredSize(new java.awt.Dimension(400, 650));
        initXY();
        init();
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(true);

    }
    public void initXY(){
        equations = new ArrayList<JTextField>(){{
            add(new JTextField("y="));
        }};

        dep = new ArrayList<JTextField>(){{
            add(new JTextField("y"));
        }};

        indep = new JTextField("x");

        winBounds = new ArrayList<JTextField>(){{
            add(new JTextField("850")); //width
            add(new JTextField("850")); //height
        }};

        eqBounds = new ArrayList<JTextField>(){{
            add(new JTextField("-10")); //min x
            add(new JTextField("-10")); //min y
            add(new JTextField("10"));  //max x
            add(new JTextField("10"));  //max y
        }};

        step = new ArrayList<JTextField>(){{
            add(new JTextField("")); //min x
            add(new JTextField("")); //max x
            add(new JTextField("1000")); //amount of drawn
        }};
        gtype = XY;
    }

    public void initPolar(){
        //3.5 - 1.5·abs(cos(Θ))·√(1.3 + abs(sin(Θ))) + cos(2·Θ) - 3·sin(Θ) + 0.7·cos(12.2·Θ)
        equations = new ArrayList<JTextField>(){{
            add(new JTextField("r="));
        }};

        dep = new ArrayList<JTextField>(){{
            add(new JTextField("r0"));
        }};

        indep = new JTextField("theta");

        winBounds = new ArrayList<JTextField>(){{
            add(new JTextField("850")); //width
            add(new JTextField("850")); //height
        }};

        eqBounds = new ArrayList<JTextField>(){{
            add(new JTextField("-4·π")); //min x
            add(new JTextField("-4·π")); //min y
            add(new JTextField("4·π"));  //max x
            add(new JTextField("4·π"));  //max y
        }};

        step = new ArrayList<JTextField>(){{
            add(new JTextField("")); //min x
            add(new JTextField("")); //max x
            add(new JTextField("1000")); //amount of drawn
        }};
        gtype = POLAR;
    }


    private javax.swing.AbstractButton addActionListener(javax.swing.AbstractButton jb, ActionListener al){
        jb.addActionListener(al);
        return jb;
    }

    private JTextField addActionListener(JTextField jtf, ActionListener al){
        jtf.addActionListener(al);
        return jtf;
    }
    private void init(){
        assert eqBounds.size() == 4;
        assert winBounds.size() == 2;
        assert step.size() == 3;
        //winBounds
            JPanel wbvalsJP = new JPanel(new GridLayout(1,2));
            winBounds.forEach(tb -> wbvalsJP.add(addActionListener(tb, al -> dotb(tb, al))));
            JPanel wbJP = new JPanel(new GridLayout(2,1));
            wbJP.add(new JLabel(" Window Bounds –– (Window X, Window Y) "));
            wbJP.add(wbvalsJP);
        //eqBounds
            JPanel eqbvarsJP = new JPanel(new GridLayout(1,4));
            eqBounds.forEach(tb -> eqbvarsJP.add(addActionListener(tb, al -> dotb(tb, al))));
            JPanel eqbJP = new JPanel(new GridLayout(2,1));
            eqbJP.add(new JLabel(" Equation Bounds –– (Min X, Min Y, Max X, Max Y) "));
            eqbJP.add(eqbvarsJP);
        //step
            JPanel stepvarsJP = new JPanel(new GridLayout(1,4));
            step.forEach(tb -> stepvarsJP.add(addActionListener(tb, al -> dotb(tb, al))));
            JPanel stepJP = new JPanel(new GridLayout(2,1));
            stepJP.add(new JLabel(" Equation Bounds –– (Start, End, Step) "));
            stepJP.add(stepvarsJP);
        //Graph types
            JPanel gtypevarJP = new JPanel(new GridLayout(1,2));
            gtypevarJP.add(addActionListener(new JRadioButton("Cartesian",gtype == XY),this));
            gtypevarJP.add(addActionListener(new JRadioButton("Polar",gtype == POLAR),this));
            JPanel gtypeJP = new JPanel(new GridLayout(2,1));
            gtypeJP.add(new JLabel(" Coordinate System "));
            gtypeJP.add(gtypevarJP);
        //all bounds
            JPanel boundsJP = new JPanel(new GridLayout(2,2));
            boundsJP.add(eqbJP);
            boundsJP.add(wbJP);
            boundsJP.add(stepJP);
            boundsJP.add(gtypeJP);

        //Variables
            JPanel varJP = new JPanel(new GridLayout(4 + dep.size(),1));
            varJP.add(new JLabel("Independent Variable"));
            varJP.add(indep);
            varJP.add(new JLabel("Dependent Variables"));

            JPanel varButtonJP = new JPanel(new GridLayout(1,2));
            varButtonJP.add(addActionListener(new JButton("Add Variable"), this));
            varButtonJP.add(addActionListener(new JButton("Remove Variable"), this));

            varJP.add(varButtonJP);
            dep.forEach(tb -> varJP.add(addActionListener(tb, al -> dotb(tb, al))));

        //Equations
            JPanel eqJP = new JPanel(new GridLayout(2+ equations.size(),1));
            eqJP.add(new JLabel("Equations"));

            JPanel eqButtonJP = new JPanel(new GridLayout(1,2));
            eqButtonJP.add(addActionListener(new JButton("Add Equation"), this));
            eqButtonJP.add(addActionListener(new JButton("Remove Equation"),this));

            eqJP.add(eqButtonJP);
            equations.get(0).requestFocusInWindow();
            equations.forEach(tb -> eqJP.add(addActionListener(tb, al -> dotb(tb, al))));

        // all inputs
            JPanel eqvarJP = new JPanel(new GridLayout(2,1));
            eqvarJP.add(varJP);
            eqvarJP.add(eqJP);

        // Graph button
            JPanel graphJP = new JPanel(new GridLayout(1,1));
            graphJP.add(addActionListener(new JButton("Go!"), t -> {
                    if(indep.getText().isEmpty())
                        for(Equation eq : eqsys().equations())
                            JOptionPane.showMessageDialog(this,
                            eq.subEquations().get(0).get(0).toString() + " = " +
                            eqsys().eval(eq.subEquations().get(0).get(0).toString()));
                    else
                        graph();
                    }
            ));
        graphJP.requestFocusInWindow();

        //PUTTING THEM IN THIS
        this.setLayout(new BorderLayout());
        this.add(graphJP, BorderLayout.SOUTH);
        this.add(boundsJP, BorderLayout.NORTH);
        this.add(eqvarJP, BorderLayout.CENTER);

        this.pack();
    }
    private ActionListener dotb(JTextField jtf, ActionEvent al){
        return t -> {System.out.println(t);};
    }
    public void graph(){
        System.out.println("graphing!");
        eqsys().graph(new GraphComponents(winBounds(),
                                        eqBounds(),
                                        step(),
                                        gtype(),
                                        indep(),
                                        dep()));
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if(event.getSource() instanceof JRadioButton){
            String val = ((JRadioButton)event.getSource()).getText();
            if (val.equals("Polar"))
                initPolar();
            else
                initXY();
            getContentPane().removeAll();
            init();
        }
        if (event.getSource() instanceof JButton) {
            String val = ((JButton) event.getSource()).getText();
            switch(val){
                case "Add Equation":
                    equations.add(new JTextField());
                    getContentPane().removeAll();
                    init();
                    break;
                case "Add Variable":
                    dep.add(new JTextField());
                    getContentPane().removeAll();
                    init();
                    break;
                case "Remove Equation":
                    if(dep.size() > 1)
                        equations.remove(equations.size() - 1);
                    getContentPane().removeAll();
                    init();
                    break;
                case "Remove Variable":
                    if(dep.size() > 1)
                        dep.remove(dep.size() - 1);
                    getContentPane().removeAll();
                    init();
                    break;
                default:
                    assert false;
            }
        }
    }

    public EquationSystem eqsys(){
        EquationSystem ret = new EquationSystem();
        for(JTextField jt : equations)
            if(!jt.getText().isEmpty() && !jt.getText().matches("^.*=$"))
                ret.add(jt.getText());
        return ret;
    }
    public int[] winBounds(){
        int[] ret = new int[winBounds.size()];
        for(int i = 0; i < winBounds.size(); i++)
            ret[i] = Integer.parseInt(winBounds.get(i).getText());
        return ret;
    }

    public double[] eqBounds(){
        double[] ret = new double[eqBounds.size()];
        EquationSystem eqsys = eqsys();
        for(int i = 0; i < eqBounds.size(); i++){
            ret[i] = eqsys.eval("__TEMP__", new EquationSystem().add("__TEMP__="+eqBounds.get(i).getText()));
        }
        return ret;
    }

    public double[] step(){
        double[] ret = new double[step.size()];
        EquationSystem eqsys = eqsys();
        for(int i = 0; i < step.size(); i++){
            if(step.get(i).getText().isEmpty() && i <= 2)
                ret[i] = eqBounds()[2 * i];
            else
                ret[i] = eqsys.eval("__TEMP__",new EquationSystem().add("__TEMP__="+step.get(i).getText()));
        }
        return ret;
    }

    public GraphTypes gtype(){
        return gtype;
    }

    public String indep(){
        return indep.getText();
    }

    public String[] dep(){
        ArrayList<String> ret2 = new ArrayList<String>(){{
            for(JTextField jt : dep)
                if(eqsys().exprExists(jt.getText()))
                    add(jt.getText());

        }};
        if(ret2.isEmpty())
            ret2.add(eqsys().equations().get(0).subEquations().get(0).get(0).toString());
        String[] ret = new String[ret2.size()];
        for(int i = 0; i < ret2.size(); i++)
            ret[i] = ret2.get(i);
        return ret;
    }

}