package West.Math.Equation;

import West.Math.MathObject;
import West.Math.Exception.NotDefinedException;

/**
 * A class that models an expression, and its behaviour.
 * @author Sam Westerman
 * @version 0.2
 */

public class Expression implements MathObject {

    /** The raw expression. */
    protected String expression;

    /**
     * The Node representing the whole expression. 
     * @see   Node#generateMasterNode(ArrayList)
     */
    protected Node node;

    /**
     * The default constructor for the Expression class. Just passes an empty String, and Node to the 
     * {@link #Expression(String,Node) main expression constructor}.
     */
    public Expression() {
        this("", new Node());
    }



    /**
     * The Object - only constructor for the expression class. Just passes <code>pObj</code>, and a Node based off
     * <code>pObj</code>, {@link #Expression(String,Node) main expression constructor}.
     * @param pObj       An Object that this class will be modeled after. 
     */ 
    public Expression(Object pObj) {
        throw new NotDefinedException();
        // this("" + pObj, Node.generateMasterNode(Expression.parseTokens("" + pObj)));
    }

    /**
     * The Node-only for the expression class. Just passes a String created based on Node's subnodes to the
     * {@link #Expression(String,Node) main expression constructor}.
     * @param pNode        The Node that the expression class will be modeled after.
     */ 
    public Expression(Node pNode) {
        this(pNode.genEqString(), pNode);
    }


    /**
     * The main constructor for the expression class. Just sets {@link #expression} and {@link #node} to their
     * respective constructors
     * @param pEq       The String representation of the expression. Is only ever used to identify individual expressions.
     * @param pNode        The Node that the entire expression is based off of.
     */ 
    public Expression(String pEq,
                      Node pNode) {
        expression = pEq.trim().replaceAll(" ","");
        node = pNode;
    }


    /**
     * Returns {@link #node} - the {@link Node} that represents this class's {@link #expression}.
     * @return {@link #node} - the {@link Node} that represents this class's {@link #expression}.
     */
    public Node node(){
        return node;
    }

    /**
     * Returns {@link #expression} - the String that this class is modeled after.
     * @return {@link #expression} - the String that this class is modeled after.
     */
    public String expression(){
        return expression;
    }

    /**
     * Takes {@link #expession} and returns it formatted cleanly. <code>A+B</code> becomes <code>A + B</code>, etc.
     * @return A cleanly formatted {@link #expression}.
     */
    public String formattedExpression(){
        return expression.replaceAll("(\\+|\\-|\\*|/|\\^)", " $1 ").replaceAll(",", ", ");
    }

    @Override
    public String toString() {
        return "Expression '" + formattedExpression() + "'";
    }

    @Override
    public String toFancyString(int idtLvl) {
        return indent(idtLvl) + "Expression:\n" + indent(idtLvl + 1 ) +
                "Raw Expression = " + formattedExpression() + "\n" + indentE(idtLvl + 1) + "Generated Expression = " +
                node.genEqString().replaceAll("(\\+|\\-|\\*|/|\\^)", " $1 ").replaceAll(",", ", ");

    }

    @Override
    public String toFullString(int idtLvl) {
        String ret = indent(idtLvl) + "Expression:\n";
        ret += indent(idtLvl + 1) + "Raw Expression:\n" + indentE(idtLvl + 2) + expression + "\n";
        ret += indent(idtLvl + 1) + "Expression Node:\n" + node.toFullString(idtLvl + 2);
        return ret + "\n" + indentE(idtLvl + 2) + "\n" + indentE(idtLvl + 1);
    }

    @Override
    public Expression copy(){
        return new Expression(expression, node);
    }

    @Override
    public boolean equals(Object pObj){
        throw new NotDefinedException();
    }
}
