public class NumericContent extends CellContent {
    private final double value;

    public NumericContent(double value) {
        this.value = value;
    }

    @Override
    public Double getValue(Spreadsheet spreadsheet) {
        return value;
    }

    @Override
    public String asString() {
        return String.valueOf(value);
    }
}
