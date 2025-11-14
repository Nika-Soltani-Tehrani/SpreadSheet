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
        sheet.setCellContent("A1", "=C1+B2");
        sheet.setCellContent("B1", "3");

        // Save
        storage.save("spreadsheet.s2v", sheet);

        System.out.println("\n=== After Modification ===");
        printSpreadsheet(sheet);
    }
}
