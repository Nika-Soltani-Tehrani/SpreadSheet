public class FormulaContent extends CellContent {
    private final String formulaText;

    public FormulaContent(String formulaText) {
        this.formulaText = formulaText;
    }

    @Override
    public Double getValue(Spreadsheet spreadsheet) {
        Formula formula = new Formula(formulaText);
        return formula.evaluate(spreadsheet);
    }

    @Override
    public String asString() {
        return formulaText;
    }
}
