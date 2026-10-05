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

    @Test
    void decimalAdditionReturnsCorrectResult() {

        Logic log = new Logic();
        double result = log.calculate(0.1, 0.2, "+");
        assertEquals(0.3, result, 0.000000001);
    }

    @Test
    void wholeNumberFormatsWithoutDecimal() {

        Logic log = new Logic();
        assertEquals("8", log.formatResult(8.0));
    }

    @Test
    void decimalNumberKeepsFraction() {

        Logic log = new Logic();
        assertEquals("8.5", log.formatResult(8.5));
    }

    @Test
    void percentageForAdditionUsesFirstNumber() {

        Logic log = new Logic();
        log.setOperation(200, "+");
        double result = log.calculatePctage(10);
        assertEquals(20.0, result, 0.000000001);
    }

    @Test
    void percentageForMultiplicationReturnsDecimal() {

        Logic log = new Logic();
        log.setOperation(200, "*");
        double result = log.calculatePctage(10);
        assertEquals(0.1, result, 0.000000001);
    }

    @Test
    void newOperationReplacesPreviousOperation() {

        Logic log = new Logic();
        log.setOperation(10, "+");
        log.setOperation(20, "*");
        double result = log.calcWith(3);
        assertEquals(60.0, result, 0.000000001);
    }
}
