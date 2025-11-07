import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Spreadsheet {
    private Map<Coordinate, Cell> spreadsheet;

    public Spreadsheet() {
        spreadsheet = new HashMap<>();
    }

    public Map<Coordinate, Cell> loadSpreadsheetFromFile(){
        return spreadsheet;
    }

    public void storeSpreadsheetInFile(){

    }

    public void setCellContent(String coordStr, String rawContent) {
        Coordinate coord = Coordinate.fromString(coordStr);
        CellContent content;

        if (rawContent.startsWith("="))
            content = new FormulaContent(rawContent);
        else if (isNumeric(rawContent))
            content = new NumericContent(Double.parseDouble(rawContent));
        else
            content = new TextContent(rawContent);

        spreadsheet.put(coord, new Cell(coord, content));
    }

    public void getCellContent() {

    }

    private boolean isNumeric(String s) {
        if (s == null || s.isEmpty()) return false;
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public Double getCellValue(String coordStr) {
        Coordinate coord = Coordinate.fromString(coordStr);
        Cell cell = spreadsheet.get(coord);
        if (cell == null) throw new IllegalArgumentException("Cell not found: " + coordStr);
        return cell.getValue(this);
    }

    public void setCellValue(String coordStr) {

    }

}
