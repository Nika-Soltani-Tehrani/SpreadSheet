import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OperationTest {

    private final Spreadsheet dummySheet = new Spreadsheet(); // not used by ConstantNumber

    @Test
    void testAddition() {
        Expression expr = new Operation(
                new ConstantNumber(5),
                Operation.Operator.ADD,
                new ConstantNumber(3)
        );
        assertEquals(8, expr.getValue(dummySheet));
    }

    @Test
    void testSubtraction() {
        Expression expr = new Operation(
                new ConstantNumber(10),
                Operation.Operator.SUBTRACT,
                new ConstantNumber(4)
        );
        assertEquals(6, expr.getValue(dummySheet));
    }

    @Test
    void testMultiplication() {
        Expression expr = new Operation(
                new ConstantNumber(7),
                Operation.Operator.MULTIPLY,
                new ConstantNumber(6)
        );
        assertEquals(42, expr.getValue(dummySheet));
    }

    @Test
    void testDivision() {
        Expression expr = new Operation(
                new ConstantNumber(20),
                Operation.Operator.DIVIDE,
                new ConstantNumber(5)
        );
        assertEquals(4, expr.getValue(dummySheet));
    }

    @Test
    void testDivisionByZero() {
        Expression expr = new Operation(
                new ConstantNumber(5),
                Operation.Operator.DIVIDE,
                new ConstantNumber(0)
        );
        assertThrows(ArithmeticException.class, () -> expr.getValue(dummySheet));
    }
}
