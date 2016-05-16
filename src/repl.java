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

/*
* HOW TO USE:
* Treat as if basic map
* > a = 3
* > b = 4
* a * b
* OUTPUT: 12.0
*
* Also supports basic arithmetic math operations.
*/
public class repl {

    static Stack symbols = new Stack();
    static Stack variables = new Stack();
    static Map alphaVariables = new HashMap();
    static StringBuffer alphaCharacter = new StringBuffer();
    static boolean alphaDetected = false; //boolean for alphabet character detected (For user defined function)
    static String temp1;
    static String temp2;

    public static void main(String[] args) throws IOException {
        Scanner myScanner = new Scanner(System.in);
        System.out.println("Enter input:");
        while (myScanner.hasNextLine()) {
            String inputA = myScanner.nextLine();
            System.out.println("CURRENT STRING: " + inputA);
            System.out.println("POSTFIX: " + advancedTokenizer(inputA));
            System.out.println("CURRENT OUTPUT: " + evaluatePostFix(advancedTokenizer(inputA)));
        }
    }

    /*
    This parses the user's input. Consolidated into cases and takes care of spaces.
    Basically formats the input into an easy post-fix string so that it can be easily read by the evaluation
    function, as that ignores the white-spaces.
     */
    public static String advancedTokenizer(String input) throws ParserException {
        StringBuffer postFix = new StringBuffer();
        Stack operators = new Stack();
        boolean spacer = false;
        boolean equalsDeteceted = false; //Detection for assignments.

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

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

            /*
            Big giant switch block that dictates the post-fix precedence
             */
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
                    if (!operators.empty() && (operators.peek().equals('*') || operators.peek().equals('/')))
                    {
                        postFix.append(operators.pop());
                    }
                    operators.push(c);
                    postFix.append(" ");
                    break;

                case '/':
                    if (!operators.empty() && (operators.peek().equals('*') || operators.peek().equals('/')))
                    {
                        postFix.append(operators.pop());
                    }
                    operators.push(c);
                    postFix.append(" ");
                    break;
                case '^':
                    if (!operators.empty())
                    {
                        postFix.append(operators.pop());
                    }
                    operators.push(c);
                    postFix.append(" ");
                    break;

                case '=':
                    if (!operators.empty())
                    {
                        postFix.append(operators.pop());
                    }
                    operators.push(c);
                    postFix.append(" ");
                    break;

                case ' ':
                    if (!spacer)
                    {
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
                while ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))
                {
                    postFix.append(c);
                    if (i + 1 < input.length())
                    {
                        c = input.charAt(++i);
                    }
                    else
                    {
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
        while (!operators.empty())
        {
            postFix.append(operators.pop());
            postFix.append(" ");
        }
        // delete the space character at the end of the string wrongly added in above while-loop
        postFix.deleteCharAt(postFix.length() - 1);
        return postFix.toString();
    }

    /*
    Takes in the post-fix string and evaluates the expression, returns the results in double.
     */
    public static double evaluatePostFix(String inputString) throws ParserException {
        Stack operators = new Stack();
        char c;
        double x = 0;
        Scanner postFixScanner = new Scanner(inputString);

        /*Scans through post-fix string, scanner ignores white-space.*/
        while (postFixScanner.hasNext())
        {
            String input = postFixScanner.next();
            for (int i = 0; i < input.length(); i++)
            {
                c = input.charAt(i);
                x = 0;

                /*Giant switch statement, checks if there exists a assignment for initial if-else blocks*/
                switch (c)
                {
                    case '+':
                        double x1;
                        double x2;
                        if(alphaVariables.containsKey(operators.peek()))
                        {
                             x1 = Double.valueOf(alphaVariables.get(operators.pop()).toString());
                        }
                        else
                        {
                             x1 = Double.valueOf(operators.pop().toString());
                        }

                        if(alphaVariables.containsKey(operators.peek().toString())){
                             x2 = Double.valueOf(alphaVariables.get(operators.pop().toString()).toString());
                        }
                        else
                        {
                             x2 = Double.valueOf(operators.pop().toString());
                        }
                        x = x2 + x1;
                        operators.push(x);
                        break;

                    case '-':
                        double x3;
                        double x4;
                        if(alphaVariables.containsKey(operators.peek()))
                        {
                             x3 = Double.valueOf(alphaVariables.get(operators.pop()).toString());
                        }
                        else
                        {
                             x3 = Double.valueOf(operators.pop().toString());
                        }
   
                        if(alphaVariables.containsKey(operators.peek().toString()))
                        {
                             x4 = Double.valueOf(alphaVariables.get(operators.pop().toString()).toString());
                        }
                        else
                        {
                             x4 = Double.valueOf(operators.pop().toString());
                        }
                        x = x4 - x3;
                        operators.push(x);
                        break;

                    case '*':
                        double x5;
                        double x6;
                        if(alphaVariables.containsKey(operators.peek()))
                        {
                             x5 = Double.valueOf(alphaVariables.get(operators.pop()).toString());
                        }
                        else
                        {
                             x5 = Double.valueOf(operators.pop().toString());
                        }
                        //System.out.println("THIS IS OUR PEEK  :::" + operators.peek().toString();

                        if(alphaVariables.containsKey(operators.peek().toString()))
                        {
                             x6 = Double.valueOf(alphaVariables.get(operators.pop().toString()).toString());
                        }
                        else
                        {
                             x6 = Double.valueOf(operators.pop().toString());
                        }
                        x = x6 * x5;
                        operators.push(x);
                        break;

                    case '/':
                        double x7;
                        double x8;
                        if(alphaVariables.containsKey(operators.peek()))
                        {
                             x7 = Double.valueOf(alphaVariables.get(operators.pop()).toString());
                        }
                        else
                        {
                             x7 = Double.valueOf(operators.pop().toString());
                        }

                        if(alphaVariables.containsKey(operators.peek().toString()))
                        {
                             x8 = Double.valueOf(alphaVariables.get(operators.pop().toString()).toString());
                        }
                        else
                        {
                             x8 = Double.valueOf(operators.pop().toString());
                        }
                        x = x8 / x7;
                        operators.push(x);
                        break;

                    case '^':
                        double x9;
                        double x10;
                        if(alphaVariables.containsKey(operators.peek()))
                        {
                             x9 = Double.valueOf(alphaVariables.get(operators.pop()).toString());
                        }
                        else
                        {
                             x9 = Double.valueOf(operators.pop().toString());
                        }

                        if(alphaVariables.containsKey(operators.peek().toString()))
                        {
                             x10 = Double.valueOf(alphaVariables.get(operators.pop().toString()).toString());
                        }
                        else
                        {
                             x10 = Double.valueOf(operators.pop().toString());
                        }
                        x = Math.pow(x10, x9);
                        operators.push(x);
                        break;

                    /*Special-Snowflake case where we put variables into a hashmap and keep it on-call in case user asks
                    for it.
                    */
                    case '=':
                        if(!alphaDetected)
                        {
                            String x11 = String.valueOf(operators.pop().toString());
                            String x12 =  String.valueOf(operators.pop().toString());
                            alphaVariables.put(x12, x11);
                            System.out.println("Variable is stored!");

                        }
                        else
                        {
                            String x11 = String.valueOf(operators.pop().toString());
                            String x12 =  String.valueOf(operators.pop().toString());
                            if(!alphaVariables.containsKey(x12.toString()))
                            {
                                alphaVariables.put(x12, x11);
                            }
                            else
                            {
                                throw new ParserException("String to number parsing exception: " + input);
                            }
                        }
                        break;
                }

                if (c >= '0' && c <= '9')
                {
                    String sub = input.substring(i);
                    int j = 0;
                    for (j = 0; j < sub.length(); j++)
                    {
                        if (sub.charAt(j) == ' ')
                        {
                            sub = sub.substring(0, j);
                        }
                    }

                    try
                    {
                        x = Double.parseDouble(sub);
                    }
                    catch (NumberFormatException ex)
                    {
                        throw new ParserException("String to number parsing exception: " + input);
                    }
                    operators.push(x);
                    // go on with next token
                    i += j - 1;
                }

                if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))
                {
                    String sub = input.substring(i);
                    int j = 0;
                    for (j = 0; j < sub.length(); j++)
                    {
                        if (sub.charAt(j) == ' ')
                        {
                            sub = sub.substring(0, j);
                        }
                    }

                    if(alphaVariables.containsKey(sub))
                    {
                        try
                        {
                            x = Double.parseDouble(alphaVariables.get(sub).toString());
                        }
                        catch (NumberFormatException ex)
                        {
                            throw new ParserException("String to number parsing exception: " + input);
                        }
                    }

                    operators.push(sub); //Look?
                    // go on with next token
                    i += j - 1;
                }
            }
        }
        return x;
    }
}




