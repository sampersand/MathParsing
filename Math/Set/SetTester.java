package Math.Set;
import java.util.ArrayList;
import static Math.Declare.*;
import java.io.File;
import java.io.FileNotFoundException;

import java.util.Scanner;

/**
 * This is just a hacked up thing I used to find the correlation values of data in <code>Math/Set/data.csv</code>.
 * 
 * @author Sam Westerman
 * @version 0.9
 */
public class SetTester {
    public File file;
    public ArrayList<String[]> lines;
    public ArrayList<String> header;
    public static void main(String[] args) {
        SetTester st = new SetTester("Math/Set/data.csv");
        st.print();
        st.findfit();
    }
    public SetTester(String fileName){
        file = new File(fileName);
        Scanner in;
        try{
            in = new Scanner(file);
        }
        catch(FileNotFoundException err){
            System.err.println("Error! The file '" + file + "' doesn't exist!");
            return;
        }

        lines = new ArrayList<String[]>();
        header = new ArrayList<String>(){{for(String s : in.nextLine().split(",")) add(s);}};
        while(in.hasNextLine())
            lines.add(in.nextLine().split(","));
    }
    public void findfit(){
        double[] ar1;
        double[] ar2;
        int x = 0;
        for(int i = 0; i < header.size(); i++)
            for(int j = i + 1; j < header.size(); j++)
                x += j;
        double[] bestfit = new double[x];
        x = 0;
        for(int h1 = 0; h1 < header.size(); h1++){
            for(int h2 = h1 + 1; h2 < header.size(); h2++){
                ar1 = new double[lines.size()];
                ar2 = new double[lines.size()];
                for(int r = 0; r < lines.size(); r++){
                    String line[] = lines.get(r);
                    for(int i = 0; i < line.length; i++){
                        if(i == h1){
                            ar1[r] = Double.parseDouble(line[i]);
                        } else if(i == h2){
                            ar2[r] = Double.parseDouble(line[i]);
                        }
                    }
                }
                x++;
                bestfit[x] = Set.R2(ar1, ar2);
                System.out.println(Set.R2(ar1, ar2) * 100 + "= R2 for " + header.get(h1) + " vs " + header.get(h2));
            }
        }
        bestfit = Set.sort(bestfit);
        for(double d : bestfit)
            System.out.println(d*100);

    }
    public void print(){
        for(String head : header)
            System.out.print(head + "\t");
        System.out.println();
        for(String[] stra : lines){
            for(String str : stra)
                System.out.print(str + "\t");
            System.out.println();
        }
    }

}