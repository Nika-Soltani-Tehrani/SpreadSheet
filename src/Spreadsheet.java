import java.util.HashMap;
import java.util.Map;

public class Spreadsheet {

    private final Map<Coordinate, Cell> spreadsheet = new HashMap<>();

    public Map<Coordinate, Cell> getSpreadsheet() {
        return spreadsheet;
    }

    public void setCellContent(String coordStr, String rawContent) {
        Coordinate coord = Coordinate.fromString(coordStr);
        CellContent content;

        if (rawContent.startsWith("=")) {
            content = new FormulaContent(rawContent);
        } else if (isNumeric(rawContent)) {
            content = new NumericContent(Double.parseDouble(rawContent));
        } else {
            content = new TextContent(rawContent);
        }

        spreadsheet.put(coord, new Cell(coord, content));
    }

    public void deleteCell(String coordStr) {
        Coordinate coord = Coordinate.fromString(coordStr);
        spreadsheet.remove(coord);
    }


    public Double getCellValue(String coordStr) {
        Coordinate coord = Coordinate.fromString(coordStr);
        Cell cell = spreadsheet.get(coord);
        if (cell == null) {
            throw new IllegalArgumentException("Cell not found: " + coordStr);
        }
        return cell.getValue(this);
    }

    public boolean isNumeric(String s) {
        if (s == null || s.isEmpty()) return false;
        try { Double.parseDouble(s); return true; }
        catch (NumberFormatException e) { return false; }
    }

    // Helpers for column <-> index
    public static int toIndex(String col) {
        int result = 0;
        for (char ch : col.toCharArray()) {
            result = result * 26 + (ch - 'A' + 1);
        }
        return result;
    }

    public static String fromIndex(int index) {
        StringBuilder sb = new StringBuilder();
        while (index > 0) {
            index--;
            sb.insert(0, (char)('A' + (index % 26)));
            index /= 26;
        }
        return sb.toString();
    }
}
