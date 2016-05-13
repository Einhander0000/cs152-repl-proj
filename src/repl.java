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
    static Map alphaVariables = new HashMap();
    static StringBuffer alphaCharacter = new StringBuffer();
    static boolean alphaDetected = false; //boolean for alphabet character detected (For user defined function)

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

    public static String advancedTokenizer(String input) throws ParserException {
        StringBuffer postFix = new StringBuffer();
        Stack operators = new Stack();
        boolean spacer = false;


        boolean equalsDeteceted = false; //Detection for assignments.

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
                    if (!operators.empty()) {
                        postFix.append(operators.pop());
                    }
                    operators.push(c);
                    postFix.append(" ");
                    break;
                case '=':
                    /*
                    * We're going to need to figure out the logic for this part.
                    * */
                    if (!operators.empty()) {
                        postFix.append(operators.pop());
                    }
                    operators.push(c);
                    postFix.append(" ");
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
            if (("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ+-^*/()1234567890= ".indexOf(c) == -1) && (c != '\u0000'))
            {
                throw new ParserException("Invalid symbol: " + c);
            }

            /*Check for alphabetical functions*/
            if("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(c) != -1)
            {

                //alphaDetected = true;
                while ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
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
                i--;
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
                    case '=':
                        if(!alphaDetected)
                        {
                            String x11 = String.valueOf(operators.pop().toString());
                            String x12 =  String.valueOf(operators.pop().toString());

                            System.out.println("x11, x12: " + x11 + ", " + x12);
                            alphaVariables.put(x12, x11);
                            System.out.println("BEFORE PUSH"  + alphaVariables.get(x12).toString());
                            System.out.println("Variable is stored!");
                            alphaDetected = true;
                        }
                        else
                        {
                            throw new ParserException("String to number parsing exception: " + input);
                        }
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

                    //IF THERE HAS BEEN AN EQUALS DECLARED BEFORE CHECK THIS
                    try {
                        x = Double.parseDouble(sub);
                    } catch (NumberFormatException ex) {
                        throw new ParserException("String to number parsing exception: " + input);
                    }
                    operators.push(x);
                    // go on with next token
                    i += j - 1;
                }

                if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
                    String sub = input.substring(i);
                    int j = 0;
                    for (j = 0; j < sub.length(); j++) {
                        if (sub.charAt(j) == ' ') {
                            sub = sub.substring(0, j);
                        }
                    }
                    // 'sub' contains now just the number
                    if(alphaDetected) {
                        try
                        {
                            System.out.println("LETTER IN QUESTION: " + sub);
                            x = Double.parseDouble(alphaVariables.get(sub).toString());

                        } catch (NumberFormatException ex)
                        {
                            throw new ParserException("String to number parsing exception: " + input);
                        }
                    }



                    operators.push(c);
                    // go on with next token
                    i += j - 1;
                }
            }
        }
        return x;
    }
}




