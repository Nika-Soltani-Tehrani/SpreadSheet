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
}
