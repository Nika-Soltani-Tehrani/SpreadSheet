import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FormulaContentTest {

    @Test
    void testSimpleFormula() {
        Spreadsheet sheet = new Spreadsheet();
        sheet.setCellContent("A1", "10");
        sheet.setCellContent("B1", "5");

        FormulaContent fc = new FormulaContent("=A1 + B1");
        assertEquals(15, fc.getValue(sheet));
    }

    @Test
    void testFormulaWithNumber() {
        Spreadsheet sheet = new Spreadsheet();
        sheet.setCellContent("A1", "3");

        FormulaContent fc = new FormulaContent("=A1 * 2");
        assertEquals(6, fc.getValue(sheet));
    }

    @Test
    void testAsString() {
        FormulaContent fc = new FormulaContent("=A1+B1");
        assertEquals("=A1+B1", fc.asString());
    }
}
