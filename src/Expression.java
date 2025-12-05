import java.util.Set;

// Component class in composite design pattern
public interface Expression {
    double getValue(Spreadsheet sheet);
    Set<Coordinate> getReferencedCells();
}
