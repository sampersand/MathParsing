package Math.Display.Calculator;

import Math.MathObject;
import Math.Print;
import Math.Equation.EquationSystem;
import Math.Equation.Function.CustomFunction;
import Math.Exception.NotDefinedException;

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


/**
 * The frame that holds all the buttons and displays for the calculator.
 * 
 * @author Sam Westerman
 * @version 0.72
 * @since 0.3
 */
public class CalcWindow extends JFrame implements ActionListener, MathObject {

    /**
     * The text field that keeps track of the used input into the calculator.
     */
    protected JTextField dispField = new JTextField(20);

    /**
     * A list of the <code>JButtons</code> on the calculator screen.
     * The key is the text on the buttons, and the value is an instance of the <code>Jbutton</code>
     * class.
     */
    protected Hashtable <String, JButton> buttons = new Hashtable <String, JButton>();

    /**
     * An instance of the <code>MainClass</code>. 
     * Used when graphing the equation obtained from <code>dispField</code>.
     */
    protected final MainClass MAINCL;

    /**
     * Initializer of the CalcWindow. 
     * 
     * @param cl        An instance of MainClass, used currently solely to graph equations.
     * @param title     The title of the window - usually "Calculator".
     */
    public CalcWindow(MainClass cl, String title) {
        super(title);
        this.setSize(400, 650);
        this.MAINCL = cl;
        this.init();
        this.setVisible(true);
    }

    /**
     * Creates the window, and puts all the different buttons / input boxes in it.
     */
    private void init() {
            // Note: This might look like a mess of JPanels. They are strategically placed to make
            // the calculator look an actual calculator.
            addButtons();
            for (Enumeration<String> button = buttons.keys(); button.hasMoreElements();) {
                buttons.get(button.nextElement()).addActionListener(this);
            }

            JPanel row0 = new JPanel(new GridLayout(1,1));
            JPanel row1 = new JPanel(new GridLayout(1,4));
            JPanel row2 = new JPanel(new GridLayout(1,4));

            JPanel row3 = new JPanel(new GridLayout(1,4));
            JPanel row4 = new JPanel(new GridLayout(1,4));
            JPanel row5 = new JPanel(new GridLayout(1,2));

            JPanel row5a = new JPanel(new GridLayout(1,1));
            JPanel row5b = new JPanel(new GridLayout(1,2));

            row0.add(dispField);

            row1.add(buttons.get("clear"));
            row1.add(buttons.get("posneg"));
            row1.add(buttons.get("perc"));
            row1.add(buttons.get("div"));

            row2.add(buttons.get("7"));
            row2.add(buttons.get("8"));
            row2.add(buttons.get("9"));
            row2.add(buttons.get("mult"));

            row3.add(buttons.get("4"));
            row3.add(buttons.get("5"));
            row3.add(buttons.get("6"));
            row3.add(buttons.get("sub"));

            row4.add(buttons.get("1"));
            row4.add(buttons.get("2"));
            row4.add(buttons.get("3"));
            row4.add(buttons.get("add"));


            row5a.add(buttons.get("0"));
            row5b.add(buttons.get("deci"));
            row5b.add(buttons.get("equal"));

            row5.add(row5a, BorderLayout.WEST);
            row5.add(row5b, BorderLayout.EAST);

            JPanel row01 = new JPanel(new GridLayout(2, 1));
            row01.add(row0, BorderLayout.NORTH);
            row01.add(row1, BorderLayout.SOUTH);

            JPanel row23 = new JPanel(new GridLayout(2, 1));
            row23.add(row2, BorderLayout.NORTH);
            row23.add(row3, BorderLayout.SOUTH);

            JPanel row45 = new JPanel(new GridLayout(2, 1));
            row45.add(row4, BorderLayout.NORTH);
            row45.add(row5, BorderLayout.SOUTH);

            this.setLayout(new BorderLayout());
            this.add(row01, BorderLayout.NORTH);
            this.add(row23, BorderLayout.CENTER);
            this.add(row45, BorderLayout.SOUTH);

            this.pack();
            this.setResizable(false);
    }

    /**
     * Just adds different buttons to the <code>buttons</code> list.
     *  This puts a key value pair into the <code>buttons</code> list, with the key being the text
     *  on the button itself, and the value being a <code>JButton</code>.
     */
    private void addButtons() {
            buttons.put("div", new JButton("÷"));
            buttons.put("mult", new JButton("x"));
            buttons.put("sub", new JButton("-"));

            buttons.put("add", new JButton("+"));
            buttons.put("clear", new JButton("AC"));
            buttons.put("posneg", new JButton("+/-"));

            buttons.put("deci", new JButton("."));
            buttons.put("equal", new JButton("="));
            buttons.put("perc", new JButton("%"));

            buttons.put("1", new JButton("1"));
            buttons.put("2", new JButton("2"));
            buttons.put("3", new JButton("3"));

            buttons.put("4", new JButton("4"));
            buttons.put("5", new JButton("5"));
            buttons.put("6", new JButton("6"));

            buttons.put("7", new JButton("7"));
            buttons.put("8", new JButton("8"));
            buttons.put("9", new JButton("9"));
            buttons.put("0", new JButton("0"));
    }

