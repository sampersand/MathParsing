package West.Math.Set;
import West.Math.Set.NumberCollection;
import West.Math.Equation.EquationSystem;
import West.Math.Display.GraphComponents;
import West.Math.Complex;
class NumberCollectionTester {
    public static void main(String[] args) {

        // Collection<Collection<NumberCollection<Complex>>> 
        // 
        // Collection<#####################################>    <-- A collection of data sets (both axis)
        //            Collection<#########################>     <-- A collection of axes [x, y] (single set of data).
        //                       NumberCollection<#######>      <-- A collection of data apoints (A single axis)
        //                                        Complex       <-- A single data point

        Collection<Collection<NumberCollection<Complex>>> datasets =
            new Collection<Collection<NumberCollection<Complex>>>(){{ // new collection of data sets
                add(new Collection<NumberCollection<Complex>>(){{ // a new data set
                    add(new NumberCollection<Complex>(){{ // x axis
                        for(Double d : new Double[]{-3d, -2d, -1d, +0d, +1d, +2d, +3d}) 
                            add(new Complex(d)); // a new element   
                    }});
                    add(new NumberCollection<Complex>(){{ // y axis
                        for(Double d : new Double[]{+9d, +4d, +1d, +0d, +1d, +4d, +9d}) 
                            add(new Complex(d)); // a new element
                    }});
                }});

                add(new Collection<NumberCollection<Complex>>(){{ // a new data set
                    add(new NumberCollection<Complex>(){{ // x axis
                        for(Double d : new Double[]{- 0d, - 1d, - 2d, - 3d, - 4d,  1d}) 
                            add(new Complex(d)); // a new element
                    }});
                    add(new NumberCollection<Complex>(){{ // y axis
                        for(Double d : new Double[]{+ 0d, + 1d, + 2d, + 3d, + 4d, + 2d}) 
                            add(new Complex(d)); // a new element
                    }});
                }});

        }};
        NumberCollection nc1 = new NumberCollection<Double>();
        NumberCollection nc2 = new NumberCollection<Double>();
        nc1.addAll(new Double[]{1d, 2d,3d});
        nc2.addAll(new Double[]{1d, 2d,3d});

        // EquationSystem eqsys = nc1.linReg(nc2);
        // System.out.println(eqsys);
        // eqsys.graph("x", "y");

        NumberCollection.graphMultiWithLinReg(datasets, new GraphComponents());

    }
}