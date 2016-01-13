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
    private JTextField[] winbounds;
    private JTextField[] eqBounds;
    private JTextField[] step;
    private JTextField[] gtype;
    private ArrayList<JTextField> equations;
    private JTextField indep;
    private ArrayList<JTextField> dep;
    public GraphGUI(){
        init();
        graph();
    }
    public void init(){
        equations = new ArrayList<JTextField>();
        String indep = "";
        ArrayList<String> dep = new ArrayList<String>();

        GraphComponents.GraphTypes gtype = GraphComponents.GraphTypes.XY;

        int[] winbounds = new int[]{810, 810};
        double[] eqBounds = new double[]{-10, -10, 10, 10};
        double[] step = new double[]{1000};



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
        equations.forEach(t -> ret.add(new Equation(t.getText())));
        return ret;
    }
    public int[] winbounds(){
        int[] ret = new int[winbounds.length];
        for(int i = 0; i < winbounds.length; i++)
            ret[i] = Integer.parseInt(winbounds[i].getText());
        return ret;
    }

    public double[] eqBounds(){
        double[] ret = new double[eqBounds.length];
        EquationSystem eqsys = eqsys();
        for(int i = 0; i < eqBounds.length; i++)
            ret[i] = eqsys.eval(step[i].getText());
        return ret;
    }

    public double[] step(){
        double[] ret = new double[step.length];
        EquationSystem eqsys = eqsys();
        for(int i = 0; i < step.length; i++)
            ret[i] = eqsys.eval(step[i].getText());
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