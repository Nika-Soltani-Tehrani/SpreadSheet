import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class SpreadsheetStorageTest {

    @Test
    void testSaveAndLoadS2V() throws IOException {
        // --- 1. Create a temporary file ---
        File tempFile = File.createTempFile("sheet_test", ".s2v");
        tempFile.deleteOnExit();

        SpreadsheetStorage storage = new SpreadsheetStorage();

        // --- 2. Create a spreadsheet with different cell types ---
        Spreadsheet original = new Spreadsheet();
        original.setCellContent("A1", "10");
        original.setCellContent("B1", "=A1+5");
        original.setCellContent("C1", "Hello");
        original.setCellContent("A2", "123.45");
        original.setCellContent("B2", "=A2*2");
        original.setCellContent("C2", "");

        // --- 3. Save spreadsheet ---
        storage.save(tempFile.getAbsolutePath(), original);

        // --- 4. Load spreadsheet into a new instance ---
        Spreadsheet loaded = storage.load(tempFile.getAbsolutePath());

        // --- 5. Verify all loaded cells exist and match originals ---

        assertEquals("10.0",
                loaded.getSpreadsheet().get(new Coordinate("A", 1)).getContent().asString());

        assertEquals("=A1+5",
                loaded.getSpreadsheet().get(new Coordinate("B", 1)).getContent().asString());

        assertEquals("Hello",
                loaded.getSpreadsheet().get(new Coordinate("C", 1)).getContent().asString());

        assertEquals("123.45",
                loaded.getSpreadsheet().get(new Coordinate("A", 2)).getContent().asString());

        assertEquals("=A2*2",
                loaded.getSpreadsheet().get(new Coordinate("B", 2)).getContent().asString());

        // empty cell: should not be stored in map
        assertFalse(loaded.getSpreadsheet().containsKey(new Coordinate("C", 2)));
    }
}
