package Math;
public class Print {
    public static enum Level {INFO,PRINT,WARNING,ERROR};
    public static void print(Object... objs){    print(objs, "\n",  Level.PRINT);    }
    public static void printi(Object... objs){   print(objs, "\n",  Level.INFO);     }
    public static void printw(Object... objs){   print(objs, "\n",  Level.WARNING);  }
    public static void printe(Object... objs){   print(objs, "\n",  Level.ERROR);    }

    public static void printnl(Object... objs){  print(objs, "",    Level.PRINT);    }
    public static void printnli(Object... objs){ print(objs, "",    Level.INFO);     }
    public static void printnlw(Object... objs){ print(objs, "",    Level.WARNING);  }
    public static void printnle(Object... objs){ print(objs, "",    Level.ERROR);    }
    private static void print(Object[] objs, String append, Level pLevel){
        String ret = pLevel != Level.PRINT ? "[" + pLevel + "] " : "";
        for(int i = 0; i < objs.length; i++){
            ret += objs[i] + " ";
        }
        
        System.out.print(ret.substring(0, ret.length() - (objs.length == 0 ? 0 : 1)) + append);
    }

}