import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ControllerTest {

    @Test
    void backspaceOnNegativeSingleDigitResetsToZero() {

        Controller control = new Controller();
        String disp = control.handleNumber("0", "5");
        disp = control.handleSignChange(disp);
        assertEquals("-5", disp);
        disp = control.handleBackspace(disp);
        assertEquals("0", disp);
        assertTrue(control.isStartNewNumber());
    }
}