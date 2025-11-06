public class Cell {
    private final Coordinate coordinate;
    private CellContent content;

    public Cell(Coordinate coordinate, CellContent content) {
        this.coordinate = coordinate;
        this.content = content;
    }

    public Coordinate getCoordinate() { return coordinate; }

    public CellContent getContent() { return content; }

    public void setContent(CellContent content) {
        this.content = content;
    }

    public Double getValue(Spreadsheet spreadsheet) {
        return content.getValue(spreadsheet);
    }
}
