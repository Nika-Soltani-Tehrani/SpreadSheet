public class Coordinate {
    private final String column;
    private final int row;

    public Coordinate(String column, int row) {
        this.column = column.toUpperCase();
        this.row = row;
    }

    /*
     This function retrieves column and row from a string identifier
     */
    public static Coordinate fromString(String s) {
        if (s == null) {
            throw new IllegalArgumentException("Coordinate string cannot be null");
        }

        String trimmed = s.trim().toUpperCase();

        // Validate format strictly: one or more letters followed by one or more digits
        java.util.regex.Matcher matcher = java.util.regex.Pattern
                .compile("^([A-Z]+)([0-9]+)$")
                .matcher(trimmed);

        if (!matcher.matches()) {
            // Model-layer: only throw exception, no System.out
            throw new IllegalArgumentException("Invalid coordinate format: " + s);
        }

        String colPart = matcher.group(1);
        String rowPart = matcher.group(2);

        int row;
        try {
            row = Integer.parseInt(rowPart);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid numeric row in coordinate: " + s, e);
        }

        // Spreadsheet rule: rows start at 1
        if (row < 1) {
            throw new IllegalArgumentException("Row number must be >= 1 in coordinate: " + s);
        }

        // Construct a valid coordinate (column validated implicitly by regex)
        return new Coordinate(colPart, row);
    }


    public String toString() {
        return column + row;
    }

    public String getColumn() { return column; }
    public int getRow() { return row; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Coordinate)) return false;
        Coordinate other = (Coordinate) obj;
        return this.column.equals(other.column) && this.row == other.row;
    }

    @Override
    public int hashCode() {
        return column.hashCode() * 31 + row;
    }
}
