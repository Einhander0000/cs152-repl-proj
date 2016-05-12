import com.sun.deploy.util.SystemUtils;
import jdk.nashorn.internal.runtime.ParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collector;


/*
Kevin Bui - 007684183
CS152 Programming Paradigms Assignment 3
Instructor: Thaddeus Aid
Parser Logic take elements from: http://www.sunshine2k.de/coding/java/SimpleParser/SimpleParser.html

 */
public class repl {

    static Stack symbols = new Stack();
    static Stack variables = new Stack();

    public static void main(String[] args) throws IOException {
        Scanner myScanner = new Scanner(System.in);
        System.out.println("Enter input:");
        while (myScanner.hasNextLine()) {

            String inputA = myScanner.nextLine();
            System.out.println("CURRENT STRING: " + inputA);
            System.out.println("POSTFIX: " + advancedTokenizer(inputA));
            System.out.println("OUTPUT: " + evaluatePostFix(advancedTokenizer(inputA)));
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

    public static String advancedTokenizer(String input) throws ParserException {
        StringBuffer postFix = new StringBuffer();
        Stack operators = new Stack();
        boolean spacer = false;

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
                        postFix.append(" ");
                    }
                }
                postFix.append(" ");
                //i--;
            }

            switch (c) {
                case ')':
                    while (!operators.isEmpty() && !operators.peek().equals('(')) {
                        postFix.append(operators.pop());
                    }
                    if (!operators.empty()) {
                        operators.pop();
                    }

                    postFix.append(" ");
                    break;
                case '(':
                    operators.push(c);
                    postFix.append(" ");
                    break;
                case '+':
                    if (!operators.empty() && (operators.peek().equals('+') || operators.peek().equals('-') || operators.peek().equals('*') || operators.peek().equals('/') || operators.peek().equals('^'))) {
                        postFix.append(operators.pop());
                        postFix.append(" ");
                    }
                    operators.push(c);
                    postFix.append(" ");
                    break;
                case '-':
                    if (!operators.empty() && (operators.peek().equals('+') || operators.peek().equals('-') || operators.peek().equals('*') || operators.peek().equals('/') || operators.peek().equals('^'))) {
                        postFix.append(operators.pop());
                    }
                    operators.push(c);
                    postFix.append(" ");
                    break;
                case '*':
                    if (!operators.empty() && (operators.peek().equals('*') || operators.peek().equals('/'))) {
                        postFix.append(operators.pop());
                    }
                    operators.push(c);
                    postFix.append(" ");
                    break;
                case '/':
                    if (!operators.empty() && (operators.peek().equals('*') || operators.peek().equals('/'))) {
                        postFix.append(operators.pop());
                    }
                    operators.push(c);
                    postFix.append(" ");
                    break;
                case '^':
                    if (!operators.empty() && operators.peek().equals('^')) {
                        postFix.append(operators.pop());
                    }
                    operators.push(c);
                    postFix.append(" ");
                    break;
                case '=':
                    /*
                    * We're going to need to figure out the logic for this part.
                    * */
                    break;
                case ' ':
                    if (!spacer) {
                        spacer = true;
                        postFix.append(" ");
                        break;
                    }
                    spacer = false;
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

    public static double evaluatePostFix(String inputString) throws ParserException {
        Stack operators = new Stack();
        char c;
        double x = 0;
        Scanner postFixScanner = new Scanner(inputString);
        while (postFixScanner.hasNext()) {
            String input = postFixScanner.next();
            for (int i = 0; i < input.length(); i++) {
                c = input.charAt(i);
                x = 0;
                switch (c) {
                    case '+':
                        double x1 = Double.valueOf(operators.pop().toString());
                        double x2 = Double.valueOf(operators.pop().toString());
                        x = x2 + x1;
                        operators.push(x);
                        break;
                    case '-':
                        double x3 = Double.valueOf(operators.pop().toString());
                        double x4 = Double.valueOf(operators.pop().toString());
                        x = x4 - x3;
                        operators.push(x);
                        break;
                    case '*':
                        double x5 = Double.valueOf(operators.pop().toString());
                        double x6 = Double.valueOf(operators.pop().toString());
                        x = x6 * x5;
                        operators.push(x);
                        break;
                    case '/':
                        double x7 = Double.valueOf(operators.pop().toString());
                        double x8 = Double.valueOf(operators.pop().toString());
                        x = x8 / x7;
                        operators.push(x);
                        break;
                    case '^':
                        double x9 = Double.valueOf(operators.pop().toString());
                        double x10 = Double.valueOf(operators.pop().toString());
                        x = Math.pow(x9, x10);
                        operators.push(x);
                        break;
                }

                if (c >= '0' && c <= '9') {
                    String sub = input.substring(i);
                    int j = 0;
                    for (j = 0; j < sub.length(); j++) {
                        if (sub.charAt(j) == ' ') {
                            sub = sub.substring(0, j);
                        }
                    }
                    // 'sub' contains now just the number
                    try {
                        x = Double.parseDouble(sub);
                    } catch (NumberFormatException ex) {
                        throw new ParserException("String to number parsing exception: " + input);
                    }
                    operators.push(x);
                    // go on with next token
                    i += j - 1;
                }
            }
        }
        return x;
    }
}




