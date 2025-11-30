import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FormulaParserTest {

    @Test
    void testParseSimpleNumber() {
        FormulaParser parser = new FormulaParser();
        Expression expr = parser.parse("=5");
        assertEquals(5, expr.getValue(new Spreadsheet()));
    }

    @Test
    void testParseCellReference() {
        Spreadsheet sheet = new Spreadsheet();
        sheet.setCellContent("A1", "10");

        FormulaParser parser = new FormulaParser();
        Expression expr = parser.parse("=A1");

        assertEquals(10, expr.getValue(sheet));
    }

    @Test
    void testAddition() {
        FormulaParser parser = new FormulaParser();
        Expression expr = parser.parse("=2+3");

        assertEquals(5, expr.getValue(new Spreadsheet()));
    }

    @Test
    void testMultiplicationPrecedence() {
        FormulaParser parser = new FormulaParser();
        Expression expr = parser.parse("=2+3*4"); // 2 + 12 = 14

        assertEquals(14, expr.getValue(new Spreadsheet()));
    }

    @Test
    void testParentheses() {
        FormulaParser parser = new FormulaParser();
        Expression expr = parser.parse("=(2+3)*4"); // 5 * 4 = 20

        assertEquals(20, expr.getValue(new Spreadsheet()));
    }
}
