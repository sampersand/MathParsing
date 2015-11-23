import java.util.ArrayList;
public class Equation {
    public static void main(String[] args) {
        Equation eq = new Equation("integral(1,2,f(x),dx)^-2.0");
    }
    public final String RAW_EQ;
    public final ArrayList<Token> TOKENS;
    public Equation(){
        this("");
    }
    public Equation(String pEq){
        RAW_EQ = pEq;
        TOKENS = parseTokens(RAW_EQ);
        System.out.println(TOKENS);
    }
    private ArrayList<Token> parseTokens(String rEq){
        rEq = rEq.trim().replaceAll(" ","");
        ArrayList<Token> tokens = new ArrayList<Token>();
        String prev = "";
        char c;
        for(int x = 0; x < rEq.length(); x++) {
            c = rEq.charAt(x);
            if(isAlphaNumP(c)){
                prev += c;
                if(x == rEq.length() - 1)
                    tokens.add(new Token(prev, isNumP(prev) ? Token.Types.NUM : Token.Types.VAR));                    
                continue;
            }
            switch(c){
                case '(': // This should never be preceeded by a number.
                    if(!isAlpha(prev))
                        System.err.println("[ERROR] Uh oh! '" + prev +
                        "' isn't Alphabetical, but is succeeded by '('. Continuing anyways.");
                    tokens.add(new Token(prev, Token.Types.FUNC));
                    tokens.add(Token.LPAR);
                    break;
                case ')': 
                    if(prev.length() != 0)
                        tokens.add(new Token(prev, isNumP(prev) ? Token.Types.NUM : Token.Types.VAR));
                    tokens.add(Token.RPAR);
                    break;
                case '-': case '+': case '*': case '/': case '^':
                    if(prev.length() != 0)
                        tokens.add(new Token(prev, isNumP(prev) ? Token.Types.NUM : Token.Types.VAR));
                    tokens.add(new Token(c, Token.Types.OPER));
                    break;
                case ',':
                    if(prev.length() != 0)
                        tokens.add(new Token(prev, isNumP(prev) ? Token.Types.NUM : Token.Types.VAR));
                    tokens.add(new Token(c, Token.Types.DELIM));
                    break;

                default:
                    if(prev.length() != 0)
                        tokens.add(new Token(prev, isNumP(prev) ? Token.Types.NUM : Token.Types.VAR));
                    tokens.add(new Token(c, Token.Types.NULL));
                    break;

            }
            prev = "";
        }
        return tokens;
    }

    public static boolean isAlphaNum(char c){
        return Character.isAlphabetic(c) || Character.isDigit(c);
    }
    public static boolean isAlphaNum(String str){
        for(char c : str.toCharArray())
            if(!isAlphaNum(c))
                return false;
        return true; //note also uses '.'
    }
    public static boolean isAlphaNumP(char c){ //note: Also uses '.'
        return isAlphaNum(c) || c == '.';
    }
    public static boolean isAlphaNumP(String str){
        for(char c : str.toCharArray())
            if(!isAlphaNumP(c))
                return false;
        return true; //note also uses '.'
    }
    public static boolean isAlpha(char c){
        return Character.isAlphabetic(c);
    }
    public static boolean isAlpha(String str){
        for(char c : str.toCharArray())
            if(!isAlpha(c))
                return false;
        return true;
    }
    public static boolean isNum(char c){
        return Character.isDigit(c);
    }
    public static boolean isNum(String str){
        for(char c : str.toCharArray())
            if(!isNum(c))
                return false;
        return true;
    }
    public static boolean isNumP(char c){
        return Character.isDigit(c) || c == '.';
    }
    public static boolean isNumP(String str){
        for(char c : str.toCharArray())
            if(!isNumP(c))
                return false;
        return true;
    }      
}

