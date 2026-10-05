import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class LogicTest {

    @Test
    void additionReturnsCorrectResult() {

        Logic log = new Logic();
        double result = log.calculate(5, 3, "+");
        assertEquals(8.0, result);
    }

    @Test
    void subtractionReturnsCorrectResult() {

        Logic log = new Logic();
        double result = log.calculate(10, 4, "-");
        assertEquals(6.0, result);
    }

    @Test
    void divisionByZeroThrowsException() {

        Logic log = new Logic();
        assertThrows(ArithmeticException.class, () -> log.calculate(10, 0, "/"));
    }

    @Test
    void sqRtOfNegativeNumberThrowsException() {

        Logic log = new Logic();
        assertThrows(ArithmeticException.class, () -> log.sqrt(-9));
    }

    @Test
    void storedOperationUsesSecondNumber() {

        Logic log = new Logic();
        log.setOperation(10, "+");
        double result = log.calcWith(4);
        assertEquals(14.0, result);
    }

    @Test
    void clearOperationRemovesStoredOperator() {

        Logic log = new Logic();
        log.setOperation(10, "+");
        assertTrue(log.hasOperation());
        log.clearOperation();
        assertFalse(log.hasOperation());
    }
}
