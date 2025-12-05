import java.util.Collections;
import java.util.Set;

// Leaf 1 in composite design pattern
public class ConstantNumber implements Expression {
    private final double value;

    public ConstantNumber(double value) {
        this.value = value;
    }

    @Override
    public double getValue(Spreadsheet sheet) {
        return value;
    }

    @Override
    public Set<Coordinate> getReferencedCells() {
        return Collections.emptySet();
    }

}
