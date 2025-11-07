public class Main {
    public static void main(String[] args) {
        Spreadsheet sheet = new Spreadsheet();

        // Fill some cells
        sheet.setCellContent("A1", "1");
        sheet.setCellContent("A2", "2");
        sheet.setCellContent("A3", "3");
        sheet.setCellContent("B1", "4");

        // Test simple formula
        sheet.setCellContent("C1", "=A1 + B1 * 2");
        System.out.println("C1 = " + sheet.getCellValue("C1")); // expected 1 + 4*2 = 9

        // Test SUMA function
        sheet.setCellContent("C2", "=SUMA(A1:A3)");
        System.out.println("C2 = " + sheet.getCellValue("C2")); // expected 6

        // Test combined
        sheet.setCellContent("C3", "=SUMA(A1:A3) + B1");
        System.out.println("C3 = " + sheet.getCellValue("C3")); // expected 6 + 4 = 10
    }
}
