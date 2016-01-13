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
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import java.util.ArrayList;

public class GraphGUI extends JFrame{
    private ArrayList<JTextField> equations;
    private ArrayList<JTextField> dep;
    private JTextField indep;
    private ArrayList<JTextField> winbounds;
    private ArrayList<JTextField> eqBounds;
    private ArrayList<JTextField> step;
    private ArrayList<JTextField> gtype;
    public GraphGUI(){
        initvars();
        init();
        graph();
    }
    public void initvars(){
        equations = new ArrayList<JTextField>(){{
            add(new JTextField());
        }};

        dep = new ArrayList<JTextField>(){{
            add(new JTextField());
        }};

        indep = new JTextField();

        winbounds = new ArrayList<JTextField>(){{
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
           // Note: This might look like a mess of JPanels. They are strategically placed to make
            // the calculator look an actual calculator.
            // addButtons();
            // for (Enumeration<String> button = buttons.keys(); button.hasMoreElements();) {
                // buttons.get(button.nextElement()).addActionListener(this);
            // }

            JPanel row0 = new JPanel(new GridLayout(1,equations.size()));
            JPanel row1 = new JPanel(new GridLayout(1,dep.size()));
            // JPanel row2 = new JPanel(new GridLayout(1,4));

            // JPanel row3 = new JPanel(new GridLayout(1,4));
            // JPanel row4 = new JPanel(new GridLayout(1,4));
            // JPanel row5 = new JPanel(new GridLayout(1,2));

            // JPanel row5a = new JPanel(new GridLayout(1,1));
            // JPanel row5b = new JPanel(new GridLayout(1,2));

            equations.forEach(row0::add);
            dep.forEach(row1::add);

            // row1.add(buttons.get("clear"));
            // row1.add(buttons.get("posneg"));
            // row1.add(buttons.get("perc"));
            // row1.add(buttons.get("div"));

            // row2.add(buttons.get("7"));
            // row2.add(buttons.get("8"));
            // row2.add(buttons.get("9"));
            // row2.add(buttons.get("mult"));

            // row3.add(buttons.get("4"));
            // row3.add(buttons.get("5"));
            // row3.add(buttons.get("6"));
            // row3.add(buttons.get("sub"));

            // row4.add(buttons.get("1"));
            // row4.add(buttons.get("2"));
            // row4.add(buttons.get("3"));
            // row4.add(buttons.get("add"));


            // row5a.add(buttons.get("0"));
            // row5b.add(buttons.get("deci"));
            // row5b.add(buttons.get("equal"));

            // row5.add(row5a, BorderLayout.WEST);
            // row5.add(row5b, BorderLayout.EAST);

            JPanel row01 = new JPanel(new GridLayout(2, 1));
            row01.add(row0, BorderLayout.NORTH);
            row01.add(row1, BorderLayout.SOUTH);

            // JPanel row23 = new JPanel(new GridLayout(2, 1));
            // row23.add(row2, BorderLayout.NORTH);
            // row23.add(row3, BorderLayout.SOUTH);

            // JPanel row45 = new JPanel(new GridLayout(2, 1));
            // row45.add(row4, BorderLayout.NORTH);
            // row45.add(row5, BorderLayout.SOUTH);

            this.setLayout(new BorderLayout());
            this.add(row01, BorderLayout.NORTH);
            // this.add(row23, BorderLayout.CENTER);
            // this.add(row45, BorderLayout.SOUTH);

            this.pack();
            this.setResizable(false);


    }
   public void graph(){
        eqsys().graph(new GraphComponents(winbounds(),
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
    public int[] winbounds(){
        int[] ret = new int[winbounds.size()];
        for(int i = 0; i < winbounds.size(); i++)
            ret[i] = Integer.parseInt(winbounds.get(i).getText());
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