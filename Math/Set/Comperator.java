// package Math.Equation;
// import Math.Exception.NotDefinedException;
// import java.util.ArrayList;

// public class Comperator<E>  {
//     public static enum Comparator {
//         LT("<"),
//         GT(">"),
//         EQ("="),
//         NEQ("≠"),
//         LTE("≤"),
//         GTE("≥");
//         public String val;

//         private Comparator(String pVal){
//             val = pVal;
//         }
//         public Comparator from(String str){
//             //DEFINE
//             throw new NotDefinedException();
//         }

//     }
//     protected ArrayList<Object> elements; // Reason it's not ArrayList<E> is because other collections can be here.
//     protected Object comparison; // its an object so functions can also use this

//     public Comperator(){
//         this(Comparator.EQ);
//     }
//     public Comperator(Object pComp){
//         comparison = Comparator.EQ;
//     }
//     public Comperator(Object... pNode) { //no subnodes, comparison is FIN
//         comparison = Comparator.EQ;
//         add(elements);
//     }

//     public Comperator(Object pComp, Object... pElements){
//         comparison = pComp;
//         add(pElements);
//     }

//     public Comperator add(Object[] pObjs){
//         if(elements == null)
//             elements = new ArrayList<Object>();
//         for(Object obj : pObjs)
//             elements.add(obj);
//         return this;
//     }
//     public Comperator add(ArrayList<Object> pObjs){
//         if(elements == null)
//             elements = new ArrayList<Object>();
//         for(Object obj : pObjs)
//             elements.add(obj);
//         return this;
//     }


//     public boolean isFinal(){
//         return elements.size() <= 1;
//     }
// }
