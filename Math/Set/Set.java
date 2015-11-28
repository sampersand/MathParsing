package Math.Set;
import Math.Equation.Equation;
import Math.Equation.Factors;
import java.util.HashMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Set {
    // public static final double e = Math.E;
    public double[] matr;
    public double[] matr2;
    public Equation equation;
    public String name;
    public static void main(String[] args) { 
        assert false;
        // Set mtr = new Set(new double[]{90,90,95,100,80,80,75,80,70,60,95,100,100,100,75,80,90,90,90,70,70,80,85,90,90,85});
        Set mtr = new Set(new double[]{-3,-2,-1,0,1,2,3},new double[]{-3,-2,-1,0,1,2,3});
        mtr.graph();

    }   
    public Set(double[] matrix){
        this(matrix, matrix);
    }
    public Set(double[] matrix, String name){
        this(matrix, matrix, name);
    }
    public Set(double[] matrix, double[] matrix2){
        this(matrix,matrix2,"Untitled Set");
    }
    public Set(Set sl1, Set sl2){
        this(sl1.matr,sl2.matr);
    }
    public Set(Set sl1, Set sl2, String name){
        this(sl1.matr,sl2.matr, name);
    }    
    public Set(double[] matrix, double[] matrix2, String name){
        this.matr = matrix;
        this.matr2 = matrix2;
        this.name = name;
        this.equation = linReg();
    }


    public double pred(double val){return pred(val, linReg());}
    public double pred(double val, double[] pMatr, double[] pMatr2){return pred(val, linReg(pMatr, pMatr2));}
    public double pred(double val, String toSolve){ return pred(val, linReg(), toSolve);}
    public double pred(double val, Equation pEq){ return pred(val, pEq, "x");}
    public static double pred(double val, Equation pEq, String toSolve){
        return pEq.solve(toSolve, new HashMap<String, Double>(){{put(toSolve,val);}});
    }
    public double[] es(){return resid();}
    public double[] resid(){
        assert matr.length == matr2.length;
        Equation lreg = linReg();
        double[] resid = new double[matr.length];
        for(int x = 0; x < matr.length; x++)
            resid[x] = pred(matr[x], lreg, "y") - matr2[x];
        return resid;
    }


    public Equation quadReg(){return quadReg(matr, matr2); }
    public static Equation quadReg(double[] x, double[] y){
        Equation eq = new Equation();
        return eq;
    }

    public Equation linReg(){ return linReg(matr, matr2); }
    public static Equation linReg(double[] x, double[] y){
        double b1 = r(x, y) * S(y) / S(x);
        double b0 = avg(y) - b1 * avg(x);
        return new Equation("yhat = b0 + b1 * y", new Factors(new HashMap<String, Double>(){{
                    put("b0", b0);
                    put("b1", b1);
                }}));
    }

    public double r(){ return r(matr, matr2); }
    public static double r(double[] x, double[] y){
        assert x.length == y.length;
        double sigZxZy = 0;
        for(int i = 0; i < x.length; i++)
            sigZxZy += Z(x[i], x) * Z(y[i], y);
        return sigZxZy /( n(x) - 1 ); }

    public double R2(){ return R2(matr, matr2); }
    public static double R2(double[] inp, double[] inp2){ return Math.pow(r(inp, inp2), 2);}

    public int n(){ return n(matr); }
    public static int n(double[] inp){ return inp.length; }

    public double Z(double x){ return Z(x, matr); }
    public static double Z(double x, double[] inp){ return (x - avg(inp)) / stdev(inp); }
    public double[] Z(){ return Z(matr);}
    public static double[] Z(double[] inp){
        double[] ret = new double[inp.length];
        for(int x = 0; x < ret.length; x++)
            ret[x] = Z(inp[x], inp);
        return ret;
    }

    public double avg(){ return avg(matr);}
    public double mean(){ return mean(matr);}
    public static double mean(double[] inp){return avg(inp);}    
    public static double avg(double[] inp){
        assert inp.length > 0;
        double sum = 0;
        for(double i : inp)
            sum += i;
        return sum / inp.length;
    }

    public double[] outliers(){return outliers(matr);}
    public static double[] outliers(double[] inp){
        double std = stdev(inp);
        double mean = mean(inp);
        int count = 0;
        for(double d : inp)
            if(d < mean - std * 1.5 || d > mean + std * 1.5)
                count++;
        double[] ret = new double[count];
        count = 0;
        for(int x = 0; x < inp.length; x++){
            double d = inp[x];
            if(d < mean - std * 1.5 || d > mean + std * 1.5){
                ret[count] = d;
                count++;
            }
        }
        return ret;
    }

    public double stdev(){ return stdev(matr); }
    public double S(){ return stdev(matr); }
    public static double S(double[] inp){ return stdev(inp); }
    public static double stdev(double[] inp){ 
        double Σyȳ = 0;
        double ȳ  = avg(inp);
        for(double y : inp)
            Σyȳ  += Math.pow(y - ȳ, 2);
        return Math.sqrt(Σyȳ  / (n(inp) - 1));
    }

    public double[] fiveNumSum(){ return fiveNumSum(matr); }
    public static double[] fiveNumSum(double[] inp){
        assert inp.length > 0;
        double[] sorted = sort(inp);
        int length = sorted.length;        
        double[] ret = {sorted[0], 0, 0, 0, sorted[length - 1]};
        ret[3] = length % 4 == 0 ? (sorted[length * 3 / 4 - 1] + sorted[length * 3 / 4]) / 2 : 
                 sorted[length * 3 / 4];
        ret[2] = length % 2 == 0 ? (sorted[length / 2 - 1] + sorted[length / 2]) / 2 : 
                 sorted[length / 2];
        ret[1] = length % 4 == 0 ? (sorted[length / 4 - 1] + sorted[length / 4]) / 2 : 
                 sorted[length / 4];
        return ret;
    } // NOT 100% THIS WORKS

    public double iqr(){ return iqr(matr); }
    public static double iqr(double[] inp){
        double[] sum = fiveNumSum(inp);
        return sum[3] - sum[1];
    }

    public double[] sort(){ return sort(matr); }
    public static double[] sort(double[] inp) {
        double[] ret = new double[inp.length];
        for(int i = 0; i < inp.length; i++) ret[i] = inp[i];
        //bubble sort b/c screw it
        int x = 0;
        double temp = 0;
        while(x < ret.length - 1){
            if(ret[x] > ret[x + 1]){
                temp = ret[x + 1];
                ret[x + 1] = ret[x];
                ret[x] = temp;
                x = -1;
            }
            x++;
        }

        return ret;
    }

    public void printBarGraph(){ printBarGraph(matr); }
    public static void printBarGraph(double[] inp){
        double[] fns = fiveNumSum(inp);
        double[] prl = new double[5];
        for(int x = 0; x < fns.length; x++)
            prl[x] = fns[x];

        double iqr = fns[3] - fns[1];

        prl[0] = fns[1] - iqr * 1.5;
        prl[4] = fns[3] + iqr * 1.5;

        boolean is1fence = true, is2fence = true;

        if (fns[0] > prl[0]) { //if the min value is higher than the fence
            prl[0] = fns[0];
            is1fence = false;
        }
        if (fns[4] < prl[4]) { //if the max value is lower than the fence
            prl[4] = fns[4];
            is2fence = false;
        }

        for(int x = 0; x < prl.length; x++ )
            prl[x] = Z(prl[x],inp) * 25 + 50;
        
        double upN =  (double)(is1fence ? (fns[1] - iqr * 1.5) : fns[0]);
        double mdN =  (double)(           (fns[2]            )         );
        double dnN =  (double)(is2fence ? (fns[3] + iqr * 1.5) : fns[4]);
        // String upperS =
        //         "%" + (prl[0])
        //      + "d%" + (prl[2] - prl[0] + (""+upN).length())
        //      + "d%" + (prl[4] - prl[2] - prl[0] + (""+upN).length() + (""+mdN).length())
        //       + "d\n";
        // System.out.printf(upperS, (int)upN, (int)mdN, (int)dnN);
        for(int i = 0; i < 101; i ++){
                 if(i == (int)prl[0]) System.out.print('|');
            else if(i == (int)prl[1]) System.out.print('|');
            else if(i == (int)prl[2]) System.out.print('|');
            else if(i == (int)prl[3]) System.out.print('|');
            else if(i == (int)prl[4]) System.out.print('|');
            else if(i <  prl[0]) System.out.print(' ');            
            else if(i >  prl[0] && i < prl[1]) System.out.print('-');
            else if(i >  prl[1] && i < prl[2]) System.out.print('#');
            else if(i >  prl[2] && i < prl[3]) System.out.print('#');
            else if(i >  prl[3] && i < prl[4]) System.out.print('-');
            else if(i >  prl[4]) System.out.print(' ');
        }
       System.out.println();
    }

    public void printFiveNumSum(){ printFiveNumSum(matr); }
    public static void printFiveNumSum(double[] inp){

        double[] fns = fiveNumSum(inp);

        System.out.printf("Min   : %f%nQ1    : %f%nMed   : %f%nQ2    : %f%nMax   : %f%nMean  : %f%nStdev : %f%n",
            fns[0], fns[1], fns[2], fns[3], fns[4],Set.avg(inp),Set.stdev(inp));
    }
    public void printLinReg(){ printLinReg(matr, matr2); }
    public static void printLinReg(double[] pMatr, double[] pMatr2){
        System.out.println(linReg(pMatr, pMatr2));
    }

    public void graphe(){(new Set(matr,resid(),"Residuals of "+this.name)).graph();}

    public void graph(){
        graph(this);
        // JFrame frame = new JFrame();
        // int xScreenLimit = 1000; // note this is the physical screen limits
        // int yScreenLimit = 1000; // note this is the physical screen limits        
        // frame.setSize(xScreenLimit, yScreenLimit);
        // frame.setTitle(this.name);
        // frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // SetDisplay gdisp = new SetDisplay(this, new int[] { xScreenLimit, yScreenLimit });
        // frame.add(gdisp);        
        // frame.setVisible(true);
    }

    public static void graph(Object... objs){
        JFrame frame = new JFrame();
        int xScreenLimit = 1000; // note this is the physical screen limits
        int yScreenLimit = 1000; // note this is the physical screen limits        
        frame.setSize(xScreenLimit, yScreenLimit);
        frame.setTitle("Graph of stuff");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        SetDisplay gdisp = new SetDisplay(new int[] { xScreenLimit, yScreenLimit }, objs);
        frame.add(gdisp);        
        frame.setVisible(true);
    }


}