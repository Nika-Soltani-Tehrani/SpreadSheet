import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CoordinateTest {

    @Test
    void testValidCoordinate() {
        Coordinate c = Coordinate.fromString("B12");
        assertEquals("B", c.getColumn());
        assertEquals(12, c.getRow());
    }

    @Test
    void testLowercaseInput() {
        Coordinate c = Coordinate.fromString("c7");
        assertEquals("C", c.getColumn());
        assertEquals(7, c.getRow());
    }

    @Test
    void testTrimmedInput() {
        Coordinate c = Coordinate.fromString("  A3  ");
        assertEquals("A", c.getColumn());
        assertEquals(3, c.getRow());
    }

    @Test
    void testInvalidFormat() {
        assertThrows(IllegalArgumentException.class, () -> Coordinate.fromString("33A"));
        assertThrows(IllegalArgumentException.class, () -> Coordinate.fromString("A"));
        assertThrows(IllegalArgumentException.class, () -> Coordinate.fromString("1"));
        assertThrows(IllegalArgumentException.class, () -> Coordinate.fromString(""));
    }

    @Test
    void testRowZeroInvalid() {
        assertThrows(IllegalArgumentException.class, () -> Coordinate.fromString("A0"));
    }

    @Test
    void testEqualsAndHashCode() {
        Coordinate c1 = new Coordinate("A", 5);
        Coordinate c2 = new Coordinate("A", 5);
        Coordinate c3 = new Coordinate("B", 5);

        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
        assertNotEquals(c1, c3);
    }
}
