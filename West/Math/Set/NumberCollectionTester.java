package West.Math.Set;
import West.Math.Set.NumberCollection;
import West.Math.Equation.EquationSystem;
import West.Math.Display.GraphComponents;
import West.Math.Complex;
import java.util.List;
class NumberCollectionTester {
    public static void main(String[] args) {

        // Collection<Collection<NumberCollection<Complex>>> 
        // 
        // Collection<#####################################>    <-- A collection of data sets (both axis)
        //            Collection<#########################>     <-- A collection of axes [x, y] (single set of data).
        //                       NumberCollection<#######>      <-- A collection of data apoints (A single axis)
        //                                        Complex       <-- A single data point

        Collection<List<NumberCollection<Complex>>> datasets =
            new Collection<List<NumberCollection<Complex>>>(){{ // new collection of data sets
                add(new Collection<NumberCollection<Complex>>(){{ //NUM = 1
                    add(new NumberCollection<Complex>(){{ // MAX
                        for(Double d : new Double[]
                        {1d, 2d, 3d, 4d, 5d, 6d, 7d}) 
                            add(new Complex(d));
                    }});
                    add(new NumberCollection<Complex>(){{ // TIME
                        for(Double d : new Double[]
                        {0.02d, 0.03d, 0.06d, 0.49d, 0.99d, 12.29d, 242.57d})
                            add(new Complex(d));
                    }});
                }});

                add(new Collection<NumberCollection<Complex>>(){{ //NUM = 2
                    add(new NumberCollection<Complex>(){{ // MAX
                        for(Double d : new Double[]
                        {1d, 2d, 3d, 4d, 5d, 6d, 7d}) 
                            add(new Complex(d));
                    }});
                    add(new NumberCollection<Complex>(){{ // TIME
                        for(Double d : new Double[]
                        {0.0d, 0.01d, 0.02d, 0.06d, 1.05d, 11.76d, 381.31d})
                            add(new Complex(d));
                    }});
                }});
                add(new Collection<NumberCollection<Complex>>(){{ //NUM = 3
                    add(new NumberCollection<Complex>(){{ // MAX
                        for(Double d : new Double[]
                        {1d, 2d, 3d, 4d, 5d, 6d, 7d}) 
                            add(new Complex(d));
                    }});
                    add(new NumberCollection<Complex>(){{ // TIME
                        for(Double d : new Double[]
                        {0.02d, 0.02d, 0.0d, 0.13d, 0.99d, 11.95d, 336.29d})
                            add(new Complex(d));
                    }});
                }});
                add(new Collection<NumberCollection<Complex>>(){{ //NUM = 4
                    add(new NumberCollection<Complex>(){{ // MAX
                        for(Double d : new Double[]
                        {1d, 2d, 3d, 4d, 5d, 6d, 7d}) 
                            add(new Complex(d));
                    }});
                    add(new NumberCollection<Complex>(){{ // TIME
                        for(Double d : new Double[]
                        {0.15d, 0.16d, 0.25d, 0.34d, 1.29d, 12.68d, 357.54d})
                            add(new Complex(d));
                    }});
                }});
                add(new Collection<NumberCollection<Complex>>(){{ //NUM = 5
                    add(new NumberCollection<Complex>(){{ // MAX
                        for(Double d : new Double[]
                        {1d, 2d, 3d, 4d, 5d, 6d, 7d}) 
                            add(new Complex(d));
                    }});
                    add(new NumberCollection<Complex>(){{ // TIME
                        for(Double d : new Double[]
                        {1.69d, 1.65d, 1.61d, 1.97d, 4.98d, 19.62d, 349.55d})
                            add(new Complex(d));
                    }});
                }});
                add(new Collection<NumberCollection<Complex>>(){{ //NUM = 6
                    add(new NumberCollection<Complex>(){{ // MAX
                        for(Double d : new Double[]
                        {1d, 2d, 3d, 4d, 5d, 6d, 7d}) 
                            add(new Complex(d));
                    }});
                    add(new NumberCollection<Complex>(){{ // TIME
                        for(Double d : new Double[]
                        {20.61d, 19.82d, 21.51d, 20.64d, 28.02d, 82.64d, 545.72d})
                            add(new Complex(d));
                    }});
                }});

        }};
        // Collection<List<NumberCollection<Complex>>> datasets =
        //     new Collection<List<NumberCollection<Complex>>>(){{ // new collection of data sets
        //         add(new Collection<NumberCollection<Complex>>(){{ // a new data set
        //             add(new NumberCollection<Complex>(){{ // x axis
        //                 for(Double d : new Double[]{-3d, -2d, -1d, +0d, +1d, +2d, +3d}) 
        //                     add(new Complex(d)); // a new element   
        //             }});
        //             add(new NumberCollection<Complex>(){{ // y axis
        //                 for(Double d : new Double[]{+9d, +4d, +1d, +0d, +1d, +4d, +9d}) 
        //                     add(new Complex(d)); // a new element
        //             }});
        //         }});

        //         add(new Collection<NumberCollection<Complex>>(){{ // a new data set
        //             add(new NumberCollection<Complex>(){{ // x axis
        //                 for(Double d : new Double[]{- 0d, - 1d, - 2d, - 3d, - 4d,  1d}) 
        //                     add(new Complex(d)); // a new element
        //             }});
        //             add(new NumberCollection<Complex>(){{ // y axis
        //                 for(Double d : new Double[]{+ 0d, + 1d, + 2d, + 3d, + 4d, + 2d}) 
        //                     add(new Complex(d)); // a new element
        //             }});
        //         }});

        // }};

        NumberCollection.graphMultiple(datasets, new GraphComponents(), true);

    }
}