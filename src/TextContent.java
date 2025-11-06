public class TextContent extends CellContent {
    private final String text;

    public TextContent(String text) {
        this.text = text;
    }

    @Override
    public Double getValue(Spreadsheet spreadsheet) {
        // Rule from project: empty string → 0, numeric string → number, else error
        if (text.isEmpty()) return 0.0;
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Cannot convert text to number: " + text);
        }
    }

    @Override
    public String asString() {
        return text;
    }
}
