import java.util.*;
import java.util.regex.*;

public class FormulaHandler {
    private final String expression;

    public FormulaHandler(String expression) {
        if (expression.startsWith("="))
            this.expression = expression.substring(1).trim();
        else
            this.expression = expression.trim();
    }

    public double evaluate(Spreadsheet spreadsheet) {
        // Step 1: Replace functions like SUMA(A1:A3)
        String replaced = handleFunctions(expression, spreadsheet);

        // Step 2: Replace cell references (A1, B2, etc.) with their numeric values
        replaced = replaceCellReferences(replaced, spreadsheet);

        // Step 3: Evaluate simple arithmetic expression
        return evalBasicMath(replaced);
    }

    private String handleFunctions(String expr, Spreadsheet spreadsheet) {
        Pattern pattern = Pattern.compile("SUMA\\(([^)]+)\\)");
        Matcher matcher = pattern.matcher(expr);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String inside = matcher.group(1).trim();
            double sum = 0.0;

            // Handle range like A1:A3
            if (inside.contains(":")) {
                String[] parts = inside.split(":");
                Coordinate start = Coordinate.fromString(parts[0]);
                Coordinate end = Coordinate.fromString(parts[1]);
                for (Coordinate coord : spreadsheet.getRange(start, end)) {
                    sum += spreadsheet.getCellValue(coord);
                }
            } else { // individual values or cells separated by ';'
                String[] parts = inside.split(";");
                for (String part : parts) {
                    part = part.trim();
                    if (part.matches("[A-Z]+[0-9]+")) {
                        sum += spreadsheet.getCellValue(Coordinate.fromString(part));
                    } else {
                        sum += Double.parseDouble(part);
                    }
                }
            }
            matcher.appendReplacement(sb, String.valueOf(sum));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private String replaceCellReferences(String expr, Spreadsheet spreadsheet) {
        Pattern refPattern = Pattern.compile("([A-Z]+[0-9]+)");
        Matcher matcher = refPattern.matcher(expr);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String ref = matcher.group(1);
            double value = spreadsheet.getCellValue(Coordinate.fromString(ref));
            matcher.appendReplacement(sb, String.valueOf(value));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private double evalBasicMath(String expr) {
        // VERY simple: use built-in ScriptEngine for math
        try {
            return (double) new javax.script.ScriptEngineManager()
                    .getEngineByName("JavaScript")
                    .eval(expr);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid formula expression: " + expr, e);
        }
    }
}
