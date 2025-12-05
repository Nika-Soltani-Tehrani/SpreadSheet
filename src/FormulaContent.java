import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class FormulaContent extends CellContent {

    private final String rawFormula;
    private final Expression expression;
    private boolean isValid = false;
    private Double cachedValue = null;
    private Set<Coordinate> dependencies;

    public FormulaContent(String rawFormula) {
        this.rawFormula = rawFormula;
        this.expression = new FormulaParser().parse(rawFormula);
        this.dependencies = new HashSet<>(expression.getReferencedCells());
    }

    @Override
    public Double getValue(Spreadsheet sheet) {
        if (!isValid) {
            cachedValue = expression.getValue(sheet);
            isValid = true;
        }
        return cachedValue;
    }


    @Override
    public String asString() {
        return rawFormula;
    }

    public Set<Coordinate> getDependencies() {
        return dependencies;
    }

    /** Mark this formula as invalid (needs recomputation) */
    public synchronized void invalidate() {
        this.isValid = false;
        this.cachedValue = null;
    }
}
