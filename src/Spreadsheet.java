import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Spreadsheet {
    private Map<Coordinate, Cell> spreadsheet;

    public Spreadsheet() {
        spreadsheet = new HashMap<>();
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

    public String getCellAsString(String coordStr) {
        Coordinate coord = Coordinate.fromString(coordStr);
        Cell cell = spreadsheet.get(coord);
        if (cell == null) throw new IllegalArgumentException("Cell not found: " + coordStr);
        return cell.getContent().asString();
    }

    // to be implemented later
    public double evaluateFormula(String formula) {
        // use FormulaParser later
        return 0.0;
    }

    public List<Coordinate> getRange(Coordinate start, Coordinate end) {
        List<Coordinate> coords = new ArrayList<>();
        int startCol = toIndex(start.getColumn());
        int endCol = toIndex(end.getColumn());
        int startRow = start.getRow();
        int endRow = end.getRow();

        for (int c = startCol; c <= endCol; c++) {
            for (int r = startRow; r <= endRow; r++) {
                coords.add(new Coordinate(fromIndex(c), r));
            }
        }
        return coords;
    }

    // Example conversion methods
    private int toIndex(String col) {
        int result = 0;
        for (char ch : col.toCharArray()) {
            result = result * 26 + (ch - 'A' + 1);
        }
        return result;
    }

    private String fromIndex(int index) {
        StringBuilder sb = new StringBuilder();
        while (index > 0) {
            index--;
            sb.insert(0, (char) ('A' + (index % 26)));
            index /= 26;
        }
        return sb.toString();
    }



}
