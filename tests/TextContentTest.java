import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TextContentTest {

    @Test
    void testEmptyTextReturnsZero() {
        TextContent tc = new TextContent("");
        assertEquals(0.0, tc.getValue(null));
    }

    @Test
    void testNumericTextReturnsNumber() {
        TextContent tc = new TextContent("123.45");
        assertEquals(123.45, tc.getValue(null));
    }

    @Test
    void testNonNumericTextThrows() {
        TextContent tc = new TextContent("hello");
        assertThrows(IllegalArgumentException.class, () -> tc.getValue(null));
    }

    @Test
    void testAsString() {
        TextContent tc = new TextContent("hi");
        assertEquals("hi", tc.asString());
    }
}
