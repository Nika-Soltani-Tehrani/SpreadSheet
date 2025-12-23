import java.io.*;
import java.util.Map;

public class SpreadsheetStorage {

    public void save(String path, Spreadsheet sheet) throws IOException {
        Map<Coordinate, Cell> data = sheet.getSpreadsheet();

        int maxRow = 0;
        int maxCol = 0;

        for (Coordinate coord : data.keySet()) {
            maxRow = Math.max(maxRow, coord.getRow());
            maxCol = Math.max(maxCol, Spreadsheet.toIndex(coord.getColumn()));
        }

        File file = new File(path);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {

            for (int r = 1; r <= maxRow; r++) {
                StringBuilder line = new StringBuilder();

                for (int c = 1; c <= maxCol; c++) {
                    Coordinate coord = new Coordinate(Spreadsheet.fromIndex(c), r);
                    Cell cell = data.get(coord);

                    if (cell != null) {
                        String content = cell.getContent().asString();
                        content = content.replace(";", "\\"); // required by S2V spec
                        line.append(content);
                    }

                    if (c < maxCol) {
                        line.append(';');
                    }
                }

                writer.write(line.toString());
                writer.newLine();
            }
        }
    }

    public Spreadsheet load(String path) throws IOException {
        File file = new File(path);

        if (!file.exists()) {
            throw new FileNotFoundException("Spreadsheet file not found: " + path);
        }

        Spreadsheet sheet = new Spreadsheet();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            String line;
            int currentRow = 1;

            while ((line = reader.readLine()) != null) {
                String[] cellContents = line.split(";", -1);
                int currentCol = 1;

                for (String raw : cellContents) {
                    raw = raw.replace("\\", ";").trim();

                    if (!raw.isEmpty()) {
                        String colName = Spreadsheet.fromIndex(currentCol);
                        Coordinate coord = new Coordinate(colName, currentRow);

                        if (raw.startsWith("=")) {
                            sheet.getSpreadsheet().put(coord, new Cell(coord, new FormulaContent(raw)));
                        } else if (sheet.isNumeric(raw)) {
                            sheet.getSpreadsheet().put(coord, new Cell(coord, new NumericContent(Double.parseDouble(raw))));
                        } else {
                            sheet.getSpreadsheet().put(coord, new Cell(coord, new TextContent(raw)));
                        }
                    }
                    currentCol++;
                }
                currentRow++;
            }
        }

        sheet.rebuildDependencies();
        return sheet;
    }
}
