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

}
