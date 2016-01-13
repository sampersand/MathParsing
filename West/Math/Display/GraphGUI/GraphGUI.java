package West.Math.Display.GraphGUI;
import West.Math.Equation.Equation;
import West.Math.Equation.EquationSystem;
import West.Math.Display.GraphComponents;

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
import java.util.ArrayList;

public class GraphGUI extends JFrame{
    private ArrayList<JTextField> equations;
    private ArrayList<JTextField> dep;
    private JTextField indep;
    private ArrayList<JTextField> winBounds;
    private ArrayList<JTextField> eqBounds;
    private ArrayList<JTextField> step;
    private ArrayList<JTextField> gtype;

    public GraphGUI(){
        this("unknown graph");
    }
    public GraphGUI(String title){
        super(title);
        this.setSize(400, 650);
        initvars();
        init();
        this.setVisible(true);
        // graph();
    }
    public void initvars(){
        equations = new ArrayList<JTextField>(){{
            add(new JTextField("A"));
            add(new JTextField("B"));
            add(new JTextField("C"));
        }};

        dep = new ArrayList<JTextField>(){{
            add(new JTextField());
        }};

        indep = new JTextField();

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
            add(new JTextField("-10")); //min x
            add(new JTextField("10")); //max x
            add(new JTextField("1000")); //amount of drawn
        }};
        gtype = new ArrayList<JTextField>(){{
            add(new JTextField("XY")); 
        }};
    }
    public void init(){
        //Variables
            JPanel varJP = new JPanel(new GridLayout(5 + dep.size(),1));
            varJP.add(new JLabel("Independent Variable"));
            varJP.add(indep);
            varJP.add(new JLabel("Dependent Variables"));
            varJP.add(new JButton("Add Variable"));
            varJP.add(new JButton("Remove Variable"));
            dep.forEach(varJP::add);

        //Equations
            JPanel eqJP = new JPanel(new GridLayout(3 + equations.size(),1));
            eqJP.add(new JLabel("Equations"));
            eqJP.add(new JButton("Add Equation"));
            eqJP.add(new JButton("Remove Equation"));
            equations.forEach(eqJP::add);
        //winBounds
            JPanel wbJP = new JPanel(new GridLayout(1,4));
            winBounds.forEach(wbJP::add);


            JPanel eqvarJP = new JPanel(new GridLayout(2,1));
            eqvarJP.add(varJP, BorderLayout.NORTH);
            eqvarJP.add(eqJP, BorderLayout.SOUTH);

            this.setLayout(new BorderLayout());

            this.add(wbJP, BorderLayout.NORTH);
            // this.add(eqvarJP, BorderLayout.SOUTH);

            this.pack();
            this.setResizable(true);


    }
   public void graph(){
        eqsys().graph(new GraphComponents(winBounds(),
                                        eqBounds(),
                                        step(),
                                        gtype(),
                                        indep(),
                                        dep()));
    }


    public EquationSystem eqsys(){
        EquationSystem ret = new EquationSystem();
        for(JTextField jt : equations)
            if(!jt.getText().isEmpty())
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
        for(int i = 0; i < step.size(); i++)
            ret[i] = eqsys.eval("__TEMP__",new EquationSystem().add("__TEMP__="+step.get(i).getText()));
        return ret;
    }

    public GraphComponents.GraphTypes gtype(){
        System.out.println("gtype not defined completely");
        return GraphComponents.GraphTypes.XY;
    }

    public String indep(){
        return indep.getText();
    }

    public String[] dep(){
        String[] ret = new String[dep.size()];
        for(int i = 0; i < dep.size(); i++)
            ret[i] = dep.get(i).getText();
        return ret;
    }

}