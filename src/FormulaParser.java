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

        // Must be at least a cell reference like A1
        if (!firstToken.matches("[A-Z]+[0-9]+")) {
            throw new IllegalArgumentException("Invalid token in formula: " + firstToken);
        }

        // ------- CHECK IF THIS IS A RANGE "A1:B5" -------
        if (pos < expr.length() && expr.charAt(pos) == ':') {
            pos++; // skip ':'

            // parse second coordinate
            int start2 = pos;
            while (pos < expr.length() &&
                    (Character.isLetterOrDigit(expr.charAt(pos)))) {
                pos++;
            }
            String secondToken = expr.substring(start2, pos);

            if (!secondToken.matches("[A-Z]+[0-9]+")) {
                throw new IllegalArgumentException("Invalid range end: " + secondToken);
            }

            Coordinate c1 = Coordinate.fromString(firstToken);
            Coordinate c2 = Coordinate.fromString(secondToken);

            return new RangeReference(c1, c2);
        }

        // ------- NORMAL CELL REFERENCE -------
        return new CellReference(firstToken);
    }
}
