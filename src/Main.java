import java.util.Map;

public class Main {

    public void printSpreadsheetWithValues(Spreadsheet spreadsheet) {
        for (Map.Entry<Coordinate, Cell> entry : spreadsheet.getSpreadsheet().entrySet()) {
            Coordinate coord = entry.getKey();
            Cell cell = entry.getValue();
            try {
                Double value = cell.getValue(spreadsheet);
                System.out.println(coord + " = " + value);
            } catch (Exception e) {
                System.out.println(coord + " = ERROR (" + e.getMessage() + ")");
            }
        }
    }


    public static void main(String[] args) {
//        Spreadsheet sheet = new Spreadsheet();
//
//        sheet.setCellContent("A1", "=C1+C2");
//        sheet.setCellContent("B1", "4");
//        sheet.setCellContent("C1", "1");
//        sheet.setCellContent("C2", "2");
//        sheet.setCellContent("B3", "TOTAL");
//        sheet.setCellContent("C3", "=A1+B1");
//
//        sheet.storeSpreadsheetInFile("spreadsheet.s2v");

        Spreadsheet sheet = new Spreadsheet();
        sheet.loadSpreadsheetFromFile("spreadsheet.s2v");

        for (Map.Entry<Coordinate, Cell> entry : sheet.getSpreadsheet().entrySet()) {
            System.out.println(entry);
            Coordinate coord = entry.getKey();
            Cell cell = entry.getValue();
            System.out.println(coord + " = " + cell.getContent().asString());
        }
    }
}
