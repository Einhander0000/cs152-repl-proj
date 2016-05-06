import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class repl {

    static Stack symbols = new Stack();
    static Stack variables = new Stack();
    static Stack operators = new Stack();


    public static void main(String[] args)  throws IOException {
        System.out.println("Hello World");
        run();
    }

    /*
    * This runs a console prompt
    *
    */
    public static void run() throws IOException {

        Scanner myScanner = new Scanner(System.in);
        while(myScanner.hasNext()) {
            tokenizer(myScanner.next());
//            System.out.println("FIRST: " + myScanner.next() + "\n");
//            System.out.println("SECOND: " + myScanner.next() + "\n" );
        }

        System.out.println(Arrays.toString(variables.toArray()));


    }

    public static void tokenizer(String token){


        for (int i = 0; i < token.length(); i++){
            char c = token.charAt(i);
            //Process char

            if("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(c) == 1){
                variables.add(token);
            }

            switch(token){
                case "+":
                    symbols.push("+");
                    break;
                case "-":
                    symbols.push("-");
                    break;
                case "*":
                    symbols.push("*");
                    break;
                case "^":
                    symbols.push("^");
                    break;


            }

        }





    }
}

