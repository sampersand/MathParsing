package West.Math.Set;
import West.Math.Set.NumberCollection;
import West.Math.Equation.EquationSystem;
class NumberCollectionTester {
    public static void main(String[] args) {
        NumberCollection nc1 = new NumberCollection<Double>();
        nc1.addAll(new Double[]{1d, 2d,3d});
        NumberCollection nc2 = new NumberCollection<Double>();
        nc2.addAll(new Double[]{1d, 2d,3d});
        // EquationSystem eqsys = nc1.linReg(nc2);
        // System.out.println(eqsys);
        // eqsys.graph("x", "y");
        nc1.graphWithLinReg(nc2);
    }
}