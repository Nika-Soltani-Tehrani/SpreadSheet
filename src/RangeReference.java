import java.util.*;

public class RangeReference implements Expression {

    private final Coordinate start;
    private final Coordinate end;

    public RangeReference(Coordinate start, Coordinate end) {
        this.start = start;
        this.end = end;
    }

    private Iterable<Coordinate> cells() {
        return () -> new RangeIterator(start, end);
    }

    @Override
    public double getValue(Spreadsheet sheet) {
        double sum = 0.0;
        for (Coordinate c : cells()) {
            sum += sheet.getCellValue(c.toString());
        }
        return sum;
    }

    @Override
    public Set<Coordinate> getReferencedCells() {
        Set<Coordinate> refs = new HashSet<>();
        for (Coordinate c : cells()) {
            refs.add(c);
        }
        return refs;
    }

    @Override
    public String toString() {
        return start + ":" + end;
    }
}
