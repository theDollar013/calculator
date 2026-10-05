/**
  *
  * Controller.java handles all control aspects of the program without altering the UI.
  * It handles all operations involving what is needed to run calculations without running
  * the calculations themselves.
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

    public void setExpStart(String expStart) {

        this.expStart = expStart;
    }

    public boolean hasOperation() {

        return log.hasOperation();
    }

    public String handleOperator(String currDisp, String op) {

        // If there is already a queued operation, calculate it first
        if (hasOperation() && !isStartNewNumber()) {

            double secondNumber = Double.parseDouble(currDisp);
            expStart = expStart + " " + currDisp + " " + op;
            double result = log.calcWith(secondNumber);
            String formResult = log.formatResult(result);
            log.setOperation(result, op);
            return formResult;
        }

        else {

            expStart = currDisp + " " + op;
            double currNum = Double.parseDouble(currDisp);
            log.setOperation(currNum, op);
            return currDisp;
        }
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
        }
    }

    public String handleEquals(double secNum) {

        double result = log.calcWith(secNum);
        return log.formatResult(result);
    }
}
