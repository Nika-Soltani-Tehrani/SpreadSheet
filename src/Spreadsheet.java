import java.util.HashMap;
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


}
