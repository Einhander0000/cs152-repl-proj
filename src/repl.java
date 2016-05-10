import com.sun.deploy.util.SystemUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collector;

public class repl {

    static Stack symbols = new Stack();
    static Stack variables = new Stack();
    static Stack operators = new Stack();



    public static void main(String[] args) throws IOException {
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

        System.out.println("LOOK SIZE: " + variables.size());

//        for(int i = 0; i < variables.size(); i++){
//            if(variables.peek() instanceof  Integer){
//                System.out.println("Look an integer: " + variables.get(i));
//            }
//        }
    }

    public static void tokenizer(String token) {
        double answer = 0;
        boolean integerFlag = false; //Used to calculate integer places
        boolean integerExists = false;
        int currentInt = 0;


        /**
         * This is for more complex tokens
         */
        for (int i = 0; i < token.length(); i++) {
            System.out.println("CurrentCHAR: " + token.charAt(i));
            System.out.println("CURRENT INTEGER: " + currentInt);

            char c = token.charAt(i);
            //Process char

            if ("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(c) != -1) {
                variables.add(c);
            }

            /*returns the integer value of number and adds it to stack*/
            if("1234567890".indexOf(c) != -1){
                if(integerFlag == true) {
                    /* Checks if it is greater than 9. I.E: 10, 1000, etc. Decimal place calculation
                     * If current index is an integer, check the rest of the token to see if there are any other numbers
                     * that fits the criteria
                     * */
                    System.out.println("ENTERED IF FIRST IF STATEMENT");
                    System.out.println("OLD INT(FIRST IF): " + currentInt);
                    currentInt = (currentInt * 10) + Character.getNumericValue(c);
                    System.out.println("NEW CURRENT INT (FIRST IF): " + currentInt);
                    System.out.println("FIRST IF: CurrentInt: " + currentInt);
                    //variables.add(currentInt);

                }else {

                    System.out.println("ENTERED IN ELSE STATEMENT");
                    System.out.println("ELSE STATEMENT: Character.getNumericValue(c): " + Character.getNumericValue(c));
                    currentInt = Character.getNumericValue(c);
                    System.out.println("ELSE STATEMENT: NEW CURRENT INT OLD LOOP: " + currentInt);
                    integerFlag = true;
                    integerExists = true;
                }
            }

            switch (c) {
                case '+':
                    symbols.push("+");
                    System.out.println("THIS IS THE FINAL INT BEFORE PUSHING: " + currentInt);
                    if(currentInt != 0 && integerExists){
                        variables.push(currentInt);
                        currentInt = 0;
                    }
                    break;
                case '-':
                    symbols.push("-");
                    if(currentInt != 0 && integerExists){
                        variables.push(currentInt);
                        currentInt = 0;
                    }
                    break;
                case '*':
                    symbols.push("*");
                    if(currentInt != 0 && integerExists){
                        variables.push(currentInt);
                        currentInt = 0;
                    }
                    break;
                case '^':
                    symbols.push("^");
                    if(currentInt != 0 && integerExists){
                        variables.push(currentInt);
                        currentInt = 0;
                    }
                    break;
                case '=':
                    symbols.push("=");
                    if(currentInt != 0 && integerExists){
                        variables.push(currentInt);
                        currentInt = 0;
                    }
                    break;
                case ' ':
                    if(currentInt != 0 && integerExists){
                        variables.push(currentInt);
                        currentInt = 0;
                    }
                    break;
            }
            System.out.println("END ONE FOR LOOP");
        }
        if(currentInt != 0 && integerExists){
            variables.push(currentInt);
            currentInt = 0;
        }
        System.out.println("This is the end dear friends");
    }

    public static void popper(){
        int output = 0;

        /*If this variable is instance of:*/
        if(variables.peek() instanceof  Integer){


        }
    }


}
