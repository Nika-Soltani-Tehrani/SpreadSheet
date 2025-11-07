public class FormulaContent extends CellContent {
    private final String formulaText;

    public FormulaContent(String formulaText) {
        this.formulaText = formulaText;
    }

    @Override
    public Double getValue(Spreadsheet spreadsheet) {
        FormulaHandler formula = new FormulaHandler(formulaText);
        return formula.evaluate(spreadsheet);
    }

    @Override
    public String asString() {
        return formulaText;
    }
}
