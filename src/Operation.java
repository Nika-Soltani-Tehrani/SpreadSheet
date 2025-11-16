// Composite class in composite design pattern
public class Operation implements Expression {

    public enum Operator {
        ADD, SUBTRACT, MULTIPLY, DIVIDE
    }

    private final Operator operator;
    private final Expression left;
    private final Expression right;

    public Operation(Expression left, Operator operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public double getValue(Spreadsheet sheet) {
        double l = left.getValue(sheet);
        double r = right.getValue(sheet);

        switch (operator) {
            case ADD: return l + r;
            case SUBTRACT: return l - r;
            case MULTIPLY: return l * r;
            case DIVIDE:
                if (r == 0) throw new ArithmeticException("Division by zero");
                return l / r;
        }
        throw new IllegalStateException("Unknown operator");
    }
}
