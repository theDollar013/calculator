/**
 *
 * Controller.java handles all control aspects of the program without altering the UI.
 * It handles all operations involving what is needed to run calculations without running
 * the calculations themselves.
 *
 */

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

    public void handleOperator(String currDisp, String op) {

        expStart = currDisp + " " + op;
        double currNum = Double.parseDouble(currDisp);
        log.setOperation(currNum, op);
    }

    public String handleEquals(double secNum) {

        try {

            double result = log.calcWith(secNum);
            return log.formatResult(result);
        }

        catch (ArithmeticException e) {

            return e.getMessage();
        }
    }
}