    /**
     * Called whenever an action is performed, usually pressing a button or typing text.
     * This thing handles all of the button presses, and is responsible for calling the 
     * <code>setUpAndDrawGraph</code> function in <code>MainClass</code>.
     * <p>
     * Note: To actually graph the function, the equation has to be preceded by an <code>=</code>.
     * <p>
     * An Example: <code>=2x+1</code> would graph the equation <code>y=2x+1</code>.
     * @param event         The action that took place. The source should be either a 
     *                      <code>JButton</code> or a <code>JTextField</code>.
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() instanceof JButton) {
            JButton button = (JButton) event.getSource();
            try {
                int num = Integer.parseInt(button.getText());
                dispField.setText(dispField.getText() + num);
            } catch (NumberFormatException err) {
                String text = button.getText();
                String eqText = dispField.getText(); // just to make it easier to use
                switch(text) {
                    case "." :
                        dispField.setText(eqText + ".");
                        break;
                    case "+" :
                        dispField.setText(eqText + "+");
                        break;
                    case "-" :
                        dispField.setText(eqText + "-");
                        break;
                    case "x" :
                        dispField.setText(eqText + "*");
                        break;
                    case "÷" :
                        dispField.setText(eqText + "/");
                        break;
                    case "%" :
                        dispField.setText(eqText + "%");
                        break;
                    case "AC" :
                        dispField.setText("");
                        break;
                    case "+/-" :
                        dispField.setText(changeSign("=" + eqText));
                        break;
                    case "=" :
                        String temp = getResultWithGraph("=" + eqText);
                        if (!temp.equals("")) {
                           JOptionPane.showMessageDialog(this, "'" + eqText + "' = " + temp);
                        }
                        break;
                    default :
                        break;
                } 
            }
        }
    }

    /**
     * This function decides whether to display the popup box with the answer, or graphs it
     * This function checks whether or not the <code>rawEq</code> starts with <code>==</code>. The 
     * first <code>=</code> is always put in by <code>actionPerformed</code>, but the second one is
     * inputted by the user to tell the program to graph it.
     * <p>
     * If a graph is meant to be displayed, the <code>setUpAndDrawGraph</code> function 
     * in <code>MainClass</code> (stored as the local varriable <code>MAINCL</code>) does so. 
     * <p>
     * If a graph isn't meant to be displayed, <code>rawEq</code> is passed onto the 
     * <code>getResult</code> function.
     *
     * @param rawEq         The equation that will be decided whether or not to be graphed. 
     *                      To graph it, the equation should be preceded by <code>=</code>.
     * @return              Either returns a String representation of the answer, or if the equation
     *                      was to be graphed, an empty string (which <code>actionPerformed</code>
     *                      just ignores)
     * @see getResult
     * @see MainClass.setUpAndDrawGraph
     * @see actionPerformed
     */
    public String getResultWithGraph(String rawEq) {
        if (rawEq.indexOf("==") == -1) { // a beginning '=' is always passed
            // try {
            return getResult(rawEq);
            // } catch (Exception err) {
                // System.err.println(err);
            // }

        }
        MAINCL.setUpAndDrawGraph(rawEq.substring(1));
        return "";
    }

