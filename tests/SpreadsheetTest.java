import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpreadsheetTest {

    @Test
    void testNumericCell() {
        Spreadsheet sheet = new Spreadsheet();
        sheet.setCellContent("A1", "10");

        assertEquals(10, sheet.getCellValue("A1"));
    }

    @Test
    void testFormulaCell() {
        Spreadsheet sheet = new Spreadsheet();
        sheet.setCellContent("A1", "10");
        sheet.setCellContent("B1", "5");
        sheet.setCellContent("C1", "=A1 + B1");

        assertEquals(15, sheet.getCellValue("C1"));
    }

    @Test
    void testUnknownCellThrows() {
        Spreadsheet sheet = new Spreadsheet();

        assertThrows(IllegalArgumentException.class, () -> sheet.getCellValue("A1"));
    }

    @Test
    void testTextContentConversion() {
        Spreadsheet sheet = new Spreadsheet();
        sheet.setCellContent("A1", "123");

        assertEquals(123, sheet.getCellValue("A1"));
    }
}
