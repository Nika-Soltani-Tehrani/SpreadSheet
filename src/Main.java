import java.util.Map;

public class Main {

    public static void printSpreadsheetWithValues(Spreadsheet spreadsheet) {
        for (Map.Entry<Coordinate, Cell> entry : spreadsheet.getSpreadsheet().entrySet()) {
            Coordinate coord = entry.getKey();
            Cell cell = entry.getValue();
            System.out.println(coord + " = " + cell.getContent().asString());
        }
    }


    public static void main(String[] args) {
        Spreadsheet sheet = new Spreadsheet();
        sheet.loadSpreadsheetFromFile("spreadsheet.s2v");
        printSpreadsheetWithValues(sheet);
//
        sheet.setCellContent("A1", "=C1+B2");
        sheet.setCellContent("B1", "3");
//        sheet.setCellContent("C1", "1");
//        sheet.setCellContent("C2", "2");
//        sheet.setCellContent("B3", "TOTAL");
//        sheet.setCellContent("C3", "=A1+B1");
//
        sheet.storeSpreadsheetInFile("spreadsheet.s2v");
        System.out.println("Spreadsheet after modification");
        printSpreadsheetWithValues(sheet);
    }
}
