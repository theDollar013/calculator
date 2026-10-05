/**
  *
  * Controller.java handles all control aspects of the program without altering the UI.
  * It manages the state of the calculator and coordinates input between the UI and
  * calculation logic.
  *
  **/

public class Controller {

    private boolean startNewNumber = true;
    private String expStart;
    private final Logic log = new Logic();

    public void clear() {

        log.clearOperation();
        expStart = "";
        startNewNumber = true;
    }

    public boolean isStartNewNumber() {

        return startNewNumber;
    }

    public void setStartNewNumber(boolean startNewNumber) {

        this.startNewNumber = startNewNumber;
    }

    public String getExpStart() {

        return expStart;
    }

    public boolean hasOperation() {

        return log.hasOperation();
    }

    public String handleOperator(String currDisp, String op) {

        String returnValue = currDisp;

        // If there is already a queued operation, calculate it first
        if (hasOperation() && !isStartNewNumber()) {

            double secondNumber = Double.parseDouble(currDisp);
            expStart = expStart + " " + currDisp + " " + op;
            double result = log.calcWith(secondNumber);
            returnValue = log.formatResult(result);
            log.setOperation(result, op);
        }

        else {

            expStart = currDisp + " " + op;
            double currNum = Double.parseDouble(currDisp);
            log.setOperation(currNum, op);
        }

        startNewNumber = true;
        return returnValue;
    }

    public String handleSignChange(String currDisp) {

        // Protects against sign change while waiting on number input
        if (!startNewNumber) {

            double currNum = Double.parseDouble(currDisp);

            // Prevents displaying -0
            if (currNum != 0) {

                currNum = currNum * -1;
                return log.formatResult(currNum);
            }

            return currDisp;
        }

        return currDisp;
    }

    public String handleSqRt(String currDisp) {

        double num = Double.parseDouble(currDisp);
        return log.formatResult(log.sqrt(num));
    }

    public String handleEquals(String currDisp) {

        double secNum = Double.parseDouble(currDisp);
        double result = log.calcWith(secNum);
        return log.formatResult(result);
    }

    public String handlePercent(String currDisp) {

        double num = Double.parseDouble(currDisp);
        double result;

        // Percentage part of an equation (like calculating totals + tip)
        if (hasOperation()) {

            result = log.calculatePctage(num);
        }

        // Standalone percentage
        else {

            result = log.pctage(num);
        }

        return log.formatResult(result);
    }

    public String handleSquare(String currDisp) {

        double num = Double.parseDouble(currDisp);
        double result = log.calculateSquare(num);
        return log.formatResult(result);
    }

    public String handleReciprocal(String currDisp) {

        double num = Double.parseDouble(currDisp);
        double result = log.calculateRecip(num);
        return log.formatResult(result);
    }
}