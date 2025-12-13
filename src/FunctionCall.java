import java.util.*;

public class FunctionCall implements Expression {

    public enum FuncType { SUMA, MIN, MAX, AVERAGE }

    private final FuncType type;
    private final List<Expression> args;

    public FunctionCall(FuncType type, List<Expression> args) {
        this.type = type;
        this.args = args;
    }

    @Override
    public double getValue(Spreadsheet sheet) {
        List<Double> values = new ArrayList<>();

        for (Expression expr : args) {
            double val = expr.getValue(sheet);
            values.add(val);
        }

        double result;
        switch (type) {
            case SUMA:
                result = values.stream().mapToDouble(Double::doubleValue).sum();
                break;
            case MIN:
                result = values.stream().mapToDouble(Double::doubleValue).min().orElse(0);
                break;
            case MAX:
                result = values.stream().mapToDouble(Double::doubleValue).max().orElse(0);
                break;
            case AVERAGE:
                result = values.stream().mapToDouble(Double::doubleValue).average().orElse(0);
                break;
            default:
                throw new IllegalStateException("Unknown function type: " + type);
        }
        return result;
    }

    @Override
    public Set<Coordinate> getReferencedCells() {
        Set<Coordinate> refs = new HashSet<>();
        for (Expression expr : args) {
            refs.addAll(expr.getReferencedCells());
        }
        return refs;
    }

    @Override
    public String toString() {
        return type + args.toString();
    }
}
