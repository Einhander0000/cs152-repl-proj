import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class repl {

    static Stack symbols = new Stack();
    static Stack variables = new Stack();
    static Stack operators = new Stack();


    public static void main(String[] args) throws IOException {
        System.out.println("Hello World");
        run();
    }

    public static void run() throws IOException {
        Scanner myScanner = new Scanner(System.in);
        while (myScanner.hasNext()) {
            String inputA = myScanner.next();
            tokenizer(inputA);
        }
        System.out.println("THIS IS VARIABLES: " +  Arrays.toString(variables.toArray()));
        System.out.println("THIS IS SYMBOLS: " + Arrays.toString(symbols.toArray()));
    }

    public static void tokenizer(String token) {
        double answer = 0;

        /**
         * This is for more complex tokens
         */
        for (int i = 0; i < token.length(); i++) {
            boolean integerFlag = false; //Used to calculate integer places
            char c = token.charAt(i);
            //Process char

            if ("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(c) != -1) {
                variables.add(c);
            }

            if("1234567890".indexOf(c) != -1){
                variables.add(c);
            }

            switch (c) {
                case '+':
                    symbols.push("+");
                    break;
                case '-':
                    symbols.push("-");
                    break;
                case '*':
                    symbols.push("*");
                    break;
                case '^':
                    symbols.push("^");
                    break;
                case '=':
                    symbols.push("=");
                    break;
                default:
                    break;
            }



        }


    System.out.println("I'm done!");
    }

    public static void popper(){
        int output = 0;

        /*If this variable is instance of:*/
        if(variables.peek() instanceof  Integer){

        }
    }


}
