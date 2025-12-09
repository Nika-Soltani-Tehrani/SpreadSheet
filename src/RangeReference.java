import java.util.*;

public class RangeReference implements Expression {

    private final Coordinate start;
    private final Coordinate end;

    public RangeReference(Coordinate start, Coordinate end) {
        this.start = start;
        this.end = end;
    }

    /** Expand the rectangular range into all coordinates inside it */
    private List<Coordinate> expand() {
        List<Coordinate> coords = new ArrayList<>();

        int startCol = Spreadsheet.toIndex(start.getColumn());
        int endCol   = Spreadsheet.toIndex(end.getColumn());
        int startRow = start.getRow();
        int endRow   = end.getRow();

        int colMin = Math.min(startCol, endCol);
        int colMax = Math.max(startCol, endCol);
        int rowMin = Math.min(startRow, endRow);
        int rowMax = Math.max(startRow, endRow);

        for (int col = colMin; col <= colMax; col++) {
            for (int row = rowMin; row <= rowMax; row++) {
                coords.add(new Coordinate(
                        Spreadsheet.fromIndex(col),  // convert back
                        row
                ));
            }
        }

        return coords;
    }

    @Override
    public double getValue(Spreadsheet sheet) {
        double sum = 0.0;
        for (Coordinate c : expand()) {
            sum += sheet.getCellValue(c.toString());
        }
        return sum;
    }

    @Override
    public Set<Coordinate> getReferencedCells() {
        return new HashSet<>(expand());
    }

    @Override
    public String toString() {
        return start + ":" + end;
    }
}
