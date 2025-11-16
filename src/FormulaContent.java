public class FormulaContent extends CellContent {

    private final String rawFormula;
    private final Expression expression;

    public FormulaContent(String rawFormula) {
        this.rawFormula = rawFormula;
        this.expression = new FormulaParser().parse(rawFormula);
    }

    @Override
    public Double getValue(Spreadsheet sheet) {
        return expression.getValue(sheet);
    }

    @Override
    public String asString() {
        return rawFormula;
    }
}
