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

    // factor = number | cellReference | (expression)
    private Expression parseFactor() {
        if (expr.charAt(pos) == '(') {
            pos++;
            Expression inside = parseExpression();
            pos++; // skip ')'
            return inside;
        }

        // number or cell reference
        int start = pos;
        while (pos < expr.length() &&
                (Character.isLetterOrDigit(expr.charAt(pos)))) {
            pos++;
        }
        String token = expr.substring(start, pos);

        if (token.matches("[A-Z]+[0-9]+")) {
            return new CellReference(token);
        } else {
            return new ConstantNumber(Double.parseDouble(token));
        }
    }
}
