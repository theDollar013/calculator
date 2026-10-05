import java.util.logging.Logger;

/**
  *
  * Logic.java handles all mathematical calculations for the program.
  *
  **/

public class Logic {

    private String op = "";
    private double firstNumber;
    private static final Logger LOGGER = Logger.getLogger(Logic.class.getName());

    public void setOperation(double num, String operator) {

        firstNumber = num;
        op = operator;
    }

    public boolean hasOperation() {

        return !op.isEmpty();
    }

    public double calcWith(double secondNumber) {

        return calculate(firstNumber, secondNumber, op);
    }

    public void clearOperation() {

        op = "";
    }

    public double calculate(double a, double b, String op) {

        switch (op) {

            case "+":
                return a + b;

            case "-":
                return a - b;

            case "*":
                return a * b;

            case "/":

                if (b == 0) {

                    throw new ArithmeticException("ERR");
                }

                return a / b;

            case "^":

                return Math.pow(a, b);

            default:
                throw new IllegalArgumentException("ERR");
        }
    }

    public double sqrt(double num) {

        // SqRts of neg numbers undefined
        if (num < 0) {

            throw new ArithmeticException("ERR");
        }

        return Math.sqrt(num);
    }

    public double pctage(double num) {

        return num / 100;
    }

    public double calculatePctage(double percent) {

        double decPct = percent / 100;

        if (op.equals("+") || op.equals("-")) {

            return firstNumber * decPct;
        }

        return decPct;
    }

    public double calculateSquare(double num) {

        return num * num;
    }

    public double calculateRecip(double num) {

        if (num == 0) {

            throw new ArithmeticException("ERR");
        }

        return 1 / num;
    }

    public String formatResult(double result) {

        if (result == (long) result) {

            return Long.toString((long) result);
        }

        return Double.toString(result);
    }
}
