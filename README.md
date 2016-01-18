# MathParsing
A java program that is used to parse strings into equations, and then solve them (among other things).
<p>more info to come.
<p>NOTE: The javadoc folder is exceedingly old, and will be updated in the future.
##How to run the code
From the command line, type `java -cp bin -ea West.Math.Tester <ARGUMENTS>`.
##Arguments:
NOTE: Double quotes are only required if there is spaces in the passed arguments. <br>This means `--e "y = 5 * cos(theta) + 4"` can be instead `--e y=5*cos(theta)+4`.
<ul>·<code>--g</code> - Use this argument exclusively on its own to open the Graphing Calculator Interface.</ul>
<ul>·<code>--func</code> - Any arguments after this will be defined as custom functions. NOTE: this is currently *not* working right now.
  <ul>Example: <code>--func "summation:summation"</code></ul></ul>
<ul>·<code>--eq</code> - Any arguments after this will be used as equations.
  <ul>Example: <code>--eq "y = 5 × cos(theta) + 4" "theta = 5^pi"</code></ul></ul>
<ul>·<code>--indep</code> - The next argument after this will be the independent variable. This will also open the graphing interface (with the independent variable as the "x axis variable")
  <ul>Example: <code>--indep theta</code></ul></ul>
<ul>·<code>--dep</code> - Any arguments after this will be dependent variables; They will be the values solved for / graphed.
  <ul>Example: <code>--dep y1 y2</code></ul></ul>
<ul>·<code>--gtype</code> - The next argument will dictate the Graph Type. Currently, the Graph Type defaults to Cartesian, and the only valid argument is "Polar".
  <ul>Example: <code>--gtype Polar</code></ul></ul>
<ul>·<code>--step</code> - A bit of a misnomer, the next argument is either a single number (Step), or three seperate numbers seperated by commas (Start, End, Step). This is only ever used when graphing, and is used to define how detailed the graph is & where the graph starts. Start and End are by default set to the window bound's min x and min y.
  <ul>Example: <code>--step "-4·pi, 4×π/5, π÷16"</code></ul></ul>
<ul>·<code>--bounds</code> - Only ever used for graphing, this defines the bounds of what will be displayed. The next argument should be 4 numbers, seperated by commas, in the format: <code>Minimum X, Minimum Y, Maximum X, Maximum Y</code>
  <ul>Example: <code>--bounds "-2·π, -1, 2×pi, 1.0"</code></ul></ul>
