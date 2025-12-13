import java.util.Iterator;
import java.util.NoSuchElementException;

public class RangeIterator implements Iterator<Coordinate> {

    private final int colMin, colMax;
    private final int rowMin, rowMax;

    private int currentCol;
    private int currentRow;

    public RangeIterator(Coordinate start, Coordinate end) {
        int startCol = Spreadsheet.toIndex(start.getColumn());
        int endCol   = Spreadsheet.toIndex(end.getColumn());
        int startRow = start.getRow();
        int endRow   = end.getRow();

        this.colMin = Math.min(startCol, endCol);
        this.colMax = Math.max(startCol, endCol);
        this.rowMin = Math.min(startRow, endRow);
        this.rowMax = Math.max(startRow, endRow);

        this.currentCol = colMin;
        this.currentRow = rowMin;
    }

    @Override
    public boolean hasNext() {
        return currentCol <= colMax && currentRow <= rowMax;
    }

    @Override
    public Coordinate next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        Coordinate coord = new Coordinate(
                Spreadsheet.fromIndex(currentCol),
                currentRow
        );

        // advance position
        currentRow++;
        if (currentRow > rowMax) {
            currentRow = rowMin;
            currentCol++;
        }

        return coord;
    }
}
