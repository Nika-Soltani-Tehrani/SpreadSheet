// Leaf 2 in composite design pattern
public class CellReference implements Expression {
    private final Coordinate coordinate;

    public CellReference(String coordStr) {
        this.coordinate = Coordinate.fromString(coordStr);
    }

    @Override
    public double getValue(Spreadsheet sheet) {
        return sheet.getCellValue(coordinate.toString());
    }
}
