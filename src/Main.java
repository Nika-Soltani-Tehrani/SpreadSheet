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
        /*
        // Modify
        sheet.setCellContent("C1", "1");
        sheet.setCellContent("A2", "2");
        sheet.setCellContent("A1", "=C1+B1*A2");
        sheet.setCellContent("B1", "3");
        sheet.setCellContent("C2", "5");
        sheet.setCellContent("C2", "=A1");
        System.out.println(sheet.getCellValue("A1"));
        System.out.println(sheet.getCellValue("C2"));
        */

        /*
        //Test Dependency management
        //sheet.setCellContent("A1", "=B1 + B2+B3");
        //System.out.println("The value of A1 before assignment is: " + sheet.getCellValue("A1"));
        sheet.setCellContent("B1", "1");
        sheet.setCellContent("B2", "3");
        sheet.setCellContent("B3", "5");

        sheet.setCellContent("A1", "=B1 + B2+B3");
        System.out.println("The value of A1 before assignment is: " + sheet.getCellValue("A1"));

        sheet.setCellContent("C1", "=A1 + 1");
        sheet.setCellContent("B2", "5");
        sheet.setCellContent("C2", "=A1");
        System.out.println("The value of C2 before assignment is: " + sheet.getCellValue("C2"));
        */
        /*
        // Test cyclic detection mechanism
        sheet.setCellContent("B1", "1");
        sheet.setCellContent("C1", "5");
        System.out.println("before adding A1");
        sheet.setCellContent("A1", "=B1 + 1");
        System.out.println("before adding B1");
        sheet.setCellContent("B1", "=C1 + 1");
        sheet.setCellContent("C1", "=A1 + 1"); // Now a cycle
        */
        // Test ranges
        /*
        sheet.setCellContent("D2", "=B1:B3");
        System.out.println("The value of D2 is: " + sheet.getCellValue("D2"));
        */

        // Test
        sheet.setCellContent("D1", "=SUMA(B1:B3, 3, C1)");
        System.out.println("The value of D1 assignment is: " + sheet.getCellValue("D1"));
        sheet.setCellContent("D2", "=MIN(1, 2, 3)");
        System.out.println("The value of D2 assignment is: " + sheet.getCellValue("D2"));
        sheet.setCellContent("D3", "=AVERAGE(A1, (B1 + C1) * 2)");
        System.out.println("The value of D3 assignment is: " + sheet.getCellValue("D3"));
        sheet.setCellContent("D4", "=MAX(A1, MAX(B1, C1))");
        System.out.println("The value of D4 assignment is: " + sheet.getCellValue("D4"));
        // Save

        storage.save("spreadsheet.s2v", sheet);

        System.out.println("\n=== After Modification ===");
        printSpreadsheet(sheet);
    }
}
