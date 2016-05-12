import com.sun.deploy.util.SystemUtils;
import jdk.nashorn.internal.runtime.ParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collector;

public class repl {

    static Stack symbols = new Stack();
    static Stack variables = new Stack();

    public static void main(String[] args) throws IOException {
        Scanner myScanner = new Scanner(System.in);
        while (myScanner.hasNextLine()) {
            String inputA = myScanner.nextLine();
            System.out.println("CURRENT STRING: " + inputA);
            System.out.println("POSTFIX: " + advancedTokenizer(inputA));

        }
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
            if ("1234567890".indexOf(c) != -1) {
                if (integerFlag == true) {
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

                } else {

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
                    if (currentInt != 0 && integerExists) {
                        variables.push(currentInt);
                        currentInt = 0;
                    }
                    break;
                case '-':
                    symbols.push("-");
                    if (currentInt != 0 && integerExists) {
                        variables.push(currentInt);
                        currentInt = 0;
                    }
                    break;
                case '*':
                    symbols.push("*");
                    if (currentInt != 0 && integerExists) {
                        variables.push(currentInt);
                        currentInt = 0;
                    }
                    break;
                case '^':
                    symbols.push("^");
                    if (currentInt != 0 && integerExists) {
                        variables.push(currentInt);
                        currentInt = 0;
                    }
                    break;
                case '=':
                    symbols.push("=");
                    if (currentInt != 0 && integerExists) {
                        variables.push(currentInt);
                        currentInt = 0;
                    }
                    break;
                case ' ':
                    if (currentInt != 0 && integerExists) {
                        variables.push(currentInt);
                        currentInt = 0;
                    }
                    break;
            }
            System.out.println("END ONE FOR LOOP");
        }
        if (currentInt != 0 && integerExists) {
            variables.push(currentInt);
            currentInt = 0;
        }
        System.out.println("This is the end dear friends");
    }

    public static void startPopper() {
        int output = 0;

        /*If this variable is instance of:*/
        if (variables.peek() instanceof Integer) {


        }
    }

    public static String advancedTokenizer(String input) throws ParserException {
        StringBuffer postFix = new StringBuffer();
        Stack operators = new Stack();

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            System.out.println("Current c: " + c);
            /*
            * This accounts for numbers > 9 I.E: 10, 1000, 12345767, etc.
            */

            if (c >= '0' && c <= '9') {
                // process numericals
                while ((c >= '0' && c <= '9') || c == '.') {
                    postFix.append(c);
                    if (i + 1 < input.length()) {
                        c = input.charAt(++i);
                    } else {
                        // abort while loop if we reach end of string
                        c = 0;
                        i = input.length();
                    }
                }
                i--;
            }



            switch (c) {
                case ')':
                    while (!operators.isEmpty() && !operators.peek().equals('(')) {
                        postFix.append(operators.pop());
                    }
                    if (!operators.empty()) {
                        operators.pop();
                    }
                    break;
                case '(':
                    operators.push(c);
                    break;
                case '+':
                    if (!operators.empty() && (operators.peek().equals('+') || operators.peek().equals('-') || operators.peek().equals('*') || operators.peek().equals('/') || operators.peek().equals('^'))) {
                        postFix.append(operators.pop());
                    }
                    operators.push(c);
                    break;
                case '-':
                    if (!operators.empty() && (operators.peek().equals('+') || operators.peek().equals('-') || operators.peek().equals('*') || operators.peek().equals('/') || operators.peek().equals('^'))) {
                        postFix.append(operators.pop());
                    }
                    operators.push(c);
                    break;
                case '*':
                    if (!operators.empty() && (operators.peek().equals('*') || operators.peek().equals('/')))
                    {
                        postFix.append(operators.pop());
                    }
                    operators.push(c);
                    break;
                case '/':
                    if (!operators.empty() && (operators.peek().equals('*') || operators.peek().equals('/')))
                    {
                        postFix.append(operators.pop());
                    }
                    operators.push(c);
                    break;
                case '^':
                    if (!operators.empty() && operators.peek().equals('^'))
                    {
                        postFix.append(operators.pop());
                    }
                    operators.push(c);
                    break;
                case '=':
                    /*
                    * We're going to need to figure out the logic for this part.
                    * */
                    break;
                case ' ':
                    postFix.append(" ");
                    break;
            }



            /*If there exists a illegal character, throw a Parser Exception*/
            if (("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ+-^*/()1234567890 ".indexOf(c) == -1) && (c != '\u0000')) {
                throw new ParserException("Invalid symbol: " + c);
            }
        }





        while (!operators.empty()) {
            postFix.append(operators.pop());
            postFix.append(" ");
        }
        // delete the space character at the end of the string wrongly added in above while-loop
        postFix.deleteCharAt(postFix.length() - 1);


        return postFix.toString();

    }


}
