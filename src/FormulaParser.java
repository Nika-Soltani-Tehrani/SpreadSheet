import java.util.ArrayList;
import java.util.List;

public class FormulaParser {

    private String expr;
    private int pos;

    public Expression parse(String formula) {
        // remove '=' if exists
        if (formula.startsWith("="))
            formula = formula.substring(1);

        this.expr = formula.replace(" ", "");
        this.pos = 0;

        return parseExpression();
    }

    // grammar:
    // expression = term | expression + term | expression - term
    private Expression parseExpression() {
        Expression left = parseTerm();
        while (pos < expr.length()) {
            char op = expr.charAt(pos);
            // STOP if end of argument or function
            if (op == ',' || op == ')') {
                break;
            }
            if (op == '+' || op == '-') {
                pos++;
                Expression right = parseTerm();
                left = new Operation(
                        left,
                        op == '+' ? Operation.Operator.ADD : Operation.Operator.SUBTRACT,
                        right
                );
            } else {
                break;
            }
        }
        return left;
    }

    // term = factor | term * factor | term / factor
    private Expression parseTerm() {
        Expression left = parseFactor();
        while (pos < expr.length()) {
            char op = expr.charAt(pos);
            if (op == ',' || op == ')') {
                break;
            }
            if (op == '*' || op == '/') {
                pos++;
                Expression right = parseFactor();
                left = new Operation(
                        left,
                        op == '*' ? Operation.Operator.MULTIPLY : Operation.Operator.DIVIDE,
                        right
                );
            } else {
                break;
            }
        }
        return left;
    }

    private Expression parseFunctionCall(String name) {
        pos++; // skip '('

        // Convert the name into function type
        FunctionCall.FuncType functionType;
        try {
            functionType = FunctionCall.FuncType.valueOf(name);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown function: " + name);
        }

        List<Expression> arguments = new ArrayList<>();

        // parse arguments until ')'
        while (pos < expr.length() && expr.charAt(pos) != ')') {
            Expression arg = parseExpression();
            arguments.add(arg);

            if (expr.charAt(pos) == ',') {
                pos++; // skip comma
            }
        }
        pos++; // skip ')'

        return new FunctionCall(functionType, arguments);
    }

    /**
     * factor = number | cellRef | range | (expression)
     * range = cellRef ':' cellRef
     */
    private Expression parseFactor() {

        // Parentheses
        if (expr.charAt(pos) == '(') {
            pos++;
            Expression inside = parseExpression();
            pos++; // skip ')'
            return inside;
        }

        int start = pos;
        while (pos < expr.length() &&
                (Character.isLetterOrDigit(expr.charAt(pos)))) {
            pos++;
        }
        String firstToken = expr.substring(start, pos);

        // Number?
        if (firstToken.matches("[0-9]+(\\.[0-9]+)?")) {
            return new ConstantNumber(Double.parseDouble(firstToken));
        }

        // FUNCTION CALL? Example: SUMA(
        if (firstToken.matches("[A-Z]+") && pos < expr.length() && expr.charAt(pos) == '(') {
            return parseFunctionCall(firstToken);
        }

        // Not a function — must be at least A1 format
        if (!firstToken.matches("[A-Z]+[0-9]+")) {
            throw new IllegalArgumentException("Invalid token in formula: " + firstToken);
        }

        // Range A1:B5 ?
        if (pos < expr.length() && expr.charAt(pos) == ':') {
            pos++; // skip ':'

            // parse second coordinate
            int start2 = pos;
            while (pos < expr.length() && Character.isLetterOrDigit(expr.charAt(pos))) pos++;
            String secondToken = expr.substring(start2, pos);

            Coordinate c1 = Coordinate.fromString(firstToken);
            Coordinate c2 = Coordinate.fromString(secondToken);

            return new RangeReference(c1, c2);
        }

        // Regular cell reference
        return new CellReference(firstToken);
    }
}
