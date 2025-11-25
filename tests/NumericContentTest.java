import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NumericContentTest {

    @Test
    void testNumericValue() {
        NumericContent nc = new NumericContent(42.5);
        assertEquals(42.5, nc.getValue(null));
    }

    @Test
    void testAsString() {
        NumericContent nc = new NumericContent(5.0);
        assertEquals("5.0", nc.asString());
    }
}
