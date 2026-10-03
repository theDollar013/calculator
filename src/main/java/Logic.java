public class Logic {

    private String op = "";
    private double firstNumber;

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

                    throw new ArithmeticException("Cannot divide by zero");
                }

                return a / b;

            default:
                throw new IllegalArgumentException("Invalid Operator");
        }
    }

    public double sqrt(double num) {

            return Math.sqrt(num);

    }

    public String formatResult(double result) {

        if (result == (long) result) {

            return Long.toString((long) result);
        }

        return Double.toString(result);
    }
}
