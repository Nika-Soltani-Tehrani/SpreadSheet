import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Spreadsheet {
    private Map<Coordinate, Cell> spreadsheet;

    public Spreadsheet() {
        spreadsheet = new HashMap<>();
    }

    public Map<Coordinate, Cell> getSpreadsheet() {
        return spreadsheet;
    }

    public void storeSpreadsheetInFile(String filePath) {
        // Step 1: Determine sheet boundaries
        int maxRow = 0;
        int maxCol = 0;

        for (Coordinate coord : spreadsheet.keySet()) {
            maxRow = Math.max(maxRow, coord.getRow());
            maxCol = Math.max(maxCol, toIndex(coord.getColumn()));
        }

        File file = new File(filePath);

        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {

                // Step 2: Iterate row by row
                for (int r = 1; r <= maxRow; r++) {
                    StringBuilder line = new StringBuilder();

                    for (int c = 1; c <= maxCol; c++) {
                        Coordinate coord = new Coordinate(fromIndex(c), r);
                        Cell cell = spreadsheet.get(coord);

                        if (cell != null) {
                            String content = cell.getContent().asString();
                            // replace ';' with ',' inside formulas/functions
                            content = content.replace(";", ",");
                            line.append(content);
                        }

                        // append ';' between cells (not after the last one)
                        if (c < maxCol) {
                            line.append(';');
                        }
                    }

                    writer.write(line.toString());
                    writer.newLine();
                }

                System.out.println("Spreadsheet stored successfully in S2V format at: " + file.getAbsolutePath());
            }

        } catch (IOException e) {
            System.err.println("Error while storing spreadsheet: " + e.getMessage());
        }
    }

    private int toIndex(String col) {
        int result = 0;
        for (char ch : col.toCharArray()) {
            result = result * 26 + (ch - 'A' + 1);
        }
        return result;
    }

    private String fromIndex(int index) {
        StringBuilder sb = new StringBuilder();
        while (index > 0) {
            index--;
            sb.insert(0, (char) ('A' + (index % 26)));
            index /= 26;
        }
        return sb.toString();
    }

    public Map<Coordinate, Cell> loadSpreadsheetFromFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            System.err.println("Error: file not found at " + filePath);
            return spreadsheet;
        }

        spreadsheet.clear(); // reset existing data

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int currentRow = 1;

            while ((line = reader.readLine()) != null) {
                // Split the line by ';' to get cell contents for this row
                String[] cellContents = line.split(";", -1); // -1 keeps empty cells
                int currentCol = 1;

                for (String rawContent : cellContents) {
                    // Replace ',' back with ';' inside formulas/functions
                    rawContent = rawContent.replace(",", ";").trim();

                    if (!rawContent.isEmpty()) {
                        // Determine cell coordinate (A, B, C...)
                        String colName = fromIndex(currentCol);
                        Coordinate coord = new Coordinate(colName, currentRow);

                        // Choose content type
                        CellContent content;
                        if (rawContent.startsWith("=")) {
                            content = new FormulaContent(rawContent);
                        } else if (isNumeric(rawContent)) {
                            content = new NumericContent(Double.parseDouble(rawContent));
                        } else {
                            content = new TextContent(rawContent);
                        }

                        // Store cell in map
                        spreadsheet.put(coord, new Cell(coord, content));
                    }

                    currentCol++;
                }

                currentRow++;
            }

            System.out.println("Spreadsheet loaded successfully from: " + file.getAbsolutePath());

        } catch (IOException e) {
            System.err.println("Error while loading spreadsheet: " + e.getMessage());
        }

        return spreadsheet;
    }

    public void setCellContent(String coordStr, String rawContent) {
        Coordinate coord = Coordinate.fromString(coordStr);
        CellContent content;

        if (rawContent.startsWith("="))
            content = new FormulaContent(rawContent);
        else if (isNumeric(rawContent))
            content = new NumericContent(Double.parseDouble(rawContent));
        else
            content = new TextContent(rawContent);

        spreadsheet.put(coord, new Cell(coord, content));
    }

    public void getCellContent() {

    }

    private boolean isNumeric(String s) {
        if (s == null || s.isEmpty()) return false;
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public Double getCellValue(String coordStr) {
        Coordinate coord = Coordinate.fromString(coordStr);
        Cell cell = spreadsheet.get(coord);
        if (cell == null) throw new IllegalArgumentException("Cell not found: " + coordStr);
        return cell.getValue(this);
    }

    public void setCellValue(String coordStr) {

    }

}
