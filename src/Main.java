import java.util.Map;

public class Main {

    public static void printSpreadsheet(Spreadsheet sheet) {
        for (Map.Entry<Coordinate, Cell> entry : sheet.getSpreadsheet().entrySet()) {
            System.out.println(entry.getKey() + " → " + entry.getValue().getContent().asString());
        }
    }

    public static void main(String[] args) {

        SpreadsheetStorage storage = new SpreadsheetStorage();

        // Load
        Spreadsheet sheet = storage.load("spreadsheet.s2v");
        System.out.println("\n=== Loaded Spreadsheet ===");
        printSpreadsheet(sheet);

        // Modify
<<<<<<< Updated upstream
        sheet.setCellContent("A1", "=C1+B2");
        sheet.setCellContent("B1", "3");

=======
        sheet.setCellContent("C1", "1");
        sheet.setCellContent("A2", "2");
        sheet.setCellContent("A1", "=C1+B1*A2");
        sheet.setCellContent("B1", "3");
        sheet.setCellContent("C2", "5");
        sheet.setCellContent("C2", "=A1");
        System.out.println(sheet.getCellValue("A1"));
        System.out.println(sheet.getCellValue("C2"));
>>>>>>> Stashed changes
        // Save
        storage.save("spreadsheet.s2v", sheet);

        System.out.println("\n=== After Modification ===");
        printSpreadsheet(sheet);
    }
}