    /**
     * Gets the numerical result of the inputted equation.
     * This uses <code>javax.script.ScriptEngineManager</code> to compile the equation into
     * JavaScript, and then executes it.
     * <p>
     * This is directly called from <code>actionPerformed</code>, passing the user-inputted 
     * equation, which can lead to user-inputted JavaScript code being compiled and run ( :O )!
     *
     * @param rawEq             The equation which will be run in JavaScript to get the answer.
     * @return                  A String representation of the resulting <code>double</code> answer.
     *
     * @throws IllegalArgumentException     Thrown when the arguments (like for functions, etc) arent valid.
     *                                  the equation (like <code>2++4</code>). General error.
     * @throws NumberFormatException    Thrown by <code>ScriptEngine</code> if there is a problem
     *                                  with the way the numbers are formatted
     *                                  (like <code>2+4.2.2</code>).
     */
    public static String getResult(String rawEq) throws IllegalArgumentException {
        rawEq = rawEq.replaceAll("%", "*100").replaceAll("\\*\\*", "^");
        String[] split = rawEq.trim().replaceAll(" ","").split(";");
        if(split.length == 1) {
            return "" + new EquationSystem().add(split[0]).eval("x");
        } else {
            HashMap<String,Double> vars = new HashMap<String,Double>() {{
                if(!split[1].equals("")) {
                    String[] split1 = split[1].split(",");
                    for(int i = 0; i < split1.length; i++) {
                        String[] spl = split1[i].split(":");
                        if(spl.length != 2) {
                            throw new IllegalArgumentException("Cannot get the result of rawEq! When passing vars, the "
                                + "format has to be 'Name:Val'");
                        } else {
                            try {
                                put(spl[0], Double.parseDouble(spl[1]));
                            } catch (NumberFormatException err) {
                                throw new IllegalArgumentException("Cannot get the result of rawEq! When passing vars, " +
                                "their values have to be doubles!");
                            }
                        }
                    }
                }
            }};
            HashMap<String,CustomFunction> funcs = new HashMap<String,CustomFunction>() {{
                if(!split[2].equals("")) {
                    String[] split2 = split[2].split(",");
                    for(int i = 0; i < split2.length; i++) {
                        String[] spl = split2[i].split(":");
                        if(spl.length == 1) {
                            put(spl[0], new CustomFunction(spl[0]));
                        } else if (spl.length == 2) {
                            put(spl[0], new CustomFunction(spl[1]));
                        } else {
                            throw new IllegalArgumentException("Cannot get the result of rawEq! When passing funcs, they "+
                                " have to be in format 'Name:File', or 'Name'");
                        }
                    }
                }
            }};
            throw new NotDefinedException();
            // return "" + new EquationSystem().add(split[0]).add(vars).add(funcs).eval();
        }
        
    }

    /**
     * Gets the text from the <code>JTextField dispField</code>.
     * @return          The text inside the <code>dispField</code>.
     */
    public String getEquation() {
        return dispField.getText();
    }

    /**
     * Takes the equation and flips the sign on the last value (from + -> - or - -> +).
     * NOTE: This thing is fairly unstable, but is only used when the "+/-" button is pushed.
     * @param rawEq     The equation which will have it's last value's sign swapped. Needs an 
     *                  <code>=</code> at the beginning.
     * @return          A string with the sign flipped, and without an equals sign at the beginning.
     */
    private String changeSign(String rawEq) {
        String PLS_TO_MIN_REG = "(?:(?:^=)|(\\+))([^-+*/]+?)$";
        String MIN_TO_PLS_REG = "(?:(?:^=)|((?<!\\*|/)-))([^-+*/]+?)$";
        String DIV_MULT_TO_NEG_REG = "(?:(?:^=)|((?:\\*(?!-))|(?:/(?!-))))([^-+*/]+?)$";
        String NEG_DIV_TO_POS_REG = "(?:(?:^=)|(/-))([^-+*/]+?)$";
        String NEG_MULT_TO_POS_REG = "(?:(?:^=)|(\\*-))([^-+*/]+?)$";
        String rawStr = rawEq;

        rawEq = rawEq.replaceAll(PLS_TO_MIN_REG, "-$2");
        if(!rawEq.equals(rawStr)) {
            return rawEq.replaceFirst("^=", "");
        }

        rawEq = rawEq.replaceAll(MIN_TO_PLS_REG, "+$2");
        if(!rawEq.equals(rawStr)) {
            return rawEq.replaceFirst("^=", "");
        }

        rawEq = rawEq.replaceAll(DIV_MULT_TO_NEG_REG, "$1-$2");
        if(!rawEq.equals(rawStr)) {
            return rawEq.replaceFirst("^=", "");
        }

        rawEq = rawEq.replaceAll(NEG_DIV_TO_POS_REG, "/$2");
        if(!rawEq.equals(rawStr)) {
            return rawEq.replaceFirst("^=", "");
        }

        rawEq = rawEq.replaceAll(NEG_MULT_TO_POS_REG, "*$2");
        if(!rawEq.equals(rawStr)) {
            return rawEq.replaceFirst("^=", "");
        }

        return rawEq.replaceFirst("^=", "");
    }

    @Override
    public String toString() {
        throw new NotDefinedException();
    }
    
    @Override
    public String toFancyString(int idtLvl) {
        throw new NotDefinedException();
    }

    @Override
    public String toFullString(int idtLvl) {
        throw new NotDefinedException();
    }

    @Override
    public CalcWindow copy(){
        throw new NotDefinedException();
    }
    
    @Override
    public boolean equals(Object pObj){
        throw new NotDefinedException();
    }
}
